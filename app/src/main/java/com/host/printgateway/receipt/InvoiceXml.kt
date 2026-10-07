package com.host.printgateway.receipt

import android.util.Xml
import com.host.printgateway.printer.PrintPipeline
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class InvoiceDraft(
    val invoiceNumber: String,
    val tableCode: String,
    val cashier: String,
    val lines: List<ReceiptLine>,
    val articleCount: Int,
    val foodTotal: Double,
    val taxAmount: Double,
    val tip: Double,
    val paymentMethod: String,
    val splitNote: String,
)

object InvoiceXml {

    fun write(draft: InvoiceDraft): String {
        val writer = StringWriter()
        val serializer = Xml.newSerializer()
        serializer.setOutput(writer)
        serializer.startDocument("UTF-8", true)
        serializer.startTag("", "Factura")
        text(serializer, "NombreComercial", "Host")
        text(serializer, "NumeroFactura", draft.invoiceNumber)
        text(serializer, "ConsecutivoDIAN", "")
        text(serializer, "FechaHora", now())
        text(serializer, "Mesas", draft.tableCode)
        text(serializer, "Cajero", draft.cashier)
        text(serializer, "Nombre", "Consumidor final")
        text(serializer, "Identificacion", "222222222222")
        serializer.startTag("", "Items")
        for (line in draft.lines) {
            serializer.startTag("", "Item")
            text(serializer, "Descripcion", line.description)
            text(serializer, "Cantidad", formatQuantity(line.quantity))
            text(serializer, "PrecioUnitario", line.unitPrice.toString())
            text(serializer, "TotalLinea", line.lineTotal.toString())
            serializer.endTag("", "Item")
        }
        if (draft.tip > 0.0) {
            serializer.startTag("", "Item")
            text(serializer, "Descripcion", "Propina")
            text(serializer, "Cantidad", "1")
            text(serializer, "PrecioUnitario", draft.tip.toString())
            text(serializer, "TotalLinea", draft.tip.toString())
            serializer.endTag("", "Item")
        }
        serializer.endTag("", "Items")
        val total = draft.foodTotal + draft.tip
        text(serializer, "Articulos", draft.articleCount.toString())
        text(serializer, "Total", total.toString())
        val method = if (draft.splitNote.isBlank()) {
            draft.paymentMethod
        } else {
            "${draft.paymentMethod} · ${draft.splitNote}"
        }
        text(serializer, "Metodo", method)
        text(serializer, "Entregado", total.toString())
        text(serializer, "BaseImpoconsumo", (draft.foodTotal - draft.taxAmount).toString())
        text(serializer, "Impoconsumo", draft.taxAmount.toString())
        text(serializer, "Numero", "")
        text(serializer, "RangoDesde", "")
        text(serializer, "RangoHasta", "")
        serializer.endTag("", "Factura")
        serializer.endDocument()
        return writer.toString()
    }

    suspend fun print(xml: String, printerMac: String): Result<Unit> =
        PrintPipeline.printSalesReceiptXml(xml, printerMac)

    private fun text(serializer: org.xmlpull.v1.XmlSerializer, tag: String, value: String) {
        serializer.startTag("", tag)
        serializer.text(value)
        serializer.endTag("", tag)
    }

    private fun formatQuantity(quantity: Double): String {
        return if (quantity % 1.0 == 0.0) quantity.toLong().toString() else quantity.toString()
    }

    private fun now(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }
}
