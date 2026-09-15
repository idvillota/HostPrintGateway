package com.host.printgateway.printer;

/**
 * Turns a Host print job into ESC/POS bytes.
 * Supported formats: escpos-v1 (Base64), sales-receipt-xml.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/host/printgateway/printer/PrintJobRenderer;", "", "formatter", "Lcom/host/printgateway/printer/EscPosReceiptFormatter;", "(Lcom/host/printgateway/printer/EscPosReceiptFormatter;)V", "toEscPos", "", "job", "Lcom/host/printgateway/network/PrintJobDto;", "Companion", "app_debug"})
public final class PrintJobRenderer {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.printer.EscPosReceiptFormatter formatter = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORMAT_ESCPOS_V1 = "escpos-v1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FORMAT_SALES_RECEIPT_XML = "sales-receipt-xml";
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.printer.PrintJobRenderer.Companion Companion = null;
    
    public PrintJobRenderer(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.printer.EscPosReceiptFormatter formatter) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] toEscPos(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.network.PrintJobDto job) {
        return null;
    }
    
    public PrintJobRenderer() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/host/printgateway/printer/PrintJobRenderer$Companion;", "", "()V", "FORMAT_ESCPOS_V1", "", "FORMAT_SALES_RECEIPT_XML", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}