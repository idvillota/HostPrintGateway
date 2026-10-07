package com.host.printgateway.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.host.printgateway.R

@Composable
fun HostLogo(
    modifier: Modifier = Modifier,
    height: Dp = 96.dp,
) {
    Image(
        painter = painterResource(R.drawable.host_logo),
        contentDescription = "Host",
        modifier = modifier.height(height),
        contentScale = ContentScale.Fit,
    )
}
