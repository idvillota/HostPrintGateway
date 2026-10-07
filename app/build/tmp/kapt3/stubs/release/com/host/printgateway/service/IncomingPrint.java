package com.host.printgateway.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\n\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0015\u001a\u00020\u0010J\u000e\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u000bJ\u0006\u0010\u0018\u001a\u00020\u0010J\u0006\u0010\u0019\u001a\u00020\u0010R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\"\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001a"}, d2 = {"Lcom/host/printgateway/service/IncomingPrint;", "", "()V", "accepting", "", "getAccepting", "()Z", "setAccepting", "(Z)V", "jobs", "Lkotlinx/coroutines/channels/Channel;", "Lcom/host/printgateway/network/PrintJobDto;", "getJobs", "()Lkotlinx/coroutines/channels/Channel;", "onReplay", "Lkotlin/Function0;", "", "getOnReplay", "()Lkotlin/jvm/functions/Function0;", "setOnReplay", "(Lkotlin/jvm/functions/Function0;)V", "accept", "offer", "job", "requestReplay", "stop", "app_release"})
public final class IncomingPrint {
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.channels.Channel<com.host.printgateway.network.PrintJobDto> jobs = null;
    @kotlin.jvm.Volatile()
    private static volatile boolean accepting = false;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile kotlin.jvm.functions.Function0<kotlin.Unit> onReplay;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.service.IncomingPrint INSTANCE = null;
    
    private IncomingPrint() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.channels.Channel<com.host.printgateway.network.PrintJobDto> getJobs() {
        return null;
    }
    
    public final boolean getAccepting() {
        return false;
    }
    
    public final void setAccepting(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnReplay() {
        return null;
    }
    
    public final void setOnReplay(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    public final void accept() {
    }
    
    public final void stop() {
    }
    
    public final void offer(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.network.PrintJobDto job) {
    }
    
    public final void requestReplay() {
    }
}