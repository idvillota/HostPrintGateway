package com.host.printgateway.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.PrintGatewayDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RoomDebugPanel(database: PrintGatewayDatabase) {
    val scope = rememberCoroutineScope()
    val dao = remember(database) { database.restaurantDao() }
    var content by remember { mutableStateOf("Sin datos cargados") }

    suspend fun refresh() {
        content = withContext(Dispatchers.IO) {
            val tables = dao.getTables()
            val types = dao.getProductTypes()
            val printers = dao.getPrinters()
            val orders = dao.getOrders()
            val tickets = dao.getPendingTickets()
            buildString {
                appendLine("Mesas: ${tables.size}")
                appendLine("Tipos de producto: ${types.size}")
                appendLine("Impresoras: ${printers.size}")
                appendLine("Órdenes locales: ${orders.size}")
                appendLine("Comandas pendientes: ${tickets.size}")
                if (orders.isNotEmpty()) {
                    appendLine()
                    appendLine("Órdenes:")
                    orders.take(10).forEach { order ->
                        appendLine("- ${order.number.ifBlank { order.id.take(8) }} | mesa ${order.diningTableCode} | ${order.status}")
                    }
                }
                if (tickets.isNotEmpty()) {
                    appendLine()
                    appendLine("Comandas pendientes:")
                    tickets.take(10).forEach { ticket ->
                        appendLine("- ${ticket.id.take(8)} | intentos ${ticket.attempts} | ${ticket.status}")
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) { refresh() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("Diagnóstico temporal de Room", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(
            onClick = { scope.launch { refresh() } },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Actualizar vista de Room")
        }
        Text(content, style = MaterialTheme.typography.bodySmall)
    }
}