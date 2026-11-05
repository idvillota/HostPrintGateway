package com.host.printgateway.data

import com.host.printgateway.network.RelocateOrderResult
import com.host.printgateway.network.RemoteSale
import com.host.printgateway.network.SalesOrderApi
import com.host.printgateway.network.SyncUnauthorized

/**
 * Facade kept for existing call sites (HostApp, ServiceHost, PaymentHost).
 * Delegates to Catalog / Order / Sync repositories — same public behavior as before.
 */
class RestaurantRepository(
    database: PrintGatewayDatabase,
    private val deviceToken: String,
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

    suspend fun markPaidForSync(
        orderIds: List<String>,
        paymentMethod: String,
        tip: Double,
        printReceipt: Boolean = false,
    ) = orders.markPaidForSync(orderIds, paymentMethod, tip, printReceipt)

    suspend fun relocateTable(
        fromTableId: String,
        toTableId: String,
        merge: Boolean?,
        baseUrl: String,
        online: Boolean,
    ): Result<MoveTableOutcome> = runCatching {
        val preview = orders.validateRelocate(fromTableId, toTableId, merge)
        if (preview !is MoveTableOutcome.Moved) {
            return@runCatching preview
        }
        if (!online) {
            return@runCatching MoveTableOutcome.Rejected("El cambio de mesa requiere conexión con HOST.")
        }
        val remoteId = orders.sourceRemoteId(fromTableId)
        if (!remoteId.isNullOrBlank()) {
            val relocated = SalesOrderApi(baseUrl, deviceToken)
                .relocate(remoteId, toTableId, merge)
                .getOrThrow()
            when {
                relocated.action.equals(RelocateOrderResult.MERGE_REQUIRED, ignoreCase = true) -> {
                    return@runCatching MoveTableOutcome.MergeRequired(
                        relocated.targetTableCode.ifBlank { toTableId },
                    )
                }
                relocated.action.equals(RelocateOrderResult.CANCELLED, ignoreCase = true) -> {
                    return@runCatching MoveTableOutcome.Rejected(
                        relocated.message.ifBlank { "Cambio de mesa cancelado." },
                    )
                }
            }
            val targetRemoteId = relocated.targetOrderId.ifBlank { remoteId }
            if (relocated.lines.isNotEmpty()) {
                sync.replaceHostLines(
                    RemoteSale(
                        remoteOrderId = targetRemoteId,
                        tableId = toTableId,
                        tableCode = relocated.targetTableCode,
                        waiterName = "",
                        cursor = "",
                        lines = relocated.lines,
                    ),
                )
            }
            return@runCatching orders.relocateOpenOrders(
                fromTableId = fromTableId,
                toTableId = toTableId,
                merge = merge,
                queueHost = false,
                hostMerged = relocated.action.equals(RelocateOrderResult.MERGED, ignoreCase = true),
                hostTargetRemoteId = targetRemoteId,
            )
        }
        orders.relocateOpenOrders(
            fromTableId = fromTableId,
            toTableId = toTableId,
            merge = merge,
            queueHost = false,
            hostMerged = false,
            hostTargetRemoteId = remoteId,
        )
    }.fold(
        onSuccess = { Result.success(it) },
        onFailure = { error ->
            if (error is SyncUnauthorized) Result.failure(error)
            else Result.success(MoveTableOutcome.Rejected(error.message ?: "No se pudo cambiar la mesa."))
        },
    )

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
