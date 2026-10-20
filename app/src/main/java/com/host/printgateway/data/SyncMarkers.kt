package com.host.printgateway.data

import java.util.Locale

/** Markers shared by local order sync with HOST. */
object SyncMarkers {
    const val REMOTE_ITEM_PREFIX = "remote:"
    const val PAYMENT_PREFIX = "PAY|"
    const val MOVE_PREFIX = "MOVE|"
    private const val PRINT_RECEIPT = "P"
    private const val MOVE_SEPARATOR = "|MOVE|"

    fun paymentMarker(method: String, tip: Double, printReceipt: Boolean): String {
        val safeMethod = method.replace("|", " ").ifBlank { "Efectivo" }
        val tipText = String.format(Locale.US, "%.2f", tip)
        val body = "$safeMethod|$tipText"
        return PAYMENT_PREFIX + if (printReceipt) "$PRINT_RECEIPT|$body" else body
    }

    fun moveMarker(fromTableId: String, toTableId: String, at: Long, merge: Boolean): String {
        val mergeFlag = if (merge) "1" else "0"
        return "$MOVE_PREFIX$fromTableId|$toTableId|$at|$mergeFlag"
    }

    fun isPayment(syncStatus: String): Boolean = syncStatus.startsWith(PAYMENT_PREFIX)

    fun isStandaloneMove(syncStatus: String): Boolean = syncStatus.startsWith(MOVE_PREFIX)

    fun parseMove(syncStatus: String): TableMove? {
        val index = syncStatus.indexOf(MOVE_PREFIX)
        if (index < 0) return null
        val parts = syncStatus.substring(index + MOVE_PREFIX.length).split("|")
        if (parts.size < 4) return null
        val at = parts[2].toLongOrNull() ?: return null
        return TableMove(
            fromTableId = parts[0],
            toTableId = parts[1],
            at = at,
            merge = parts[3] == "1",
        )
    }

    /**
     * Keeps a pending table change when the order is paid before that change syncs.
     */
    fun attachingPayment(paymentMarker: String, currentStatus: String): String {
        val move = parseMove(currentStatus) ?: return paymentMarker
        return "$paymentMarker|${moveMarker(move.fromTableId, move.toTableId, move.at, move.merge)}"
    }

    fun withoutMove(syncStatus: String): String {
        val index = syncStatus.indexOf(MOVE_SEPARATOR)
        return if (index >= 0) syncStatus.substring(0, index) else syncStatus
    }

    fun parsePayment(syncStatus: String): PaymentIntent {
        val parts = withoutMove(syncStatus).removePrefix(PAYMENT_PREFIX).split("|")
        val printReceipt = parts.firstOrNull() == PRINT_RECEIPT
        val body = if (printReceipt) parts.drop(1) else parts
        val tip = body.lastOrNull()?.toDoubleOrNull() ?: 0.0
        val method = body.dropLast(1).joinToString("|").ifBlank { "Efectivo" }
        return PaymentIntent(method, tip, printReceipt)
    }
}

data class TableMove(
    val fromTableId: String,
    val toTableId: String,
    val at: Long,
    val merge: Boolean,
)

data class PaymentIntent(
    val method: String,
    val tip: Double,
    val printReceipt: Boolean,
)
