package com.host.printgateway.network;

/**
 * Thin HTTP client for the Host print-client job queue.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J)\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000e\u0010\u000fJ\u001f\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\t\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0013\u0010\u0014J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0016\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0017"}, d2 = {"Lcom/host/printgateway/network/PrintJobApi;", "", "baseUrl", "", "deviceToken", "(Ljava/lang/String;Ljava/lang/String;)V", "client", "Lcom/host/printgateway/network/ApiClient;", "acknowledge", "Lkotlin/Result;", "", "jobId", "ack", "Lcom/host/printgateway/network/PrintJobAckRequest;", "acknowledge-gIAlu-s", "(Ljava/lang/String;Lcom/host/printgateway/network/PrintJobAckRequest;)Ljava/lang/Object;", "fetchPendingJobs", "", "Lcom/host/printgateway/network/PrintJobDto;", "fetchPendingJobs-d1pmJ48", "()Ljava/lang/Object;", "parseJobs", "body", "app_debug"})
public final class PrintJobApi {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String baseUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deviceToken = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.network.ApiClient client = null;
    
    public PrintJobApi(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceToken) {
        super();
    }
    
    private final java.util.List<com.host.printgateway.network.PrintJobDto> parseJobs(java.lang.String body) {
        return null;
    }
}