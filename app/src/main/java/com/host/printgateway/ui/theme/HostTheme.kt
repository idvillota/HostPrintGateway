package com.host.printgateway.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val HostColors = lightColorScheme(
    primary = Color(0xFF1F6F5B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7EFE6),
    onPrimaryContainer = Color(0xFF0E3D32),
    secondary = Color(0xFF8C5A3C),
    onSecondary = Color.White,
    background = Color(0xFFF6F4F1),
    onBackground = Color(0xFF1C1B19),
    surface = Color.White,
    onSurface = Color(0xFF1C1B19),
    surfaceVariant = Color(0xFFE7E2DA),
    onSurfaceVariant = Color(0xFF5C574F),
    outline = Color(0xFFD0CBC3),
)

private val HostShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
)

@Composable
fun HostTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HostColors,
        shapes = HostShapes,
        content = content,
    )
}
