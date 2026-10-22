package com.host.printgateway.ui.payment

import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.OrderEntity
import com.host.printgateway.data.OrderItemEntity
import kotlin.math.round

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

data class TransferQr(
    val amount: Double,
    val shares: Int,
)

fun parseAmount(raw: String): Double? {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return 0.0
    val normalized = normalizeMoney(trimmed) ?: return null
    if (normalized.isEmpty()) return 0.0
    return normalized.toDoubleOrNull()?.takeIf { it >= 0.0 }
}

/** Digits and one decimal comma. Thousand dots from a paste are removed. */
fun sanitizeMoneyTyping(raw: String): String {
    val filtered = raw.filter { it.isDigit() || it == '.' || it == ',' }
    if (filtered.isEmpty()) return ""
    val body = if (',' in filtered) {
        val plain = filtered.replace(".", "")
        val comma = plain.indexOf(',')
        val head = plain.substring(0, comma).filter { it.isDigit() }
        val tail = plain.substring(comma + 1).filter { it.isDigit() }.take(2)
        head + "," + tail
    } else if ('.' in filtered) {
        val parts = filtered.split('.')
        val grouped = parts.size > 1 && parts.drop(1).all { it.length == 3 }
        if (grouped) {
            parts.joinToString("").filter { it.isDigit() }
        } else if (parts.size == 2) {
            parts[0].filter { it.isDigit() } + "," + parts[1].filter { it.isDigit() }.take(2)
        } else {
            filtered.filter { it.isDigit() }
        }
    } else {
        filtered.filter { it.isDigit() }
    }
    val trimmedZero = body.trimStart('0')
    return when {
        trimmedZero.isEmpty() -> "0"
        trimmedZero.startsWith(',') -> "0$trimmedZero"
        else -> trimmedZero
    }
}

/** Shows thousand dots while the stored text stays digits plus an optional comma. */
fun groupedMoneyDisplay(raw: String): String {
    val comma = raw.indexOf(',')
    val integer = if (comma >= 0) raw.substring(0, comma) else raw
    val fraction = if (comma >= 0) raw.substring(comma) else ""
    if (integer.length <= 3) return raw
    val grouped = integer.reversed().chunked(3).joinToString(".").reversed()
    return grouped + fraction
}

fun transferQrAmounts(
    due: Double,
    mode: SplitMode,
    equalParts: Int,
    customParts: List<String>,
): List<TransferQr> {
    if (due <= 0.0) return emptyList()
    val amounts = when (mode) {
        SplitMode.None -> listOf(roundMoney(due))
        SplitMode.Equal -> equalShares(due, equalParts)
        SplitMode.Custom -> customParts.mapNotNull { parseAmount(it) }.filter { it > 0.0 }.map(::roundMoney)
    }
    val grouped = linkedMapOf<Long, Int>()
    for (amount in amounts) {
        val cents = round(amount * 100.0).toLong()
        if (cents <= 0L) continue
        grouped[cents] = (grouped[cents] ?: 0) + 1
    }
    return grouped.map { (cents, shares) -> TransferQr(cents / 100.0, shares) }
}

private fun equalShares(due: Double, parts: Int): List<Double> {
    val count = parts.coerceIn(2, 20)
    val totalCents = round(due * 100.0).toLong()
    if (totalCents <= 0L) return emptyList()
    val base = totalCents / count
    val remainder = (totalCents % count).toInt()
    return List(count) { index ->
        (base + if (index == count - 1) remainder else 0) / 100.0
    }
}

private fun roundMoney(value: Double): Double = round(value * 100.0) / 100.0

private fun normalizeMoney(raw: String): String? {
    if (raw.any { !it.isDigit() && it != '.' && it != ',' }) return null
    val canonical = if (',' in raw) {
        if (raw.count { it == ',' } > 1) return null
        raw.replace(".", "").replace(',', '.')
    } else if ('.' in raw) {
        val parts = raw.split('.')
        val grouped = parts.size > 1 && parts.drop(1).all { it.length == 3 }
        if (grouped) parts.joinToString("") else raw
    } else {
        raw
    }
    if (canonical.count { it == '.' } > 1) return null
    return canonical
}
