package com.host.printgateway.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val HostColors = lightColorScheme(
    primary = Color(0xFFEA580C),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF4E6),
    onPrimaryContainer = Color(0xFF9A3412),
    secondary = Color(0xFF495057),
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF212529),
    surface = Color.White,
    onSurface = Color(0xFF212529),
    surfaceVariant = Color(0xFFF1F3F5),
    onSurfaceVariant = Color(0xFF868E96),
    outline = Color(0xFFDEE2E6),
    error = Color(0xFFFA5252),
)

private val HostShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
)

@Composable
fun HostTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HostColors,
        shapes = HostShapes,
        content = content,
    )
}
