package com.host.printgateway.printer

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.util.Locale

class KitchenTicketFormatter {

    fun format(payload: String): ByteArray {

        val ticket = parseXml(payload)

        val output = mutableListOf<Byte>()

        /*
         * Inicializar impresora.
         */
        output.addAll(
            byteArrayOf(
                0x1B,
                0x40,
            ).toList()
        )

        /*
         * Centrar.
         */
        output.addAll(
            byteArrayOf(
                0x1B,
                0x61,
                0x01,
            ).toList()
        )

        /*
         * Negrita.
         */
        output.addAll(
            byteArrayOf(
                0x1B,
                0x45,
                0x01,
            ).toList()
        )

        output.addAll(
            text("COMANDA\n")
        )

        /*
         * Desactivar negrita.
         */
        output.addAll(
            byteArrayOf(
                0x1B,
                0x45,
                0x00,
            ).toList()
        )

        /*
         * Alinear izquierda.
         */
        output.addAll(
            byteArrayOf(
                0x1B,
                0x61,
                0x00,
            ).toList()
        )

        output.addAll(
            text("==============================\n")
        )

        output.addAll(
            text("Mesa: ${ticket.tableCode}\n")
        )

        output.addAll(
            text("Orden: ${ticket.orderNumber}\n")
        )

        if (ticket.sentBy.isNotBlank()) {
            output.addAll(
                text("Mesero: ${ticket.sentBy}\n")
            )
        }

        if (ticket.printerStationName.isNotBlank()) {
            output.addAll(
                text("Estacion: ${ticket.printerStationName}\n")
            )
        }

        output.addAll(
            text("------------------------------\n")
        )

        for (line in ticket.lines) {

            output.addAll(
                text(
                    "${formatQuantity(line.quantity)} x ${line.productName}\n"
                )
            )

            if (line.notes.isNotBlank()) {
                output.addAll(
                    text(
                        "  Nota: ${line.notes}\n"
                    )
                )
            }

            if (line.excludedIngredients.isNotEmpty()) {

                output.addAll(
                    text("  Sin:\n")
                )

                for (ingredient in line.excludedIngredients) {
                    output.addAll(
                        text(
                            "    - $ingredient\n"
                        )
                    )
                }
            }
        }

        output.addAll(
            text("------------------------------\n")
        )

        if (ticket.isCancellation) {

            output.addAll(
                byteArrayOf(
                    0x1B,
                    0x45,
                    0x01,
                ).toList()
            )

            output.addAll(
                text("ANULACION\n")
            )

            output.addAll(
                byteArrayOf(
                    0x1B,
                    0x45,
                    0x00,
                ).toList()
            )

            if (ticket.cancelReason.isNotBlank()) {
                output.addAll(
                    text(
                        "Motivo: ${ticket.cancelReason}\n"
                    )
                )
            }
        }

        output.addAll(
            text("\n")
        )

        /*
         * Alimentar papel.
         */
        output.addAll(
            text("\n\n")
        )

        /*
         * Corte.
         */
        output.addAll(
            byteArrayOf(
                0x1D,
                0x56,
                0x41,
                0x00,
            ).toList()
        )

        return output.toByteArray()
    }

    private fun parseXml(
        payload: String,
    ): KitchenTicket {

        require(payload.isNotBlank()) {
            "El XML de la comanda está vacío."
        }

        if (!payload.trimStart().startsWith("<")) {
            error(
                "El payload de la comanda no es XML."
            )
        }

        val parser = Xml.newPullParser()

        parser.setInput(
            payload.reader(),
        )

        var event = parser.eventType

        var tableCode = ""
        var orderNumber = ""
        var sentBy = ""
        var sentAtUtc = ""
        var printerStationName = ""
        var printerStationCode = ""
        var isCancellation = false
        var cancelReason = ""

        val lines = mutableListOf<KitchenTicketLine>()

        var currentLine: MutableKitchenTicketLine? = null
        var currentExcludedIngredients =
            mutableListOf<String>()

        while (event != XmlPullParser.END_DOCUMENT) {

            when (event) {

                XmlPullParser.START_TAG -> {

                    when (parser.name) {

                        "Line" -> {
                            currentLine =
                                MutableKitchenTicketLine()

                            currentExcludedIngredients =
                                mutableListOf()
                        }

                        "TableCode" -> {
                            tableCode =
                                parser.nextText()
                        }

                        "OrderNumber" -> {
                            orderNumber =
                                parser.nextText()
                        }

                        "SentBy" -> {
                            sentBy =
                                parser.nextText()
                        }

                        "SentAtUtc" -> {
                            sentAtUtc =
                                parser.nextText()
                        }

                        "PrinterStation" -> {
                            // La información se obtiene de Name/Code.
                        }

                        "Name" -> {
                            /*
                             * Name puede pertenecer a PrinterStation.
                             * En nuestro XML actual solamente existe allí.
                             */
                            if (currentLine == null) {
                                printerStationName =
                                    parser.nextText()
                            }
                        }

                        "Code" -> {
                            if (currentLine == null) {
                                printerStationCode =
                                    parser.nextText()
                            }
                        }

                        "ProductName" -> {
                            currentLine?.productName =
                                parser.nextText()
                        }

                        "Quantity" -> {
                            currentLine?.quantity =
                                parser.nextText()
                                    .toDoubleOrNull()
                                    ?: 0.0
                        }

                        "Notes" -> {
                            currentLine?.notes =
                                parser.nextText()
                        }

                        "Ingredient" -> {
                            currentExcludedIngredients.add(
                                parser.nextText()
                            )
                        }

                        "IsCancellation" -> {
                            isCancellation =
                                parser.nextText()
                                    .equals(
                                        "true",
                                        ignoreCase = true,
                                    )
                        }

                        "CancelReason" -> {
                            cancelReason =
                                parser.nextText()
                        }
                    }
                }

                XmlPullParser.END_TAG -> {

                    when (parser.name) {

                        "Line" -> {

                            val line = currentLine

                            if (line != null) {

                                lines.add(
                                    KitchenTicketLine(
                                        productName =
                                            line.productName,
                                        quantity =
                                            line.quantity,
                                        notes =
                                            line.notes,
                                        excludedIngredients =
                                            currentExcludedIngredients
                                                .toList(),
                                    )
                                )
                            }

                            currentLine = null

                            currentExcludedIngredients =
                                mutableListOf()
                        }
                    }
                }
            }

            event = parser.next()
        }

        return KitchenTicket(
            tableCode = tableCode,
            orderNumber = orderNumber,
            sentBy = sentBy,
            sentAtUtc = sentAtUtc,
            printerStationName = printerStationName,
            printerStationCode = printerStationCode,
            lines = lines,
            isCancellation = isCancellation,
            cancelReason = cancelReason,
        )
    }

    private fun formatQuantity(
        quantity: Double,
    ): String {
        return if (quantity % 1.0 == 0.0) {
            quantity.toLong().toString()
        } else {
            String.format(
                Locale.US,
                "%.2f",
                quantity,
            ).trimEnd('0').trimEnd('.')
        }
    }

    private fun text(
        value: String,
    ): List<Byte> {
        return value
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("Á", "A")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ó", "O")
            .replace("Ú", "U")
            .replace("ñ", "n")
            .replace("Ñ", "N")
            .toByteArray(
                Charsets.ISO_8859_1
            )
            .toList()
    }

    private data class KitchenTicket(
        val tableCode: String,
        val orderNumber: String,
        val sentBy: String,
        val sentAtUtc: String,
        val printerStationName: String,
        val printerStationCode: String,
        val lines: List<KitchenTicketLine>,
        val isCancellation: Boolean,
        val cancelReason: String,
    )

    private data class KitchenTicketLine(
        val productName: String,
        val quantity: Double,
        val notes: String,
        val excludedIngredients: List<String>,
    )

    private class MutableKitchenTicketLine(
        var productName: String = "",
        var quantity: Double = 0.0,
        var notes: String = "",
    )
}