package com.host.printgateway.printer;

/**
 * Builds ESC/POS bytes for a Host sales receipt (58/80mm thermal).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0012\u001a\u00020\u0010H\u0002J \u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0017H\u0002\u00a8\u0006\u0018"}, d2 = {"Lcom/host/printgateway/printer/EscPosReceiptFormatter;", "", "()V", "align", "", "", "mode", "bold", "on", "", "format", "", "receipt", "Lcom/host/printgateway/receipt/SalesReceipt;", "formatTestTicket", "macHint", "", "text", "value", "twoColumns", "left", "right", "width", "", "app_debug"})
public final class EscPosReceiptFormatter {
    
    public EscPosReceiptFormatter() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] format(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.receipt.SalesReceipt receipt) {
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
}