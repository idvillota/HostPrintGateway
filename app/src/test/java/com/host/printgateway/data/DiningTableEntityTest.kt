package com.host.printgateway.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DiningTableEntityTest {

    @Test
    fun isOccupied_accepts_numeric_and_busy_status() {
        assertTrue(table(status = "1").isOccupied())
        assertTrue(table(status = "Busy").isOccupied())
        assertTrue(table(status = "busy").isOccupied())
        assertFalse(table(status = "0").isOccupied())
        assertFalse(table(status = "Available").isOccupied())
    }

    private fun table(status: String) = DiningTableEntity(
        id = "id",
        code = "M1",
        capacity = 4,
        zone = null,
        layoutX = null,
        layoutY = null,
        status = status,
        isActive = true,
    )
}
