package com.host.printgateway.receipt

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SalesReceiptXmlParserTest {

    @Test
    fun parse_reads_header_lines_and_totals() {
        val xml = """
            <Factura>
              <NombreComercial>Host Café</NombreComercial>
              <NumeroFactura>FV-100</NumeroFactura>
              <ConsecutivoDIAN>DIAN-9</ConsecutivoDIAN>
              <FechaHora>2026-10-07 12:00</FechaHora>
              <Mesas>M3</Mesas>
              <Cajero>Ana</Cajero>
              <Nombre>Consumidor final</Nombre>
              <Identificacion>222</Identificacion>
              <Items>
                <Item>
                  <Descripcion>Café</Descripcion>
                  <Cantidad>2</Cantidad>
                  <PrecioUnitario>5000</PrecioUnitario>
                  <TotalLinea>10000</TotalLinea>
                </Item>
                <Item>
                  <Descripcion>Pan</Descripcion>
                  <Cantidad>1</Cantidad>
                  <PrecioUnitario>3000</PrecioUnitario>
                  <TotalLinea>3000</TotalLinea>
                </Item>
              </Items>
              <Articulos>3</Articulos>
              <Total>13000</Total>
              <Metodo>Efectivo</Metodo>
              <Entregado>15000</Entregado>
              <BaseImpoconsumo>12037</BaseImpoconsumo>
              <Impoconsumo>963</Impoconsumo>
              <Numero>RES-1</Numero>
              <RangoDesde>1</RangoDesde>
              <RangoHasta>1000</RangoHasta>
            </Factura>
        """.trimIndent()

        val receipt = SalesReceiptXmlParser.parse(xml)

        assertEquals("Host Café", receipt.tradeName)
        assertEquals("FV-100", receipt.invoiceNumber)
        assertEquals("DIAN-9", receipt.dianConsecutive)
        assertEquals("M3", receipt.tableCodes)
        assertEquals("Ana", receipt.cashier)
        assertEquals("Consumidor final", receipt.customerName)
        assertEquals(2, receipt.lines.size)
        assertEquals("Café", receipt.lines[0].description)
        assertEquals(2.0, receipt.lines[0].quantity, 0.0)
        assertEquals(10000.0, receipt.lines[0].lineTotal, 0.0)
        assertEquals(3, receipt.articleCount)
        assertEquals(13000.0, receipt.total, 0.0)
        assertEquals("Efectivo", receipt.paymentMethod)
        assertEquals(15000.0, receipt.amountTendered, 0.0)
        assertEquals("RES-1", receipt.resolutionNumber)
    }
}
