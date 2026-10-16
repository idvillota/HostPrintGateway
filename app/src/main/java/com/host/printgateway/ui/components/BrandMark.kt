package com.host.printgateway.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun BrandMark(
    logoPath: String,
    modifier: Modifier = Modifier,
    height: Dp = 96.dp,
) {
    val restaurantLogo by produceState<ImageBitmap?>(null, logoPath) {
        value = withContext(Dispatchers.IO) {
            if (logoPath.isBlank() || !File(logoPath).isFile) {
                null
            } else {
                BitmapFactory.decodeFile(logoPath)?.asImageBitmap()
            }
        }
    }
    val logo = restaurantLogo
    if (logo == null) {
        HostLogo(modifier = modifier, height = height)
    } else {
        Image(
            bitmap = logo,
            contentDescription = "Logo del restaurante",
            modifier = modifier.height(height).widthIn(max = 280.dp),
            contentScale = ContentScale.Fit,
        )
    }
}
