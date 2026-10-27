package com.host.printgateway.data

/**
 * Chooses which configured printer receives each kitchen line or the invoice.
 * A line prints only on printers that marked that category.
 * A printer with no categories still receives leftover lines nobody marked.
 * An unmarked invoice printer does not print the receipt.
 */
internal object PrinterRouting {

    fun kitchenCopies(categoryIds: List<String>, printers: List<PrinterTarget>): List<KitchenCopy> {
        val active = printers.filter { it.mac.isNotBlank() }
        if (active.isEmpty() || categoryIds.isEmpty()) return emptyList()

        val catchAll = active.filter { it.categoryIds.isEmpty() }
        val indexesByMac = linkedMapOf<String, MutableSet<Int>>()
        categoryIds.forEachIndexed { index, categoryId ->
            val key = categoryId.trim().lowercase()
            val matches = if (key.isNotEmpty()) {
                active.filter { key in it.categoryIds }
            } else {
                emptyList()
            }
            val targets = when {
                matches.isNotEmpty() -> matches
                catchAll.isNotEmpty() -> catchAll
                else -> emptyList()
            }
            targets.forEach { printer ->
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
)

internal data class KitchenCopy(
    val mac: String,
    val lineIndexes: Set<Int>,
)
