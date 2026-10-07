package com.host.printgateway.ui;

/**
 * Polling intervals for Host Lite background loops.
 * Kept outside Compose so unit tests can lock the intended cadence.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/host/printgateway/ui/HostSyncTiming;", "", "()V", "OFFLINE_AFTER_MS", "", "REACH_POLL_MS", "TABLE_SETTLE_POLL_MS", "app_debug"})
public final class HostSyncTiming {
    
    /**
     * Offer offline mode after consecutive unreachable pings.
     */
    public static final long OFFLINE_AFTER_MS = 20000L;
    
    /**
     * Connectivity check interval ([CatalogApi.ping] → GET /health).
     */
    public static final long REACH_POLL_MS = 5000L;
    
    /**
     * How often to re-check HOST open accounts to free local tables.
     * Uses [com.host.printgateway.network.CatalogApi.fetchTableAccounts]
     * (`GET /api/SalesOrders/tables`), not the reachability ping.
     */
    public static final long TABLE_SETTLE_POLL_MS = 30000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.ui.HostSyncTiming INSTANCE = null;
    
    private HostSyncTiming() {
        super();
    }
}