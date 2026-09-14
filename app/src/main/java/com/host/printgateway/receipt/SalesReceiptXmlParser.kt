package com.host.printgateway.receipt

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

object SalesReceiptXmlParser {

    fun parse(xml: String): SalesReceipt {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xml))

        var tag = ""
        var tradeName = ""
        var invoiceNumber = ""
        var dianConsecutive = ""
        var dateTime = ""
        var tableCodes = ""
        var cashier = ""
        var customerName = ""
        var customerId = ""
        var resolutionNumber = ""
        var rangeFrom = ""
        var rangeTo = ""
        var total = 0.0
        var amountTendered = 0.0
        var articleCount = 0
        var impoconsumoBase = 0.0
        var impoconsumo = 0.0
        var paymentMethod = ""

        val lines = mutableListOf<ReceiptLine>()
        var lineDesc = ""
        var lineQty = 0.0
        var linePrice = 0.0
        var lineTotal = 0.0

        var event = parser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> tag = parser.name
                XmlPullParser.TEXT -> {
                    val text = parser.text?.trim().orEmpty()
                    if (text.isNotEmpty()) {
                        when (tag) {
                            "NombreComercial" -> if (tradeName.isEmpty()) tradeName = text
                            "NumeroFactura" -> invoiceNumber = text
                            "ConsecutivoDIAN" -> dianConsecutive = text
                            "FechaHora" -> dateTime = text
                            "Mesas" -> tableCodes = text
                            "Cajero" -> cashier = text
                            "Nombre" -> if (customerName.isEmpty()) customerName = text
                            "Identificacion" -> customerId = text
                            "Numero" -> resolutionNumber = text
                            "RangoDesde" -> rangeFrom = text
                            "RangoHasta" -> rangeTo = text
                            "Descripcion" -> lineDesc = text
                            "Cantidad" -> lineQty = text.toDoubleOrNull() ?: 0.0
                            "PrecioUnitario" -> linePrice = text.toDoubleOrNull() ?: 0.0
                            "TotalLinea" -> lineTotal = text.toDoubleOrNull() ?: 0.0
                            "Articulos" -> articleCount = text.toIntOrNull() ?: 0
                            "Total" -> total = text.toDoubleOrNull() ?: 0.0
                            "Metodo" -> paymentMethod = text
                            "Entregado" -> amountTendered = text.toDoubleOrNull() ?: 0.0
                            "BaseImpoconsumo" -> impoconsumoBase = text.toDoubleOrNull() ?: 0.0
                            "Impoconsumo" -> impoconsumo = text.toDoubleOrNull() ?: 0.0
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "Item") {
                        lines += ReceiptLine(lineDesc, lineQty, linePrice, lineTotal)
                        lineDesc = ""
                        lineQty = 0.0
                        linePrice = 0.0
                        lineTotal = 0.0
                    }
                    tag = ""
                }
            }
            event = parser.next()
        }

        return SalesReceipt(
            tradeName = tradeName,
            invoiceNumber = invoiceNumber,
            dianConsecutive = dianConsecutive,
            dateTime = dateTime,
            tableCodes = tableCodes,
            cashier = cashier,
            customerName = customerName,
            customerId = customerId,
            lines = lines,
            articleCount = articleCount,
            total = total,
            paymentMethod = paymentMethod,
            amountTendered = amountTendered,
            impoconsumoBase = impoconsumoBase,
            impoconsumo = impoconsumo,
            resolutionNumber = resolutionNumber,
            rangeFrom = rangeFrom,
            rangeTo = rangeTo,
        )
    }
}
