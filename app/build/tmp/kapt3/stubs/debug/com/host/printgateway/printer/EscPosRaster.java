package com.host.printgateway.printer;

/**
 * Turns a bitmap into one ESC/POS raster.
 * The saved logo is left unchanged. The row stays 384 dots, the width this
 * 58 mm head consumes, and the drawing is a small centered mark. A tall
 * square overflows the printer buffer and the leftover bytes print as text.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\bH\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\bH\u0002J\u000e\u0010\r\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\bJ(\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0004H\u0002J(\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0002J\u0010\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/host/printgateway/printer/EscPosRaster;", "", "()V", "CONTENT_HEIGHT", "", "CONTENT_WIDTH", "OUTPUT_WIDTH", "cropOuterMargin", "Landroid/graphics/Bitmap;", "source", "encode", "", "bitmap", "fromBitmap", "isBlankColumn", "", "x", "top", "bottom", "isBlankRow", "y", "left", "right", "isInk", "pixel", "placeForPrint", "artwork", "app_debug"})
public final class EscPosRaster {
    private static final int OUTPUT_WIDTH = 384;
    private static final int CONTENT_WIDTH = 200;
    private static final int CONTENT_HEIGHT = 144;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.printer.EscPosRaster INSTANCE = null;
    
    private EscPosRaster() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] fromBitmap(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap source) {
        return null;
    }
    
    private final android.graphics.Bitmap cropOuterMargin(android.graphics.Bitmap source) {
        return null;
    }
    
    private final android.graphics.Bitmap placeForPrint(android.graphics.Bitmap artwork) {
        return null;
    }
    
    private final byte[] encode(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final boolean isBlankRow(android.graphics.Bitmap bitmap, int y, int left, int right) {
        return false;
    }
    
    private final boolean isBlankColumn(android.graphics.Bitmap bitmap, int x, int top, int bottom) {
        return false;
    }
    
    private final boolean isInk(int pixel) {
        return false;
    }
}