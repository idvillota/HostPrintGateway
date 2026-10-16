package com.host.printgateway.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File

/**
 * Keeps one restaurant logo inside app storage so the picker URI can expire.
 */
class RestaurantLogoStore(context: Context) {

    private val appContext = context.applicationContext

    fun save(uri: Uri): String {
        val source = File(appContext.cacheDir, "restaurant-logo-source")
        appContext.contentResolver.openInputStream(uri)?.use { input ->
            source.outputStream().use { input.copyTo(it) }
        } ?: error("No se pudo abrir la imagen")
        try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(source.absolutePath, bounds)
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) error("La imagen no es válida")
            val decoded = BitmapFactory.decodeFile(
                source.absolutePath,
                BitmapFactory.Options().apply {
                    inSampleSize = sampleSize(bounds.outWidth, bounds.outHeight, MAX_EDGE)
                },
            ) ?: error("No se pudo leer la imagen")
            val scaled = scaleDown(decoded, MAX_EDGE)
            val destination = file()
            destination.outputStream().use { scaled.compress(Bitmap.CompressFormat.PNG, 100, it) }
            if (scaled !== decoded) scaled.recycle()
            decoded.recycle()
            return destination.absolutePath
        } finally {
            source.delete()
        }
    }

    fun clear() {
        file().delete()
    }

    private fun file(): File = File(appContext.filesDir, FILE_NAME)

    private fun sampleSize(width: Int, height: Int, maxEdge: Int): Int {
        var sample = 1
        while (width / sample > maxEdge * 2 || height / sample > maxEdge * 2) {
            sample *= 2
        }
        return sample
    }

    private fun scaleDown(bitmap: Bitmap, maxEdge: Int): Bitmap {
        val largest = maxOf(bitmap.width, bitmap.height)
        if (largest <= maxEdge) return bitmap
        val ratio = maxEdge.toFloat() / largest
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * ratio).toInt().coerceAtLeast(1),
            (bitmap.height * ratio).toInt().coerceAtLeast(1),
            true,
        )
    }

    private companion object {
        const val FILE_NAME = "restaurant-logo.png"
        const val MAX_EDGE = 512
    }
}
