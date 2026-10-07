package com.host.printgateway.printer

import com.host.printgateway.network.PrintJobDto
import com.host.printgateway.receipt.SalesReceiptXmlParser

/**
 * Single entry for ESC/POS rendering + Bluetooth send.
 *
 * Does not own Room queues or cloud ACK — callers keep their existing
 * submit-now vs gateway-service workflows.
 */
object PrintPipeline {

    private val kitchenFormatter = KitchenTicketFormatter()
    private val receiptFormatter = EscPosReceiptFormatter()
    private val jobRenderer = PrintJobRenderer(receiptFormatter)

    fun requirePrinterMac(printerMac: String): Result<String> {
        val mac = printerMac.trim()
        if (mac.isBlank()) {
            return Result.failure(
                IllegalStateException("Indica la MAC de la impresora en Configuración."),
            )
        }
        return Result.success(mac)
    }

    fun renderKitchenTicketXml(xml: String): Result<ByteArray> =
        runCatching { kitchenFormatter.format(xml) }

    fun renderSalesReceiptXml(xml: String): Result<ByteArray> =
        runCatching { receiptFormatter.format(SalesReceiptXmlParser.parse(xml)) }

    fun renderCloudJob(job: PrintJobDto): Result<ByteArray> =
        runCatching { jobRenderer.toEscPos(job) }

    suspend fun printEscPosBytes(bytes: ByteArray, printerMac: String): Result<Unit> {
        val mac = requirePrinterMac(printerMac).getOrElse { return Result.failure(it) }
        return BluetoothEscPosPrinter(mac).print(bytes)
    }

    suspend fun printKitchenTicketXml(xml: String, printerMac: String): Result<Unit> {
        val bytes = renderKitchenTicketXml(xml).getOrElse { return Result.failure(it) }
        return printEscPosBytes(bytes, printerMac)
    }

    suspend fun printSalesReceiptXml(xml: String, printerMac: String): Result<Unit> {
        val bytes = renderSalesReceiptXml(xml).getOrElse { return Result.failure(it) }
        return printEscPosBytes(bytes, printerMac)
    }

    suspend fun printCloudJob(job: PrintJobDto, printerMac: String): Result<Unit> {
        val bytes = renderCloudJob(job).getOrElse { return Result.failure(it) }
        return printEscPosBytes(bytes, printerMac)
    }

    suspend fun printTestTicket(printerMac: String): Result<Unit> {
        val mac = requirePrinterMac(printerMac).getOrElse { return Result.failure(it) }
        val bytes = receiptFormatter.formatTestTicket(mac)
        return BluetoothEscPosPrinter(mac).print(bytes)
    }
}
