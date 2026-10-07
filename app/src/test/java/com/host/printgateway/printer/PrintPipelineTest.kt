package com.host.printgateway.printer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PrintPipelineTest {

    @Test
    fun requirePrinterMac_rejects_blank() {
        val result = PrintPipeline.requirePrinterMac("   ")
        assertTrue(result.isFailure)
        assertEquals(
            "Indica la MAC de la impresora en Configuración.",
            result.exceptionOrNull()?.message,
        )
    }

    @Test
    fun requirePrinterMac_trims_value() {
        assertEquals("AA:BB:CC:DD:EE:FF", PrintPipeline.requirePrinterMac("  AA:BB:CC:DD:EE:FF  ").getOrThrow())
    }

    @Test
    fun renderKitchenTicketXml_produces_escpos_with_comanda_header() {
        val xml = """
            <?xml version='1.0' encoding='UTF-8'?>
            <KitchenTicket>
              <TableCode>M1</TableCode>
              <OrderNumber>ABC12345</OrderNumber>
              <SentBy>Luis</SentBy>
              <SentAtUtc>2026-10-07T12:00:00Z</SentAtUtc>
              <PrinterStation><Name>Cocina</Name><Code>K1</Code></PrinterStation>
              <Lines>
                <Line>
                  <ProductName>Bandeja</ProductName>
                  <Quantity>1</Quantity>
                  <Notes>sin cebolla</Notes>
                  <ExcludedIngredients><Ingredient>Cebolla</Ingredient></ExcludedIngredients>
                </Line>
              </Lines>
              <IsCancellation>false</IsCancellation>
              <CancelReason></CancelReason>
            </KitchenTicket>
        """.trimIndent()

        val bytes = PrintPipeline.renderKitchenTicketXml(xml).getOrThrow()
        val text = bytes.toString(Charsets.ISO_8859_1)

        assertTrue(text.contains("COMANDA"))
        assertTrue(text.contains("Mesa: M1"))
        assertTrue(text.contains("Bandeja"))
        assertTrue(text.contains("sin cebolla"))
        assertTrue(text.contains("Cebolla"))
    }

    @Test
    fun renderSalesReceiptXml_produces_escpos_bytes() {
        val xml = """
            <Factura>
              <NombreComercial>Host</NombreComercial>
              <NumeroFactura>FV-1</NumeroFactura>
              <ConsecutivoDIAN></ConsecutivoDIAN>
              <FechaHora>now</FechaHora>
              <Mesas>M1</Mesas>
              <Cajero>Caja</Cajero>
              <Nombre>Cliente</Nombre>
              <Identificacion>1</Identificacion>
              <Items>
                <Item>
                  <Descripcion>Item</Descripcion>
                  <Cantidad>1</Cantidad>
                  <PrecioUnitario>1000</PrecioUnitario>
                  <TotalLinea>1000</TotalLinea>
                </Item>
              </Items>
              <Articulos>1</Articulos>
              <Total>1000</Total>
              <Metodo>Efectivo</Metodo>
              <Entregado>1000</Entregado>
              <BaseImpoconsumo>926</BaseImpoconsumo>
              <Impoconsumo>74</Impoconsumo>
              <Numero></Numero>
              <RangoDesde></RangoDesde>
              <RangoHasta></RangoHasta>
            </Factura>
        """.trimIndent()

        val bytes = PrintPipeline.renderSalesReceiptXml(xml).getOrThrow()
        val text = bytes.toString(Charsets.ISO_8859_1)
        assertTrue(bytes.isNotEmpty())
        assertTrue(text.contains("FACTURA"))
        assertTrue(text.contains("Host"))
    }
}
