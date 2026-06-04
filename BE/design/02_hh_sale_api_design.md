# API Design: Bảng Lương HH_SALE

## Thông tin chung

| Thuộc tính     | Giá trị                          |
|----------------|----------------------------------|
| Module         | Salary Service – HH_SALE         |
| Controller     | `HhSaleSalaryController`         |
| Base Path      | `/api/v1/salary/hh-sale`         |
| Phiên bản      | v1.0                             |
| Ngày tạo       | 2026-04-21                       |
| Người tạo      | CAM Team                         |

---

## Danh sách API

| STT | Method | Endpoint                    | Mô tả                                                                 |
|-----|--------|-----------------------------|-----------------------------------------------------------------------|
| 1   | GET    | `/total-salary`             | Tổng lương HH_SALE trong tháng                                        |
| 2   | GET    | `/total-employees`          | Tổng số nhân viên HH_SALE trong tháng                                 |
| 3   | GET    | `/salary-comparison`        | Chênh lệch & % tổng lương HH_SALE so với tháng trước                 |
| 4   | GET    | `/average-salary`           | Trung bình thu nhập/nhân viên HH_SALE                                 |
| 5   | GET    | `/province-salary-ratio`    | Tỉ lệ % tổng lương HH_SALE theo tỉnh                                 |
| 6   | GET    | `/monthly-salary-trend`     | Tổng lương HH_SALE theo tháng của năm hiện tại và năm trước          |
| 7   | GET    | `/income-structure`         | Cơ cấu thu nhập theo từng loại lương trong HH_SALE                   |
| 8   | GET    | `/salary-list`              | Danh sách bảng lương HH_SALE tháng N-1, lọc theo đơn vị/nhân viên   |

---

## Chi tiết API

---

### 1. API Tổng Lương HH_SALE

**`GET /api/v1/salary/hh-sale/total-salary`**

**Mô tả:** Trả về tổng tiền lương của toàn bộ nhân viên loại HH_SALE trong tháng được chỉ định.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/total-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_SALE",
    "totalSalary": "number – tổng lương (USD)"
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-03",
    "salaryType": "HH_SALE",
    "totalSalary": 7200000000
  }
}
```

---

### 2. API Tổng Số Nhân Viên HH_SALE

**`GET /api/v1/salary/hh-sale/total-employees`**

**Mô tả:** Trả về tổng số nhân viên thuộc loại HH_SALE được tính lương trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/total-employees?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_SALE",
    "totalEmployees": "integer – tổng số nhân viên"
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-03",
    "salaryType": "HH_SALE",
    "totalEmployees": 620
  }
}
```

---

### 3. API Chênh Lệch Tổng Lương So Với Tháng Trước

**`GET /api/v1/salary/hh-sale/salary-comparison`**

**Mô tả:** Trả về chênh lệch tuyệt đối (USD) và tỉ lệ % tổng lương HH_SALE giữa tháng hiện tại và tháng liền trước. Xác định xu hướng tăng/giảm.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần so sánh (YYYY-MM-DD)     | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/salary-comparison?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string – tháng hiện tại (YYYY-MM)",
    "prevMonth": "string – tháng trước (YYYY-MM)",
    "salaryType": "HH_SALE",
    "currentTotalSalary": "number – tổng lương tháng hiện tại",
    "prevTotalSalary": "number – tổng lương tháng trước",
    "difference": "number – chênh lệch tuyệt đối (dương = tăng, âm = giảm)",
    "differencePercent": "number – tỉ lệ % thay đổi (dương = tăng, âm = giảm)",
    "trend": "string – UP | DOWN | EQUAL"
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-03",
    "prevMonth": "2026-02",
    "salaryType": "HH_SALE",
    "currentTotalSalary": 7200000000,
    "prevTotalSalary": 6850000000,
    "difference": 350000000,
    "differencePercent": 5.11,
    "trend": "UP"
  }
}
```

---

### 4. API Trung Bình Thu Nhập / Nhân Viên HH_SALE

**`GET /api/v1/salary/hh-sale/average-salary`**

**Mô tả:** Trả về thu nhập bình quân trên mỗi nhân viên HH_SALE trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/average-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_SALE",
    "totalSalary": "number – tổng lương (USD)",
    "totalEmployees": "integer – tổng số nhân viên",
    "averageSalary": "number – trung bình lương/người (USD)"
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-03",
    "salaryType": "HH_SALE",
    "totalSalary": 7200000000,
    "totalEmployees": 620,
    "averageSalary": 11612903
  }
}
```

---

### 5. API Tỉ Lệ % Tổng Lương HH_SALE Theo Tỉnh

**`GET /api/v1/salary/hh-sale/province-salary-ratio`**

**Mô tả:** Trả về danh sách tổng lương HH_SALE theo từng tỉnh/thành phố cùng tỉ lệ % đóng góp vào tổng lương HH_SALE toàn quốc trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/province-salary-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_SALE",
    "totalSalary": "number – tổng lương HH_SALE",
    "items": [
      {
        "provinceCode": "string – mã tỉnh",
        "provinceName": "string – tên tỉnh",
        "totalAmount": "number – tổng lương của tỉnh",
        "ratio": "number – tỉ lệ %"
      }
    ]
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-03",
    "salaryType": "HH_SALE",
    "totalSalary": 7200000000,
    "items": [
      { "provinceCode": "HNI", "provinceName": "Hà Nội",         "totalAmount": 2160000000, "ratio": 30.00 },
      { "provinceCode": "HCM", "provinceName": "TP. Hồ Chí Minh","totalAmount": 1800000000, "ratio": 25.00 },
      { "provinceCode": "DNG", "provinceName": "Đà Nẵng",        "totalAmount": 720000000,  "ratio": 10.00 }
    ]
  }
}
```

---

### 6. API Tổng Lương Theo Tháng (Năm Hiện Tại & Năm Trước)

**`GET /api/v1/salary/hh-sale/monthly-salary-trend`**

**Mô tả:** Trả về tổng lương HH_SALE theo từng tháng của năm hiện tại và năm liền trước để phục vụ biểu đồ xu hướng.

#### Request Parameters

| Tên    | Kiểu    | Bắt buộc | Mô tả                        | Ví dụ  |
|--------|---------|----------|------------------------------|--------|
| `year` | Integer | ✅        | Năm cần lấy dữ liệu (YYYY)  | `2026` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/monthly-salary-trend?year=2026
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "salaryType": "HH_SALE",
    "currentYear": "integer",
    "prevYear": "integer",
    "currentYearData": [
      {
        "month": "string (YYYY-MM)",
        "totalSalary": "number"
      }
    ],
    "prevYearData": [
      {
        "month": "string (YYYY-MM)",
        "totalSalary": "number"
      }
    ]
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "salaryType": "HH_SALE",
    "currentYear": 2026,
    "prevYear": 2025,
    "currentYearData": [
      { "month": "2026-01", "totalSalary": 6800000000 },
      { "month": "2026-02", "totalSalary": 6850000000 },
      { "month": "2026-03", "totalSalary": 7200000000 }
    ],
    "prevYearData": [
      { "month": "2025-01", "totalSalary": 6100000000 },
      { "month": "2025-02", "totalSalary": 6050000000 },
      { "month": "2025-03", "totalSalary": 6400000000 }
    ]
  }
}
```

---

### 7. API Cơ Cấu Thu Nhập Theo Từng Loại Lương

**`GET /api/v1/salary/hh-sale/income-structure`**

**Mô tả:** Trả về cơ cấu thu nhập của nhân viên HH_SALE phân tách theo từng hạng mục lương (lương cơ bản, KPI, hoa hồng, phụ cấp…) trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-sale/income-structure?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_SALE",
    "totalSalary": "number",
    "items": [
      {
        "categoryCode": "string – mã hạng mục lương",
        "categoryName": "string – tên hạng mục lương",
        "totalAmount": "number – tổng tiền của hạng mục",
        "ratio": "number – tỉ lệ % trên tổng lương"
      }
    ]
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-03",
    "salaryType": "HH_SALE",
    "totalSalary": 7200000000,
    "items": [
      { "categoryCode": "BASIC",      "categoryName": "Lương cơ bản",  "totalAmount": 2880000000, "ratio": 40.00 },
      { "categoryCode": "COMMISSION", "categoryName": "Hoa hồng sale", "totalAmount": 2520000000, "ratio": 35.00 },
      { "categoryCode": "KPI",        "categoryName": "Thưởng KPI",    "totalAmount": 1080000000, "ratio": 15.00 },
      { "categoryCode": "ALLOWANCE",  "categoryName": "Phụ cấp",       "totalAmount": 720000000,  "ratio": 10.00 }
    ]
  }
}
```

---

### 8. API Danh Sách Bảng Lương HH_SALE Tháng N-1

**`GET /api/v1/salary/hh-sale/salary-list`**

**Mô tả:** Trả về danh sách chi tiết bảng lương của tháng N-1 (tháng liền trước tháng hiện tại). Có thể lọc theo mã đơn vị hoặc mã nhân viên. Hỗ trợ phân trang.

#### Request Parameters

| Tên            | Kiểu    | Bắt buộc | Mô tả                                                | Ví dụ        |
|----------------|---------|----------|------------------------------------------------------|--------------|
| `month`        | String  | ✅        | Tháng N-1 cần lấy dữ liệu (YYYY-MM-DD)             | `2026-02-01` |
| `unitCode`     | String  | ❌        | Mã đơn vị để lọc (nếu không có thì lấy toàn bộ)    | `UNIT_HNI`   |
| `employeeCode` | String  | ❌        | Mã nhân viên để lọc                                  | `EMP001`     |
| `page`         | Integer | ❌        | Trang hiện tại (mặc định: 0)                        | `0`          |
| `size`         | Integer | ❌        | Số bản ghi mỗi trang (mặc định: 20, tối đa: 100)   | `20`         |

> **Lưu ý:** `unitCode` và `employeeCode` không bắt buộc nhưng nên cung cấp ít nhất một trong hai để tối ưu hiệu năng truy vấn.

#### Request Example

```http
GET /api/v1/salary/hh-sale/salary-list?month=2026-02-01&unitCode=UNIT_HNI&page=0&size=20
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_SALE",
    "totalRecords": "integer – tổng số bản ghi",
    "page": "integer – trang hiện tại",
    "size": "integer – số bản ghi mỗi trang",
    "totalPages": "integer – tổng số trang",
    "items": [
      {
        "employeeCode": "string – mã nhân viên",
        "employeeName": "string – họ tên nhân viên",
        "unitCode": "string – mã đơn vị",
        "unitName": "string – tên đơn vị",
        "provinceCode": "string – mã tỉnh",
        "provinceName": "string – tên tỉnh",
        "contractType": "string – loại hợp đồng",
        "basicSalary": "number – lương cơ bản",
        "commission": "number – hoa hồng sale",
        "kpiBonus": "number – thưởng KPI",
        "allowance": "number – phụ cấp",
        "totalSalary": "number – tổng lương",
        "salaryMonth": "string (YYYY-MM)"
      }
    ]
  }
}
```

#### Response Example

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "2026-02",
    "salaryType": "HH_SALE",
    "totalRecords": 620,
    "page": 0,
    "size": 20,
    "totalPages": 31,
    "items": [
      {
        "employeeCode": "EMP001",
        "employeeName": "Nguyễn Văn An",
        "unitCode": "UNIT_HNI",
        "unitName": "Chi nhánh Hà Nội",
        "provinceCode": "HNI",
        "provinceName": "Hà Nội",
        "contractType": "FULLTIME",
        "basicSalary": 8000000,
        "commission": 5500000,
        "kpiBonus": 2000000,
        "allowance": 500000,
        "totalSalary": 16000000,
        "salaryMonth": "2026-02"
      }
    ]
  }
}
```

---

## Mã Lỗi Chung

| HTTP Status | Code  | Mô tả                                        |
|-------------|-------|----------------------------------------------|
| 400         | 4001  | Tham số `month`/`year` không đúng định dạng  |
| 400         | 4002  | Tháng không hợp lệ                           |
| 401         | 4010  | Chưa xác thực (token không hợp lệ)          |
| 403         | 4030  | Không có quyền truy cập                      |
| 404         | 4040  | Không tìm thấy dữ liệu                       |
| 500         | 5000  | Lỗi hệ thống nội bộ                          |

---

## Ghi Chú

- Tất cả giá trị tiền tệ đơn vị là **USD**.
- `differencePercent`: dương (+) là tăng, âm (-) là giảm so với tháng trước.
- `trend`: `UP` (tăng) | `DOWN` (giảm) | `EQUAL` (không đổi).
- Phân trang bắt đầu từ `page=0`.
- Tháng đầu vào theo chuẩn: `YYYY-MM-DD` nhưng sẽ được convert ra ngày đầu của tháng đó (ví dụ: `2026-03-01`).
- Mỗi nhóm sẽ có `salary_process_type`: `HH_SALE`, `HH_N3`, `OTHER`.
