package com.host.printgateway.printer;

/**
 * Sends one HOST or local ticket to the printers selected for its categories or for invoices.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ&\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0017\u0010\u000f\u001a\u00020\u0004*\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0002\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/host/printgateway/printer/ConfiguredPrint;", "", "()V", "printInvoice", "Lcom/host/printgateway/printer/CopyResult;", "dao", "Lcom/host/printgateway/data/RestaurantDao;", "fallbackMac", "", "bytes", "", "(Lcom/host/printgateway/data/RestaurantDao;Ljava/lang/String;[BLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "printKitchen", "payload", "(Lcom/host/printgateway/data/RestaurantDao;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toCopyResult", "Lkotlin/Result;", "", "(Ljava/lang/Object;)Lcom/host/printgateway/printer/CopyResult;", "app_debug"})
public final class ConfiguredPrint {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.printer.ConfiguredPrint INSTANCE = null;
    
    private ConfiguredPrint() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object printKitchen(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.RestaurantDao dao, @org.jetbrains.annotations.NotNull()
    java.lang.String fallbackMac, @org.jetbrains.annotations.NotNull()
    java.lang.String payload, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.host.printgateway.printer.CopyResult> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object printInvoice(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.RestaurantDao dao, @org.jetbrains.annotations.NotNull()
    java.lang.String fallbackMac, @org.jetbrains.annotations.NotNull()
    byte[] bytes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.host.printgateway.printer.CopyResult> $completion) {
        return null;
    }
    
    private final com.host.printgateway.printer.CopyResult toCopyResult(java.lang.Object $this$toCopyResult) {
        return null;
    }
}