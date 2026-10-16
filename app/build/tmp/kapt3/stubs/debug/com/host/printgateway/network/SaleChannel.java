package com.host.printgateway.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 $2\u00020\u0001:\u0002$%B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J$\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00112\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003J\u0006\u0010\u0014\u001a\u00020\u000fJr\u0010\u0015\u001a\u00020\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u000f0\u00182\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\u001b2\u001a\b\u0002\u0010\u001c\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u001d\u0012\u0004\u0012\u00020\u000f0\u00182\u0014\b\u0002\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u000f0\u00182\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000f0\u001bH\u0086@\u00a2\u0006\u0002\u0010!J\"\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0003H\u0002R\u000e\u0010\u0007\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/host/printgateway/network/SaleChannel;", "", "baseUrl", "", "token", "deviceId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "announceLock", "client", "Lokhttp3/OkHttpClient;", "pendingReady", "Lcom/host/printgateway/network/SaleChannel$PendingReady;", "socket", "Lokhttp3/WebSocket;", "announcePrinting", "", "ready", "", "fresh", "since", "close", "listen", "Lcom/host/printgateway/network/SaleListenEnd;", "onSale", "Lkotlin/Function1;", "Lcom/host/printgateway/network/RemoteSale;", "onConnected", "Lkotlin/Function0;", "onTablesAvailable", "", "onPrint", "Lcom/host/printgateway/network/PrintJobDto;", "onPrintQueueReady", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendReady", "webSocket", "Companion", "PendingReady", "app_debug"})
public final class SaleChannel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String baseUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String token = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deviceId = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object announceLock = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile okhttp3.WebSocket socket;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.host.printgateway.network.SaleChannel.PendingReady pendingReady;
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat utcFormat = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.network.SaleChannel.Companion Companion = null;
    
    public SaleChannel(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object listen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.network.RemoteSale, kotlin.Unit> onSale, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConnected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<java.lang.String>, kotlin.Unit> onTablesAvailable, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.network.PrintJobDto, kotlin.Unit> onPrint, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPrintQueueReady, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.host.printgateway.network.SaleListenEnd> $completion) {
        return null;
    }
    
    public final void announcePrinting(boolean ready, boolean fresh, @org.jetbrains.annotations.Nullable()
    java.lang.String since) {
    }
    
    private final void sendReady(okhttp3.WebSocket webSocket, boolean fresh, java.lang.String since) {
    }
    
    public final void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006J\u0006\u0010\t\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/host/printgateway/network/SaleChannel$Companion;", "", "()V", "utcFormat", "Ljava/text/SimpleDateFormat;", "streamUrl", "", "baseUrl", "deviceId", "utcNowIso", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String utcNowIso() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String streamUrl(@org.jetbrains.annotations.NotNull()
        java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
        java.lang.String deviceId) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u001f\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001J\t\u0010\u0012\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0013"}, d2 = {"Lcom/host/printgateway/network/SaleChannel$PendingReady;", "", "fresh", "", "since", "", "(ZLjava/lang/String;)V", "getFresh", "()Z", "getSince", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
    static final class PendingReady {
        private final boolean fresh = false;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String since = null;
        
        public PendingReady(boolean fresh, @org.jetbrains.annotations.Nullable()
        java.lang.String since) {
            super();
        }
        
        public final boolean getFresh() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getSince() {
            return null;
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.host.printgateway.network.SaleChannel.PendingReady copy(boolean fresh, @org.jetbrains.annotations.Nullable()
        java.lang.String since) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}