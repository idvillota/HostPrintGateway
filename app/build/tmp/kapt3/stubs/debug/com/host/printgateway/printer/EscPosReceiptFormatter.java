package com.host.printgateway.printer;

/**
 * Formatea facturas ESC/POS para papel térmico estrecho.
 *
 * Mantiene los modelos SalesReceipt y ReceiptPayment existentes.
 * Ancho lógico inicial: 32 caracteres.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 .2\u00020\u0001:\u0001.B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\t\u001a\u00020\nH\u0002J\u001e\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u001a\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0010H\u0002J\u000e\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001cH\u0002J\u000e\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u0010J\u0010\u0010!\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\u001cH\u0002J\u0018\u0010#\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u00102\u0006\u0010%\u001a\u00020\u0010H\u0002J\u0016\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u00042\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010(\u001a\u00020\u00102\u0006\u0010)\u001a\u00020\u0010H\u0002J\u0016\u0010*\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000eH\u0002J\u0016\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u001e\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00100\u00042\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010-\u001a\u00020\u0007H\u0002\u00a8\u0006/"}, d2 = {"Lcom/host/printgateway/printer/EscPosReceiptFormatter;", "", "()V", "align", "", "", "mode", "", "bold", "on", "", "centered", "", "out", "", "value", "", "format", "", "receipt", "Lcom/host/printgateway/receipt/SalesReceipt;", "logo", "Landroid/graphics/Bitmap;", "formatIssued", "raw", "formatLogo", "formatPercent", "percent", "", "formatQuantity", "quantity", "formatTestTicket", "macHint", "money", "amount", "pairLines", "left", "right", "paymentsOf", "Lcom/host/printgateway/receipt/ReceiptPayment;", "rangeLabel", "invoiceNumber", "separator", "text", "wrap", "width", "Companion", "app_debug"})
public final class EscPosReceiptFormatter {
    @java.lang.Deprecated()
    public static final int RECEIPT_WIDTH = 32;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.Byte> RESET = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.Byte> LINE_SPACING_DEFAULT = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.Byte> CUT_PAPER = null;
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
     * Imprime únicamente el logo.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] formatLogo(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap logo) {
        return null;
    }
    
    /**
     * Ticket de prueba sin necesidad de XML.
     */
    @org.jetbrains.annotations.NotNull()
    public final byte[] formatTestTicket(@org.jetbrains.annotations.NotNull()
    java.lang.String macHint) {
        return null;
    }
    
    private final void centered(java.util.List<java.lang.Byte> out, java.lang.String value) {
    }
    
    private final void separator(java.util.List<java.lang.Byte> out) {
    }
    
    private final java.util.List<com.host.printgateway.receipt.ReceiptPayment> paymentsOf(com.host.printgateway.receipt.SalesReceipt receipt) {
        return null;
    }
    
    /**
     * Dos columnas que nunca se superponen.
     * Si no caben, pasan a dos líneas.
     */
    private final java.lang.String pairLines(java.lang.String left, java.lang.String right) {
        return null;
    }
    
    /**
     * Divide texto por palabras sin eliminar caracteres.
     * Las palabras más largas que el ancho también se dividen.
     */
    private final java.util.List<java.lang.String> wrap(java.lang.String value, int width) {
        return null;
    }
    
    private final java.lang.String formatQuantity(double quantity) {
        return null;
    }
    
    private final java.lang.String formatPercent(double percent) {
        return null;
    }
    
    private final java.lang.String formatIssued(java.lang.String raw) {
        return null;
    }
    
    private final java.lang.String money(double amount) {
        return null;
    }
    
    private final java.lang.String rangeLabel(java.lang.String invoiceNumber) {
        return null;
    }
    
    private final java.util.List<java.lang.Byte> text(java.lang.String value) {
        return null;
    }
    
    private final java.util.List<java.lang.Byte> align(int mode) {
        return null;
    }
    
    private final java.util.List<java.lang.Byte> bold(boolean on) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0007R\u000e\u0010\r\u001a\u00020\u000eX\u0086T\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007\u00a8\u0006\u0011"}, d2 = {"Lcom/host/printgateway/printer/EscPosReceiptFormatter$Companion;", "", "()V", "CUT_PAPER", "", "", "getCUT_PAPER", "()Ljava/util/List;", "LINE_SPACING_DEFAULT", "getLINE_SPACING_DEFAULT", "MONTHS", "", "getMONTHS", "RECEIPT_WIDTH", "", "RESET", "getRESET", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Byte> getRESET() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Byte> getLINE_SPACING_DEFAULT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Byte> getCUT_PAPER() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getMONTHS() {
            return null;
        }
    }
}