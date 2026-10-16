package com.host.printgateway.printer;

/**
 * Builds ESC/POS bytes for a Host sales receipt, following the 80 mm PDF.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u0000 )2\u00020\u0001:\u0001)B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u001e\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u001a\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0015J\u0010\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000fH\u0002J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u000e\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u000fJ\u0010\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001aH\u0002J\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u00042\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u000fH\u0002J\u0016\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J \u0010$\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u000f2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020(H\u0002\u00a8\u0006*"}, d2 = {"Lcom/host/printgateway/printer/EscPosReceiptFormatter;", "", "()V", "align", "", "", "mode", "bold", "on", "", "centered", "", "out", "", "value", "", "format", "", "receipt", "Lcom/host/printgateway/receipt/SalesReceipt;", "logo", "Landroid/graphics/Bitmap;", "formatIssued", "raw", "formatQuantity", "quantity", "", "formatTestTicket", "macHint", "money", "amount", "paymentsOf", "Lcom/host/printgateway/receipt/ReceiptPayment;", "rangeLabel", "invoiceNumber", "text", "twoColumns", "left", "right", "width", "", "Companion", "app_debug"})
public final class EscPosReceiptFormatter {
    @org.jetbrains.annotations.NotNull()
    @java.lang.Deprecated()
    public static final java.lang.String RULE = "--------------------------------";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> MONTHS = null;
    @org.jetbrains.annotations.NotNull()
    private static final com.host.printgateway.printer.EscPosReceiptFormatter.Companion Companion = null;
    
    public EscPosReceiptFormatter() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] format(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.receipt.SalesReceipt receipt, @org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap logo) {
        return null;
    }
    
    /**
     * Minimal self-test ticket (no Host XML required).
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] formatTestTicket(@org.jetbrains.annotations.NotNull()
    java.lang.String macHint) {
        return null;
    }
    
    private final void centered(java.util.List<java.lang.Byte> out, java.lang.String value) {
    }
    
    private final java.util.List<com.host.printgateway.receipt.ReceiptPayment> paymentsOf(com.host.printgateway.receipt.SalesReceipt receipt) {
        return null;
    }
    
    private final java.lang.String rangeLabel(java.lang.String invoiceNumber) {
        return null;
    }
    
    private final java.lang.String formatQuantity(double quantity) {
        return null;
    }
    
    private final java.lang.String formatIssued(java.lang.String raw) {
        return null;
    }
    
    private final java.lang.String money(double amount) {
        return null;
    }
    
    private final java.util.List<java.lang.Byte> text(java.lang.String value) {
        return null;
    }
    
    private final java.util.List<java.lang.Byte> align(byte mode) {
        return null;
    }
    
    private final java.util.List<java.lang.Byte> bold(boolean on) {
        return null;
    }
    
    private final java.lang.String twoColumns(java.lang.String left, java.lang.String right, int width) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/host/printgateway/printer/EscPosReceiptFormatter$Companion;", "", "()V", "MONTHS", "", "", "getMONTHS", "()Ljava/util/List;", "RULE", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getMONTHS() {
            return null;
        }
    }
}