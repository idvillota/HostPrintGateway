package com.host.printgateway.ui.payment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00040\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001\u001a8\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00010\f\u001a\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00012\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002\u001a\u000e\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\n\u001a\u0012\u0010\u0015\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0014\u001a\u00020\nH\u0002\u001a\u0015\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0014\u001a\u00020\n\u00a2\u0006\u0002\u0010\u0017\u001a\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u000fH\u0002\u001a\u000e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\n\u001a\u001a\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001\u001a2\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u00012\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00122\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\n0\u0001\u00a8\u0006\""}, d2 = {"accountsOf", "", "Lcom/host/printgateway/ui/payment/TableAccountSummary;", "tables", "Lcom/host/printgateway/data/DiningTableEntity;", "orders", "Lcom/host/printgateway/data/OrderEntity;", "billOf", "Lcom/host/printgateway/ui/payment/TableBill;", "tableKey", "", "itemsByOrder", "", "Lcom/host/printgateway/data/OrderItemEntity;", "equalShares", "", "due", "parts", "", "groupedMoneyDisplay", "raw", "normalizeMoney", "parseAmount", "(Ljava/lang/String;)Ljava/lang/Double;", "roundMoney", "value", "sanitizeMoneyTyping", "summariesOf", "transferQrAmounts", "Lcom/host/printgateway/ui/payment/TransferQr;", "mode", "Lcom/host/printgateway/ui/payment/SplitMode;", "equalParts", "customParts", "app_debug"})
public final class TableBillKt {
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.host.printgateway.ui.payment.TableAccountSummary> accountsOf(@org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.DiningTableEntity> tables, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.OrderEntity> orders) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.host.printgateway.ui.payment.TableAccountSummary> summariesOf(@org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.OrderEntity> orders) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final com.host.printgateway.ui.payment.TableBill billOf(@org.jetbrains.annotations.NotNull()
    java.lang.String tableKey, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.OrderEntity> orders, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<com.host.printgateway.data.OrderItemEntity>> itemsByOrder) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.Double parseAmount(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    /**
     * Digits and one decimal comma. Thousand dots from a paste are removed.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String sanitizeMoneyTyping(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    /**
     * Shows thousand dots while the stored text stays digits plus an optional comma.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String groupedMoneyDisplay(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.host.printgateway.ui.payment.TransferQr> transferQrAmounts(double due, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.ui.payment.SplitMode mode, int equalParts, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> customParts) {
        return null;
    }
    
    private static final java.util.List<java.lang.Double> equalShares(double due, int parts) {
        return null;
    }
    
    private static final double roundMoney(double value) {
        return 0.0;
    }
    
    private static final java.lang.String normalizeMoney(java.lang.String raw) {
        return null;
    }
}