package com.host.printgateway.ui

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.IngredientEntity
import com.host.printgateway.data.OrderDraftLine
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.ProductEntity
import com.host.printgateway.data.ProductTypeEntity
import com.host.printgateway.data.RestaurantRepository
import com.host.printgateway.printer.BluetoothEscPosPrinter
import com.host.printgateway.printer.KitchenTicketFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class OrderScreen {
    Tables,
    ProductTypes,
    Products,
    ProductOptions,
    Order,
}

private data class CartLine(
    val key: String,
    val product: ProductEntity,
    val quantity: Int,
    val notes: String,
    val excludedIngredients: List<String>,
)

@Composable
fun LiteOrderPanel(
    apiUrl: String,
    deviceToken: String,
    database: PrintGatewayDatabase,
    printerMac: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember(database) { database.restaurantDao() }
    val repository = remember(database, deviceToken) {
        RestaurantRepository(database, deviceToken)
    }
    val cart = remember { mutableStateListOf<CartLine>() }

    var screen by remember { mutableStateOf(OrderScreen.Tables) }
    var tables by remember { mutableStateOf(emptyList<DiningTableEntity>()) }
    var productTypes by remember { mutableStateOf(emptyList<ProductTypeEntity>()) }
    var products by remember { mutableStateOf(emptyList<ProductEntity>()) }
    var selectedTable by remember { mutableStateOf<DiningTableEntity?>(null) }
    var selectedType by remember { mutableStateOf<ProductTypeEntity?>(null) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var optionIngredients by remember { mutableStateOf(emptyList<IngredientEntity>()) }
    var includedIngredientIds by remember { mutableStateOf(setOf<String>()) }
    var usingFullIngredientList by remember { mutableStateOf(false) }
    var optionNotes by remember { mutableStateOf("") }
    var waiter by remember { mutableStateOf("Mesero") }
    var message by remember { mutableStateOf("Catálogo local pendiente de sincronizar") }
    var sending by remember { mutableStateOf(false) }
    var unprintedOrderId by remember { mutableStateOf<String?>(null) }

    suspend fun loadLocal() {
        tables = dao.getTables()
        productTypes = dao.getProductTypes()
        selectedTable = selectedTable?.let { current ->
            tables.firstOrNull { it.id == current.id }
        }
        selectedType = selectedType?.let { current ->
            productTypes.firstOrNull { it.id == current.id }
        }
        products = selectedType?.let { dao.getProducts(it.id) }.orEmpty()
    }

    LaunchedEffect(Unit) {
        loadLocal()
    }

    LaunchedEffect(selectedType?.id) {
        products = selectedType?.let { dao.getProducts(it.id) }.orEmpty()
    }

    LaunchedEffect(editingProduct?.id) {
        val product = editingProduct ?: return@LaunchedEffect
        val linked = withContext(Dispatchers.IO) {
            dao.getIngredientsForProduct(product.id)
        }
        val ingredients = if (linked.isNotEmpty()) {
            usingFullIngredientList = false
            linked
        } else {
            usingFullIngredientList = true
            withContext(Dispatchers.IO) { dao.getIngredients() }
        }
        optionIngredients = ingredients
        includedIngredientIds = ingredients.map { it.id }.toSet()
        optionNotes = ""
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Operación Lite", style = MaterialTheme.typography.titleLarge)
        Text(
            text = when (screen) {
                OrderScreen.Tables -> "Mesas"
                OrderScreen.ProductTypes -> "Mesa ${selectedTable?.code ?: "-"} · Tipos"
                OrderScreen.Products -> "Mesa ${selectedTable?.code ?: "-"} · ${selectedType?.name ?: "Productos"}"
                OrderScreen.ProductOptions -> editingProduct?.name ?: "Producto"
                OrderScreen.Order -> "Mesa ${selectedTable?.code ?: "-"} · Orden"
            },
            style = MaterialTheme.typography.titleMedium,
        )

        if (screen != OrderScreen.Tables) {
            OutlinedButton(
                onClick = {
                    screen = when (screen) {
                        OrderScreen.ProductTypes -> OrderScreen.Tables
                        OrderScreen.Products -> OrderScreen.ProductTypes
                        OrderScreen.ProductOptions -> OrderScreen.Products
                        OrderScreen.Order -> OrderScreen.Products
                        OrderScreen.Tables -> OrderScreen.Tables
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Volver")
            }
        }

        when (screen) {
            OrderScreen.Tables -> TablesScreen(
                tables = tables,
                onSync = {
                    scope.launch {
                        message = "Sincronizando con HOST..."
                        val result = withContext(Dispatchers.IO) {
                            val catalogResult = repository.syncCatalog(apiUrl)
                            if (catalogResult.isFailure) {
                                return@withContext Result.failure(
                                    catalogResult.exceptionOrNull() ?: Exception("Error sincronizando catálogo"),
                                )
                            }
                            val ordersResult = repository.syncPendingOrders(apiUrl)
                            if (ordersResult.isFailure) {
                                return@withContext Result.failure(
                                    ordersResult.exceptionOrNull() ?: Exception("Error sincronizando órdenes"),
                                )
                            }
                            Result.success(ordersResult.getOrNull() ?: 0)
                        }
                        if (result.isSuccess) {
                            loadLocal()
                            message = "Sincronización OK. Mesas disponibles: ${tables.size}"
                        } else {
                            message = "Error online: ${result.exceptionOrNull()?.message}"
                        }
                    }
                },
                onTableClick = { table ->
                    selectedTable = table
                    cart.clear()
                    screen = OrderScreen.ProductTypes
                },
            )

            OrderScreen.ProductTypes -> TypeScreen(
                productTypes = productTypes,
                onTypeClick = { type ->
                    selectedType = type
                    screen = OrderScreen.Products
                },
            )

            OrderScreen.Products -> ProductScreen(
                products = products,
                cartCount = cart.sumOf { it.quantity },
                onProductClick = { product ->
                    editingProduct = product
                    screen = OrderScreen.ProductOptions
                },
                onOpenOrder = { screen = OrderScreen.Order },
            )

            OrderScreen.ProductOptions -> ProductOptionsScreen(
                product = editingProduct,
                ingredients = optionIngredients,
                usingFullIngredientList = usingFullIngredientList,
                includedIngredientIds = includedIngredientIds,
                notes = optionNotes,
                onNotesChange = { optionNotes = it },
                onToggleIngredient = { ingredientId ->
                    includedIngredientIds = if (ingredientId in includedIngredientIds) {
                        includedIngredientIds - ingredientId
                    } else {
                        includedIngredientIds + ingredientId
                    }
                },
                onAdd = {
                    val product = editingProduct ?: return@ProductOptionsScreen
                    val excluded = optionIngredients
                        .filter { it.id !in includedIngredientIds }
                        .map { it.name }
                    val notes = optionNotes.trim()
                    val key = listOf(
                        product.id,
                        notes,
                        excluded.sorted().joinToString("|"),
                    ).joinToString("#")
                    val index = cart.indexOfFirst { it.key == key }
                    if (index >= 0) {
                        val current = cart[index]
                        cart[index] = current.copy(quantity = current.quantity + 1)
                    } else {
                        cart.add(
                            CartLine(
                                key = key,
                                product = product,
                                quantity = 1,
                                notes = notes,
                                excludedIngredients = excluded,
                            ),
                        )
                    }
                    message = "Agregado: ${product.name}"
                    screen = OrderScreen.Products
                },
            )

            OrderScreen.Order -> OrderScreenContent(
                cart = cart,
                waiter = waiter,
                onWaiterChange = { waiter = it },
                sending = sending,
                buttonLabel = if (unprintedOrderId != null) {
                    "Reintentar impresión"
                } else {
                    "Enviar comanda a impresora"
                },
                canSend = selectedTable != null && waiter.isNotBlank() &&
                    (cart.isNotEmpty() || unprintedOrderId != null) && !sending,
                onChangeQuantity = { key, delta ->
                    val index = cart.indexOfFirst { it.key == key }
                    if (index < 0) return@OrderScreenContent
                    val updated = cart[index].quantity + delta
                    if (updated <= 0) {
                        cart.removeAt(index)
                    } else {
                        cart[index] = cart[index].copy(quantity = updated)
                    }
                },
                onSend = {
                    val table = selectedTable ?: return@OrderScreenContent
                    sending = true
                    message = "Enviando comanda..."
                    scope.launch {
                        val deviceId = Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID,
                        ) ?: "unknown-device"
                        val existingOrderId = unprintedOrderId
                        val lines = cart.map { line ->
                            OrderDraftLine(
                                productId = line.product.id,
                                productName = line.product.name,
                                quantity = line.quantity.toDouble(),
                                unitPrice = line.product.unitPrice,
                                notes = line.notes,
                                excludedIngredients = line.excludedIngredients,
                            )
                        }
                        val result = withContext(Dispatchers.IO) {
                            runCatching {
                                val orderId = existingOrderId ?: repository.createOrder(
                                    table = table,
                                    waiterName = waiter.trim(),
                                    deviceId = deviceId,
                                    lines = lines,
                                )
                                val ticket = dao.getTicketForOrder(orderId)
                                    ?: error("No se generó el XML de la comanda")
                                if (printerMac.isBlank()) {
                                    return@runCatching orderId to "Indica la MAC de la impresora."
                                }
                                val printed = BluetoothEscPosPrinter(printerMac.trim()).print(
                                    KitchenTicketFormatter().format(ticket.payload),
                                )
                                if (printed.isFailure) {
                                    dao.markTicketFailed(
                                        ticket.id,
                                        printed.exceptionOrNull()?.message ?: "print failed",
                                    )
                                    return@runCatching orderId to (
                                        printed.exceptionOrNull()?.message ?: "No se pudo imprimir"
                                    )
                                }
                                dao.markTicketPrinted(ticket.id, System.currentTimeMillis())
                                orderId to null
                            }
                        }
                        sending = false
                        result.fold(
                            onSuccess = { (orderId, printError) ->
                                if (printError == null) {
                                    unprintedOrderId = null
                                    cart.clear()
                                    screen = OrderScreen.Tables
                                    message = "Comanda enviada a la impresora. Orden ${orderId.take(8)}"
                                } else {
                                    unprintedOrderId = orderId
                                    message = "Orden ${orderId.take(8)} guardada. $printError"
                                }
                            },
                            onFailure = { error ->
                                message = "No se pudo crear la orden: ${error.message}"
                            },
                        )
                    }
                },
            )
        }

        if (screen != OrderScreen.Order && cart.isNotEmpty()) {
            OutlinedButton(
                onClick = { screen = OrderScreen.Order },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Ver orden (${cart.sumOf { it.quantity }})")
            }
        }

        Text(text = message, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun TablesScreen(
    tables: List<DiningTableEntity>,
    onSync: () -> Unit,
    onTableClick: (DiningTableEntity) -> Unit,
) {
    OutlinedButton(onClick = onSync, modifier = Modifier.fillMaxWidth()) {
        Text("Sincronizar HOST")
    }
    if (tables.isEmpty()) {
        Text(
            text = "No hay mesas cargadas en la base local.",
            color = MaterialTheme.colorScheme.secondary,
        )
        return
    }
    Text("Mesas (${tables.size})", style = MaterialTheme.typography.titleMedium)
    tables.chunked(2).forEach { rowTables ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            rowTables.forEach { table ->
                Button(
                    onClick = { onTableClick(table) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 72.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(table.code)
                        val zone = table.zone.orEmpty()
                        if (zone.isNotBlank()) {
                            Text(zone, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            if (rowTables.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TypeScreen(
    productTypes: List<ProductTypeEntity>,
    onTypeClick: (ProductTypeEntity) -> Unit,
) {
    if (productTypes.isEmpty()) {
        Text(
            text = "No hay tipos de producto en la base local.",
            color = MaterialTheme.colorScheme.secondary,
        )
        return
    }
    productTypes.forEach { type ->
        Button(
            onClick = { onTypeClick(type) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(type.name)
        }
    }
}

@Composable
private fun ProductScreen(
    products: List<ProductEntity>,
    cartCount: Int,
    onProductClick: (ProductEntity) -> Unit,
    onOpenOrder: () -> Unit,
) {
    if (cartCount > 0) {
        Button(onClick = onOpenOrder, modifier = Modifier.fillMaxWidth()) {
            Text("Enviar orden ($cartCount)")
        }
    }
    if (products.isEmpty()) {
        Text(
            text = "No hay productos para este tipo.",
            color = MaterialTheme.colorScheme.secondary,
        )
        return
    }
    products.forEach { product ->
        OutlinedButton(
            onClick = { onProductClick(product) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("${product.name}  ·  ${product.unitPrice}")
        }
    }
}

@Composable
private fun ProductOptionsScreen(
    product: ProductEntity?,
    ingredients: List<IngredientEntity>,
    usingFullIngredientList: Boolean,
    includedIngredientIds: Set<String>,
    notes: String,
    onNotesChange: (String) -> Unit,
    onToggleIngredient: (String) -> Unit,
    onAdd: () -> Unit,
) {
    if (product == null) {
        Text("Selecciona un producto.")
        return
    }
    Text("Ingredientes", style = MaterialTheme.typography.titleMedium)
    Text(
        text = if (usingFullIngredientList) {
            "Este producto no tiene receta guardada. Quita la marca de lo que no debe llevar."
        } else {
            "Quita la marca del ingrediente que no debe llevar."
        },
        style = MaterialTheme.typography.bodySmall,
    )
    if (ingredients.isEmpty()) {
        Text(
            text = "No hay ingredientes en la base local.",
            color = MaterialTheme.colorScheme.secondary,
        )
    } else {
        ingredients.forEach { ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = ingredient.id in includedIngredientIds,
                    onCheckedChange = { onToggleIngredient(ingredient.id) },
                )
                Text(ingredient.name)
            }
        }
    }
    OutlinedTextField(
        value = notes,
        onValueChange = onNotesChange,
        label = { Text("Cambios") },
        minLines = 2,
        modifier = Modifier.fillMaxWidth(),
    )
    Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
        Text("Agregar a la orden")
    }
}

@Composable
private fun OrderScreenContent(
    cart: List<CartLine>,
    waiter: String,
    onWaiterChange: (String) -> Unit,
    sending: Boolean,
    buttonLabel: String,
    canSend: Boolean,
    onChangeQuantity: (String, Int) -> Unit,
    onSend: () -> Unit,
) {
    OutlinedTextField(
        value = waiter,
        onValueChange = onWaiterChange,
        label = { Text("Mesero") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    if (cart.isEmpty()) {
        Text(
            text = "La orden no tiene productos.",
            color = MaterialTheme.colorScheme.secondary,
        )
    }
    cart.forEach { line ->
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("${line.quantity} x ${line.product.name}", style = MaterialTheme.typography.bodyLarge)
            if (line.excludedIngredients.isNotEmpty()) {
                Text("Sin: ${line.excludedIngredients.joinToString(", ")}")
            }
            if (line.notes.isNotBlank()) {
                Text("Cambios: ${line.notes}")
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onChangeQuantity(line.key, -1) }) {
                    Text("-")
                }
                OutlinedButton(onClick = { onChangeQuantity(line.key, 1) }) {
                    Text("+")
                }
            }
        }
    }
    Button(
        enabled = canSend,
        onClick = onSend,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(if (sending) "Enviando..." else buttonLabel)
    }
}
