package com.host.printgateway.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lcom/host/printgateway/data/PrintGatewayDatabase;", "Landroidx/room/RoomDatabase;", "()V", "printJobDao", "Lcom/host/printgateway/data/PrintJobDao;", "restaurantDao", "Lcom/host/printgateway/data/RestaurantDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.host.printgateway.data.PrintJobEntity.class, com.host.printgateway.data.DiningTableEntity.class, com.host.printgateway.data.ProductTypeEntity.class, com.host.printgateway.data.ProductEntity.class, com.host.printgateway.data.IngredientEntity.class, com.host.printgateway.data.IngredientCategoryEntity.class, com.host.printgateway.data.ProductIngredientEntity.class, com.host.printgateway.data.ProductBundleLineEntity.class, com.host.printgateway.data.PrinterStationEntity.class, com.host.printgateway.data.ProductTypePrinterMappingEntity.class, com.host.printgateway.data.OrderEntity.class, com.host.printgateway.data.OrderItemEntity.class, com.host.printgateway.data.KitchenTicketEntity.class}, version = 4, exportSchema = false)
public abstract class PrintGatewayDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.host.printgateway.data.PrintGatewayDatabase instance;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_2_3 = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_3_4 = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.PrintGatewayDatabase.Companion Companion = null;
    
    public PrintGatewayDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.host.printgateway.data.PrintJobDao printJobDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.host.printgateway.data.RestaurantDao restaurantDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/host/printgateway/data/PrintGatewayDatabase$Companion;", "", "()V", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "MIGRATION_2_3", "MIGRATION_3_4", "instance", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "get", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.host.printgateway.data.PrintGatewayDatabase get(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}