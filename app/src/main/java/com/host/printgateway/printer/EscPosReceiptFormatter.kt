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

        out += byteArrayOf(0x1B, 0x40) // reset

        out += align(2)
        out += text("${receipt.invoiceNumber}\n")
        out += text("ORIGINAL\n\n")

        out += align(1)
        out += byteArrayOf(0x1D, 0x21, 0x11)
        out += text("${receipt.tradeName}\n")
        out += byteArrayOf(0x1D, 0x21, 0x00)
        out += bold(true)
        out += text("FACTURA ELECTRÓNICA\n")
        out += bold(false)
        out += text("Identificador: ${receipt.dianConsecutive}\n")
        out += text("Cajero: ${receipt.cashier}\n")
        out += text("${receipt.dateTime}\n")
        out += text("Sala-Mesa: ${receipt.tableCodes}\n")
        out += text("================================\n")

        out += align(0)
        out += text("${receipt.customerName.uppercase()}\n")
        out += text("CC: ${receipt.customerId}\n")
        out += text("--------------------------------\n")
        out += text("DESCRIPCIÓN\n")
        out += text(" | CAN REF UM %IM | VALOR\n")
        out += text("--------------------------------\n")

        for (line in receipt.lines) {
            out += text("${line.description}\n")
            val left = " | ${line.quantity.toInt()}   uds  8"
            out += text("${twoColumns(left, money.format(line.lineTotal), 32)}\n")
        }

        out += text("--------------------------------\n")
        out += bold(true)
        out += text(
            "${twoColumns("${receipt.articleCount} Artículos", "TOTAL ${money.format(receipt.total)}", 32)}\n"
        )
        out += bold(false)
        out += text("--------------------------------\n")
        out += text("${twoColumns(receipt.paymentMethod, "Entregado", 32)}\n")
        out += text("${twoColumns("", money.format(receipt.amountTendered), 32)}\n")
        out += text("--------------------------------\n")
        out += text("Impuestos incluidos\n")
        out += text(
            "${twoColumns("IMPOCONSUMO 8%  ${money.format(receipt.impoconsumoBase)}", money.format(receipt.impoconsumo), 32)}\n"
        )
        out += text("--------------------------------\n")

        out += align(1)
        out += text("Resolución DIAN ${receipt.resolutionNumber}\n")
        out += text("RANGO (Desde ${receipt.rangeFrom} Hasta ${receipt.rangeTo})\n")
        out += text("Autorizado\n\n\n\n")
        out += byteArrayOf(0x1D, 0x56, 0x41, 0x00) // cut

        return out.toByteArray()
    }

    /** Minimal self-test ticket (no Host XML required). */
    fun formatTestTicket(macHint: String): ByteArray {
        val out = mutableListOf<Byte>()
        out += byteArrayOf(0x1B, 0x40)
        out += align(1)
        out += bold(true)
        out += text("Host Print Gateway\n")
        out += bold(false)
        out += text("Prueba de impresion\n")
        out += text("$macHint\n\n\n\n")
        out += byteArrayOf(0x1D, 0x56, 0x41, 0x00)
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
