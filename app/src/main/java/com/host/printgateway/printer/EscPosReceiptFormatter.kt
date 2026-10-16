package com.host.printgateway.printer

import android.graphics.Bitmap
import com.host.printgateway.receipt.ReceiptPayment
import com.host.printgateway.receipt.SalesReceipt

/** Builds ESC/POS bytes for a Host sales receipt, following the 80 mm PDF. */
class EscPosReceiptFormatter {

    fun format(receipt: SalesReceipt, logo: Bitmap? = null): ByteArray {
        val out = mutableListOf<Byte>()
        val width = 32

        out.addAll(byteArrayOf(0x1B, 0x40).toList())
        if (logo != null) {
            out.addAll(align(0))
            out.addAll(EscPosRaster.fromBitmap(logo).toList())
            out.addAll(text("\n"))
        }
        out.addAll(align(1))

        if (receipt.tradeName.isNotBlank()) {
            out.addAll(byteArrayOf(0x1D, 0x21, 0x11).toList())
            out.addAll(bold(true))
            out.addAll(text("${receipt.tradeName}\n"))
            out.addAll(byteArrayOf(0x1D, 0x21, 0x00).toList())
            out.addAll(bold(false))
        }
        centered(out, receipt.legalName)
        centered(out, receipt.taxId)
        centered(out, receipt.address)
        centered(out, receipt.phone)
        centered(out, receipt.taxRegime)
        centered(out, receipt.legalRepresentative)

        out.addAll(text("$RULE\n"))
        out.addAll(bold(true))
        out.addAll(text("FACTURA ELECTRÓNICA\n"))
        out.addAll(bold(false))
        out.addAll(align(0))
        out.addAll(text("${twoColumns(receipt.invoiceNumber, "Identificador: ${receipt.dianConsecutive}", width)}\n"))
        out.addAll(text("${twoColumns("ORIGINAL", receipt.cashier, width)}\n"))
        out.addAll(text("${formatIssued(receipt.dateTime)}\n"))
        if (receipt.tableCodes.isNotBlank()) {
            out.addAll(text("Sala-Mesa: ${receipt.tableCodes}\n"))
        }

        out.addAll(text("$RULE\n"))
        out.addAll(bold(true))
        out.addAll(text("${receipt.customerName.uppercase()}\n"))
        out.addAll(bold(false))
        if (receipt.customerId.isNotBlank()) {
            out.addAll(text("CC : ${receipt.customerId}\n"))
        }
        out.addAll(text("$RULE\n"))
        out.addAll(text("DESCRIPCIÓN\n"))
        out.addAll(text("${twoColumns("CAN REF UM %IMP", "VALOR", width)}\n"))

        val percent = receipt.impoconsumoPercent.toInt()
        for (line in receipt.lines) {
            out.addAll(text("${line.description}\n"))
            val quantity = formatQuantity(line.quantity)
            val left = "$quantity  uds  $percent"
            out.addAll(text("${twoColumns(left, money(line.lineTotal), width)}\n"))
            if (line.notes.isNotBlank()) {
                out.addAll(text("+ ${line.notes}\n"))
            }
        }

        out.addAll(text("$RULE\n"))
        out.addAll(bold(true))
        out.addAll(text("${twoColumns("${receipt.articleCount} Artículos", "TOTAL ${money(receipt.total)}", width)}\n"))
        out.addAll(bold(false))

        for (payment in paymentsOf(receipt)) {
            out.addAll(text("${twoColumns(payment.method, "Entregado ${money(payment.amount)}", width)}\n"))
        }
        if (receipt.changeDue > 0) {
            out.addAll(text("${twoColumns("Cambio", money(receipt.changeDue), width)}\n"))
        }
        if (receipt.discountAmount > 0) {
            out.addAll(text("${twoColumns("Descuento", "-${money(receipt.discountAmount)}", width)}\n"))
        }
        if (receipt.tipAmount > 0) {
            out.addAll(text("${twoColumns("Propina", money(receipt.tipAmount), width)}\n"))
        }

        out.addAll(text("$RULE\n"))
        out.addAll(text("Impuestos incluidos\n"))
        out.addAll(text("${twoColumns("IMPOCONSUMO $percent%", money(receipt.impoconsumo), width)}\n"))
        out.addAll(text("${twoColumns("Base", money(receipt.impoconsumoBase), width)}\n"))

        out.addAll(text("$RULE\n"))
        out.addAll(align(1))
        if (receipt.resolutionNumber.isNotBlank()) {
            out.addAll(text("Resolución DIAN ${receipt.resolutionNumber}\n"))
        }
        out.addAll(text("RANGO ${rangeLabel(receipt.invoiceNumber)} (Desde ${receipt.rangeFrom} Hasta ${receipt.rangeTo})\n"))
        out.addAll(bold(true))
        out.addAll(text("Autorizado\n"))
        out.addAll(bold(false))
        out.addAll(text("Diseñado por HOST\n\n\n\n"))
        out.addAll(byteArrayOf(0x1D, 0x56, 0x41, 0x00).toList())
        return out.toByteArray()
    }

    /** Logo only, using the same placement as the invoice. */
    fun formatLogo(logo: Bitmap): ByteArray {
        val out = mutableListOf<Byte>()
        out.addAll(byteArrayOf(0x1B, 0x40).toList())
        out.addAll(align(0))
        out.addAll(EscPosRaster.fromBitmap(logo).toList())
        out.addAll(text("\n\n\n"))
        out.addAll(byteArrayOf(0x1D, 0x56, 0x41, 0x00).toList())
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

    private fun centered(out: MutableList<Byte>, value: String) {
        if (value.isBlank()) return
        out.addAll(text("$value\n"))
    }

    private fun paymentsOf(receipt: SalesReceipt): List<ReceiptPayment> {
        if (receipt.payments.isNotEmpty()) return receipt.payments
        if (receipt.paymentMethod.isBlank() && receipt.amountTendered <= 0) return emptyList()
        return listOf(ReceiptPayment(receipt.paymentMethod, receipt.amountTendered))
    }

    private fun rangeLabel(invoiceNumber: String): String {
        val prefix = invoiceNumber.trim().dropLastWhile { it.isDigit() }.trimEnd('-', ' ')
        return prefix.ifBlank { "—" }
    }

    private fun formatQuantity(quantity: Double): String =
        if (quantity % 1.0 == 0.0) quantity.toInt().toString() else quantity.toString()

    private fun formatIssued(raw: String): String {
        val match = Regex("""(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})""").find(raw) ?: return raw
        val (year, month, day, hour, minute) = match.destructured
        val monthIndex = month.toIntOrNull()?.minus(1) ?: return raw
        if (monthIndex !in MONTHS.indices) return raw
        val hour24 = hour.toIntOrNull() ?: return raw
        val hour12 = when (val mod = hour24 % 12) {
            0 -> 12
            else -> mod
        }
        val suffix = if (hour24 < 12) "a. m." else "p. m."
        return "${MONTHS[monthIndex]} $day $year ${hour12.toString().padStart(2, '0')}:$minute $suffix"
    }

    private fun money(amount: Double): String {
        val rounded = amount.toLong()
        val digits = rounded.toString()
        val grouped = digits.reversed().chunked(3).joinToString(".").reversed()
        return "$$grouped"
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

    private companion object {
        const val RULE = "--------------------------------"
        val MONTHS = listOf(
            "ene.", "feb.", "mar.", "abr.", "may.", "jun.",
            "jul.", "ago.", "sep.", "oct.", "nov.", "dic.",
        )
    }
}
