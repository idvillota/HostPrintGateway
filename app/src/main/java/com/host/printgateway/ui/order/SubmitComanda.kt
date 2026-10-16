package com.host.printgateway.ui.order

import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.OrderDraftLine
import com.host.printgateway.data.RestaurantDao
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.printer.ConfiguredPrint
import com.host.printgateway.printer.CopyResult

sealed interface SubmitOutcome {
    data class Printed(val orderId: String) : SubmitOutcome
    data class NeedsRetry(val orderId: String, val reason: String) : SubmitOutcome
    data class Failed(val message: String) : SubmitOutcome
}

class SubmitComanda(
    private val repository: RestaurantRepository,
    private val dao: RestaurantDao,
) {
    suspend fun send(
        existingOrderId: String?,
        table: DiningTableEntity,
        waiterName: String,
        deviceId: String,
        lines: List<OrderDraftLine>,
        printerMac: String,
    ): SubmitOutcome {
        return try {
            val orderId = existingOrderId ?: repository.createOrder(
                table = table,
                waiterName = waiterName,
                deviceId = deviceId,
                lines = lines,
            )
            val ticket = dao.getTicketForOrder(orderId)
                ?: return SubmitOutcome.Failed("No se generó el XML de la comanda")
            if (printerMac.isBlank() && dao.getLocalPrinters().none { it.mac.isNotBlank() }) {
                return SubmitOutcome.NeedsRetry(
                    orderId,
                    "Indica la MAC de la impresora en Configuración.",
                )
            }
            if (ticket.isPrinted) {
                return SubmitOutcome.Printed(orderId)
            }
            if (dao.claimTicketForPrinting(ticket.id) == 0) {
                val latest = dao.getTicketForOrder(orderId)
                return if (latest?.isPrinted == true) {
                    SubmitOutcome.Printed(orderId)
                } else {
                    SubmitOutcome.NeedsRetry(orderId, "La comanda ya se está imprimiendo.")
                }
            }
            when (val printed = ConfiguredPrint.printKitchen(dao, printerMac, ticket.payload)) {
                CopyResult.Printed -> {
                    dao.markTicketPrinted(ticket.id, System.currentTimeMillis())
                    SubmitOutcome.Printed(orderId)
                }
                is CopyResult.Invalid -> {
                    dao.markTicketFailed(ticket.id, printed.message)
                    SubmitOutcome.NeedsRetry(orderId, printed.message)
                }
                is CopyResult.Failed -> {
                    dao.markTicketFailed(ticket.id, printed.message)
                    SubmitOutcome.NeedsRetry(orderId, printed.message)
                }
            }
        } catch (error: Exception) {
            SubmitOutcome.Failed(error.message ?: "No se pudo crear la orden")
        }
    }
}
