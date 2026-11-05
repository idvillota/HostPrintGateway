package com.host.printgateway.network

import com.host.printgateway.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Shared OkHttp clients for Host Lite.
 * Do not shut down these dispatchers — [SaleChannel] reconnects for the app lifetime.
 */
object HostHttp {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /** Short timeouts used by connectivity ping. */
    val pingClient: OkHttpClient by lazy {
        client.newBuilder()
            .connectTimeout(4, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .writeTimeout(4, TimeUnit.SECONDS)
            .build()
    }

    val webSocketClient: OkHttpClient by lazy {
        client.newBuilder()
            .pingInterval(30, TimeUnit.SECONDS)
            .build()
    }
}

data class HttpResult(
    val code: Int,
    val body: String,
)

/**
 * Thin HTTP helper preserving previous Auth / Accept / timeout behavior.
 */
class ApiClient(
    baseUrl: String,
    private val token: String? = null,
    private val http: OkHttpClient = HostHttp.client,
) {
    private val root = baseUrl.trimEnd('/')

    fun call(
        method: String,
        path: String,
        jsonBody: String? = null,
    ): HttpResult {
        val builder = Request.Builder()
            .url(root + path)
            .header("Accept", "application/json")

        if (!token.isNullOrBlank()) {
            builder.header(
                BackendAuth.AUTHORIZATION_HEADER,
                BackendAuth.authorizationValue(token),
            )
        }

        if (!isAllowedInThisApp(method, path)) {
            error("Esta app solo consulta. No envía cambios a HOST.")
        }

        val mediaJson = JSON_MEDIA
        when (method.uppercase()) {
            "GET" -> builder.get()
            "POST" -> {
                val body = if (jsonBody != null) {
                    jsonBody.toRequestBody(mediaJson)
                } else {
                    ByteArray(0).toRequestBody(null)
                }
                builder.post(body)
            }
            "PUT" -> {
                val body = (jsonBody ?: "").toRequestBody(mediaJson)
                builder.put(body)
            }
            "DELETE" -> builder.delete()
            else -> error("Unsupported HTTP method: $method")
        }

        http.newCall(builder.build()).execute().use { response ->
            val body = response.body?.string().orEmpty()
            return HttpResult(response.code, body)
        }
    }

    companion object {
        private val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()

        private fun isAllowedInThisApp(method: String, path: String): Boolean {
            if (!BuildConfig.READ_ONLY) return true
            val verb = method.uppercase()
            if (verb == "GET") return true
            val route = path.substringBefore('?')
            return verb == "POST" && route.equals("/api/Auth/login", ignoreCase = true)
        }
    }
}
