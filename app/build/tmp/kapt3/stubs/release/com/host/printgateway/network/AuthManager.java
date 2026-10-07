package com.host.printgateway.network;

/**
 * Gestiona las credenciales y la sesión del backend.
 *
 * Las credenciales se almacenan cifradas mediante Android Keystore.
 *
 * El accessToken recibido del backend se coloca en GatewaySettings
 * porque las demás APIs del proyecto ya lo consumen desde allí.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 %2\u00020\u0001:\u0001%B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fH\u0002J\u0010\u0010\u000f\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013J\u0006\u0010\u0014\u001a\u00020\u0015J9\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\f2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eJ!\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b \u0010!J\u0006\u0010\"\u001a\u00020#J\u001e\u0010$\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\fR\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006&"}, d2 = {"Lcom/host/printgateway/network/AuthManager;", "", "context", "Landroid/content/Context;", "settings", "Lcom/host/printgateway/data/GatewaySettings;", "(Landroid/content/Context;Lcom/host/printgateway/data/GatewaySettings;)V", "appContext", "kotlin.jvm.PlatformType", "prefs", "Landroid/content/SharedPreferences;", "currentToken", "", "decrypt", "value", "encrypt", "getOrCreateSecretKey", "Ljavax/crypto/SecretKey;", "getSavedCredentials", "Lcom/host/printgateway/network/SavedCredentials;", "hasSavedCredentials", "", "login", "Lkotlin/Result;", "Lcom/host/printgateway/network/LoginResponse;", "baseUrl", "email", "password", "tenantSlug", "login-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "loginWithSavedCredentials", "loginWithSavedCredentials-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "logout", "", "saveCredentials", "Companion", "app_release"})
public final class AuthManager {
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.data.GatewaySettings settings = null;
    private final android.content.Context appContext = null;
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "host_print_auth_secure";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_EMAIL = "email";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PASSWORD = "password";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_TENANT_SLUG = "tenant_slug";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ALIAS = "host_print_gateway_auth_key";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ANDROID_KEYSTORE = "AndroidKeyStore";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.network.AuthManager.Companion Companion = null;
    
    public AuthManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.GatewaySettings settings) {
        super();
    }
    
    /**
     * Guarda las credenciales cifradas.
     */
    public final void saveCredentials(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String tenantSlug) {
    }
    
    /**
     * Recupera las credenciales almacenadas.
     *
     * Si no existen o no pueden descifrarse, devuelve null.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.host.printgateway.network.SavedCredentials getSavedCredentials() {
        return null;
    }
    
    /**
     * Indica si existen credenciales guardadas.
     */
    public final boolean hasSavedCredentials() {
        return false;
    }
    
    /**
     * Devuelve el token actualmente guardado.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String currentToken() {
        return null;
    }
    
    /**
     * Cierra la sesión y elimina las credenciales almacenadas.
     */
    public final void logout() {
    }
    
    private final javax.crypto.SecretKey getOrCreateSecretKey() {
        return null;
    }
    
    private final java.lang.String encrypt(java.lang.String value) {
        return null;
    }
    
    private final java.lang.String decrypt(java.lang.String value) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/host/printgateway/network/AuthManager$Companion;", "", "()V", "ANDROID_KEYSTORE", "", "GCM_TAG_LENGTH_BITS", "", "KEY_ALIAS", "KEY_EMAIL", "KEY_PASSWORD", "KEY_TENANT_SLUG", "PREFS_NAME", "TRANSFORMATION", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}