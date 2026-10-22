package com.host.printgateway.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/host/printgateway/ui/SessionRenewalNotifier;", "", "()V", "CHANNEL_ID", "", "EXTRA_RENEW_SESSION", "NOTIFICATION_ID", "", "show", "", "context", "Landroid/content/Context;", "app_debug"})
public final class SessionRenewalNotifier {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_RENEW_SESSION = "renew_session";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "host_session";
    private static final int NOTIFICATION_ID = 42;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.ui.SessionRenewalNotifier INSTANCE = null;
    
    private SessionRenewalNotifier() {
        super();
    }
    
    public final void show(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}