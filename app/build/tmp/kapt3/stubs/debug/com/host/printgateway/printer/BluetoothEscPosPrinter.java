package com.host.printgateway.printer;

/**
 * Sends raw ESC/POS bytes to a paired Bluetooth SPP printer.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0002J$\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\f2\u0006\u0010\r\u001a\u00020\u000eH\u0087@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0012"}, d2 = {"Lcom/host/printgateway/printer/BluetoothEscPosPrinter;", "", "macAddress", "", "(Ljava/lang/String;)V", "sppUuid", "Ljava/util/UUID;", "closeQuietly", "", "socket", "Landroid/bluetooth/BluetoothSocket;", "print", "Lkotlin/Result;", "bytes", "", "print-gIAlu-s", "([BLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class BluetoothEscPosPrinter {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String macAddress = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.UUID sppUuid = null;
    
    /**
     * Keep the radio up until the printer has taken the bytes.
     */
    @java.lang.Deprecated()
    public static final long DRAIN_BEFORE_CLOSE_MS = 400L;
    
    /**
     * Let the printer finish before the next ticket opens Bluetooth.
     */
    @java.lang.Deprecated()
    public static final long SETTLE_BEFORE_NEXT_MS = 400L;
    @org.jetbrains.annotations.NotNull()
    private static final com.host.printgateway.printer.BluetoothEscPosPrinter.Companion Companion = null;
    
    public BluetoothEscPosPrinter(@org.jetbrains.annotations.NotNull()
    java.lang.String macAddress) {
        super();
    }
    
    private final void closeQuietly(android.bluetooth.BluetoothSocket socket) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/host/printgateway/printer/BluetoothEscPosPrinter$Companion;", "", "()V", "DRAIN_BEFORE_CLOSE_MS", "", "SETTLE_BEFORE_NEXT_MS", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
    }
}