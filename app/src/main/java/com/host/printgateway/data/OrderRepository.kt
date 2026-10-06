package com.host.printgateway.data

import java.util.Locale
import java.util.UUID

data class OrderDraftLine(
    val productId: String,
    val productName: String,
    val quantity: Double,
    val unitPrice: Double,
    val notes: String = "",
    val excludedIngredients: List<String> = emptyList(),
)

class OrderRepository(
    private val database: PrintGatewayDatabase,
) {
    private val dao = database.restaurantDao()

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
        val orderNumber = orderId.take(8).uppercase(Locale.US)
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
            subtotal = lines.sumOf { it.quantity * it.unitPrice },
            taxAmount = 0.0,
            total = lines.sumOf { it.quantity * it.unitPrice },
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

        val printer = dao.getPrinters().firstOrNull()
        val payload = KitchenTicketXmlBuilder.build(
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

    suspend fun markPaidForSync(orderIds: List<String>, paymentMethod: String, tip: Double) {
        val method = paymentMethod.replace("|", " ").ifBlank { "Efectivo" }
        val marker = SyncMarkers.PAYMENT_PREFIX + method + "|" + String.format(Locale.US, "%.2f", tip)
        val tableIds = orderIds.mapNotNull { dao.getOrder(it)?.diningTableId }
        dao.markOrdersPaid(orderIds, System.currentTimeMillis(), marker)
        LocalAccountActions.freeSettledTables(dao, tableIds)
    }
}
