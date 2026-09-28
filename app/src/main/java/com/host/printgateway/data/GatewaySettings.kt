package com.host.printgateway.data

import android.content.Context
import com.host.printgateway.network.BackendAuth

/**
 * Persists gateway configuration (SharedPreferences — minimal, no extra libs).
 */
class GatewaySettings(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    var apiBaseUrl: String
        get() = prefs.getString(KEY_API_URL, DEFAULT_API_URL).orEmpty()
        set(value) = prefs.edit().putString(KEY_API_URL, value.trim().trimEnd('/')).apply()

    var deviceToken: String
        get() = prefs.getString(KEY_TOKEN, null).orEmpty().ifBlank { BackendAuth.DEFAULT_TOKEN }
        set(value) = prefs.edit().putString(KEY_TOKEN, value.trim()).apply()

    var printerMac: String
        get() = prefs.getString(KEY_MAC, "").orEmpty()
        set(value) = prefs.edit().putString(KEY_MAC, value.trim()).apply()

    var pollIntervalMs: Long
        get() = prefs.getLong(KEY_POLL_MS, DEFAULT_POLL_MS)
        set(value) = prefs.edit().putLong(KEY_POLL_MS, value.coerceIn(2_000L, 60_000L)).apply()

    fun isConfigured(): Boolean =
        apiBaseUrl.isNotBlank() && deviceToken.isNotBlank() && printerMac.isNotBlank()

    fun isPrinterConfigured(): Boolean = printerMac.isNotBlank()

    companion object {
        private const val PREFS = "host_print_gateway"
        private const val KEY_API_URL = "api_base_url"
        private const val KEY_TOKEN = "device_token"
        private const val KEY_MAC = "printer_mac"
        private const val KEY_POLL_MS = "poll_interval_ms"
        const val DEFAULT_API_URL = "http://10.0.2.2:5228"
        const val DEFAULT_POLL_MS = 3_000L
    }
}
