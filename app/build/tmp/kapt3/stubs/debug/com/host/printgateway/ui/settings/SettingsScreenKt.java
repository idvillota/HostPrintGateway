package com.host.printgateway.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\u001a$\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u0007H\u0003\u001aV\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00072\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00030\rH\u0003\u001a\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0001H\u0003\u001a\u0084\u0002\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00072\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00072\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00030\r2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\r2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00030\r2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00030\r2\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00072\u0006\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00030\r2\u0006\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00142\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00030\rH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"ADMIN_PASSWORD", "", "LocalSalesAdmin", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "onStatus", "Lkotlin/Function1;", "PrinterMenuScreen", "printerMac", "status", "onPrinterMacChange", "onBack", "Lkotlin/Function0;", "SectionTitle", "text", "SettingsScreen", "apiUrl", "logoPath", "signedIn", "", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "onApiUrlChange", "onLogoChange", "onStartGateway", "onStopGateway", "onLogout", "autoSyncEnabled", "autoSyncIntervalMinutes", "", "onToggleAutoSync", "offlineMode", "offlineBusy", "onToggleOffline", "app_debug"})
public final class SettingsScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ADMIN_PASSWORD = "GVD06";
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String apiUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String printerMac, @org.jetbrains.annotations.NotNull()
    java.lang.String logoPath, boolean signedIn, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onApiUrlChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPrinterMacChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLogoChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStartGateway, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStopGateway, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLogout, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStatus, boolean autoSyncEnabled, int autoSyncIntervalMinutes, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleAutoSync, boolean offlineMode, boolean offlineBusy, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleOffline) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PrinterMenuScreen(com.host.printgateway.data.PrintGatewayDatabase database, java.lang.String printerMac, java.lang.String status, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPrinterMacChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStatus, kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocalSalesAdmin(com.host.printgateway.data.PrintGatewayDatabase database, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStatus) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionTitle(java.lang.String text) {
    }
}