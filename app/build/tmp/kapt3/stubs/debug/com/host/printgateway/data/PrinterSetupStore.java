package com.host.printgateway.data;

/**
 * Phone-owned printers. Catalog sync replaces HOST stations and must not touch these rows.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ$\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u000b\u001a\u00020\bH\u0080@\u00a2\u0006\u0004\b\u0019\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/host/printgateway/data/PrinterSetupStore;", "", "dao", "Lcom/host/printgateway/data/RestaurantDao;", "(Lcom/host/printgateway/data/RestaurantDao;)V", "delete", "", "printerId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ensureLegacy", "fallbackMac", "load", "Lcom/host/printgateway/data/PrinterSetup;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "mirrorMac", "save", "printer", "Lcom/host/printgateway/data/LocalPrinterEntity;", "categoryIds", "", "(Lcom/host/printgateway/data/LocalPrinterEntity;Ljava/util/Set;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "targets", "", "Lcom/host/printgateway/data/PrinterTarget;", "targets$app_debug", "app_debug"})
public final class PrinterSetupStore {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.RestaurantDao dao = null;
    
    public PrinterSetupStore(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.RestaurantDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object ensureLegacy(@org.jetbrains.annotations.NotNull()
    java.lang.String fallbackMac, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object targets$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String fallbackMac, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.host.printgateway.data.PrinterTarget>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object load(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.host.printgateway.data.PrinterSetup> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object save(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.LocalPrinterEntity printer, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> categoryIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    java.lang.String printerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object mirrorMac(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}