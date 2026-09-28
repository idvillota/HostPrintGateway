package com.host.printgateway.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PrintGatewayDatabase_Impl extends PrintGatewayDatabase {
  private volatile PrintJobDao _printJobDao;

  private volatile RestaurantDao _restaurantDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `print_jobs` (`id` TEXT NOT NULL, `kind` TEXT NOT NULL, `payloadFormat` TEXT NOT NULL, `payload` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `dining_tables` (`id` TEXT NOT NULL, `code` TEXT NOT NULL, `capacity` INTEGER NOT NULL, `zone` TEXT, `layoutX` REAL, `layoutY` REAL, `status` TEXT NOT NULL, `isActive` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `product_types` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `sortOrder` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`id` TEXT NOT NULL, `productTypeId` TEXT NOT NULL, `compositionType` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `sku` TEXT, `imagePath` TEXT, `unitPrice` REAL NOT NULL, `isActive` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ingredients` (`id` TEXT NOT NULL, `categoryId` TEXT NOT NULL, `name` TEXT NOT NULL, `unit` TEXT NOT NULL, `unitCost` REAL, `stockQuantity` REAL, `reorderLevel` REAL, `isActive` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ingredient_categories` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `sortOrder` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `product_ingredients` (`id` TEXT NOT NULL, `productId` TEXT NOT NULL, `ingredientId` TEXT NOT NULL, `quantity` REAL NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `product_bundle_lines` (`id` TEXT NOT NULL, `productId` TEXT NOT NULL, `componentProductId` TEXT NOT NULL, `quantity` REAL NOT NULL, `sortOrder` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `printer_stations` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `code` TEXT NOT NULL, `bluetoothMac` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `sortOrder` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `product_type_printer_mappings` (`id` TEXT NOT NULL, `productTypeId` TEXT NOT NULL, `printerStationId` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `orders` (`id` TEXT NOT NULL, `diningTableId` TEXT, `diningTableCode` TEXT NOT NULL, `number` TEXT NOT NULL, `customerId` TEXT, `waiterName` TEXT NOT NULL, `deviceId` TEXT NOT NULL, `status` TEXT NOT NULL, `openedAtUtc` INTEGER NOT NULL, `subtotal` REAL NOT NULL, `taxAmount` REAL NOT NULL, `total` REAL NOT NULL, `createdAt` INTEGER NOT NULL, `remoteId` TEXT, `closedAtUtc` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `order_items` (`id` TEXT NOT NULL, `orderId` TEXT NOT NULL, `productId` TEXT NOT NULL, `productName` TEXT NOT NULL, `quantity` REAL NOT NULL, `unitPrice` REAL NOT NULL, `lineTotal` REAL NOT NULL, `unitCostPrice` REAL, `notes` TEXT NOT NULL, `sentToKitchenAtUtc` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `kitchen_tickets` (`id` TEXT NOT NULL, `orderId` TEXT NOT NULL, `printerStationId` TEXT, `payload` TEXT NOT NULL, `status` TEXT NOT NULL, `isPrinted` INTEGER NOT NULL, `attempts` INTEGER NOT NULL, `lastError` TEXT, `createdAt` INTEGER NOT NULL, `printedAt` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '30aa8d28abadc08de51e68059691a4d1')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `print_jobs`");
        db.execSQL("DROP TABLE IF EXISTS `dining_tables`");
        db.execSQL("DROP TABLE IF EXISTS `product_types`");
        db.execSQL("DROP TABLE IF EXISTS `products`");
        db.execSQL("DROP TABLE IF EXISTS `ingredients`");
        db.execSQL("DROP TABLE IF EXISTS `ingredient_categories`");
        db.execSQL("DROP TABLE IF EXISTS `product_ingredients`");
        db.execSQL("DROP TABLE IF EXISTS `product_bundle_lines`");
        db.execSQL("DROP TABLE IF EXISTS `printer_stations`");
        db.execSQL("DROP TABLE IF EXISTS `product_type_printer_mappings`");
        db.execSQL("DROP TABLE IF EXISTS `orders`");
        db.execSQL("DROP TABLE IF EXISTS `order_items`");
        db.execSQL("DROP TABLE IF EXISTS `kitchen_tickets`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPrintJobs = new HashMap<String, TableInfo.Column>(6);
        _columnsPrintJobs.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintJobs.put("kind", new TableInfo.Column("kind", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintJobs.put("payloadFormat", new TableInfo.Column("payloadFormat", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintJobs.put("payload", new TableInfo.Column("payload", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintJobs.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrintJobs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrintJobs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrintJobs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrintJobs = new TableInfo("print_jobs", _columnsPrintJobs, _foreignKeysPrintJobs, _indicesPrintJobs);
        final TableInfo _existingPrintJobs = TableInfo.read(db, "print_jobs");
        if (!_infoPrintJobs.equals(_existingPrintJobs)) {
          return new RoomOpenHelper.ValidationResult(false, "print_jobs(com.host.printgateway.data.PrintJobEntity).\n"
                  + " Expected:\n" + _infoPrintJobs + "\n"
                  + " Found:\n" + _existingPrintJobs);
        }
        final HashMap<String, TableInfo.Column> _columnsDiningTables = new HashMap<String, TableInfo.Column>(8);
        _columnsDiningTables.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("capacity", new TableInfo.Column("capacity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("zone", new TableInfo.Column("zone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("layoutX", new TableInfo.Column("layoutX", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("layoutY", new TableInfo.Column("layoutY", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiningTables.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDiningTables = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDiningTables = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDiningTables = new TableInfo("dining_tables", _columnsDiningTables, _foreignKeysDiningTables, _indicesDiningTables);
        final TableInfo _existingDiningTables = TableInfo.read(db, "dining_tables");
        if (!_infoDiningTables.equals(_existingDiningTables)) {
          return new RoomOpenHelper.ValidationResult(false, "dining_tables(com.host.printgateway.data.DiningTableEntity).\n"
                  + " Expected:\n" + _infoDiningTables + "\n"
                  + " Found:\n" + _existingDiningTables);
        }
        final HashMap<String, TableInfo.Column> _columnsProductTypes = new HashMap<String, TableInfo.Column>(5);
        _columnsProductTypes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductTypes.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductTypes.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductTypes.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductTypes.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductTypes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductTypes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductTypes = new TableInfo("product_types", _columnsProductTypes, _foreignKeysProductTypes, _indicesProductTypes);
        final TableInfo _existingProductTypes = TableInfo.read(db, "product_types");
        if (!_infoProductTypes.equals(_existingProductTypes)) {
          return new RoomOpenHelper.ValidationResult(false, "product_types(com.host.printgateway.data.ProductTypeEntity).\n"
                  + " Expected:\n" + _infoProductTypes + "\n"
                  + " Found:\n" + _existingProductTypes);
        }
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(9);
        _columnsProducts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("productTypeId", new TableInfo.Column("productTypeId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("compositionType", new TableInfo.Column("compositionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("sku", new TableInfo.Column("sku", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("imagePath", new TableInfo.Column("imagePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("unitPrice", new TableInfo.Column("unitPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProducts = new TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "products(com.host.printgateway.data.ProductEntity).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsIngredients = new HashMap<String, TableInfo.Column>(8);
        _columnsIngredients.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("categoryId", new TableInfo.Column("categoryId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("unitCost", new TableInfo.Column("unitCost", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("stockQuantity", new TableInfo.Column("stockQuantity", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("reorderLevel", new TableInfo.Column("reorderLevel", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredients.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIngredients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIngredients = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIngredients = new TableInfo("ingredients", _columnsIngredients, _foreignKeysIngredients, _indicesIngredients);
        final TableInfo _existingIngredients = TableInfo.read(db, "ingredients");
        if (!_infoIngredients.equals(_existingIngredients)) {
          return new RoomOpenHelper.ValidationResult(false, "ingredients(com.host.printgateway.data.IngredientEntity).\n"
                  + " Expected:\n" + _infoIngredients + "\n"
                  + " Found:\n" + _existingIngredients);
        }
        final HashMap<String, TableInfo.Column> _columnsIngredientCategories = new HashMap<String, TableInfo.Column>(5);
        _columnsIngredientCategories.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredientCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredientCategories.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredientCategories.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIngredientCategories.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIngredientCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIngredientCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIngredientCategories = new TableInfo("ingredient_categories", _columnsIngredientCategories, _foreignKeysIngredientCategories, _indicesIngredientCategories);
        final TableInfo _existingIngredientCategories = TableInfo.read(db, "ingredient_categories");
        if (!_infoIngredientCategories.equals(_existingIngredientCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "ingredient_categories(com.host.printgateway.data.IngredientCategoryEntity).\n"
                  + " Expected:\n" + _infoIngredientCategories + "\n"
                  + " Found:\n" + _existingIngredientCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsProductIngredients = new HashMap<String, TableInfo.Column>(4);
        _columnsProductIngredients.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductIngredients.put("productId", new TableInfo.Column("productId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductIngredients.put("ingredientId", new TableInfo.Column("ingredientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductIngredients.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductIngredients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductIngredients = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductIngredients = new TableInfo("product_ingredients", _columnsProductIngredients, _foreignKeysProductIngredients, _indicesProductIngredients);
        final TableInfo _existingProductIngredients = TableInfo.read(db, "product_ingredients");
        if (!_infoProductIngredients.equals(_existingProductIngredients)) {
          return new RoomOpenHelper.ValidationResult(false, "product_ingredients(com.host.printgateway.data.ProductIngredientEntity).\n"
                  + " Expected:\n" + _infoProductIngredients + "\n"
                  + " Found:\n" + _existingProductIngredients);
        }
        final HashMap<String, TableInfo.Column> _columnsProductBundleLines = new HashMap<String, TableInfo.Column>(5);
        _columnsProductBundleLines.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductBundleLines.put("productId", new TableInfo.Column("productId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductBundleLines.put("componentProductId", new TableInfo.Column("componentProductId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductBundleLines.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductBundleLines.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductBundleLines = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductBundleLines = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductBundleLines = new TableInfo("product_bundle_lines", _columnsProductBundleLines, _foreignKeysProductBundleLines, _indicesProductBundleLines);
        final TableInfo _existingProductBundleLines = TableInfo.read(db, "product_bundle_lines");
        if (!_infoProductBundleLines.equals(_existingProductBundleLines)) {
          return new RoomOpenHelper.ValidationResult(false, "product_bundle_lines(com.host.printgateway.data.ProductBundleLineEntity).\n"
                  + " Expected:\n" + _infoProductBundleLines + "\n"
                  + " Found:\n" + _existingProductBundleLines);
        }
        final HashMap<String, TableInfo.Column> _columnsPrinterStations = new HashMap<String, TableInfo.Column>(6);
        _columnsPrinterStations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrinterStations.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrinterStations.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrinterStations.put("bluetoothMac", new TableInfo.Column("bluetoothMac", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrinterStations.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrinterStations.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrinterStations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrinterStations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrinterStations = new TableInfo("printer_stations", _columnsPrinterStations, _foreignKeysPrinterStations, _indicesPrinterStations);
        final TableInfo _existingPrinterStations = TableInfo.read(db, "printer_stations");
        if (!_infoPrinterStations.equals(_existingPrinterStations)) {
          return new RoomOpenHelper.ValidationResult(false, "printer_stations(com.host.printgateway.data.PrinterStationEntity).\n"
                  + " Expected:\n" + _infoPrinterStations + "\n"
                  + " Found:\n" + _existingPrinterStations);
        }
        final HashMap<String, TableInfo.Column> _columnsProductTypePrinterMappings = new HashMap<String, TableInfo.Column>(3);
        _columnsProductTypePrinterMappings.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductTypePrinterMappings.put("productTypeId", new TableInfo.Column("productTypeId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductTypePrinterMappings.put("printerStationId", new TableInfo.Column("printerStationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductTypePrinterMappings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductTypePrinterMappings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductTypePrinterMappings = new TableInfo("product_type_printer_mappings", _columnsProductTypePrinterMappings, _foreignKeysProductTypePrinterMappings, _indicesProductTypePrinterMappings);
        final TableInfo _existingProductTypePrinterMappings = TableInfo.read(db, "product_type_printer_mappings");
        if (!_infoProductTypePrinterMappings.equals(_existingProductTypePrinterMappings)) {
          return new RoomOpenHelper.ValidationResult(false, "product_type_printer_mappings(com.host.printgateway.data.ProductTypePrinterMappingEntity).\n"
                  + " Expected:\n" + _infoProductTypePrinterMappings + "\n"
                  + " Found:\n" + _existingProductTypePrinterMappings);
        }
        final HashMap<String, TableInfo.Column> _columnsOrders = new HashMap<String, TableInfo.Column>(15);
        _columnsOrders.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("diningTableId", new TableInfo.Column("diningTableId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("diningTableCode", new TableInfo.Column("diningTableCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("number", new TableInfo.Column("number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("customerId", new TableInfo.Column("customerId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("waiterName", new TableInfo.Column("waiterName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("deviceId", new TableInfo.Column("deviceId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("openedAtUtc", new TableInfo.Column("openedAtUtc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("subtotal", new TableInfo.Column("subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("taxAmount", new TableInfo.Column("taxAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("total", new TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("remoteId", new TableInfo.Column("remoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("closedAtUtc", new TableInfo.Column("closedAtUtc", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrders = new TableInfo("orders", _columnsOrders, _foreignKeysOrders, _indicesOrders);
        final TableInfo _existingOrders = TableInfo.read(db, "orders");
        if (!_infoOrders.equals(_existingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "orders(com.host.printgateway.data.OrderEntity).\n"
                  + " Expected:\n" + _infoOrders + "\n"
                  + " Found:\n" + _existingOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsOrderItems = new HashMap<String, TableInfo.Column>(10);
        _columnsOrderItems.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("orderId", new TableInfo.Column("orderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("productId", new TableInfo.Column("productId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("productName", new TableInfo.Column("productName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("unitPrice", new TableInfo.Column("unitPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("lineTotal", new TableInfo.Column("lineTotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("unitCostPrice", new TableInfo.Column("unitCostPrice", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("sentToKitchenAtUtc", new TableInfo.Column("sentToKitchenAtUtc", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrderItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrderItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrderItems = new TableInfo("order_items", _columnsOrderItems, _foreignKeysOrderItems, _indicesOrderItems);
        final TableInfo _existingOrderItems = TableInfo.read(db, "order_items");
        if (!_infoOrderItems.equals(_existingOrderItems)) {
          return new RoomOpenHelper.ValidationResult(false, "order_items(com.host.printgateway.data.OrderItemEntity).\n"
                  + " Expected:\n" + _infoOrderItems + "\n"
                  + " Found:\n" + _existingOrderItems);
        }
        final HashMap<String, TableInfo.Column> _columnsKitchenTickets = new HashMap<String, TableInfo.Column>(10);
        _columnsKitchenTickets.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("orderId", new TableInfo.Column("orderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("printerStationId", new TableInfo.Column("printerStationId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("payload", new TableInfo.Column("payload", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("isPrinted", new TableInfo.Column("isPrinted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("attempts", new TableInfo.Column("attempts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("lastError", new TableInfo.Column("lastError", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKitchenTickets.put("printedAt", new TableInfo.Column("printedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysKitchenTickets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesKitchenTickets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoKitchenTickets = new TableInfo("kitchen_tickets", _columnsKitchenTickets, _foreignKeysKitchenTickets, _indicesKitchenTickets);
        final TableInfo _existingKitchenTickets = TableInfo.read(db, "kitchen_tickets");
        if (!_infoKitchenTickets.equals(_existingKitchenTickets)) {
          return new RoomOpenHelper.ValidationResult(false, "kitchen_tickets(com.host.printgateway.data.KitchenTicketEntity).\n"
                  + " Expected:\n" + _infoKitchenTickets + "\n"
                  + " Found:\n" + _existingKitchenTickets);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "30aa8d28abadc08de51e68059691a4d1", "0aa2c152899540e055ec5247e117935d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "print_jobs","dining_tables","product_types","products","ingredients","ingredient_categories","product_ingredients","product_bundle_lines","printer_stations","product_type_printer_mappings","orders","order_items","kitchen_tickets");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `print_jobs`");
      _db.execSQL("DELETE FROM `dining_tables`");
      _db.execSQL("DELETE FROM `product_types`");
      _db.execSQL("DELETE FROM `products`");
      _db.execSQL("DELETE FROM `ingredients`");
      _db.execSQL("DELETE FROM `ingredient_categories`");
      _db.execSQL("DELETE FROM `product_ingredients`");
      _db.execSQL("DELETE FROM `product_bundle_lines`");
      _db.execSQL("DELETE FROM `printer_stations`");
      _db.execSQL("DELETE FROM `product_type_printer_mappings`");
      _db.execSQL("DELETE FROM `orders`");
      _db.execSQL("DELETE FROM `order_items`");
      _db.execSQL("DELETE FROM `kitchen_tickets`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PrintJobDao.class, PrintJobDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestaurantDao.class, RestaurantDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PrintJobDao printJobDao() {
    if (_printJobDao != null) {
      return _printJobDao;
    } else {
      synchronized(this) {
        if(_printJobDao == null) {
          _printJobDao = new PrintJobDao_Impl(this);
        }
        return _printJobDao;
      }
    }
  }

  @Override
  public RestaurantDao restaurantDao() {
    if (_restaurantDao != null) {
      return _restaurantDao;
    } else {
      synchronized(this) {
        if(_restaurantDao == null) {
          _restaurantDao = new RestaurantDao_Impl(this);
        }
        return _restaurantDao;
      }
    }
  }
}
