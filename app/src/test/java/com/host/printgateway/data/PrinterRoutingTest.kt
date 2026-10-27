package com.host.printgateway.data

import org.junit.Assert.assertEquals
import org.junit.Test

class PrinterRoutingTest {

    @Test
    fun one_printer_without_categories_receives_every_line_and_the_invoice() {
        val printer = PrinterTarget("p", "AA:BB", emptySet(), printsInvoices = true)
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "postre"), listOf(printer))

        assertEquals(setOf(0, 1), copies.single().lineIndexes)
        assertEquals(listOf("AA:BB"), PrinterRouting.invoiceMacs(listOf(printer)))
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
    fun a_printer_without_categories_receives_only_lines_nobody_claimed() {
        val grill = PrinterTarget("g", "11", setOf("parrilla"), printsInvoices = false)
        val other = PrinterTarget("o", "22", emptySet(), printsInvoices = false)
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "postre"), listOf(grill, other))

        assertEquals(setOf(0), copies.first { it.mac == "11" }.lineIndexes)
        assertEquals(setOf(1), copies.first { it.mac == "22" }.lineIndexes)
    }

    @Test
    fun an_unmarked_category_is_not_printed() {
        val grill = PrinterTarget("g", "11", setOf("parrilla"), printsInvoices = false)
        val copies = PrinterRouting.kitchenCopies(listOf("parrilla", "nueva"), listOf(grill))

        assertEquals(setOf(0), copies.single().lineIndexes)
        assertEquals(emptyList<String>(), PrinterRouting.invoiceMacs(listOf(grill)))
    }
}
