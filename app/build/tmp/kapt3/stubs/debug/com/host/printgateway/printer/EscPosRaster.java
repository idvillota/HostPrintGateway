package com.host.printgateway.printer;

/**
 * Turns a bitmap into an ESC/POS raster the thermal printer can draw.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/host/printgateway/printer/EscPosRaster;", "", "()V", "MAX_HEIGHT", "", "MAX_WIDTH", "fromBitmap", "", "source", "Landroid/graphics/Bitmap;", "isBlack", "", "pixel", "app_debug"})
public final class EscPosRaster {
    private static final int MAX_WIDTH = 384;
    private static final int MAX_HEIGHT = 180;
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
    
    private final boolean isBlack(int pixel) {
        return false;
    }
}