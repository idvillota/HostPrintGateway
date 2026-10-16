package com.host.printgateway.printer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Turns a bitmap into an ESC/POS raster.
 * The saved logo is left unchanged. At print time the artwork is fitted,
 * keeping its proportions, inside a line narrow printers can finish.
 */
object EscPosRaster {
    private const val LINE_WIDTH = 360
    private const val CONTENT_WIDTH = 280
    private const val CONTENT_HEIGHT = 180

    fun fromBitmap(source: Bitmap): ByteArray {
        if (source.width <= 0 || source.height <= 0) return byteArrayOf()
        val artwork = cropOuterMargin(source) ?: return byteArrayOf()
        val placed = placeOnLine(artwork)
        try {
            return encode(placed)
        } finally {
            if (placed !== artwork) placed.recycle()
            if (artwork !== source) artwork.recycle()
        }
    }

    private fun cropOuterMargin(source: Bitmap): Bitmap? {
        var top = 0
        var bottom = source.height - 1
        var left = 0
        var right = source.width - 1
        while (top <= bottom && isBlankRow(source, top, left, right)) top++
        while (bottom >= top && isBlankRow(source, bottom, left, right)) bottom--
        if (top > bottom) return null
        while (left <= right && isBlankColumn(source, left, top, bottom)) left++
        while (right >= left && isBlankColumn(source, right, top, bottom)) right--
        if (left == 0 && top == 0 && right == source.width - 1 && bottom == source.height - 1) {
            return source
        }
        val crop = Bitmap.createBitmap(right - left + 1, bottom - top + 1, Bitmap.Config.ARGB_8888)
        Canvas(crop).apply {
            drawColor(Color.WHITE)
            drawBitmap(source, -left.toFloat(), -top.toFloat(), null)
        }
        return crop
    }

    private fun placeOnLine(artwork: Bitmap): Bitmap {
        val scale = min(
            1f,
            min(
                CONTENT_WIDTH.toFloat() / artwork.width,
                CONTENT_HEIGHT.toFloat() / artwork.height,
            ),
        )
        val width = (artwork.width * scale).roundToInt().coerceIn(1, CONTENT_WIDTH)
        val height = (artwork.height * scale).roundToInt().coerceIn(1, CONTENT_HEIGHT)
        val scaled = if (width == artwork.width && height == artwork.height) {
            artwork
        } else {
            Bitmap.createScaledBitmap(artwork, width, height, true)
        }
        val line = Bitmap.createBitmap(LINE_WIDTH, height, Bitmap.Config.ARGB_8888)
        Canvas(line).apply {
            drawColor(Color.WHITE)
            drawBitmap(scaled, ((LINE_WIDTH - width) / 2).toFloat(), 0f, null)
        }
        if (scaled !== artwork) scaled.recycle()
        return line
    }

    private fun encode(bitmap: Bitmap): ByteArray {
        val bytesPerRow = bitmap.width / 8
        val data = ByteArray(bytesPerRow * bitmap.height)
        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                if (!isInk(bitmap.getPixel(x, y))) continue
                val index = y * bytesPerRow + x / 8
                data[index] = (data[index].toInt() or (0x80 shr (x % 8))).toByte()
            }
        }
        return byteArrayOf(
            0x1D,
            0x76,
            0x30,
            0x00,
            (bytesPerRow and 0xFF).toByte(),
            ((bytesPerRow shr 8) and 0xFF).toByte(),
            (bitmap.height and 0xFF).toByte(),
            ((bitmap.height shr 8) and 0xFF).toByte(),
        ) + data
    }

    private fun isBlankRow(bitmap: Bitmap, y: Int, left: Int, right: Int): Boolean {
        for (x in left..right) {
            if (isInk(bitmap.getPixel(x, y))) return false
        }
        return true
    }

    private fun isBlankColumn(bitmap: Bitmap, x: Int, top: Int, bottom: Int): Boolean {
        for (y in top..bottom) {
            if (isInk(bitmap.getPixel(x, y))) return false
        }
        return true
    }

    private fun isInk(pixel: Int): Boolean {
        if (Color.alpha(pixel) < 128) return false
        val luminance = Color.red(pixel) * 0.30 + Color.green(pixel) * 0.59 + Color.blue(pixel) * 0.11
        return luminance < 235
    }
}
