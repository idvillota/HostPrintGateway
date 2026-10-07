package com.host.printgateway.network

import android.util.Base64
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

enum class SaleListenEnd {
    Closed,
    Unauthorized,
}

class SaleChannel(
    private val baseUrl: String,
    private val token: String,
    private val deviceId: String,
) {
    private val client = HostHttp.webSocketClient
    private val announceLock = Any()

    @Volatile
    private var socket: WebSocket? = null

    @Volatile
    private var pendingFresh: Boolean? = null

    suspend fun listen(
        onSale: (RemoteSale) -> Unit,
        onConnected: () -> Unit,
        onTablesAvailable: (List<String>) -> Unit = {},
        onPrint: (PrintJobDto) -> Unit = {},
    ): SaleListenEnd = suspendCancellableCoroutine { continuation ->
        val finished = AtomicBoolean(false)
        fun finish(end: SaleListenEnd) {
            if (finished.compareAndSet(false, true) && continuation.isActive) {
                continuation.resume(end)
            }
        }
        val request = Request.Builder()
            .url(streamUrl(baseUrl, deviceId))
            .header(BackendAuth.AUTHORIZATION_HEADER, BackendAuth.authorizationValue(token))
            .build()
        val webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                synchronized(announceLock) {
                    socket = webSocket
                    val queued = pendingFresh
                    pendingFresh = null
                    if (queued != null) sendReady(webSocket, queued)
                }
                onConnected()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = runCatching { JSONObject(text) }.getOrNull() ?: return
                val type = json.optString("type")
                if (type.equals("print", ignoreCase = true)) {
                    val id = json.optString("id")
                    val format = json.optString("payloadFormat")
                    val payload = json.optString("payload")
                    if (id.isNotBlank() && format.isNotBlank() && payload.isNotBlank()) {
                        onPrint(
                            PrintJobDto(
                                id = id,
                                kind = json.optString("kind", "kitchen"),
                                payloadFormat = format,
                                payload = payload,
                            ),
                        )
                    }
                    return
                }
                if (type.equals("tablesAvailable", ignoreCase = true)) {
                    val ids = json.optJSONArray("tableIds") ?: return
                    val tables = buildList {
                        for (index in 0 until ids.length()) {
                            ids.optString(index).takeIf { it.isNotBlank() }?.let(::add)
                        }
                    }
                    if (tables.isNotEmpty()) onTablesAvailable(tables)
                    return
                }
                if (type.equals("sale", ignoreCase = true) || json.has("remoteOrderId") || json.has("orderId")) {
                    onSale(MobileSyncApi.parseSale(json))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                finish(if (response?.code == 401) SaleListenEnd.Unauthorized else SaleListenEnd.Closed)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                finish(SaleListenEnd.Closed)
            }
        })
        continuation.invokeOnCancellation {
            webSocket.cancel()
        }
    }

    fun announcePrinting(ready: Boolean, fresh: Boolean = false) {
        synchronized(announceLock) {
            if (!ready) {
                pendingFresh = null
                socket?.send("""{"type":"printIdle"}""")
                return
            }
            val current = socket
            if (current == null) {
                pendingFresh = fresh || pendingFresh == true
                return
            }
            sendReady(current, fresh)
        }
    }

    private fun sendReady(webSocket: WebSocket, fresh: Boolean) {
        val freshField = if (fresh) ""","fresh":true""" else ""
        webSocket.send("""{"type":"printReady"$freshField}""")
    }

    fun close() {
        // Close only this socket. Never shut down HostHttp.webSocketClient —
        // HostApp reconnects SaleChannel for the whole session.
        synchronized(announceLock) {
            pendingFresh = null
            socket?.close(1000, "bye")
            socket = null
        }
    }

    companion object {
        fun streamUrl(baseUrl: String, deviceId: String): String {
            val trimmed = baseUrl.trimEnd('/')
            val wsBase = when {
                trimmed.startsWith("https://") -> "wss://" + trimmed.removePrefix("https://")
                trimmed.startsWith("http://") -> "ws://" + trimmed.removePrefix("http://")
                else -> "ws://$trimmed"
            }
            return "$wsBase/api/mobile-sync/stream?deviceId=$deviceId"
        }
    }
}

object ActiveSaleChannel {
    private val current = AtomicReference<SaleChannel?>(null)

    fun attach(channel: SaleChannel) {
        current.set(channel)
    }

    fun detach(channel: SaleChannel) {
        current.compareAndSet(channel, null)
    }

    fun announce(ready: Boolean, fresh: Boolean = false) {
        current.get()?.announcePrinting(ready, fresh)
    }
}

object JwtExpiry {
    fun expiresAtEpochMs(token: String): Long? {
        val parts = token.split('.')
        if (parts.size < 2) return null
        return runCatching {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING))
            val exp = JSONObject(payload).optLong("exp", 0L)
            if (exp <= 0L) null else exp * 1000
        }.getOrNull()
    }
}
