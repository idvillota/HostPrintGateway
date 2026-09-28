package com.host.printgateway.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.ui.components.AppScaffold

@Composable
fun HomeScreen(
    onOpenTables: () -> Unit,
    onOpenPayments: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    AppScaffold(
        title = "Inicio",
        onSettings = onOpenSettings,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Menú principal",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "Elige si vas a tomar pedidos o a cobrar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            MenuCard(
                title = "Mesas",
                subtitle = "Ver las mesas, armar la comanda e imprimirla.",
                onClick = onOpenTables,
            )
            MenuCard(
                title = "Pagos",
                subtitle = "Cobrar una mesa, agregar propina e imprimir la factura.",
                onClick = onOpenPayments,
            )
        }
    }
}

@Composable
private fun MenuCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
