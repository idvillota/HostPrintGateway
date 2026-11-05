package com.host.printgateway.data

/**
 * Chooses which configured printer receives each kitchen line or the invoice.
 * Kitchen lines go only to printers that marked that category or "all kitchen".
 * Invoices go only to printers marked for receipts.
 */
internal object PrinterRouting {

    fun kitchenCopies(categoryIds: List<String>, printers: List<PrinterTarget>): List<KitchenCopy> {
        val active = printers.filter { it.mac.isNotBlank() }
        if (active.isEmpty() || categoryIds.isEmpty()) return emptyList()

        val indexesByMac = linkedMapOf<String, MutableSet<Int>>()
        categoryIds.forEachIndexed { index, categoryId ->
            val key = categoryId.trim().lowercase()
            active.filter { printer -> printer.acceptsKitchenLine(key) }.forEach { printer ->
                indexesByMac.getOrPut(printer.mac) { linkedSetOf() }.add(index)
            }
        }
        return indexesByMac.map { (mac, indexes) -> KitchenCopy(mac, indexes) }
    }

    fun invoiceMacs(printers: List<PrinterTarget>): List<String> {
        return printers
            .filter { it.mac.isNotBlank() && it.printsInvoices }
            .map { it.mac }
            .distinct()
    }
}

internal data class PrinterTarget(
    val id: String,
    val mac: String,
    val categoryIds: Set<String>,
    val printsInvoices: Boolean,
    val printsAllKitchen: Boolean = false,
) {
    fun acceptsKitchenLine(categoryId: String): Boolean {
        if (printsAllKitchen) return true
        return categoryId.isNotEmpty() && categoryId in categoryIds
    }
}

internal data class KitchenCopy(
    val mac: String,
    val lineIndexes: Set<Int>,
)
