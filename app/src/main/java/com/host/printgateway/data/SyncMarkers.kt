package com.host.printgateway.data

import java.util.Locale

/** Markers shared by local order sync with HOST. */
object SyncMarkers {
    const val REMOTE_ITEM_PREFIX = "remote:"
    const val PAYMENT_PREFIX = "PAY|"
    private const val PRINT_RECEIPT = "P"
    private const val MOVE_SEPARATOR = "|MOVE|"

    fun paymentMarker(method: String, tip: Double, printReceipt: Boolean): String {
        val safeMethod = method.replace("|", " ").ifBlank { "Efectivo" }
        val tipText = String.format(Locale.US, "%.2f", tip)
        val body = "$safeMethod|$tipText"
        return PAYMENT_PREFIX + if (printReceipt) "$PRINT_RECEIPT|$body" else body
    }

    fun isPayment(syncStatus: String): Boolean = syncStatus.startsWith(PAYMENT_PREFIX)

    fun parsePayment(syncStatus: String): PaymentIntent {
        val parts = withoutLegacyMove(syncStatus).removePrefix(PAYMENT_PREFIX).split("|")
        val printReceipt = parts.firstOrNull() == PRINT_RECEIPT
        val body = if (printReceipt) parts.drop(1) else parts
        val tip = body.lastOrNull()?.toDoubleOrNull() ?: 0.0
        val method = body.dropLast(1).joinToString("|").ifBlank { "Efectivo" }
        return PaymentIntent(method, tip, printReceipt)
    }

    private fun withoutLegacyMove(syncStatus: String): String {
        val index = syncStatus.indexOf(MOVE_SEPARATOR)
        return if (index >= 0) syncStatus.substring(0, index) else syncStatus
    }
}

data class PaymentIntent(
    val method: String,
    val tip: Double,
    val printReceipt: Boolean,
)
