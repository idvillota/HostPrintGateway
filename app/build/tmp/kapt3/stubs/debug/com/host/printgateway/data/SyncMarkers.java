package com.host.printgateway.data;

/**
 * Markers shared by local order sync with HOST.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0004J\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0004J&\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rJ\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u000e\u001a\u00020\u0004J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u000e\u001a\u00020\u0004J\u001e\u0010\n\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\rJ\u000e\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/host/printgateway/data/SyncMarkers;", "", "()V", "MOVE_PREFIX", "", "MOVE_SEPARATOR", "PAYMENT_PREFIX", "PRINT_RECEIPT", "REMOTE_ITEM_PREFIX", "attachingPayment", "paymentMarker", "currentStatus", "isPayment", "", "syncStatus", "isStandaloneMove", "moveMarker", "fromTableId", "toTableId", "at", "", "merge", "parseMove", "Lcom/host/printgateway/data/TableMove;", "parsePayment", "Lcom/host/printgateway/data/PaymentIntent;", "method", "tip", "", "printReceipt", "withoutMove", "app_debug"})
public final class SyncMarkers {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String REMOTE_ITEM_PREFIX = "remote:";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PAYMENT_PREFIX = "PAY|";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOVE_PREFIX = "MOVE|";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PRINT_RECEIPT = "P";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MOVE_SEPARATOR = "|MOVE|";
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
    public final java.lang.String moveMarker(@org.jetbrains.annotations.NotNull()
    java.lang.String fromTableId, @org.jetbrains.annotations.NotNull()
    java.lang.String toTableId, long at, boolean merge) {
        return null;
    }
    
    public final boolean isPayment(@org.jetbrains.annotations.NotNull()
    java.lang.String syncStatus) {
        return false;
    }
    
    public final boolean isStandaloneMove(@org.jetbrains.annotations.NotNull()
    java.lang.String syncStatus) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.host.printgateway.data.TableMove parseMove(@org.jetbrains.annotations.NotNull()
    java.lang.String syncStatus) {
        return null;
    }
    
    /**
     * Keeps a pending table change when the order is paid before that change syncs.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String attachingPayment(@org.jetbrains.annotations.NotNull()
    java.lang.String paymentMarker, @org.jetbrains.annotations.NotNull()
    java.lang.String currentStatus) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String withoutMove(@org.jetbrains.annotations.NotNull()
    java.lang.String syncStatus) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.data.PaymentIntent parsePayment(@org.jetbrains.annotations.NotNull()
    java.lang.String syncStatus) {
        return null;
    }
}