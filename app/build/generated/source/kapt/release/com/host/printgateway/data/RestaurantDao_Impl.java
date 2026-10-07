package com.host.printgateway.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDao_Impl extends RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DiningTableEntity> __insertionAdapterOfDiningTableEntity;

  private final EntityInsertionAdapter<ProductTypeEntity> __insertionAdapterOfProductTypeEntity;

  private final EntityInsertionAdapter<ProductEntity> __insertionAdapterOfProductEntity;

  private final EntityInsertionAdapter<IngredientEntity> __insertionAdapterOfIngredientEntity;

  private final EntityInsertionAdapter<PrinterStationEntity> __insertionAdapterOfPrinterStationEntity;

  private final EntityInsertionAdapter<ProductIngredientEntity> __insertionAdapterOfProductIngredientEntity;

  private final EntityInsertionAdapter<OrderEntity> __insertionAdapterOfOrderEntity;

  private final EntityInsertionAdapter<OrderItemEntity> __insertionAdapterOfOrderItemEntity;

  private final EntityInsertionAdapter<KitchenTicketEntity> __insertionAdapterOfKitchenTicketEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearTables;

  private final SharedSQLiteStatement __preparedStmtOfClearProductTypes;

  private final SharedSQLiteStatement __preparedStmtOfClearProducts;

  private final SharedSQLiteStatement __preparedStmtOfClearIngredients;

  private final SharedSQLiteStatement __preparedStmtOfClearProductIngredients;

  private final SharedSQLiteStatement __preparedStmtOfClearPrinters;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTableStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOrderItems;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTicketsForOrder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOrderMissingOnHost;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderTotals;

  private final SharedSQLiteStatement __preparedStmtOfMarkOrderSynced;

  private final SharedSQLiteStatement __preparedStmtOfMarkOrderSyncFailed;

  private final SharedSQLiteStatement __preparedStmtOfSaveRemoteOrderId;

  private final SharedSQLiteStatement __preparedStmtOfClaimTicketForPrinting;

  private final SharedSQLiteStatement __preparedStmtOfMarkTicketPrinted;

  private final SharedSQLiteStatement __preparedStmtOfMarkTicketFailed;

  public RestaurantDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDiningTableEntity = new EntityInsertionAdapter<DiningTableEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `dining_tables` (`id`,`code`,`capacity`,`zone`,`layoutX`,`layoutY`,`status`,`isActive`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DiningTableEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getCode() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCode());
        }
        statement.bindLong(3, entity.getCapacity());
        if (entity.getZone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getZone());
        }
        if (entity.getLayoutX() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getLayoutX());
        }
        if (entity.getLayoutY() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getLayoutY());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getStatus());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__insertionAdapterOfProductTypeEntity = new EntityInsertionAdapter<ProductTypeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `product_types` (`id`,`name`,`description`,`sortOrder`,`isActive`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductTypeEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getSortOrder());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__insertionAdapterOfProductEntity = new EntityInsertionAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `products` (`id`,`productTypeId`,`compositionType`,`name`,`description`,`sku`,`imagePath`,`unitPrice`,`isActive`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getProductTypeId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProductTypeId());
        }
        if (entity.getCompositionType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCompositionType());
        }
        if (entity.getName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getSku() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSku());
        }
        if (entity.getImagePath() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImagePath());
        }
        statement.bindDouble(8, entity.getUnitPrice());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__insertionAdapterOfIngredientEntity = new EntityInsertionAdapter<IngredientEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ingredients` (`id`,`categoryId`,`name`,`unit`,`unitCost`,`stockQuantity`,`reorderLevel`,`isActive`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final IngredientEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCategoryId());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getUnit() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUnit());
        }
        if (entity.getUnitCost() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getUnitCost());
        }
        if (entity.getStockQuantity() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getStockQuantity());
        }
        if (entity.getReorderLevel() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getReorderLevel());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__insertionAdapterOfPrinterStationEntity = new EntityInsertionAdapter<PrinterStationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `printer_stations` (`id`,`name`,`code`,`bluetoothMac`,`isActive`,`sortOrder`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PrinterStationEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getCode() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCode());
        }
        if (entity.getBluetoothMac() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBluetoothMac());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getSortOrder());
      }
    };
    this.__insertionAdapterOfProductIngredientEntity = new EntityInsertionAdapter<ProductIngredientEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `product_ingredients` (`id`,`productId`,`ingredientId`,`quantity`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductIngredientEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getProductId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProductId());
        }
        if (entity.getIngredientId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIngredientId());
        }
        statement.bindDouble(4, entity.getQuantity());
      }
    };
    this.__insertionAdapterOfOrderEntity = new EntityInsertionAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `orders` (`id`,`diningTableId`,`diningTableCode`,`number`,`customerId`,`waiterName`,`deviceId`,`status`,`openedAtUtc`,`subtotal`,`taxAmount`,`total`,`createdAt`,`remoteId`,`closedAtUtc`,`syncStatus`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getDiningTableId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDiningTableId());
        }
        if (entity.getDiningTableCode() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDiningTableCode());
        }
        if (entity.getNumber() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNumber());
        }
        if (entity.getCustomerId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCustomerId());
        }
        if (entity.getWaiterName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getWaiterName());
        }
        if (entity.getDeviceId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDeviceId());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getStatus());
        }
        statement.bindLong(9, entity.getOpenedAtUtc());
        statement.bindDouble(10, entity.getSubtotal());
        statement.bindDouble(11, entity.getTaxAmount());
        statement.bindDouble(12, entity.getTotal());
        statement.bindLong(13, entity.getCreatedAt());
        if (entity.getRemoteId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getRemoteId());
        }
        if (entity.getClosedAtUtc() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getClosedAtUtc());
        }
        if (entity.getSyncStatus() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getSyncStatus());
        }
      }
    };
    this.__insertionAdapterOfOrderItemEntity = new EntityInsertionAdapter<OrderItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `order_items` (`id`,`orderId`,`productId`,`productName`,`quantity`,`unitPrice`,`lineTotal`,`unitCostPrice`,`notes`,`sentToKitchenAtUtc`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderItemEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getOrderId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOrderId());
        }
        if (entity.getProductId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getProductId());
        }
        if (entity.getProductName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getProductName());
        }
        statement.bindDouble(5, entity.getQuantity());
        statement.bindDouble(6, entity.getUnitPrice());
        statement.bindDouble(7, entity.getLineTotal());
        if (entity.getUnitCostPrice() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getUnitCostPrice());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        if (entity.getSentToKitchenAtUtc() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getSentToKitchenAtUtc());
        }
      }
    };
    this.__insertionAdapterOfKitchenTicketEntity = new EntityInsertionAdapter<KitchenTicketEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `kitchen_tickets` (`id`,`orderId`,`printerStationId`,`payload`,`status`,`isPrinted`,`attempts`,`lastError`,`createdAt`,`printedAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KitchenTicketEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getOrderId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOrderId());
        }
        if (entity.getPrinterStationId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPrinterStationId());
        }
        if (entity.getPayload() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPayload());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        final int _tmp = entity.isPrinted() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getAttempts());
        if (entity.getLastError() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLastError());
        }
        statement.bindLong(9, entity.getCreatedAt());
        if (entity.getPrintedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getPrintedAt());
        }
      }
    };
    this.__preparedStmtOfClearTables = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM dining_tables";
        return _query;
      }
    };
    this.__preparedStmtOfClearProductTypes = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM product_types";
        return _query;
      }
    };
    this.__preparedStmtOfClearProducts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM products";
        return _query;
      }
    };
    this.__preparedStmtOfClearIngredients = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ingredients";
        return _query;
      }
    };
    this.__preparedStmtOfClearProductIngredients = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM product_ingredients";
        return _query;
      }
    };
    this.__preparedStmtOfClearPrinters = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM printer_stations";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTableStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE dining_tables SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOrderItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM order_items WHERE orderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTicketsForOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM kitchen_tickets WHERE orderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOrderMissingOnHost = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM orders WHERE id = ? AND (remoteId IS NULL OR remoteId = '')";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateOrderTotals = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET subtotal = ?, total = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkOrderSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET syncStatus = 'SYNCED', remoteId = ?, status = CASE WHEN status = 'PAID' THEN 'PAID' ELSE 'SYNCED' END WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkOrderSyncFailed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET syncStatus = 'FAILED' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSaveRemoteOrderId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET remoteId = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClaimTicketForPrinting = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE kitchen_tickets SET status = 'PRINTING' WHERE id = ? AND isPrinted = 0 AND status != 'PRINTING'";
        return _query;
      }
    };
    this.__preparedStmtOfMarkTicketPrinted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE kitchen_tickets SET isPrinted = 1, status = 'PRINTED', printedAt = ?, lastError = NULL WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkTicketFailed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE kitchen_tickets SET attempts = attempts + 1, status = 'FAILED', lastError = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object replaceTables(final List<DiningTableEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDiningTableEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceProductTypes(final List<ProductTypeEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductTypeEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceProducts(final List<ProductEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceIngredients(final List<IngredientEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfIngredientEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replacePrinters(final List<PrinterStationEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPrinterStationEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceProductIngredients(final List<ProductIngredientEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductIngredientEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertOrder(final OrderEntity order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrderEntity.insert(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertOrderItems(final List<OrderItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrderItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTicket(final KitchenTicketEntity ticket,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfKitchenTicketEntity.insert(ticket);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceCatalog(final List<DiningTableEntity> tables,
      final List<ProductTypeEntity> types, final List<ProductEntity> products,
      final List<IngredientEntity> ingredients,
      final List<ProductIngredientEntity> productIngredients,
      final List<PrinterStationEntity> printers, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> RestaurantDao_Impl.super.replaceCatalog(tables, types, products, ingredients, productIngredients, printers, __cont), $completion);
  }

  @Override
  public Object deleteLocalOnlyOrder(final String orderId,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> RestaurantDao_Impl.super.deleteLocalOnlyOrder(orderId, __cont), $completion);
  }

  @Override
  public Object saveOrderWithTicket(final OrderEntity order, final List<OrderItemEntity> items,
      final KitchenTicketEntity ticket, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> RestaurantDao_Impl.super.saveOrderWithTicket(order, items, ticket, __cont), $completion);
  }

  @Override
  public Object clearTables(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearTables.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearTables.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearProductTypes(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearProductTypes.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearProductTypes.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearProducts(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearProducts.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearProducts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearIngredients(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearIngredients.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearIngredients.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearProductIngredients(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearProductIngredients.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearProductIngredients.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearPrinters(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearPrinters.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearPrinters.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTableStatus(final String id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTableStatus.acquire();
        int _argIndex = 1;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, status);
        }
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateTableStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrderItems(final String orderId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOrderItems.acquire();
        int _argIndex = 1;
        if (orderId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, orderId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOrderItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTicketsForOrder(final String orderId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTicketsForOrder.acquire();
        int _argIndex = 1;
        if (orderId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, orderId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTicketsForOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrderMissingOnHost(final String orderId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOrderMissingOnHost.acquire();
        int _argIndex = 1;
        if (orderId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, orderId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOrderMissingOnHost.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderTotals(final String id, final double subtotal, final double total,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderTotals.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, subtotal);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, total);
        _argIndex = 3;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateOrderTotals.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markOrderSynced(final String localId, final String remoteId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkOrderSynced.acquire();
        int _argIndex = 1;
        if (remoteId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, remoteId);
        }
        _argIndex = 2;
        if (localId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, localId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkOrderSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markOrderSyncFailed(final String localId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkOrderSyncFailed.acquire();
        int _argIndex = 1;
        if (localId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, localId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkOrderSyncFailed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object saveRemoteOrderId(final String localId, final String remoteId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSaveRemoteOrderId.acquire();
        int _argIndex = 1;
        if (remoteId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, remoteId);
        }
        _argIndex = 2;
        if (localId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, localId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSaveRemoteOrderId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object claimTicketForPrinting(final String id,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClaimTicketForPrinting.acquire();
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClaimTicketForPrinting.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markTicketPrinted(final String id, final long printedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkTicketPrinted.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, printedAt);
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkTicketPrinted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markTicketFailed(final String id, final String error,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkTicketFailed.acquire();
        int _argIndex = 1;
        if (error == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, error);
        }
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkTicketFailed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getTables(final Continuation<? super List<DiningTableEntity>> $completion) {
    final String _sql = "SELECT * FROM dining_tables WHERE isActive = 1 ORDER BY code";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DiningTableEntity>>() {
      @Override
      @NonNull
      public List<DiningTableEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfCapacity = CursorUtil.getColumnIndexOrThrow(_cursor, "capacity");
          final int _cursorIndexOfZone = CursorUtil.getColumnIndexOrThrow(_cursor, "zone");
          final int _cursorIndexOfLayoutX = CursorUtil.getColumnIndexOrThrow(_cursor, "layoutX");
          final int _cursorIndexOfLayoutY = CursorUtil.getColumnIndexOrThrow(_cursor, "layoutY");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<DiningTableEntity> _result = new ArrayList<DiningTableEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DiningTableEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpCode;
            if (_cursor.isNull(_cursorIndexOfCode)) {
              _tmpCode = null;
            } else {
              _tmpCode = _cursor.getString(_cursorIndexOfCode);
            }
            final int _tmpCapacity;
            _tmpCapacity = _cursor.getInt(_cursorIndexOfCapacity);
            final String _tmpZone;
            if (_cursor.isNull(_cursorIndexOfZone)) {
              _tmpZone = null;
            } else {
              _tmpZone = _cursor.getString(_cursorIndexOfZone);
            }
            final Double _tmpLayoutX;
            if (_cursor.isNull(_cursorIndexOfLayoutX)) {
              _tmpLayoutX = null;
            } else {
              _tmpLayoutX = _cursor.getDouble(_cursorIndexOfLayoutX);
            }
            final Double _tmpLayoutY;
            if (_cursor.isNull(_cursorIndexOfLayoutY)) {
              _tmpLayoutY = null;
            } else {
              _tmpLayoutY = _cursor.getDouble(_cursorIndexOfLayoutY);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new DiningTableEntity(_tmpId,_tmpCode,_tmpCapacity,_tmpZone,_tmpLayoutX,_tmpLayoutY,_tmpStatus,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getProductTypes(final Continuation<? super List<ProductTypeEntity>> $completion) {
    final String _sql = "SELECT * FROM product_types WHERE isActive = 1 ORDER BY sortOrder, name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProductTypeEntity>>() {
      @Override
      @NonNull
      public List<ProductTypeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ProductTypeEntity> _result = new ArrayList<ProductTypeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductTypeEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ProductTypeEntity(_tmpId,_tmpName,_tmpDescription,_tmpSortOrder,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getProducts(final String typeId,
      final Continuation<? super List<ProductEntity>> $completion) {
    final String _sql = "SELECT * FROM products WHERE productTypeId = ? AND isActive = 1 ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (typeId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, typeId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductTypeId = CursorUtil.getColumnIndexOrThrow(_cursor, "productTypeId");
          final int _cursorIndexOfCompositionType = CursorUtil.getColumnIndexOrThrow(_cursor, "compositionType");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSku = CursorUtil.getColumnIndexOrThrow(_cursor, "sku");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpProductTypeId;
            if (_cursor.isNull(_cursorIndexOfProductTypeId)) {
              _tmpProductTypeId = null;
            } else {
              _tmpProductTypeId = _cursor.getString(_cursorIndexOfProductTypeId);
            }
            final String _tmpCompositionType;
            if (_cursor.isNull(_cursorIndexOfCompositionType)) {
              _tmpCompositionType = null;
            } else {
              _tmpCompositionType = _cursor.getString(_cursorIndexOfCompositionType);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpSku;
            if (_cursor.isNull(_cursorIndexOfSku)) {
              _tmpSku = null;
            } else {
              _tmpSku = _cursor.getString(_cursorIndexOfSku);
            }
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            final double _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getDouble(_cursorIndexOfUnitPrice);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ProductEntity(_tmpId,_tmpProductTypeId,_tmpCompositionType,_tmpName,_tmpDescription,_tmpSku,_tmpImagePath,_tmpUnitPrice,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getProduct(final String id, final Continuation<? super ProductEntity> $completion) {
    final String _sql = "SELECT * FROM products WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ProductEntity>() {
      @Override
      @Nullable
      public ProductEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductTypeId = CursorUtil.getColumnIndexOrThrow(_cursor, "productTypeId");
          final int _cursorIndexOfCompositionType = CursorUtil.getColumnIndexOrThrow(_cursor, "compositionType");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSku = CursorUtil.getColumnIndexOrThrow(_cursor, "sku");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final ProductEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpProductTypeId;
            if (_cursor.isNull(_cursorIndexOfProductTypeId)) {
              _tmpProductTypeId = null;
            } else {
              _tmpProductTypeId = _cursor.getString(_cursorIndexOfProductTypeId);
            }
            final String _tmpCompositionType;
            if (_cursor.isNull(_cursorIndexOfCompositionType)) {
              _tmpCompositionType = null;
            } else {
              _tmpCompositionType = _cursor.getString(_cursorIndexOfCompositionType);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpSku;
            if (_cursor.isNull(_cursorIndexOfSku)) {
              _tmpSku = null;
            } else {
              _tmpSku = _cursor.getString(_cursorIndexOfSku);
            }
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            final double _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getDouble(_cursorIndexOfUnitPrice);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new ProductEntity(_tmpId,_tmpProductTypeId,_tmpCompositionType,_tmpName,_tmpDescription,_tmpSku,_tmpImagePath,_tmpUnitPrice,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getIngredientsForProduct(final String productId,
      final Continuation<? super List<IngredientEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT ingredients.id, ingredients.categoryId, ingredients.name, ingredients.unit,\n"
            + "               ingredients.unitCost, ingredients.stockQuantity, ingredients.reorderLevel, ingredients.isActive\n"
            + "        FROM ingredients\n"
            + "        INNER JOIN product_ingredients ON product_ingredients.ingredientId = ingredients.id\n"
            + "        WHERE product_ingredients.productId = ? AND ingredients.isActive = 1\n"
            + "        ORDER BY ingredients.name\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (productId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, productId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<IngredientEntity>>() {
      @Override
      @NonNull
      public List<IngredientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfCategoryId = 1;
          final int _cursorIndexOfName = 2;
          final int _cursorIndexOfUnit = 3;
          final int _cursorIndexOfUnitCost = 4;
          final int _cursorIndexOfStockQuantity = 5;
          final int _cursorIndexOfReorderLevel = 6;
          final int _cursorIndexOfIsActive = 7;
          final List<IngredientEntity> _result = new ArrayList<IngredientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IngredientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpUnitCost;
            if (_cursor.isNull(_cursorIndexOfUnitCost)) {
              _tmpUnitCost = null;
            } else {
              _tmpUnitCost = _cursor.getDouble(_cursorIndexOfUnitCost);
            }
            final Double _tmpStockQuantity;
            if (_cursor.isNull(_cursorIndexOfStockQuantity)) {
              _tmpStockQuantity = null;
            } else {
              _tmpStockQuantity = _cursor.getDouble(_cursorIndexOfStockQuantity);
            }
            final Double _tmpReorderLevel;
            if (_cursor.isNull(_cursorIndexOfReorderLevel)) {
              _tmpReorderLevel = null;
            } else {
              _tmpReorderLevel = _cursor.getDouble(_cursorIndexOfReorderLevel);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new IngredientEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpUnit,_tmpUnitCost,_tmpStockQuantity,_tmpReorderLevel,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getIngredients(final Continuation<? super List<IngredientEntity>> $completion) {
    final String _sql = "SELECT * FROM ingredients WHERE isActive = 1 ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<IngredientEntity>>() {
      @Override
      @NonNull
      public List<IngredientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfUnitCost = CursorUtil.getColumnIndexOrThrow(_cursor, "unitCost");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfReorderLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "reorderLevel");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<IngredientEntity> _result = new ArrayList<IngredientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IngredientEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final Double _tmpUnitCost;
            if (_cursor.isNull(_cursorIndexOfUnitCost)) {
              _tmpUnitCost = null;
            } else {
              _tmpUnitCost = _cursor.getDouble(_cursorIndexOfUnitCost);
            }
            final Double _tmpStockQuantity;
            if (_cursor.isNull(_cursorIndexOfStockQuantity)) {
              _tmpStockQuantity = null;
            } else {
              _tmpStockQuantity = _cursor.getDouble(_cursorIndexOfStockQuantity);
            }
            final Double _tmpReorderLevel;
            if (_cursor.isNull(_cursorIndexOfReorderLevel)) {
              _tmpReorderLevel = null;
            } else {
              _tmpReorderLevel = _cursor.getDouble(_cursorIndexOfReorderLevel);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new IngredientEntity(_tmpId,_tmpCategoryId,_tmpName,_tmpUnit,_tmpUnitCost,_tmpStockQuantity,_tmpReorderLevel,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPrinters(final Continuation<? super List<PrinterStationEntity>> $completion) {
    final String _sql = "SELECT * FROM printer_stations WHERE isActive = 1 ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PrinterStationEntity>>() {
      @Override
      @NonNull
      public List<PrinterStationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfBluetoothMac = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothMac");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<PrinterStationEntity> _result = new ArrayList<PrinterStationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PrinterStationEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCode;
            if (_cursor.isNull(_cursorIndexOfCode)) {
              _tmpCode = null;
            } else {
              _tmpCode = _cursor.getString(_cursorIndexOfCode);
            }
            final String _tmpBluetoothMac;
            if (_cursor.isNull(_cursorIndexOfBluetoothMac)) {
              _tmpBluetoothMac = null;
            } else {
              _tmpBluetoothMac = _cursor.getString(_cursorIndexOfBluetoothMac);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new PrinterStationEntity(_tmpId,_tmpName,_tmpCode,_tmpBluetoothMac,_tmpIsActive,_tmpSortOrder);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTicketForOrder(final String orderId,
      final Continuation<? super KitchenTicketEntity> $completion) {
    final String _sql = "SELECT * FROM kitchen_tickets WHERE orderId = ? ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (orderId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, orderId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<KitchenTicketEntity>() {
      @Override
      @Nullable
      public KitchenTicketEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfPrinterStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "printerStationId");
          final int _cursorIndexOfPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "payload");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsPrinted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrinted");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfPrintedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "printedAt");
          final KitchenTicketEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpOrderId;
            if (_cursor.isNull(_cursorIndexOfOrderId)) {
              _tmpOrderId = null;
            } else {
              _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            }
            final String _tmpPrinterStationId;
            if (_cursor.isNull(_cursorIndexOfPrinterStationId)) {
              _tmpPrinterStationId = null;
            } else {
              _tmpPrinterStationId = _cursor.getString(_cursorIndexOfPrinterStationId);
            }
            final String _tmpPayload;
            if (_cursor.isNull(_cursorIndexOfPayload)) {
              _tmpPayload = null;
            } else {
              _tmpPayload = _cursor.getString(_cursorIndexOfPayload);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final boolean _tmpIsPrinted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrinted);
            _tmpIsPrinted = _tmp != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpPrintedAt;
            if (_cursor.isNull(_cursorIndexOfPrintedAt)) {
              _tmpPrintedAt = null;
            } else {
              _tmpPrintedAt = _cursor.getLong(_cursorIndexOfPrintedAt);
            }
            _result = new KitchenTicketEntity(_tmpId,_tmpOrderId,_tmpPrinterStationId,_tmpPayload,_tmpStatus,_tmpIsPrinted,_tmpAttempts,_tmpLastError,_tmpCreatedAt,_tmpPrintedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOrders(final Continuation<? super List<OrderEntity>> $completion) {
    final String _sql = "SELECT * FROM orders ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getUnpaidOrders(final Continuation<? super List<OrderEntity>> $completion) {
    final String _sql = "SELECT * FROM orders WHERE status != 'PAID' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOrder(final String id, final Continuation<? super OrderEntity> $completion) {
    final String _sql = "SELECT * FROM orders WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _result = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOrdersPendingSync(final Continuation<? super List<OrderEntity>> $completion) {
    final String _sql = "SELECT * FROM orders WHERE syncStatus != 'SYNCED' ORDER BY createdAt";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRemoteOrderIdForTable(final String tableId,
      final Continuation<? super String> $completion) {
    final String _sql = "SELECT remoteId FROM orders WHERE diningTableId = ? AND remoteId IS NOT NULL AND status != 'PAID' ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tableId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tableId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOpenLocalOrderForTable(final String tableId,
      final Continuation<? super OrderEntity> $completion) {
    final String _sql = "SELECT * FROM orders WHERE diningTableId = ? AND status != 'PAID' AND (remoteId IS NULL OR remoteId = '') ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tableId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tableId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _result = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOrderByRemoteId(final String remoteId,
      final Continuation<? super OrderEntity> $completion) {
    final String _sql = "SELECT * FROM orders WHERE remoteId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (remoteId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, remoteId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _result = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOrdersMissingOnHost(final Continuation<? super List<OrderEntity>> $completion) {
    final String _sql = "SELECT * FROM orders WHERE remoteId IS NULL OR remoteId = '' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiningTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableId");
          final int _cursorIndexOfDiningTableCode = CursorUtil.getColumnIndexOrThrow(_cursor, "diningTableCode");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfWaiterName = CursorUtil.getColumnIndexOrThrow(_cursor, "waiterName");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "openedAtUtc");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfClosedAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAtUtc");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpDiningTableId;
            if (_cursor.isNull(_cursorIndexOfDiningTableId)) {
              _tmpDiningTableId = null;
            } else {
              _tmpDiningTableId = _cursor.getString(_cursorIndexOfDiningTableId);
            }
            final String _tmpDiningTableCode;
            if (_cursor.isNull(_cursorIndexOfDiningTableCode)) {
              _tmpDiningTableCode = null;
            } else {
              _tmpDiningTableCode = _cursor.getString(_cursorIndexOfDiningTableCode);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
            }
            final String _tmpWaiterName;
            if (_cursor.isNull(_cursorIndexOfWaiterName)) {
              _tmpWaiterName = null;
            } else {
              _tmpWaiterName = _cursor.getString(_cursorIndexOfWaiterName);
            }
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpOpenedAtUtc;
            _tmpOpenedAtUtc = _cursor.getLong(_cursorIndexOfOpenedAtUtc);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getDouble(_cursorIndexOfTaxAmount);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpClosedAtUtc;
            if (_cursor.isNull(_cursorIndexOfClosedAtUtc)) {
              _tmpClosedAtUtc = null;
            } else {
              _tmpClosedAtUtc = _cursor.getLong(_cursorIndexOfClosedAtUtc);
            }
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            _item = new OrderEntity(_tmpId,_tmpDiningTableId,_tmpDiningTableCode,_tmpNumber,_tmpCustomerId,_tmpWaiterName,_tmpDeviceId,_tmpStatus,_tmpOpenedAtUtc,_tmpSubtotal,_tmpTaxAmount,_tmpTotal,_tmpCreatedAt,_tmpRemoteId,_tmpClosedAtUtc,_tmpSyncStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getOrderItems(final String orderId,
      final Continuation<? super List<OrderItemEntity>> $completion) {
    final String _sql = "SELECT * FROM order_items WHERE orderId = ? ORDER BY id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (orderId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, orderId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderItemEntity>>() {
      @Override
      @NonNull
      public List<OrderItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfLineTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "lineTotal");
          final int _cursorIndexOfUnitCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitCostPrice");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSentToKitchenAtUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "sentToKitchenAtUtc");
          final List<OrderItemEntity> _result = new ArrayList<OrderItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderItemEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpOrderId;
            if (_cursor.isNull(_cursorIndexOfOrderId)) {
              _tmpOrderId = null;
            } else {
              _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            }
            final String _tmpProductId;
            if (_cursor.isNull(_cursorIndexOfProductId)) {
              _tmpProductId = null;
            } else {
              _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            }
            final String _tmpProductName;
            if (_cursor.isNull(_cursorIndexOfProductName)) {
              _tmpProductName = null;
            } else {
              _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            }
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final double _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getDouble(_cursorIndexOfUnitPrice);
            final double _tmpLineTotal;
            _tmpLineTotal = _cursor.getDouble(_cursorIndexOfLineTotal);
            final Double _tmpUnitCostPrice;
            if (_cursor.isNull(_cursorIndexOfUnitCostPrice)) {
              _tmpUnitCostPrice = null;
            } else {
              _tmpUnitCostPrice = _cursor.getDouble(_cursorIndexOfUnitCostPrice);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpSentToKitchenAtUtc;
            if (_cursor.isNull(_cursorIndexOfSentToKitchenAtUtc)) {
              _tmpSentToKitchenAtUtc = null;
            } else {
              _tmpSentToKitchenAtUtc = _cursor.getLong(_cursorIndexOfSentToKitchenAtUtc);
            }
            _item = new OrderItemEntity(_tmpId,_tmpOrderId,_tmpProductId,_tmpProductName,_tmpQuantity,_tmpUnitPrice,_tmpLineTotal,_tmpUnitCostPrice,_tmpNotes,_tmpSentToKitchenAtUtc);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingTickets(
      final Continuation<? super List<KitchenTicketEntity>> $completion) {
    final String _sql = "SELECT * FROM kitchen_tickets WHERE isPrinted = 0 ORDER BY createdAt";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<KitchenTicketEntity>>() {
      @Override
      @NonNull
      public List<KitchenTicketEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfPrinterStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "printerStationId");
          final int _cursorIndexOfPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "payload");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsPrinted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrinted");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final int _cursorIndexOfLastError = CursorUtil.getColumnIndexOrThrow(_cursor, "lastError");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfPrintedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "printedAt");
          final List<KitchenTicketEntity> _result = new ArrayList<KitchenTicketEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final KitchenTicketEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpOrderId;
            if (_cursor.isNull(_cursorIndexOfOrderId)) {
              _tmpOrderId = null;
            } else {
              _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            }
            final String _tmpPrinterStationId;
            if (_cursor.isNull(_cursorIndexOfPrinterStationId)) {
              _tmpPrinterStationId = null;
            } else {
              _tmpPrinterStationId = _cursor.getString(_cursorIndexOfPrinterStationId);
            }
            final String _tmpPayload;
            if (_cursor.isNull(_cursorIndexOfPayload)) {
              _tmpPayload = null;
            } else {
              _tmpPayload = _cursor.getString(_cursorIndexOfPayload);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final boolean _tmpIsPrinted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrinted);
            _tmpIsPrinted = _tmp != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            final String _tmpLastError;
            if (_cursor.isNull(_cursorIndexOfLastError)) {
              _tmpLastError = null;
            } else {
              _tmpLastError = _cursor.getString(_cursorIndexOfLastError);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpPrintedAt;
            if (_cursor.isNull(_cursorIndexOfPrintedAt)) {
              _tmpPrintedAt = null;
            } else {
              _tmpPrintedAt = _cursor.getLong(_cursorIndexOfPrintedAt);
            }
            _item = new KitchenTicketEntity(_tmpId,_tmpOrderId,_tmpPrinterStationId,_tmpPayload,_tmpStatus,_tmpIsPrinted,_tmpAttempts,_tmpLastError,_tmpCreatedAt,_tmpPrintedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPrinter(final String id,
      final Continuation<? super PrinterStationEntity> $completion) {
    final String _sql = "SELECT * FROM printer_stations WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PrinterStationEntity>() {
      @Override
      @Nullable
      public PrinterStationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfBluetoothMac = CursorUtil.getColumnIndexOrThrow(_cursor, "bluetoothMac");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final PrinterStationEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCode;
            if (_cursor.isNull(_cursorIndexOfCode)) {
              _tmpCode = null;
            } else {
              _tmpCode = _cursor.getString(_cursorIndexOfCode);
            }
            final String _tmpBluetoothMac;
            if (_cursor.isNull(_cursorIndexOfBluetoothMac)) {
              _tmpBluetoothMac = null;
            } else {
              _tmpBluetoothMac = _cursor.getString(_cursorIndexOfBluetoothMac);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _result = new PrinterStationEntity(_tmpId,_tmpName,_tmpCode,_tmpBluetoothMac,_tmpIsActive,_tmpSortOrder);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object markOrdersPaid(final List<String> ids, final long closedAt, final String syncStatus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE orders SET status = 'PAID', closedAtUtc = ");
        _stringBuilder.append("?");
        _stringBuilder.append(", syncStatus = ");
        _stringBuilder.append("?");
        _stringBuilder.append(" WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, closedAt);
        _argIndex = 2;
        if (syncStatus == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, syncStatus);
        }
        _argIndex = 3;
        for (String _item : ids) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindString(_argIndex, _item);
          }
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
