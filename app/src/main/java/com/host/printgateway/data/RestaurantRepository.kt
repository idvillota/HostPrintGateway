package com.host.printgateway.data

import android.util.Xml
import com.host.printgateway.network.AddOrderLineRequest
import com.host.printgateway.network.CatalogApi
import com.host.printgateway.network.MobileSyncApi
import com.host.printgateway.network.RemoteSale
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

data class OrderDraftLine(
    val productId: String,
    val productName: String,
    val quantity: Double,
    val unitPrice: Double,
    val notes: String = "",
    val excludedIngredients: List<String> = emptyList(),
)

class RestaurantRepository(
    private val database: PrintGatewayDatabase,
    private val deviceToken: String,
) {

    private val dao = database.restaurantDao()

    suspend fun syncCatalog(baseUrl: String): Result<Int> = runCatching {
        val api = CatalogApi(baseUrl, deviceToken)

        val tables = api.fetchTables().getOrThrow()
        val types = api.fetchProductTypes().getOrThrow()
        val productPage = api.fetchProducts().getOrThrow()
        val products = productPage.products
        val ingredients = api.fetchIngredients().getOrThrow()
        val printers = api.fetchPrinters().getOrThrow()

        dao.replaceCatalog(
            tables.map {
                DiningTableEntity(
                    it.id,
                    it.code,
                    it.capacity,
                    it.zone,
                    null,
                    null,
                    it.status,
                    it.isActive,
                )
            },
            types.map {
                ProductTypeEntity(
                    it.id,
                    it.name,
                    it.description,
                    it.sortOrder,
                    it.isActive,
                )
            },
            products.map {
                ProductEntity(
                    it.id,
                    it.productTypeId,
                    it.compositionType,
                    it.name,
                    it.description,
                    it.sku,
                    it.imagePath,
                    it.unitPrice,
                    it.isActive,
                )
            },
            ingredients.map {
                IngredientEntity(
                    it.id,
                    it.categoryId,
                    it.name,
                    it.unit,
                    it.unitCost,
                    it.stockQuantity,
                    it.reorderLevel,
                    it.isActive,
                )
            },
            productPage.productIngredients.map {
                ProductIngredientEntity(
                    it.id,
                    it.productId,
                    it.ingredientId,
                    it.quantity,
                )
            },
            printers.map {
                PrinterStationEntity(
                    it.id,
                    it.name,
                    it.code,
                    "",
                    it.isActive,
                    it.sortOrder,
                )
            },
        )

        tables.size +
            types.size +
            products.size +
            ingredients.size +
            printers.size
    }

    suspend fun createOrder(
        table: DiningTableEntity,
        waiterName: String,
        deviceId: String,
        lines: List<OrderDraftLine>,
    ): String {
        require(lines.isNotEmpty()) {
            "La orden debe tener productos"
        }

        val orderId = UUID.randomUUID().toString()

        val orderNumber = orderId
            .take(8)
            .uppercase(Locale.US)

        val knownRemoteId = dao.getRemoteOrderIdForTable(table.id)

        val order = OrderEntity(
            id = orderId,
            diningTableId = table.id,
            diningTableCode = table.code,
            number = orderNumber,
            customerId = null,
            waiterName = waiterName,
            deviceId = deviceId,
            status = RestaurantStatuses.ORDER_SYNC_PENDING,
            openedAtUtc = System.currentTimeMillis(),
            subtotal = lines.sumOf {
                it.quantity * it.unitPrice
            },
            taxAmount = 0.0,
            total = lines.sumOf {
                it.quantity * it.unitPrice
            },
            remoteId = knownRemoteId,
            syncStatus = RestaurantStatuses.SYNC_STATE_PENDING,
        )

        val items = lines.map { line ->
            OrderItemEntity(
                id = UUID.randomUUID().toString(),
                orderId = orderId,
                productId = line.productId,
                productName = line.productName,
                quantity = line.quantity,
                unitPrice = line.unitPrice,
                lineTotal = line.quantity * line.unitPrice,
                unitCostPrice = null,
                notes = line.notes,
                sentToKitchenAtUtc = null,
            )
        }

        /*
         * getPrinters() es suspend, por lo tanto se ejecuta aquí,
         * dentro de createOrder(), que también es suspend.
         */
        val printer = dao.getPrinters()
            .firstOrNull()

        /*
         * Generamos el XML directamente en la APK.
         *
         * El XML queda almacenado en kitchen_tickets.payload.
         * No dependemos del backend para generar la comanda.
         */
        val payload = buildKitchenTicketXml(
            table = table,
            orderNumber = orderNumber,
            waiterName = waiterName,
            printer = printer,
            lines = lines,
        )

        dao.saveOrderWithTicket(
            order,
            items,
            KitchenTicketEntity(
                id = UUID.randomUUID().toString(),
                orderId = orderId,
                printerStationId = printer?.id,
                payload = payload,
            ),
        )

        return orderId
    }

    private companion object {
        const val REMOTE_ITEM_PREFIX = "remote:"
        const val PAYMENT_PREFIX = "PAY|"
    }

    suspend fun markPaidForSync(orderIds: List<String>, paymentMethod: String, tip: Double) {
        val method = paymentMethod.replace("|", " ").ifBlank { "Efectivo" }
        val marker = PAYMENT_PREFIX + method + "|" + String.format(Locale.US, "%.2f", tip)
        val tableIds = orderIds.mapNotNull { dao.getOrder(it)?.diningTableId }
        dao.markOrdersPaid(orderIds, System.currentTimeMillis(), marker)
        freeSettledTables(tableIds)
    }

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
        val freeIds = summaries
            .filter { it.openOrderId.isNullOrBlank() }
            .map { it.tableId.lowercase() }
            .toSet()
        closeAccountsOnFreeTables(freeIds)
    }

    suspend fun releaseNotifiedTables(tableIds: List<String>): Boolean =
        closeAccountsOnFreeTables(tableIds.map { it.lowercase() }.toSet())

    private suspend fun closeAccountsOnFreeTables(freeIds: Set<String>): Boolean {
        if (freeIds.isEmpty()) return false
        fun isFree(tableId: String?) = tableId != null && tableId.lowercase() in freeIds
        val staleIds = dao.getUnpaidOrders()
            .filter { order -> isFree(order.diningTableId) && !order.remoteId.isNullOrBlank() }
            .map { it.id }
        if (staleIds.isNotEmpty()) {
            dao.markOrdersPaid(staleIds, System.currentTimeMillis(), RestaurantStatuses.SYNC_STATE_SYNCED)
        }
        var statusChanged = false
        dao.getTables().filter { table -> isFree(table.id) && table.isOccupied() }.forEach { table ->
            val stillUnpaid = dao.getUnpaidOrders().any { it.diningTableId.equals(table.id, ignoreCase = true) }
            if (!stillUnpaid) {
                dao.updateTableStatus(table.id, "0")
                statusChanged = true
            }
        }
        return staleIds.isNotEmpty() || statusChanged
    }

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

        val paymentOrders = pending.filter { it.syncStatus.startsWith(PAYMENT_PREFIX) }
        val lineOrders = pending.filterNot { it.syncStatus.startsWith(PAYMENT_PREFIX) }
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
            val marker = group.first().syncStatus.removePrefix(PAYMENT_PREFIX).split("|")
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
                    freeSettledTables(current.mapNotNull { it.diningTableId })
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

    private suspend fun freeSettledTables(tableIds: List<String>) {
        tableIds.distinct().forEach { tableId ->
            val stillOpen = dao.getUnpaidOrders().any { it.diningTableId == tableId }
            if (!stillOpen) dao.updateTableStatus(tableId, "0")
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
                .filter { !it.id.startsWith(REMOTE_ITEM_PREFIX) }
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
            val itemId = REMOTE_ITEM_PREFIX + sourceId
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

    /**
     * Genera el XML de una comanda.
     *
     * El XML se almacena como payload de KitchenTicketEntity.
     *
     * La impresora no recibe directamente este XML.
     * KitchenTicketFormatter lo interpreta y lo convierte
     * posteriormente a ESC/POS.
     */
    private fun buildKitchenTicketXml(
        table: DiningTableEntity,
        orderNumber: String,
        waiterName: String,
        printer: PrinterStationEntity?,
        lines: List<OrderDraftLine>,
    ): String {

        val writer = StringWriter()

        val serializer = Xml.newSerializer()

        serializer.setOutput(writer)

        serializer.startDocument(
            "UTF-8",
            true,
        )

        serializer.startTag(
            "",
            "KitchenTicket",
        )

        writeXmlValue(
            serializer,
            "TableCode",
            table.code,
        )

        writeXmlValue(
            serializer,
            "OrderNumber",
            orderNumber,
        )

        writeXmlValue(
            serializer,
            "SentBy",
            waiterName,
        )

        writeXmlValue(
            serializer,
            "SentAtUtc",
            utcNow(),
        )

        serializer.startTag(
            "",
            "PrinterStation",
        )

        writeXmlValue(
            serializer,
            "Name",
            printer?.name ?: "",
        )

        writeXmlValue(
            serializer,
            "Code",
            printer?.code ?: "",
        )

        serializer.endTag(
            "",
            "PrinterStation",
        )

        serializer.startTag(
            "",
            "Lines",
        )

        for (item in lines) {

            serializer.startTag(
                "",
                "Line",
            )

            writeXmlValue(
                serializer,
                "ProductName",
                item.productName,
            )

            writeXmlValue(
                serializer,
                "Quantity",
                formatQuantity(item.quantity),
            )

            writeXmlValue(
                serializer,
                "Notes",
                item.notes,
            )

            serializer.startTag(
                "",
                "ExcludedIngredients",
            )

            for (ingredient in item.excludedIngredients) {
                writeXmlValue(
                    serializer,
                    "Ingredient",
                    ingredient,
                )
            }

            serializer.endTag(
                "",
                "ExcludedIngredients",
            )

            serializer.endTag(
                "",
                "Line",
            )
        }

        serializer.endTag(
            "",
            "Lines",
        )

        writeXmlValue(
            serializer,
            "IsCancellation",
            "false",
        )

        writeXmlValue(
            serializer,
            "CancelReason",
            "",
        )

        serializer.endTag(
            "",
            "KitchenTicket",
        )

        serializer.endDocument()

        return writer.toString()
    }

    private fun writeXmlValue(
        serializer: org.xmlpull.v1.XmlSerializer,
        tagName: String,
        value: String,
    ) {
        serializer.startTag(
            "",
            tagName,
        )

        serializer.text(value)

        serializer.endTag(
            "",
            tagName,
        )
    }

    private fun formatQuantity(
        quantity: Double,
    ): String {
        return if (quantity % 1.0 == 0.0) {
            quantity.toLong().toString()
        } else {
            quantity.toString()
        }
    }

    private fun utcNow(): String {
        val formatter = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.US,
        )

        formatter.timeZone =
            TimeZone.getTimeZone("UTC")

        return formatter.format(Date())
    }
}