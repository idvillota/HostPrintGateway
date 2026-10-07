package com.host.printgateway.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000Z\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u009e\u0001\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00040\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00040\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00040\u001dH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"OFFLINE_AFTER_MS", "", "REACH_POLL_MS", "HostSyncEffects", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "token", "", "savedApiUrl", "offlineMode", "", "autoSyncEnabled", "uploadNonce", "", "offlineModeNow", "Landroidx/compose/runtime/State;", "session", "Lcom/host/printgateway/ui/order/OrderSession;", "deviceId", "scope", "Lkotlinx/coroutines/CoroutineScope;", "offlineNotice", "Lcom/host/printgateway/ui/OfflineNotice;", "onOfflineNoticeChange", "Lkotlin/Function1;", "onCatalogRevisionBump", "Lkotlin/Function0;", "onUnauthorized", "app_debug"})
public final class HostSyncEffectsKt {
    private static final long OFFLINE_AFTER_MS = 20000L;
    private static final long REACH_POLL_MS = 5000L;
    
    /**
     * Background sync / reachability / sale-stream loops formerly inline in [HostApp].
     * Sequences and keys match the previous LaunchedEffect blocks.
     */
    @androidx.compose.runtime.Composable()
    public static final void HostSyncEffects(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    java.lang.String savedApiUrl, boolean offlineMode, boolean autoSyncEnabled, int uploadNonce, @org.jetbrains.annotations.NotNull()
    androidx.compose.runtime.State<java.lang.Boolean> offlineModeNow, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.ui.order.OrderSession session, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope scope, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.ui.OfflineNotice offlineNotice, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.ui.OfflineNotice, kotlin.Unit> onOfflineNoticeChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCatalogRevisionBump, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onUnauthorized) {
    }
}