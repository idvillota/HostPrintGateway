package com.host.printgateway.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PrintJobEntity::class,
        DiningTableEntity::class,
        ProductTypeEntity::class,
        ProductEntity::class,
        IngredientEntity::class,
        IngredientCategoryEntity::class,
        ProductIngredientEntity::class,
        ProductBundleLineEntity::class,
        PrinterStationEntity::class,
        ProductTypePrinterMappingEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        KitchenTicketEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class PrintGatewayDatabase : RoomDatabase() {

    abstract fun printJobDao(): PrintJobDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var instance: PrintGatewayDatabase? = null

        fun get(context: Context): PrintGatewayDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PrintGatewayDatabase::class.java,
                    "print_gateway.db",
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build().also { instance = it }
            }

        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS dining_tables (id TEXT NOT NULL PRIMARY KEY, code TEXT NOT NULL, capacity INTEGER NOT NULL, zone TEXT NOT NULL, status TEXT NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS product_types (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, description TEXT NOT NULL, sortOrder INTEGER NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS products (id TEXT NOT NULL PRIMARY KEY, productTypeId TEXT NOT NULL, name TEXT NOT NULL, description TEXT NOT NULL, sku TEXT NOT NULL, unitPrice REAL NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS ingredients (id TEXT NOT NULL PRIMARY KEY, categoryId TEXT, name TEXT NOT NULL, unit TEXT NOT NULL, unitCost REAL NOT NULL, stockQuantity REAL NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS printer_stations (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, code TEXT NOT NULL, bluetoothMac TEXT NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS orders (id TEXT NOT NULL PRIMARY KEY, diningTableId TEXT NOT NULL, diningTableCode TEXT NOT NULL, waiterName TEXT NOT NULL, deviceId TEXT NOT NULL, status TEXT NOT NULL, createdAt INTEGER NOT NULL, remoteId TEXT)")
                database.execSQL("CREATE TABLE IF NOT EXISTS order_items (id TEXT NOT NULL PRIMARY KEY, orderId TEXT NOT NULL, productId TEXT NOT NULL, productName TEXT NOT NULL, quantity REAL NOT NULL, unitPrice REAL NOT NULL, notes TEXT NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS kitchen_tickets (id TEXT NOT NULL PRIMARY KEY, orderId TEXT NOT NULL, printerStationId TEXT, payload TEXT NOT NULL, isPrinted INTEGER NOT NULL, attempts INTEGER NOT NULL, lastError TEXT, createdAt INTEGER NOT NULL, printedAt INTEGER)")
            }
        }

        private val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE dining_tables_new (id TEXT NOT NULL PRIMARY KEY, code TEXT NOT NULL, capacity INTEGER NOT NULL, zone TEXT, layoutX REAL, layoutY REAL, status TEXT NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("INSERT INTO dining_tables_new (id, code, capacity, zone, status, isActive) SELECT id, code, capacity, zone, status, isActive FROM dining_tables")
                database.execSQL("DROP TABLE dining_tables")
                database.execSQL("ALTER TABLE dining_tables_new RENAME TO dining_tables")
                database.execSQL("CREATE TABLE product_types_new (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, description TEXT, sortOrder INTEGER NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("INSERT INTO product_types_new SELECT id, name, description, sortOrder, isActive FROM product_types")
                database.execSQL("DROP TABLE product_types")
                database.execSQL("ALTER TABLE product_types_new RENAME TO product_types")
                database.execSQL("CREATE TABLE products_new (id TEXT NOT NULL PRIMARY KEY, productTypeId TEXT NOT NULL, compositionType TEXT NOT NULL, name TEXT NOT NULL, description TEXT, sku TEXT, imagePath TEXT, unitPrice REAL NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("INSERT INTO products_new (id, productTypeId, compositionType, name, description, sku, unitPrice, isActive) SELECT id, productTypeId, 'Prepared', name, description, sku, unitPrice, isActive FROM products")
                database.execSQL("DROP TABLE products")
                database.execSQL("ALTER TABLE products_new RENAME TO products")
                database.execSQL("CREATE TABLE ingredients_new (id TEXT NOT NULL PRIMARY KEY, categoryId TEXT NOT NULL, name TEXT NOT NULL, unit TEXT NOT NULL, unitCost REAL, stockQuantity REAL, reorderLevel REAL, isActive INTEGER NOT NULL)")
                database.execSQL("INSERT INTO ingredients_new (id, categoryId, name, unit, unitCost, stockQuantity, isActive) SELECT id, COALESCE(categoryId, ''), name, unit, unitCost, stockQuantity, isActive FROM ingredients")
                database.execSQL("DROP TABLE ingredients")
                database.execSQL("ALTER TABLE ingredients_new RENAME TO ingredients")
                database.execSQL("ALTER TABLE printer_stations ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
                database.execSQL("CREATE TABLE orders_new (id TEXT NOT NULL PRIMARY KEY, diningTableId TEXT, diningTableCode TEXT NOT NULL, number TEXT NOT NULL, customerId TEXT, waiterName TEXT NOT NULL, deviceId TEXT NOT NULL, status TEXT NOT NULL, openedAtUtc INTEGER NOT NULL, subtotal REAL NOT NULL, taxAmount REAL NOT NULL, total REAL NOT NULL, createdAt INTEGER NOT NULL, remoteId TEXT, closedAtUtc INTEGER)")
                database.execSQL("INSERT INTO orders_new (id, diningTableId, diningTableCode, number, waiterName, deviceId, status, openedAtUtc, subtotal, taxAmount, total, createdAt, remoteId) SELECT id, diningTableId, diningTableCode, '', waiterName, deviceId, status, createdAt, 0, 0, 0, createdAt, remoteId FROM orders")
                database.execSQL("DROP TABLE orders")
                database.execSQL("ALTER TABLE orders_new RENAME TO orders")
                database.execSQL("ALTER TABLE order_items ADD COLUMN lineTotal REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE order_items ADD COLUMN unitCostPrice REAL")
                database.execSQL("ALTER TABLE order_items ADD COLUMN sentToKitchenAtUtc INTEGER")
                database.execSQL("ALTER TABLE kitchen_tickets ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING'")
                database.execSQL("CREATE TABLE IF NOT EXISTS ingredient_categories (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, description TEXT, sortOrder INTEGER NOT NULL, isActive INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS product_ingredients (id TEXT NOT NULL PRIMARY KEY, productId TEXT NOT NULL, ingredientId TEXT NOT NULL, quantity REAL NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS product_bundle_lines (id TEXT NOT NULL PRIMARY KEY, productId TEXT NOT NULL, componentProductId TEXT NOT NULL, quantity REAL NOT NULL, sortOrder INTEGER NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS product_type_printer_mappings (id TEXT NOT NULL PRIMARY KEY, productTypeId TEXT NOT NULL, printerStationId TEXT NOT NULL)")
            }
        }
    }
}