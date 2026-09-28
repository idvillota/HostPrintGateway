package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Cliente HTTP de autenticación del backend.
 *
 * POST /api/Auth/login
 */
class AuthApi(
    private val baseUrl: String,
) {

    fun login(
        email: String,
        password: String,
        tenantSlug: String,
    ): Result<LoginResponse> = runCatching {

        require(email.isNotBlank()) {
            "El email es obligatorio"
        }

        require(password.isNotBlank()) {
            "La contraseña es obligatoria"
        }

        require(tenantSlug.isNotBlank()) {
            "El tenantSlug es obligatorio"
        }

        val url = URL(
            baseUrl.trimEnd('/') + "/api/Auth/login"
        )

        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.doOutput = true

            connection.setRequestProperty(
                "Accept",
                "application/json"
            )

            connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=utf-8"
            )

            val requestBody = JSONObject()
                .put("email", email.trim())
                .put("password", password)
                .put("tenantSlug", tenantSlug.trim())
                .toString()

            OutputStreamWriter(
                connection.outputStream,
                StandardCharsets.UTF_8,
            ).use { writer ->
                writer.write(requestBody)
            }

            val responseCode = connection.responseCode

            val stream =
                if (responseCode in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

            val responseBody =
                stream?.let {
                    BufferedReader(
                        InputStreamReader(
                            it,
                            StandardCharsets.UTF_8,
                        )
                    ).use { reader ->
                        reader.readText()
                    }
                }.orEmpty()

            if (responseCode !in 200..299) {
                val detail = responseBody
                    .replace("\n", " ")
                    .take(500)

                error(
                    "POST /api/Auth/login HTTP $responseCode: $detail"
                )
            }

            if (responseBody.isBlank()) {
                error("El backend devolvió una respuesta vacía.")
            }

            parseLoginResponse(responseBody)

        } finally {
            connection.disconnect()
        }
    }

    private fun parseLoginResponse(
        body: String,
    ): LoginResponse {

        val json = JSONObject(body)

        val accessToken = json.optString("accessToken")

        if (accessToken.isBlank()) {
            error(
                "El login fue respondido, pero no contiene accessToken."
            )
        }

        return LoginResponse(
            accessToken = accessToken,
            userId = json.optString("userId"),
            tenantId = json.optString("tenantId"),
            tenantSlug = json.optString("tenantSlug"),
            email = json.optString("email"),
            roles = json.optStringList("roles"),
            permissions = json.optStringList("permissions"),
            isPlatformAdmin = json.optBoolean(
                "isPlatformAdmin",
                false,
            ),
            brandTheme = json.optString(
                "brandTheme",
                "",
            ),
            colorScheme = json.optString(
                "colorScheme",
                "",
            ),
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

private fun JSONObject.optStringList(
    name: String,
): List<String> {

    val array: JSONArray =
        optJSONArray(name) ?: return emptyList()

    return buildList {
        for (index in 0 until array.length()) {
            val value = array.optString(index)

            if (value.isNotBlank()) {
                add(value)
            }
        }
    }
}