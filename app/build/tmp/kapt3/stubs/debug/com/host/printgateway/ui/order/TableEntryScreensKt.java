package com.host.printgateway.ui.order;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0000\u001aF\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u001a,\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a\u0010\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a&\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00072\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003H\u0007\u001ad\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0010\b\u0002\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\tH\u0007\u001a\u0010\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020!H\u0002\u00a8\u0006\""}, d2 = {"ChangeTableDialog", "", "tables", "", "Lcom/host/printgateway/data/DiningTableEntity;", "occupiedTableIds", "", "", "onDismiss", "Lkotlin/Function0;", "onPick", "Lkotlin/Function1;", "MergeTablesDialog", "targetCode", "onConfirm", "OrderedProductCard", "item", "Lcom/host/printgateway/data/OrderItemEntity;", "OrderedProductsScreen", "padding", "Landroidx/compose/foundation/layout/PaddingValues;", "tableCode", "items", "TableMenuScreen", "notice", "cartCount", "", "onOrderedProducts", "onNewOrder", "onOpenComanda", "onChangeTable", "quantityLabel", "quantity", "", "app_debug"})
public final class TableEntryScreensKt {
    
    @androidx.compose.runtime.Composable()
    public static final void TableMenuScreen(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.PaddingValues padding, @org.jetbrains.annotations.NotNull()
    java.lang.String tableCode, @org.jetbrains.annotations.NotNull()
    java.lang.String notice, int cartCount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOrderedProducts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNewOrder, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenComanda, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onChangeTable) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ChangeTableDialog(@org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.DiningTableEntity> tables, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> occupiedTableIds, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.data.DiningTableEntity, kotlin.Unit> onPick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void MergeTablesDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String targetCode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConfirm, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
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