package com.host.printgateway.network

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CatalogApiFetchTableAccountsTest {

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
    fun fetchTableAccounts_uses_sales_orders_tables_endpoint() {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    [
                      {"tableId":"T-1","openOrderId":"ord-1"},
                      {"tableId":"T-2","openOrderId":null},
                      {"tableId":"T-3","openOrderId":""}
                    ]
                    """.trimIndent(),
                ),
        )

        val result = CatalogApi(baseUrl(), token = "tok").fetchTableAccounts()

        assertTrue(result.isSuccess)
        val accounts = result.getOrThrow()
        assertEquals(3, accounts.size)
        assertEquals("ord-1", accounts[0].openOrderId)
        assertNull(accounts[1].openOrderId)
        assertNull(accounts[2].openOrderId)

        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/SalesOrders/tables", request.path)
    }

    @Test
    fun fetchTableAccounts_reads_items_wrapper() {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{"items":[{"tableId":"A","openOrderId":"x"}]}"""),
        )

        val accounts = CatalogApi(baseUrl()).fetchTableAccounts().getOrThrow()

        assertEquals(listOf(RemoteTableAccount("A", "x")), accounts)
    }

    private fun baseUrl(): String = server.url("/").toString().trimEnd('/')
}
