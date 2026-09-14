package com.host.printgateway.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.network.PrintJobAckRequest
import com.host.printgateway.network.PrintJobApi
import com.host.printgateway.printer.BluetoothEscPosPrinter
import com.host.printgateway.printer.PrintJobRenderer
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
 * Foreground poll loop: Host pending jobs → ESC/POS → Bluetooth → ACK.
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
        if (!settings.isConfigured()) {
            updateNotification("Configuración incompleta")
            stopSelf()
            return
        }

        startForeground(NOTIFICATION_ID, buildNotification("Gateway iniciado…"))
        loopJob?.cancel()
        loopJob = scope.launch {
            val api = PrintJobApi(settings.apiBaseUrl, settings.deviceToken)
            val printer = BluetoothEscPosPrinter(settings.printerMac)
            val renderer = PrintJobRenderer()

            while (isActive) {
                try {
                    printMutex.withLock {
                        val jobs = api.fetchPendingJobs().getOrElse { error ->
                            updateNotification("API: ${error.message?.take(80) ?: "error"}")
                            return@withLock
                        }
                        if (jobs.isEmpty()) {
                            updateNotification("Escuchando… (sin trabajos)")
                        }
                        for (job in jobs) {
                            updateNotification("Imprimiendo ${job.id.take(8)}…")
                            val bytes = runCatching { renderer.toEscPos(job) }.getOrElse { e ->
                                api.acknowledge(job.id, PrintJobAckRequest(false, e.message))
                                return@withLock
                            }
                            val printed = printer.print(bytes)
                            if (printed.isSuccess) {
                                api.acknowledge(job.id, PrintJobAckRequest(true))
                                updateNotification("OK ${job.id.take(8)}")
                            } else {
                                val msg = printed.exceptionOrNull()?.message ?: "print failed"
                                api.acknowledge(job.id, PrintJobAckRequest(false, msg))
                                updateNotification("Error impresora: ${msg.take(60)}")
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
