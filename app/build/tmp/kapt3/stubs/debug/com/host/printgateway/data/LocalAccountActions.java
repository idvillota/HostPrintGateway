package com.host.printgateway.data;

/**
 * Shared local table/account mutations used by order payment and sync.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0086@\u00a2\u0006\u0002\u0010\nJ$\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/host/printgateway/data/LocalAccountActions;", "", "()V", "closeAccountsOnFreeTables", "", "dao", "Lcom/host/printgateway/data/RestaurantDao;", "freeIds", "", "", "(Lcom/host/printgateway/data/RestaurantDao;Ljava/util/Set;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "freeSettledTables", "", "tableIds", "", "(Lcom/host/printgateway/data/RestaurantDao;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class LocalAccountActions {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.data.LocalAccountActions INSTANCE = null;
    
    private LocalAccountActions() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object freeSettledTables(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.RestaurantDao dao, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> tableIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object closeAccountsOnFreeTables(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.RestaurantDao dao, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> freeIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
}