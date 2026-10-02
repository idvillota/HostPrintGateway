package com.host.printgateway.network

import android.content.Context
import android.util.Base64
import com.host.printgateway.data.GatewaySettings
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties

/**
 * Gestiona las credenciales y la sesión del backend.
 *
 * Las credenciales se almacenan cifradas mediante Android Keystore.
 *
 * El accessToken recibido del backend se coloca en GatewaySettings
 * porque las demás APIs del proyecto ya lo consumen desde allí.
 */
class AuthManager(
    context: Context,
    private val settings: GatewaySettings,
) {

    private val appContext = context.applicationContext

    private val prefs = appContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE,
    )

    /**
     * Guarda las credenciales cifradas.
     */
    fun saveCredentials(
        email: String,
        password: String,
        tenantSlug: String,
    ) {
        prefs.edit()
            .putString(KEY_EMAIL, encrypt(email))
            .putString(KEY_PASSWORD, encrypt(password))
            .putString(KEY_TENANT_SLUG, encrypt(tenantSlug))
            .apply()
    }

    /**
     * Recupera las credenciales almacenadas.
     *
     * Si no existen o no pueden descifrarse, devuelve null.
     */
    fun getSavedCredentials(): SavedCredentials? {

        val encryptedEmail =
            prefs.getString(KEY_EMAIL, null)

        val encryptedPassword =
            prefs.getString(KEY_PASSWORD, null)

        val encryptedTenant =
            prefs.getString(KEY_TENANT_SLUG, null)

        if (
            encryptedEmail.isNullOrBlank() ||
            encryptedPassword.isNullOrBlank() ||
            encryptedTenant.isNullOrBlank()
        ) {
            return null
        }

        return try {
            SavedCredentials(
                email = decrypt(encryptedEmail),
                password = decrypt(encryptedPassword),
                tenantSlug = decrypt(encryptedTenant),
            )
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Hace login contra el backend y guarda el accessToken.
     */
    fun login(
        baseUrl: String,
        email: String,
        password: String,
        tenantSlug: String,
    ): Result<LoginResponse> = runCatching {

        val cleanBaseUrl = baseUrl
            .trim()
            .trimEnd('/')

        require(cleanBaseUrl.isNotBlank()) {
            "La URL del backend es obligatoria."
        }

        val cleanEmail = email.trim()
        val cleanTenant = tenantSlug.trim()

        val response = AuthApi(cleanBaseUrl)
            .login(
                email = cleanEmail,
                password = password,
                tenantSlug = cleanTenant,
            )
            .getOrThrow()

        /**
         * Guardamos las credenciales para poder volver a hacer
         * login automáticamente en el próximo arranque.
         */
        saveCredentials(
            email = cleanEmail,
            password = password,
            tenantSlug = cleanTenant,
        )

        /**
         * Guardamos el JWT en la configuración existente.
         *
         * CatalogApi, SalesOrderApi y PrintJobApi ya utilizan
         * settings.deviceToken como Bearer token.
         */
        settings.deviceToken = response.accessToken

        response
    }

    /**
     * Hace login usando las credenciales almacenadas.
     *
     * Esto permite el login automático al iniciar la APK.
     */
    fun loginWithSavedCredentials(
        baseUrl: String,
    ): Result<LoginResponse> {

        val credentials =
            getSavedCredentials()
                ?: return Result.failure(
                    IllegalStateException(
                        "No existen credenciales configuradas."
                    )
                )

        return login(
            baseUrl = baseUrl,
            email = credentials.email,
            password = credentials.password,
            tenantSlug = credentials.tenantSlug,
        )
    }

    /**
     * Indica si existen credenciales guardadas.
     */
    fun hasSavedCredentials(): Boolean =
        getSavedCredentials() != null

    /**
     * Devuelve el token actualmente guardado.
     */
    fun currentToken(): String =
        settings.deviceToken

    /**
     * Cierra la sesión y elimina las credenciales almacenadas.
     */
    fun logout() {
        settings.deviceToken = ""

        prefs.edit()
            .remove(KEY_EMAIL)
            .remove(KEY_PASSWORD)
            .remove(KEY_TENANT_SLUG)
            .apply()
    }

    // ---------------------------------------------------------------------
    // Android Keystore
    // ---------------------------------------------------------------------

    private fun getOrCreateSecretKey(): SecretKey {

        val keyStore = java.security.KeyStore
            .getInstance(ANDROID_KEYSTORE)
            .apply {
                load(null)
            }

        val existingKey =
            keyStore.getKey(KEY_ALIAS, null)

        if (existingKey is SecretKey) {
            return existingKey
        }

        val keyGenerator =
            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE,
            )

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(
                KeyProperties.BLOCK_MODE_GCM
            )
            .setEncryptionPaddings(
                KeyProperties.ENCRYPTION_PADDING_NONE
            )
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(spec)

        return keyGenerator.generateKey()
    }

    private fun encrypt(value: String): String {

        val cipher = Cipher.getInstance(
            TRANSFORMATION
        )

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getOrCreateSecretKey(),
        )

        val encrypted = cipher.doFinal(
            value.toByteArray(
                StandardCharsets.UTF_8
            )
        )

        val iv = Base64.encodeToString(
            cipher.iv,
            Base64.NO_WRAP,
        )

        val data = Base64.encodeToString(
            encrypted,
            Base64.NO_WRAP,
        )

        return "$iv:$data"
    }

    private fun decrypt(value: String): String {

        val parts = value.split(":", limit = 2)

        require(parts.size == 2) {
            "Dato cifrado inválido."
        }

        val iv = Base64.decode(
            parts[0],
            Base64.NO_WRAP,
        )

        val encrypted = Base64.decode(
            parts[1],
            Base64.NO_WRAP,
        )

        val cipher = Cipher.getInstance(
            TRANSFORMATION
        )

        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(),
            GCMParameterSpec(
                GCM_TAG_LENGTH_BITS,
                iv,
            ),
        )

        return String(
            cipher.doFinal(encrypted),
            StandardCharsets.UTF_8,
        )
    }

    companion object {

        private const val PREFS_NAME =
            "host_print_auth_secure"

        private const val KEY_EMAIL =
            "email"

        private const val KEY_PASSWORD =
            "password"

        private const val KEY_TENANT_SLUG =
            "tenant_slug"

        private const val KEY_ALIAS =
            "host_print_gateway_auth_key"

        private const val ANDROID_KEYSTORE =
            "AndroidKeyStore"

        private const val TRANSFORMATION =
            "AES/GCM/NoPadding"

        private const val GCM_TAG_LENGTH_BITS =
            128
    }
}

data class SavedCredentials(
    val email: String,
    val password: String,
    val tenantSlug: String,
)