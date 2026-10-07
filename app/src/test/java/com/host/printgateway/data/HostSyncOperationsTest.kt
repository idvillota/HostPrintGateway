package com.host.printgateway.data

import com.host.printgateway.network.SyncUnauthorized
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HostSyncOperationsTest {

    @Before
    fun setUp() {
        mockkConstructor(RestaurantRepository::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun pushThenPull_stops_on_unauthorized_upload() = runBlocking {
        val database = mockk<PrintGatewayDatabase>(relaxed = true)
        val settings = mockk<GatewaySettings>(relaxed = true)

        coEvery {
            anyConstructed<RestaurantRepository>().syncPendingOrders(any(), any())
        } returns Result.failure(SyncUnauthorized("expired"))

        val result = HostSyncOperations.pushThenPull(
            database = database,
            token = "tok",
            baseUrl = "http://host",
            deviceId = "device",
            settings = settings,
        )

        assertEquals(HostSyncOperations.OfflineSyncResult.Unauthorized, result)
        coVerify(exactly = 0) { anyConstructed<RestaurantRepository>().syncCatalog(any()) }
    }

    @Test
    fun pushThenPull_completes_full_sequence_and_stores_cursor() = runBlocking {
        val database = mockk<PrintGatewayDatabase>(relaxed = true)
        val settings = mockk<GatewaySettings>(relaxed = true)
        every { settings.saleCursor } returns "prev"

        coEvery { anyConstructed<RestaurantRepository>().syncPendingOrders(any(), any()) } returns Result.success(2)
        coEvery { anyConstructed<RestaurantRepository>().syncCatalog(any()) } returns Result.success(1)
        coEvery { anyConstructed<RestaurantRepository>().closeOrdersOnFreeHostTables() } returns Unit
        coEvery { anyConstructed<RestaurantRepository>().releaseTablesSettledOnHost(any()) } returns Result.success(true)
        coEvery {
            anyConstructed<RestaurantRepository>().catchUpOpenAccounts(any(), any(), any())
        } returns Result.success("cursor-9")

        val result = HostSyncOperations.pushThenPull(
            database = database,
            token = "tok",
            baseUrl = "http://host",
            deviceId = "device",
            settings = settings,
        )

        assertEquals(HostSyncOperations.OfflineSyncResult.Done, result)
        coVerify { settings.saleCursor = "cursor-9" }
        coVerify { anyConstructed<RestaurantRepository>().closeOrdersOnFreeHostTables() }
        coVerify { anyConstructed<RestaurantRepository>().releaseTablesSettledOnHost("http://host") }
    }

    @Test
    fun pushThenPull_maps_catalog_failure() = runBlocking {
        val database = mockk<PrintGatewayDatabase>(relaxed = true)
        val settings = mockk<GatewaySettings>(relaxed = true)

        coEvery { anyConstructed<RestaurantRepository>().syncPendingOrders(any(), any()) } returns Result.success(0)
        coEvery {
            anyConstructed<RestaurantRepository>().syncCatalog(any())
        } returns Result.failure(IllegalStateException("down"))

        val result = HostSyncOperations.pushThenPull(
            database = database,
            token = "tok",
            baseUrl = "http://host",
            deviceId = "device",
            settings = settings,
        )

        assertTrue(result is HostSyncOperations.OfflineSyncResult.Failed)
        assertEquals("down", (result as HostSyncOperations.OfflineSyncResult.Failed).message)
    }
}
