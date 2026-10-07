package com.host.printgateway.data

import com.host.printgateway.network.RemoteTableAccount

/**
 * Pure helpers for freeing local tables when HOST reports no open account.
 */
object TableSettle {

    /**
     * Table ids HOST considers free (no open order), normalized for local matching.
     */
    fun freeHostTableIds(summaries: List<RemoteTableAccount>): Set<String> =
        summaries
            .filter { it.openOrderId.isNullOrBlank() }
            .map { it.tableId.lowercase() }
            .toSet()
}
