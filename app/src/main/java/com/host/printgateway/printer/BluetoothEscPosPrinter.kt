package com.host.printgateway.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

/** Sends raw ESC/POS bytes to a paired Bluetooth SPP printer. */
class BluetoothEscPosPrinter(private val macAddress: String) {

    private val sppUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    @SuppressLint("MissingPermission")
    suspend fun print(bytes: ByteArray): Result<Unit> = withContext(Dispatchers.IO) {
        var socket: BluetoothSocket? = null
        var sent = false
        try {
            val adapter = BluetoothAdapter.getDefaultAdapter()
                ?: return@withContext Result.failure(IllegalStateException("Bluetooth no disponible"))

            val mac = macAddress.replace("-", ":").trim().uppercase()
            val device = adapter.getRemoteDevice(mac)
            adapter.cancelDiscovery()

            socket = device.createRfcommSocketToServiceRecord(sppUuid)
            socket.connect()
            val stream = socket.outputStream
            stream.write(bytes)
            stream.flush()
            sent = true
            delay(DRAIN_BEFORE_CLOSE_MS)
        } catch (e: IOException) {
            if (!sent) return@withContext Result.failure(e)
        } finally {
            closeQuietly(socket)
        }
        delay(SETTLE_BEFORE_NEXT_MS)
        Result.success(Unit)
    }

    private fun closeQuietly(socket: BluetoothSocket?) {
        try {
            socket?.close()
        } catch (_: IOException) {
        }
    }

    private companion object {
        /** Keep the radio up until the printer has taken the bytes. */
        const val DRAIN_BEFORE_CLOSE_MS = 400L

        /** Let the printer finish before the next ticket opens Bluetooth. */
        const val SETTLE_BEFORE_NEXT_MS = 400L
    }
}
