package com.host.printgateway.data;

/**
 * Offline leave + sale-channel connect sequences.
 * Keep these separate — order of steps differs on purpose.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0010B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J6\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ6\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u0011"}, d2 = {"Lcom/host/printgateway/data/HostSyncOperations;", "", "()V", "pushThenPull", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult;", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "token", "", "baseUrl", "deviceId", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "(Lcom/host/printgateway/data/PrintGatewayDatabase;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/host/printgateway/data/GatewaySettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reconcileAfterSaleChannelConnect", "", "OfflineSyncResult", "app_release"})
public final class HostSyncOperations {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.HostSyncOperations INSTANCE = null;
    
    private HostSyncOperations() {
        super();
    }
    
    /**
     * Leave offline mode: upload local work, then refresh from HOST.
     * Sequence: pending orders → catalog → close free → release settled → catch-up sales.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object pushThenPull(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.host.printgateway.data.HostSyncOperations.OfflineSyncResult> $completion) {
        return null;
    }
    
    /**
     * After sale WebSocket connects.
     * Sequence: register device → catalog → close free → release settled → upload pending → catch-up.
     * @return true if session is unauthorized
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object reconcileAfterSaleChannelConnect(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0003\u0002\u0003\u0004\u0082\u0001\u0003\u0005\u0006\u0007\u00a8\u0006\b"}, d2 = {"Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult;", "", "Done", "Failed", "Unauthorized", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult$Done;", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult$Failed;", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult$Unauthorized;", "app_release"})
    public static abstract interface OfflineSyncResult {
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult$Done;", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
        public static final class Done implements com.host.printgateway.data.HostSyncOperations.OfflineSyncResult {
            @org.jetbrains.annotations.NotNull()
            public static final com.host.printgateway.data.HostSyncOperations.OfflineSyncResult.Done INSTANCE = null;
            
            private Done() {
                super();
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult$Failed;", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_release"})
        public static final class Failed implements com.host.printgateway.data.HostSyncOperations.OfflineSyncResult {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;
            
            public Failed(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.host.printgateway.data.HostSyncOperations.OfflineSyncResult.Failed copy(@org.jetbrains.annotations.NotNull()
            java.lang.String message) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult$Unauthorized;", "Lcom/host/printgateway/data/HostSyncOperations$OfflineSyncResult;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
        public static final class Unauthorized implements com.host.printgateway.data.HostSyncOperations.OfflineSyncResult {
            @org.jetbrains.annotations.NotNull()
            public static final com.host.printgateway.data.HostSyncOperations.OfflineSyncResult.Unauthorized INSTANCE = null;
            
            private Unauthorized() {
                super();
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
}