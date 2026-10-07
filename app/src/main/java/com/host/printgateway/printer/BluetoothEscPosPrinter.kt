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
            socket.outputStream.use { stream ->
                stream.write(bytes)
                stream.flush()
                sent = true
            }
            delay(400)
            Result.success(Unit)
        } catch (e: IOException) {
            if (sent) Result.success(Unit) else Result.failure(e)
        } finally {
            try {
                socket?.close()
            } catch (_: IOException) {
            }
        }
    }
}
