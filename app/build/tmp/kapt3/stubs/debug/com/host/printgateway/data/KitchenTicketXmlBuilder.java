package com.host.printgateway.data;

/**
 * Builds kitchen-ticket XML stored in Room and printed via ESC/POS.
 * Same document shape as before (no backend required offline).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J6\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\b\u0010\t\u001a\u0004\u0018\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0004H\u0002J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0004H\u0002\u00a8\u0006\u0018"}, d2 = {"Lcom/host/printgateway/data/KitchenTicketXmlBuilder;", "", "()V", "build", "", "table", "Lcom/host/printgateway/data/DiningTableEntity;", "orderNumber", "waiterName", "printer", "Lcom/host/printgateway/data/PrinterStationEntity;", "lines", "", "Lcom/host/printgateway/data/OrderDraftLine;", "formatQuantity", "quantity", "", "utcNow", "writeXmlValue", "", "serializer", "Lorg/xmlpull/v1/XmlSerializer;", "tagName", "value", "app_debug"})
public final class KitchenTicketXmlBuilder {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.KitchenTicketXmlBuilder INSTANCE = null;
    
    private KitchenTicketXmlBuilder() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String build(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.DiningTableEntity table, @org.jetbrains.annotations.NotNull()
    java.lang.String orderNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String waiterName, @org.jetbrains.annotations.Nullable()
    com.host.printgateway.data.PrinterStationEntity printer, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.OrderDraftLine> lines) {
        return null;
    }
    
    private final void writeXmlValue(org.xmlpull.v1.XmlSerializer serializer, java.lang.String tagName, java.lang.String value) {
    }
    
    private final java.lang.String formatQuantity(double quantity) {
        return null;
    }
    
    private final java.lang.String utcNow() {
        return null;
    }
}