package com.host.printgateway.printer

import com.host.printgateway.receipt.ReceiptLine
import com.host.printgateway.receipt.SalesReceipt
import org.junit.Assert.assertTrue
import org.junit.Test

class EscPosReceiptFormatterTest {

    private val formatter = EscPosReceiptFormatter()

    @Test
    fun formatTestTicket_includes_brand_and_mac_hint() {
        val text = formatter.formatTestTicket("AA:BB").toString(Charsets.ISO_8859_1)
        assertTrue(text.contains("Host Print Gateway"))
        assertTrue(text.contains("AA:BB"))
    }

    @Test
    fun format_includes_trade_name_and_line_description() {
        val receipt = SalesReceipt(
            tradeName = "Host Bistro",
            invoiceNumber = "FV-9",
            cashier = "Ana",
            tableCodes = "M2",
            customerName = "Cliente",
            customerId = "999",
            lines = listOf(ReceiptLine("Sopa", 1.0, 8000.0, 8000.0)),
            articleCount = 1,
            total = 8000.0,
            paymentMethod = "Tarjeta",
            amountTendered = 8000.0,
        )

        val text = formatter.format(receipt).toString(Charsets.ISO_8859_1)
        assertTrue(text.contains("Host Bistro"))
        assertTrue(text.contains("Sopa"))
        assertTrue(text.contains("FACTURA"))
    }
}
