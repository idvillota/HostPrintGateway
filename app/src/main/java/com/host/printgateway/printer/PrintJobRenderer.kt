package com.host.printgateway.printer

import android.util.Base64
import com.host.printgateway.network.PrintJobDto
import com.host.printgateway.receipt.SalesReceiptXmlParser

/**
 * Turns a Host print job into ESC/POS bytes.
 * Supported formats: escpos-v1 (Base64), sales-receipt-xml.
 */
class PrintJobRenderer(
    private val formatter: EscPosReceiptFormatter = EscPosReceiptFormatter(),
) {

    fun toEscPos(job: PrintJobDto): ByteArray {
        return when (job.payloadFormat.lowercase()) {
            FORMAT_ESCPOS_V1 -> Base64.decode(job.payload, Base64.DEFAULT)
            FORMAT_SALES_RECEIPT_XML -> formatter.format(SalesReceiptXmlParser.parse(job.payload))
            else -> error("Formato no soportado: ${job.payloadFormat}")
        }
    }

    companion object {
        const val FORMAT_ESCPOS_V1 = "escpos-v1"
        const val FORMAT_SALES_RECEIPT_XML = "sales-receipt-xml"
    }
}
