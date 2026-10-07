package com.host.printgateway.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalAccountActionsTest {

    @Test
    fun closeAccountsOnFreeTables_marks_remote_unpaid_orders_paid_and_frees_table() = runTest {
        val dao = mockk<RestaurantDao>(relaxed = true)
        val unpaid = listOf(
            order(id = "o1", tableId = "T1", remoteId = "remote-1", status = RestaurantStatuses.ORDER_SYNCED),
            order(id = "o2", tableId = "T1", remoteId = null, status = RestaurantStatuses.ORDER_SYNCED),
        )
        coEvery { dao.getUnpaidOrders() } returnsMany listOf(
            unpaid,
            emptyList(), // after mark paid
        )
        coEvery { dao.getTables() } returns listOf(
            DiningTableEntity("T1", "M1", 4, null, null, null, status = "1", isActive = true),
        )

        val changed = LocalAccountActions.closeAccountsOnFreeTables(dao, setOf("t1"))

        assertTrue(changed)
        val paidIds = slot<List<String>>()
        coVerify { dao.markOrdersPaid(capture(paidIds), any(), RestaurantStatuses.SYNC_STATE_SYNCED) }
        assertEquals(listOf("o1"), paidIds.captured)
        coVerify { dao.updateTableStatus("T1", "0") }
    }

    @Test
    fun closeAccountsOnFreeTables_returns_false_when_no_matching_free_ids() = runTest {
        val dao = mockk<RestaurantDao>(relaxed = true)
        coEvery { dao.getUnpaidOrders() } returns listOf(
            order(id = "o1", tableId = "T9", remoteId = "r", status = RestaurantStatuses.ORDER_SYNCED),
        )
        coEvery { dao.getTables() } returns listOf(
            DiningTableEntity("T9", "M9", 2, null, null, null, status = "1", isActive = true),
        )

        val changed = LocalAccountActions.closeAccountsOnFreeTables(dao, setOf("t1"))

        assertFalse(changed)
        coVerify(exactly = 0) { dao.markOrdersPaid(any(), any(), any()) }
        coVerify(exactly = 0) { dao.updateTableStatus(any(), any()) }
    }

    @Test
    fun freeSettledTables_only_when_no_unpaid_remain() = runTest {
        val dao = mockk<RestaurantDao>(relaxed = true)
        coEvery { dao.getUnpaidOrders() } returns emptyList()

        LocalAccountActions.freeSettledTables(dao, listOf("T1", "T1"))

        coVerify(exactly = 1) { dao.updateTableStatus("T1", "0") }
    }

    private fun order(
        id: String,
        tableId: String?,
        remoteId: String?,
        status: String,
    ) = OrderEntity(
        id = id,
        diningTableId = tableId,
        diningTableCode = "M",
        number = "N",
        customerId = null,
        waiterName = "W",
        deviceId = "d",
        status = status,
        openedAtUtc = 1L,
        subtotal = 10.0,
        taxAmount = 0.0,
        total = 10.0,
        remoteId = remoteId,
        syncStatus = RestaurantStatuses.SYNC_STATE_PENDING,
    )
}
