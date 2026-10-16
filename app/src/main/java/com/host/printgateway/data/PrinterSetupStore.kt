package com.host.printgateway.data

import java.util.UUID

/**
 * Phone-owned printers. Catalog sync replaces HOST stations and must not touch these rows.
 */
class PrinterSetupStore(
    private val dao: RestaurantDao,
) {
    suspend fun ensureLegacy(fallbackMac: String) {
        if (dao.getLocalPrinters().isNotEmpty()) return
        val mac = fallbackMac.trim()
        if (mac.isBlank()) return
        dao.upsertLocalPrinter(
            LocalPrinterEntity(
                id = UUID.randomUUID().toString(),
                name = "Impresora",
                mac = mac,
                printsInvoices = true,
                sortOrder = 0,
            ),
        )
    }

    internal suspend fun targets(fallbackMac: String): List<PrinterTarget> {
        val printers = dao.getLocalPrinters()
        if (printers.isEmpty()) {
            val mac = fallbackMac.trim()
            if (mac.isBlank()) return emptyList()
            return listOf(PrinterTarget(id = "legacy", mac = mac, categoryIds = emptySet(), printsInvoices = true))
        }
        val categories = dao.getLocalPrinterCategories()
            .groupBy { it.printerId }
        return printers.map { printer ->
            PrinterTarget(
                id = printer.id,
                mac = printer.mac.trim(),
                categoryIds = categories[printer.id].orEmpty()
                    .map { it.productTypeId.trim().lowercase() }
                    .filter { it.isNotEmpty() }
                    .toSet(),
                printsInvoices = printer.printsInvoices,
            )
        }
    }

    suspend fun load(): PrinterSetup {
        val types = dao.getProductTypes()
        val printers = dao.getLocalPrinters()
        val categories = dao.getLocalPrinterCategories().groupBy { it.printerId }
        return PrinterSetup(
            types = types,
            printers = printers.map { printer ->
                printer to categories[printer.id].orEmpty().map { it.productTypeId }.toSet()
            },
        )
    }

    suspend fun save(printer: LocalPrinterEntity, categoryIds: Set<String>) {
        dao.saveLocalPrinter(printer, categoryIds.toList())
    }

    suspend fun delete(printerId: String) {
        dao.deleteLocalPrinterCategories(printerId)
        dao.deleteLocalPrinter(printerId)
    }

    suspend fun mirrorMac(): String =
        dao.getLocalPrinters().firstOrNull { it.mac.isNotBlank() }?.mac.orEmpty()
}

data class PrinterSetup(
    val types: List<ProductTypeEntity>,
    val printers: List<Pair<LocalPrinterEntity, Set<String>>>,
)
