package com.host.printgateway.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * End-to-end settle path: HOST table summaries → free ids → local account close.
 */
class SyncRepositoryReleaseTablesTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun releaseTablesSettledOnHost_fetches_summaries_and_frees_local_table() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    [
                      {"tableId":"T1","openOrderId":null},
                      {"tableId":"T2","openOrderId":"still-open"}
                    ]
                    """.trimIndent(),
                ),
        )

        val dao = mockk<RestaurantDao>(relaxed = true)
        coEvery { dao.getUnpaidOrders() } returnsMany listOf(
            listOf(
                OrderEntity(
                    id = "local-1",
                    diningTableId = "T1",
                    diningTableCode = "M1",
                    number = "N1",
                    customerId = null,
                    waiterName = "W",
                    deviceId = "d",
                    status = RestaurantStatuses.ORDER_SYNCED,
                    openedAtUtc = 1L,
                    subtotal = 10.0,
                    taxAmount = 0.0,
                    total = 10.0,
                    remoteId = "remote-1",
                ),
            ),
            emptyList(),
        )
        coEvery { dao.getTables() } returns listOf(
            DiningTableEntity("T1", "M1", 4, null, null, null, "1", true),
            DiningTableEntity("T2", "M2", 2, null, null, null, "1", true),
        )

        val database = mockk<PrintGatewayDatabase>()
        every { database.restaurantDao() } returns dao

        val repo = SyncRepository(database, deviceToken = "tok")
        val result = repo.releaseTablesSettledOnHost(server.url("/").toString().trimEnd('/'))

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow())
        assertEquals("/api/SalesOrders/tables", server.takeRequest().path)
        coVerify { dao.markOrdersPaid(listOf("local-1"), any(), RestaurantStatuses.SYNC_STATE_SYNCED) }
        coVerify { dao.updateTableStatus("T1", "0") }
        coVerify(exactly = 0) { dao.updateTableStatus("T2", any()) }
    }
}
