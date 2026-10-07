package com.host.printgateway.data

import android.util.Xml
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Builds kitchen-ticket XML stored in Room and printed via ESC/POS.
 * Same document shape as before (no backend required offline).
 */
object KitchenTicketXmlBuilder {

    fun build(
        table: DiningTableEntity,
        orderNumber: String,
        waiterName: String,
        printer: PrinterStationEntity?,
        lines: List<OrderDraftLine>,
    ): String {
        val writer = StringWriter()
        val serializer = Xml.newSerializer()
        serializer.setOutput(writer)
        serializer.startDocument("UTF-8", true)
        serializer.startTag("", "KitchenTicket")

        writeXmlValue(serializer, "TableCode", table.code)
        writeXmlValue(serializer, "OrderNumber", orderNumber)
        writeXmlValue(serializer, "SentBy", waiterName)
        writeXmlValue(serializer, "SentAtUtc", utcNow())

        serializer.startTag("", "PrinterStation")
        writeXmlValue(serializer, "Name", printer?.name ?: "")
        writeXmlValue(serializer, "Code", printer?.code ?: "")
        serializer.endTag("", "PrinterStation")

        serializer.startTag("", "Lines")
        for (item in lines) {
            serializer.startTag("", "Line")
            writeXmlValue(serializer, "ProductName", item.productName)
            writeXmlValue(serializer, "Quantity", formatQuantity(item.quantity))
            writeXmlValue(serializer, "Notes", item.notes)
            serializer.startTag("", "ExcludedIngredients")
            for (ingredient in item.excludedIngredients) {
                writeXmlValue(serializer, "Ingredient", ingredient)
            }
            serializer.endTag("", "ExcludedIngredients")
            serializer.endTag("", "Line")
        }
        serializer.endTag("", "Lines")

        writeXmlValue(serializer, "IsCancellation", "false")
        writeXmlValue(serializer, "CancelReason", "")
        serializer.endTag("", "KitchenTicket")
        serializer.endDocument()
        return writer.toString()
    }

    private fun writeXmlValue(
        serializer: org.xmlpull.v1.XmlSerializer,
        tagName: String,
        value: String,
    ) {
        serializer.startTag("", tagName)
        serializer.text(value)
        serializer.endTag("", tagName)
    }

    private fun formatQuantity(quantity: Double): String =
        if (quantity % 1.0 == 0.0) quantity.toLong().toString() else quantity.toString()

    private fun utcNow(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date())
    }
}
