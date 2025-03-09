// کلاس Operation برای نگهداری اطلاعات هر عملیات
public class Operation {
    char action;       // نوع عمل: 'R' برای خواندن و 'W' برای نوشتن
    int transId;       // شناسه تراکنش
    String dataItem;   // داده‌ای که روی آن عمل انجام می‌شود

    public Operation(char action, int transId, String dataItem) {
        this.action = action;
        this.transId = transId;
        this.dataItem = dataItem;
    }
}