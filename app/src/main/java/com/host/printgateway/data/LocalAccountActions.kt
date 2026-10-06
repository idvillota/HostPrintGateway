package com.host.printgateway.data

/**
 * Shared local table/account mutations used by order payment and sync.
 */
internal object LocalAccountActions {

    suspend fun freeSettledTables(dao: RestaurantDao, tableIds: List<String>) {
        tableIds.distinct().forEach { tableId ->
            val stillOpen = dao.getUnpaidOrders().any { it.diningTableId == tableId }
            if (!stillOpen) dao.updateTableStatus(tableId, "0")
        }
    }

    suspend fun closeAccountsOnFreeTables(dao: RestaurantDao, freeIds: Set<String>): Boolean {
        if (freeIds.isEmpty()) return false
        fun isFree(tableId: String?) = tableId != null && tableId.lowercase() in freeIds
        val staleIds = dao.getUnpaidOrders()
            .filter { order -> isFree(order.diningTableId) && !order.remoteId.isNullOrBlank() }
            .map { it.id }
        if (staleIds.isNotEmpty()) {
            dao.markOrdersPaid(staleIds, System.currentTimeMillis(), RestaurantStatuses.SYNC_STATE_SYNCED)
        }
        var statusChanged = false
        dao.getTables().filter { table -> isFree(table.id) && table.isOccupied() }.forEach { table ->
            val stillUnpaid = dao.getUnpaidOrders().any { it.diningTableId.equals(table.id, ignoreCase = true) }
            if (!stillUnpaid) {
                dao.updateTableStatus(table.id, "0")
                statusChanged = true
            }
        }
        return staleIds.isNotEmpty() || statusChanged
    }
}
