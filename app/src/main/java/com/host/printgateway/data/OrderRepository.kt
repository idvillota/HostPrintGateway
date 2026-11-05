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

    suspend fun sourceRemoteId(fromTableId: String): String? =
        dao.getUnpaidOrders()
            .filter { it.diningTableId == fromTableId }
            .mapNotNull { it.remoteId?.takeIf(String::isNotBlank) }
            .firstOrNull()
            ?: dao.getRemoteOrderIdForTable(fromTableId)

    suspend fun validateRelocate(
        fromTableId: String,
        toTableId: String,
        merge: Boolean?,
    ): MoveTableOutcome {
        if (fromTableId == toTableId) {
            return MoveTableOutcome.Rejected("La mesa de destino debe ser distinta.")
        }
        val destination = dao.getTables().firstOrNull { it.id == toTableId }
            ?: return MoveTableOutcome.Rejected("La mesa de destino no existe.")
        if (!destination.isActive) {
            return MoveTableOutcome.Rejected("La mesa de destino no está activa.")
        }
        val sourceOrders = dao.getUnpaidOrders().filter { it.diningTableId == fromTableId }
        if (sourceOrders.isEmpty()) {
            return MoveTableOutcome.Rejected("Esta mesa no tiene un pedido abierto.")
        }
        val targetOrders = dao.getUnpaidOrders().filter { it.diningTableId == toTableId }
        if (targetOrders.isEmpty() && destination.isOccupied() && merge != true) {
            return MoveTableOutcome.Rejected(
                "La mesa ${destination.code} está ocupada, pero su pedido no está en el celular.",
            )
        }
        if (targetOrders.isNotEmpty() && merge != true) {
            return if (merge == null) {
                MoveTableOutcome.MergeRequired(destination.code)
            } else {
                MoveTableOutcome.Rejected("Cambio de mesa cancelado.")
            }
        }
        return MoveTableOutcome.Moved(destination.code)
    }

    suspend fun relocateOpenOrders(
        fromTableId: String,
        toTableId: String,
        merge: Boolean?,
        queueHost: Boolean,
        hostMerged: Boolean,
        hostTargetRemoteId: String?,
    ): MoveTableOutcome {
        val blocked = validateRelocate(fromTableId, toTableId, merge)
        if (blocked !is MoveTableOutcome.Moved) return blocked
        val destination = dao.getTables().first { it.id == toTableId }
        val sourceOrders = dao.getUnpaidOrders().filter { it.diningTableId == fromTableId }
        val targetOrders = dao.getUnpaidOrders().filter { it.diningTableId == toTableId }
        val now = System.currentTimeMillis()
        if (targetOrders.isEmpty() || merge != true) {
            sourceOrders.forEach { order ->
                placeOnTable(
                    order = order,
                    destination = destination,
                    now = now,
                    queueHost = queueHost && !order.remoteId.isNullOrBlank(),
                    merge = false,
                    remoteId = hostTargetRemoteId ?: order.remoteId,
                )
            }
        } else {
            var survivor = targetOrders.first()
            targetOrders.drop(1).forEach { extra ->
                survivor = combineInto(extra, survivor, destination, now, queueHost, hostMerged, hostTargetRemoteId)
            }
            sourceOrders.forEach { source ->
                survivor = combineInto(source, survivor, destination, now, queueHost, hostMerged, hostTargetRemoteId)
            }
        }
        LocalAccountActions.freeSettledTables(dao, listOf(fromTableId))
        dao.updateTableStatus(destination.id, "1")
        return MoveTableOutcome.Moved(destination.code)
    }

    private suspend fun placeOnTable(
        order: OrderEntity,
        destination: DiningTableEntity,
        now: Long,
        queueHost: Boolean,
        merge: Boolean,
        remoteId: String?,
    ) {
        val fromId = order.diningTableId ?: destination.id
        dao.updateOrderPlacement(
            order.id,
            destination.id,
            destination.code,
            nextStatus(order, fromId, destination.id, merge, now, queueHost),
            remoteId?.ifBlank { null } ?: order.remoteId,
        )
    }

    private suspend fun combineInto(
        source: OrderEntity,
        target: OrderEntity,
        destination: DiningTableEntity,
        now: Long,
        queueHost: Boolean,
        hostMerged: Boolean,
        hostTargetRemoteId: String?,
    ): OrderEntity {
        val sourceOnHost = !source.remoteId.isNullOrBlank()
        val sourcePendingUpload = source.syncStatus == RestaurantStatuses.SYNC_STATE_PENDING ||
            source.syncStatus == RestaurantStatuses.ORDER_SYNC_PENDING
        val queueThis = queueHost && sourceOnHost
        if (sourceOnHost && !sourcePendingUpload) {
            if (hostMerged) {
                dao.deleteOrderItems(source.id)
            } else {
                adoptAsRemoteDisplay(source.id, target.id)
            }
        } else {
            dao.reassignOrderItems(source.id, target.id)
        }
        refreshTotals(target.id)
        val destRemote = hostTargetRemoteId ?: target.remoteId ?: source.remoteId
        val targetStatus = if (sourcePendingUpload && !hostMerged) {
            RestaurantStatuses.SYNC_STATE_PENDING
        } else if (queueThis) {
            target.syncStatus
        } else {
            nextStatus(target, target.diningTableId ?: destination.id, destination.id, true, now, false)
        }
        if (target.diningTableId != destination.id || destRemote != target.remoteId || target.syncStatus != targetStatus) {
            dao.updateOrderPlacement(
                target.id,
                destination.id,
                destination.code,
                targetStatus,
                destRemote,
            )
        }
        dao.shelveMovedOrder(
            source.id,
            if (queueThis) {
                SyncMarkers.moveMarker(source.diningTableId ?: destination.id, destination.id, now, true)
            } else {
                RestaurantStatuses.SYNC_STATE_SYNCED
            },
            now,
        )
        return dao.getOrder(target.id) ?: target
    }

    private suspend fun adoptAsRemoteDisplay(sourceId: String, targetId: String) {
        val items = dao.getOrderItems(sourceId)
        if (items.isNotEmpty()) {
            dao.insertOrderItems(
                items.map { item ->
                    val alreadyRemote = item.id.startsWith(SyncMarkers.REMOTE_ITEM_PREFIX)
                    item.copy(
                        id = if (alreadyRemote) item.id else SyncMarkers.REMOTE_ITEM_PREFIX + item.id,
                        orderId = targetId,
                    )
                },
            )
        }
        dao.deleteOrderItems(sourceId)
    }

    private suspend fun refreshTotals(orderId: String) {
        val total = dao.getOrderItems(orderId).sumOf { it.lineTotal }
        dao.updateOrderTotals(orderId, total, total)
    }

    private fun nextStatus(
        order: OrderEntity,
        fromTableId: String,
        toTableId: String,
        merge: Boolean,
        now: Long,
        queueHost: Boolean,
    ): String {
        if (!queueHost) {
            return if (SyncMarkers.isPayment(order.syncStatus)) {
                SyncMarkers.withoutMove(order.syncStatus)
            } else {
                order.syncStatus
            }
        }
        val marker = SyncMarkers.moveMarker(fromTableId, toTableId, now, merge)
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
