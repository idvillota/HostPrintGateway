package com.host.printgateway.receipt

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

object SalesReceiptXmlParser {

    fun parse(xml: String): SalesReceipt {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xml))

        var tag = ""
        var inItem = false
        var inPayment = false
        var tradeName = ""
        var legalName = ""
        var taxId = ""
        var address = ""
        var phone = ""
        var taxRegime = ""
        var legalRepresentative = ""
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
        var changeDue = 0.0
        var discountAmount = 0.0
        var tipAmount = 0.0
        var articleCount = 0
        var impoconsumoPercent = 0.0
        var impoconsumoBase = 0.0
        var impoconsumo = 0.0
        var paymentMethod = ""
        var payMethod = ""
        var payAmount = 0.0

        val lines = mutableListOf<ReceiptLine>()
        val payments = mutableListOf<ReceiptPayment>()
        var lineDesc = ""
        var lineQty = 0.0
        var linePrice = 0.0
        var lineTotal = 0.0
        var lineNotes = ""

        var event = parser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> {
                    tag = parser.name
                    when (tag) {
                        "Item" -> inItem = true
                        "Pago" -> inPayment = true
                    }
                }
                XmlPullParser.TEXT -> {
                    val text = parser.text?.trim().orEmpty()
                    if (text.isNotEmpty()) {
                        when (tag) {
                            "NombreComercial" -> if (tradeName.isEmpty()) tradeName = text
                            "RazonSocial" -> legalName = text
                            "NIT" -> taxId = text
                            "Direccion" -> address = text
                            "Telefono" -> phone = text
                            "Regimen" -> taxRegime = text
                            "RepresentanteLegal" -> legalRepresentative = text
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
                            "Notas" -> if (inItem) lineNotes = text
                            "Articulos" -> articleCount = text.toIntOrNull() ?: 0
                            "Total" -> total = text.toDoubleOrNull() ?: 0.0
                            "Metodo" -> if (inPayment) payMethod = text else paymentMethod = text
                            "Monto" -> if (inPayment) payAmount = text.toDoubleOrNull() ?: 0.0
                            "Entregado" -> amountTendered = text.toDoubleOrNull() ?: 0.0
                            "Cambio" -> changeDue = text.toDoubleOrNull() ?: 0.0
                            "Descuento" -> discountAmount = text.toDoubleOrNull() ?: 0.0
                            "Propina" -> tipAmount = text.toDoubleOrNull() ?: 0.0
                            "ImpoconsumoPorcentaje" -> impoconsumoPercent = text.toDoubleOrNull() ?: 0.0
                            "BaseImpoconsumo" -> impoconsumoBase = text.toDoubleOrNull() ?: 0.0
                            "Impoconsumo" -> if (!inItem) impoconsumo = text.toDoubleOrNull() ?: 0.0
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "Item" -> {
                            lines += ReceiptLine(lineDesc, lineQty, linePrice, lineTotal, lineNotes)
                            lineDesc = ""
                            lineQty = 0.0
                            linePrice = 0.0
                            lineTotal = 0.0
                            lineNotes = ""
                            inItem = false
                        }
                        "Pago" -> {
                            if (payMethod.isNotBlank() || payAmount > 0) {
                                payments += ReceiptPayment(payMethod, payAmount)
                            }
                            payMethod = ""
                            payAmount = 0.0
                            inPayment = false
                        }
                    }
                    tag = ""
                }
            }
            event = parser.next()
        }

        return SalesReceipt(
            tradeName = tradeName,
            legalName = legalName,
            taxId = taxId,
            address = address,
            phone = phone,
            taxRegime = taxRegime,
            legalRepresentative = legalRepresentative,
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
            payments = payments,
            paymentMethod = paymentMethod,
            amountTendered = amountTendered,
            changeDue = changeDue,
            discountAmount = discountAmount,
            tipAmount = tipAmount,
            impoconsumoPercent = impoconsumoPercent,
            impoconsumoBase = impoconsumoBase,
            impoconsumo = impoconsumo,
            resolutionNumber = resolutionNumber,
            rangeFrom = rangeFrom,
            rangeTo = rangeTo,
        )
    }
}
