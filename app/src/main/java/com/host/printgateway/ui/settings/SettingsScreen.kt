package com.host.printgateway.ui.settings

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.PrintJobEntity
import com.host.printgateway.printer.BluetoothEscPosPrinter
import com.host.printgateway.printer.EscPosReceiptFormatter
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
    signedIn: Boolean,
    status: String,
    database: PrintGatewayDatabase,
    onApiUrlChange: (String) -> Unit,
    onPrinterMacChange: (String) -> Unit,
    onBack: () -> Unit,
    onStartGateway: () -> Unit,
    onStopGateway: () -> Unit,
    onLogout: () -> Unit,
    onStatus: (String) -> Unit,
    autoSyncEnabled: Boolean,
    autoSyncIntervalMinutes: Int,
    onToggleAutoSync: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var showAdvanced by remember { mutableStateOf(false) }
    var manualXml by remember { mutableStateOf("") }
    var testingPrinter by remember { mutableStateOf(false) }

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
            SectionTitle("Servidor")
            Text(
                text = "Dirección del sistema Host. Hace falta para iniciar sesión.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = apiUrl,
                onValueChange = onApiUrlChange,
                label = { Text("URL del servidor") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
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

            SectionTitle("Impresora")
            Text(
                text = "La comanda se envía a esta impresora térmica.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = printerMac,
                onValueChange = onPrinterMacChange,
                label = { Text("MAC Bluetooth") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedButton(
                onClick = {
                    if (printerMac.isBlank()) {
                        onStatus("Indica la MAC de la impresora.")
                        return@OutlinedButton
                    }
                    testingPrinter = true
                    onStatus("Enviando prueba…")
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            val bytes = EscPosReceiptFormatter().formatTestTicket(printerMac.trim())
                            BluetoothEscPosPrinter(printerMac.trim()).print(bytes)
                        }
                        testingPrinter = false
                        onStatus(
                            if (result.isSuccess) {
                                "Prueba OK"
                            } else {
                                "Error: ${result.exceptionOrNull()?.message ?: "error"}"
                            },
                        )
                    }
                },
                enabled = !testingPrinter,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (testingPrinter) "Enviando prueba…" else "Probar impresora")
            }

            SectionTitle("Gateway")
            Text(
                text = "Déjalo activo cerca de la impresora para recibir trabajos del servidor.",
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
                    enabled = manualXml.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Insertar XML en Room")
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
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp),
    )
}
