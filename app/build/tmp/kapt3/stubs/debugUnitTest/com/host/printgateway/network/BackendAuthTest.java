package com.host.printgateway.network;

/**
 * Host API returns a raw JWT in `accessToken` (no Bearer prefix).
 * Host Lite stores that value in GatewaySettings.deviceToken and every
 * ApiClient / SaleChannel request must send exactly `Authorization: Bearer <jwt>`.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007\u00a8\u0006\u0006"}, d2 = {"Lcom/host/printgateway/network/BackendAuthTest;", "", "()V", "authorizationValue_matches_host_jwt_bearer_contract", "", "authorization_header_name_is_standard", "app_debugUnitTest"})
public final class BackendAuthTest {
    
    public BackendAuthTest() {
        super();
    }
    
    @org.junit.Test()
    public final void authorizationValue_matches_host_jwt_bearer_contract() {
    }
    
    @org.junit.Test()
    public final void authorization_header_name_is_standard() {
    }
}