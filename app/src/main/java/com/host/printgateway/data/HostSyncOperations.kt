package com.host.printgateway.data

import com.host.printgateway.network.MobileSyncApi
import com.host.printgateway.network.SyncUnauthorized

/**
 * Offline leave + sale-channel connect sequences.
 * Keep these separate — order of steps differs on purpose.
 */
object HostSyncOperations {

    sealed interface OfflineSyncResult {
        data object Done : OfflineSyncResult
        data object Unauthorized : OfflineSyncResult
        data class Failed(val message: String) : OfflineSyncResult
    }

    /**
     * Leave offline mode: upload local work, then refresh from HOST.
     * Sequence: pending orders → catalog → close free → release settled → catch-up sales.
     */
    suspend fun pushThenPull(
        database: PrintGatewayDatabase,
        token: String,
        baseUrl: String,
        deviceId: String,
        settings: GatewaySettings,
    ): OfflineSyncResult {
        val repository = RestaurantRepository(database, token)
        val uploaded = repository.syncPendingOrders(baseUrl, deviceId)
        if (uploaded.exceptionOrNull() is SyncUnauthorized) return OfflineSyncResult.Unauthorized
        if (uploaded.isFailure) {
            return OfflineSyncResult.Failed(
                uploaded.exceptionOrNull()?.message ?: "No se pudo enviar lo guardado.",
            )
        }
        val catalog = repository.syncCatalog(baseUrl)
        if (catalog.exceptionOrNull() is SyncUnauthorized) return OfflineSyncResult.Unauthorized
        if (catalog.isFailure) {
            return OfflineSyncResult.Failed(
                catalog.exceptionOrNull()?.message ?: "No se pudo traer el catálogo.",
            )
        }
        repository.closeOrdersOnFreeHostTables()
        val released = repository.releaseTablesSettledOnHost(baseUrl)
        if (released.exceptionOrNull() is SyncUnauthorized) return OfflineSyncResult.Unauthorized
        if (released.isFailure) {
            return OfflineSyncResult.Failed(
                released.exceptionOrNull()?.message ?: "No se pudieron actualizar las mesas.",
            )
        }
        val pulled = repository.catchUpOpenAccounts(baseUrl, deviceId, settings.saleCursor)
        if (pulled.exceptionOrNull() is SyncUnauthorized) return OfflineSyncResult.Unauthorized
        if (pulled.isFailure) {
            return OfflineSyncResult.Failed(
                pulled.exceptionOrNull()?.message ?: "No se pudieron traer las cuentas.",
            )
        }
        pulled.onSuccess { cursor ->
            if (cursor.isNotBlank()) settings.saleCursor = cursor
        }
        return OfflineSyncResult.Done
    }

    /**
     * After sale WebSocket connects.
     * Sequence: register device → catalog → close free → release settled → upload pending → catch-up.
     * @return true if session is unauthorized
     */
    suspend fun reconcileAfterSaleChannelConnect(
        database: PrintGatewayDatabase,
        token: String,
        baseUrl: String,
        deviceId: String,
        settings: GatewaySettings,
    ): Boolean {
        val repository = RestaurantRepository(database, token)
        val api = MobileSyncApi(baseUrl, token)
        val registered = api.registerDevice(deviceId)
        if (registered.exceptionOrNull() is SyncUnauthorized) return true
        val catalog = repository.syncCatalog(baseUrl)
        if (catalog.exceptionOrNull() is SyncUnauthorized) return true
        if (catalog.isSuccess) repository.closeOrdersOnFreeHostTables()
        val released = repository.releaseTablesSettledOnHost(baseUrl)
        if (released.exceptionOrNull() is SyncUnauthorized) return true
        val uploaded = repository.syncPendingOrders(baseUrl, deviceId)
        if (uploaded.exceptionOrNull() is SyncUnauthorized) return true
        val pulled = repository.catchUpOpenAccounts(baseUrl, deviceId, settings.saleCursor)
        if (pulled.exceptionOrNull() is SyncUnauthorized) return true
        pulled.onSuccess { cursor ->
            if (cursor.isNotBlank()) settings.saleCursor = cursor
        }
        return false
    }
}
