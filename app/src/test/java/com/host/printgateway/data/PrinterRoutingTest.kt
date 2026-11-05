package com.host.printgateway.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PrinterRoutingTest {

    @Test
    fun invoice_only_printer_does_not_receive_kitchen() {
        val printer = PrinterTarget("p", "AA:BB", emptySet(), printsInvoices = true)
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "postre"), listOf(printer))

        assertTrue(copies.isEmpty())
        assertEquals(listOf("AA:BB"), PrinterRouting.invoiceMacs(listOf(printer)))
    }

    @Test
    fun all_kitchen_printer_receives_every_line() {
        val printer = PrinterTarget(
            "p",
            "AA:BB",
            emptySet(),
            printsInvoices = false,
            printsAllKitchen = true,
        )
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "postre"), listOf(printer))

        assertEquals(setOf(0, 1), copies.single().lineIndexes)
        assertEquals(emptyList<String>(), PrinterRouting.invoiceMacs(listOf(printer)))
    }

    @Test
    fun a_printer_can_own_several_categories() {
        val grill = PrinterTarget("g", "11", setOf("parrilla", "ensaladas"), printsInvoices = false)
        val pastry = PrinterTarget("p", "22", setOf("postre"), printsInvoices = true)
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "postre", "ensaladas"), listOf(grill, pastry))

        assertEquals(setOf(0, 2), copies.first { it.mac == "11" }.lineIndexes)
        assertEquals(setOf(1), copies.first { it.mac == "22" }.lineIndexes)
        assertEquals(listOf("22"), PrinterRouting.invoiceMacs(listOf(grill, pastry)))
    }

    @Test
    fun an_unmarked_category_is_not_printed() {
        val grill = PrinterTarget("g", "11", setOf("parrilla"), printsInvoices = false)
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "nueva"), listOf(grill))

        assertEquals(setOf(0), copies.single().lineIndexes)
        assertEquals(emptyList<String>(), PrinterRouting.invoiceMacs(listOf(grill)))
    }

    @Test
    fun invoice_and_all_kitchen_can_share_one_printer() {
        val caja = PrinterTarget(
            "c",
            "33",
            emptySet(),
            printsInvoices = true,
            printsAllKitchen = true,
        )
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla"), listOf(caja))

        assertEquals(setOf(0), copies.single().lineIndexes)
        assertEquals(listOf("33"), PrinterRouting.invoiceMacs(listOf(caja)))
    }
}
