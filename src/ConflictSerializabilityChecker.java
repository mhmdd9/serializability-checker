/*
این برنامه یک رشته ورودی که نشان‌دهنده زمانبندی تراکنش‌ها است را دریافت می‌کند
و بررسی می‌کند که آیا زمانبندی وارد شده پی‌درپی پذیر (Conflict Serializable) است یا خیر.
*/

import java.util.*;
import java.util.stream.Collectors;

public class ConflictSerializabilityChecker {

    // تابع اصلی برنامه
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("رشته زمانبندی را وارد کنید (مثال: R1(A),W2(B),...):");
        String schedule = scanner.nextLine();

        // تجزیه رشته زمانبندی به لیستی از عملیات‌ها
        List<Operation> operations = Common.parseSchedule(schedule);

        // اگر رشته ورودی نامعتبر باشد، برنامه پایان می‌یابد
        if (operations == null) {
            System.out.println("فرمت ورودی نامعتبر است.");
            return;
        }

        // بررسی پی در پی پذیر بودن
        boolean isConflictSerializable = isConflictSerializable(operations);
        System.out.println("آیا زمانبندی به صورت Csr پی در پی پذیر است؟ " + isConflictSerializable);
    }

    // تابع برای بررسی پی در پی بودن
    private static boolean isConflictSerializable(List<Operation> operations) {
        // استخراج شناسه‌های تراکنش‌ها
        Set<Integer> transactions = operations.stream()
                .map(op -> op.transId)
                .collect(Collectors.toSet());

        // ایجاد گراف اولویت با استفاده از لیست مجاورت
        Map<Integer, Set<Integer>> adjacencyList = new HashMap<>();
        // ایجاد درجه ورودی برای هر تراکنش
        Map<Integer, Integer> inDegree = new HashMap<>();

        // مقداردهی اولیه گراف و درجه‌های ورودی
        for (int trans : transactions) {
            adjacencyList.put(trans, new HashSet<>()); // لیست مجاورت خالی
            inDegree.put(trans, 0); // درجه ورودی صفر
        }

        // بررسی تضاد بین عملیات‌ها و ساخت گراف اولویت
        for (int i = 0; i < operations.size(); i++) {
            Operation oi = operations.get(i);
            for (int j = i + 1; j < operations.size(); j++) {
                Operation oj = operations.get(j);
                // اگر دو عملیات با هم تضاد داشته باشند و متعلق به تراکنش‌های مختلف باشند
                if (conflict(oi, oj) && oi.transId != oj.transId) {
                    int ti = oi.transId;
                    int tj = oj.transId;
                    // اگر یال بین ti و tj وجود نداشته باشد، آن را اضافه می‌کنیم
                    if (!adjacencyList.get(ti).contains(tj)) {
                        adjacencyList.get(ti).add(tj);
                        inDegree.put(tj, inDegree.get(tj) + 1); // افزایش درجه ورودی tj
                    }
                }
            }
        }

        // استفاده از الگوریتم کاهن برای تشخیص چرخه
        Queue<Integer> queue = new LinkedList<>();
        // اضافه کردن تراکنش‌هایی با درجه ورودی صفر به صف
        for (int trans : transactions) {
            if (inDegree.get(trans) == 0) {
                queue.add(trans);
            }
        }

        int processedCount = 0; // شمارنده تراکنش‌های پردازش شده
        while (!queue.isEmpty()) {
            int u = queue.poll(); // حذف تراکنش از صف
            processedCount++;
            // کاهش درجه ورودی همسایه‌ها و اضافه کردن آن‌ها به صف اگر درجه ورودی صفر شود
            for (int v : adjacencyList.get(u)) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    queue.add(v);
                }
            }
        }

        // اگر تعداد تراکنش‌های پردازش شده با تعداد کل تراکنش‌ها برابر باشد، چرخه‌ای وجود ندارد
        return processedCount == transactions.size();
    }

    // تابع برای بررسی Conflict بین دو عملیات
    private static boolean conflict(Operation o1, Operation o2) {
        // دو عملیات تضاد دارند اگر روی یک داده باشند و حداقل یکی از آن‌ها نوشتن باشد
        return o1.dataItem.equals(o2.dataItem) && (o1.action == 'W' || o2.action == 'W');
    }
}