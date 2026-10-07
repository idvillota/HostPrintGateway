package com.host.printgateway.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\u001f\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eJ\u001f\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0011\u0010\u000eJ\u001f\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000b0\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0014\u0010\u000eJ\u0019\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0017\u0010\u000eJ\u001f\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u000b0\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u000eJ\u001f\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u000b0\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u000eJ\u001e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000b2\u0006\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\"H\u0002J\u0019\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b%\u0010\u000eJC\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\'0\u000b0\n\"\u0004\b\u0000\u0010\'2\u0006\u0010(\u001a\u00020\u00032\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u0002H\'0*H\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b+\u0010,J\u001b\u0010-\u001a\u0004\u0018\u00010.*\u00020\"2\u0006\u0010/\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\u00100R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00061"}, d2 = {"Lcom/host/printgateway/network/CatalogApi;", "", "baseUrl", "", "token", "(Ljava/lang/String;Ljava/lang/String;)V", "client", "Lcom/host/printgateway/network/ApiClient;", "pingHttp", "fetchIngredients", "Lkotlin/Result;", "", "Lcom/host/printgateway/network/RemoteIngredient;", "fetchIngredients-d1pmJ48", "()Ljava/lang/Object;", "fetchPrinters", "Lcom/host/printgateway/network/RemotePrinter;", "fetchPrinters-d1pmJ48", "fetchProductTypes", "Lcom/host/printgateway/network/RemoteProductType;", "fetchProductTypes-d1pmJ48", "fetchProducts", "Lcom/host/printgateway/network/RemoteProductPage;", "fetchProducts-d1pmJ48", "fetchTableAccounts", "Lcom/host/printgateway/network/RemoteTableAccount;", "fetchTableAccounts-d1pmJ48", "fetchTables", "Lcom/host/printgateway/network/RemoteTable;", "fetchTables-d1pmJ48", "parseProductIngredients", "Lcom/host/printgateway/network/RemoteProductIngredient;", "productId", "item", "Lorg/json/JSONObject;", "ping", "", "ping-d1pmJ48", "request", "T", "path", "mapper", "Lkotlin/Function1;", "request-gIAlu-s", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "optNullableDouble", "", "name", "(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/Double;", "app_debug"})
public final class CatalogApi {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String baseUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String token = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.network.ApiClient client = null;
    @org.jetbrains.annotations.NotNull()
    private final com.host.printgateway.network.ApiClient pingHttp = null;
    
    public CatalogApi(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String token) {
        super();
    }
    
    private final java.util.List<com.host.printgateway.network.RemoteProductIngredient> parseProductIngredients(java.lang.String productId, org.json.JSONObject item) {
        return null;
    }
    
    private final java.lang.Double optNullableDouble(org.json.JSONObject $this$optNullableDouble, java.lang.String name) {
        return null;
    }
}