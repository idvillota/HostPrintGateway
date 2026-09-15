package com.host.printgateway.printer

import com.host.printgateway.receipt.SalesReceipt
import java.text.NumberFormat
import java.util.Locale

/** Builds ESC/POS bytes for a Host sales receipt (58/80mm thermal). */
class EscPosReceiptFormatter {

    fun format(receipt: SalesReceipt): ByteArray {
        val out = mutableListOf<Byte>()
        val money = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }

        out.addAll(byteArrayOf(0x1B, 0x40).toList()) // reset

        out.addAll(align(2))
        out.addAll(text("${receipt.invoiceNumber}\n"))
        out.addAll(text("ORIGINAL\n\n"))

        out.addAll(align(1))
        out.addAll(byteArrayOf(0x1D, 0x21, 0x11).toList())
        out.addAll(text("${receipt.tradeName}\n"))
        out.addAll(byteArrayOf(0x1D, 0x21, 0x00).toList())
        out.addAll(bold(true))
        out.addAll(text("FACTURA ELECTRÓNICA\n"))
        out.addAll(bold(false))
        out.addAll(text("Identificador: ${receipt.dianConsecutive}\n"))
        out.addAll(text("Cajero: ${receipt.cashier}\n"))
        out.addAll(text("${receipt.dateTime}\n"))
        out.addAll(text("Sala-Mesa: ${receipt.tableCodes}\n"))
        out.addAll(text("================================\n"))

        out.addAll(align(0))
        out.addAll(text("${receipt.customerName.uppercase()}\n"))
        out.addAll(text("CC: ${receipt.customerId}\n"))
        out.addAll(text("--------------------------------\n"))
        out.addAll(text("DESCRIPCIÓN\n"))
        out.addAll(text(" | CAN REF UM %IM | VALOR\n"))
        out.addAll(text("--------------------------------\n"))

        for (line in receipt.lines) {
            out.addAll(text("${line.description}\n"))
            val left = " | ${line.quantity.toInt()}   uds  8"
            out.addAll(text("${twoColumns(left, money.format(line.lineTotal), 32)}\n"))
        }

        out.addAll(text("--------------------------------\n"))
        out.addAll(bold(true))
        out.addAll(text(
            "${twoColumns("${receipt.articleCount} Artículos", "TOTAL ${money.format(receipt.total)}", 32)}\n"
        ))
        out.addAll(bold(false))
        out.addAll(text("--------------------------------\n"))
        out.addAll(text("${twoColumns(receipt.paymentMethod, "Entregado", 32)}\n"))
        out.addAll(text("${twoColumns("", money.format(receipt.amountTendered), 32)}\n"))
        out.addAll(text("--------------------------------\n"))
        out.addAll(text("Impuestos incluidos\n"))
        out.addAll(text(
            "${twoColumns("IMPOCONSUMO 8%  ${money.format(receipt.impoconsumoBase)}", money.format(receipt.impoconsumo), 32)}\n"
        ))
        out.addAll(text("--------------------------------\n"))

        out.addAll(align(1))
        out.addAll(text("Resolución DIAN ${receipt.resolutionNumber}\n"))
        out.addAll(text("RANGO (Desde ${receipt.rangeFrom} Hasta ${receipt.rangeTo})\n"))
        out.addAll(text("Autorizado\n\n\n\n"))
        out.addAll(byteArrayOf(0x1D, 0x56, 0x41, 0x00).toList()) // cut

        return out.toByteArray()
    }

    /** Minimal self-test ticket (no Host XML required). */
    fun formatTestTicket(macHint: String): ByteArray {
        val out = mutableListOf<Byte>()
        out.addAll(byteArrayOf(0x1B, 0x40).toList())
        out.addAll(align(1))
        out.addAll(bold(true))
        out.addAll(text("Host Print Gateway\n"))
        out.addAll(bold(false))
        out.addAll(text("Prueba de impresion\n"))
        out.addAll(text("$macHint\n\n\n\n"))
        out.addAll(byteArrayOf(0x1D, 0x56, 0x41, 0x00).toList())
        return out.toByteArray()
    }

    private fun text(value: String): List<Byte> =
        value.toByteArray(Charsets.ISO_8859_1).toList()

    private fun align(mode: Byte): List<Byte> = byteArrayOf(0x1B, 0x61, mode).toList()

    private fun bold(on: Boolean): List<Byte> =
        byteArrayOf(0x1B, 0x45, if (on) 0x01 else 0x00).toList()

    private fun twoColumns(left: String, right: String, width: Int): String {
        val total = left.length + right.length
        if (total >= width) return "$left $right"
        return left + " ".repeat(width - total) + right
    }
}
