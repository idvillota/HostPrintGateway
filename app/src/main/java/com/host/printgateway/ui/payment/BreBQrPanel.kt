package com.host.printgateway.ui.payment

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.host.printgateway.payment.BreBProfile
import com.host.printgateway.payment.BreBQrPayload
import com.host.printgateway.ui.order.formatPrice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.math.round

@Composable
fun BreBQrList(
    due: Double,
    splitMode: SplitMode,
    equalParts: Int,
    customParts: List<String>,
    profile: BreBProfile,
) {
    val amounts = transferAmounts(due, splitMode, equalParts, customParts)
    if (amounts.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        amounts.forEach { amount ->
            BreBQrCard(amount = amount, profile = profile)
        }
    }
}

@Composable
private fun BreBQrCard(amount: Double, profile: BreBProfile) {
    val reference = remember(amount, profile) {
        UUID.randomUUID().toString().replace("-", "").take(10)
    }
    val payload = remember(amount, profile, reference) {
        runCatching { BreBQrPayload.build(profile, amount, reference) }
    }
    Text(
        text = "QR de transferencia · ${formatPrice(amount)}",
        style = MaterialTheme.typography.titleSmall,
    )
    payload.fold(
        onSuccess = { text ->
            QrImage(text)
            Text(
                text = "El cliente escanea este código desde su banco. Registra el pago cuando el dinero haya llegado.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        onFailure = { error ->
            Text(
                text = error.message ?: "No se pudo generar el QR.",
                color = MaterialTheme.colorScheme.error,
            )
        },
    )
}

@Composable
private fun QrImage(payload: String) {
    var enlarged by remember(payload) { mutableStateOf(false) }
    val image by produceState<androidx.compose.ui.graphics.ImageBitmap?>(null, payload) {
        value = withContext(Dispatchers.Default) { qrBitmap(payload).asImageBitmap() }
    }
    val bitmap = image ?: return
    Image(
        bitmap = bitmap,
        contentDescription = "QR de transferencia. Toca para ampliarlo.",
        modifier = Modifier
            .size(220.dp)
            .clickable { enlarged = true },
        filterQuality = FilterQuality.None,
    )
    Text(
        text = "Toca el QR para ampliarlo.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    if (enlarged) {
        Dialog(onDismissRequest = { enlarged = false }) {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "QR de transferencia ampliado",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable { enlarged = false },
                        filterQuality = FilterQuality.None,
                    )
                    Text(
                        text = "Toca el QR para cerrarlo.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

private fun qrBitmap(payload: String): Bitmap {
    val size = 720
    val matrix = QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (y in 0 until size) {
        for (x in 0 until size) {
            bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
        }
    }
    return bitmap
}

fun transferAmounts(
    due: Double,
    mode: SplitMode,
    equalParts: Int,
    customParts: List<String>,
): List<Double> {
    if (due <= 0.0) return emptyList()
    return when (mode) {
        SplitMode.None -> listOf(roundMoney(due))
        SplitMode.Equal -> equalShares(due, equalParts)
        SplitMode.Custom -> customParts.mapNotNull { parseAmount(it) }.filter { it > 0.0 }.map(::roundMoney)
    }
}

private fun equalShares(due: Double, parts: Int): List<Double> {
    val count = parts.coerceIn(2, 20)
    val totalCents = round(due * 100.0).toLong()
    val base = totalCents / count
    val remainder = (totalCents % count).toInt()
    return List(count) { index ->
        (base + if (index == count - 1) remainder else 0) / 100.0
    }
}

private fun roundMoney(value: Double): Double = round(value * 100.0) / 100.0
