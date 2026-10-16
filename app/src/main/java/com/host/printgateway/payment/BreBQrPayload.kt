package com.host.printgateway.payment

import java.text.Normalizer
import java.util.Locale

data class BreBProfile(
    val key: String = "",
    val keyType: Int = 0,
    val merchantName: String = "",
    val city: String = "",
    val postalCode: String = "",
)

/**
 * Same merchant QR text HOST builds for a Bre-B transfer.
 * The amount already includes tax and tip.
 */
object BreBQrPayload {
    const val MISSING_KEY = "Configura la llave Bre-B del local antes de mostrar el QR."
    const val MISSING_PLACE = "Configura el nombre comercial y la ciudad del local para generar el QR."

    fun build(
        profile: BreBProfile,
        amount: Double,
        reference: String,
    ): String {
        if (profile.key.isBlank() || profile.keyType !in 1..5) {
            throw IllegalArgumentException(MISSING_KEY)
        }
        if (profile.merchantName.isBlank() || profile.city.isBlank()) {
            throw IllegalArgumentException(MISSING_PLACE)
        }
        val key = requireKey(profile.key)
        val rounded = kotlin.math.round(amount * 100.0) / 100.0
        if (rounded <= 0.0) throw IllegalArgumentException("El monto del QR debe ser mayor que cero.")
        val amountText = String.format(Locale.US, "%.2f", rounded)
        if (amountText.length > 13) {
            throw IllegalArgumentException("El monto del QR supera el máximo permitido.")
        }
        val merchant = merchantText(profile.merchantName, 25, "nombre comercial")
        val city = merchantText(profile.city, 15, "ciudad")
        val postal = optionalCode(profile.postalCode, 10)
        val ref = requireReference(reference)
        val keyTag = profile.keyType.toString().padStart(2, '0')

        val body = StringBuilder()
        append(body, "00", "01")
        append(body, "01", "11")
        appendTemplate(body, "26", "00" to "CO.COM.ACH.LLA", keyTag to key)
        appendTemplate(body, "49", "00" to "CO.COM.ACH.RED", "01" to "ACH")
        appendTemplate(body, "50", "00" to "CO.COM.ACH.CU", "01" to key)
        append(body, "52", "0000")
        append(body, "53", "170")
        append(body, "54", amountText)
        append(body, "58", "CO")
        append(body, "59", merchant)
        append(body, "60", city)
        if (postal != null) append(body, "61", postal)
        appendTemplate(body, "62", "07" to "0000", "08" to "00")
        appendTemplate(body, "80", "00" to "CO.COM.ACH.CANAL", "01" to "POS")
        appendTemplate(body, "81", "00" to "CO.COM.ACH.CIVA", "01" to "02")
        appendTemplate(body, "82", "00" to "CO.COM.ACH.IVA", "01" to "0")
        appendTemplate(body, "83", "00" to "CO.COM.ACH.BASE", "01" to "0")
        appendTemplate(body, "84", "00" to "CO.COM.ACH.CINC", "01" to "02")
        appendTemplate(body, "85", "00" to "CO.COM.ACH.INC", "01" to "0")
        appendTemplate(body, "90", "00" to "CO.COM.ACH.TRXID", "01" to ref)

        val withCrcTag = body.toString() + "6304"
        return withCrcTag + crc16Ccitt(withCrcTag)
    }

    fun crc16Ccitt(value: String): String {
        var crc = 0xFFFF
        for (byte in value.toByteArray(Charsets.US_ASCII)) {
            crc = crc xor ((byte.toInt() and 0xFF) shl 8)
            repeat(8) {
                crc = if (crc and 0x8000 != 0) {
                    ((crc shl 1) xor 0x1021) and 0xFFFF
                } else {
                    (crc shl 1) and 0xFFFF
                }
            }
        }
        return "%04X".format(crc)
    }

    private fun append(body: StringBuilder, tag: String, value: String) {
        body.append(tag)
        body.append(value.length.toString().padStart(2, '0'))
        body.append(value)
    }

    private fun appendTemplate(body: StringBuilder, tag: String, vararg fields: Pair<String, String>) {
        val inner = StringBuilder()
        for (field in fields) append(inner, field.first, field.second)
        append(body, tag, inner.toString())
    }

    private fun requireKey(key: String): String {
        val value = key.trim()
        if (value.isEmpty() || value.length > 40 || value.any { it.code !in 33..126 }) {
            throw IllegalArgumentException(
                "La llave Bre-B debe tener entre 1 y 40 caracteres simples, sin espacios.",
            )
        }
        return value
    }

    private fun requireReference(reference: String): String {
        val value = reference.trim()
        if (value.isEmpty() || value.length > 18 || value.any { !it.isLetterOrDigit() || it.code > 127 }) {
            throw IllegalArgumentException("La referencia del QR no es válida.")
        }
        return value
    }

    private fun optionalCode(value: String, maxLength: Int): String? {
        val text = value.filter { it.isLetterOrDigit() && it.code < 128 }
        if (text.isEmpty()) return null
        return text.take(maxLength)
    }

    private fun merchantText(value: String, maxLength: Int, label: String): String {
        val normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
        val chars = StringBuilder()
        for (c in normalized) {
            if (Character.getType(c) == Character.NON_SPACING_MARK.toInt()) continue
            if (c.isLetterOrDigit() && c.code < 128) {
                chars.append(c)
                continue
            }
            if (chars.isNotEmpty() && chars.last() != ' ') chars.append(' ')
        }
        val text = chars.toString().split(' ').filter { it.isNotEmpty() }.joinToString(" ")
        if (text.isEmpty()) {
            throw IllegalArgumentException("El $label del local no puede usarse en el QR. Usa letras o números.")
        }
        return if (text.length > maxLength) text.take(maxLength).trimEnd() else text
    }
}
