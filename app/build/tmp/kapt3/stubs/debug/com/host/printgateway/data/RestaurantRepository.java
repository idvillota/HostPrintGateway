package com.host.printgateway.data;

/**
 * Facade kept for existing call sites (HostApp, ServiceHost, PaymentHost).
 * Delegates to Catalog / Order / Sync repositories — same public behavior as before.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J4\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u00132\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0017\u0010\u0018J\u000e\u0010\u0019\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u001aJ4\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00052\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H\u0086@\u00a2\u0006\u0002\u0010\"J,\u0010#\u001a\u00020\u000e2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050 2\u0006\u0010%\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\'H\u0086@\u00a2\u0006\u0002\u0010(J4\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00050\u00132\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b*\u0010\u0018J\u001c\u0010+\u001a\u00020,2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00050 H\u0086@\u00a2\u0006\u0002\u0010.J$\u0010/\u001a\b\u0012\u0004\u0012\u00020,0\u00132\u0006\u0010\u0014\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00101J$\u00102\u001a\b\u0012\u0004\u0012\u0002030\u00132\u0006\u0010\u0014\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u00101J,\u00105\u001a\b\u0012\u0004\u0012\u0002030\u00132\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b6\u00107R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00068"}, d2 = {"Lcom/host/printgateway/data/RestaurantRepository;", "", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "deviceToken", "", "(Lcom/host/printgateway/data/PrintGatewayDatabase;Ljava/lang/String;)V", "catalog", "Lcom/host/printgateway/data/CatalogRepository;", "orders", "Lcom/host/printgateway/data/OrderRepository;", "sync", "Lcom/host/printgateway/data/SyncRepository;", "applyRemoteSale", "", "sale", "Lcom/host/printgateway/network/RemoteSale;", "(Lcom/host/printgateway/network/RemoteSale;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "catchUpOpenAccounts", "Lkotlin/Result;", "baseUrl", "deviceId", "since", "catchUpOpenAccounts-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "closeOrdersOnFreeHostTables", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createOrder", "table", "Lcom/host/printgateway/data/DiningTableEntity;", "waiterName", "lines", "", "Lcom/host/printgateway/data/OrderDraftLine;", "(Lcom/host/printgateway/data/DiningTableEntity;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markPaidForSync", "orderIds", "paymentMethod", "tip", "", "(Ljava/util/List;Ljava/lang/String;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "pullMissedSales", "pullMissedSales-BWLJW6A", "releaseNotifiedTables", "", "tableIds", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "releaseTablesSettledOnHost", "releaseTablesSettledOnHost-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncCatalog", "", "syncCatalog-gIAlu-s", "syncPendingOrders", "syncPendingOrders-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RestaurantRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.CatalogRepository catalog = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.OrderRepository orders = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.SyncRepository sync = null;
    
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markPaidForSync(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> orderIds, @org.jetbrains.annotations.NotNull()
    java.lang.String paymentMethod, double tip, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object closeOrdersOnFreeHostTables(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object releaseNotifiedTables(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> tableIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object applyRemoteSale(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.network.RemoteSale sale, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}