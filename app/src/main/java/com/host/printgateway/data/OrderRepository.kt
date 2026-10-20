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
    val productTypeId: String = "",
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

    suspend fun markPaidForSync(
        orderIds: List<String>,
        paymentMethod: String,
        tip: Double,
        printReceipt: Boolean = false,
    ) {
        val marker = SyncMarkers.paymentMarker(paymentMethod, tip, printReceipt)
        val closedAt = System.currentTimeMillis()
        val tableIds = mutableListOf<String>()
        orderIds.forEach { id ->
            val order = dao.getOrder(id) ?: return@forEach
            order.diningTableId?.let { tableIds += it }
            dao.markOrdersPaid(
                listOf(id),
                closedAt,
                SyncMarkers.attachingPayment(marker, order.syncStatus),
            )
        }
        LocalAccountActions.freeSettledTables(dao, tableIds)
    }

    suspend fun moveOpenOrders(
        fromTableId: String,
        toTableId: String,
        merge: Boolean,
    ): MoveTableOutcome {
        if (fromTableId == toTableId) {
            return MoveTableOutcome.Rejected("La mesa de destino debe ser distinta.")
        }
        val tables = dao.getTables()
        val destination = tables.firstOrNull { it.id == toTableId }
            ?: return MoveTableOutcome.Rejected("La mesa de destino no existe.")
        if (!destination.isActive) {
            return MoveTableOutcome.Rejected("La mesa de destino no está activa.")
        }
        val sourceOrders = dao.getUnpaidOrders().filter { it.diningTableId == fromTableId }
        if (sourceOrders.isEmpty()) {
            return MoveTableOutcome.Rejected("Esta mesa no tiene un pedido abierto.")
        }
        val targetOrders = dao.getUnpaidOrders().filter { it.diningTableId == toTableId }
        if (targetOrders.isEmpty() && destination.isOccupied()) {
            return MoveTableOutcome.Rejected(
                "La mesa ${destination.code} está ocupada, pero su pedido no está en el celular.",
            )
        }
        if (targetOrders.isNotEmpty() && !merge) {
            return MoveTableOutcome.MergeRequired(destination.code)
        }

        val now = System.currentTimeMillis()
        if (targetOrders.isEmpty()) {
            sourceOrders.forEach { order ->
                placeOnTable(order, fromTableId, destination, merge = false, now = now)
            }
        } else {
            var survivor = targetOrders.first()
            targetOrders.drop(1).forEach { extra ->
                survivor = combine(extra, survivor, destination, now)
            }
            sourceOrders.forEach { source ->
                survivor = combine(source, survivor, destination, now)
            }
        }
        LocalAccountActions.freeSettledTables(dao, listOf(fromTableId))
        dao.updateTableStatus(destination.id, "1")
        return MoveTableOutcome.Moved(destination.code)
    }

    private suspend fun placeOnTable(
        order: OrderEntity,
        fromTableId: String,
        destination: DiningTableEntity,
        merge: Boolean,
        now: Long,
    ) {
        dao.updateOrderPlacement(
            order.id,
            destination.id,
            destination.code,
            moveStatus(order, fromTableId, destination.id, merge, now),
        )
    }

    private suspend fun combine(
        source: OrderEntity,
        target: OrderEntity,
        destination: DiningTableEntity,
        now: Long,
    ): OrderEntity {
        val sourceRemote = !source.remoteId.isNullOrBlank()
        val targetRemote = !target.remoteId.isNullOrBlank()
        when {
            sourceRemote && targetRemote -> {
                dao.reassignOrderItems(source.id, target.id)
                refreshTotals(target.id)
                val fromId = source.diningTableId ?: destination.id
                dao.shelveMovedOrder(
                    source.id,
                    moveStatus(source, fromId, destination.id, merge = true, now = now),
                    now,
                )
            }
            sourceRemote -> {
                dao.reassignOrderItems(target.id, source.id)
                refreshTotals(source.id)
                val fromId = source.diningTableId ?: destination.id
                dao.updateOrderPlacement(
                    source.id,
                    destination.id,
                    destination.code,
                    moveStatus(source, fromId, destination.id, merge = false, now = now),
                )
                dao.deleteLocalOnlyOrder(target.id)
                keepLocalLinesPending(source.id)
                return dao.getOrder(source.id) ?: source
            }
            else -> {
                dao.reassignOrderItems(source.id, target.id)
                refreshTotals(target.id)
                dao.deleteLocalOnlyOrder(source.id)
            }
        }
        if (target.diningTableId != destination.id) {
            placeOnTable(target, target.diningTableId ?: destination.id, destination, merge = false, now)
        }
        keepLocalLinesPending(target.id)
        return dao.getOrder(target.id) ?: target
    }

    private suspend fun keepLocalLinesPending(orderId: String) {
        val order = dao.getOrder(orderId) ?: return
        if (SyncMarkers.isPayment(order.syncStatus) || SyncMarkers.parseMove(order.syncStatus) != null) return
        val hasLocalLines = dao.getOrderItems(orderId)
            .any { !it.id.startsWith(SyncMarkers.REMOTE_ITEM_PREFIX) }
        if (hasLocalLines && order.syncStatus == RestaurantStatuses.SYNC_STATE_SYNCED) {
            dao.updateSyncStatus(orderId, RestaurantStatuses.SYNC_STATE_PENDING)
        }
    }

    private suspend fun refreshTotals(orderId: String) {
        val total = dao.getOrderItems(orderId).sumOf { it.lineTotal }
        dao.updateOrderTotals(orderId, total, total)
    }

    private fun moveStatus(
        order: OrderEntity,
        fromTableId: String,
        toTableId: String,
        merge: Boolean,
        now: Long,
    ): String {
        val previous = SyncMarkers.parseMove(order.syncStatus)
        val marker = SyncMarkers.moveMarker(
            fromTableId = previous?.fromTableId ?: fromTableId,
            toTableId = toTableId,
            at = previous?.at ?: now,
            merge = merge || previous?.merge == true,
        )
        return if (SyncMarkers.isPayment(order.syncStatus)) {
            SyncMarkers.attachingPayment(SyncMarkers.withoutMove(order.syncStatus), marker)
        } else {
            marker
        }
    }
}

sealed interface MoveTableOutcome {
    data class Moved(val tableCode: String) : MoveTableOutcome
    data class MergeRequired(val tableCode: String) : MoveTableOutcome
    data class Rejected(val message: String) : MoveTableOutcome
}
