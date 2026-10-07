package com.host.printgateway.data

import com.host.printgateway.network.AddOrderLineRequest
import com.host.printgateway.network.CatalogApi
import com.host.printgateway.network.MobileSyncApi
import com.host.printgateway.network.RemoteSale
import java.util.Locale
import java.util.UUID

class SyncRepository(
    private val database: PrintGatewayDatabase,
    private val deviceToken: String,
) {
    private val dao = database.restaurantDao()

    suspend fun closeOrdersOnFreeHostTables() {
        val freeTableIds = dao.getTables().filterNot { it.isOccupied() }.map { it.id }.toSet()
        val staleIds = dao.getUnpaidOrders()
            .filter { order -> order.diningTableId == null || order.diningTableId in freeTableIds }
            .map { it.id }
        if (staleIds.isEmpty()) return
        dao.markOrdersPaid(staleIds, System.currentTimeMillis(), RestaurantStatuses.SYNC_STATE_SYNCED)
    }

    suspend fun releaseTablesSettledOnHost(baseUrl: String): Result<Boolean> = runCatching {
        val summaries = CatalogApi(baseUrl, deviceToken).fetchTableAccounts().getOrThrow()
        LocalAccountActions.closeAccountsOnFreeTables(dao, TableSettle.freeHostTableIds(summaries))
    }

    suspend fun releaseNotifiedTables(tableIds: List<String>): Boolean =
        LocalAccountActions.closeAccountsOnFreeTables(dao, tableIds.map { it.lowercase() }.toSet())

    suspend fun syncPendingOrders(
        baseUrl: String,
        deviceId: String,
    ): Result<Int> = runCatching {
        val pending = dao.getOrdersPendingSync()
        val errors = mutableListOf<String>()
        if (pending.isEmpty()) {
            healOccupiedTables(baseUrl, deviceId, errors)
            if (errors.isNotEmpty()) error(errors.distinct().joinToString(" "))
            return@runCatching 0
        }

        val paymentOrders = pending.filter { it.syncStatus.startsWith(SyncMarkers.PAYMENT_PREFIX) }
        val lineOrders = pending.filterNot { it.syncStatus.startsWith(SyncMarkers.PAYMENT_PREFIX) }
        var synced = uploadOrderLines(baseUrl, deviceId, lineOrders, preservePayment = false, errors = errors)

        paymentOrders.groupBy { it.diningTableId ?: it.id }.values.forEach { group ->
            val errorsBefore = errors.size
            uploadOrderLines(baseUrl, deviceId, group, preservePayment = true, errors = errors)
            if (errors.size > errorsBefore) return@forEach
            val current = group.map { dao.getOrder(it.id) ?: it }
            val remoteIds = current.mapNotNull { it.remoteId?.takeIf(String::isNotBlank) }
            if (remoteIds.size != current.size) {
                errors += "La comanda de la mesa ${group.first().diningTableCode} todavía no está en HOST."
                return@forEach
            }
            val marker = group.first().syncStatus.removePrefix(SyncMarkers.PAYMENT_PREFIX).split("|")
            val method = marker.dropLast(1).joinToString("|").ifBlank { "Efectivo" }
            val tip = marker.lastOrNull()?.toDoubleOrNull() ?: 0.0
            val paid = MobileSyncApi(baseUrl, deviceToken).uploadPayment(
                deviceId = deviceId,
                remoteIds = remoteIds,
                paymentMethod = method,
                tipAmount = tip,
            ).getOrThrow()
            if (paid.synced) {
                current.forEach { order ->
                    dao.markOrderSynced(order.id, order.remoteId.orEmpty())
                }
                if (paid.tablesAvailable) {
                    LocalAccountActions.freeSettledTables(dao, current.mapNotNull { it.diningTableId })
                } else {
                    errors += "La mesa ${group.first().diningTableCode} sigue ocupada en HOST."
                }
                synced += current.size
            } else {
                errors += paid.error.ifBlank { "HOST rechazó el cobro." }
            }
        }

        healOccupiedTables(baseUrl, deviceId, errors)
        if (errors.isNotEmpty()) error(errors.distinct().joinToString(" "))
        synced
    }

    private suspend fun healOccupiedTables(
        baseUrl: String,
        deviceId: String,
        errors: MutableList<String>,
    ) {
        val unpaidTableIds = dao.getUnpaidOrders().mapNotNull { it.diningTableId }.toSet()
        val stuck = dao.getTables().filter { it.isOccupied() && it.id !in unpaidTableIds }
        if (stuck.isEmpty()) return
        val paidByTable = dao.getOrders()
            .filter {
                it.status == RestaurantStatuses.ORDER_PAID &&
                    it.syncStatus == RestaurantStatuses.SYNC_STATE_SYNCED &&
                    !it.remoteId.isNullOrBlank()
            }
            .groupBy { it.diningTableId }
        val api = MobileSyncApi(baseUrl, deviceToken)
        stuck.forEach { table ->
            val remoteIds = paidByTable[table.id].orEmpty().mapNotNull { it.remoteId }.distinct()
            if (remoteIds.isEmpty()) return@forEach
            val paid = runCatching {
                api.uploadPayment(
                    deviceId = deviceId,
                    remoteIds = remoteIds,
                    paymentMethod = "Efectivo",
                    tipAmount = 0.0,
                ).getOrThrow()
            }
            paid.onSuccess { result ->
                if (result.synced && result.tablesAvailable) {
                    dao.updateTableStatus(table.id, "0")
                } else if (!result.synced && result.error.isNotBlank()) {
                    errors += "Mesa ${table.code}: ${result.error}"
                }
            }.onFailure { failure ->
                errors += "Mesa ${table.code}: ${failure.message ?: "No se pudo liberar la mesa."}"
            }
        }
    }

    private suspend fun uploadOrderLines(
        baseUrl: String,
        deviceId: String,
        orders: List<OrderEntity>,
        preservePayment: Boolean,
        errors: MutableList<String>,
    ): Int {
        if (orders.isEmpty()) return 0
        val ready = mutableListOf<Pair<OrderEntity, List<AddOrderLineRequest>>>()
        var synced = 0
        orders.forEach { order ->
            val localLines = dao.getOrderItems(order.id)
                .filter { !it.id.startsWith(SyncMarkers.REMOTE_ITEM_PREFIX) }
                .map {
                    AddOrderLineRequest(
                        productId = it.productId,
                        quantity = it.quantity,
                        notes = it.notes,
                    )
                }
            if (localLines.isEmpty() && !order.remoteId.isNullOrBlank()) {
                if (!preservePayment) {
                    dao.markOrderSynced(order.id, order.remoteId)
                    synced++
                }
            } else if (localLines.isNotEmpty()) {
                ready += order to localLines
            }
        }
        if (ready.isEmpty()) return synced

        val response = MobileSyncApi(baseUrl, deviceToken)
            .uploadBatch(deviceId, ready)
            .getOrThrow()
        val errorCountBefore = errors.size
        response.forEach { result ->
            if (result.synced && result.remoteId.isNotBlank()) {
                if (preservePayment) {
                    dao.saveRemoteOrderId(result.localId, result.remoteId)
                } else {
                    dao.markOrderSynced(result.localId, result.remoteId)
                    synced++
                }
            } else if (!result.synced && !(preservePayment && result.error.contains("ya está cerrado"))) {
                if (!preservePayment) dao.markOrderSyncFailed(result.localId)
                errors += result.error.ifBlank { "HOST rechazó la comanda." }
            }
        }
        if (preservePayment && errors.size > errorCountBefore) return synced
        return synced
    }

    suspend fun catchUpOpenAccounts(
        baseUrl: String,
        deviceId: String,
        since: String,
    ): Result<String> = runCatching {
        val cursor = pullMissedSales(baseUrl, deviceId, since).getOrThrow()
        if (!hasOccupiedTableWithoutBill()) {
            return@runCatching cursor.ifBlank { since }
        }
        val full = pullMissedSales(baseUrl, deviceId, "").getOrThrow()
        full.ifBlank { cursor.ifBlank { since } }
    }

    private suspend fun hasOccupiedTableWithoutBill(): Boolean {
        val billedTableIds = dao.getUnpaidOrders().mapNotNull { it.diningTableId }.toSet()
        return dao.getTables().any { table -> table.isOccupied() && table.id !in billedTableIds }
    }

    suspend fun pullMissedSales(
        baseUrl: String,
        deviceId: String,
        since: String,
    ): Result<String> = runCatching {
        val page = MobileSyncApi(baseUrl, deviceToken)
            .fetchPendingSales(deviceId, since)
            .getOrThrow()
        page.sales.forEach { applyRemoteSale(it) }
        page.cursor.ifBlank { since }
    }

    suspend fun applyRemoteSale(sale: RemoteSale) {
        if (sale.remoteOrderId.isBlank()) return
        val existing = dao.getOrderByRemoteId(sale.remoteOrderId)
            ?: sale.tableId.takeIf { it.isNotBlank() }?.let { dao.getOpenLocalOrderForTable(it) }
        val orderId = existing?.id ?: UUID.randomUUID().toString()
        if (existing == null) {
            val subtotal = sale.lines.sumOf { it.quantity * it.unitPrice }
            dao.insertOrder(
                OrderEntity(
                    id = orderId,
                    diningTableId = sale.tableId.ifBlank { null },
                    diningTableCode = sale.tableCode.ifBlank { "—" },
                    number = sale.remoteOrderId.take(8).uppercase(Locale.US),
                    customerId = null,
                    waiterName = sale.waiterName.ifBlank { "HOST" },
                    deviceId = "host",
                    status = RestaurantStatuses.ORDER_SYNCED,
                    openedAtUtc = System.currentTimeMillis(),
                    subtotal = subtotal,
                    taxAmount = 0.0,
                    total = subtotal,
                    remoteId = sale.remoteOrderId,
                    syncStatus = RestaurantStatuses.SYNC_STATE_SYNCED,
                ),
            )
        } else if (existing.remoteId.isNullOrBlank()) {
            dao.saveRemoteOrderId(existing.id, sale.remoteOrderId)
        }

        val knownIds = dao.getOrderItems(orderId).map { it.id }.toSet()
        val freshItems = sale.lines.mapNotNull { line ->
            val sourceId = line.lineId.ifBlank {
                "${sale.remoteOrderId}:${line.productId}:${line.notes}"
            }
            val itemId = SyncMarkers.REMOTE_ITEM_PREFIX + sourceId
            if (itemId in knownIds) {
                null
            } else {
                OrderItemEntity(
                    id = itemId,
                    orderId = orderId,
                    productId = line.productId,
                    productName = line.productName.ifBlank { "Producto" },
                    quantity = line.quantity,
                    unitPrice = line.unitPrice,
                    lineTotal = line.quantity * line.unitPrice,
                    unitCostPrice = null,
                    notes = line.notes,
                    sentToKitchenAtUtc = null,
                )
            }
        }
        if (freshItems.isNotEmpty()) {
            dao.insertOrderItems(freshItems)
        }
        val items = dao.getOrderItems(orderId)
        val total = items.sumOf { it.lineTotal }
        dao.updateOrderTotals(orderId, total, total)
    }
}
