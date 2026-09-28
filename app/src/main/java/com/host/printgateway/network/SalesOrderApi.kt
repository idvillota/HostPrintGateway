package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class SalesOrderApi(
    private val baseUrl: String,
    private val token: String = BackendAuth.DEFAULT_TOKEN,
) {

    fun createOpenOrder(tableId: String): Result<String> = request(
        method = "POST",
        path = "/api/SalesOrders/table/$tableId",
        body = null,
    ) { body ->
        JSONObject(body).getString("id")
    }

    fun confirmOrder(orderId: String, lines: List<AddOrderLineRequest>): Result<Unit> {
        val jsonLines = JSONArray()
        lines.forEach { line ->
            jsonLines.put(
                JSONObject()
                    .put("productId", line.productId)
                    .put("quantity", line.quantity)
                    .put("notes", line.notes.ifBlank { JSONObject.NULL })
                    .put("excludedIngredientIds", JSONArray()),
            )
        }
        val body = JSONObject().put("lines", jsonLines).toString()
        return request("POST", "/api/SalesOrders/$orderId/confirm", body) { Unit }
    }

    private fun <T> request(
        method: String,
        path: String,
        body: String?,
        parser: (String) -> T,
    ): Result<T> = runCatching {
        val connection = URL(baseUrl.trimEnd('/') + path).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = method
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty(
            BackendAuth.AUTHORIZATION_HEADER,
            BackendAuth.authorizationValue(token)
        )
            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use { it.write(body) }
            }
            val responseBody = BufferedReader(InputStreamReader(
                if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream,
                StandardCharsets.UTF_8,
            )).use { it.readText() }
            if (connection.responseCode !in 200..299) {
                error("$method $path HTTP ${connection.responseCode}: ${responseBody.take(200)}")
            }
            parser(responseBody)
        } finally {
            connection.disconnect()
        }
    }
}

data class AddOrderLineRequest(
    val productId: String,
    val quantity: Double,
    val notes: String,
)