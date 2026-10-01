package com.host.printgateway.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dining_tables")
data class DiningTableEntity(
    @PrimaryKey val id: String,
    val code: String,
    val capacity: Int,
    val zone: String?,
    val layoutX: Double?,
    val layoutY: Double?,
    val status: String,
    val isActive: Boolean,
) {
    fun isOccupied(): Boolean =
        status == "1" || status.equals("Busy", ignoreCase = true)
}

@Entity(tableName = "product_types")
data class ProductTypeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val sortOrder: Int,
    val isActive: Boolean,
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val productTypeId: String,
    val compositionType: String,
    val name: String,
    val description: String?,
    val sku: String?,
    val imagePath: String?,
    val unitPrice: Double,
    val isActive: Boolean,
)

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val name: String,
    val unit: String,
    val unitCost: Double?,
    val stockQuantity: Double?,
    val reorderLevel: Double?,
    val isActive: Boolean,
)

@Entity(tableName = "ingredient_categories")
data class IngredientCategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val sortOrder: Int,
    val isActive: Boolean,
)

@Entity(tableName = "product_ingredients")
data class ProductIngredientEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val ingredientId: String,
    val quantity: Double,
)

@Entity(tableName = "product_bundle_lines")
data class ProductBundleLineEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val componentProductId: String,
    val quantity: Double,
    val sortOrder: Int,
)

@Entity(tableName = "printer_stations")
data class PrinterStationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val bluetoothMac: String,
    val isActive: Boolean,
    val sortOrder: Int,
)

@Entity(tableName = "product_type_printer_mappings")
data class ProductTypePrinterMappingEntity(
    @PrimaryKey val id: String,
    val productTypeId: String,
    val printerStationId: String,
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val diningTableId: String?,
    val diningTableCode: String,
    val number: String,
    val customerId: String?,
    val waiterName: String,
    val deviceId: String,
    val status: String,
    val openedAtUtc: Long,
    val subtotal: Double,
    val taxAmount: Double,
    val total: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val remoteId: String? = null,
    val closedAtUtc: Long? = null,
    val syncStatus: String = RestaurantStatuses.SYNC_STATE_PENDING,
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val productId: String,
    val productName: String,
    val quantity: Double,
    val unitPrice: Double,
    val lineTotal: Double,
    val unitCostPrice: Double?,
    val notes: String,
    val sentToKitchenAtUtc: Long?,
)

@Entity(tableName = "kitchen_tickets")
data class KitchenTicketEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val printerStationId: String?,
    val payload: String,
    val status: String = RestaurantStatuses.TICKET_PENDING,
    val isPrinted: Boolean = false,
    val attempts: Int = 0,
    val lastError: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val printedAt: Long? = null,
)

object RestaurantStatuses {
    const val ORDER_CONFIRMED = "CONFIRMED"
    const val ORDER_SYNC_PENDING = "SYNC_PENDING"
    const val ORDER_SYNCED = "SYNCED"
    const val ORDER_PAID = "PAID"
    const val SYNC_STATE_PENDING = "PENDING"
    const val SYNC_STATE_SYNCED = "SYNCED"
    const val SYNC_STATE_FAILED = "FAILED"
    const val TICKET_PENDING = "PENDING"
    const val TICKET_PRINTING = "PRINTING"
    const val TICKET_PRINTED = "PRINTED"
    const val TICKET_FAILED = "FAILED"
}