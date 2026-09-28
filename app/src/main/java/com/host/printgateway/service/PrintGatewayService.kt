package com.host.printgateway.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.PrintJobEntity
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.network.PrintJobAckRequest
import com.host.printgateway.network.PrintJobApi
import com.host.printgateway.printer.BluetoothEscPosPrinter
import com.host.printgateway.printer.PrintJobRenderer
import com.host.printgateway.printer.KitchenTicketFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Foreground poll loop: API → Room → ESC/POS → Bluetooth → ACK.
 */
class PrintGatewayService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val printMutex = Mutex()
    private var loopJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopSelfGracefully()
                return START_NOT_STICKY
            }
            else -> startGateway()
        }
        return START_STICKY
    }

    private fun startGateway() {
        val settings = GatewaySettings(this)
        if (!settings.isPrinterConfigured()) {
            updateNotification("Configuración incompleta")
            stopSelf()
            return
        }

        startForeground(NOTIFICATION_ID, buildNotification("Gateway iniciado…"))
        loopJob?.cancel()
        loopJob = scope.launch {
            val api = if (settings.isConfigured()) {
                PrintJobApi(settings.apiBaseUrl, settings.deviceToken)
            } else {
                null
            }
            val printer = BluetoothEscPosPrinter(settings.printerMac)
            val renderer = PrintJobRenderer()
            val ticketFormatter = KitchenTicketFormatter()
            val database = PrintGatewayDatabase.get(this@PrintGatewayService)
            val printJobsDao = database.printJobDao()
            val restaurantDao = database.restaurantDao()

            while (isActive) {
                try {
                    printMutex.withLock {
                        val remoteJobs = if (api != null) {
                            api.fetchPendingJobs().getOrElse { error ->
                                updateNotification("Offline: ${error.message?.take(70) ?: "sin conexión"}")
                                emptyList()
                            }
                        } else {
                            emptyList()
                        }
                        if (remoteJobs.isNotEmpty()) {
                            printJobsDao.insertAll(remoteJobs.map { job ->
                                PrintJobEntity(
                                    id = job.id,
                                    kind = job.kind,
                                    payloadFormat = job.payloadFormat,
                                    payload = job.payload,
                                )
                            })
                            updateNotification("Trabajos guardados: ${remoteJobs.size}")
                        }

                        for (job in printJobsDao.getPrintedAwaitingAck()) {
                            if (api == null || api.acknowledge(job.id, PrintJobAckRequest(true)).isSuccess) {
                                printJobsDao.deleteById(job.id)
                            }
                        }

                        val pendingJobs = printJobsDao.getPending()
                        if (pendingJobs.isEmpty()) {
                            updateNotification("Escuchando… (cola vacía)")
                        }
                        for (job in pendingJobs) {
                            updateNotification("Imprimiendo ${job.id.take(8)}…")
                            val rendered = runCatching {
                                renderer.toEscPos(
                                    com.host.printgateway.network.PrintJobDto(
                                        id = job.id,
                                        kind = job.kind,
                                        payloadFormat = job.payloadFormat,
                                        payload = job.payload,
                                    ),
                                )
                            }
                            if (rendered.isFailure) {
                                val error = rendered.exceptionOrNull()
                                api?.acknowledge(job.id, PrintJobAckRequest(false, error?.message))
                                updateNotification("Formato inválido: ${job.id.take(8)}")
                                continue
                            }
                            val bytes = rendered.getOrThrow()
                            val printed = printer.print(bytes)
                            if (printed.isSuccess) {
                                printJobsDao.markPrinted(job.id)
                                if (api == null || api.acknowledge(job.id, PrintJobAckRequest(true)).isSuccess) {
                                    printJobsDao.deleteById(job.id)
                                    updateNotification("OK ${job.id.take(8)}")
                                } else {
                                    updateNotification("Impreso offline: ${job.id.take(8)}")
                                }
                            } else {
                                val msg = printed.exceptionOrNull()?.message ?: "print failed"
                                api?.acknowledge(job.id, PrintJobAckRequest(false, msg))
                                updateNotification("Error impresora: ${msg.take(60)}")
                                break
                            }
                        }

                        for (ticket in restaurantDao.getPendingTickets()) {
                            updateNotification("Imprimiendo comanda ${ticket.id.take(8)}…")
                            val printed = printer.print(ticketFormatter.format(ticket.payload))
                            if (printed.isSuccess) {
                                restaurantDao.markTicketPrinted(ticket.id, System.currentTimeMillis())
                                updateNotification("Comanda OK ${ticket.id.take(8)}")
                            } else {
                                restaurantDao.markTicketFailed(
                                    ticket.id,
                                    printed.exceptionOrNull()?.message ?: "print failed",
                                )
                                updateNotification("Error comanda: ${ticket.id.take(8)}")
                                break
                            }
                        }
                    }
                } catch (e: Exception) {
                    updateNotification("Error: ${e.message?.take(80)}")
                }
                delay(settings.pollIntervalMs)
            }
        }
    }

    private fun stopSelfGracefully() {
        loopJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(text: String): Notification {
        ensureChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Host Print Gateway")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_share)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification(text: String) {
        val manager = getSystemService(NotificationManager::class.java) ?: return
        manager.notify(NOTIFICATION_ID, buildNotification(text))
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Print Gateway",
            NotificationManager.IMPORTANCE_LOW,
        )
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        loopJob?.cancel()
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_STOP = "com.host.printgateway.STOP"
        private const val CHANNEL_ID = "host_print_gateway"
        private const val NOTIFICATION_ID = 1001
    }
}
