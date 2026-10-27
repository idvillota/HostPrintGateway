package com.host.printgateway.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncMarkersTest {

    @Test
    fun paymentMarker_without_invoice_keeps_the_previous_shape() {
        val marker = SyncMarkers.paymentMarker("Efectivo", 1.5, printReceipt = false)

        assertEquals("PAY|Efectivo|1.50", marker)
        val parsed = SyncMarkers.parsePayment(marker)
        assertEquals("Efectivo", parsed.method)
        assertEquals(1.5, parsed.tip, 0.001)
        assertFalse(parsed.printReceipt)
    }

    @Test
    fun paymentMarker_with_invoice_round_trips_the_print_flag() {
        val marker = SyncMarkers.paymentMarker("Tarjeta", 0.0, printReceipt = true)

        val parsed = SyncMarkers.parsePayment(marker)
        assertEquals("Tarjeta", parsed.method)
        assertEquals(0.0, parsed.tip, 0.001)
        assertTrue(parsed.printReceipt)
    }

    @Test
    fun parsePayment_reads_a_marker_saved_before_the_print_flag() {
        val parsed = SyncMarkers.parsePayment("PAY|Efectivo|0.00")

        assertEquals("Efectivo", parsed.method)
        assertEquals(0.0, parsed.tip, 0.001)
        assertFalse(parsed.printReceipt)
    }
}
