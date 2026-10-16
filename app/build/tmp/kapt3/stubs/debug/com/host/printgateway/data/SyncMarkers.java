package com.host.printgateway.data;

/**
 * Markers shared by local order sync with HOST.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004J\u001e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/host/printgateway/data/SyncMarkers;", "", "()V", "PAYMENT_PREFIX", "", "PRINT_RECEIPT", "REMOTE_ITEM_PREFIX", "parsePayment", "Lcom/host/printgateway/data/PaymentIntent;", "syncStatus", "paymentMarker", "method", "tip", "", "printReceipt", "", "app_debug"})
public final class SyncMarkers {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String REMOTE_ITEM_PREFIX = "remote:";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PAYMENT_PREFIX = "PAY|";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PRINT_RECEIPT = "P";
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.SyncMarkers INSTANCE = null;
    
    private SyncMarkers() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String paymentMarker(@org.jetbrains.annotations.NotNull()
    java.lang.String method, double tip, boolean printReceipt) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.data.PaymentIntent parsePayment(@org.jetbrains.annotations.NotNull()
    java.lang.String syncStatus) {
        return null;
    }
}