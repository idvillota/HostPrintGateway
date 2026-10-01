package com.host.printgateway.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000R\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a`\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00040\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u000e2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u000eH\u0007\u001aF\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00040\u000e2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00040\u000eH\u0003\u001a6\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u001a2\u0006\u0010\u0005\u001a\u00020\u0006H\u0082@\u00a2\u0006\u0002\u0010\"\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"OFFLINE_AFTER_MS", "", "REACH_POLL_MS", "HostApp", "", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "authManager", "Lcom/host/printgateway/network/AuthManager;", "renewSessionTick", "", "onEnsurePermissions", "Lkotlin/Function0;", "onStartGateway", "onStopGateway", "onExit", "OfflineModeBanner", "modifier", "Landroidx/compose/ui/Modifier;", "notice", "Lcom/host/printgateway/ui/OfflineNotice;", "busy", "", "detail", "", "onActivate", "onResume", "pushThenPull", "Lcom/host/printgateway/ui/OfflineSyncResult;", "token", "baseUrl", "deviceId", "(Lcom/host/printgateway/data/PrintGatewayDatabase;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/host/printgateway/data/GatewaySettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class HostAppKt {
    private static final long OFFLINE_AFTER_MS = 20000L;
    private static final long REACH_POLL_MS = 5000L;
    
    @androidx.compose.runtime.Composable()
    public static final void HostApp(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.network.AuthManager authManager, int renewSessionTick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEnsurePermissions, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStartGateway, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStopGateway, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExit) {
    }
    
    private static final java.lang.Object pushThenPull(com.host.printgateway.data.PrintGatewayDatabase database, java.lang.String token, java.lang.String baseUrl, java.lang.String deviceId, com.host.printgateway.data.GatewaySettings settings, kotlin.coroutines.Continuation<? super com.host.printgateway.ui.OfflineSyncResult> $completion) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OfflineModeBanner(androidx.compose.ui.Modifier modifier, com.host.printgateway.ui.OfflineNotice notice, boolean busy, java.lang.String detail, kotlin.jvm.functions.Function0<kotlin.Unit> onActivate, kotlin.jvm.functions.Function0<kotlin.Unit> onResume) {
    }
}