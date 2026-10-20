package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject

class SalesOrderApi(
    private val baseUrl: String,
    private val token: String = BackendAuth.DEFAULT_TOKEN,
) {
    private val client = ApiClient(baseUrl = baseUrl, token = token)

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

    fun relocate(
        remoteOrderId: String,
        targetTableId: String,
        mergeIfTargetBusy: Boolean?,
    ): Result<Unit> = runCatching {
        val body = JSONObject()
            .put("targetTableId", targetTableId)
            .put("mergeIfTargetBusy", mergeIfTargetBusy ?: JSONObject.NULL)
            .toString()
        val response = client.call(
            method = "POST",
            path = "/api/SalesOrders/$remoteOrderId/relocate",
            jsonBody = body,
        )
        val json = response.body.takeIf { it.isNotBlank() }?.let { raw ->
            runCatching { JSONObject(raw) }.getOrNull()
        }
        val action = json?.optString("action").orEmpty()
        val message = json?.optString("message").orEmpty()
        val alreadyThere = message.contains("distinta a la mesa actual", ignoreCase = true)
        val alreadyMerged = mergeIfTargetBusy == true &&
            message.contains("Solo se pueden mover pedidos activos", ignoreCase = true)
        if (alreadyThere || alreadyMerged) return@runCatching
        if (response.code in 200..299 &&
            !action.equals("merge_required", ignoreCase = true) &&
            !action.equals("cancelled", ignoreCase = true)
        ) {
            return@runCatching
        }
        error(message.ifBlank { "No se pudo cambiar la mesa (HTTP ${response.code})." })
    }

    private fun <T> request(
        method: String,
        path: String,
        body: String?,
        parser: (String) -> T,
    ): Result<T> = runCatching {
        val response = client.call(
            method = method,
            path = path,
            jsonBody = body,
        )
        if (response.code !in 200..299) {
            error("$method $path HTTP ${response.code}: ${response.body.take(200)}")
        }
        parser(response.body)
    }
}

data class AddOrderLineRequest(
    val productId: String,
    val quantity: Double,
    val notes: String,
)
