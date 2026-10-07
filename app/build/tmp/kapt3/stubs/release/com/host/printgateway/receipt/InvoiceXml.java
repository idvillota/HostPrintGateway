package com.host.printgateway.receipt;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010\u0007\u001a\u00020\u0004H\u0002J,\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eJ \u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J\u000e\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0016\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0017"}, d2 = {"Lcom/host/printgateway/receipt/InvoiceXml;", "", "()V", "formatQuantity", "", "quantity", "", "now", "print", "Lkotlin/Result;", "", "xml", "printerMac", "print-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "text", "serializer", "Lorg/xmlpull/v1/XmlSerializer;", "tag", "value", "write", "draft", "Lcom/host/printgateway/receipt/InvoiceDraft;", "app_release"})
public final class InvoiceXml {
    @org.jetbrains.annotations.NotNull()
    public static final com.host.printgateway.receipt.InvoiceXml INSTANCE = null;
    
    private InvoiceXml() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String write(@org.jetbrains.annotations.NotNull()
    com.host.printgateway.receipt.InvoiceDraft draft) {
        return null;
    }
    
    private final void text(org.xmlpull.v1.XmlSerializer serializer, java.lang.String tag, java.lang.String value) {
    }
    
    private final java.lang.String formatQuantity(double quantity) {
        return null;
    }
    
    private final java.lang.String now() {
        return null;
    }
}