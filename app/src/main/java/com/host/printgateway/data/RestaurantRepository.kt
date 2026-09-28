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
    }

    suspend fun syncPendingOrders(
        baseUrl: String,
        deviceId: String,
    ): Result<Int> = runCatching {
        val pending = dao.getOrdersPendingSync()
        if (pending.isEmpty()) return@runCatching 0

        val ready = mutableListOf<Pair<OrderEntity, List<AddOrderLineRequest>>>()
        pending.forEach { order ->
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
                dao.markOrderSynced(order.id, order.remoteId)
            } else if (localLines.isNotEmpty()) {
                ready += order to localLines
            }
        }
        if (ready.isEmpty()) return@runCatching 0

        val api = MobileSyncApi(baseUrl, deviceToken)
        val response = api.uploadBatch(
            deviceId = deviceId,
            orders = ready,
        ).getOrThrow()

        var synced = 0
        response.forEach { result ->
            if (result.synced && result.remoteId.isNotBlank()) {
                dao.markOrderSynced(result.localId, result.remoteId)
                synced++
            } else if (!result.synced) {
                dao.markOrderSyncFailed(result.localId)
            }
        }
        synced
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