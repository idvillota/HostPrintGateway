package com.host.printgateway.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.OrderItemEntity

@Composable
fun TableMenuScreen(
    padding: PaddingValues,
    tableCode: String,
    notice: String,
    cartCount: Int,
    onOrderedProducts: () -> Unit,
    onNewOrder: () -> Unit,
    onOpenComanda: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
    ) {
        ScreenIntro(
            title = "Mesa $tableCode",
            subtitle = "Elige qué quieres hacer",
            notice = notice,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onOrderedProducts,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Productos pedidos")
            }
            Button(
                onClick = onNewOrder,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Hacer pedido")
            }
        }
        if (cartCount > 0) {
            Button(
                onClick = onOpenComanda,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                Text("Ver comanda ($cartCount)")
            }
        }
    }
}

@Composable
fun OrderedProductsScreen(
    padding: PaddingValues,
    tableCode: String,
    items: List<OrderItemEntity>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
    ) {
        ScreenIntro(
            title = "Mesa $tableCode",
            subtitle = "Productos ya pedidos",
            notice = "",
        )
        if (items.isEmpty()) {
            Text(
                text = "Esta mesa todavía no tiene productos pedidos.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            ) {
                items(items, key = { it.id }) { item ->
                    OrderedProductCard(item)
                }
            }
        }
    }
}

@Composable
private fun OrderedProductCard(item: OrderItemEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${quantityLabel(item.quantity)}  ${item.productName}",
                    style = MaterialTheme.typography.titleMedium,
                )
                if (item.notes.isNotBlank()) {
                    Text(
                        text = item.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = formatPrice(item.lineTotal),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

private fun quantityLabel(quantity: Double): String {
    val amount = if (quantity % 1.0 == 0.0) quantity.toLong().toString() else quantity.toString()
    return "${amount}×"
}
