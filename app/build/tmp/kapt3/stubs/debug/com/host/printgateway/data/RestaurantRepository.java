package com.host.printgateway.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J8\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0002J4\u0010\u0013\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0010\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J$\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001c\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eJ$\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001c\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b \u0010\u001eJ\b\u0010!\u001a\u00020\u0005H\u0002J \u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u00052\u0006\u0010\'\u001a\u00020\u0005H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006("}, d2 = {"Lcom/host/printgateway/data/RestaurantRepository;", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "deviceToken", "", "(Lcom/host/printgateway/data/PrintGatewayDatabase;Ljava/lang/String;)V", "dao", "Lcom/host/printgateway/data/RestaurantDao;", "buildKitchenTicketXml", "table", "Lcom/host/printgateway/data/DiningTableEntity;", "orderNumber", "waiterName", "printer", "Lcom/host/printgateway/data/PrinterStationEntity;", "lines", "", "Lcom/host/printgateway/data/OrderDraftLine;", "createOrder", "deviceId", "(Lcom/host/printgateway/data/DiningTableEntity;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "formatQuantity", "quantity", "", "syncCatalog", "Lkotlin/Result;", "", "baseUrl", "syncCatalog-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncPendingOrders", "syncPendingOrders-gIAlu-s", "utcNow", "writeXmlValue", "", "serializer", "Lorg/xmlpull/v1/XmlSerializer;", "tagName", "value", "app_debug"})
public final class RestaurantRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.PrintGatewayDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deviceToken = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.RestaurantDao dao = null;
    
    public RestaurantRepository(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceToken) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createOrder(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.DiningTableEntity table, @org.jetbrains.annotations.NotNull()
    java.lang.String waiterName, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.OrderDraftLine> lines, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Genera el XML de una comanda.
     *
     * El XML se almacena como payload de KitchenTicketEntity.
     *
     * La impresora no recibe directamente este XML.
     * KitchenTicketFormatter lo interpreta y lo convierte
     * posteriormente a ESC/POS.
     */
    private final java.lang.String buildKitchenTicketXml(com.host.printgateway.data.DiningTableEntity table, java.lang.String orderNumber, java.lang.String waiterName, com.host.printgateway.data.PrinterStationEntity printer, java.util.List<com.host.printgateway.data.OrderDraftLine> lines) {
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