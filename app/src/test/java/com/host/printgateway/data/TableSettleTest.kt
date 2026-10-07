package com.host.printgateway.data

import com.host.printgateway.network.RemoteTableAccount
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TableSettleTest {

    @Test
    fun freeHostTableIds_includes_null_and_blank_open_orders_lowercased() {
        val free = TableSettle.freeHostTableIds(
            listOf(
                RemoteTableAccount(tableId = "AbC", openOrderId = null),
                RemoteTableAccount(tableId = "DEF", openOrderId = ""),
                RemoteTableAccount(tableId = "GHI", openOrderId = "   "),
                RemoteTableAccount(tableId = "JKL", openOrderId = "still-open"),
            ),
        )

        assertEquals(setOf("abc", "def", "ghi"), free)
        assertTrue("jkl" !in free)
    }

    @Test
    fun freeHostTableIds_empty_when_all_occupied() {
        val free = TableSettle.freeHostTableIds(
            listOf(RemoteTableAccount("t1", "o1"), RemoteTableAccount("t2", "o2")),
        )
        assertTrue(free.isEmpty())
    }
}
