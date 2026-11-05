package com.host.printgateway.data

import com.host.printgateway.network.AddOrderLineRequest
import com.host.printgateway.network.CatalogApi
import com.host.printgateway.network.MobileSyncApi
import com.host.printgateway.network.PendingOrderUpload
import com.host.printgateway.network.RelocateOrderResult
import com.host.printgateway.network.RemoteSale
import com.host.printgateway.network.SalesOrderApi
import java.util.Locale
import java.util.UUID

class SyncRepository(
    private val database: PrintGatewayDatabase,
    private val deviceToken: String,
) {
    private val dao = database.restaurantDao()

    suspend fun closeOrdersOnFreeHostTables() {
        if (dao.getOrders().any { SyncMarkers.parseMove(it.syncStatus) != null }) return
        val freeTableIds = dao.getTables().filterNot { it.isOccupied() }.map { it.id }.toSet()
        val staleIds = dao.getUnpaidOrders()
            .filter { order ->
                SyncMarkers.parseMove(order.syncStatus) == null &&
                    (order.diningTableId == null || order.diningTableId in freeTableIds)
            }
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
        val queuedMoves = dao.getOrders().mapNotNull { order ->
            val move = SyncMarkers.parseMove(order.syncStatus) ?: return@mapNotNull null
            PendingTableMove(
                orderId = order.id,
                fromTableId = move.fromTableId,
                toTableId = move.toTableId,
                at = move.at,
                merge = move.merge,
            )
        }
        if (pending.isEmpty() && queuedMoves.isEmpty()) {
            healOccupiedTables(baseUrl, deviceId, errors)
            if (errors.isNotEmpty()) error(errors.distinct().joinToString(" "))
            return@runCatching 0
        }

        val paymentOrders = pending.filter { SyncMarkers.isPayment(it.syncStatus) }
        val stoppedTables = mutableSetOf<String>()
        var synced = 0
        val charges = PendingSync.chargesByTable(
            paymentOrders.map { order ->
                PendingPayment(
                    orderId = order.id,
                    tableKey = PendingSync.tableKey(order.diningTableId, order.id),
                    closedAt = order.closedAtUtc ?: order.createdAt,
                )
            },
        ).flatMap { (tableKey, tableCharges) ->
            tableCharges.map { group ->
                PendingCharge(
                    tableKey = tableKey,
                    closedAt = group.first().closedAt,
                    orderIds = group.map { it.orderId },
                )
            }
        }
        for (event in PendingSync.syncTimeline(charges, queuedMoves)) {
            when (event) {
                is SyncEvent.Charge -> {
                    if (event.tableKey in stoppedTables) continue
                    val group = event.orderIds.mapNotNull { dao.getOrder(it) }
                    if (group.isEmpty()) continue
                    val settled = settleOfflinePayment(baseUrl, deviceId, group, errors)
                    synced += settled.synced
                    if (!settled.cleared) {
                        stoppedTables += event.tableKey
                    }
                }
                is SyncEvent.Move -> {
                    if (!replayQueuedMove(baseUrl, event.move, errors)) {
                        stoppedTables += event.move.fromTableId
                        stoppedTables += event.move.toTableId
                    } else {
                        synced++
                    }
                }
            }
        }

        val lineOrders = dao.getOrdersPendingSync().filter { order ->
            !SyncMarkers.isPayment(order.syncStatus) &&
                !SyncMarkers.isStandaloneMove(order.syncStatus)
        }
        val readyLines = lineOrders.filter {
            PendingSync.isReadyToUpload(it.diningTableId, it.id, stoppedTables)
        }
        if (readyLines.size < lineOrders.size) {
            errors += "Hay comandas nuevas en espera hasta que HOST cierre el cobro de su mesa."
        }
        synced += uploadOrderLines(baseUrl, deviceId, readyLines, preservePayment = false, errors = errors)

        healOccupiedTables(baseUrl, deviceId, errors)
        if (errors.isNotEmpty()) error(errors.distinct().joinToString(" "))
        synced
    }

    private suspend fun replayQueuedMove(
        baseUrl: String,
        move: PendingTableMove,
        errors: MutableList<String>,
    ): Boolean {
        val order = dao.getOrder(move.orderId) ?: return true
        val remoteId = order.remoteId?.takeIf { it.isNotBlank() }
        if (remoteId == null) {
            clearMoveMarker(order)
            return true
        }
        val relocated = SalesOrderApi(baseUrl, deviceToken)
            .relocate(remoteId, move.toTableId, move.merge)
            .getOrElse { failure ->
                errors += failure.message ?: "No se pudo cambiar la mesa."
                return false
            }
        if (relocated.action.equals(RelocateOrderResult.MERGE_REQUIRED, ignoreCase = true) ||
            relocated.action.equals(RelocateOrderResult.CANCELLED, ignoreCase = true)
        ) {
            errors += relocated.message.ifBlank { "No se pudo cambiar la mesa." }
            return false
        }
        val targetRemoteId = relocated.targetOrderId.ifBlank { remoteId }
        if (relocated.lines.isNotEmpty()) {
            replaceHostLines(
                RemoteSale(
                    remoteOrderId = targetRemoteId,
                    tableId = move.toTableId,
                    tableCode = relocated.targetTableCode,
                    waiterName = order.waiterName,
                    cursor = "",
                    lines = relocated.lines,
                ),
            )
        }
        clearMoveMarker(dao.getOrder(order.id) ?: order)
        return true
    }

    private suspend fun clearMoveMarker(order: OrderEntity) {
        val leftover = SyncMarkers.withoutMove(order.syncStatus)
        val next = when {
            leftover.startsWith(SyncMarkers.PAYMENT_PREFIX) -> leftover
            leftover.isNotBlank() -> leftover
            order.status == RestaurantStatuses.ORDER_PAID -> RestaurantStatuses.SYNC_STATE_SYNCED
            dao.getOrderItems(order.id).any { !it.id.startsWith(SyncMarkers.REMOTE_ITEM_PREFIX) } ->
                RestaurantStatuses.SYNC_STATE_PENDING
            else -> RestaurantStatuses.SYNC_STATE_SYNCED
        }
        dao.updateSyncStatus(order.id, next)
    }

    private suspend fun settleOfflinePayment(
        baseUrl: String,
        deviceId: String,
        group: List<OrderEntity>,
        errors: MutableList<String>,
    ): OfflinePaymentResult {
        val errorsBefore = errors.size
        uploadOrderLines(baseUrl, deviceId, group, preservePayment = true, errors = errors)
        if (errors.size > errorsBefore) return OfflinePaymentResult(synced = 0, cleared = false)

        val current = group.map { dao.getOrder(it.id) ?: it }
        val remoteIds = current.mapNotNull { it.remoteId?.takeIf(String::isNotBlank) }
        if (remoteIds.size != current.size) {
            errors += "La comanda de la mesa ${group.first().diningTableCode} todavía no está en HOST."
            return OfflinePaymentResult(synced = 0, cleared = false)
        }
        val payment = SyncMarkers.parsePayment(group.first().syncStatus)
        val paid = MobileSyncApi(baseUrl, deviceToken).uploadPayment(
            deviceId = deviceId,
            remoteIds = remoteIds,
            paymentMethod = payment.method,
            tipAmount = payment.tip,
            printReceipt = payment.printReceipt,
        ).getOrThrow()
        if (!paid.synced) {
            errors += paid.error.ifBlank { "HOST rechazó el cobro." }
            return OfflinePaymentResult(synced = 0, cleared = false)
        }
        if (!paid.tablesAvailable) {
            errors += "La mesa ${group.first().diningTableCode} sigue ocupada en HOST."
            return OfflinePaymentResult(synced = 0, cleared = false)
        }
        current.forEach { order ->
            dao.markOrderSynced(order.id, order.remoteId.orEmpty())
        }
        LocalAccountActions.freeSettledTables(dao, current.mapNotNull { it.diningTableId })
        return OfflinePaymentResult(synced = current.size, cleared = true)
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
        val ready = mutableListOf<PendingOrderUpload>()
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
                ready += PendingOrderUpload(
                    order = order,
                    lines = localLines,
                    kitchenTicketPrinted = dao.getTicketForOrder(order.id)?.isPrinted == true,
                )
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
        upsertHostSale(sale, replaceRemoteLines = false)
    }

    suspend fun replaceHostLines(sale: RemoteSale) {
        upsertHostSale(sale, replaceRemoteLines = true)
    }

    private suspend fun upsertHostSale(sale: RemoteSale, replaceRemoteLines: Boolean) {
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

        val currentItems = dao.getOrderItems(orderId)
        val unsynced = if (replaceRemoteLines) {
            currentItems.filter { !it.id.startsWith(SyncMarkers.REMOTE_ITEM_PREFIX) }
        } else {
            emptyList()
        }
        if (replaceRemoteLines) {
            dao.deleteOrderItems(orderId)
            if (unsynced.isNotEmpty()) {
                dao.insertOrderItems(unsynced)
            }
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

private data class OfflinePaymentResult(val synced: Int, val cleared: Boolean)
