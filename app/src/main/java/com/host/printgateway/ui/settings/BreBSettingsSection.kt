package com.host.printgateway.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.payment.BreBProfile

private val keyTypes = listOf(
    1 to "Identificación",
    2 to "Celular",
    3 to "Correo",
    4 to "Alfanumérica",
    5 to "Código de comercio",
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreBSettingsSection(settings: GatewaySettings) {
    var profile by remember { mutableStateOf(settings.breBProfile()) }

    fun update(next: BreBProfile) {
        profile = next
        settings.saveBreB(next)
    }

    Text(
        text = "Estos datos quedan en el celular. Si el cobro es por transferencia, se muestra un QR con el valor. El pago se registra con el mismo botón de siempre.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    OutlinedTextField(
        value = profile.key,
        onValueChange = { update(profile.copy(key = it)) },
        label = { Text("Llave Bre-B") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    Text("Tipo de llave", style = MaterialTheme.typography.titleSmall)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        keyTypes.forEach { (type, label) ->
            FilterChip(
                selected = profile.keyType == type,
                onClick = { update(profile.copy(keyType = type)) },
                label = { Text(label) },
            )
        }
    }
    OutlinedTextField(
        value = profile.merchantName,
        onValueChange = { update(profile.copy(merchantName = it)) },
        label = { Text("Nombre en la transferencia") },
        supportingText = { Text("Es el nombre que ve el cliente en su banco. Máximo 25 caracteres.") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = profile.city,
        onValueChange = { update(profile.copy(city = it)) },
        label = { Text("Ciudad") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = profile.postalCode,
        onValueChange = { update(profile.copy(postalCode = it)) },
        label = { Text("Código postal") },
        supportingText = { Text("Opcional.") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}
