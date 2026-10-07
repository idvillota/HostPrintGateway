package com.host.printgateway.network;

/**
 * Thin HTTP helper preserving previous Auth / Accept / timeout behavior.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\"\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/host/printgateway/network/ApiClient;", "", "baseUrl", "", "token", "http", "Lokhttp3/OkHttpClient;", "(Ljava/lang/String;Ljava/lang/String;Lokhttp3/OkHttpClient;)V", "root", "call", "Lcom/host/printgateway/network/HttpResult;", "method", "path", "jsonBody", "Companion", "app_release"})
public final class ApiClient {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String token = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient http = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String root = null;
    @org.jetbrains.annotations.NotNull()
    private static final okhttp3.MediaType JSON_MEDIA = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.network.ApiClient.Companion Companion = null;
    
    public ApiClient(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.Nullable()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient http) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.host.printgateway.network.HttpResult call(@org.jetbrains.annotations.NotNull()
    java.lang.String method, @org.jetbrains.annotations.NotNull()
    java.lang.String path, @org.jetbrains.annotations.Nullable()
    java.lang.String jsonBody) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/host/printgateway/network/ApiClient$Companion;", "", "()V", "JSON_MEDIA", "Lokhttp3/MediaType;", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}