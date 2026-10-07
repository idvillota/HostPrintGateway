package com.host.printgateway.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J4\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J,\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u000e2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/host/printgateway/data/OrderRepository;", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "(Lcom/host/printgateway/data/PrintGatewayDatabase;)V", "dao", "Lcom/host/printgateway/data/RestaurantDao;", "createOrder", "", "table", "Lcom/host/printgateway/data/DiningTableEntity;", "waiterName", "deviceId", "lines", "", "Lcom/host/printgateway/data/OrderDraftLine;", "(Lcom/host/printgateway/data/DiningTableEntity;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markPaidForSync", "", "orderIds", "paymentMethod", "tip", "", "(Ljava/util/List;Ljava/lang/String;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
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
    java.lang.String paymentMethod, double tip, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}