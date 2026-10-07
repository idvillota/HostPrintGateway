package com.host.printgateway.ui.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.host.printgateway.data.ProductEntity
import java.text.NumberFormat
import java.util.Locale

data class CartLine(
    val key: String,
    val product: ProductEntity,
    val quantity: Int,
    val notes: String,
)

class OrderSession {
    val cart = mutableStateListOf<CartLine>()
    var tableId by mutableStateOf<String?>(null)
    var waiter by mutableStateOf("Mesero")
    var notice by mutableStateOf("")
    var sending by mutableStateOf(false)
    var unprintedOrderId by mutableStateOf<String?>(null)

    val itemCount: Int
        get() = cart.sumOf { it.quantity }

    fun addProduct(product: ProductEntity, notes: String) {
        val cleanNotes = notes.trim()
        val key = "${product.id}#$cleanNotes"
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
                    notes = cleanNotes,
                ),
            )
        }
    }

    fun changeQuantity(key: String, delta: Int) {
        val index = cart.indexOfFirst { it.key == key }
        if (index < 0) return
        val updated = cart[index].quantity + delta
        if (updated <= 0) {
            cart.removeAt(index)
        } else {
            cart[index] = cart[index].copy(quantity = updated)
        }
    }

    fun clearCart() {
        cart.clear()
        unprintedOrderId = null
    }

    fun reset() {
        clearCart()
        tableId = null
        waiter = "Mesero"
        notice = ""
        sending = false
    }
}

internal fun formatPrice(value: Double): String {
    val format = NumberFormat.getNumberInstance(Locale("es", "CO"))
    format.isGroupingUsed = true
    if (value % 1.0 == 0.0) {
        format.maximumFractionDigits = 0
    } else {
        format.minimumFractionDigits = 2
        format.maximumFractionDigits = 2
    }
    return format.format(value)
}
