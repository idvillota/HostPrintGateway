package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.UUID

class SyncUnauthorized(message: String) : Exception(message)

data class RemoteSaleLine(
    val lineId: String,
    val productId: String,
    val productName: String,
    val quantity: Double,
    val unitPrice: Double,
    val notes: String,
)

data class RemoteSale(
    val remoteOrderId: String,
    val tableId: String,
    val tableCode: String,
    val waiterName: String,
    val cursor: String,
    val lines: List<RemoteSaleLine>,
)

data class BatchOrderResult(
    val localId: String,
    val remoteId: String,
    val synced: Boolean,
)

data class PendingSalesPage(
    val sales: List<RemoteSale>,
    val cursor: String,
)

class MobileSyncApi(
    private val baseUrl: String,
    private val token: String,
) {

    fun registerDevice(deviceId: String): Result<Unit> = runCatching {
        request(
            method = "POST",
            path = "/api/mobile-sync/devices",
            body = JSONObject().put("deviceId", deviceId).toString(),
        )
    }

    fun uploadBatch(
        deviceId: String,
        orders: List<Pair<com.host.printgateway.data.OrderEntity, List<AddOrderLineRequest>>>,
    ): Result<List<BatchOrderResult>> = runCatching {
        val payloadOrders = JSONArray()
        orders.forEach { (order, lines) ->
            val jsonLines = JSONArray()
            lines.forEach { line ->
                jsonLines.put(
                    JSONObject()
                        .put("productId", line.productId)
                        .put("quantity", line.quantity)
                        .put("notes", line.notes),
                )
            }
            payloadOrders.put(
                JSONObject()
                    .put("localId", order.id)
                    .put("remoteId", order.remoteId ?: JSONObject.NULL)
                    .put("tableId", order.diningTableId ?: "")
                    .put("tableCode", order.diningTableCode)
                    .put("waiterName", order.waiterName)
                    .put("openedAtUtc", order.openedAtUtc)
                    .put("lines", jsonLines),
            )
        }
        val body = JSONObject()
            .put("batchId", UUID.randomUUID().toString())
            .put("deviceId", deviceId)
            .put("orders", payloadOrders)
            .toString()
        val response = request("POST", "/api/mobile-sync/batches", body)
        val results = JSONObject(response).optJSONArray("results") ?: JSONArray()
        buildList {
            for (index in 0 until results.length()) {
                val item = results.getJSONObject(index)
                add(
                    BatchOrderResult(
                        localId = item.optString("localId"),
                        remoteId = item.optString("remoteId"),
                        synced = item.optString("status").equals("synced", ignoreCase = true),
                    ),
                )
            }
        }
    }

    fun fetchPendingSales(deviceId: String, since: String): Result<PendingSalesPage> = runCatching {
        val sinceQuery = if (since.isBlank()) "" else "&since=${java.net.URLEncoder.encode(since, "UTF-8")}"
        val response = request(
            method = "GET",
            path = "/api/mobile-sync/pending-sales?deviceId=$deviceId$sinceQuery",
            body = null,
        )
        val root = JSONObject(response)
        PendingSalesPage(
            sales = parseSales(root.optJSONArray("sales") ?: JSONArray()),
            cursor = root.optString("cursor"),
        )
    }

    private fun request(method: String, path: String, body: String?): String {
        val connection = URL(baseUrl.trimEnd('/') + path).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = method
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty(BackendAuth.AUTHORIZATION_HEADER, BackendAuth.authorizationValue(token))
            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use { it.write(body) }
            }
            val stream = if (connection.responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            val responseBody = if (stream == null) {
                ""
            } else {
                BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8)).use { it.readText() }
            }
            if (connection.responseCode == 401) {
                throw SyncUnauthorized("La sesión expiró")
            }
            if (connection.responseCode !in 200..299) {
                error("$method $path HTTP ${connection.responseCode}: ${responseBody.take(200)}")
            }
            return responseBody.ifBlank { "{}" }
        } finally {
            connection.disconnect()
        }
    }

    companion object {
        fun parseSale(json: JSONObject): RemoteSale {
            val linesJson = json.optJSONArray("lines") ?: JSONArray()
            val lines = buildList {
                for (index in 0 until linesJson.length()) {
                    val line = linesJson.getJSONObject(index)
                    add(
                        RemoteSaleLine(
                            lineId = line.optString("lineId"),
                            productId = line.optString("productId"),
                            productName = line.optString("productName"),
                            quantity = line.optDouble("quantity", 1.0),
                            unitPrice = line.optDouble("unitPrice", 0.0),
                            notes = line.optString("notes"),
                        ),
                    )
                }
            }
            return RemoteSale(
                remoteOrderId = json.optString("remoteOrderId").ifBlank { json.optString("orderId") },
                tableId = json.optString("tableId"),
                tableCode = json.optString("tableCode"),
                waiterName = json.optString("waiterName"),
                cursor = json.optString("cursor"),
                lines = lines,
            )
        }

        fun parseSales(array: JSONArray): List<RemoteSale> = buildList {
            for (index in 0 until array.length()) {
                add(parseSale(array.getJSONObject(index)))
            }
        }
    }
}
