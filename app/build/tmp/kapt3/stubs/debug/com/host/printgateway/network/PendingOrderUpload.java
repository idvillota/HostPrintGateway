package com.host.printgateway.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\bH\u00c6\u0003J-\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001a"}, d2 = {"Lcom/host/printgateway/network/PendingOrderUpload;", "", "order", "Lcom/host/printgateway/data/OrderEntity;", "lines", "", "Lcom/host/printgateway/network/AddOrderLineRequest;", "kitchenTicketPrinted", "", "(Lcom/host/printgateway/data/OrderEntity;Ljava/util/List;Z)V", "getKitchenTicketPrinted", "()Z", "getLines", "()Ljava/util/List;", "getOrder", "()Lcom/host/printgateway/data/OrderEntity;", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class PendingOrderUpload {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.OrderEntity order = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.host.printgateway.network.AddOrderLineRequest> lines = null;
    private final boolean kitchenTicketPrinted = false;
    
    public PendingOrderUpload(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.OrderEntity order, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.network.AddOrderLineRequest> lines, boolean kitchenTicketPrinted) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.data.OrderEntity getOrder() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.network.AddOrderLineRequest> getLines() {
        return null;
    }
    
    public final boolean getKitchenTicketPrinted() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.data.OrderEntity component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.host.printgateway.network.AddOrderLineRequest> component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.network.PendingOrderUpload copy(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.OrderEntity order, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.network.AddOrderLineRequest> lines, boolean kitchenTicketPrinted) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}