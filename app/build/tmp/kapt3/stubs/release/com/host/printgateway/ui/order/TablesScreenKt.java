package com.host.printgateway.ui.order;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000N\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a&\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0003\u001at\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00102\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00130\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0007\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0003\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0002\u00a8\u0006\u001c"}, d2 = {"OccupiedLabel", "Landroidx/compose/ui/graphics/Color;", "J", "OccupiedTable", "TableCard", "", "table", "Lcom/host/printgateway/data/DiningTableEntity;", "occupied", "", "onClick", "Lkotlin/Function0;", "TablesScreen", "padding", "Landroidx/compose/foundation/layout/PaddingValues;", "tables", "", "syncing", "notice", "", "cartCount", "", "occupiedTableIds", "", "onSync", "onTable", "Lkotlin/Function1;", "onOpenComanda", "app_release"})
public final class TablesScreenKt {
    private static final long OccupiedTable = 0L;
    private static final long OccupiedLabel = 0L;
    
    @androidx.compose.runtime.Composable()
    public static final void TablesScreen(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.PaddingValues padding, @org.jetbrains.annotations.NotNull()
    java.util.List<com.host.printgateway.data.DiningTableEntity> tables, boolean syncing, @org.jetbrains.annotations.NotNull()
    java.lang.String notice, int cartCount, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> occupiedTableIds, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSync, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.host.printgateway.data.DiningTableEntity, kotlin.Unit> onTable, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenComanda) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TableCard(com.host.printgateway.data.DiningTableEntity table, boolean occupied, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}