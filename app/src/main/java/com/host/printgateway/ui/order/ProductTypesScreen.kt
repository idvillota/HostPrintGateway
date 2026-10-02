package com.host.printgateway.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.host.printgateway.data.ProductTypeEntity

@Composable
fun ProductTypesScreen(
    padding: PaddingValues,
    tableCode: String,
    types: List<ProductTypeEntity>,
    notice: String,
    cartCount: Int,
    onType: (ProductTypeEntity) -> Unit,
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
            subtitle = "Elige un tipo de producto",
            notice = notice,
        )
        if (types.isEmpty()) {
            Text(
                text = "No hay tipos de producto en este dispositivo.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            ) {
                items(types, key = { it.id }) { type ->
                    Card(
                        onClick = { onType(type) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = type.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                        )
                    }
                }
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
