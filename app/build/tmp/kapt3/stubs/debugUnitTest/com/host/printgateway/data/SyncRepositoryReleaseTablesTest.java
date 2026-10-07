package com.host.printgateway.data;

/**
 * End-to-end settle path: HOST table summaries → free ids → local account close.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0005\u001a\u00060\u0006j\u0002`\u0007H\u0007J\b\u0010\b\u001a\u00020\u0006H\u0007J\b\u0010\t\u001a\u00020\u0006H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/host/printgateway/data/SyncRepositoryReleaseTablesTest;", "", "()V", "server", "Lokhttp3/mockwebserver/MockWebServer;", "releaseTablesSettledOnHost_fetches_summaries_and_frees_local_table", "", "Lkotlinx/coroutines/test/TestResult;", "setUp", "tearDown", "app_debugUnitTest"})
public final class SyncRepositoryReleaseTablesTest {
    private okhttp3.mockwebserver.MockWebServer server;
    
    public SyncRepositoryReleaseTablesTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setUp() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
    
    @org.junit.Test()
    public final void releaseTablesSettledOnHost_fetches_summaries_and_frees_local_table() {
    }
}