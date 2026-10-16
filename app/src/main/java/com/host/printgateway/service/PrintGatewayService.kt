@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.host.printgateway.service

import android.annotation.SuppressLint
import android.app.Notification
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.host.printgateway.data.GatewaySettings
import com.host.printgateway.data.PrintGatewayDatabase
import com.host.printgateway.data.RestaurantDao
import com.host.printgateway.data.RestaurantStatuses
import com.host.printgateway.network.PrintJobAckRequest
import com.host.printgateway.network.PrintJobApi
import com.host.printgateway.network.PrintJobDto
import com.host.printgateway.printer.ConfiguredPrint
import com.host.printgateway.printer.CopyResult
import com.host.printgateway.printer.PrintJobRenderer
import com.host.printgateway.printer.PrintPipeline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

/**
 * Prints local tickets created after start, then prints comandas pushed by HOST.
 */
class PrintGatewayService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val printMutex = Mutex()
    private val printedJobIds = LinkedHashSet<String>()
    private var loopJob: Job? = null
    private var wakeLock: PowerManager.WakeLock? = null

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
        acquireStayAwake()
        loopJob?.cancel()
        loopJob = scope.launch {
            val api = if (settings.isConfigured()) {
                PrintJobApi(settings.apiBaseUrl, settings.deviceToken)
            } else {
                null
            }
            val printerMac = settings.printerMac
            val restaurantDao = PrintGatewayDatabase.get(this@PrintGatewayService).restaurantDao()
            val startedAt = System.currentTimeMillis()
            updateNotification("Escuchando comandas nuevas…")

            while (isActive) {
                try {
                    printMutex.withLock {
                        printLocalTickets(restaurantDao, printerMac, startedAt)
                    }
                    if (GatewaySettings(this@PrintGatewayService).offlineMode) {
                        delay(settings.pollIntervalMs)
                        continue
                    }
                    val job = select<PrintJobDto?> {
                        IncomingPrint.jobs.onReceive { it }
                        onTimeout(settings.pollIntervalMs) { null }
                    } ?: continue
                    val retry = printMutex.withLock {
                        printPushed(api, restaurantDao, printerMac, job)
                    }
                    if (retry) {
                        delay(settings.pollIntervalMs)
                        IncomingPrint.requestReplay()
                    }
                } catch (e: kotlinx.coroutines.CancellationException) {
                    throw e
                } catch (e: Exception) {
                    updateNotification("Error: ${e.message?.take(80)}")
                    delay(settings.pollIntervalMs)
                }
            }
        }
    }

    private suspend fun printLocalTickets(
        restaurantDao: RestaurantDao,
        printerMac: String,
        startedAt: Long,
    ) {
        val retryable = restaurantDao.getPendingTickets().filter {
            it.createdAt >= startedAt && it.status == RestaurantStatuses.TICKET_FAILED
        }
        for (ticket in retryable) {
            if (restaurantDao.claimTicketForPrinting(ticket.id) == 0) continue
            updateNotification("Imprimiendo comanda ${ticket.id.take(8)}…")
            when (val printed = ConfiguredPrint.printKitchen(restaurantDao, printerMac, ticket.payload)) {
                CopyResult.Printed -> {
                    restaurantDao.markTicketPrinted(ticket.id, System.currentTimeMillis())
                    updateNotification("Comanda OK ${ticket.id.take(8)}")
                }
                is CopyResult.Invalid -> {
                    restaurantDao.markTicketFailed(ticket.id, printed.message)
                    updateNotification("Error comanda: ${ticket.id.take(8)}")
                }
                is CopyResult.Failed -> {
                    restaurantDao.markTicketFailed(ticket.id, printed.message)
                    updateNotification("Error comanda: ${ticket.id.take(8)}")
                    break
                }
            }
        }
    }

    private suspend fun printPushed(
        api: PrintJobApi?,
        restaurantDao: RestaurantDao,
        printerMac: String,
        job: PrintJobDto,
    ): Boolean {
        if (job.id in printedJobIds) {
            val acked = api == null || api.acknowledge(job.id, PrintJobAckRequest(true)).isSuccess
            updateNotification(if (acked) "OK ${job.id.take(8)}" else "Impreso, sin confirmar")
            return api != null && !acked
        }

        updateNotification("Imprimiendo ${job.id.take(8)}…")
        when (val printed = dispatch(restaurantDao, printerMac, job)) {
            CopyResult.Printed -> {
                rememberPrinted(job.id)
                val acked = api == null || api.acknowledge(job.id, PrintJobAckRequest(true)).isSuccess
                updateNotification(if (acked) "OK ${job.id.take(8)}" else "Impreso, sin confirmar")
                return api != null && !acked
            }
            is CopyResult.Invalid -> {
                api?.acknowledge(job.id, PrintJobAckRequest(false, printed.message))
                updateNotification("Formato inválido: ${job.id.take(8)}")
                return false
            }
            is CopyResult.Failed -> {
                updateNotification("Error impresora: ${printed.message.take(60)}")
                return true
            }
        }
    }

    private suspend fun dispatch(
        restaurantDao: RestaurantDao,
        printerMac: String,
        job: PrintJobDto,
    ): CopyResult {
        if (job.payloadFormat.equals(PrintJobRenderer.FORMAT_KITCHEN_TICKET_XML, ignoreCase = true)) {
            return ConfiguredPrint.printKitchen(restaurantDao, printerMac, job.payload)
        }
        val logo = invoiceLogo()
        try {
            val bytes = PrintPipeline.renderCloudJob(job, logo).getOrElse {
                return CopyResult.Invalid(it.message ?: "Formato inválido")
            }
            return ConfiguredPrint.printInvoice(restaurantDao, printerMac, bytes)
        } finally {
            logo?.recycle()
        }
    }

    private fun invoiceLogo(): Bitmap? {
        val path = GatewaySettings(this).logoPath
        if (path.isBlank() || !File(path).isFile) return null
        return BitmapFactory.decodeFile(path)
    }

    private fun rememberPrinted(id: String) {
        if (printedJobIds.size >= 300) {
            printedJobIds.remove(printedJobIds.first())
        }
        printedJobIds.add(id)
    }

    private fun stopSelfGracefully() {
        loopJob?.cancel()
        releaseStayAwake()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireStayAwake() {
        if (wakeLock?.isHeld == true) return
        val power = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = power.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HostLite:print-gateway").apply {
            setReferenceCounted(false)
            acquire()
        }
    }

    private fun releaseStayAwake() {
        wakeLock?.let { lock ->
            if (lock.isHeld) lock.release()
        }
        wakeLock = null
    }

    private fun buildNotification(text: String): Notification {
        ensureChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Host Lite")
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
            "Host Lite",
            NotificationManager.IMPORTANCE_LOW,
        )
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        loopJob?.cancel()
        releaseStayAwake()
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

object IncomingPrint {
    val jobs = Channel<PrintJobDto>(Channel.UNLIMITED)

    @Volatile
    var accepting: Boolean = false

    @Volatile
    var onReplay: (() -> Unit)? = null

    fun accept() {
        accepting = true
    }

    fun stop() {
        accepting = false
        while (jobs.tryReceive().isSuccess) {
            // Drop jobs that arrived before the gateway stopped.
        }
    }

    fun offer(job: PrintJobDto) {
        if (!accepting) return
        jobs.trySend(job)
    }

    fun requestReplay() {
        if (accepting) onReplay?.invoke()
    }
}
