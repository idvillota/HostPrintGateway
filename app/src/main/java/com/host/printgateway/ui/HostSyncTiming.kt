package com.host.printgateway.ui

/**
 * Polling intervals for Host Lite background loops.
 * Kept outside Compose so unit tests can lock the intended cadence.
 */
object HostSyncTiming {
    /** Offer offline mode after consecutive unreachable pings. */
    const val OFFLINE_AFTER_MS = 20_000L

    /** Connectivity check interval ([CatalogApi.ping] → GET /health). */
    const val REACH_POLL_MS = 5_000L

    /**
     * How often to re-check HOST open accounts to free local tables.
     * Uses [com.host.printgateway.network.CatalogApi.fetchTableAccounts]
     * (`GET /api/SalesOrders/tables`), not the reachability ping.
     */
    const val TABLE_SETTLE_POLL_MS = 30_000L
}
