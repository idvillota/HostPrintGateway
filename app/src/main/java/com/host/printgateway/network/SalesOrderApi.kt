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
    ): Result<RelocateOrderResult> = runCatching {
        val body = JSONObject()
            .put("targetTableId", targetTableId)
            .put("mergeIfTargetBusy", mergeIfTargetBusy ?: JSONObject.NULL)
            .toString()
        val response = client.call(
            method = "POST",
            path = "/api/SalesOrders/$remoteOrderId/relocate",
            jsonBody = body,
        )
        if (response.code == 401) throw SyncUnauthorized("La sesión expiró")
        val json = response.body.takeIf { it.isNotBlank() }?.let { raw ->
            runCatching { JSONObject(raw) }.getOrNull()
        }
        val action = json?.optString("action").orEmpty()
        val message = json?.optString("message").orEmpty()
        val alreadyThere = message.contains("distinta a la mesa actual", ignoreCase = true)
        val alreadyMerged = mergeIfTargetBusy == true &&
            message.contains("Solo se pueden mover pedidos activos", ignoreCase = true)
        if (response.code == 409 && action.equals(RelocateOrderResult.MERGE_REQUIRED, ignoreCase = true)) {
            return@runCatching parseRelocate(json, RelocateOrderResult.MERGE_REQUIRED, message)
        }
        if (alreadyThere) {
            return@runCatching parseRelocate(json, RelocateOrderResult.TRANSFERRED, message)
                .copy(targetTableId = targetTableId, targetOrderId = remoteOrderId)
        }
        if (alreadyMerged) {
            return@runCatching parseRelocate(json, RelocateOrderResult.MERGED, message)
                .copy(targetTableId = targetTableId)
        }
        if (response.code in 200..299 &&
            !action.equals(RelocateOrderResult.CANCELLED, ignoreCase = true)
        ) {
            return@runCatching parseRelocate(json, action.ifBlank { RelocateOrderResult.TRANSFERRED }, message)
        }
        if (response.code == 403) {
            error("No tiene permiso para cambiar de mesa.")
        }
        error(message.ifBlank { "No se pudo cambiar la mesa (HTTP ${response.code})." })
    }

    private fun parseRelocate(json: JSONObject?, action: String, message: String): RelocateOrderResult {
        val order = json?.optJSONObject("order")
        val orderId = order?.optString("id").orEmpty()
        return RelocateOrderResult(
            action = action,
            message = message,
            targetTableId = json?.optString("targetTableId").orEmpty(),
            targetTableCode = json?.optString("targetTableCode").orEmpty(),
            targetOrderId = json?.optString("targetOrderId")?.ifBlank { null } ?: orderId,
            lines = parseOrderLines(order),
        )
    }

    private fun parseOrderLines(order: JSONObject?): List<RemoteSaleLine> {
        val lines = order?.optJSONArray("lines") ?: return emptyList()
        return (0 until lines.length()).mapNotNull { index ->
            val line = lines.optJSONObject(index) ?: return@mapNotNull null
            RemoteSaleLine(
                lineId = line.optString("id"),
                productId = line.optString("productId"),
                productName = line.optString("productName"),
                quantity = line.optDouble("quantity"),
                unitPrice = line.optDouble("unitPrice"),
                notes = line.optString("notes"),
            )
        }
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

data class RelocateOrderResult(
    val action: String,
    val message: String,
    val targetTableId: String,
    val targetTableCode: String,
    val targetOrderId: String,
    val lines: List<RemoteSaleLine> = emptyList(),
) {
    companion object {
        const val TRANSFERRED = "transferred"
        const val MERGED = "merged"
        const val CANCELLED = "cancelled"
        const val MERGE_REQUIRED = "merge_required"
    }
}

data class AddOrderLineRequest(
    val productId: String,
    val quantity: Double,
    val notes: String,
)
