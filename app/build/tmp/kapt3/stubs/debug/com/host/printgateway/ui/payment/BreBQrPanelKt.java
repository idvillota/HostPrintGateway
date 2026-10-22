package com.host.printgateway.ui.payment;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0003\u001a6\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00052\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u0010\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u000fH\u0003\u001a\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0011\u001a\u00020\u000fH\u0002\u00a8\u0006\u0014"}, d2 = {"BreBQrCard", "", "amount", "", "shares", "", "profile", "Lcom/host/printgateway/payment/BreBProfile;", "BreBQrList", "due", "splitMode", "Lcom/host/printgateway/ui/payment/SplitMode;", "equalParts", "customParts", "", "", "QrImage", "payload", "qrBitmap", "Landroid/graphics/Bitmap;", "app_debug"})
public final class BreBQrPanelKt {
    
    @androidx.compose.runtime.Composable()
    public static final void BreBQrList(double due, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.ui.payment.SplitMode splitMode, int equalParts, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> customParts, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.payment.BreBProfile profile) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BreBQrCard(double amount, int shares, com.host.printgateway.payment.BreBProfile profile) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QrImage(java.lang.String payload) {
    }
    
    private static final android.graphics.Bitmap qrBitmap(java.lang.String payload) {
        return null;
    }
}