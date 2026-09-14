package com.host.printgateway.receipt

data class ReceiptLine(
    val description: String,
    val quantity: Double,
    val unitPrice: Double,
    val lineTotal: Double,
)

data class SalesReceipt(
    val tradeName: String = "",
    val invoiceNumber: String = "",
    val dianConsecutive: String = "",
    val dateTime: String = "",
    val tableCodes: String = "",
    val cashier: String = "",
    val customerName: String = "",
    val customerId: String = "",
    val lines: List<ReceiptLine> = emptyList(),
    val articleCount: Int = 0,
    val total: Double = 0.0,
    val paymentMethod: String = "",
    val amountTendered: Double = 0.0,
    val impoconsumoBase: Double = 0.0,
    val impoconsumo: Double = 0.0,
    val resolutionNumber: String = "",
    val rangeFrom: String = "",
    val rangeTo: String = "",
)
