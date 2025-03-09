# بررسی پی‌در‌پی‌پذیری زمانبندی تراکنش‌ها

این پروژه شامل دو برنامه اصلی برای بررسی **Conflict Serializability** و **View Serializability** زمانبندی تراکنش‌ها است. این برنامه‌ها به زبان جاوا نوشته شده‌اند و با استفاده از آن‌ها می‌توانید بررسی کنید که آیا یک زمانبندی تراکنش‌ها به صورت پی‌در‌پی پذیر است یا خیر.

---

## فهرست مطالب
1. [مقدمه](#مقدمه)
2. [نحوه اجرای برنامه](#نحوه-اجرای-برنامه)
3. [توضیحات برنامه‌ها](#توضیحات-برنامه‌ها)
    - [ConflictSerializabilityChecker](#conflictserializabilitychecker)
    - [ViewSerializabilityChecker](#viewserializabilitychecker)
4. [ورودی و خروجی](#ورودی-و-خروجی)
5. [مثال‌ها](#مثال‌ها)

---

## مقدمه

در سیستم‌های پایگاه داده، زمانبندی تراکنش‌ها تعیین می‌کند که عملیات‌های مختلف چگونه اجرا شوند. بررسی پی‌درپی‌پذیری زمانبندی‌ها برای اطمینان از صحت و سازگاری داده‌ها بسیار مهم است. این پروژه دو نوع پی‌درپی‌پذیری را بررسی می‌کند:

- **Conflict Serializability**
- **View Serializability**

---

## نحوه اجرای برنامه

1. مطمئن شوید که **JDK (Java Development Kit)** روی سیستم شما نصب است.
2. فایل‌های `ConflictSerializabilityChecker.java` و `ViewSerializabilityChecker.java` را دانلود کنید.
3. هر فایل را با دستور زیر کامپایل کنید:

   ```bash
   javac ConflictSerializabilityChecker.java
   javac ViewSerializabilityChecker.java
   ```
4. برنامه‌ها را با دستور زیر اجرا کنید:

   ```bash
   java ConflictSerializabilityChecker
   java ViewSerializabilityChecker
   ```
5. رشته زمانبندی را وارد کنید و خروجی را مشاهده کنید.

---

## توضیحات برنامه‌ها

### ConflictSerializabilityChecker

این برنامه بررسی می‌کند که آیا یک زمانبندی تراکنش‌ها به صورت **تضاد سریالی پذیر** است یا خیر. برای این کار، **گراف اولویت (Precedence Graph)** را می‌سازد و با استفاده از الگوریتم **کاهن**، چرخه‌ها را در گراف بررسی می‌کند.

#### ویژگی‌ها:
- **ورودی**: رشته زمانبندی (مثال: `R1(A),W2(A),R2(B),W1(B)`).
- **خروجی**: `true` اگر زمانبندی تضاد سریالی پذیر باشد، در غیر این صورت `false`.

### ViewSerializabilityChecker

این برنامه بررسی می‌کند که آیا یک زمانبندی تراکنش‌ها به صورت **دیداری سریالی پذیر** است یا خیر. برای این کار، **تمام جایگشت‌های ممکن** از تراکنش‌ها را بررسی می‌کند و معادل دیداری بودن آن‌ها را با زمانبندی اصلی مقایسه می‌کند.

#### ویژگی‌ها:
- **ورودی**: رشته زمانبندی (مثال: `R1(A),W2(A),R2(B),W1(B)`).
- **خروجی**: زمانبندی وارد شده پی‌درپی پذیر (View Serializable) می‌باشد/نمی باشد..

---

## ورودی و خروجی

### **فرمت ورودی**
رشته زمانبندی ورودی باید به یکی از دو صورت زیر باشد:

```text
R1(A),W2(A),R2(B),W1(B),...
```


```text
R1(A) W2(A) R2(B) W1(B) ...
```

- `R` برای عملیات **خواندن** و `W` برای عملیات **نوشتن**.
- عدد بعد از `R` یا `W` **شناسه تراکنش** است.
- حرف داخل پرانتز **داده‌ای** است که عملیات روی آن انجام می‌شود.

### **فرمت خروجی**
خروجی بررسی Csr یک مقدار **boolean** است:

- `true`: زمانبندی **پی‌درپی پذیر** است.
- `false`: زمانبندی **پی‌درپی پذیر نیست**.

خروجی بررسی Vsr :
- زمانبندی وارد شده پی‌درپی پذیر (View Serializable) می‌باشد.

 یک ترتیب سریال معادل: [1, 2, 3, ...]

- زمانبندی وارد شده پی‌درپی پذیر (View Serializable) نمی‌باشد.
