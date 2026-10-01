package com.host.printgateway.ui.payment

import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.OrderEntity
import com.host.printgateway.data.OrderItemEntity

data class TableAccountSummary(
    val tableKey: String,
    val tableCode: String,
    val orderCount: Int,
    val total: Double,
)

data class BillLine(
    val description: String,
    val quantity: Double,
    val unitPrice: Double,
    val lineTotal: Double,
)

data class TableBill(
    val tableKey: String,
    val tableCode: String,
    val orderIds: List<String>,
    val cashier: String,
    val lines: List<BillLine>,
    val foodTotal: Double,
    val taxAmount: Double,
    val articleCount: Int,
)

enum class SplitMode {
    None,
    Equal,
    Custom,
}

fun accountsOf(
    tables: List<DiningTableEntity>,
    orders: List<OrderEntity>,
): List<TableAccountSummary> {
    val summaries = summariesOf(orders).associateBy { it.tableKey }
    val keys = linkedSetOf<String>()
    tables.forEach { table ->
        if (table.isOccupied() || orders.any { it.diningTableId == table.id }) {
            keys += table.id
        }
    }
    orders.forEach { order ->
        val key = order.diningTableId ?: order.diningTableCode
        if (key !in keys) keys += key
    }
    return keys.map { key ->
        summaries[key] ?: TableAccountSummary(
            tableKey = key,
            tableCode = tables.firstOrNull { it.id == key }?.code ?: key,
            orderCount = 0,
            total = 0.0,
        )
    }.sortedBy { it.tableCode }
}

fun summariesOf(orders: List<OrderEntity>): List<TableAccountSummary> {
    return orders
        .groupBy { it.diningTableId ?: it.diningTableCode }
        .map { (key, tableOrders) ->
            TableAccountSummary(
                tableKey = key,
                tableCode = tableOrders.first().diningTableCode,
                orderCount = tableOrders.size,
                total = tableOrders.sumOf { it.total },
            )
        }
        .sortedBy { it.tableCode }
}

fun billOf(
    tableKey: String,
    orders: List<OrderEntity>,
    itemsByOrder: Map<String, List<OrderItemEntity>>,
): TableBill? {
    val tableOrders = orders.filter { (it.diningTableId ?: it.diningTableCode) == tableKey }
    if (tableOrders.isEmpty()) return null
    val lines = tableOrders.flatMap { order ->
        itemsByOrder[order.id].orEmpty().map { item ->
            val description = if (item.notes.isBlank()) {
                item.productName
            } else {
                "${item.productName} (${item.notes})"
            }
            BillLine(
                description = description,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                lineTotal = item.lineTotal,
            )
        }
    }
    return TableBill(
        tableKey = tableKey,
        tableCode = tableOrders.first().diningTableCode,
        orderIds = tableOrders.map { it.id },
        cashier = tableOrders.last().waiterName.ifBlank { "Caja" },
        lines = lines,
        foodTotal = tableOrders.sumOf { it.total },
        taxAmount = tableOrders.sumOf { it.taxAmount },
        articleCount = lines.sumOf { it.quantity.toInt().coerceAtLeast(1) },
    )
}

fun parseAmount(raw: String): Double? {
    val cleaned = raw.trim().replace(',', '.')
    if (cleaned.isEmpty()) return 0.0
    return cleaned.toDoubleOrNull()?.takeIf { it >= 0.0 }
}
