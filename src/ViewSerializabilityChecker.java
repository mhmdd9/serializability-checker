/*
این برنامه یک رشته ورودی که نشان‌دهنده زمانبندی تراکنش‌ها است را دریافت می‌کند
و بررسی می‌کند که آیا زمانبندی وارد شده پی‌درپی پذیر (View Serializable) است یا خیر.
استفاده از الگوریتم: تولید تمامی ترتیب‌های سریال ممکن و بررسی تطابق قوانین دیدگاهی (نگاشت خواندن و آخرین نوشتن).
*/

import java.util.*;

public class ViewSerializabilityChecker {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("لطفاً زمانبندی تراکنش‌ها را وارد کنید (مثال: R1(x),W2(x),R2(x),W1(x)):");
//        String scheduleStr = scanner.nextLine();
        String schedule = scanner.nextLine();

        // تقسیم رشته ورودی به عملیات‌ها (با فاصله یا ویرگول جدا شده)
//        String[] tokens = scheduleStr.split("[,\\s]+");
//        List<Operation> schedule = new ArrayList<>();
        List<Operation> operations = Common.parseSchedule(schedule);

        // اگر رشته ورودی نامعتبر باشد، برنامه پایان می‌یابد
        if (operations == null) {
            System.out.println("فرمت ورودی نامعتبر است.");
            return;
        }
        
        // استخراج شناسه تراکنش‌ها
        Set<Integer> transactions = new HashSet<>();
        for(Operation op: operations) {
            transactions.add(op.transId);
        }
        List<Integer> transList = new ArrayList<>(transactions);
        
        // محاسبه نگاشت خواندن: برای هر عملیات خواندن، منبع خوانده شده را مشخص می‌کند.
        // اگر پیش از خواندن، عملیاتی نوشتاری برای آن آیتم وجود نداشته باشد، منبع "init" در نظر گرفته می‌شود.
        Map<Integer, String> readFrom = new HashMap<>();
        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);
            if(op.action == 'R') {
                String source = "init";
                for (int j = i - 1; j >= 0; j--) {
                    Operation prev = operations.get(j);
                    if(prev.dataItem.equals(op.dataItem) && prev.action == 'W') {
                        source = Integer.toString(prev.transId);
                        break;
                    }
                }
                readFrom.put(i, source);
            }
        }
        
        // تعیین آخرین نوشتن برای هر آیتم داده
        Map<String, Integer> finalWrite = new HashMap<>();
        for(Operation op: operations) {
            if(op.action == 'W') {
                finalWrite.put(op.dataItem, op.transId);
            }
        }
        
        // تولید تمامی ترتیب‌های سریال ممکن تراکنش‌ها
        List<List<Integer>> permutations = new ArrayList<>();
        permute(transList, 0, permutations);
        
        boolean isViewSerializable = false;
        List<Integer> validOrder = null;
        for(List<Integer> order : permutations) {
            if(checkSerialSchedule(order, operations, readFrom, finalWrite)) {
                isViewSerializable = true;
                validOrder = order;
                break;
            }
        }
        
        if(isViewSerializable) {
            System.out.println("زمانبندی وارد شده پی‌درپی پذیر (View Serializable) می‌باشد.");
            System.out.println("یک ترتیب سریال معادل: " + validOrder);
        } else {
            System.out.println("زمانبندی وارد شده پی‌درپی پذیر (View Serializable) نمی‌باشد.");
        }
    }

    // تابع کمکی برای تولید تمام جایگشت‌های ممکن از لیست تراکنش‌ها
    private static void permute(List<Integer> list, int start, List<List<Integer>> result) {
        if(start == list.size() - 1) {
            result.add(new ArrayList<>(list));
            return;
        }
        for(int i = start; i < list.size(); i++) {
            Collections.swap(list, start, i);
            permute(list, start + 1, result);
            Collections.swap(list, start, i);
        }
    }
    
    // بررسی اینکه آیا ترتیب سریال داده شده از نظر دیدگاهی معادل زمانبندی اصلی است یا خیر.
    // در یک ترتیب سریال، هر تراکنش به صورت کامل اجرا می‌شود و مقدار خوانده شده برای یک آیتم، از آخرین تراکنش پیش از آن (که آن آیتم را نوشته) به دست می‌آید.
    private static boolean checkSerialSchedule(List<Integer> order, List<Operation> schedule, Map<Integer, String> readFrom, Map<String, Integer> finalWrite) {
        // ساخت نقشه‌ای که موقعیت هر تراکنش در ترتیب سریال را نشان می‌دهد.
        Map<Integer, Integer> pos = new HashMap<>();
        for(int i = 0; i < order.size(); i++) {
            pos.put(order.get(i), i);
        }
        
        // بررسی عملیات خواندن: برای هر عملیات خواندن در زمانبندی اصلی، در ترتیب سریال مقدار خوانده شده باید برابر باشد.
        for (int i = 0; i < schedule.size(); i++) {
            Operation op = schedule.get(i);
            if(op.action == 'R') {
                int currentTrans = op.transId;
                String candidateSource = "init";
                int currentPos = pos.get(currentTrans);
                // جستجو برای آخرین تراکنش در ترتیب سریال که پیش از currentTrans قرار دارد و آیتم داده را نوشته است.
                for (int j = currentPos - 1; j >= 0; j--) {
                    int prevTrans = order.get(j);
                    boolean wroteData = false;
                    for (Operation op2 : schedule) {
                        if(op2.transId == prevTrans && op2.action == 'W' && op2.dataItem.equals(op.dataItem)) {
                            wroteData = true;
                            break;
                        }
                    }
                    if(wroteData) {
                        candidateSource = Integer.toString(prevTrans);
                        break;
                    }
                }
                // بررسی تطابق منبع خواندن در زمانبندی اصلی و ترتیب سریال
                String originalSource = readFrom.get(i);
                if(!candidateSource.equals(originalSource)) {
                    return false;
                }
            }
        }
        
        // بررسی آخرین نوشتن برای هر آیتم داده در ترتیب سریال
        for(Map.Entry<String, Integer> entry : finalWrite.entrySet()) {
            String data = entry.getKey();
            int origFinal = entry.getValue();
            String candidateFinal = "";
            for (int j = order.size() - 1; j >= 0; j--) {
                int t = order.get(j);
                boolean wroteData = false;
                for (Operation op : schedule) {
                    if(op.transId == t && op.action == 'W' && op.dataItem.equals(data)) {
                        wroteData = true;
                        break;
                    }
                }
                if(wroteData) {
                    candidateFinal = Integer.toString(t);
                    break;
                }
            }
            if(candidateFinal.isEmpty()) candidateFinal = "init";
            if(!candidateFinal.equals(Integer.toString(origFinal))) {
                return false;
            }
        }
        
        return true;
    }
}
