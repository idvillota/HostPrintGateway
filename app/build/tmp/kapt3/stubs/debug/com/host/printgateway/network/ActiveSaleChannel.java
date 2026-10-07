package com.host.printgateway.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tJ\u000e\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0005J\u000e\u0010\r\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0005R\u0016\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/host/printgateway/network/ActiveSaleChannel;", "", "()V", "current", "Ljava/util/concurrent/atomic/AtomicReference;", "Lcom/host/printgateway/network/SaleChannel;", "announce", "", "ready", "", "fresh", "attach", "channel", "detach", "app_debug"})
public final class ActiveSaleChannel {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.concurrent.atomic.AtomicReference<com.host.printgateway.network.SaleChannel> current = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.network.ActiveSaleChannel INSTANCE = null;
    
    private ActiveSaleChannel() {
        super();
    }
    
    public final void attach(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.network.SaleChannel channel) {
    }
    
    public final void detach(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.network.SaleChannel channel) {
    }
    
    public final void announce(boolean ready, boolean fresh) {
    }
}