package com.host.printgateway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class GatewayLink {
    Stopped,
    Connecting,
    Ready,
}

object GatewayLinkState {
    var status by mutableStateOf(GatewayLink.Stopped)
        private set

    fun start() {
        status = GatewayLink.Connecting
    }

    fun markReady() {
        if (status != GatewayLink.Stopped) status = GatewayLink.Ready
    }

    fun markConnecting() {
        if (status != GatewayLink.Stopped) status = GatewayLink.Connecting
    }

    fun stop() {
        status = GatewayLink.Stopped
    }
}

@Composable
fun GatewayStatusChip(status: GatewayLink) {
    val (label, dot) = when (status) {
        GatewayLink.Stopped -> "Detenido" to Color(0xFF868E96)
        GatewayLink.Connecting -> "Conectando" to Color(0xFFFA5252)
        GatewayLink.Ready -> "Listo" to Color(0xFF2B8A3E)
    }
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .padding(end = 4.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape)
            .background(Color.White, shape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dot, CircleShape),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
