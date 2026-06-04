# API Design: Bảng Lương HH_N3

## Thông tin chung

| Thuộc tính     | Giá trị                         |
|----------------|---------------------------------|
| Module         | Salary Service – HH_N3          |
| Controller     | `HhN3SalaryController`          |
| Base Path      | `/api/v1/salary/hh-n3`          |
| Phiên bản      | v1.0                            |
| Ngày tạo       | 2026-04-21                      |
| Người tạo      | CAM Team                        |

---

## Danh sách API

| STT | Method | Endpoint                   | Mô tả                                                                |
|-----|--------|----------------------------|----------------------------------------------------------------------|
| 1   | GET    | `/total-salary`            | Tổng lương HH_N3 trong tháng                                         |
| 2   | GET    | `/total-employees`         | Tổng số nhân viên HH_N3 trong tháng                                  |
| 3   | GET    | `/salary-comparison`       | Chênh lệch & % tổng lương HH_N3 so với tháng trước                  |
| 4   | GET    | `/average-salary`          | Trung bình thu nhập/nhân viên HH_N3                                  |
| 5   | GET    | `/province-salary-ratio`   | Tỉ lệ % tổng lương HH_N3 theo tỉnh                                  |
| 6   | GET    | `/monthly-salary-trend`    | Tổng lương HH_N3 theo tháng của năm hiện tại và năm trước           |
| 7   | GET    | `/income-structure`        | Cơ cấu thu nhập theo từng loại lương trong HH_N3                    |
| 8   | GET    | `/salary-list`             | Danh sách bảng lương HH_N3 tháng N-1, lọc theo đơn vị/nhân viên    |

---

## Chi tiết API

---

### 1. API Tổng Lương HH_N3

**`GET /api/v1/salary/hh-n3/total-salary`**

**Mô tả:** Trả về tổng tiền lương của toàn bộ nhân viên thuộc nhóm HH_N3 trong tháng được chỉ định.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/total-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_N3",
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
    "salaryType": "HH_N3",
    "totalSalary": 5600000000
  }
}
```

---

### 2. API Tổng Số Nhân Viên HH_N3

**`GET /api/v1/salary/hh-n3/total-employees`**

**Mô tả:** Trả về tổng số nhân viên thuộc nhóm HH_N3 được tính lương trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/total-employees?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_N3",
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
    "salaryType": "HH_N3",
    "totalEmployees": 430
  }
}
```

---

### 3. API Chênh Lệch Tổng Lương So Với Tháng Trước

**`GET /api/v1/salary/hh-n3/salary-comparison`**

**Mô tả:** Trả về chênh lệch tuyệt đối (USD) và tỉ lệ % tổng lương HH_N3 giữa tháng hiện tại và tháng liền trước. Giá trị `difference` mang dấu âm nếu giảm, dương nếu tăng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                        | Ví dụ     |
|---------|--------|----------|------------------------------|-----------|
| `month` | String | ✅        | Tháng cần so sánh (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/salary-comparison?month=2026-03-01
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
    "salaryType": "HH_N3",
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
    "salaryType": "HH_N3",
    "currentTotalSalary": 5600000000,
    "prevTotalSalary": 5900000000,
    "difference": -300000000,
    "differencePercent": -5.08,
    "trend": "DOWN"
  }
}
```

---

### 4. API Trung Bình Thu Nhập / Nhân Viên HH_N3

**`GET /api/v1/salary/hh-n3/average-salary`**

**Mô tả:** Trả về thu nhập bình quân trên mỗi nhân viên thuộc nhóm HH_N3 trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/average-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_N3",
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
    "salaryType": "HH_N3",
    "totalSalary": 5600000000,
    "totalEmployees": 430,
    "averageSalary": 13023255
  }
}
```

---

### 5. API Tỉ Lệ % Tổng Lương HH_N3 Theo Tỉnh

**`GET /api/v1/salary/hh-n3/province-salary-ratio`**

**Mô tả:** Trả về danh sách các tỉnh cùng tổng lương HH_N3 và tỉ lệ % trên tổng lương HH_N3 toàn quốc trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/province-salary-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_N3",
    "totalSalary": "number",
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
    "salaryType": "HH_N3",
    "totalSalary": 5600000000,
    "items": [
      { "provinceCode": "HNI", "provinceName": "Hà Nội",          "totalAmount": 1680000000, "ratio": 30.00 },
      { "provinceCode": "HCM", "provinceName": "TP. Hồ Chí Minh", "totalAmount": 1120000000, "ratio": 20.00 },
      { "provinceCode": "DNG", "provinceName": "Đà Nẵng",         "totalAmount": 840000000,  "ratio": 15.00 },
      { "provinceCode": "CTH", "provinceName": "Cần Thơ",         "totalAmount": 560000000,  "ratio": 10.00 }
    ]
  }
}
```

---

### 6. API Tổng Lương Theo Tháng (Năm Hiện Tại & Năm Trước)

**`GET /api/v1/salary/hh-n3/monthly-salary-trend`**

**Mô tả:** Trả về tổng lương HH_N3 theo từng tháng trong năm hiện tại và năm liền trước, phục vụ vẽ biểu đồ đường xu hướng.

#### Request Parameters

| Tên    | Kiểu    | Bắt buộc | Mô tả                       | Ví dụ  |
|--------|---------|----------|-----------------------------|--------|
| `year` | Integer | ✅        | Năm cần lấy dữ liệu (YYYY) | `2026` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/monthly-salary-trend?year=2026
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "salaryType": "HH_N3",
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
    "salaryType": "HH_N3",
    "currentYear": 2026,
    "prevYear": 2025,
    "currentYearData": [
      { "month": "2026-01", "totalSalary": 5400000000 },
      { "month": "2026-02", "totalSalary": 5900000000 },
      { "month": "2026-03", "totalSalary": 5600000000 }
    ],
    "prevYearData": [
      { "month": "2025-01", "totalSalary": 5000000000 },
      { "month": "2025-02", "totalSalary": 5200000000 },
      { "month": "2025-03", "totalSalary": 5350000000 }
    ]
  }
}
```

---

### 7. API Cơ Cấu Thu Nhập Theo Từng Loại Lương

**`GET /api/v1/salary/hh-n3/income-structure`**

**Mô tả:** Trả về cơ cấu thu nhập của nhân viên HH_N3 phân tách theo từng hạng mục lương trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/hh-n3/income-structure?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_N3",
    "totalSalary": "number",
    "items": [
      {
        "categoryCode": "string – mã hạng mục",
        "categoryName": "string – tên hạng mục",
        "totalAmount": "number",
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
    "salaryType": "HH_N3",
    "totalSalary": 5600000000,
    "items": [
      { "categoryCode": "BASIC",     "categoryName": "Lương cơ bản",    "totalAmount": 2800000000, "ratio": 50.00 },
      { "categoryCode": "N3_BONUS",  "categoryName": "Thưởng N3",       "totalAmount": 1400000000, "ratio": 25.00 },
      { "categoryCode": "KPI",       "categoryName": "Thưởng KPI",      "totalAmount": 840000000,  "ratio": 15.00 },
      { "categoryCode": "ALLOWANCE", "categoryName": "Phụ cấp",         "totalAmount": 560000000,  "ratio": 10.00 }
    ]
  }
}
```

---

### 8. API Danh Sách Bảng Lương HH_N3 Tháng N-1

**`GET /api/v1/salary/hh-n3/salary-list`**

**Mô tả:** Trả về danh sách chi tiết bảng lương HH_N3 của tháng N-1. Có thể lọc theo mã đơn vị hoặc mã nhân viên. Hỗ trợ phân trang.

#### Request Parameters

| Tên            | Kiểu    | Bắt buộc | Mô tả                                              | Ví dụ      |
|----------------|---------|----------|----------------------------------------------------|------------|
| `month`        | String  | ✅        | Tháng N-1 cần lấy dữ liệu (YYYY-MM-DD)           | `2026-02-01` |
| `unitCode`     | String  | ❌        | Mã đơn vị để lọc                                  | `UNIT_HCM` |
| `employeeCode` | String  | ❌        | Mã nhân viên để lọc                               | `EMP021`   |
| `page`         | Integer | ❌        | Trang hiện tại (mặc định: 0)                      | `0`        |
| `size`         | Integer | ❌        | Số bản ghi mỗi trang (mặc định: 20, tối đa: 100) | `20`       |

> **Lưu ý:** `unitCode` và `employeeCode` không bắt buộc nhưng nên cung cấp ít nhất một trong hai để tối ưu hiệu năng truy vấn.

#### Request Example

```http
GET /api/v1/salary/hh-n3/salary-list?month=2026-02-01&unitCode=UNIT_HCM&page=0&size=20
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "HH_N3",
    "totalRecords": "integer",
    "page": "integer",
    "size": "integer",
    "totalPages": "integer",
    "items": [
      {
        "employeeCode": "string – mã nhân viên",
        "employeeName": "string – họ tên",
        "unitCode": "string – mã đơn vị",
        "unitName": "string – tên đơn vị",
        "provinceCode": "string – mã tỉnh",
        "provinceName": "string – tên tỉnh",
        "contractType": "string – loại hợp đồng",
        "basicSalary": "number – lương cơ bản",
        "n3Bonus": "number – thưởng N3",
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
    "salaryType": "HH_N3",
    "totalRecords": 430,
    "page": 0,
    "size": 20,
    "totalPages": 22,
    "items": [
      {
        "employeeCode": "EMP021",
        "employeeName": "Trần Thị Bình",
        "unitCode": "UNIT_HCM",
        "unitName": "Chi nhánh TP. Hồ Chí Minh",
        "provinceCode": "HCM",
        "provinceName": "TP. Hồ Chí Minh",
        "contractType": "FULLTIME",
        "basicSalary": 9000000,
        "n3Bonus": 4000000,
        "kpiBonus": 2500000,
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
