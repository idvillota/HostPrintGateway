package com.host.printgateway.ui.navigation

sealed interface AppRoute {
    data object Login : AppRoute
    data object Settings : AppRoute
    data object Home : AppRoute
    data class Ordering(val step: OrderingStep) : AppRoute
    data class Paying(val step: PaymentStep) : AppRoute
}

sealed interface PaymentStep {
    data object Accounts : PaymentStep
    data class Bill(val tableKey: String) : PaymentStep
}

sealed interface OrderingStep {
    data object Tables : OrderingStep

    data class TableMenu(val tableId: String) : OrderingStep

    data class OrderedProducts(val tableId: String) : OrderingStep

    data class ProductTypes(val tableId: String) : OrderingStep

    data class Products(val tableId: String, val typeId: String) : OrderingStep

    data class ProductDetail(
        val tableId: String,
        val typeId: String,
        val productId: String,
    ) : OrderingStep

    data class Comanda(
        val tableId: String,
        val returnTo: OrderingStep,
    ) : OrderingStep
}
