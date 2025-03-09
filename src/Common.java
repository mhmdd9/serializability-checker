import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Common {

    // تابع برای تجزیه رشته زمانبندی به لیستی از عملیات‌ها
    static List<Operation> parseSchedule(String schedule) {
        try {
            return Arrays.stream(schedule.split("[,\\s]+"))
                    .map(String::trim)                    // فضاهای خالی را حذف می‌کند
                    .filter(s -> !s.isEmpty())            // رشته‌های خالی را حذف می‌کند
                    .map(part -> {
                        // تجزیه هر بخش به نوع عمل، شناسه تراکنش و داده
                        char action = part.charAt(0); // اولین کاراکتر نوع عمل است
                        int transIdEnd = part.indexOf('('); // محل پایان شناسه تراکنش
                        int transId = Integer.parseInt(part.substring(1, transIdEnd)); // شناسه تراکنش
                        String dataItem = part.substring(transIdEnd + 1, part.indexOf(')')); // داده
                        return new Operation(action, transId, dataItem); // ایجاد شیء Operation
                    })
                    .collect(Collectors.toList()); // جمع‌آوری عملیات‌ها در لیست
        } catch (Exception e) {
            return null; // در صورت خطا، null برگردانده می‌شود
        }
    }
}
