package com.host.printgateway.network

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Host API returns a raw JWT in `accessToken` (no Bearer prefix).
 * Host Lite stores that value in GatewaySettings.deviceToken and every
 * ApiClient / SaleChannel request must send exactly `Authorization: Bearer <jwt>`.
 */
class BackendAuthTest {

    @Test
    fun authorizationValue_matches_host_jwt_bearer_contract() {
        val jwt = "eyJhbGciOiJIUzI1NiJ9.payload.signature"

        // Normal login path (AuthManager stores accessToken as-is).
        assertEquals("Bearer $jwt", BackendAuth.authorizationValue(jwt))

        // Idempotent if a caller already prefixed Bearer.
        assertEquals("Bearer $jwt", BackendAuth.authorizationValue("Bearer $jwt"))

        // Whitespace around a raw JWT (prefs / paste) must not break Host validation.
        assertEquals("Bearer $jwt", BackendAuth.authorizationValue("  $jwt  "))

        // Whitespace before an already-prefixed value must not double-prefix.
        assertEquals("Bearer $jwt", BackendAuth.authorizationValue("  Bearer $jwt  "))
    }

    @Test
    fun authorization_header_name_is_standard() {
        assertEquals("Authorization", BackendAuth.AUTHORIZATION_HEADER)
    }
}
