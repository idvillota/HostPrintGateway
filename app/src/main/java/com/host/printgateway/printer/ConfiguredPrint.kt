package com.host.printgateway.printer

import com.host.printgateway.data.PrinterRouting
import com.host.printgateway.data.PrinterSetupStore
import com.host.printgateway.data.RestaurantDao

/**
 * Sends one HOST or local ticket to the printers selected for its categories or for invoices.
 */
object ConfiguredPrint {

    suspend fun printKitchen(
        dao: RestaurantDao,
        fallbackMac: String,
        payload: String,
        printKitchenTickets: Boolean = true,
    ): CopyResult {
        if (!printKitchenTickets) return CopyResult.Printed
        val formatter = KitchenTicketFormatter()
        val categories = runCatching { formatter.categoryIds(payload) }.getOrElse {
            return CopyResult.Invalid(it.message ?: "XML inválido")
        }
        val printers = PrinterSetupStore(dao).targets(fallbackMac)
        val copies = PrinterRouting.kitchenCopies(categories, printers)
        if (copies.isEmpty()) {
            if (printers.any { it.categoryIds.isNotEmpty() }) {
                return CopyResult.Printed
            }
            return PrintPipeline.printKitchenTicketXml(payload, fallbackMac).toCopyResult()
        }
        var failure: String? = null
        for (copy in copies) {
            val bytes = formatter.format(payload, copy.lineIndexes)
            val printed = PrintPipeline.printEscPosBytes(bytes, copy.mac)
            if (printed.isFailure && failure == null) {
                failure = printed.exceptionOrNull()?.message ?: "No se pudo imprimir"
            }
        }
        return failure?.let { CopyResult.Failed(it) } ?: CopyResult.Printed
    }

    suspend fun printInvoice(dao: RestaurantDao, fallbackMac: String, bytes: ByteArray): CopyResult {
        val printers = PrinterSetupStore(dao).targets(fallbackMac)
        val macs = PrinterRouting.invoiceMacs(printers)
        if (macs.isEmpty()) {
            if (printers.isNotEmpty()) {
                return CopyResult.Failed("Marca Facturas en la impresora que debe imprimir el recibo.")
            }
            val fallback = fallbackMac.trim()
            if (fallback.isEmpty()) {
                return CopyResult.Failed("Indica la MAC de la impresora en Configuración.")
            }
            return PrintPipeline.printEscPosBytes(bytes, fallback).toCopyResult()
        }
        var failure: String? = null
        for (mac in macs) {
            val printed = PrintPipeline.printEscPosBytes(bytes, mac)
            if (printed.isFailure && failure == null) {
                failure = printed.exceptionOrNull()?.message ?: "No se pudo imprimir"
            }
        }
        return failure?.let { CopyResult.Failed(it) } ?: CopyResult.Printed
    }

    private fun Result<Unit>.toCopyResult(): CopyResult =
        if (isSuccess) {
            CopyResult.Printed
        } else {
            CopyResult.Failed(exceptionOrNull()?.message ?: "No se pudo imprimir")
        }
}

sealed interface CopyResult {
    data object Printed : CopyResult
    data class Invalid(val message: String) : CopyResult
    data class Failed(val message: String) : CopyResult
}
