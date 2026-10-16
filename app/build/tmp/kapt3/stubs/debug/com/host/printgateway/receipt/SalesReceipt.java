package com.host.printgateway.receipt;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\bL\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u00b3\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0017\u0012\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0012\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0017\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0017\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u0017\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u0017\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u0017\u0012\b\b\u0002\u0010 \u001a\u00020\u0017\u0012\b\b\u0002\u0010!\u001a\u00020\u0017\u0012\b\b\u0002\u0010\"\u001a\u00020\u0003\u0012\b\b\u0002\u0010#\u001a\u00020\u0003\u0012\b\b\u0002\u0010$\u001a\u00020\u0003\u00a2\u0006\u0002\u0010%J\t\u0010G\u001a\u00020\u0003H\u00c6\u0003J\t\u0010H\u001a\u00020\u0003H\u00c6\u0003J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\t\u0010J\u001a\u00020\u0003H\u00c6\u0003J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010M\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u00c6\u0003J\t\u0010N\u001a\u00020\u0015H\u00c6\u0003J\t\u0010O\u001a\u00020\u0017H\u00c6\u0003J\u000f\u0010P\u001a\b\u0012\u0004\u0012\u00020\u00190\u0012H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\t\u0010S\u001a\u00020\u0017H\u00c6\u0003J\t\u0010T\u001a\u00020\u0017H\u00c6\u0003J\t\u0010U\u001a\u00020\u0017H\u00c6\u0003J\t\u0010V\u001a\u00020\u0017H\u00c6\u0003J\t\u0010W\u001a\u00020\u0017H\u00c6\u0003J\t\u0010X\u001a\u00020\u0017H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0017H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0003H\u00c6\u0003J\t\u0010[\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0003H\u00c6\u0003J\t\u0010]\u001a\u00020\u0003H\u00c6\u0003J\t\u0010^\u001a\u00020\u0003H\u00c6\u0003J\t\u0010_\u001a\u00020\u0003H\u00c6\u0003J\t\u0010`\u001a\u00020\u0003H\u00c6\u0003J\t\u0010a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010c\u001a\u00020\u0003H\u00c6\u0003J\u00b7\u0002\u0010d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00172\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00122\b\b\u0002\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u00172\b\b\u0002\u0010\u001c\u001a\u00020\u00172\b\b\u0002\u0010\u001d\u001a\u00020\u00172\b\b\u0002\u0010\u001e\u001a\u00020\u00172\b\b\u0002\u0010\u001f\u001a\u00020\u00172\b\b\u0002\u0010 \u001a\u00020\u00172\b\b\u0002\u0010!\u001a\u00020\u00172\b\b\u0002\u0010\"\u001a\u00020\u00032\b\b\u0002\u0010#\u001a\u00020\u00032\b\b\u0002\u0010$\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010e\u001a\u00020f2\b\u0010g\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010h\u001a\u00020\u0015H\u00d6\u0001J\t\u0010i\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u001b\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\'R\u0011\u0010\u001c\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010)R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\'R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\'R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\'R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\'R\u0011\u0010\u001d\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010)R\u0011\u0010!\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010)R\u0011\u0010 \u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010)R\u0011\u0010\u001f\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010)R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010\'R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010\'R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\'R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u0011\u0010\u001a\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\'R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010:R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010\'R\u0011\u0010#\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010\'R\u0011\u0010$\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010\'R\u0011\u0010\"\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\'R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010\'R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010\'R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010\'R\u0011\u0010\u001e\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010)R\u0011\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010)R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010\'\u00a8\u0006j"}, d2 = {"Lcom/host/printgateway/receipt/SalesReceipt;", "", "tradeName", "", "legalName", "taxId", "address", "phone", "taxRegime", "legalRepresentative", "invoiceNumber", "dianConsecutive", "dateTime", "tableCodes", "cashier", "customerName", "customerId", "lines", "", "Lcom/host/printgateway/receipt/ReceiptLine;", "articleCount", "", "total", "", "payments", "Lcom/host/printgateway/receipt/ReceiptPayment;", "paymentMethod", "amountTendered", "changeDue", "discountAmount", "tipAmount", "impoconsumoPercent", "impoconsumoBase", "impoconsumo", "resolutionNumber", "rangeFrom", "rangeTo", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;IDLjava/util/List;Ljava/lang/String;DDDDDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAddress", "()Ljava/lang/String;", "getAmountTendered", "()D", "getArticleCount", "()I", "getCashier", "getChangeDue", "getCustomerId", "getCustomerName", "getDateTime", "getDianConsecutive", "getDiscountAmount", "getImpoconsumo", "getImpoconsumoBase", "getImpoconsumoPercent", "getInvoiceNumber", "getLegalName", "getLegalRepresentative", "getLines", "()Ljava/util/List;", "getPaymentMethod", "getPayments", "getPhone", "getRangeFrom", "getRangeTo", "getResolutionNumber", "getTableCodes", "getTaxId", "getTaxRegime", "getTipAmount", "getTotal", "getTradeName", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class SalesReceipt {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tradeName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String legalName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String taxId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String address = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phone = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String taxRegime = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String legalRepresentative = null;
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
    private final java.util.List<com.host.printgateway.receipt.ReceiptPayment> payments = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String paymentMethod = null;
    private final double amountTendered = 0.0;
    private final double changeDue = 0.0;
    private final double discountAmount = 0.0;
    private final double tipAmount = 0.0;
    private final double impoconsumoPercent = 0.0;
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
    java.lang.String legalName, @org.jetbrains.annotations.NotNull()
    java.lang.String taxId, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    java.lang.String taxRegime, @org.jetbrains.annotations.NotNull()
    java.lang.String legalRepresentative, @org.jetbrains.annotations.NotNull()
    java.lang.String invoiceNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String dianConsecutive, @org.jetbrains.annotations.NotNull()
    java.lang.String dateTime, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCodes, @org.jetbrains.annotations.NotNull()
    java.lang.String cashier, @org.jetbrains.annotations.NotNull()
    java.lang.String customerName, @org.jetbrains.annotations.NotNull()
    java.lang.String customerId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.receipt.ReceiptLine> lines, int articleCount, double total, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.receipt.ReceiptPayment> payments, @org.jetbrains.annotations.NotNull()
    java.lang.String paymentMethod, double amountTendered, double changeDue, double discountAmount, double tipAmount, double impoconsumoPercent, double impoconsumoBase, double impoconsumo, @org.jetbrains.annotations.NotNull()
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
    public final java.lang.String getLegalName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTaxId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAddress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTaxRegime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLegalRepresentative() {
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
    public final java.util.List<com.host.printgateway.receipt.ReceiptPayment> getPayments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPaymentMethod() {
        return null;
    }
    
    public final double getAmountTendered() {
        return 0.0;
    }
    
    public final double getChangeDue() {
        return 0.0;
    }
    
    public final double getDiscountAmount() {
        return 0.0;
    }
    
    public final double getTipAmount() {
        return 0.0;
    }
    
    public final double getImpoconsumoPercent() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.receipt.ReceiptLine> component15() {
        return null;
    }
    
    public final int component16() {
        return 0;
    }
    
    public final double component17() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.receipt.ReceiptPayment> component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final double component20() {
        return 0.0;
    }
    
    public final double component21() {
        return 0.0;
    }
    
    public final double component22() {
        return 0.0;
    }
    
    public final double component23() {
        return 0.0;
    }
    
    public final double component24() {
        return 0.0;
    }
    
    public final double component25() {
        return 0.0;
    }
    
    public final double component26() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component27() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component28() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component29() {
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
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.receipt.SalesReceipt copy(@org.jetbrains.annotations.NotNull()
    java.lang.String tradeName, @org.jetbrains.annotations.NotNull()
    java.lang.String legalName, @org.jetbrains.annotations.NotNull()
    java.lang.String taxId, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    java.lang.String taxRegime, @org.jetbrains.annotations.NotNull()
    java.lang.String legalRepresentative, @org.jetbrains.annotations.NotNull()
    java.lang.String invoiceNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String dianConsecutive, @org.jetbrains.annotations.NotNull()
    java.lang.String dateTime, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCodes, @org.jetbrains.annotations.NotNull()
    java.lang.String cashier, @org.jetbrains.annotations.NotNull()
    java.lang.String customerName, @org.jetbrains.annotations.NotNull()
    java.lang.String customerId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.receipt.ReceiptLine> lines, int articleCount, double total, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.receipt.ReceiptPayment> payments, @org.jetbrains.annotations.NotNull()
    java.lang.String paymentMethod, double amountTendered, double changeDue, double discountAmount, double tipAmount, double impoconsumoPercent, double impoconsumoBase, double impoconsumo, @org.jetbrains.annotations.NotNull()
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