package com.host.printgateway.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderReviewScreen(
    padding: PaddingValues,
    tableCode: String,
    lines: List<CartLine>,
    waiter: String,
    sending: Boolean,
    buttonLabel: String,
    canSend: Boolean,
    notice: String,
    onWaiterChange: (String) -> Unit,
    onChangeQuantity: (String, Int) -> Unit,
    onSend: () -> Unit,
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
        Text(text = "Mesa $tableCode", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Revisa la comanda y envíala a cocina.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = waiter,
            onValueChange = onWaiterChange,
            label = { Text("Mesero") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        if (lines.isEmpty()) {
            Text(
                text = "La comanda no tiene productos.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        lines.forEach { line ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = line.product.name, style = MaterialTheme.typography.titleMedium)
                    if (line.notes.isNotBlank()) {
                        Text(
                            text = line.notes,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onChangeQuantity(line.key, -1) }) {
                            Text("−", style = MaterialTheme.typography.titleLarge)
                        }
                        Text(
                            text = line.quantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                        IconButton(onClick = { onChangeQuantity(line.key, 1) }) {
                            Text("+", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
        if (notice.isNotBlank()) {
            Text(text = notice, color = MaterialTheme.colorScheme.primary)
        }
        Button(
            onClick = onSend,
            enabled = canSend,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 14.dp),
        ) {
            Text(if (sending) "Enviando…" else buttonLabel)
        }
    }
}
