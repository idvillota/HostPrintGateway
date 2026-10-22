package com.host.printgateway.network;

/**
 * Start of the print queue for the current gateway session. Survives a socket reconnect.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nJ\u0006\u0010\u0012\u001a\u00020\u0010R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/host/printgateway/network/PrintSession;", "", "()V", "freshSent", "", "getFreshSent", "()Z", "setFreshSent", "(Z)V", "sinceIso", "", "getSinceIso", "()Ljava/lang/String;", "setSinceIso", "(Ljava/lang/String;)V", "arm", "", "since", "clear", "app_debug"})
public final class PrintSession {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile java.lang.String sinceIso;
    @kotlin.jvm.Volatile()
    private static volatile boolean freshSent = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.network.PrintSession INSTANCE = null;
    
    private PrintSession() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSinceIso() {
        return null;
    }
    
    public final void setSinceIso(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final boolean getFreshSent() {
        return false;
    }
    
    public final void setFreshSent(boolean p0) {
    }
    
    public final void arm(@org.jetbrains.annotations.NotNull()
    java.lang.String since) {
    }
    
    public final void clear() {
    }
}