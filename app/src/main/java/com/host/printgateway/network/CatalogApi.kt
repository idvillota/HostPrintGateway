package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject

data class RemoteTable(val id: String, val code: String, val capacity: Int, val zone: String, val status: String, val isActive: Boolean)
data class RemoteTableAccount(val tableId: String, val openOrderId: String?)
data class RemoteProductType(val id: String, val name: String, val description: String?, val sortOrder: Int, val isActive: Boolean)
data class RemoteProduct(val id: String, val productTypeId: String, val compositionType: String, val name: String, val description: String?, val sku: String?, val imagePath: String?, val unitPrice: Double, val isActive: Boolean)
data class RemoteIngredient(val id: String, val categoryId: String, val name: String, val unit: String, val unitCost: Double?, val stockQuantity: Double?, val reorderLevel: Double?, val isActive: Boolean)
data class RemotePrinter(val id: String, val name: String, val code: String, val isActive: Boolean, val sortOrder: Int)
data class RemoteProductIngredient(val id: String, val productId: String, val ingredientId: String, val quantity: Double)
data class RemoteProductPage(val products: List<RemoteProduct>, val productIngredients: List<RemoteProductIngredient>)

class CatalogApi(
    private val baseUrl: String,
    private val token: String = BackendAuth.DEFAULT_TOKEN,
    private val client: ApiClient = ApiClient(baseUrl = baseUrl, token = token),
    private val pingHttp: ApiClient = ApiClient(baseUrl = baseUrl, token = token, http = HostHttp.pingClient),
) {

    fun fetchTables(): Result<List<RemoteTable>> = request("/api/DiningTables") { item ->
        RemoteTable(
            id = item.getString("id"),
            code = item.optString("code"),
            capacity = item.optInt("capacity"),
            zone = item.optString("zone"),
            status = item.optString("status"),
            isActive = item.optBoolean("isActive", true),
        )
    }

    /**
     * Lightweight reachability check. Uses /health (not salon table summaries)
     * so the API is not hit with ListTableSummaries every few seconds.
     * Session expiry is still detected by authenticated loops (auto-sync, settle tables, sale stream).
     */
    fun ping(): Result<Unit> = runCatching {
        val response = pingHttp.call("GET", "/health")
        if (response.code == 401) throw SyncUnauthorized("La sesión expiró")
        if (response.code !in 200..299) error("HOST no responde")
    }

    fun fetchTableAccounts(): Result<List<RemoteTableAccount>> = request("/api/SalesOrders/tables") { item ->
        val openOrderId = if (item.isNull("openOrderId")) {
            null
        } else {
            item.optString("openOrderId").ifBlank { null }
        }
        RemoteTableAccount(
            tableId = item.getString("tableId"),
            openOrderId = openOrderId,
        )
    }

    fun fetchProductTypes(): Result<List<RemoteProductType>> = request("/api/ProductTypes") { item ->
        RemoteProductType(
            id = item.getString("id"),
            name = item.optString("name"),
            description = item.optString("description").ifBlank { null },
            sortOrder = item.optInt("sortOrder"),
            isActive = item.optBoolean("isActive", true),
        )
    }

    fun fetchProducts(): Result<RemoteProductPage> {
        val links = mutableListOf<RemoteProductIngredient>()
        val products = request("/api/Products?pageSize=1000") { item ->
            val product = RemoteProduct(
                id = item.getString("id"),
                productTypeId = item.getString("productTypeId"),
                compositionType = item.optString("compositionType", "Prepared"),
                name = item.optString("name"),
                description = item.optString("description").ifBlank { null },
                sku = item.optString("sku").ifBlank { null },
                imagePath = item.optString("imagePath").ifBlank { null },
                unitPrice = item.optDouble("unitPrice"),
                isActive = item.optBoolean("isActive", true),
            )
            links += parseProductIngredients(product.id, item)
            product
        }
        return products.map { RemoteProductPage(it, links.toList()) }
    }

    private fun parseProductIngredients(productId: String, item: JSONObject): List<RemoteProductIngredient> {
        val array = sequenceOf("ingredients", "productIngredients", "recipe")
            .mapNotNull { key -> item.optJSONArray(key) }
            .firstOrNull()
            ?: return emptyList()
        return buildList {
            for (index in 0 until array.length()) {
                val node = array.optJSONObject(index) ?: continue
                val nested = node.optJSONObject("ingredient")
                val ingredientId = node.optString("ingredientId")
                    .ifBlank { nested?.optString("id").orEmpty() }
                    .ifBlank { if (node.has("ingredientId") || nested != null) "" else node.optString("id") }
                if (ingredientId.isBlank()) continue
                val quantity = if (node.has("quantity") && !node.isNull("quantity")) {
                    node.optDouble("quantity", 1.0)
                } else {
                    1.0
                }
                val rawId = node.optString("id")
                val linkId = rawId.takeIf { it.isNotBlank() && it != ingredientId } ?: "$productId:$ingredientId"
                add(RemoteProductIngredient(linkId, productId, ingredientId, quantity))
            }
        }
    }

    fun fetchIngredients(): Result<List<RemoteIngredient>> = request("/api/Ingredients?pageSize=1000") { item ->
        RemoteIngredient(
            id = item.getString("id"),
            categoryId = item.optString("ingredientCategoryId"),
            name = item.optString("name"),
            unit = item.optString("unit"),
            unitCost = item.optNullableDouble("unitCost"),
            stockQuantity = item.optNullableDouble("stockQuantity"),
            reorderLevel = item.optNullableDouble("reorderLevel"),
            isActive = item.optBoolean("isActive", true),
        )
    }

    fun fetchPrinters(): Result<List<RemotePrinter>> = request("/api/kitchen/printers/stations") { item ->
        RemotePrinter(
            id = item.getString("id"),
            name = item.optString("name"),
            code = item.optString("code"),
            isActive = item.optBoolean("isActive", true),
            sortOrder = item.optInt("sortOrder"),
        )
    }

    private fun <T> request(path: String, mapper: (JSONObject) -> T): Result<List<T>> = runCatching {
        val response = client.call("GET", path)
        if (response.code == 401) throw SyncUnauthorized("La sesión expiró")
        if (response.code !in 200..299) {
            error("GET $path HTTP ${response.code}: ${response.body.take(200)}")
        }
        val root = response.body.trim()
        val array = when {
            root.startsWith("[") -> JSONArray(root)
            root.startsWith("{") -> {
                val objectRoot = JSONObject(root)
                when {
                    objectRoot.has("items") -> objectRoot.getJSONArray("items")
                    objectRoot.has("data") -> objectRoot.getJSONArray("data")
                    else -> JSONArray().put(objectRoot)
                }
            }
            else -> error("Respuesta JSON inválida para $path")
        }
        buildList { for (index in 0 until array.length()) add(mapper(array.getJSONObject(index))) }
    }

    private fun JSONObject.optNullableDouble(name: String): Double? =
        if (isNull(name)) null else optDouble(name)
}
