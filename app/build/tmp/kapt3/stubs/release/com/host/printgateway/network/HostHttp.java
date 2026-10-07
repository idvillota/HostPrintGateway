package com.host.printgateway.network;

/**
 * Shared OkHttp clients for Host Lite.
 * Do not shut down these dispatchers — [SaleChannel] reconnects for the app lifetime.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\b\u001a\u0004\b\n\u0010\u0006R\u001b\u0010\f\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\b\u001a\u0004\b\r\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/host/printgateway/network/HostHttp;", "", "()V", "client", "Lokhttp3/OkHttpClient;", "getClient", "()Lokhttp3/OkHttpClient;", "client$delegate", "Lkotlin/Lazy;", "pingClient", "getPingClient", "pingClient$delegate", "webSocketClient", "getWebSocketClient", "webSocketClient$delegate", "app_release"})
public final class HostHttp {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy client$delegate = null;
    
    /**
     * Short timeouts used by connectivity ping.
     */
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy pingClient$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy webSocketClient$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.network.HostHttp INSTANCE = null;
    
    private HostHttp() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient getClient() {
        return null;
    }
    
    /**
     * Short timeouts used by connectivity ping.
     */
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient getPingClient() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient getWebSocketClient() {
        return null;
    }
}