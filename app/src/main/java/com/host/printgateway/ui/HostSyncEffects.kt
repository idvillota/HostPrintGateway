package com.host.printgateway.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.HostSyncOperations
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.network.CatalogApi
import com.host.printgateway.network.SaleChannel
import com.host.printgateway.network.SaleListenEnd
import com.host.printgateway.network.SyncUnauthorized
import com.host.printgateway.ui.order.OrderSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class OfflineNotice { None, Offer, Resume }

private const val OFFLINE_AFTER_MS = 20_000L
private const val REACH_POLL_MS = 5_000L

/**
 * Background sync / reachability / sale-stream loops formerly inline in [HostApp].
 * Sequences and keys match the previous LaunchedEffect blocks.
 */
@Composable
fun HostSyncEffects(
    database: PrintGatewayDatabase,
    settings: GatewaySettings,
    token: String,
    savedApiUrl: String,
    offlineMode: Boolean,
    autoSyncEnabled: Boolean,
    uploadNonce: Int,
    offlineModeNow: State<Boolean>,
    session: OrderSession,
    deviceId: String,
    scope: CoroutineScope,
    offlineNotice: OfflineNotice,
    onOfflineNoticeChange: (OfflineNotice) -> Unit,
    onCatalogRevisionBump: () -> Unit,
    onUnauthorized: () -> Unit,
) {
    val offlineNoticeNow = rememberUpdatedState(offlineNotice)

    fun notifyIfUnauthorized(error: Throwable?) {
        if (error is SyncUnauthorized) onUnauthorized()
    }

    LaunchedEffect(token, savedApiUrl, offlineMode) {
        if (token.isBlank() || savedApiUrl.isBlank()) {
            onOfflineNoticeChange(OfflineNotice.None)
            return@LaunchedEffect
        }
        var lastReachableAt = System.currentTimeMillis()
        while (isActive) {
            val ping = withContext(Dispatchers.IO) {
                CatalogApi(savedApiUrl, token).ping()
            }
            val now = System.currentTimeMillis()
            when {
                ping.exceptionOrNull() is SyncUnauthorized -> {
                    notifyIfUnauthorized(ping.exceptionOrNull())
                    lastReachableAt = now
                }
                ping.isSuccess -> {
                    lastReachableAt = now
                    onOfflineNoticeChange(
                        if (offlineMode) OfflineNotice.Resume else OfflineNotice.None,
                    )
                }
                !offlineMode && now - lastReachableAt >= OFFLINE_AFTER_MS -> {
                    onOfflineNoticeChange(OfflineNotice.Offer)
                }
                offlineMode && offlineNoticeNow.value == OfflineNotice.Resume -> {
                    onOfflineNoticeChange(OfflineNotice.None)
                }
            }
            delay(REACH_POLL_MS)
        }
    }

    LaunchedEffect(autoSyncEnabled, token, savedApiUrl, offlineMode) {
        if (offlineMode || !autoSyncEnabled || token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
        val repository = RestaurantRepository(database, token)
        while (isActive) {
            val result = withContext(Dispatchers.IO) {
                val catalog = repository.syncCatalog(savedApiUrl)
                if (catalog.isSuccess) repository.closeOrdersOnFreeHostTables()
                catalog
            }
            if (result.isSuccess) {
                onCatalogRevisionBump()
            } else {
                notifyIfUnauthorized(result.exceptionOrNull())
            }
            delay(settings.autoSyncIntervalMs)
        }
    }

    LaunchedEffect(token, savedApiUrl, offlineMode) {
        if (offlineMode || token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
        val repository = RestaurantRepository(database, token)
        while (isActive) {
            delay(15_000)
            val released = withContext(Dispatchers.IO) {
                repository.releaseTablesSettledOnHost(savedApiUrl)
            }
            if (released.exceptionOrNull() is SyncUnauthorized) {
                notifyIfUnauthorized(released.exceptionOrNull())
            } else if (released.getOrNull() == true) {
                onCatalogRevisionBump()
            }
        }
    }

    LaunchedEffect(uploadNonce, token, savedApiUrl) {
        if (uploadNonce == 0 || offlineModeNow.value || token.isBlank() || savedApiUrl.isBlank()) {
            return@LaunchedEffect
        }
        val result = withContext(Dispatchers.IO) {
            RestaurantRepository(database, token).syncPendingOrders(savedApiUrl, deviceId)
        }
        notifyIfUnauthorized(result.exceptionOrNull())
        session.notice = when {
            result.isSuccess && result.getOrThrow() > 0 -> "Comanda enviada a HOST"
            result.isFailure -> result.exceptionOrNull()?.message ?: "No se pudo enviar la comanda a HOST"
            else -> session.notice
        }
    }

    LaunchedEffect(token, savedApiUrl, offlineMode) {
        if (offlineMode || token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
        val repository = RestaurantRepository(database, token)
        val currentDeviceId = deviceId
        while (isActive) {
            val channel = SaleChannel(savedApiUrl, token, currentDeviceId)
            val end = try {
                channel.listen(
                    onSale = { sale ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                repository.applyRemoteSale(sale)
                                if (sale.cursor.isNotBlank()) {
                                    settings.saleCursor = sale.cursor
                                }
                            }
                            onCatalogRevisionBump()
                        }
                    },
                    onTablesAvailable = { tableIds ->
                        scope.launch {
                            val changed = withContext(Dispatchers.IO) {
                                repository.releaseNotifiedTables(tableIds)
                            }
                            if (changed) onCatalogRevisionBump()
                        }
                    },
                    onConnected = {
                        scope.launch {
                            val unauthorized = withContext(Dispatchers.IO) {
                                HostSyncOperations.reconcileAfterSaleChannelConnect(
                                    database = database,
                                    token = token,
                                    baseUrl = savedApiUrl,
                                    deviceId = currentDeviceId,
                                    settings = settings,
                                )
                            }
                            if (unauthorized) {
                                onUnauthorized()
                            } else {
                                onCatalogRevisionBump()
                            }
                        }
                    },
                )
            } finally {
                channel.close()
            }
            if (end == SaleListenEnd.Unauthorized) {
                onUnauthorized()
                break
            }
            delay(5_000)
        }
    }
}
