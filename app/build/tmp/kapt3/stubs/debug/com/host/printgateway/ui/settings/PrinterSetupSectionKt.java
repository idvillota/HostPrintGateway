package com.host.printgateway.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aP\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001aT\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u00052\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u0013H\u0003\u001a@\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00062\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00132\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0013H\u0007\u00a8\u0006\u001c"}, d2 = {"PrinterCard", "", "printer", "Lcom/host/printgateway/data/LocalPrinterEntity;", "categoryNames", "", "", "testing", "", "onEdit", "Lkotlin/Function0;", "onTest", "onDelete", "PrinterEditor", "draft", "Lcom/host/printgateway/ui/settings/PrinterDraft;", "types", "Lcom/host/printgateway/data/ProductTypeEntity;", "onChange", "Lkotlin/Function1;", "onCancel", "onSave", "PrinterSetupSection", "database", "Lcom/host/printgateway/data/PrintGatewayDatabase;", "fallbackMac", "onMirrorMac", "onStatus", "app_debug"})
public final class PrinterSetupSectionKt {
    
    @androidx.compose.runtime.Composable()
    public static final void PrinterSetupSection(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.data.PrintGatewayDatabase database, @org.jetbrains.annotations.NotNull()
    java.lang.String fallbackMac, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onMirrorMac, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStatus) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PrinterCard(com.host.printgateway.data.LocalPrinterEntity printer, java.util.List<java.lang.String> categoryNames, boolean testing, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onTest, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PrinterEditor(com.host.printgateway.ui.settings.PrinterDraft draft, java.util.List<com.host.printgateway.data.ProductTypeEntity> types, kotlin.jvm.functions.Function1<? super com.host.printgateway.ui.settings.PrinterDraft, kotlin.Unit> onChange, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel, kotlin.jvm.functions.Function1<? super com.host.printgateway.ui.settings.PrinterDraft, kotlin.Unit> onSave) {
    }
}