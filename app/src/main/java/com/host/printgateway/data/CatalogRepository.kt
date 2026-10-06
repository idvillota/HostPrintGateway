package com.host.printgateway.data

import com.host.printgateway.network.CatalogApi

class CatalogRepository(
    private val database: PrintGatewayDatabase,
    private val deviceToken: String,
) {
    private val dao = database.restaurantDao()

    suspend fun syncCatalog(baseUrl: String): Result<Int> = runCatching {
        val api = CatalogApi(baseUrl, deviceToken)

        val tables = api.fetchTables().getOrThrow()
        val types = api.fetchProductTypes().getOrThrow()
        val productPage = api.fetchProducts().getOrThrow()
        val products = productPage.products
        val ingredients = api.fetchIngredients().getOrThrow()
        val printers = api.fetchPrinters().getOrThrow()

        dao.replaceCatalog(
            tables.map {
                DiningTableEntity(
                    it.id,
                    it.code,
                    it.capacity,
                    it.zone,
                    null,
                    null,
                    it.status,
                    it.isActive,
                )
            },
            types.map {
                ProductTypeEntity(
                    it.id,
                    it.name,
                    it.description,
                    it.sortOrder,
                    it.isActive,
                )
            },
            products.map {
                ProductEntity(
                    it.id,
                    it.productTypeId,
                    it.compositionType,
                    it.name,
                    it.description,
                    it.sku,
                    it.imagePath,
                    it.unitPrice,
                    it.isActive,
                )
            },
            ingredients.map {
                IngredientEntity(
                    it.id,
                    it.categoryId,
                    it.name,
                    it.unit,
                    it.unitCost,
                    it.stockQuantity,
                    it.reorderLevel,
                    it.isActive,
                )
            },
            productPage.productIngredients.map {
                ProductIngredientEntity(
                    it.id,
                    it.productId,
                    it.ingredientId,
                    it.quantity,
                )
            },
            printers.map {
                PrinterStationEntity(
                    it.id,
                    it.name,
                    it.code,
                    "",
                    it.isActive,
                    it.sortOrder,
                )
            },
        )

        tables.size +
            types.size +
            products.size +
            ingredients.size +
            printers.size
    }
}
