package com.host.printgateway.data;

/**
 * Persists gateway configuration (SharedPreferences — minimal, no extra libs).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 ,2\u00020\u0001:\u0001,B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010*\u001a\u00020\fJ\u0006\u0010+\u001a\u00020\fR$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR$\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R$\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u00128F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R$\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0019\u0010\t\"\u0004\b\u001a\u0010\u000bR$\u0010\u001b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001c\u0010\u000f\"\u0004\b\u001d\u0010\u0011R$\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u00128F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001f\u0010\u0015\"\u0004\b \u0010\u0017R\u0016\u0010!\u001a\n #*\u0004\u0018\u00010\"0\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010$\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b%\u0010\t\"\u0004\b&\u0010\u000bR$\u0010\'\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b(\u0010\t\"\u0004\b)\u0010\u000b\u00a8\u0006-"}, d2 = {"Lcom/host/printgateway/data/GatewaySettings;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "apiBaseUrl", "getApiBaseUrl", "()Ljava/lang/String;", "setApiBaseUrl", "(Ljava/lang/String;)V", "", "autoSyncEnabled", "getAutoSyncEnabled", "()Z", "setAutoSyncEnabled", "(Z)V", "", "autoSyncIntervalMs", "getAutoSyncIntervalMs", "()J", "setAutoSyncIntervalMs", "(J)V", "deviceToken", "getDeviceToken", "setDeviceToken", "offlineMode", "getOfflineMode", "setOfflineMode", "pollIntervalMs", "getPollIntervalMs", "setPollIntervalMs", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "printerMac", "getPrinterMac", "setPrinterMac", "saleCursor", "getSaleCursor", "setSaleCursor", "isConfigured", "isPrinterConfigured", "Companion", "app_debug"})
public final class GatewaySettings {
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS = "host_print_gateway";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_API_URL = "api_base_url";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_TOKEN = "device_token";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_MAC = "printer_mac";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_POLL_MS = "poll_interval_ms";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_AUTO_SYNC = "auto_sync_enabled";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_AUTO_SYNC_MS = "auto_sync_interval_ms";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SALE_CURSOR = "sale_cursor";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_OFFLINE_MODE = "offline_mode";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_API_URL = "http://10.0.2.2:5228";
    public static final long DEFAULT_POLL_MS = 3000L;
    public static final long DEFAULT_AUTO_SYNC_MS = 900000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.GatewaySettings.Companion Companion = null;
    
    public GatewaySettings(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getApiBaseUrl() {
        return null;
    }
    
    public final void setApiBaseUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeviceToken() {
        return null;
    }
    
    public final void setDeviceToken(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPrinterMac() {
        return null;
    }
    
    public final void setPrinterMac(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final long getPollIntervalMs() {
        return 0L;
    }
    
    public final void setPollIntervalMs(long value) {
    }
    
    public final boolean getAutoSyncEnabled() {
        return false;
    }
    
    public final void setAutoSyncEnabled(boolean value) {
    }
    
    public final long getAutoSyncIntervalMs() {
        return 0L;
    }
    
    public final void setAutoSyncIntervalMs(long value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaleCursor() {
        return null;
    }
    
    public final void setSaleCursor(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final boolean getOfflineMode() {
        return false;
    }
    
    public final void setOfflineMode(boolean value) {
    }
    
    public final boolean isConfigured() {
        return false;
    }
    
    public final boolean isPrinterConfigured() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/host/printgateway/data/GatewaySettings$Companion;", "", "()V", "DEFAULT_API_URL", "", "DEFAULT_AUTO_SYNC_MS", "", "DEFAULT_POLL_MS", "KEY_API_URL", "KEY_AUTO_SYNC", "KEY_AUTO_SYNC_MS", "KEY_MAC", "KEY_OFFLINE_MODE", "KEY_POLL_MS", "KEY_SALE_CURSOR", "KEY_TOKEN", "PREFS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}