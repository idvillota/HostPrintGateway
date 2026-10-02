package com.host.printgateway.ui.order;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a&\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u0007\u001aR\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u0010H\u0007\u001a\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002\u00a8\u0006\u0016"}, d2 = {"OrderedProductCard", "", "item", "Lcom/host/printgateway/data/OrderItemEntity;", "OrderedProductsScreen", "padding", "Landroidx/compose/foundation/layout/PaddingValues;", "tableCode", "", "items", "", "TableMenuScreen", "notice", "cartCount", "", "onOrderedProducts", "Lkotlin/Function0;", "onNewOrder", "onOpenComanda", "quantityLabel", "quantity", "", "app_debug"})
public final class TableEntryScreensKt {
    
    @androidx.compose.runtime.Composable()
    public static final void TableMenuScreen(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.PaddingValues padding, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCode, @org.jetbrains.annotations.NotNull()
    java.lang.String notice, int cartCount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOrderedProducts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNewOrder, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenComanda) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void OrderedProductsScreen(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.PaddingValues padding, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCode, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.OrderItemEntity> items) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OrderedProductCard(com.host.printgateway.data.OrderItemEntity item) {
    }
    
    private static final java.lang.String quantityLabel(double quantity) {
        return null;
    }
}