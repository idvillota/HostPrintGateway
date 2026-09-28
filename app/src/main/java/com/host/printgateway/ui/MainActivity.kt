package com.host.printgateway.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.PrintJobEntity
import com.host.printgateway.network.AuthManager
import com.host.printgateway.printer.BluetoothEscPosPrinter
import com.host.printgateway.printer.EscPosReceiptFormatter
import com.host.printgateway.service.PrintGatewayService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : ComponentActivity() {

    private lateinit var settings: GatewaySettings

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) {
            // Los permisos se vuelven a comprobar cuando se
            // realiza la siguiente acción.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = GatewaySettings(this)

        ensurePermissions()

        setContent {

            MaterialTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val scope =
                        rememberCoroutineScope()

                    val database =
                        remember {
                            PrintGatewayDatabase.get(
                                this@MainActivity
                            )
                        }

                    val authManager =
                        remember {
                            AuthManager(
                                this@MainActivity,
                                settings,
                            )
                        }

                    var apiUrl by remember {
                        mutableStateOf(
                            settings.apiBaseUrl
                        )
                    }

                    val savedCredentials =
                        remember {
                            authManager
                                .getSavedCredentials()
                        }

                    var email by remember {
                        mutableStateOf(
                            savedCredentials
                                ?.email
                                .orEmpty()
                        )
                    }

                    var password by remember {
                        mutableStateOf(
                            savedCredentials
                                ?.password
                                .orEmpty()
                        )
                    }

                    var tenantSlug by remember {
                        mutableStateOf(
                            savedCredentials
                                ?.tenantSlug
                                .orEmpty()
                        )
                    }

                    var token by remember {
                        mutableStateOf(
                            settings.deviceToken
                        )
                    }

                    var mac by remember {
                        mutableStateOf(
                            settings.printerMac
                        )
                    }

                    var manualXml by remember {
                        mutableStateOf("")
                    }

                    var status by remember {
                        mutableStateOf("Listo")
                    }

                    var loggingIn by remember {
                        mutableStateOf(false)
                    }

                    var busy by remember {
                        mutableStateOf(false)
                    }

                    /**
                     * Login automático al abrir la APK si existen
                     * credenciales previamente configuradas.
                     */
                    LaunchedEffect(Unit) {

                        if (
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            tenantSlug.isNotBlank() &&
                            apiUrl.isNotBlank()
                        ) {

                            loggingIn = true
                            status = "Iniciando sesión…"

                            val result =
                                withContext(Dispatchers.IO) {
                                    authManager.loginWithSavedCredentials(
                                        apiUrl
                                    )
                                }

                            loggingIn = false

                            if (result.isSuccess) {

                                token =
                                    result
                                        .getOrThrow()
                                        .accessToken

                                status =
                                    "Sesión iniciada: ${result.getOrThrow().email}"

                            } else {

                                token = ""

                                status =
                                    "Login automático falló: " +
                                        (
                                            result
                                                .exceptionOrNull()
                                                ?.message
                                                ?: "error desconocido"
                                            )
                            }
                        }
                    }

                    fun persistPrinterSettings() {

                        settings.apiBaseUrl =
                            apiUrl

                        settings.printerMac =
                            mac
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(20.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp),
                    ) {

                        Text(
                            text = "Host Print Gateway",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text =
                                "Configura el backend, inicia sesión y deja la tablet cerca de la impresora.",
                            style =
                                MaterialTheme.typography.bodyMedium,
                        )

                        if (token.isNotBlank()) {
                            LiteOrderPanel(
                                apiUrl = apiUrl,
                                deviceToken = token,
                                database = database,
                                printerMac = mac,
                            )
                        } else {
                            Text(
                                text = "Inicia sesión para tomar pedidos.",
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }

                        // -------------------------------------------------
                        // BACKEND
                        // -------------------------------------------------

                        Text(
                            text = "Backend",
                            style =
                                MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = apiUrl,
                            onValueChange = {
                                apiUrl = it
                            },
                            label = {
                                Text("URL API Host")
                            },
                            singleLine = true,
                            modifier =
                                Modifier.fillMaxWidth(),
                        )

                        // -------------------------------------------------
                        // CREDENCIALES
                        // -------------------------------------------------

                        Text(
                            text = "Autenticación",
                            style =
                                MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                            },
                            label = {
                                Text("Email")
                            },
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Email
                                ),
                            modifier =
                                Modifier.fillMaxWidth(),
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                            },
                            label = {
                                Text("Contraseña")
                            },
                            singleLine = true,
                            visualTransformation =
                                PasswordVisualTransformation(),
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Password
                                ),
                            modifier =
                                Modifier.fillMaxWidth(),
                        )

                        OutlinedTextField(
                            value = tenantSlug,
                            onValueChange = {
                                tenantSlug = it
                            },
                            label = {
                                Text("Tenant")
                            },
                            singleLine = true,
                            modifier =
                                Modifier.fillMaxWidth(),
                        )

                        Button(
                            enabled =
                                !loggingIn &&
                                    apiUrl.isNotBlank() &&
                                    email.isNotBlank() &&
                                    password.isNotBlank() &&
                                    tenantSlug.isNotBlank(),
                            onClick = {

                                settings.apiBaseUrl =
                                    apiUrl

                                loggingIn = true
                                status =
                                    "Autenticando…"

                                scope.launch {

                                    val result =
                                        withContext(
                                            Dispatchers.IO
                                        ) {
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

                                            token =
                                                response.accessToken

                                            status =
                                                "Login correcto. " +
                                                    "Usuario: ${response.email}. " +
                                                    "Tenant: ${response.tenantSlug}"

                                        },

                                        onFailure = { error ->

                                            token = ""

                                            status =
                                                "Error de login: " +
                                                    (
                                                        error.message
                                                            ?: "error desconocido"
                                                        )
                                        }
                                    )
                                }
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                if (loggingIn)
                                    "Iniciando sesión…"
                                else
                                    "Iniciar sesión"
                            )
                        }

                        if (token.isNotBlank()) {

                            Text(
                                text =
                                    "Autenticado correctamente",
                                color =
                                    MaterialTheme.colorScheme.primary,
                            )

                        } else {

                            Text(
                                text =
                                    "Sin sesión",
                                color =
                                    MaterialTheme.colorScheme.error,
                            )
                        }

                        // -------------------------------------------------
                        // IMPRESORA
                        // -------------------------------------------------

                        Text(
                            text = "Impresora",
                            style =
                                MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = mac,
                            onValueChange = {
                                mac = it
                            },
                            label = {
                                Text(
                                    "MAC Bluetooth impresora"
                                )
                            },
                            singleLine = true,
                            modifier =
                                Modifier.fillMaxWidth(),
                        )

                        RoomDebugPanel(
                            database = database,
                        )

                        // -------------------------------------------------
                        // PRUEBA OFFLINE
                        // -------------------------------------------------

                        Text(
                            text = "Prueba temporal offline",
                            style =
                                MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = manualXml,
                            onValueChange = {
                                manualXml = it
                            },
                            label = {
                                Text("XML del recibo")
                            },
                            minLines = 6,
                            maxLines = 12,
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Text
                                ),
                            modifier =
                                Modifier.fillMaxWidth(),
                        )

                        OutlinedButton(
                            enabled =
                                manualXml.isNotBlank(),
                            onClick = {

                                scope.launch {

                                    val xml =
                                        manualXml

                                    val result =
                                        withContext(
                                            Dispatchers.IO
                                        ) {
                                            runCatching {

                                                database
                                                    .printJobDao()
                                                    .insertAll(
                                                        listOf(
                                                            PrintJobEntity(
                                                                id =
                                                                    "manual-${UUID.randomUUID()}",
                                                                kind =
                                                                    "receipt",
                                                                payloadFormat =
                                                                    "sales-receipt-xml",
                                                                payload =
                                                                    xml,
                                                            )
                                                        )
                                                    )
                                            }
                                        }

                                    status =
                                        if (result.isSuccess) {

                                            manualXml = ""

                                            "XML guardado en la cola offline"

                                        } else {

                                            "Error guardando XML: " +
                                                (
                                                    result
                                                        .exceptionOrNull()
                                                        ?.message
                                                        ?: "error"
                                                    )
                                        }
                                }
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "Insertar XML en Room"
                            )
                        }

                        // -------------------------------------------------
                        // GATEWAY
                        // -------------------------------------------------

                        Button(
                            enabled =
                                token.isNotBlank(),
                            onClick = {

                                persistPrinterSettings()

                                if (token.isBlank()) {

                                    status =
                                        "Primero inicia sesión."

                                    return@Button
                                }

                                if (
                                    !settings
                                        .isPrinterConfigured()
                                ) {

                                    status =
                                        "Indica la MAC de la impresora."

                                    return@Button
                                }

                                ensurePermissions()

                                val intent =
                                    Intent(
                                        this@MainActivity,
                                        PrintGatewayService::class.java,
                                    )

                                ContextCompat
                                    .startForegroundService(
                                        this@MainActivity,
                                        intent,
                                    )

                                status =
                                    "Gateway iniciado (notificación activa)"
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "Iniciar gateway"
                            )
                        }

                        OutlinedButton(
                            onClick = {

                                val intent =
                                    Intent(
                                        this@MainActivity,
                                        PrintGatewayService::class.java,
                                    ).apply {
                                        action =
                                            PrintGatewayService.ACTION_STOP
                                    }

                                startService(intent)

                                status =
                                    "Gateway detenido"
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "Detener gateway"
                            )
                        }

                        // -------------------------------------------------
                        // PRUEBA IMPRESORA
                        // -------------------------------------------------

                        OutlinedButton(
                            enabled =
                                !busy,
                            onClick = {

                                persistPrinterSettings()

                                if (mac.isBlank()) {

                                    status =
                                        "Indica la MAC de la impresora."

                                    return@OutlinedButton
                                }

                                busy = true
                                status =
                                    "Enviando prueba…"

                                scope.launch {

                                    val result =
                                        withContext(
                                            Dispatchers.IO
                                        ) {

                                            val bytes =
                                                EscPosReceiptFormatter()
                                                    .formatTestTicket(
                                                        mac.trim()
                                                    )

                                            BluetoothEscPosPrinter(
                                                mac.trim()
                                            ).print(bytes)
                                        }

                                    busy = false

                                    status =
                                        if (result.isSuccess) {

                                            "Prueba OK"

                                        } else {

                                            "Error: " +
                                                (
                                                    result
                                                        .exceptionOrNull()
                                                        ?.message
                                                        ?: "error"
                                                    )
                                        }
                                }
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "Probar impresora"
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text = status,
                            color =
                                MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }

    private fun ensurePermissions() {

        val needed =
            mutableListOf<String>()

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.S
        ) {

            if (
                checkSelfPermission(
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                needed +=
                    Manifest.permission.BLUETOOTH_CONNECT
            }

            if (
                checkSelfPermission(
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                needed +=
                    Manifest.permission.BLUETOOTH_SCAN
            }
        }

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                needed +=
                    Manifest.permission.POST_NOTIFICATIONS
            }
        }

        if (needed.isNotEmpty()) {

            permissionLauncher.launch(
                needed.toTypedArray()
            )
        }
    }
}