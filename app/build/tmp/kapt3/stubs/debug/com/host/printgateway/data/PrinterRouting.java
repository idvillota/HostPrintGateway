package com.host.printgateway.data;

/**
 * Chooses which configured printer receives each kitchen line or the invoice.
 * A printer with no categories is the catch-all for lines nobody else claimed.
 * Lines still unclaimed are sent to every printer so a comanda is not dropped.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004J(\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00042\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004\u00a8\u0006\u000b"}, d2 = {"Lcom/host/printgateway/data/PrinterRouting;", "", "()V", "invoiceMacs", "", "", "printers", "Lcom/host/printgateway/data/PrinterTarget;", "kitchenCopies", "Lcom/host/printgateway/data/KitchenCopy;", "categoryIds", "app_debug"})
public final class PrinterRouting {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.PrinterRouting INSTANCE = null;
    
    private PrinterRouting() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.data.KitchenCopy> kitchenCopies(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> categoryIds, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.PrinterTarget> printers) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> invoiceMacs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.PrinterTarget> printers) {
        return null;
    }
}