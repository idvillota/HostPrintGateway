package com.host.printgateway.data

import android.util.Xml
import com.host.printgateway.network.AddOrderLineRequest
import com.host.printgateway.network.CatalogApi
import com.host.printgateway.network.SalesOrderApi
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

    suspend fun syncPendingOrders(
        baseUrl: String,
    ): Result<Int> = runCatching {
        val api = SalesOrderApi(baseUrl, deviceToken)

        var synced = 0

        for (order in dao.getOrdersPendingSync()) {

            val tableId = order.diningTableId
                ?: error(
                    "La orden ${order.id} no tiene mesa"
                )

            val remoteId = order.remoteId
                ?: api.createOpenOrder(tableId)
                    .getOrThrow()
                    .also {
                        dao.saveRemoteOrderId(
                            order.id,
                            it,
                        )
                    }

            val lines = dao.getOrderItems(order.id).map {
                AddOrderLineRequest(
                    it.productId,
                    it.quantity,
                    it.notes,
                )
            }

            api.confirmOrder(
                remoteId,
                lines,
            ).getOrThrow()

            dao.markOrderSynced(
                order.id,
                remoteId,
            )

            synced++
        }

        synced
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