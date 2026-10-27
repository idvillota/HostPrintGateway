package com.host.printgateway.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.OrderEntity
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.PrintJobEntity
import com.host.printgateway.ui.order.formatPrice
import com.host.printgateway.ui.RoomDebugPanel
import com.host.printgateway.ui.components.AppScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun SettingsScreen(
    apiUrl: String,
    printerMac: String,
    logoPath: String,
    signedIn: Boolean,
    status: String,
    database: PrintGatewayDatabase,
    settings: GatewaySettings,
    onPrinterMacChange: (String) -> Unit,
    onLogoChange: (String) -> Unit,
    onBack: () -> Unit,
    onStartGateway: () -> Unit,
    onStopGateway: () -> Unit,
    onLogout: () -> Unit,
    onStatus: (String) -> Unit,
    autoSyncEnabled: Boolean,
    autoSyncIntervalMinutes: Int,
    onToggleAutoSync: () -> Unit,
    offlineMode: Boolean,
    offlineBusy: Boolean,
    onToggleOffline: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var showPrinterMenu by remember { mutableStateOf(false) }
    var showAdvanced by remember { mutableStateOf(false) }
    var manualXml by remember { mutableStateOf("") }
    var printKitchenTickets by remember { mutableStateOf(settings.printKitchenTickets) }

    if (showPrinterMenu) {
        PrinterMenuScreen(
            database = database,
            printerMac = printerMac,
            status = status,
            onPrinterMacChange = onPrinterMacChange,
            onStatus = onStatus,
            onBack = { showPrinterMenu = false },
        )
        return
    }

    BackHandler(onBack = onBack)

    AppScaffold(title = "Configuración", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionTitle("Restaurante")
            RestaurantLogoSection(
                logoPath = logoPath,
                printerMac = printerMac,
                database = database,
                onLogoChange = onLogoChange,
                onStatus = onStatus,
            )

            SectionTitle("Transferencia Bre-B")
            BreBSettingsSection(settings)

            SectionTitle("Servidor")
            Text(
                text = "HOST de producción. Esta dirección queda fija en la instalación.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = apiUrl,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            SectionTitle("Sincronización")
            Text(
                text = if (autoSyncEnabled) {
                    "Activa. El catálogo se actualiza cada $autoSyncIntervalMinutes minutos."
                } else {
                    "Desactivada. El inventario solo cambia si sincronizas a mano."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                onClick = onToggleAutoSync,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (autoSyncEnabled) "Desactivar sincronización" else "Activar sincronización")
            }
            Text(
                text = if (offlineMode) {
                    "Modo sin internet activo. Pedidos y cobros quedan en el celular."
                } else {
                    "Modo sin internet desactivado."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                onClick = onToggleOffline,
                enabled = signedIn && !offlineBusy,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    when {
                        offlineBusy -> "Sincronizando…"
                        offlineMode -> "Desactivar modo sin internet"
                        else -> "Activar modo sin internet"
                    },
                )
            }

            SectionTitle("Impresoras")
            Text(
                text = if (printKitchenTickets) {
                    "Las comandas de cocina se envían a la impresora."
                } else {
                    "Las comandas de cocina no se imprimen. El pedido se guarda igual. Las facturas no cambian."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                onClick = {
                    printKitchenTickets = !printKitchenTickets
                    settings.printKitchenTickets = printKitchenTickets
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    if (printKitchenTickets) {
                        "Dejar de imprimir comandas"
                    } else {
                        "Imprimir comandas de cocina"
                    },
                )
            }
            Button(
                onClick = { showPrinterMenu = true },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Menú de impresoras")
            }

            SectionTitle("Gateway")
            Text(
                text = "Déjalo activo cerca de las impresoras para recibir trabajos del servidor.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                onClick = onStartGateway,
                enabled = signedIn,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Iniciar gateway")
            }
            OutlinedButton(
                onClick = onStopGateway,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Detener gateway")
            }

            if (signedIn) {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Cerrar sesión", color = MaterialTheme.colorScheme.error)
                }
            }

            TextButton(onClick = { showAdvanced = !showAdvanced }) {
                Text(if (showAdvanced) "Ocultar herramientas" else "Herramientas avanzadas")
            }
            if (showAdvanced) {
                val adminOpen = manualXml.trim() == ADMIN_PASSWORD
                RoomDebugPanel(database = database)
                OutlinedTextField(
                    value = manualXml,
                    onValueChange = { manualXml = it },
                    label = { Text("XML del recibo") },
                    minLines = 4,
                    maxLines = 8,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedButton(
                    onClick = {
                        val xml = manualXml
                        scope.launch {
                            val result = withContext(Dispatchers.IO) {
                                runCatching {
                                    database.printJobDao().insertAll(
                                        listOf(
                                            PrintJobEntity(
                                                id = "manual-${UUID.randomUUID()}",
                                                kind = "receipt",
                                                payloadFormat = "sales-receipt-xml",
                                                payload = xml,
                                            ),
                                        ),
                                    )
                                }
                            }
                            if (result.isSuccess) {
                                manualXml = ""
                                onStatus("XML guardado en la cola offline")
                            } else {
                                onStatus("Error guardando XML: ${result.exceptionOrNull()?.message ?: "error"}")
                            }
                        }
                    },
                    enabled = manualXml.isNotBlank() && !adminOpen,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Insertar XML en Room")
                }
                if (adminOpen) {
                    LocalSalesAdmin(
                        database = database,
                        onStatus = onStatus,
                    )
                }
            }

            if (status.isNotBlank()) {
                Text(
                    text = status,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun PrinterMenuScreen(
    database: PrintGatewayDatabase,
    printerMac: String,
    status: String,
    onPrinterMacChange: (String) -> Unit,
    onStatus: (String) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    AppScaffold(title = "Menú de impresoras", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PrinterSetupSection(
                database = database,
                fallbackMac = printerMac,
                onMirrorMac = onPrinterMacChange,
                onStatus = onStatus,
            )
            if (status.isNotBlank()) {
                Text(
                    text = status,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private const val ADMIN_PASSWORD = "GVD06"

@Composable
private fun LocalSalesAdmin(
    database: PrintGatewayDatabase,
    onStatus: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val dao = remember(database) { database.restaurantDao() }
    var orders by remember { mutableStateOf(emptyList<OrderEntity>()) }
    var busy by remember { mutableStateOf(false) }

    suspend fun reload() {
        orders = withContext(Dispatchers.IO) { dao.getOrdersMissingOnHost() }
    }

    LaunchedEffect(Unit) {
        reload()
    }

    suspend fun remove(orderIds: List<String>) {
        withContext(Dispatchers.IO) {
            val tableIds = orders.filter { it.id in orderIds }.mapNotNull { it.diningTableId }.distinct()
            orderIds.forEach { dao.deleteLocalOnlyOrder(it) }
            tableIds.forEach { tableId ->
                val stillOpen = dao.getUnpaidOrders().any { it.diningTableId == tableId }
                if (!stillOpen) dao.updateTableStatus(tableId, "0")
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Ventas solo en este celular",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Estas comandas no tienen pedido en HOST. Quitarlas no cambia la web.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (orders.isEmpty()) {
            Text(
                text = "No hay ventas locales fuera de HOST.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            orders.forEach { order ->
                OutlinedButton(
                    onClick = {
                        busy = true
                        scope.launch {
                            remove(listOf(order.id))
                            reload()
                            busy = false
                            onStatus("Venta local de la mesa ${order.diningTableCode} retirada.")
                        }
                    },
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Quitar mesa ${order.diningTableCode} · ${formatPrice(order.total)}")
                }
            }
            OutlinedButton(
                onClick = {
                    busy = true
                    scope.launch {
                        val count = orders.size
                        remove(orders.map { it.id })
                        reload()
                        busy = false
                        onStatus("Se retiraron $count ventas locales.")
                    }
                },
                enabled = !busy,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Quitar todas")
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp),
    )
}
