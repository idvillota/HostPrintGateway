package com.host.printgateway.ui.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.ui.order.formatPrice

private val paymentMethods = listOf("Efectivo", "Tarjeta", "Transferencia")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentBillScreen(
    padding: PaddingValues,
    bill: TableBill?,
    tipText: String,
    paymentMethod: String,
    splitMode: SplitMode,
    equalParts: Int,
    customParts: List<String>,
    notice: String,
    sending: Boolean,
    onTipChange: (String) -> Unit,
    onTipPercent: (Int) -> Unit,
    onPaymentMethod: (String) -> Unit,
    onSplitMode: (SplitMode) -> Unit,
    onEqualParts: (Int) -> Unit,
    onCustomPart: (Int, String) -> Unit,
    onAddCustomPart: () -> Unit,
    onRemoveCustomPart: () -> Unit,
    onPrint: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (bill == null) {
            Text(
                text = "Esta mesa ya no tiene una cuenta abierta.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            return@Column
        }
        Text(text = "Mesa ${bill.tableCode}", style = MaterialTheme.typography.headlineSmall)
        bill.lines.forEach { line ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "${formatQuantity(line.quantity)}  ${line.description}",
                    modifier = Modifier.weight(1f),
                )
                Text(text = formatPrice(line.lineTotal))
            }
        }
        Text(
            text = "Consumo  ${formatPrice(bill.foodTotal)}",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(text = "Propina", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { onTipPercent(0) }) { Text("0%") }
            OutlinedButton(onClick = { onTipPercent(10) }) { Text("10%") }
        }
        OutlinedTextField(
            value = tipText,
            onValueChange = onTipChange,
            label = { Text("Valor de la propina") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(text = "Forma de pago", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            paymentMethods.forEach { method ->
                FilterChip(
                    selected = paymentMethod == method,
                    onClick = { onPaymentMethod(method) },
                    label = { Text(method) },
                )
            }
        }
        Text(text = "Dividir cuenta", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SplitChip("No", splitMode == SplitMode.None) { onSplitMode(SplitMode.None) }
            SplitChip("Iguales", splitMode == SplitMode.Equal) { onSplitMode(SplitMode.Equal) }
            SplitChip("Personalizado", splitMode == SplitMode.Custom) { onSplitMode(SplitMode.Custom) }
        }
        val tip = parseAmount(tipText)
        val due = bill.foodTotal + (tip ?: 0.0)
        when (splitMode) {
            SplitMode.None -> Unit
            SplitMode.Equal -> EqualSplit(
                parts = equalParts,
                due = due,
                onChange = onEqualParts,
            )
            SplitMode.Custom -> CustomSplit(
                due = due,
                parts = customParts,
                onPart = onCustomPart,
                onAdd = onAddCustomPart,
                onRemove = onRemoveCustomPart,
            )
        }
        Text(
            text = "Total a cobrar  ${formatPrice(due)}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        if (notice.isNotBlank()) {
            Text(text = notice, color = MaterialTheme.colorScheme.primary)
        }
        Button(
            onClick = onPrint,
            enabled = !sending && bill.lines.isNotEmpty() && tip != null,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 14.dp),
        ) {
            Text(if (sending) "Imprimiendo…" else "Imprimir factura")
        }
    }
}

@Composable
private fun SplitChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}

@Composable
private fun EqualSplit(
    parts: Int,
    due: Double,
    onChange: (Int) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (parts > 2) onChange(parts - 1) }) {
            Text("−", style = MaterialTheme.typography.titleLarge)
        }
        Text(text = "$parts partes", style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = { if (parts < 20) onChange(parts + 1) }) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
    Text(
        text = "Cada parte: ${formatPrice(due / parts)}",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun CustomSplit(
    due: Double,
    parts: List<String>,
    onPart: (Int, String) -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
) {
    val entered = parts.map { parseAmount(it) }
    val sum = entered.sumOf { it ?: 0.0 }
    parts.forEachIndexed { index, value ->
        OutlinedTextField(
            value = value,
            onValueChange = { onPart(index, it) },
            label = { Text("Parte ${index + 1}") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onAdd) { Text("Agregar parte") }
        if (parts.size > 2) {
            OutlinedButton(onClick = onRemove) { Text("Quitar") }
        }
    }
    val difference = due - sum
    Text(
        text = when {
            entered.any { it == null } -> "Revisa los valores de cada parte."
            kotlin.math.abs(difference) < 0.5 -> "Las partes cubren el total."
            difference > 0 -> "Falta ${formatPrice(difference)}"
            else -> "Sobra ${formatPrice(-difference)}"
        },
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private fun formatQuantity(quantity: Double): String {
    return if (quantity % 1.0 == 0.0) "${quantity.toLong()}×" else "$quantity×"
}
