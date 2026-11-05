package com.host.printgateway.ui.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.host.printgateway.BuildConfig
import com.host.printgateway.ui.components.AppScaffold
import com.host.printgateway.ui.components.BrandMark

@Composable
fun HomeScreen(
    logoPath: String,
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
            BrandMark(
                logoPath = logoPath,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                height = 88.dp,
                fallbackToHost = false,
            )
            Text(
                text = "Menú principal",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = if (BuildConfig.READ_ONLY) {
                    "Consulta el estado de las mesas y las cuentas. Esta app no cambia nada en HOST."
                } else {
                    "Elige si vas a tomar pedidos o a cobrar."
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            MenuCard(
                title = "Mesas",
                subtitle = if (BuildConfig.READ_ONLY) {
                    "Ver cuáles mesas están libres u ocupadas y qué se pidió."
                } else {
                    "Ver las mesas, armar la comanda e imprimirla."
                },
                onClick = onOpenTables,
            )
            MenuCard(
                title = if (BuildConfig.READ_ONLY) "Ventas" else "Pagos",
                subtitle = if (BuildConfig.READ_ONLY) {
                    "Ver las cuentas abiertas y sus totales. No se puede cobrar."
                } else {
                    "Cobrar una mesa, agregar propina e imprimir la factura."
                },
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
