package com.host.printgateway.ui.payment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u001aX\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0007\u001a\u0010\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0011H\u0002\u001a0\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u00172\u0006\u0010\u0018\u001a\u00020\u0011H\u0002\u00a8\u0006\u0019"}, d2 = {"PaymentHost", "", "step", "Lcom/host/printgateway/ui/navigation/PaymentStep;", "printerMac", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "onStep", "Lkotlin/Function1;", "onLeave", "Lkotlin/Function0;", "onOpenSettings", "catalogRevision", "", "moneyInput", "value", "", "splitNote", "mode", "Lcom/host/printgateway/ui/payment/SplitMode;", "equalParts", "customParts", "", "due", "app_debug"})
public final class PaymentHostKt {
    
    @androidx.compose.runtime.Composable()
    public static final void PaymentHost(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.ui.navigation.PaymentStep step, @org.jetbrains.annotations.NotNull()
    java.lang.String printerMac, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.ui.navigation.PaymentStep, kotlin.Unit> onStep, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLeave, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, int catalogRevision) {
    }
    
    private static final java.lang.String splitNote(com.host.printgateway.ui.payment.SplitMode mode, int equalParts, java.util.List<java.lang.String> customParts, double due) {
        return null;
    }
    
    private static final java.lang.String moneyInput(double value) {
        return null;
    }
}