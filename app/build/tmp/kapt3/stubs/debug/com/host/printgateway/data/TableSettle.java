package com.host.printgateway.data;

/**
 * Pure helpers for freeing local tables when HOST reports no open account.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a8\u0006\t"}, d2 = {"Lcom/host/printgateway/data/TableSettle;", "", "()V", "freeHostTableIds", "", "", "summaries", "", "Lcom/host/printgateway/network/RemoteTableAccount;", "app_debug"})
public final class TableSettle {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.TableSettle INSTANCE = null;
    
    private TableSettle() {
        super();
    }
    
    /**
     * Table ids HOST considers free (no open order), normalized for local matching.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> freeHostTableIds(@org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.network.RemoteTableAccount> summaries) {
        return null;
    }
}