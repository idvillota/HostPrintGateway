package com.host.printgateway.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.network.AuthManager
import com.host.printgateway.service.PrintGatewayService
import com.host.printgateway.ui.theme.HostTheme

class MainActivity : ComponentActivity() {

    private lateinit var settings: GatewaySettings
    private var renewSessionTick by mutableIntStateOf(0)

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = GatewaySettings(this)
        if (intent.getBooleanExtra(SessionRenewalNotifier.EXTRA_RENEW_SESSION, false)) {
            renewSessionTick++
        }
        ensurePermissions()

        setContent {
            HostTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val database = remember { PrintGatewayDatabase.get(this@MainActivity) }
                    val authManager = remember { AuthManager(this@MainActivity, settings) }
                    val tick = renewSessionTick
                    HostApp(
                        settings = settings,
                        database = database,
                        authManager = authManager,
                        renewSessionTick = tick,
                        onEnsurePermissions = ::ensurePermissions,
                        onStartGateway = {
                            ContextCompat.startForegroundService(
                                this@MainActivity,
                                Intent(this@MainActivity, PrintGatewayService::class.java),
                            )
                        },
                        onStopGateway = {
                            startService(
                                Intent(this@MainActivity, PrintGatewayService::class.java).apply {
                                    action = PrintGatewayService.ACTION_STOP
                                },
                            )
                        },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.getBooleanExtra(SessionRenewalNotifier.EXTRA_RENEW_SESSION, false)) {
            renewSessionTick++
        }
    }

    private fun ensurePermissions() {
        val needed = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                needed += Manifest.permission.BLUETOOTH_CONNECT
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                needed += Manifest.permission.BLUETOOTH_SCAN
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            needed += Manifest.permission.POST_NOTIFICATIONS
        }
        if (needed.isNotEmpty()) {
            permissionLauncher.launch(needed.toTypedArray())
        }
    }
}
