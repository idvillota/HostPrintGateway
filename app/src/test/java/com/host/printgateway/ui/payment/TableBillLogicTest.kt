package com.host.printgateway.ui.payment

import com.host.printgateway.data.DiningTableEntity
import com.host.printgateway.data.OrderEntity
import com.host.printgateway.data.OrderItemEntity
import com.host.printgateway.data.RestaurantStatuses
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TableBillLogicTest {

    @Test
    fun parseAmount_accepts_comma_decimal_and_rejects_negative() {
        assertEquals(0.0, parseAmount("")!!, 0.0)
        assertEquals(12.5, parseAmount("12,5")!!, 0.0)
        assertEquals(12.5, parseAmount("12.50")!!, 0.0)
        assertEquals(1250.0, parseAmount("1.250")!!, 0.0)
        assertEquals(12500.5, parseAmount("12.500,50")!!, 0.0)
        assertNull(parseAmount("-1"))
        assertNull(parseAmount("abc"))
    }

    @Test
    fun sanitizeMoneyTyping_groups_thousands_and_keeps_a_decimal_comma() {
        assertEquals("12500", sanitizeMoneyTyping("12.500"))
        assertEquals("12500,5", sanitizeMoneyTyping("12500,5"))
        assertEquals("12,5", sanitizeMoneyTyping("12.5"))
        assertEquals("12.500", groupedMoneyDisplay("12500"))
        assertEquals("12.500,5", groupedMoneyDisplay("12500,5"))
    }

    @Test
    fun transferQrAmounts_keeps_one_code_per_distinct_share() {
        val exact = transferQrAmounts(100.0, SplitMode.Equal, 5, emptyList())
        assertEquals(1, exact.size)
        assertEquals(20.0, exact[0].amount, 0.0)
        assertEquals(5, exact[0].shares)

        val remainder = transferQrAmounts(10.0, SplitMode.Equal, 3, emptyList())
        assertEquals(2, remainder.size)
        assertEquals(3.33, remainder[0].amount, 0.001)
        assertEquals(2, remainder[0].shares)
        assertEquals(3.34, remainder[1].amount, 0.001)
        assertEquals(1, remainder[1].shares)
    }

    @Test
    fun billOf_aggregates_lines_and_totals_for_table() {
        val orders = listOf(
            order("o1", "T1", "M1", total = 10.0, tax = 1.0, waiter = "Luis"),
            order("o2", "T1", "M1", total = 5.0, tax = 0.5, waiter = "Ana"),
        )
        val items = mapOf(
            "o1" to listOf(item("i1", "o1", "Café", 2.0, 5.0, 10.0, "")),
            "o2" to listOf(item("i2", "o2", "Té", 1.0, 5.0, 5.0, "caliente")),
        )

        val bill = billOf("T1", orders, items)!!

        assertEquals("M1", bill.tableCode)
        assertEquals(listOf("o1", "o2"), bill.orderIds)
        assertEquals("Ana", bill.cashier)
        assertEquals(15.0, bill.foodTotal, 0.0)
        assertEquals(1.5, bill.taxAmount, 0.0)
        assertEquals(2, bill.lines.size)
        assertEquals("Té (caliente)", bill.lines[1].description)
        assertEquals(3, bill.articleCount)
    }

    @Test
    fun accountsOf_includes_occupied_tables_even_without_orders() {
        val tables = listOf(
            DiningTableEntity("T1", "M1", 4, null, null, null, "1", true),
            DiningTableEntity("T2", "M2", 2, null, null, null, "0", true),
        )

        val accounts = accountsOf(tables, emptyList())

        assertEquals(1, accounts.size)
        assertEquals("T1", accounts[0].tableKey)
        assertEquals(0.0, accounts[0].total, 0.0)
    }

    @Test
    fun summariesOf_groups_by_table() {
        val summaries = summariesOf(
            listOf(
                order("o1", "T1", "M1", 10.0, 0.0, "A"),
                order("o2", "T1", "M1", 4.0, 0.0, "A"),
            ),
        )
        assertEquals(1, summaries.size)
        assertEquals(2, summaries[0].orderCount)
        assertEquals(14.0, summaries[0].total, 0.0)
        assertTrue(summaries[0].tableCode == "M1")
    }

    private fun order(
        id: String,
        tableId: String,
        code: String,
        total: Double,
        tax: Double,
        waiter: String,
    ) = OrderEntity(
        id = id,
        diningTableId = tableId,
        diningTableCode = code,
        number = id.take(4),
        customerId = null,
        waiterName = waiter,
        deviceId = "d",
        status = RestaurantStatuses.ORDER_SYNCED,
        openedAtUtc = 1L,
        subtotal = total,
        taxAmount = tax,
        total = total,
    )

    private fun item(
        id: String,
        orderId: String,
        name: String,
        qty: Double,
        price: Double,
        lineTotal: Double,
        notes: String,
    ) = OrderItemEntity(
        id = id,
        orderId = orderId,
        productId = "p",
        productName = name,
        quantity = qty,
        unitPrice = price,
        lineTotal = lineTotal,
        unitCostPrice = null,
        notes = notes,
        sentToKitchenAtUtc = null,
    )
}
