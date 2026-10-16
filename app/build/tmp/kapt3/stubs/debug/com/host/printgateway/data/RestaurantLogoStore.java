package com.host.printgateway.data;

/**
 * Keeps one restaurant logo inside app storage so the picker URI can expire.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\b\u0010\t\u001a\u00020\nH\u0002J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\fH\u0002J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\fH\u0002R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/host/printgateway/data/RestaurantLogoStore;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "clear", "", "file", "Ljava/io/File;", "sampleSize", "", "width", "height", "maxEdge", "save", "", "uri", "Landroid/net/Uri;", "scaleDown", "Landroid/graphics/Bitmap;", "bitmap", "Companion", "app_debug"})
public final class RestaurantLogoStore {
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    @java.lang.Deprecated()
    public static final java.lang.String FILE_NAME = "restaurant-logo.png";
    @java.lang.Deprecated()
    public static final int MAX_EDGE = 512;
    @org.jetbrains.annotations.NotNull()
    private static final com.host.printgateway.data.RestaurantLogoStore.Companion Companion = null;
    
    public RestaurantLogoStore(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String save(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    public final void clear() {
    }
    
    private final java.io.File file() {
        return null;
    }
    
    private final int sampleSize(int width, int height, int maxEdge) {
        return 0;
    }
    
    private final android.graphics.Bitmap scaleDown(android.graphics.Bitmap bitmap, int maxEdge) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/host/printgateway/data/RestaurantLogoStore$Companion;", "", "()V", "FILE_NAME", "", "MAX_EDGE", "", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
    }
}