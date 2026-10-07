package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject

/**
 * Cliente HTTP de autenticación del backend.
 *
 * POST /api/Auth/login
 */
class AuthApi(
    private val baseUrl: String,
) {
    private val client = ApiClient(baseUrl = baseUrl, token = null)

    fun login(
        email: String,
        password: String,
        tenantSlug: String,
    ): Result<LoginResponse> = runCatching {
        require(email.isNotBlank()) { "El email es obligatorio" }
        require(password.isNotBlank()) { "La contraseña es obligatoria" }
        require(tenantSlug.isNotBlank()) { "El tenantSlug es obligatorio" }

        val requestBody = JSONObject()
            .put("email", email.trim())
            .put("password", password)
            .put("tenantSlug", tenantSlug.trim())
            .toString()

        val response = client.call("POST", "/api/Auth/login", jsonBody = requestBody)
        if (response.code !in 200..299) {
            val detail = response.body.replace("\n", " ").take(500)
            error("POST /api/Auth/login HTTP ${response.code}: $detail")
        }
        if (response.body.isBlank()) {
            error("El backend devolvió una respuesta vacía.")
        }
        parseLoginResponse(response.body)
    }

    private fun parseLoginResponse(body: String): LoginResponse {
        val json = JSONObject(body)
        val accessToken = json.optString("accessToken")
        if (accessToken.isBlank()) {
            error("El login fue respondido, pero no contiene accessToken.")
        }
        return LoginResponse(
            accessToken = accessToken,
            userId = json.optString("userId"),
            tenantId = json.optString("tenantId"),
            tenantSlug = json.optString("tenantSlug"),
            email = json.optString("email"),
            roles = json.optStringList("roles"),
            permissions = json.optStringList("permissions"),
            isPlatformAdmin = json.optBoolean("isPlatformAdmin", false),
            brandTheme = json.optString("brandTheme", ""),
            colorScheme = json.optString("colorScheme", ""),
        )
    }
}

/**
 * Respuesta real de POST /api/Auth/login.
 */
data class LoginResponse(
    val accessToken: String,
    val userId: String,
    val tenantId: String,
    val tenantSlug: String,
    val email: String,
    val roles: List<String>,
    val permissions: List<String>,
    val isPlatformAdmin: Boolean,
    val brandTheme: String,
    val colorScheme: String,
)

private fun JSONObject.optStringList(name: String): List<String> {
    val array: JSONArray = optJSONArray(name) ?: return emptyList()
    return buildList {
        for (index in 0 until array.length()) {
            val value = array.optString(index)
            if (value.isNotBlank()) add(value)
        }
    }
}
