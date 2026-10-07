package com.host.printgateway.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a`\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u001aF\u0010\u000f\u001a\u00020\u00012\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u00a8\u0006\u001a"}, d2 = {"HostApp", "", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "authManager", "Lcom/host/printgateway/network/AuthManager;", "renewSessionTick", "", "onEnsurePermissions", "Lkotlin/Function0;", "onStartGateway", "onStopGateway", "onExit", "OfflineModeBanner", "modifier", "Landroidx/compose/ui/Modifier;", "notice", "Lcom/host/printgateway/ui/OfflineNotice;", "busy", "", "detail", "", "onActivate", "onResume", "app_debug"})
public final class HostAppKt {
    
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
    
    @androidx.compose.runtime.Composable()
    private static final void OfflineModeBanner(androidx.compose.ui.Modifier modifier, com.host.printgateway.ui.OfflineNotice notice, boolean busy, java.lang.String detail, kotlin.jvm.functions.Function0<kotlin.Unit> onActivate, kotlin.jvm.functions.Function0<kotlin.Unit> onResume) {
    }
}