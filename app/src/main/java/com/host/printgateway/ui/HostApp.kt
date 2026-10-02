package com.host.printgateway.ui

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.network.AuthManager
import com.host.printgateway.network.CatalogApi
import com.host.printgateway.network.JwtExpiry
import com.host.printgateway.network.MobileSyncApi
import com.host.printgateway.network.SaleChannel
import com.host.printgateway.network.SaleListenEnd
import com.host.printgateway.network.SyncUnauthorized
import com.host.printgateway.ui.home.HomeScreen
import com.host.printgateway.ui.login.LoginScreen
import com.host.printgateway.ui.navigation.AppRoute
import com.host.printgateway.ui.navigation.OrderingStep
import com.host.printgateway.ui.navigation.PaymentStep
import com.host.printgateway.ui.order.OrderSession
import com.host.printgateway.ui.order.ServiceHost
import com.host.printgateway.ui.payment.PaymentHost
import com.host.printgateway.ui.settings.SettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HostApp(
    settings: GatewaySettings,
    database: PrintGatewayDatabase,
    authManager: AuthManager,
    renewSessionTick: Int,
    onEnsurePermissions: () -> Unit,
    onStartGateway: () -> Unit,
    onStopGateway: () -> Unit,
    onExit: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val savedCredentials = remember { authManager.getSavedCredentials() }
    val session = remember { OrderSession() }

    var route by remember {
        mutableStateOf<AppRoute>(
            if (settings.deviceToken.isNotBlank()) {
                AppRoute.Home
            } else {
                AppRoute.Login
            },
        )
    }
    var settingsReturn by remember { mutableStateOf<AppRoute>(AppRoute.Login) }
    var apiUrl by remember { mutableStateOf(settings.apiBaseUrl) }
    var printerMac by remember { mutableStateOf(settings.printerMac) }
    var email by remember { mutableStateOf(savedCredentials?.email.orEmpty()) }
    var password by remember { mutableStateOf(savedCredentials?.password.orEmpty()) }
    var tenantSlug by remember { mutableStateOf(savedCredentials?.tenantSlug.orEmpty()) }
    var token by remember { mutableStateOf(settings.deviceToken) }
    var loginMessage by remember { mutableStateOf("") }
    var settingsStatus by remember { mutableStateOf("") }
    var autoSyncEnabled by remember { mutableStateOf(settings.autoSyncEnabled) }
    var offlineMode by remember { mutableStateOf(settings.offlineMode) }
    var offlineNotice by remember { mutableStateOf(OfflineNotice.None) }
    var offlineDetail by remember { mutableStateOf("") }
    var offlineSyncing by remember { mutableStateOf(false) }
    var confirmExit by remember { mutableStateOf(false) }
    val offlineModeNow = rememberUpdatedState(offlineMode)
    var catalogRevision by remember { mutableIntStateOf(0) }
    var uploadNonce by remember { mutableIntStateOf(0) }
    var savedApiUrl by remember { mutableStateOf(settings.apiBaseUrl) }
    var loggingIn by remember {
        mutableStateOf(
            settings.deviceToken.isBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                tenantSlug.isNotBlank() &&
                apiUrl.isNotBlank(),
        )
    }

    fun persistConnection() {
        settings.apiBaseUrl = apiUrl
        settings.printerMac = printerMac
        savedApiUrl = settings.apiBaseUrl
    }

    fun openSettings() {
        settingsReturn = route
        route = AppRoute.Settings
    }

    fun deviceId(): String = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID,
    ) ?: "unknown-device"

    fun notifyIfUnauthorized(error: Throwable?) {
        if (error is SyncUnauthorized) {
            SessionRenewalNotifier.show(context)
        }
    }

    fun activateOfflineMode() {
        offlineMode = true
        settings.offlineMode = true
        offlineNotice = OfflineNotice.None
        offlineDetail = ""
    }

    fun leaveOfflineMode() {
        if (offlineSyncing) return
        offlineSyncing = true
        offlineDetail = ""
        scope.launch {
            val outcome = withContext(Dispatchers.IO) {
                pushThenPull(database, token, savedApiUrl, deviceId(), settings)
            }
            offlineSyncing = false
            when (outcome) {
                OfflineSyncResult.Done -> {
                    offlineMode = false
                    settings.offlineMode = false
                    offlineNotice = OfflineNotice.None
                    catalogRevision++
                }
                OfflineSyncResult.Unauthorized -> SessionRenewalNotifier.show(context)
                is OfflineSyncResult.Failed -> offlineDetail = outcome.message
            }
        }
    }

    LaunchedEffect(Unit) {
        val canRefresh = email.isNotBlank() &&
            password.isNotBlank() &&
            tenantSlug.isNotBlank() &&
            apiUrl.isNotBlank()
        if (!canRefresh) {
            loggingIn = false
            return@LaunchedEffect
        }
        val result = withContext(Dispatchers.IO) {
            authManager.loginWithSavedCredentials(apiUrl)
        }
        loggingIn = false
        if (result.isSuccess) {
            token = result.getOrThrow().accessToken
            if (route is AppRoute.Login) {
                route = AppRoute.Home
            }
        } else if (token.isBlank()) {
            loginMessage = result.exceptionOrNull()?.message ?: "No se pudo recuperar la sesión"
        }
    }

    LaunchedEffect(renewSessionTick) {
        if (renewSessionTick == 0) return@LaunchedEffect
        val canRefresh = email.isNotBlank() &&
            password.isNotBlank() &&
            tenantSlug.isNotBlank() &&
            apiUrl.isNotBlank()
        if (!canRefresh) return@LaunchedEffect
        val result = withContext(Dispatchers.IO) {
            authManager.loginWithSavedCredentials(apiUrl)
        }
        if (result.isSuccess) {
            token = result.getOrThrow().accessToken
        }
    }

    LaunchedEffect(token) {
        if (token.isBlank()) return@LaunchedEffect
        val expiresAt = JwtExpiry.expiresAtEpochMs(token) ?: return@LaunchedEffect
        val wait = expiresAt - System.currentTimeMillis() - 5 * 60 * 1000L
        if (wait > 0) delay(wait)
        SessionRenewalNotifier.show(context)
    }

    LaunchedEffect(token, savedApiUrl, offlineMode) {
        if (token.isBlank() || savedApiUrl.isBlank()) {
            offlineNotice = OfflineNotice.None
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
                    offlineNotice = if (offlineMode) OfflineNotice.Resume else OfflineNotice.None
                }
                !offlineMode && now - lastReachableAt >= OFFLINE_AFTER_MS -> {
                    offlineNotice = OfflineNotice.Offer
                }
                offlineMode && offlineNotice == OfflineNotice.Resume -> {
                    offlineNotice = OfflineNotice.None
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
                catalogRevision++
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
                catalogRevision++
            }
        }
    }

    LaunchedEffect(uploadNonce, token, savedApiUrl) {
        if (uploadNonce == 0 || offlineModeNow.value || token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
        val result = withContext(Dispatchers.IO) {
            RestaurantRepository(database, token).syncPendingOrders(savedApiUrl, deviceId())
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
        val currentDeviceId = deviceId()
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
                            catalogRevision++
                        }
                    },
                    onTablesAvailable = { tableIds ->
                        scope.launch {
                            val changed = withContext(Dispatchers.IO) {
                                repository.releaseNotifiedTables(tableIds)
                            }
                            if (changed) catalogRevision++
                        }
                    },
                    onConnected = {
                        scope.launch {
                            val unauthorized = withContext(Dispatchers.IO) {
                                val api = MobileSyncApi(savedApiUrl, token)
                                val registered = api.registerDevice(currentDeviceId)
                                if (registered.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                val catalog = repository.syncCatalog(savedApiUrl)
                                if (catalog.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                if (catalog.isSuccess) repository.closeOrdersOnFreeHostTables()
                                val released = repository.releaseTablesSettledOnHost(savedApiUrl)
                                if (released.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                val uploaded = repository.syncPendingOrders(savedApiUrl, currentDeviceId)
                                if (uploaded.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                val pulled = repository.catchUpOpenAccounts(
                                    savedApiUrl,
                                    currentDeviceId,
                                    settings.saleCursor,
                                )
                                if (pulled.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                pulled.onSuccess { cursor ->
                                    if (cursor.isNotBlank()) settings.saleCursor = cursor
                                }
                                false
                            }
                            if (unauthorized) {
                                SessionRenewalNotifier.show(context)
                            } else {
                                catalogRevision++
                            }
                        }
                    },
                )
            } finally {
                channel.close()
            }
            if (end == SaleListenEnd.Unauthorized) {
                SessionRenewalNotifier.show(context)
                break
            }
            delay(5_000)
        }
    }

    LaunchedEffect(route) {
        if (route !is AppRoute.Home) confirmExit = false
    }
    LaunchedEffect(confirmExit) {
        if (!confirmExit) return@LaunchedEffect
        delay(2_000)
        confirmExit = false
    }
    BackHandler(enabled = route is AppRoute.Home) {
        if (confirmExit) {
            onExit()
        } else {
            confirmExit = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
    when (val current = route) {
        AppRoute.Login -> LoginScreen(
            email = email,
            password = password,
            tenantSlug = tenantSlug,
            message = loginMessage,
            busy = loggingIn,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onTenantChange = { tenantSlug = it },
            onLogin = {
                persistConnection()
                loggingIn = true
                loginMessage = ""
                scope.launch {
                    val result = withContext(Dispatchers.IO) {
                        authManager.login(
                            baseUrl = apiUrl,
                            email = email,
                            password = password,
                            tenantSlug = tenantSlug,
                        )
                    }
                    loggingIn = false
                    result.fold(
                        onSuccess = { response ->
                            token = response.accessToken
                            route = AppRoute.Home
                        },
                        onFailure = { error ->
                            token = ""
                            loginMessage = error.message ?: "No se pudo iniciar sesión"
                        },
                    )
                }
            },
            onOpenSettings = ::openSettings,
        )

        AppRoute.Settings -> SettingsScreen(
            apiUrl = apiUrl,
            printerMac = printerMac,
            signedIn = token.isNotBlank(),
            status = settingsStatus,
            database = database,
            onApiUrlChange = { apiUrl = it },
            onPrinterMacChange = { printerMac = it },
            onBack = {
                persistConnection()
                route = settingsReturn
            },
            onStartGateway = {
                persistConnection()
                when {
                    token.isBlank() -> settingsStatus = "Primero inicia sesión."
                    printerMac.isBlank() -> settingsStatus = "Indica la MAC de la impresora."
                    else -> {
                        onEnsurePermissions()
                        onStartGateway()
                        settingsStatus = "Gateway iniciado"
                    }
                }
            },
            onStopGateway = {
                onStopGateway()
                settingsStatus = "Gateway detenido"
            },
            onLogout = {
                authManager.logout()
                token = ""
                email = ""
                password = ""
                tenantSlug = ""
                session.reset()
                settingsStatus = ""
                loginMessage = ""
                route = AppRoute.Login
            },
            onStatus = { settingsStatus = it },
            autoSyncEnabled = autoSyncEnabled,
            autoSyncIntervalMinutes = (settings.autoSyncIntervalMs / 60_000L).toInt(),
            onToggleAutoSync = {
                autoSyncEnabled = !autoSyncEnabled
                settings.autoSyncEnabled = autoSyncEnabled
            },
            offlineMode = offlineMode,
            offlineBusy = offlineSyncing,
            onToggleOffline = {
                if (offlineMode) leaveOfflineMode() else activateOfflineMode()
            },
        )

        AppRoute.Home -> HomeScreen(
            onOpenTables = { route = AppRoute.Ordering(OrderingStep.Tables) },
            onOpenPayments = { route = AppRoute.Paying(PaymentStep.Accounts) },
            onOpenSettings = ::openSettings,
        )

        is AppRoute.Paying -> PaymentHost(
            step = current.step,
            apiUrl = apiUrl,
            deviceToken = token,
            printerMac = printerMac,
            database = database,
            settings = settings,
            onStep = { route = AppRoute.Paying(it) },
            onLeave = { route = AppRoute.Home },
            onOpenSettings = ::openSettings,
            catalogRevision = catalogRevision,
            offlineMode = offlineMode,
        )

        is AppRoute.Ordering -> ServiceHost(
            step = current.step,
            apiUrl = apiUrl,
            deviceToken = token,
            printerMac = printerMac,
            database = database,
            settings = settings,
            session = session,
            onStep = { route = AppRoute.Ordering(it) },
            onOpenSettings = ::openSettings,
            onLeave = { route = AppRoute.Home },
            catalogRevision = catalogRevision,
            onLocalOrderSaved = { uploadNonce++ },
            offlineMode = offlineMode,
        )
    }
        if (confirmExit && route is AppRoute.Home) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding()
                    .zIndex(2f),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.onSurface,
                shadowElevation = 4.dp,
            ) {
                Text(
                    text = "Pulsa otra vez para salir",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        if (offlineNotice != OfflineNotice.None && token.isNotBlank()) {
            OfflineModeBanner(
                modifier = Modifier.align(Alignment.BottomCenter),
                notice = offlineNotice,
                busy = offlineSyncing,
                detail = offlineDetail,
                onActivate = ::activateOfflineMode,
                onResume = ::leaveOfflineMode,
            )
        }
    }
}

private const val OFFLINE_AFTER_MS = 20_000L
private const val REACH_POLL_MS = 5_000L

private enum class OfflineNotice { None, Offer, Resume }

private sealed interface OfflineSyncResult {
    data object Done : OfflineSyncResult
    data object Unauthorized : OfflineSyncResult
    data class Failed(val message: String) : OfflineSyncResult
}

private suspend fun pushThenPull(
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

@Composable
private fun OfflineModeBanner(
    modifier: Modifier = Modifier,
    notice: OfflineNotice,
    busy: Boolean,
    detail: String,
    onActivate: () -> Unit,
    onResume: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1f)
            .navigationBarsPadding()
            .padding(12.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = if (notice == OfflineNotice.Offer) {
                    "Problema con la conexión. Puedes activar el modo sin internet."
                } else {
                    "HOST volvió. Desactiva el modo para enviar lo guardado y traer las mesas."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            if (detail.isNotBlank()) {
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Button(
                onClick = if (notice == OfflineNotice.Offer) onActivate else onResume,
                enabled = !busy,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    if (busy) {
                        "Sincronizando…"
                    } else if (notice == OfflineNotice.Offer) {
                        "Activar modo sin internet"
                    } else {
                        "Desactivar y sincronizar"
                    },
                )
            }
        }
    }
}
