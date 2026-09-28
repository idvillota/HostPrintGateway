package com.host.printgateway.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.DiningTableEntity

@Composable
fun TablesScreen(
    padding: PaddingValues,
    tables: List<DiningTableEntity>,
    syncing: Boolean,
    notice: String,
    cartCount: Int,
    onSync: () -> Unit,
    onTable: (DiningTableEntity) -> Unit,
    onOpenComanda: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
    ) {
        OutlinedButton(
            onClick = onSync,
            enabled = !syncing,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (syncing) "Sincronizando…" else "Sincronizar mesas")
        }
        if (notice.isNotBlank()) {
            Text(
                text = notice,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        when {
            tables.isEmpty() -> {
                Text(
                    text = "Todavía no hay mesas. Sincroniza cuando tengas internet.",
                    modifier = Modifier.padding(top = 24.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            else -> {
                Text(
                    text = "${tables.size} mesas",
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                BoxWithConstraints(modifier = Modifier.weight(1f)) {
                    val columns = when {
                        maxWidth >= 900.dp -> 4
                        maxWidth >= 600.dp -> 3
                        else -> 2
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp),
                    ) {
                        items(tables, key = { it.id }) { table ->
                            TableCard(table = table, onClick = { onTable(table) })
                        }
                    }
                }
            }
        }
        if (cartCount > 0) {
            Button(
                onClick = onOpenComanda,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                Text("Ver comanda ($cartCount)")
            }
        }
    }
}

@Composable
private fun TableCard(
    table: DiningTableEntity,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = table.code,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            val zone = table.zone.orEmpty()
            if (zone.isNotBlank()) {
                Text(
                    text = zone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (table.capacity > 0) {
                Text(
                    text = "${table.capacity} puestos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
