
package com.host.printgateway.printer

import android.graphics.Bitmap
import com.host.printgateway.receipt.ReceiptPayment
import com.host.printgateway.receipt.SalesReceipt
import java.util.Locale

/**
 * Formatea facturas ESC/POS para papel térmico estrecho.
 *
 * Mantiene los modelos SalesReceipt y ReceiptPayment existentes.
 * Ancho lógico inicial: 32 caracteres.
 */
class EscPosReceiptFormatter {

    fun format(receipt: SalesReceipt, logo: Bitmap? = null): ByteArray {
        val out = mutableListOf<Byte>()

        out.addAll(RESET)
        out.addAll(LINE_SPACING_DEFAULT)

        if (logo != null) {
            out.addAll(align(0))
            out.addAll(EscPosRaster.fromBitmap(logo).toList())
            out.addAll(RESET)
            out.addAll(LINE_SPACING_DEFAULT)
        }

        // ENCABEZADO
        out.addAll(align(1))

        if (receipt.tradeName.isNotBlank()) {
            out.addAll(bold(true))
            out.addAll(text("${receipt.tradeName.trim()}\n"))
            out.addAll(bold(false))
        }

        centered(out, receipt.legalName)
        centered(out, receipt.taxId)
        centered(out, receipt.address)
        centered(out, receipt.phone)
        centered(out, receipt.taxRegime)
        centered(out, receipt.legalRepresentative)

        separator(out)

        // INFORMACIÓN DE LA FACTURA
        out.addAll(align(0))
        out.addAll(text(pairLines(
            receipt.invoiceNumber,
            "Identificador: ${receipt.dianConsecutive}"
        )))

        out.addAll(text("ORIGINAL\n"))

        if (receipt.cashier.isNotBlank()) {
            out.addAll(align(1))
            out.addAll(bold(true))
            out.addAll(text("${receipt.cashier}\n"))
            out.addAll(bold(false))
            out.addAll(align(0))
        }

        out.addAll(text("${formatIssued(receipt.dateTime)}\n"))

        if (receipt.tableCodes.isNotBlank()) {
            out.addAll(text("Sala-Mesa: ${receipt.tableCodes}\n"))
        }

        separator(out)

        // CLIENTE
        out.addAll(bold(true))
        out.addAll(text("${receipt.customerName.uppercase(Locale.ROOT)}\n"))
        out.addAll(bold(false))

        if (receipt.customerId.isNotBlank()) {
            out.addAll(text("CC: ${receipt.customerId}\n"))
        }

        separator(out)

        // PRODUCTOS
        out.addAll(bold(true))
        out.addAll(text("DESCRIPCION\n"))
        out.addAll(bold(false))

        val percent = formatPercent(receipt.impoconsumoPercent)

        for (line in receipt.lines) {
            for (part in wrap(line.description, RECEIPT_WIDTH)) {
                out.addAll(text("$part\n"))
            }

            out.addAll(text(
                pairLines(
                    formatQuantity(line.quantity),
                    money(line.lineTotal)
                )
            ))

            if (line.notes.isNotBlank()) {
                for (part in wrap("+ ${line.notes}", RECEIPT_WIDTH)) {
                    out.addAll(text("$part\n"))
                }
            }
        }

        separator(out)

        // TOTAL
        out.addAll(bold(true))
        out.addAll(text(
            pairLines(
                "${receipt.articleCount} Articulos",
                "TOTAL ${money(receipt.total)}"
            )
        ))
        out.addAll(bold(false))

        // PAGOS
        for (payment in paymentsOf(receipt)) {
            out.addAll(text(
                pairLines(
                    payment.method,
                    "Entregado ${money(payment.amount)}"
                )
            ))
        }

        if (receipt.changeDue > 0) {
            out.addAll(text(
                pairLines("Cambio", money(receipt.changeDue))
            ))
        }

        if (receipt.discountAmount > 0) {
            out.addAll(text(
                pairLines("Descuento", "-${money(receipt.discountAmount)}")
            ))
        }

        if (receipt.tipAmount > 0) {
            out.addAll(text(
                pairLines("Propina", money(receipt.tipAmount))
            ))
        }

        separator(out)

        // IMPUESTOS
        out.addAll(text("Impuestos incluidos\n"))
        out.addAll(text("IMPOCONSUMO $percent%\n"))

        out.addAll(text(
            pairLines(
                "Base ${money(receipt.impoconsumoBase)}",
                "Impuesto ${money(receipt.impoconsumo)}"
            )
        ))

        separator(out)

        // INFORMACIÓN DIAN
        out.addAll(align(1))

        if (receipt.resolutionNumber.isNotBlank()) {
            out.addAll(text(
                "Resolucion DIAN ${receipt.resolutionNumber}\n"
            ))
        }

        out.addAll(text(
            "RANGO ${rangeLabel(receipt.invoiceNumber)}\n"
        ))

        out.addAll(text(
            "Desde ${receipt.rangeFrom} Hasta ${receipt.rangeTo}\n"
        ))

        out.addAll(bold(true))
        out.addAll(text("Autorizado\n"))
        out.addAll(bold(false))

        out.addAll(text("Implementado por HOST\n\n\n"))
        out.addAll(CUT_PAPER)

        return out.toByteArray()
    }

    /** Imprime únicamente el logo. */
    fun formatLogo(logo: Bitmap): ByteArray {
        val out = mutableListOf<Byte>()

        out.addAll(RESET)
        out.addAll(align(0))
        out.addAll(EscPosRaster.fromBitmap(logo).toList())
        out.addAll(text("\n\n\n"))
        out.addAll(CUT_PAPER)

        return out.toByteArray()
    }

    /** Ticket de prueba sin necesidad de XML. */
    fun formatTestTicket(macHint: String): ByteArray {
        val out = mutableListOf<Byte>()

        out.addAll(RESET)
        out.addAll(align(1))
        out.addAll(bold(true))
        out.addAll(text("Host Print Gateway\n"))
        out.addAll(bold(false))
        out.addAll(text("Prueba de impresion\n"))
        out.addAll(text("$macHint\n\n\n"))
        out.addAll(CUT_PAPER)

        return out.toByteArray()
    }

    private fun centered(out: MutableList<Byte>, value: String) {
        if (value.isBlank()) return

        // Mantiene centradas las líneas largas sin perder información.
        for (part in wrap(value.trim(), RECEIPT_WIDTH)) {
            out.addAll(text("$part\n"))
        }
    }

    private fun separator(out: MutableList<Byte>) {
        out.addAll(align(0))
        out.addAll(text("${"-".repeat(RECEIPT_WIDTH)}\n"))
    }

    private fun paymentsOf(receipt: SalesReceipt): List<ReceiptPayment> {
        if (receipt.payments.isNotEmpty()) return receipt.payments

        if (receipt.paymentMethod.isBlank() && receipt.amountTendered <= 0) {
            return emptyList()
        }

        return listOf(
            ReceiptPayment(
                receipt.paymentMethod,
                receipt.amountTendered
            )
        )
    }

    /**
     * Dos columnas que nunca se superponen.
     * Si no caben, pasan a dos líneas.
     */
    private fun pairLines(left: String, right: String): String {
        val l = left.trim()
        val r = right.trim()

        if (l.isEmpty()) return "$r\n"
        if (r.isEmpty()) return "$l\n"

        if (l.length + r.length + 1 <= RECEIPT_WIDTH) {
            return l + " ".repeat(
                RECEIPT_WIDTH - l.length - r.length
            ) + r + "\n"
        }

        return wrap(l, RECEIPT_WIDTH).joinToString("\n") +
            "\n" +
            r + "\n"
    }

    /**
     * Divide texto por palabras sin eliminar caracteres.
     * Las palabras más largas que el ancho también se dividen.
     */
    private fun wrap(value: String, width: Int): List<String> {
        if (value.isEmpty()) return listOf("")
        if (width <= 0) return listOf(value)

        val result = mutableListOf<String>()
        var current = StringBuilder()

        for (word in value.trim().split(Regex("\\s+"))) {
            if (word.isEmpty()) continue

            if (word.length > width) {
                if (current.isNotEmpty()) {
                    result.add(current.toString())
                    current = StringBuilder()
                }

                word.chunked(width).forEachIndexed { index, part ->
                    if (index < word.length / width ||
                        word.length % width == 0
                    ) {
                        result.add(part)
                    } else {
                        current.append(part)
                    }
                }
                continue
            }

            val needed = if (current.isEmpty()) {
                word.length
            } else {
                current.length + 1 + word.length
            }

            if (needed > width) {
                result.add(current.toString())
                current = StringBuilder(word)
            } else {
                if (current.isNotEmpty()) current.append(' ')
                current.append(word)
            }
        }

        if (current.isNotEmpty()) result.add(current.toString())

        return result.ifEmpty { listOf("") }
    }

    private fun formatQuantity(quantity: Double): String =
        if (quantity % 1.0 == 0.0) {
            quantity.toLong().toString()
        } else {
            quantity.toString()
        }

    private fun formatPercent(percent: Double): String =
        if (percent % 1.0 == 0.0) {
            percent.toInt().toString()
        } else {
            percent.toString()
        }

    private fun formatIssued(raw: String): String {
        val match = Regex(
            """(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})"""
        ).find(raw) ?: return raw

        val (year, month, day, hour, minute) = match.destructured
        val monthIndex = month.toIntOrNull()?.minus(1) ?: return raw

        if (monthIndex !in MONTHS.indices) return raw

        val hour24 = hour.toIntOrNull() ?: return raw
        val hour12 = when (hour24 % 12) {
            0 -> 12
            else -> hour24 % 12
        }

        val suffix = if (hour24 < 12) "a. m." else "p. m."

        return "${MONTHS[monthIndex]} $day $year " +
            "${hour12.toString().padStart(2, '0')}:$minute $suffix"
    }

    private fun money(amount: Double): String {
        val rounded = amount.toLong()
        val negative = rounded < 0
        val digits = rounded.toString().removePrefix("-")
        val grouped = digits.reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()

        return "${if (negative) "-$" else "$"}$grouped"
    }

    private fun rangeLabel(invoiceNumber: String): String {
        val prefix = invoiceNumber.trim()
            .dropLastWhile { it.isDigit() }
            .trimEnd('-', ' ')

        return prefix.ifBlank { "—" }
    }

    private fun text(value: String): List<Byte> =
        value.toByteArray(Charsets.ISO_8859_1).toList()

    private fun align(mode: Int): List<Byte> =
        byteArrayOf(0x1B, 0x61, mode.toByte()).toList()

    private fun bold(on: Boolean): List<Byte> =
        byteArrayOf(
            0x1B,
            0x45,
            if (on) 0x01 else 0x00
        ).toList()

    private companion object {
        const val RECEIPT_WIDTH = 32

        val RESET = byteArrayOf(0x1B, 0x40).toList()
        val LINE_SPACING_DEFAULT = byteArrayOf(0x1B, 0x32).toList()
        val CUT_PAPER = byteArrayOf(0x1D, 0x56, 0x00).toList()

        val MONTHS = listOf(
            "ene.", "feb.", "mar.", "abr.", "may.", "jun.",
            "jul.", "ago.", "sep.", "oct.", "nov.", "dic."
        )
    }
}
