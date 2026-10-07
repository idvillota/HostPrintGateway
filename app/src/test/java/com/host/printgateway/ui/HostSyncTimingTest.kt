package com.host.printgateway.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HostSyncTimingTest {

    @Test
    fun settle_poll_is_thirty_seconds_and_slower_than_reachability() {
        assertEquals(30_000L, HostSyncTiming.TABLE_SETTLE_POLL_MS)
        assertEquals(5_000L, HostSyncTiming.REACH_POLL_MS)
        assertTrue(HostSyncTiming.TABLE_SETTLE_POLL_MS > HostSyncTiming.REACH_POLL_MS)
    }

    @Test
    fun offline_offer_requires_multiple_failed_pings() {
        assertEquals(20_000L, HostSyncTiming.OFFLINE_AFTER_MS)
        assertTrue(HostSyncTiming.OFFLINE_AFTER_MS >= HostSyncTiming.REACH_POLL_MS * 4)
    }
}
