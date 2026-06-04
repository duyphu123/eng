# API Design: Dashboard Tổng Hợp Lương

## Thông tin chung

| Thuộc tính     | Giá trị                        |
|----------------|-------------------------------|
| Module         | Salary Service – Dashboard    |
| Controller     | `DashboardController`         |
| Base Path      | `/api/v1/salary/dashboard`    |
| Phiên bản      | v1.0                          |
| Ngày tạo       | 2026-04-21                    |
| Người tạo      | CAM Team                      |

---

## Danh sách API

| STT | Method | Endpoint                          | Mô tả                                                        |
|-----|--------|-----------------------------------|--------------------------------------------------------------|
| 1   | GET    | `/total-salary`                   | Tổng lương 3 loại và % so sánh với tháng trước              |
| 2   | GET    | `/total-employees`                | Tổng số nhân viên và % so sánh với tháng trước              |
| 3   | GET    | `/average-salary`                 | Trung bình lương/nhân viên và % chênh lệch tháng trước      |
| 4   | GET    | `/salary-type-ratio`              | Tỉ lệ % tổng lương theo 3 loại (HH_SALE, HH_N3, Other)      |
| 5   | GET    | `/province-salary-ratio`          | Tỉ lệ % tổng lương theo từng tỉnh                           |
| 6   | GET    | `/salary-category-ratio`          | Tỉ lệ % lương theo từng loại lương (cơ bản, KPI, phụ cấp…) |
| 7   | GET    | `/contract-type-ratio`            | Tỉ lệ % theo loại hợp đồng lao động                        |

---

## Chi tiết API

---

### 1. API Tổng Lương 3 Loại

**`GET /api/v1/salary/dashboard/total-salary`**

**Mô tả:** Trả về tổng tiền lương gộp của cả 3 loại (HH_SALE, HH_N3, Other) trong tháng hiện tại, đồng thời so sánh giá trị tuyệt đối và tỉ lệ % so với tháng trước.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/total-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "totalSalary": "number – tổng lương tháng hiện tại (USD)",
    "prevMonthTotalSalary": "number – tổng lương tháng trước (USD)",
    "difference": "number – chênh lệch tuyệt đối (dương/âm)",
    "differencePercent": "number – % chênh lệch (dương/âm)",
    "trend": "string – UP | DOWN | EQUAL",
    "breakdown": {
      "hhSale": "number – tổng lương HH_SALE",
      "hhN3": "number – tổng lương HH_N3",
      "other": "number – tổng lương Other"
    }
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
    "totalSalary": 15800000000,
    "prevMonthTotalSalary": 14500000000,
    "difference": 1300000000,
    "differencePercent": 8.97,
    "trend": "UP",
    "breakdown": {
      "hhSale": 7200000000,
      "hhN3": 5600000000,
      "other": 3000000000
    }
  }
}
```

---

### 2. API Tổng Số Nhân Viên

**`GET /api/v1/salary/dashboard/total-employees`**

**Mô tả:** Trả về tổng số nhân viên được tính lương trong tháng và tỉ lệ % thay đổi so với tháng trước.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/total-employees?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "totalEmployees": "integer – tổng số nhân viên tháng hiện tại",
    "prevMonthTotalEmployees": "integer – tổng số nhân viên tháng trước",
    "difference": "integer – chênh lệch số nhân viên",
    "differencePercent": "number – % chênh lệch (dương/âm)",
    "trend": "string – UP | DOWN | EQUAL",
    "breakdown": {
      "hhSale": "integer – số nhân viên HH_SALE",
      "hhN3": "integer – số nhân viên HH_N3",
      "other": "integer – số nhân viên Other"
    }
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
    "totalEmployees": 1245,
    "prevMonthTotalEmployees": 1198,
    "difference": 47,
    "differencePercent": 3.92,
    "trend": "UP",
    "breakdown": {
      "hhSale": 620,
      "hhN3": 430,
      "other": 195
    }
  }
}
```

---

### 3. API Trung Bình Lương / Nhân Viên

**`GET /api/v1/salary/dashboard/average-salary`**

**Mô tả:** Trả về thu nhập trung bình trên mỗi nhân viên trong tháng, kèm giá trị chênh lệch tuyệt đối và tỉ lệ % tăng/giảm so với tháng trước.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/average-salary?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "averageSalary": "number – trung bình lương tháng hiện tại (USD)",
    "prevMonthAverageSalary": "number – trung bình lương tháng trước (USD)",
    "difference": "number – chênh lệch tuyệt đối (dương/âm)",
    "differencePercent": "number – % tăng/giảm so với tháng trước",
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
    "averageSalary": 12690000,
    "prevMonthAverageSalary": 12103000,
    "difference": 587000,
    "differencePercent": 4.85,
    "trend": "UP"
  }
}
```

---

### 4. API Tỉ Lệ % Tổng Lương Theo 3 Loại

**`GET /api/v1/salary/dashboard/salary-type-ratio`**

**Mô tả:** Trả về tỉ lệ phần trăm tổng lương của từng loại (HH_SALE, HH_N3, Other) trên tổng lương chung trong tháng.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/salary-type-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "totalSalary": "number – tổng lương tất cả loại (USD)",
    "items": [
      {
        "salaryType": "string – HH_SALE | HH_N3 | OTHER",
        "totalAmount": "number – tổng lương của loại đó",
        "ratio": "number – tỉ lệ % trên tổng"
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
    "totalSalary": 15800000000,
    "items": [
      { "salaryType": "HH_SALE", "totalAmount": 7200000000, "ratio": 45.57 },
      { "salaryType": "HH_N3",   "totalAmount": 5600000000, "ratio": 35.44 },
      { "salaryType": "OTHER",   "totalAmount": 3000000000, "ratio": 18.99 }
    ]
  }
}
```

---

### 5. API Tỉ Lệ % Tổng Lương Theo Tỉnh

**`GET /api/v1/salary/dashboard/province-salary-ratio`**

**Mô tả:** Trả về danh sách các tỉnh cùng tổng lương và tỉ lệ % đóng góp trên tổng lương toàn quốc trong tháng.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/province-salary-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "totalSalary": "number – tổng lương toàn quốc (USD)",
    "items": [
      {
        "provinceCode": "string – mã tỉnh",
        "provinceName": "string – tên tỉnh",
        "totalAmount": "number – tổng lương của tỉnh",
        "ratio": "number – tỉ lệ % trên tổng"
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
    "totalSalary": 15800000000,
    "items": [
      { "provinceCode": "HNI", "provinceName": "Hà Nội",       "totalAmount": 4200000000, "ratio": 26.58 },
      { "provinceCode": "HCM", "provinceName": "TP. Hồ Chí Minh","totalAmount": 3800000000, "ratio": 24.05 },
      { "provinceCode": "DNG", "provinceName": "Đà Nẵng",      "totalAmount": 1500000000, "ratio": 9.49 }
    ]
  }
}
```

---

### 6. API Tỉ Lệ % Lương Theo Loại Lương (Cơ Cấu)

**`GET /api/v1/salary/dashboard/salary-category-ratio`**

**Mô tả:** Trả về tỉ lệ phần trăm của từng hạng mục lương (lương cơ bản, KPI, hoa hồng, phụ cấp…) trên tổng lương trong tháng.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/salary-category-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "totalSalary": "number",
    "items": [
      {
        "categoryCode": "string – mã loại lương",
        "categoryName": "string – tên loại lương",
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
    "totalSalary": 15800000000,
    "items": [
      { "categoryCode": "BASIC",      "categoryName": "Lương cơ bản",   "totalAmount": 6500000000, "ratio": 41.14 },
      { "categoryCode": "KPI",        "categoryName": "Thưởng KPI",      "totalAmount": 4200000000, "ratio": 26.58 },
      { "categoryCode": "COMMISSION", "categoryName": "Hoa hồng",        "totalAmount": 3100000000, "ratio": 19.62 },
      { "categoryCode": "ALLOWANCE",  "categoryName": "Phụ cấp",         "totalAmount": 2000000000, "ratio": 12.66 }
    ]
  }
}
```

---

### 7. API Tỉ Lệ % Theo Loại Hợp Đồng

**`GET /api/v1/salary/dashboard/contract-type-ratio`**

**Mô tả:** Trả về tỉ lệ phần trăm số nhân viên và tổng lương theo từng loại hợp đồng lao động trong tháng.

#### Request Parameters

| Tên        | Kiểu   | Bắt buộc | Mô tả                            | Ví dụ     |
|------------|--------|----------|----------------------------------|-----------|
| `month`    | String | ✅        | Tháng cần lấy dữ liệu (YYYY-MM-DD) | `2026-03-01` |

#### Request Example

```http
GET /api/v1/salary/dashboard/contract-type-ratio?month=2026-03-01
Authorization: Bearer {token}
```

#### Response Schema

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "month": "string (YYYY-MM)",
    "totalEmployees": "integer",
    "totalSalary": "number",
    "items": [
      {
        "contractTypeCode": "string – mã loại hợp đồng",
        "contractTypeName": "string – tên loại hợp đồng",
        "employeeCount": "integer – số nhân viên",
        "employeeRatio": "number – % nhân viên",
        "totalAmount": "number – tổng lương",
        "salaryRatio": "number – % tổng lương"
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
    "totalEmployees": 1245,
    "totalSalary": 15800000000,
    "items": [
      { "contractTypeCode": "FULLTIME",   "contractTypeName": "Hợp đồng chính thức",  "employeeCount": 980, "employeeRatio": 78.71, "totalAmount": 13500000000, "salaryRatio": 85.44 },
      { "contractTypeCode": "PARTTIME",   "contractTypeName": "Hợp đồng bán thời gian","employeeCount": 165, "employeeRatio": 13.25, "totalAmount": 1500000000, "salaryRatio": 9.49  },
      { "contractTypeCode": "PROBATION",  "contractTypeName": "Thử việc",              "employeeCount": 100, "employeeRatio": 8.03,  "totalAmount": 800000000,  "salaryRatio": 5.06  }
    ]
  }
}
```

---

## Mã Lỗi Chung

| HTTP Status | Code  | Mô tả                                  |
|-------------|-------|----------------------------------------|
| 400         | 4001  | Tham số `month` không đúng định dạng  |
| 400         | 4002  | Tháng không hợp lệ hoặc quá khứ xa    |
| 401         | 4010  | Chưa xác thực (token không hợp lệ)    |
| 403         | 4030  | Không có quyền truy cập               |
| 404         | 4040  | Không tìm thấy dữ liệu cho tháng này  |
| 500         | 5000  | Lỗi hệ thống nội bộ                   |

---

## Ghi Chú

- Tất cả giá trị tiền tệ đơn vị là **USD**.
- `differencePercent`: dương (+) là tăng, âm (-) là giảm so với tháng trước.
- `trend`: `UP` (tăng) | `DOWN` (giảm) | `EQUAL` (không đổi).
- Phân trang bắt đầu từ `page=0`.
- Tháng đầu vào theo chuẩn: `YYYY-MM-DD` nhưng sẽ được convert ra ngày đầu của tháng đó (ví dụ: `2026-03-01`).
- Mỗi nhóm sẽ có `salary_process_type`: `HH_SALE`, `HH_N3`, `OTHER`.
