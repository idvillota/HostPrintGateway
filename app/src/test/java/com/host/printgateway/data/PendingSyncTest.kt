package com.host.printgateway.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PendingSyncTest {

    @Test
    fun comanda_on_a_settled_table_uploads() {
        assertTrue(PendingSync.isReadyToUpload("mesa-3", "nuevo", unsettledTableKeys = setOf("mesa-2")))
    }

    @Test
    fun comanda_waits_while_its_table_payment_is_still_open() {
        assertFalse(PendingSync.isReadyToUpload("mesa-2", "nuevo", unsettledTableKeys = setOf("mesa-2")))
    }

    @Test
    fun comanda_without_a_table_uses_its_own_id() {
        assertFalse(PendingSync.isReadyToUpload(null, "suelto", unsettledTableKeys = setOf("suelto")))
        assertTrue(PendingSync.isReadyToUpload(null, "otro", unsettledTableKeys = setOf("suelto")))
    }

    @Test
    fun repeated_charges_on_one_table_stay_separate_and_oldest_first() {
        val charges = PendingSync.chargesByTable(
            listOf(
                PendingPayment("c", "mesa-2", closedAt = 30),
                PendingPayment("a", "mesa-2", closedAt = 10),
                PendingPayment("b", "mesa-2", closedAt = 20),
            ),
        )

        val mesa = charges.getValue("mesa-2")
        assertEquals(3, mesa.size)
        assertEquals(listOf("a"), mesa[0].map { it.orderId })
        assertEquals(listOf("b"), mesa[1].map { it.orderId })
        assertEquals(listOf("c"), mesa[2].map { it.orderId })
    }

    @Test
    fun orders_paid_in_the_same_tap_stay_one_charge() {
        val charges = PendingSync.chargesByTable(
            listOf(
                PendingPayment("a1", "mesa-2", closedAt = 10),
                PendingPayment("a2", "mesa-2", closedAt = 10),
                PendingPayment("b", "mesa-4", closedAt = 5),
            ),
        )

        assertEquals(listOf("a1", "a2"), charges.getValue("mesa-2").single().map { it.orderId })
        assertEquals(listOf("b"), charges.getValue("mesa-4").single().map { it.orderId })
    }
}
