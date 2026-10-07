package com.host.printgateway.network;

/**
 * Reachability must hit GET /health — never salon table summaries —
 * so Host Lite does not hammer ListTableSummaries every few seconds.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\bH\u0007J\b\u0010\n\u001a\u00020\bH\u0007J\b\u0010\u000b\u001a\u00020\bH\u0007J\b\u0010\f\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/host/printgateway/network/CatalogApiPingTest;", "", "()V", "server", "Lokhttp3/mockwebserver/MockWebServer;", "baseUrl", "", "ping_calls_health_not_sales_order_tables", "", "ping_fails_on_non_success_status", "ping_maps_401_to_sync_unauthorized", "setUp", "tearDown", "app_debugUnitTest"})
public final class CatalogApiPingTest {
    private okhttp3.mockwebserver.MockWebServer server;
    
    public CatalogApiPingTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setUp() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
    
    @org.junit.Test()
    public final void ping_calls_health_not_sales_order_tables() {
    }
    
    @org.junit.Test()
    public final void ping_maps_401_to_sync_unauthorized() {
    }
    
    @org.junit.Test()
    public final void ping_fails_on_non_success_status() {
    }
    
    private final java.lang.String baseUrl() {
        return null;
    }
}