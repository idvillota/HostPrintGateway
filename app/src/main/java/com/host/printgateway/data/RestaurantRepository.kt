package com.host.printgateway.data

import com.host.printgateway.network.RemoteSale

/**
 * Facade kept for existing call sites (HostApp, ServiceHost, PaymentHost).
 * Delegates to Catalog / Order / Sync repositories — same public behavior as before.
 */
class RestaurantRepository(
    database: PrintGatewayDatabase,
    deviceToken: String,
) {
    private val catalog = CatalogRepository(database, deviceToken)
    private val orders = OrderRepository(database)
    private val sync = SyncRepository(database, deviceToken)

    suspend fun syncCatalog(baseUrl: String): Result<Int> =
        catalog.syncCatalog(baseUrl)

    suspend fun createOrder(
        table: DiningTableEntity,
        waiterName: String,
        deviceId: String,
        lines: List<OrderDraftLine>,
    ): String = orders.createOrder(table, waiterName, deviceId, lines)

    suspend fun markPaidForSync(orderIds: List<String>, paymentMethod: String, tip: Double) =
        orders.markPaidForSync(orderIds, paymentMethod, tip)

    suspend fun closeOrdersOnFreeHostTables() =
        sync.closeOrdersOnFreeHostTables()

    suspend fun releaseTablesSettledOnHost(baseUrl: String): Result<Boolean> =
        sync.releaseTablesSettledOnHost(baseUrl)

    suspend fun releaseNotifiedTables(tableIds: List<String>): Boolean =
        sync.releaseNotifiedTables(tableIds)

    suspend fun syncPendingOrders(baseUrl: String, deviceId: String): Result<Int> =
        sync.syncPendingOrders(baseUrl, deviceId)

    suspend fun catchUpOpenAccounts(baseUrl: String, deviceId: String, since: String): Result<String> =
        sync.catchUpOpenAccounts(baseUrl, deviceId, since)

    suspend fun pullMissedSales(baseUrl: String, deviceId: String, since: String): Result<String> =
        sync.pullMissedSales(baseUrl, deviceId, since)

    suspend fun applyRemoteSale(sale: RemoteSale) =
        sync.applyRemoteSale(sale)
}
