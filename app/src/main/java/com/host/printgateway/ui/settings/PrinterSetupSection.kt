package com.host.printgateway.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.LocalPrinterEntity
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.PrinterSetup
import com.host.printgateway.data.PrinterSetupStore
import com.host.printgateway.data.ProductTypeEntity
import com.host.printgateway.printer.PrintPipeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun PrinterSetupSection(
    database: PrintGatewayDatabase,
    fallbackMac: String,
    onMirrorMac: (String) -> Unit,
    onStatus: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val store = remember(database) { PrinterSetupStore(database.restaurantDao()) }
    var setup by remember { mutableStateOf(PrinterSetup(emptyList(), emptyList())) }
    var editor by remember { mutableStateOf<PrinterDraft?>(null) }
    var testingId by remember { mutableStateOf("") }

    suspend fun reload() {
        setup = store.load()
        onMirrorMac(store.mirrorMac())
    }

    LaunchedEffect(fallbackMac) {
        store.ensureLegacy(fallbackMac)
        reload()
    }

    Text(
        text = "Cada impresora usa su MAC. Las categorías salen de HOST. Sin categorías, imprime lo que ninguna otra tomó.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    setup.printers.forEach { (printer, categoryIds) ->
        PrinterCard(
            printer = printer,
            categoryNames = setup.types.filter { it.id in categoryIds }.map { it.name },
            testing = testingId == printer.id,
            onEdit = {
                editor = PrinterDraft(
                    id = printer.id,
                    name = printer.name,
                    mac = printer.mac,
                    printsInvoices = printer.printsInvoices,
                    categoryIds = categoryIds,
                )
            },
            onTest = {
                testingId = printer.id
                onStatus("Enviando prueba…")
                scope.launch {
                    val result = withContext(Dispatchers.IO) { PrintPipeline.printTestTicket(printer.mac) }
                    testingId = ""
                    onStatus(if (result.isSuccess) "Prueba OK" else "Error: ${result.exceptionOrNull()?.message ?: "error"}")
                }
            },
            onDelete = {
                scope.launch {
                    withContext(Dispatchers.IO) { store.delete(printer.id) }
                    reload()
                }
            },
        )
    }

    if (editor == null) {
        Button(
            onClick = {
                editor = PrinterDraft(
                    id = UUID.randomUUID().toString(),
                    name = "",
                    mac = "",
                    printsInvoices = setup.printers.isEmpty(),
                    categoryIds = emptySet(),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Agregar impresora")
        }
    } else {
        PrinterEditor(
            draft = editor!!,
            types = setup.types,
            onChange = { editor = it },
            onCancel = { editor = null },
            onSave = { draft ->
                if (draft.mac.isBlank()) {
                    onStatus("Indica la MAC de la impresora.")
                    return@PrinterEditor
                }
                scope.launch {
                    withContext(Dispatchers.IO) {
                        store.save(
                            LocalPrinterEntity(
                                id = draft.id,
                                name = draft.name.ifBlank { "Impresora" },
                                mac = draft.mac.trim(),
                                printsInvoices = draft.printsInvoices,
                                sortOrder = setup.printers.indexOfFirst { it.first.id == draft.id }
                                    .let { index -> if (index < 0) setup.printers.size else index },
                            ),
                            draft.categoryIds,
                        )
                    }
                    editor = null
                    reload()
                    onStatus("Impresora guardada.")
                }
            },
        )
    }
}

@Composable
private fun PrinterCard(
    printer: LocalPrinterEntity,
    categoryNames: List<String>,
    testing: Boolean,
    onEdit: () -> Unit,
    onTest: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(printer.name, style = MaterialTheme.typography.titleMedium)
        Text(printer.mac, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = when {
                categoryNames.isEmpty() -> "Todas las categorías sin asignar"
                else -> categoryNames.joinToString(", ")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (printer.printsInvoices) {
            Text("Imprime facturas", style = MaterialTheme.typography.bodySmall)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onEdit) { Text("Editar") }
            TextButton(onClick = onTest, enabled = !testing) { Text(if (testing) "Enviando…" else "Probar") }
            TextButton(onClick = onDelete) { Text("Quitar") }
        }
    }
}

@Composable
private fun PrinterEditor(
    draft: PrinterDraft,
    types: List<ProductTypeEntity>,
    onChange: (PrinterDraft) -> Unit,
    onCancel: () -> Unit,
    onSave: (PrinterDraft) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = draft.name,
            onValueChange = { onChange(draft.copy(name = it)) },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = draft.mac,
            onValueChange = { onChange(draft.copy(mac = it)) },
            label = { Text("MAC Bluetooth") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Categorías", style = MaterialTheme.typography.titleSmall)
        Text(
            text = "Solo se imprime lo marcado. Si una categoría no está marcada en ninguna impresora, esa línea no sale.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (types.isEmpty()) {
            Text(
                "Sincroniza el catálogo para ver las categorías de HOST.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        types.forEach { type ->
            FilterChip(
                selected = type.id in draft.categoryIds,
                onClick = {
                    val next = draft.categoryIds.toMutableSet()
                    if (!next.add(type.id)) next.remove(type.id)
                    onChange(draft.copy(categoryIds = next))
                },
                label = { Text(type.name) },
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterChip(
                selected = draft.printsInvoices,
                onClick = { onChange(draft.copy(printsInvoices = !draft.printsInvoices)) },
                label = { Text("Facturas") },
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSave(draft) }) { Text("Guardar") }
            OutlinedButton(onClick = onCancel) { Text("Cancelar") }
        }
    }
}

private data class PrinterDraft(
    val id: String,
    val name: String,
    val mac: String,
    val printsInvoices: Boolean,
    val categoryIds: Set<String>,
)
