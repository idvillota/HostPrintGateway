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

    @Query("SELECT * FROM orders WHERE status = 'SYNC_PENDING' ORDER BY createdAt")
    abstract suspend fun getOrdersPendingSync(): List<OrderEntity>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id")
    abstract suspend fun getOrderItems(orderId: String): List<OrderItemEntity>

    @Query("UPDATE orders SET status = 'SYNCED', remoteId = :remoteId WHERE id = :localId")
    abstract suspend fun markOrderSynced(localId: String, remoteId: String)

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
    open suspend fun saveOrderWithTicket(order: OrderEntity, items: List<OrderItemEntity>, ticket: KitchenTicketEntity) {
        insertOrder(order)
        insertOrderItems(items)
        insertTicket(ticket)
    }
}