package com.host.printgateway.ui.payment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000`\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001ax\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0007\u001a\u001e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00052\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0002\u001a\u0010\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u001dH\u0002\u001a0\u0010\u001e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00122\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00050\u00192\u0006\u0010#\u001a\u00020\u001dH\u0002\u00a8\u0006$"}, d2 = {"PaymentHost", "", "step", "Lcom/host/printgateway/ui/navigation/PaymentStep;", "apiUrl", "", "deviceToken", "printerMac", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "onStep", "Lkotlin/Function1;", "onLeave", "Lkotlin/Function0;", "onOpenSettings", "catalogRevision", "", "offlineMode", "", "emptyBill", "Lcom/host/printgateway/ui/payment/TableBill;", "tableKey", "tables", "", "Lcom/host/printgateway/data/DiningTableEntity;", "moneyInput", "value", "", "splitNote", "mode", "Lcom/host/printgateway/ui/payment/SplitMode;", "equalParts", "customParts", "due", "app_release"})
public final class PaymentHostKt {
    
    @androidx.compose.runtime.Composable()
    public static final void PaymentHost(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.ui.navigation.PaymentStep step, @org.jetbrains.annotations.NotNull()
    java.lang.String apiUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceToken, @org.jetbrains.annotations.NotNull()
    java.lang.String printerMac, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.ui.navigation.PaymentStep, kotlin.Unit> onStep, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLeave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, int catalogRevision, boolean offlineMode) {
    }
    
    private static final com.host.printgateway.ui.payment.TableBill emptyBill(java.lang.String tableKey, java.util.List<com.host.printgateway.data.DiningTableEntity> tables) {
        return null;
    }
    
    private static final java.lang.String splitNote(com.host.printgateway.ui.payment.SplitMode mode, int equalParts, java.util.List<java.lang.String> customParts, double due) {
        return null;
    }
    
    private static final java.lang.String moneyInput(double value) {
        return null;
    }
}