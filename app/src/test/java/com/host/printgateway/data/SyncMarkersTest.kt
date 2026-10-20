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

    @Test
    fun moveMarker_round_trips_and_survives_a_later_payment() {
        val move = SyncMarkers.moveMarker("mesa-1", "mesa-4", at = 20, merge = false)
        val parsed = SyncMarkers.parseMove(move)

        assertEquals("mesa-1", parsed?.fromTableId)
        assertEquals("mesa-4", parsed?.toTableId)
        assertEquals(20L, parsed?.at)
        assertFalse(parsed?.merge == true)

        val paid = SyncMarkers.attachingPayment("PAY|Efectivo|1.50", move)
        val payment = SyncMarkers.parsePayment(paid)
        val kept = SyncMarkers.parseMove(paid)

        assertEquals("Efectivo", payment.method)
        assertEquals(1.5, payment.tip, 0.001)
        assertEquals("mesa-1", kept?.fromTableId)
        assertEquals("PAY|Efectivo|1.50", SyncMarkers.withoutMove(paid))
    }
}
