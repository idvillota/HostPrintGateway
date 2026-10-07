package com.host.printgateway.service;

/**
 * Prints local tickets created after start, then prints comandas pushed by HOST.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 22\u00020\u0001:\u00012B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0003J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\tH\u0002J\b\u0010\u0014\u001a\u00020\u0010H\u0002J\u0014\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\"\u0010\u001a\u001a\u00020\u001b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u001bH\u0016J&\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\t2\u0006\u0010\"\u001a\u00020#H\u0082@\u00a2\u0006\u0002\u0010$J(\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010(2\u0006\u0010!\u001a\u00020\t2\u0006\u0010)\u001a\u00020*H\u0082@\u00a2\u0006\u0002\u0010+J\b\u0010,\u001a\u00020\u0010H\u0002J\u0010\u0010-\u001a\u00020\u00102\u0006\u0010.\u001a\u00020\tH\u0002J\b\u0010/\u001a\u00020\u0010H\u0002J\b\u00100\u001a\u00020\u0010H\u0002J\u0010\u00101\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\tH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0018\u00010\rR\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/host/printgateway/service/PrintGatewayService;", "Landroid/app/Service;", "()V", "loopJob", "Lkotlinx/coroutines/Job;", "printMutex", "Lkotlinx/coroutines/sync/Mutex;", "printedJobIds", "Ljava/util/LinkedHashSet;", "", "scope", "Lkotlinx/coroutines/CoroutineScope;", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "Landroid/os/PowerManager;", "acquireStayAwake", "", "buildNotification", "Landroid/app/Notification;", "text", "ensureChannel", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onDestroy", "onStartCommand", "", "flags", "startId", "printLocalTickets", "restaurantDao", "Lcom/host/printgateway/data/RestaurantDao;", "printerMac", "startedAt", "", "(Lcom/host/printgateway/data/RestaurantDao;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "printPushed", "", "api", "Lcom/host/printgateway/network/PrintJobApi;", "job", "Lcom/host/printgateway/network/PrintJobDto;", "(Lcom/host/printgateway/network/PrintJobApi;Ljava/lang/String;Lcom/host/printgateway/network/PrintJobDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "releaseStayAwake", "rememberPrinted", "id", "startGateway", "stopSelfGracefully", "updateNotification", "Companion", "app_debug"})
public final class PrintGatewayService extends android.app.Service {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex printMutex = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.LinkedHashSet<java.lang.String> printedJobIds = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job loopJob;
    @org.jetbrains.annotations.Nullable()
    private android.os.PowerManager.WakeLock wakeLock;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.host.printgateway.STOP";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "host_print_gateway";
    private static final int NOTIFICATION_ID = 1001;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.service.PrintGatewayService.Companion Companion = null;
    
    public PrintGatewayService() {
        super();
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startGateway() {
    }
    
    private final java.lang.Object printLocalTickets(com.host.printgateway.data.RestaurantDao restaurantDao, java.lang.String printerMac, long startedAt, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object printPushed(com.host.printgateway.network.PrintJobApi api, java.lang.String printerMac, com.host.printgateway.network.PrintJobDto job, kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final void rememberPrinted(java.lang.String id) {
    }
    
    private final void stopSelfGracefully() {
    }
    
    @android.annotation.SuppressLint(value = {"WakelockTimeout"})
    private final void acquireStayAwake() {
    }
    
    private final void releaseStayAwake() {
    }
    
    private final android.app.Notification buildNotification(java.lang.String text) {
        return null;
    }
    
    private final void updateNotification(java.lang.String text) {
    }
    
    private final void ensureChannel() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/host/printgateway/service/PrintGatewayService$Companion;", "", "()V", "ACTION_STOP", "", "CHANNEL_ID", "NOTIFICATION_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}