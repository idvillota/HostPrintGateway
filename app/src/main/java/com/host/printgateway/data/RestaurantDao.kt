package com.host.printgateway.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class RestaurantDao {

    @Query("SELECT * FROM dining_tables WHERE isActive = 1 ORDER BY code")
    abstract suspend fun getTables(): List<DiningTableEntity>

    @Query("SELECT * FROM product_types WHERE isActive = 1 ORDER BY sortOrder, name")
    abstract suspend fun getProductTypes(): List<ProductTypeEntity>

    @Query("SELECT * FROM products WHERE productTypeId = :typeId AND isActive = 1 ORDER BY name")
    abstract suspend fun getProducts(typeId: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    abstract suspend fun getProduct(id: String): ProductEntity?

    @Query(
        """
        SELECT ingredients.id, ingredients.categoryId, ingredients.name, ingredients.unit,
               ingredients.unitCost, ingredients.stockQuantity, ingredients.reorderLevel, ingredients.isActive
        FROM ingredients
        INNER JOIN product_ingredients ON product_ingredients.ingredientId = ingredients.id
        WHERE product_ingredients.productId = :productId AND ingredients.isActive = 1
        ORDER BY ingredients.name
        """,
    )
    abstract suspend fun getIngredientsForProduct(productId: String): List<IngredientEntity>

    @Query("SELECT * FROM ingredients WHERE isActive = 1 ORDER BY name")
    abstract suspend fun getIngredients(): List<IngredientEntity>

    @Query("SELECT * FROM printer_stations WHERE isActive = 1 ORDER BY name")
    abstract suspend fun getPrinters(): List<PrinterStationEntity>

    @Query("SELECT * FROM kitchen_tickets WHERE orderId = :orderId ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getTicketForOrder(orderId: String): KitchenTicketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replaceTables(items: List<DiningTableEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replaceProductTypes(items: List<ProductTypeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replaceProducts(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replaceIngredients(items: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replacePrinters(items: List<PrinterStationEntity>)

    @Query("DELETE FROM dining_tables")
    abstract suspend fun clearTables()

    @Query("DELETE FROM product_types")
    abstract suspend fun clearProductTypes()

    @Query("DELETE FROM products")
    abstract suspend fun clearProducts()

    @Query("DELETE FROM ingredients")
    abstract suspend fun clearIngredients()

    @Query("DELETE FROM product_ingredients")
    abstract suspend fun clearProductIngredients()

    @Query("DELETE FROM printer_stations")
    abstract suspend fun clearPrinters()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replaceProductIngredients(items: List<ProductIngredientEntity>)

    @Transaction
    open suspend fun replaceCatalog(
        tables: List<DiningTableEntity>,
        types: List<ProductTypeEntity>,
        products: List<ProductEntity>,
        ingredients: List<IngredientEntity>,
        productIngredients: List<ProductIngredientEntity>,
        printers: List<PrinterStationEntity>,
    ) {
        clearTables()
        clearProductTypes()
        clearProducts()
        clearIngredients()
        clearProductIngredients()
        clearPrinters()
        replaceTables(tables)
        replaceProductTypes(types)
        replaceProducts(products)
        replaceIngredients(ingredients)
        if (productIngredients.isNotEmpty()) {
            replaceProductIngredients(productIngredients)
        }
        replacePrinters(printers)
    }

    @Insert
    abstract suspend fun insertOrder(order: OrderEntity)

    @Insert
    abstract suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Insert
    abstract suspend fun insertTicket(ticket: KitchenTicketEntity)

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    abstract suspend fun getOrders(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE status != 'PAID' ORDER BY createdAt DESC")
    abstract suspend fun getUnpaidOrders(): List<OrderEntity>

    @Query("UPDATE orders SET status = 'PAID', closedAtUtc = :closedAt, syncStatus = :syncStatus WHERE id IN (:ids)")
    abstract suspend fun markOrdersPaid(ids: List<String>, closedAt: Long, syncStatus: String)

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    abstract suspend fun getOrder(id: String): OrderEntity?

    @Query("UPDATE dining_tables SET status = :status WHERE id = :id")
    abstract suspend fun updateTableStatus(id: String, status: String)

    @Query("SELECT * FROM orders WHERE syncStatus != 'SYNCED' ORDER BY createdAt")
    abstract suspend fun getOrdersPendingSync(): List<OrderEntity>

    @Query("SELECT remoteId FROM orders WHERE diningTableId = :tableId AND remoteId IS NOT NULL AND status != 'PAID' ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getRemoteOrderIdForTable(tableId: String): String?

    @Query("SELECT * FROM orders WHERE diningTableId = :tableId AND status != 'PAID' AND (remoteId IS NULL OR remoteId = '') ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getOpenLocalOrderForTable(tableId: String): OrderEntity?

    @Query("SELECT * FROM orders WHERE remoteId = :remoteId LIMIT 1")
    abstract suspend fun getOrderByRemoteId(remoteId: String): OrderEntity?

    @Query("SELECT * FROM orders WHERE remoteId IS NULL OR remoteId = '' ORDER BY createdAt DESC")
    abstract suspend fun getOrdersMissingOnHost(): List<OrderEntity>

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    abstract suspend fun deleteOrderItems(orderId: String)

    @Query("DELETE FROM kitchen_tickets WHERE orderId = :orderId")
    abstract suspend fun deleteTicketsForOrder(orderId: String)

    @Query("DELETE FROM orders WHERE id = :orderId AND (remoteId IS NULL OR remoteId = '')")
    abstract suspend fun deleteOrderMissingOnHost(orderId: String)

    @Query("UPDATE orders SET subtotal = :subtotal, total = :total WHERE id = :id")
    abstract suspend fun updateOrderTotals(id: String, subtotal: Double, total: Double)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id")
    abstract suspend fun getOrderItems(orderId: String): List<OrderItemEntity>

    @Query("UPDATE orders SET syncStatus = 'SYNCED', remoteId = :remoteId, status = CASE WHEN status = 'PAID' THEN 'PAID' ELSE 'SYNCED' END WHERE id = :localId")
    abstract suspend fun markOrderSynced(localId: String, remoteId: String)

    @Query("UPDATE orders SET syncStatus = 'FAILED' WHERE id = :localId")
    abstract suspend fun markOrderSyncFailed(localId: String)

    @Query("UPDATE orders SET remoteId = :remoteId WHERE id = :localId")
    abstract suspend fun saveRemoteOrderId(localId: String, remoteId: String)

    @Query("SELECT * FROM kitchen_tickets WHERE isPrinted = 0 ORDER BY createdAt")
    abstract suspend fun getPendingTickets(): List<KitchenTicketEntity>

    @Query("SELECT * FROM printer_stations WHERE id = :id LIMIT 1")
    abstract suspend fun getPrinter(id: String): PrinterStationEntity?

    @Query("UPDATE kitchen_tickets SET isPrinted = 1, status = 'PRINTED', printedAt = :printedAt, lastError = NULL WHERE id = :id")
    abstract suspend fun markTicketPrinted(id: String, printedAt: Long)

    @Query("UPDATE kitchen_tickets SET attempts = attempts + 1, status = 'FAILED', lastError = :error WHERE id = :id")
    abstract suspend fun markTicketFailed(id: String, error: String)

    @Transaction
    open suspend fun deleteLocalOnlyOrder(orderId: String) {
        deleteOrderItems(orderId)
        deleteTicketsForOrder(orderId)
        deleteOrderMissingOnHost(orderId)
    }

    @Transaction
    open suspend fun saveOrderWithTicket(order: OrderEntity, items: List<OrderItemEntity>, ticket: KitchenTicketEntity) {
        insertOrder(order)
        insertOrderItems(items)
        insertTicket(ticket)
    }
}