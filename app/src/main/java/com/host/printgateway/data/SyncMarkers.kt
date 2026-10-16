package com.host.printgateway.data

import java.util.Locale

/** Markers shared by local order sync with HOST. */
object SyncMarkers {
    const val REMOTE_ITEM_PREFIX = "remote:"
    const val PAYMENT_PREFIX = "PAY|"
    private const val PRINT_RECEIPT = "P"

    fun paymentMarker(method: String, tip: Double, printReceipt: Boolean): String {
        val safeMethod = method.replace("|", " ").ifBlank { "Efectivo" }
        val tipText = String.format(Locale.US, "%.2f", tip)
        val body = "$safeMethod|$tipText"
        return PAYMENT_PREFIX + if (printReceipt) "$PRINT_RECEIPT|$body" else body
    }

    fun parsePayment(syncStatus: String): PaymentIntent {
        val parts = syncStatus.removePrefix(PAYMENT_PREFIX).split("|")
        val printReceipt = parts.firstOrNull() == PRINT_RECEIPT
        val body = if (printReceipt) parts.drop(1) else parts
        val tip = body.lastOrNull()?.toDoubleOrNull() ?: 0.0
        val method = body.dropLast(1).joinToString("|").ifBlank { "Efectivo" }
        return PaymentIntent(method, tip, printReceipt)
    }
}

data class PaymentIntent(
    val method: String,
    val tip: Double,
    val printReceipt: Boolean,
)
