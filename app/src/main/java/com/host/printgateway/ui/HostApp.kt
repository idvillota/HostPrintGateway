package com.host.printgateway.ui

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
import com.host.printgateway.data.DeviceIdProvider
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.HostSyncOperations
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.network.ActiveSaleChannel
import com.host.printgateway.network.AuthManager
import com.host.printgateway.network.JwtExpiry
import com.host.printgateway.service.IncomingPrint
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

    fun deviceId(): String = DeviceIdProvider.get(context)

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
                HostSyncOperations.pushThenPull(database, token, savedApiUrl, deviceId(), settings)
            }
            offlineSyncing = false
            when (outcome) {
                HostSyncOperations.OfflineSyncResult.Done -> {
                    offlineMode = false
                    settings.offlineMode = false
                    offlineNotice = OfflineNotice.None
                    catalogRevision++
                }
                HostSyncOperations.OfflineSyncResult.Unauthorized -> SessionRenewalNotifier.show(context)
                is HostSyncOperations.OfflineSyncResult.Failed -> offlineDetail = outcome.message
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

    HostSyncEffects(
        database = database,
        settings = settings,
        token = token,
        savedApiUrl = savedApiUrl,
        offlineMode = offlineMode,
        autoSyncEnabled = autoSyncEnabled,
        uploadNonce = uploadNonce,
        offlineModeNow = offlineModeNow,
        session = session,
        deviceId = deviceId(),
        scope = scope,
        offlineNotice = offlineNotice,
        onOfflineNoticeChange = { offlineNotice = it },
        onCatalogRevisionBump = { catalogRevision++ },
        onUnauthorized = { SessionRenewalNotifier.show(context) },
    )

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
                            IncomingPrint.accept()
                            onStartGateway()
                            ActiveSaleChannel.announce(ready = true, fresh = true)
                            settingsStatus = "Gateway iniciado. Se imprimen las comandas nuevas."
                        }
                    }
                },
                onStopGateway = {
                    ActiveSaleChannel.announce(ready = false)
                    IncomingPrint.stop()
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
