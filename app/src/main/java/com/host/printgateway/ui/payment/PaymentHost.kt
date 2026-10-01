package com.host.printgateway.ui.payment

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.OrderEntity
import com.host.printgateway.data.OrderItemEntity
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.network.SyncUnauthorized
import com.host.printgateway.receipt.InvoiceDraft
import com.host.printgateway.receipt.InvoiceXml
import com.host.printgateway.receipt.ReceiptLine
import com.host.printgateway.ui.SessionRenewalNotifier
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
    apiUrl: String,
    deviceToken: String,
    printerMac: String,
    database: PrintGatewayDatabase,
    settings: GatewaySettings,
    onStep: (PaymentStep) -> Unit,
    onLeave: () -> Unit,
    onOpenSettings: () -> Unit,
    catalogRevision: Int,
    offlineMode: Boolean,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember(database) { database.restaurantDao() }
    val repository = remember(database, deviceToken) { RestaurantRepository(database, deviceToken) }
    var tables by remember { mutableStateOf(emptyList<DiningTableEntity>()) }
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
        tables = dao.getTables()
        orders = unpaid
        itemsByOrder = items
    }

    LaunchedEffect(catalogRevision, offlineMode) {
        if (!offlineMode && apiUrl.isNotBlank() && deviceToken.isNotBlank()) {
            val deviceId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID,
            ) ?: "unknown-device"
            val caughtUp = withContext(Dispatchers.IO) {
                val catalog = repository.syncCatalog(apiUrl)
                if (catalog.isSuccess) repository.closeOrdersOnFreeHostTables()
                repository.releaseTablesSettledOnHost(apiUrl)
                repository.catchUpOpenAccounts(apiUrl, deviceId, settings.saleCursor)
            }
            caughtUp.onSuccess { cursor ->
                if (cursor.isNotBlank()) settings.saleCursor = cursor
            }
            if (caughtUp.exceptionOrNull() is SyncUnauthorized) {
                SessionRenewalNotifier.show(context)
            }
        }
        reload()
    }

    fun settle(current: TableBill, printInvoice: Boolean) {
        if (current.lines.isEmpty()) return
        val tip = parseAmount(tipText)
        if (tip == null) {
            notice = "La propina no es válida."
            return
        }
        val due = current.foodTotal + tip
        val split = splitNote(splitMode, equalParts, customParts, due)
        if (split == null) {
            notice = "Las partes personalizadas deben sumar el total."
            return
        }
        sending = true
        notice = if (printInvoice) "Imprimiendo factura…" else "Cobrando…"
        scope.launch {
            val printed = if (printInvoice) {
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
                    splitNote = split,
                )
                val xml = InvoiceXml.write(draft)
                withContext(Dispatchers.IO) { InvoiceXml.print(xml, printerMac) }
            } else {
                Result.success(Unit)
            }
            if (printed.isFailure) {
                sending = false
                notice = printed.exceptionOrNull()?.message ?: "No se pudo imprimir la factura"
                return@launch
            }
            val deviceId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID,
            ) ?: "unknown-device"
            val uploaded = withContext(Dispatchers.IO) {
                repository.markPaidForSync(current.orderIds, paymentMethod, tip)
                when {
                    offlineMode -> Result.success(0)
                    apiUrl.isBlank() || deviceToken.isBlank() ->
                        Result.failure(IllegalStateException("No hay conexión con HOST."))
                    else -> repository.syncPendingOrders(apiUrl, deviceId)
                }
            }
            reload()
            sending = false
            notice = when {
                printInvoice && uploaded.isSuccess -> "Factura enviada a la impresora."
                printInvoice -> "Factura impresa. ${uploaded.exceptionOrNull()?.message ?: "No se pudo cobrar en HOST."}"
                uploaded.isSuccess -> "Cobro registrado."
                else -> uploaded.exceptionOrNull()?.message ?: "No se pudo cobrar en HOST."
            }
            onStep(PaymentStep.Accounts)
        }
    }

    val title = if (step is PaymentStep.Bill) "Cobro" else "Pagos"
    val onBack = if (step is PaymentStep.Bill) {
        { notice = ""; onStep(PaymentStep.Accounts) }
    } else {
        onLeave
    }

    BackHandler(onBack = onBack)

    AppScaffold(
        title = title,
        onBack = onBack,
        onSettings = if (step is PaymentStep.Accounts) onOpenSettings else null,
    ) { padding ->
        when (step) {
            PaymentStep.Accounts -> PaymentAccountsScreen(
                padding = padding,
                accounts = accountsOf(tables, orders),
                notice = notice,
                onAccount = { account ->
                    notice = ""
                    onStep(PaymentStep.Bill(account.tableKey))
                },
            )

            is PaymentStep.Bill -> {
                val bill = billOf(step.tableKey, orders, itemsByOrder) ?: emptyBill(step.tableKey, tables)
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
                    onPrint = { settle(bill, printInvoice = true) },
                    onCharge = { settle(bill, printInvoice = false) },
                )
            }
        }
    }
}

private fun emptyBill(tableKey: String, tables: List<DiningTableEntity>): TableBill {
    val table = tables.firstOrNull { it.id == tableKey }
    return TableBill(
        tableKey = tableKey,
        tableCode = table?.code ?: tableKey,
        orderIds = emptyList(),
        cashier = "Caja",
        lines = emptyList(),
        foodTotal = 0.0,
        taxAmount = 0.0,
        articleCount = 0,
    )
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
