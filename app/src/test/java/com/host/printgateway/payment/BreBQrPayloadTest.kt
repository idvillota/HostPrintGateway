package com.host.printgateway.payment

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BreBQrPayloadTest {
    @Test
    fun crc16_matches_the_standard_vector() {
        assertEquals("29B1", BreBQrPayload.crc16Ccitt("123456789"))
    }

    @Test
    fun build_locks_the_transfer_amount_and_key() {
        val payload = BreBQrPayload.build(
            BreBProfile(
                key = "@localkey",
                keyType = 4,
                merchantName = "José Café",
                city = "Bogotá",
                postalCode = "11001",
            ),
            amount = 15000.0,
            reference = "ABC123",
        )

        assertTrue(payload.contains("010211"))
        assertTrue(payload.contains("0409@localkey"))
        assertTrue(payload.contains("540815000.00"))
        assertTrue(payload.contains("5909Jose Cafe"))
        assertTrue(payload.contains("6006Bogota"))
        assertTrue(payload.contains("610511001"))
        assertTrue(payload.endsWith(BreBQrPayload.crc16Ccitt(payload.dropLast(4))))
        assertFalse(payload.dropLast(8).contains("6304"))
    }

    @Test
    fun build_rejects_a_blank_key() {
        val error = runCatching {
            BreBQrPayload.build(
                BreBProfile(key = "  ", keyType = 2, merchantName = "Local", city = "Cali"),
                amount = 10.0,
                reference = "REF1",
            )
        }.exceptionOrNull()

        assertTrue(error?.message?.contains("llave", ignoreCase = true) == true)
    }
}
