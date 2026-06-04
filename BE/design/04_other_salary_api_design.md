# API Design: Bảng Lương Other (Lương Khác)

## Thông tin chung

| Thuộc tính     | Giá trị                          |
|----------------|----------------------------------|
| Module         | Salary Service – Other Salary    |
| Controller     | `OtherSalaryController`          |
| Base Path      | `/api/v1/salary/other`           |
| Phiên bản      | v1.0                             |
| Ngày tạo       | 2026-04-21                       |
| Người tạo      | CAM Team                         |

---

## Danh sách API

| STT | Method | Endpoint                   | Mô tả                                                                    |
|-----|--------|----------------------------|--------------------------------------------------------------------------|
| 1   | GET    | `/total-salary`            | Tổng lương Other trong tháng                                              |
| 2   | GET    | `/total-employees`         | Tổng số nhân viên Other trong tháng                                       |
| 3   | GET    | `/salary-comparison`       | Chênh lệch & % tổng lương Other so với tháng trước                       |
| 4   | GET    | `/average-salary`          | Trung bình thu nhập/nhân viên Other                                       |
| 5   | GET    | `/province-salary-ratio`   | Tỉ lệ % tổng lương Other theo tỉnh                                       |
| 6   | GET    | `/monthly-salary-trend`    | Tổng lương Other theo tháng của năm hiện tại và năm trước                |
| 7   | GET    | `/income-structure`        | Cơ cấu thu nhập theo từng loại lương trong Other                         |
| 8   | GET    | `/salary-list`             | Danh sách bảng lương Other tháng N-1, lọc theo đơn vị/nhân viên         |

---

## Chi tiết API

---

### 1. API Tổng Lương Other

**`GET /api/v1/salary/other/total-salary`**

**Mô tả:** Trả về tổng tiền lương của toàn bộ nhân viên thuộc nhóm lương Other (không thuộc HH_SALE và HH_N3) trong tháng được chỉ định.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/other/total-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "totalSalary": 3000000000
  }
}
```

---

### 2. API Tổng Số Nhân Viên Other

**`GET /api/v1/salary/other/total-employees`**

**Mô tả:** Trả về tổng số nhân viên thuộc nhóm lương Other được tính lương trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/other/total-employees?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "totalEmployees": 195
  }
}
```

---

### 3. API Chênh Lệch Tổng Lương So Với Tháng Trước

**`GET /api/v1/salary/other/salary-comparison`**

**Mô tả:** Trả về chênh lệch tuyệt đối (USD) và tỉ lệ % tổng lương Other giữa tháng hiện tại và tháng liền trước. Giá trị `difference` mang dấu âm nếu giảm, dương nếu tăng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                        | Ví dụ     |
|---------|--------|----------|------------------------------|-----------|
| `month` | String | ✅        | Tháng cần so sánh (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/other/salary-comparison?month=2026-03-01
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
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "currentTotalSalary": 3000000000,
    "prevTotalSalary": 2750000000,
    "difference": 250000000,
    "differencePercent": 9.09,
    "trend": "UP"
  }
}
```

---

### 4. API Trung Bình Thu Nhập / Nhân Viên Other

**`GET /api/v1/salary/other/average-salary`**

**Mô tả:** Trả về thu nhập bình quân trên mỗi nhân viên thuộc nhóm Other trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/other/average-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "totalSalary": 3000000000,
    "totalEmployees": 195,
    "averageSalary": 15384615
  }
}
```

---

### 5. API Tỉ Lệ % Tổng Lương Other Theo Tỉnh

**`GET /api/v1/salary/other/province-salary-ratio`**

**Mô tả:** Trả về tổng lương Other theo từng tỉnh/thành phố cùng tỉ lệ % trên tổng lương Other toàn quốc trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/other/province-salary-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "totalSalary": 3000000000,
    "items": [
      { "provinceCode": "HNI", "provinceName": "Hà Nội",          "totalAmount": 900000000,  "ratio": 30.00 },
      { "provinceCode": "HCM", "provinceName": "TP. Hồ Chí Minh", "totalAmount": 750000000,  "ratio": 25.00 },
      { "provinceCode": "DNG", "provinceName": "Đà Nẵng",         "totalAmount": 450000000,  "ratio": 15.00 },
      { "provinceCode": "CTH", "provinceName": "Cần Thơ",         "totalAmount": 300000000,  "ratio": 10.00 },
      { "provinceCode": "OTHER_PROV", "provinceName": "Các tỉnh khác", "totalAmount": 600000000, "ratio": 20.00 }
    ]
  }
}
```

---

### 6. API Tổng Lương Theo Tháng (Năm Hiện Tại & Năm Trước)

**`GET /api/v1/salary/other/monthly-salary-trend`**

**Mô tả:** Trả về tổng lương Other theo từng tháng trong năm hiện tại và năm liền trước, phục vụ vẽ biểu đồ xu hướng.

#### Request Parameters

| Tên    | Kiểu    | Bắt buộc | Mô tả                       | Ví dụ  |
|--------|---------|----------|-----------------------------|--------|
| `year` | Integer | ✅        | Năm cần lấy dữ liệu (YYYY) | `2026` |

#### Request Example

```http
GET /api/v1/salary/other/monthly-salary-trend?year=2026
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "currentYear": 2026,
    "prevYear": 2025,
    "currentYearData": [
      { "month": "2026-01", "totalSalary": 2600000000 },
      { "month": "2026-02", "totalSalary": 2750000000 },
      { "month": "2026-03", "totalSalary": 3000000000 }
    ],
    "prevYearData": [
      { "month": "2025-01", "totalSalary": 2300000000 },
      { "month": "2025-02", "totalSalary": 2400000000 },
      { "month": "2025-03", "totalSalary": 2500000000 }
    ]
  }
}
```

---

### 7. API Cơ Cấu Thu Nhập Theo Từng Loại Lương

**`GET /api/v1/salary/other/income-structure`**

**Mô tả:** Trả về cơ cấu thu nhập của nhân viên Other phân tách theo từng hạng mục lương (lương cơ bản, phụ cấp chức vụ, thâm niên, hỗ trợ…) trong tháng.

#### Request Parameters

| Tên     | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|---------|--------|----------|----------------------------------|-----------|
| `month` | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/other/income-structure?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "OTHER",
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
    "salaryType": "OTHER",
    "totalSalary": 3000000000,
    "items": [
      { "categoryCode": "BASIC",     "categoryName": "Lương cơ bản",       "totalAmount": 1500000000, "ratio": 50.00 },
      { "categoryCode": "POSITION",  "categoryName": "Phụ cấp chức vụ",    "totalAmount": 600000000,  "ratio": 20.00 },
      { "categoryCode": "SENIORITY", "categoryName": "Phụ cấp thâm niên",  "totalAmount": 450000000,  "ratio": 15.00 },
      { "categoryCode": "SUPPORT",   "categoryName": "Hỗ trợ công tác",    "totalAmount": 300000000,  "ratio": 10.00 },
      { "categoryCode": "OTHER_CAT", "categoryName": "Khác",               "totalAmount": 150000000,  "ratio":  5.00 }
    ]
  }
}
```

---

### 8. API Danh Sách Bảng Lương Other Tháng N-1

**`GET /api/v1/salary/other/salary-list`**

**Mô tả:** Trả về danh sách chi tiết bảng lương Other của tháng N-1 (tháng liền trước tháng hiện tại). Có thể lọc theo mã đơn vị hoặc mã nhân viên. Hỗ trợ phân trang.

#### Request Parameters

| Tên            | Kiểu    | Bắt buộc | Mô tả                                              | Ví dụ      |
|----------------|---------|----------|----------------------------------------------------|------------|
| `month`        | String  | ✅        | Tháng N-1 cần lấy dữ liệu (YYYY-MM-DD)           | `2026-02-01` |
| `unitCode`     | String  | ❌        | Mã đơn vị để lọc                                  | `UNIT_DNG` |
| `employeeCode` | String  | ❌        | Mã nhân viên để lọc                               | `EMP101`   |
| `page`         | Integer | ❌        | Trang hiện tại (mặc định: 0)                      | `0`        |
| `size`         | Integer | ❌        | Số bản ghi mỗi trang (mặc định: 20, tối đa: 100) | `20`       |

> **Lưu ý:** `unitCode` và `employeeCode` không bắt buộc nhưng nên cung cấp ít nhất một trong hai để tối ưu hiệu năng truy vấn.

#### Request Example

```http
GET /api/v1/salary/other/salary-list?month=2026-02-01&unitCode=UNIT_DNG&page=0&size=20
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "salaryType": "OTHER",
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
        "positionAllowance": "number – phụ cấp chức vụ",
        "seniorityAllowance": "number – phụ cấp thâm niên",
        "supportAllowance": "number – hỗ trợ công tác",
        "otherAmount": "number – các khoản khác",
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
    "salaryType": "OTHER",
    "totalRecords": 195,
    "page": 0,
    "size": 20,
    "totalPages": 10,
    "items": [
      {
        "employeeCode": "EMP101",
        "employeeName": "Lê Văn Cường",
        "unitCode": "UNIT_DNG",
        "unitName": "Chi nhánh Đà Nẵng",
        "provinceCode": "DNG",
        "provinceName": "Đà Nẵng",
        "contractType": "FULLTIME",
        "basicSalary": 10000000,
        "positionAllowance": 3000000,
        "seniorityAllowance": 1500000,
        "supportAllowance": 500000,
        "otherAmount": 200000,
        "totalSalary": 15200000,
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
- Nhóm "Other" bao gồm tất cả nhân viên không phân loại vào HH_SALE hoặc HH_N3.
