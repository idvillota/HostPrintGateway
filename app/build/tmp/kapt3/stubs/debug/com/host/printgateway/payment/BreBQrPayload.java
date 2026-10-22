package com.host.printgateway.payment;

/**
 * Same merchant QR text HOST builds for a Bre-B transfer.
 * The amount already includes tax and tip.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\tj\u0002`\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004H\u0002JM\u0010\r\u001a\u00020\u00072\n\u0010\b\u001a\u00060\tj\u0002`\n2\u0006\u0010\u000b\u001a\u00020\u00042*\u0010\u000e\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u00100\u000f\"\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0010H\u0002\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0004J\u000e\u0010\u0018\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004J \u0010\u0019\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0004H\u0002J\u001a\u0010\u001d\u001a\u0004\u0018\u00010\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0010\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0004H\u0002J\u0010\u0010 \u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/host/printgateway/payment/BreBQrPayload;", "", "()V", "MISSING_KEY", "", "MISSING_PLACE", "append", "", "body", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "tag", "value", "appendTemplate", "fields", "", "Lkotlin/Pair;", "(Ljava/lang/StringBuilder;Ljava/lang/String;[Lkotlin/Pair;)V", "build", "profile", "Lcom/host/printgateway/payment/BreBProfile;", "amount", "", "reference", "crc16Ccitt", "merchantText", "maxLength", "", "label", "optionalCode", "requireKey", "key", "requireReference", "app_debug"})
public final class BreBQrPayload {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MISSING_KEY = "Configura la llave Bre-B del local antes de mostrar el QR.";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MISSING_PLACE = "Configura el nombre comercial y la ciudad del local para generar el QR.";
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.payment.BreBQrPayload INSTANCE = null;
    
    private BreBQrPayload() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String build(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.payment.BreBProfile profile, double amount, @org.jetbrains.annotations.NotNull()
    java.lang.String reference) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String crc16Ccitt(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    private final void append(java.lang.StringBuilder body, java.lang.String tag, java.lang.String value) {
    }
    
    private final void appendTemplate(java.lang.StringBuilder body, java.lang.String tag, kotlin.Pair<java.lang.String, java.lang.String>... fields) {
    }
    
    private final java.lang.String requireKey(java.lang.String key) {
        return null;
    }
    
    private final java.lang.String requireReference(java.lang.String reference) {
        return null;
    }
    
    private final java.lang.String optionalCode(java.lang.String value, int maxLength) {
        return null;
    }
    
    private final java.lang.String merchantText(java.lang.String value, int maxLength, java.lang.String label) {
        return null;
    }
}