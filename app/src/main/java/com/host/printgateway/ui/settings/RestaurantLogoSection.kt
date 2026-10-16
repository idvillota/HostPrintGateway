package com.host.printgateway.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.RestaurantLogoStore
import com.host.printgateway.printer.ConfiguredPrint
import com.host.printgateway.printer.CopyResult
import com.host.printgateway.printer.EscPosReceiptFormatter
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RestaurantLogoSection(
    logoPath: String,
    printerMac: String,
    database: PrintGatewayDatabase,
    onLogoChange: (String) -> Unit,
    onStatus: (String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val store = remember { RestaurantLogoStore(context) }
    var printing by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(PickVisualMedia()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val saved = runCatching { withContext(Dispatchers.IO) { store.save(uri) } }
            saved.fold(
                onSuccess = {
                    onLogoChange(it)
                    onStatus("Logo guardado.")
                },
                onFailure = {
                    onStatus("No se pudo leer la imagen.")
                },
            )
        }
    }

    Text(
        text = "Si eliges una imagen, reemplaza el logo de Host al entrar y en el inicio.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Surface(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (logoPath.isBlank()) {
                Text("Sin logo", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                RestaurantLogoPreview(logoPath)
            }
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { picker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            modifier = Modifier.weight(1f),
        ) {
            Text("Elegir imagen")
        }
        OutlinedButton(
            onClick = {
                scope.launch {
                    withContext(Dispatchers.IO) { store.clear() }
                    onLogoChange("")
                    onStatus("Logo retirado.")
                }
            },
            enabled = logoPath.isNotBlank(),
            modifier = Modifier.weight(1f),
        ) {
            Text("Quitar")
        }
    }
    Button(
        onClick = {
            scope.launch {
                printing = true
                val message = withContext(Dispatchers.IO) { printLogo(database, printerMac, logoPath) }
                printing = false
                onStatus(message)
            }
        },
        enabled = logoPath.isNotBlank() && !printing,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(if (printing) "Enviando…" else "Probar logo")
    }
}

@Composable
private fun RestaurantLogoPreview(logoPath: String) {
    val logo by produceState<androidx.compose.ui.graphics.ImageBitmap?>(null, logoPath) {
        value = withContext(Dispatchers.IO) {
            if (!File(logoPath).isFile) null else BitmapFactory.decodeFile(logoPath)?.asImageBitmap()
        }
    }
    logo?.let {
        Image(
            bitmap = it,
            contentDescription = "Logo del restaurante",
            modifier = Modifier.height(88.dp).widthIn(max = 220.dp),
            contentScale = ContentScale.Fit,
        )
    }
}

private suspend fun printLogo(
    database: PrintGatewayDatabase,
    printerMac: String,
    logoPath: String,
): String {
    val bitmap = BitmapFactory.decodeFile(logoPath)
        ?: return "No se pudo leer el logo."
    return try {
        val bytes = EscPosReceiptFormatter().formatLogo(bitmap)
        when (val printed = ConfiguredPrint.printInvoice(database.restaurantDao(), printerMac, bytes)) {
            CopyResult.Printed -> "Logo enviado a la impresora."
            is CopyResult.Failed -> printed.message
            is CopyResult.Invalid -> printed.message
        }
    } finally {
        bitmap.recycle()
    }
}
