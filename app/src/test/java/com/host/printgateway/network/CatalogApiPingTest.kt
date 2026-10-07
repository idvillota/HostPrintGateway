package com.host.printgateway.network

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Reachability must hit GET /health — never salon table summaries —
 * so Host Lite does not hammer ListTableSummaries every few seconds.
 */
class CatalogApiPingTest {

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
    fun ping_calls_health_not_sales_order_tables() {
        server.enqueue(MockResponse().setResponseCode(200).setBody("""{"status":"ok"}"""))

        val result = CatalogApi(baseUrl(), token = "session-token").ping()

        assertTrue(result.isSuccess)
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/health", request.path)
        assertEquals("Bearer session-token", request.getHeader("Authorization"))
    }

    @Test
    fun ping_maps_401_to_sync_unauthorized() {
        server.enqueue(MockResponse().setResponseCode(401).setBody("expired"))

        val result = CatalogApi(baseUrl(), token = "stale").ping()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is SyncUnauthorized)
        assertEquals("/health", server.takeRequest().path)
    }

    @Test
    fun ping_fails_on_non_success_status() {
        server.enqueue(MockResponse().setResponseCode(503).setBody("down"))

        val result = CatalogApi(baseUrl()).ping()

        assertTrue(result.isFailure)
        assertEquals("HOST no responde", result.exceptionOrNull()?.message)
    }

    private fun baseUrl(): String = server.url("/").toString().trimEnd('/')
}
