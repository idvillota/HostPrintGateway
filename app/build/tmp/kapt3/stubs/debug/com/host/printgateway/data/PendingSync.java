package com.host.printgateway.data;

/**
 * Offline payments for one table are separate HOST charges.
 * Orders paid in the same tap share [PendingPayment.closedAt] and stay together.
 * A later charge, and any new comanda, wait until the earlier charge has closed the table.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J,\u0010\u0003\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u00060\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J&\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\u00052\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000eJ\u0018\u0010\u000f\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\u0005\u00a8\u0006\u0010"}, d2 = {"Lcom/host/printgateway/data/PendingSync;", "", "()V", "chargesByTable", "", "", "", "Lcom/host/printgateway/data/PendingPayment;", "payments", "isReadyToUpload", "", "diningTableId", "orderId", "unsettledTableKeys", "", "tableKey", "app_debug"})
public final class PendingSync {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.PendingSync INSTANCE = null;
    
    private PendingSync() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String tableKey(@org.jetbrains.annotations.Nullable()
    java.lang.String diningTableId, @org.jetbrains.annotations.NotNull()
    java.lang.String orderId) {
        return null;
    }
    
    public final boolean isReadyToUpload(@org.jetbrains.annotations.Nullable()
    java.lang.String diningTableId, @org.jetbrains.annotations.NotNull()
    java.lang.String orderId, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> unsettledTableKeys) {
        return false;
    }
    
    /**
     * Charges per table, oldest first. Each inner list is one payment.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<java.util.List<com.host.printgateway.data.PendingPayment>>> chargesByTable(@org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.PendingPayment> payments) {
        return null;
    }
}