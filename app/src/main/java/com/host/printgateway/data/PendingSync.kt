package com.host.printgateway.data

/**
 * Offline payments for one table are separate HOST charges.
 * Orders paid in the same tap share [PendingPayment.closedAt] and stay together.
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
}

internal data class PendingPayment(
    val orderId: String,
    val tableKey: String,
    val closedAt: Long,
)
