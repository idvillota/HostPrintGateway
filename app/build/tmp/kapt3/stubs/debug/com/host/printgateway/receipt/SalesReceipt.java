package com.host.printgateway.receipt;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b2\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u00bf\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0019J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\t\u00101\u001a\u00020\u000fH\u00c6\u0003J\t\u00102\u001a\u00020\u0011H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0011H\u00c6\u0003J\t\u00105\u001a\u00020\u0011H\u00c6\u0003J\t\u00106\u001a\u00020\u0011H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u0003H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\t\u0010?\u001a\u00020\u0003H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010A\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003J\u00c3\u0001\u0010B\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00112\b\b\u0002\u0010\u0014\u001a\u00020\u00112\b\b\u0002\u0010\u0015\u001a\u00020\u00112\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010C\u001a\u00020D2\b\u0010E\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010F\u001a\u00020\u000fH\u00d6\u0001J\t\u0010G\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0013\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001fR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001fR\u0011\u0010\u0015\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001bR\u0011\u0010\u0014\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001fR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001fR\u0011\u0010\u0017\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001fR\u0011\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001fR\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001fR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001fR\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001f\u00a8\u0006H"}, d2 = {"Lcom/host/printgateway/receipt/SalesReceipt;", "", "tradeName", "", "invoiceNumber", "dianConsecutive", "dateTime", "tableCodes", "cashier", "customerName", "customerId", "lines", "", "Lcom/host/printgateway/receipt/ReceiptLine;", "articleCount", "", "total", "", "paymentMethod", "amountTendered", "impoconsumoBase", "impoconsumo", "resolutionNumber", "rangeFrom", "rangeTo", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;IDLjava/lang/String;DDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAmountTendered", "()D", "getArticleCount", "()I", "getCashier", "()Ljava/lang/String;", "getCustomerId", "getCustomerName", "getDateTime", "getDianConsecutive", "getImpoconsumo", "getImpoconsumoBase", "getInvoiceNumber", "getLines", "()Ljava/util/List;", "getPaymentMethod", "getRangeFrom", "getRangeTo", "getResolutionNumber", "getTableCodes", "getTotal", "getTradeName", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class SalesReceipt {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tradeName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String invoiceNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String dianConsecutive = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String dateTime = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tableCodes = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cashier = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String customerName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String customerId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.host.printgateway.receipt.ReceiptLine> lines = null;
    private final int articleCount = 0;
    private final double total = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String paymentMethod = null;
    private final double amountTendered = 0.0;
    private final double impoconsumoBase = 0.0;
    private final double impoconsumo = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String resolutionNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rangeFrom = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rangeTo = null;
    
    public SalesReceipt(@org.jetbrains.annotations.NotNull()
    java.lang.String tradeName, @org.jetbrains.annotations.NotNull()
    java.lang.String invoiceNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String dianConsecutive, @org.jetbrains.annotations.NotNull()
    java.lang.String dateTime, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCodes, @org.jetbrains.annotations.NotNull()
    java.lang.String cashier, @org.jetbrains.annotations.NotNull()
    java.lang.String customerName, @org.jetbrains.annotations.NotNull()
    java.lang.String customerId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.receipt.ReceiptLine> lines, int articleCount, double total, @org.jetbrains.annotations.NotNull()
    java.lang.String paymentMethod, double amountTendered, double impoconsumoBase, double impoconsumo, @org.jetbrains.annotations.NotNull()
    java.lang.String resolutionNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String rangeFrom, @org.jetbrains.annotations.NotNull()
    java.lang.String rangeTo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTradeName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInvoiceNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDianConsecutive() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDateTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTableCodes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCashier() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCustomerName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCustomerId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.receipt.ReceiptLine> getLines() {
        return null;
    }
    
    public final int getArticleCount() {
        return 0;
    }
    
    public final double getTotal() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPaymentMethod() {
        return null;
    }
    
    public final double getAmountTendered() {
        return 0.0;
    }
    
    public final double getImpoconsumoBase() {
        return 0.0;
    }
    
    public final double getImpoconsumo() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getResolutionNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRangeFrom() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRangeTo() {
        return null;
    }
    
    public SalesReceipt() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final double component11() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    public final double component13() {
        return 0.0;
    }
    
    public final double component14() {
        return 0.0;
    }
    
    public final double component15() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.receipt.ReceiptLine> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.receipt.SalesReceipt copy(@org.jetbrains.annotations.NotNull()
    java.lang.String tradeName, @org.jetbrains.annotations.NotNull()
    java.lang.String invoiceNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String dianConsecutive, @org.jetbrains.annotations.NotNull()
    java.lang.String dateTime, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCodes, @org.jetbrains.annotations.NotNull()
    java.lang.String cashier, @org.jetbrains.annotations.NotNull()
    java.lang.String customerName, @org.jetbrains.annotations.NotNull()
    java.lang.String customerId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.receipt.ReceiptLine> lines, int articleCount, double total, @org.jetbrains.annotations.NotNull()
    java.lang.String paymentMethod, double amountTendered, double impoconsumoBase, double impoconsumo, @org.jetbrains.annotations.NotNull()
    java.lang.String resolutionNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String rangeFrom, @org.jetbrains.annotations.NotNull()
    java.lang.String rangeTo) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}