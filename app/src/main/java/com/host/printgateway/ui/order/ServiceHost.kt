package com.host.printgateway.ui.order

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.OrderDraftLine
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.ProductEntity
import com.host.printgateway.data.ProductTypeEntity
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.network.SyncUnauthorized
import com.host.printgateway.ui.SessionRenewalNotifier
import com.host.printgateway.ui.components.AppScaffold
import com.host.printgateway.ui.navigation.OrderingStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ServiceHost(
    step: OrderingStep,
    apiUrl: String,
    deviceToken: String,
    printerMac: String,
    database: PrintGatewayDatabase,
    session: OrderSession,
    onStep: (OrderingStep) -> Unit,
    onOpenSettings: () -> Unit,
    onLeave: () -> Unit,
    catalogRevision: Int,
    onLocalOrderSaved: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember(database) { database.restaurantDao() }
    val repository = remember(database, deviceToken) { RestaurantRepository(database, deviceToken) }
    val submitComanda = remember(repository, dao) { SubmitComanda(repository, dao) }

    var tables by remember { mutableStateOf(emptyList<DiningTableEntity>()) }
    var types by remember { mutableStateOf(emptyList<ProductTypeEntity>()) }
    var products by remember { mutableStateOf(emptyList<ProductEntity>()) }
    var syncing by remember { mutableStateOf(false) }

    val detailProductId = (step as? OrderingStep.ProductDetail)?.productId
    var draftNotes by remember(detailProductId) { mutableStateOf("") }

    LaunchedEffect(catalogRevision) {
        tables = dao.getTables()
        types = dao.getProductTypes()
    }

    val activeTypeId = when (step) {
        is OrderingStep.Products -> step.typeId
        is OrderingStep.ProductDetail -> step.typeId
        else -> null
    }
    LaunchedEffect(activeTypeId, catalogRevision) {
        if (activeTypeId != null) {
            products = dao.getProducts(activeTypeId)
        }
    }

    fun open(next: OrderingStep, notice: String = "") {
        session.notice = notice
        onStep(next)
    }

    val title = when (step) {
        OrderingStep.Tables -> "Mesas"
        is OrderingStep.ProductTypes -> "Tipos"
        is OrderingStep.Products -> "Productos"
        is OrderingStep.ProductDetail -> "Producto"
        is OrderingStep.Comanda -> "Comanda"
    }
    val backTarget = when (step) {
        OrderingStep.Tables -> null
        is OrderingStep.ProductTypes -> OrderingStep.Tables
        is OrderingStep.Products -> OrderingStep.ProductTypes(step.tableId)
        is OrderingStep.ProductDetail -> OrderingStep.Products(step.tableId, step.typeId)
        is OrderingStep.Comanda -> step.returnTo
    }

    AppScaffold(
        title = title,
        onBack = if (step == OrderingStep.Tables) {
            onLeave
        } else {
            backTarget?.let { target -> { open(target) } }
        },
        onSettings = if (step == OrderingStep.Tables) onOpenSettings else null,
    ) { padding ->
        when (step) {
            OrderingStep.Tables -> TablesScreen(
                padding = padding,
                tables = tables,
                syncing = syncing,
                notice = session.notice,
                cartCount = session.itemCount,
                onSync = {
                    scope.launch {
                        syncing = true
                        session.notice = ""
                        val deviceId = Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID,
                        ) ?: "unknown-device"
                        val outcome = withContext(Dispatchers.IO) {
                            val catalog = repository.syncCatalog(apiUrl)
                            val orders = repository.syncPendingOrders(apiUrl, deviceId)
                            if (catalog.exceptionOrNull() is SyncUnauthorized ||
                                orders.exceptionOrNull() is SyncUnauthorized
                            ) {
                                SessionRenewalNotifier.show(context)
                            }
                            if (catalog.isFailure) {
                                SyncSnapshot(
                                    message = catalog.exceptionOrNull()?.message ?: "No se pudo sincronizar",
                                )
                            } else {
                                val loadedTables = dao.getTables()
                                val loadedTypes = dao.getProductTypes()
                                SyncSnapshot(
                                    tables = loadedTables,
                                    types = loadedTypes,
                                    message = if (orders.isSuccess) {
                                        "Mesas listas: ${loadedTables.size}"
                                    } else {
                                        "Catálogo listo. ${orders.exceptionOrNull()?.message ?: "No se sincronizaron pedidos."}"
                                    },
                                )
                            }
                        }
                        outcome.tables?.let { tables = it }
                        outcome.types?.let { types = it }
                        session.notice = outcome.message
                        syncing = false
                    }
                },
                onTable = { table ->
                    if (session.tableId != table.id) {
                        session.clearCart()
                    }
                    session.tableId = table.id
                    open(OrderingStep.ProductTypes(table.id))
                },
                onOpenComanda = {
                    val tableId = session.tableId ?: return@TablesScreen
                    open(OrderingStep.Comanda(tableId, OrderingStep.Tables))
                },
            )

            is OrderingStep.ProductTypes -> ProductTypesScreen(
                padding = padding,
                tableCode = tables.codeOf(step.tableId),
                types = types,
                notice = session.notice,
                cartCount = session.itemCount,
                onType = { type ->
                    open(OrderingStep.Products(step.tableId, type.id))
                },
                onOpenComanda = {
                    open(OrderingStep.Comanda(step.tableId, step))
                },
            )

            is OrderingStep.Products -> ProductsScreen(
                padding = padding,
                tableCode = tables.codeOf(step.tableId),
                typeName = types.nameOf(step.typeId),
                products = products,
                notice = session.notice,
                cartCount = session.itemCount,
                onProduct = { product ->
                    open(OrderingStep.ProductDetail(step.tableId, step.typeId, product.id))
                },
                onOpenComanda = {
                    open(OrderingStep.Comanda(step.tableId, step))
                },
            )

            is OrderingStep.ProductDetail -> ProductDetailScreen(
                padding = padding,
                product = products.firstOrNull { it.id == step.productId },
                notes = draftNotes,
                onNotesChange = { draftNotes = it },
                onAdd = {
                    val product = products.firstOrNull { it.id == step.productId } ?: return@ProductDetailScreen
                    session.addProduct(product, draftNotes)
                    open(
                        OrderingStep.Products(step.tableId, step.typeId),
                        notice = "Agregado: ${product.name}",
                    )
                },
            )

            is OrderingStep.Comanda -> {
                val table = tables.firstOrNull { it.id == step.tableId }
                OrderReviewScreen(
                    padding = padding,
                    tableCode = table?.code ?: tables.codeOf(step.tableId),
                    lines = session.cart,
                    waiter = session.waiter,
                    sending = session.sending,
                    buttonLabel = if (session.unprintedOrderId != null) {
                        "Reintentar impresión"
                    } else {
                        "Enviar comanda"
                    },
                    canSend = table != null &&
                        session.waiter.isNotBlank() &&
                        (session.cart.isNotEmpty() || session.unprintedOrderId != null) &&
                        !session.sending,
                    notice = session.notice,
                    onWaiterChange = { session.waiter = it },
                    onChangeQuantity = session::changeQuantity,
                    onSend = {
                        val currentTable = table ?: return@OrderReviewScreen
                        session.sending = true
                        session.notice = "Enviando comanda…"
                        scope.launch {
                            val deviceId = Settings.Secure.getString(
                                context.contentResolver,
                                Settings.Secure.ANDROID_ID,
                            ) ?: "unknown-device"
                            val lines = session.cart.map { line ->
                                OrderDraftLine(
                                    productId = line.product.id,
                                    productName = line.product.name,
                                    quantity = line.quantity.toDouble(),
                                    unitPrice = line.product.unitPrice,
                                    notes = line.notes,
                                )
                            }
                            val outcome = withContext(Dispatchers.IO) {
                                submitComanda.send(
                                    existingOrderId = session.unprintedOrderId,
                                    table = currentTable,
                                    waiterName = session.waiter.trim(),
                                    deviceId = deviceId,
                                    lines = lines,
                                    printerMac = printerMac,
                                )
                            }
                            session.sending = false
                            when (outcome) {
                                is SubmitOutcome.Printed -> {
                                    session.clearCart()
                                    onLocalOrderSaved()
                                    open(
                                        OrderingStep.Tables,
                                        notice = "Comanda enviada. Orden ${outcome.orderId.take(8)}",
                                    )
                                }
                                is SubmitOutcome.NeedsRetry -> {
                                    session.unprintedOrderId = outcome.orderId
                                    onLocalOrderSaved()
                                    session.notice = "Orden ${outcome.orderId.take(8)} guardada. ${outcome.reason}"
                                }
                                is SubmitOutcome.Failed -> {
                                    session.notice = outcome.message
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

private data class SyncSnapshot(
    val tables: List<DiningTableEntity>? = null,
    val types: List<ProductTypeEntity>? = null,
    val message: String,
)

private fun List<DiningTableEntity>.codeOf(id: String): String =
    firstOrNull { it.id == id }?.code ?: "—"

private fun List<ProductTypeEntity>.nameOf(id: String): String =
    firstOrNull { it.id == id }?.name ?: "Productos"
