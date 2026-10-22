package com.host.printgateway.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J.\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0082@\u00a2\u0006\u0002\u0010\u000fJ4\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u001cJ6\u0010\u001d\u001a\u00020\u001a2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00110\u00162\u0006\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020#H\u0086@\u00a2\u0006\u0002\u0010$J&\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u00112\u0006\u0010(\u001a\u00020\u00112\u0006\u0010)\u001a\u00020#H\u0086@\u00a2\u0006\u0002\u0010*J0\u0010+\u001a\u00020\u00112\u0006\u0010,\u001a\u00020\b2\u0006\u0010\'\u001a\u00020\u00112\u0006\u0010(\u001a\u00020\u00112\u0006\u0010)\u001a\u00020#2\u0006\u0010\r\u001a\u00020\u000eH\u0002J6\u0010-\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\b2\u0006\u0010\'\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010)\u001a\u00020#2\u0006\u0010\r\u001a\u00020\u000eH\u0082@\u00a2\u0006\u0002\u0010.J\u0016\u0010/\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u001cR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/host/printgateway/data/OrderRepository;", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "(Lcom/host/printgateway/data/PrintGatewayDatabase;)V", "dao", "Lcom/host/printgateway/data/RestaurantDao;", "combine", "Lcom/host/printgateway/data/OrderEntity;", "source", "target", "destination", "Lcom/host/printgateway/data/DiningTableEntity;", "now", "", "(Lcom/host/printgateway/data/OrderEntity;Lcom/host/printgateway/data/OrderEntity;Lcom/host/printgateway/data/DiningTableEntity;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createOrder", "", "table", "waiterName", "deviceId", "lines", "", "Lcom/host/printgateway/data/OrderDraftLine;", "(Lcom/host/printgateway/data/DiningTableEntity;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "keepLocalLinesPending", "", "orderId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markPaidForSync", "orderIds", "paymentMethod", "tip", "", "printReceipt", "", "(Ljava/util/List;Ljava/lang/String;DZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveOpenOrders", "Lcom/host/printgateway/data/MoveTableOutcome;", "fromTableId", "toTableId", "merge", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveStatus", "order", "placeOnTable", "(Lcom/host/printgateway/data/OrderEntity;Ljava/lang/String;Lcom/host/printgateway/data/DiningTableEntity;ZJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshTotals", "app_debug"})
public final class OrderRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.PrintGatewayDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.RestaurantDao dao = null;
    
    public OrderRepository(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database) {
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markPaidForSync(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> orderIds, @org.jetbrains.annotations.NotNull()
    java.lang.String paymentMethod, double tip, boolean printReceipt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object moveOpenOrders(@org.jetbrains.annotations.NotNull()
    java.lang.String fromTableId, @org.jetbrains.annotations.NotNull()
    java.lang.String toTableId, boolean merge, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.host.printgateway.data.MoveTableOutcome> $completion) {
        return null;
    }
    
    private final java.lang.Object placeOnTable(com.host.printgateway.data.OrderEntity order, java.lang.String fromTableId, com.host.printgateway.data.DiningTableEntity destination, boolean merge, long now, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object combine(com.host.printgateway.data.OrderEntity source, com.host.printgateway.data.OrderEntity target, com.host.printgateway.data.DiningTableEntity destination, long now, kotlin.coroutines.Continuation<? super com.host.printgateway.data.OrderEntity> $completion) {
        return null;
    }
    
    private final java.lang.Object keepLocalLinesPending(java.lang.String orderId, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object refreshTotals(java.lang.String orderId, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String moveStatus(com.host.printgateway.data.OrderEntity order, java.lang.String fromTableId, java.lang.String toTableId, boolean merge, long now) {
        return null;
    }
}