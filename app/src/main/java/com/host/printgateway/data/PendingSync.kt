package com.host.printgateway.data

/**
 * Offline payments for one table are separate HOST charges.
 * Orders paid in the same tap share [PendingPayment.closedAt] and stay together.
 * A table change is an event at the time it happened, before any later charge.
 * A later charge, and any new comanda, wait until the earlier charge has closed the table.
 */
internal object PendingSync {

    fun tableKey(diningTableId: String?, orderId: String): String = diningTableId ?: orderId

    fun isReadyToUpload(
        diningTableId: String?,
        orderId: String,
        unsettledTableKeys: Set<String>,
    ): Boolean = tableKey(diningTableId, orderId) !in unsettledTableKeys

    /**
     * Charges per table, oldest first. Each inner list is one payment.
     */
    fun chargesByTable(payments: List<PendingPayment>): Map<String, List<List<PendingPayment>>> =
        payments
            .groupBy { it.tableKey }
            .mapValues { (_, tablePayments) ->
                tablePayments
                    .groupBy { it.closedAt }
                    .entries
                    .sortedBy { it.key }
                    .map { it.value }
            }

    /**
     * Charges and table changes in the order they happened.
     * A change and a charge with the same time run the change first.
     */
    fun syncTimeline(
        charges: List<PendingCharge>,
        moves: List<PendingTableMove>,
    ): List<SyncEvent> {
        val events = ArrayList<SyncEvent>(charges.size + moves.size)
        charges.forEach { charge ->
            events += SyncEvent.Charge(charge.closedAt, charge.tableKey, charge.orderIds)
        }
        moves.forEach { move ->
            events += SyncEvent.Move(move.at, move)
        }
        events.sortWith(compareBy<SyncEvent> { it.at }.thenBy { if (it is SyncEvent.Move) 0 else 1 })
        return events
    }
}

internal data class PendingPayment(
    val orderId: String,
    val tableKey: String,
    val closedAt: Long,
)

internal data class PendingCharge(
    val tableKey: String,
    val closedAt: Long,
    val orderIds: List<String>,
)

internal data class PendingTableMove(
    val orderId: String,
    val fromTableId: String,
    val toTableId: String,
    val at: Long,
    val merge: Boolean,
)

internal sealed interface SyncEvent {
    val at: Long

    data class Charge(
        override val at: Long,
        val tableKey: String,
        val orderIds: List<String>,
    ) : SyncEvent

    data class Move(
        override val at: Long,
        val move: PendingTableMove,
    ) : SyncEvent
}
