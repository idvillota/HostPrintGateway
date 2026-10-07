package com.host.printgateway.network;

/**
 * Cliente HTTP de autenticación del backend.
 *
 * POST /api/Auth/login
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J1\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0011"}, d2 = {"Lcom/host/printgateway/network/AuthApi;", "", "baseUrl", "", "(Ljava/lang/String;)V", "client", "Lcom/host/printgateway/network/ApiClient;", "login", "Lkotlin/Result;", "Lcom/host/printgateway/network/LoginResponse;", "email", "password", "tenantSlug", "login-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "parseLoginResponse", "body", "app_release"})
public final class AuthApi {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String baseUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.network.ApiClient client = null;
    
    public AuthApi(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl) {
        super();
    }
    
    private final com.host.printgateway.network.LoginResponse parseLoginResponse(java.lang.String body) {
        return null;
    }
}