package com.host.printgateway.ui

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.network.AuthManager
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

    LaunchedEffect(autoSyncEnabled, token, savedApiUrl) {
        if (!autoSyncEnabled || token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
        val repository = RestaurantRepository(database, token)
        while (isActive) {
            val result = withContext(Dispatchers.IO) {
                repository.syncCatalog(savedApiUrl)
            }
            if (result.isSuccess) {
                catalogRevision++
            } else {
                notifyIfUnauthorized(result.exceptionOrNull())
            }
            delay(settings.autoSyncIntervalMs)
        }
    }

    LaunchedEffect(uploadNonce, token, savedApiUrl) {
        if (uploadNonce == 0 || token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
        val result = withContext(Dispatchers.IO) {
            RestaurantRepository(database, token).syncPendingOrders(savedApiUrl, deviceId())
        }
        notifyIfUnauthorized(result.exceptionOrNull())
    }

    LaunchedEffect(token, savedApiUrl) {
        if (token.isBlank() || savedApiUrl.isBlank()) return@LaunchedEffect
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
                    onConnected = {
                        scope.launch {
                            val unauthorized = withContext(Dispatchers.IO) {
                                val api = MobileSyncApi(savedApiUrl, token)
                                val registered = api.registerDevice(currentDeviceId)
                                if (registered.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                val uploaded = repository.syncPendingOrders(savedApiUrl, currentDeviceId)
                                if (uploaded.exceptionOrNull() is SyncUnauthorized) return@withContext true
                                val pulled = repository.pullMissedSales(
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
        )

        AppRoute.Home -> HomeScreen(
            onOpenTables = { route = AppRoute.Ordering(OrderingStep.Tables) },
            onOpenPayments = { route = AppRoute.Paying(PaymentStep.Accounts) },
            onOpenSettings = ::openSettings,
        )

        is AppRoute.Paying -> PaymentHost(
            step = current.step,
            printerMac = printerMac,
            database = database,
            onStep = { route = AppRoute.Paying(it) },
            onLeave = { route = AppRoute.Home },
            onOpenSettings = ::openSettings,
            catalogRevision = catalogRevision,
        )

        is AppRoute.Ordering -> ServiceHost(
            step = current.step,
            apiUrl = apiUrl,
            deviceToken = token,
            printerMac = printerMac,
            database = database,
            session = session,
            onStep = { route = AppRoute.Ordering(it) },
            onOpenSettings = ::openSettings,
            onLeave = { route = AppRoute.Home },
            catalogRevision = catalogRevision,
            onLocalOrderSaved = { uploadNonce++ },
        )
    }
}
