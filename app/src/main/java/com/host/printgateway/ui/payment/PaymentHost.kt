package com.host.printgateway.ui.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.host.printgateway.data.OrderEntity
import com.host.printgateway.data.OrderItemEntity
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.receipt.InvoiceDraft
import com.host.printgateway.receipt.InvoiceXml
import com.host.printgateway.receipt.ReceiptLine
import com.host.printgateway.ui.components.AppScaffold
import com.host.printgateway.ui.navigation.PaymentStep
import com.host.printgateway.ui.order.formatPrice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.abs

@Composable
fun PaymentHost(
    step: PaymentStep,
    printerMac: String,
    database: PrintGatewayDatabase,
    onStep: (PaymentStep) -> Unit,
    onLeave: () -> Unit,
    onOpenSettings: () -> Unit,
    catalogRevision: Int,
) {
    val scope = rememberCoroutineScope()
    val dao = remember(database) { database.restaurantDao() }
    var orders by remember { mutableStateOf(emptyList<OrderEntity>()) }
    var itemsByOrder by remember { mutableStateOf(emptyMap<String, List<OrderItemEntity>>()) }
    var notice by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }

    val billKey = (step as? PaymentStep.Bill)?.tableKey
    var tipText by remember(billKey) { mutableStateOf("0") }
    var paymentMethod by remember(billKey) { mutableStateOf("Efectivo") }
    var splitMode by remember(billKey) { mutableStateOf(SplitMode.None) }
    var equalParts by remember(billKey) { mutableStateOf(2) }
    var customParts by remember(billKey) { mutableStateOf(listOf("", "")) }

    suspend fun reload() {
        val unpaid = dao.getUnpaidOrders()
        val items = unpaid.associate { order -> order.id to dao.getOrderItems(order.id) }
        orders = unpaid
        itemsByOrder = items
    }

    LaunchedEffect(catalogRevision) {
        reload()
    }

    val title = if (step is PaymentStep.Bill) "Cobro" else "Pagos"
    val onBack = if (step is PaymentStep.Bill) {
        { notice = ""; onStep(PaymentStep.Accounts) }
    } else {
        onLeave
    }

    AppScaffold(
        title = title,
        onBack = onBack,
        onSettings = if (step is PaymentStep.Accounts) onOpenSettings else null,
    ) { padding ->
        when (step) {
            PaymentStep.Accounts -> PaymentAccountsScreen(
                padding = padding,
                accounts = summariesOf(orders),
                notice = notice,
                onAccount = { account ->
                    notice = ""
                    onStep(PaymentStep.Bill(account.tableKey))
                },
            )

            is PaymentStep.Bill -> {
                val bill = billOf(step.tableKey, orders, itemsByOrder)
                PaymentBillScreen(
                    padding = padding,
                    bill = bill,
                    tipText = tipText,
                    paymentMethod = paymentMethod,
                    splitMode = splitMode,
                    equalParts = equalParts,
                    customParts = customParts,
                    notice = notice,
                    sending = sending,
                    onTipChange = { tipText = it },
                    onTipPercent = { percent ->
                        val food = bill?.foodTotal ?: 0.0
                        tipText = moneyInput(food * percent / 100.0)
                    },
                    onPaymentMethod = { paymentMethod = it },
                    onSplitMode = { splitMode = it },
                    onEqualParts = { equalParts = it },
                    onCustomPart = { index, value ->
                        customParts = customParts.toMutableList().also { parts ->
                            parts[index] = value
                        }
                    },
                    onAddCustomPart = { customParts = customParts + "" },
                    onRemoveCustomPart = {
                        if (customParts.size > 2) {
                            customParts = customParts.dropLast(1)
                        }
                    },
                    onPrint = {
                        val current = bill ?: return@PaymentBillScreen
                        val tip = parseAmount(tipText)
                        if (tip == null) {
                            notice = "La propina no es válida."
                            return@PaymentBillScreen
                        }
                        val due = current.foodTotal + tip
                        val splitNote = splitNote(splitMode, equalParts, customParts, due)
                        if (splitNote == null) {
                            notice = "Las partes personalizadas deben sumar el total."
                            return@PaymentBillScreen
                        }
                        sending = true
                        notice = "Imprimiendo factura…"
                        scope.launch {
                            val draft = InvoiceDraft(
                                invoiceNumber = "F${System.currentTimeMillis().toString().takeLast(6)}",
                                tableCode = current.tableCode,
                                cashier = current.cashier,
                                lines = current.lines.map {
                                    ReceiptLine(it.description, it.quantity, it.unitPrice, it.lineTotal)
                                },
                                articleCount = current.articleCount,
                                foodTotal = current.foodTotal,
                                taxAmount = current.taxAmount,
                                tip = tip,
                                paymentMethod = paymentMethod,
                                splitNote = splitNote,
                            )
                            val xml = InvoiceXml.write(draft)
                            val printed = withContext(Dispatchers.IO) {
                                InvoiceXml.print(xml, printerMac)
                            }
                            if (printed.isSuccess) {
                                withContext(Dispatchers.IO) {
                                    dao.markOrdersPaid(current.orderIds, System.currentTimeMillis())
                                }
                                reload()
                                sending = false
                                notice = "Factura enviada a la impresora."
                                onStep(PaymentStep.Accounts)
                            } else {
                                sending = false
                                notice = printed.exceptionOrNull()?.message ?: "No se pudo imprimir la factura"
                            }
                        }
                    },
                )
            }
        }
    }
}

private fun splitNote(
    mode: SplitMode,
    equalParts: Int,
    customParts: List<String>,
    due: Double,
): String? {
    return when (mode) {
        SplitMode.None -> ""
        SplitMode.Equal -> "$equalParts partes iguales de ${formatPrice(due / equalParts)}"
        SplitMode.Custom -> {
            val amounts = customParts.map { parseAmount(it) }
            if (amounts.any { it == null || it <= 0.0 }) return null
            val values = amounts.filterNotNull()
            if (abs(due - values.sum()) >= 0.5) return null
            values.joinToString(" + ") { formatPrice(it) }
        }
    }
}

private fun moneyInput(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toLong().toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}
