# BE — salary-service (Spring Boot)

Module backend của monorepo **eng**. Đây là **salary-service** nội bộ của SmartViet (API tính lương) được tái sử dụng làm **bộ khung tham chiếu (reference skeleton)**. Backend thật của eng (TOEIC) **chưa được dựng**.

> Thư mục `.git` nằm ở gốc monorepo (`../`). FE nằm ở `../FE/` (Angular). GitNexus chỉ index phần TypeScript của FE, **KHÔNG** index code Java này.

## ⚠️ Quan trọng — đọc trước khi sửa bất cứ thứ gì

- **Đây là service tính lương dùng làm THAM CHIẾU, giữ nguyên hiện trạng.** **Đừng** tìm-thay-thế hàng loạt `salary` → `eng`. Chữ "salary" vừa là **danh tính package** (`com.smartviet.base.salary`, artifact `salary-service` — có thể đổi tên) **vừa là tên miền DB thật** (các entity `Employees`, `SalaryAdvance`, `PersonalIncomeTax`; bảng `salary_*`, cột `salary_type`… — đổi tên những thứ này sẽ làm hỏng ánh xạ với `sql.sql`).
- Đừng nắn miền nghiệp vụ tính lương cho khớp với app TOEIC. Khi dựng backend eng thật, đó là một quyết định riêng (khởi tạo sạch vs. lược bỏ domain) — hãy hỏi trước.

## Công nghệ (Stack)

Spring Boot **3.4.7** · Java **17** · Maven (wrapper) · Spring Data JPA · Spring Security · Actuator · Lombok.
Có sẵn cấu hình Kafka & Redis nhưng đang **tắt** (`configs/KafkaConfig`, `configs/RedisConfig`).

## Chạy / build

```bash
# từ thư mục BE/
./mvnw spring-boot:run          # dùng mvnw.cmd trên Windows cmd
./mvnw clean install            # build
./mvnw test                     # chạy test (JUnit 5)
```

- Chọn profile qua `ENV_RUNTIME` (`dev`/`prod`); cấu hình ở `src/main/resources/application.yml` + `application-dev.yml` / `application-prod.yml`.
- Thông tin bí mật (secrets) nằm ở `env/.env`, `env/.env.dev`, `env/.env.prod` (**bị gitignore**; file mẫu: `env-template/.env-template`). App cần có `DATASOURCE_URL/USERNAME/PASSWORD` trước khi khởi động được.

## Kiến trúc & quy ước code (hãy tuân theo)

Phân tầng: `controllers/` → `services/` (impl) hiện thực `services/iservice/` (interface) → `repositories/` (Spring Data) → `entities/`.

- **Vỏ bọc phản hồi (response envelope):** mọi endpoint trả về `ResponseEntity<ExecutionResult<T>>`. Dựng bằng `ExecutionResult.<T>builder().responseCode("200").data(...).build()`. Xem `dto/common/ExecutionResult.java`. Mã phản hồi (response code) định nghĩa ở `common/Constants.java` (`Constants.ExecutionCode`).
- **Lỗi (errors):** đừng tự trả body lỗi tuỳ tiện. Hãy ném exception tự định nghĩa (`exceptions/LogicException`, `AuthException`, `PermissionDeniedException`) — chúng được xử lý tập trung bởi `controllers/handler/GlobalExceptionHandler.java` (`@RestControllerAdvice`). Câu thông báo lấy từ các key trong `common/ResponseMessage` qua `ResourceConfig.getResourceMessage(...)` (i18n — đa ngôn ngữ).
- **Kiểm tra dữ liệu (validation):** Bean Validation (`@Valid`, `@NotBlank`, `@Size`) + validator tự viết ở `utils/annotations/`. Lỗi validation được `GlobalExceptionHandler` tự xử lý.
- **DTO:** DTO request đặt ở `dto/requests/`; trả về DTO, không trả entity. Phân trang/tìm kiếm qua `dto/common/Paginator` + `dto/requests/search/CommonSearch`.
- **Entity:** JPA `@Entity/@Table`; một số kế thừa `entities/base/BaseEntity` (tự ghi audit `createdAt/By`, `updatedAt/By` qua `@PrePersist/@PreUpdate` lấy từ `SecurityContext`). Lombok `@Getter/@Setter/@Builder`.
- **Service:** interface ở `services/iservice/`, bản hiện thực ở `services/` (`@Service`); ưu tiên tiêm phụ thuộc qua constructor (`@RequiredArgsConstructor`).
- **Bảo mật (security):** Spring Security (`security/` — `JWTProvider`, `AuthHandler`, `WebConfig`). Endpoint mới có thể bị chặn (401) cho tới khi được cho phép — nhớ kiểm tra cấu hình security khi test.

Package gốc: `com.smartviet.base.salary`.

## Quy tắc viết API ⚠️ (cập nhật 2026-06-05)

- **CHỈ dùng 2 HTTP method: `GET` và `POST`.** **KHÔNG** dùng `PUT`, `PATCH`, `DELETE`.
  - **`GET`** → chỉ để **ĐỌC** dữ liệu: lấy danh sách, xem chi tiết 1 bản ghi, tìm kiếm.
  - **`POST`** → mọi thao tác **GHI**: **tạo mới (create), cập nhật (update), và xoá (delete)** đều là `POST`.
- **Quy ước đặt URL** (để phân biệt 3 thao tác ghi vì cùng dùng `POST`):

  | Thao tác | Method | URL (ví dụ với `vocabulary`) |
  |----------|--------|------------------------------|
  | Lấy danh sách / tìm kiếm | `GET`  | `GET /vocabulary` (kèm `?keyword=`) |
  | Xem chi tiết 1 bản ghi   | `GET`  | `GET /vocabulary/{id}` |
  | Tạo mới                  | `POST` | `POST /vocabulary` |
  | Cập nhật                 | `POST` | `POST /vocabulary/{id}/update` |
  | Xoá                      | `POST` | `POST /vocabulary/{id}/delete` |

  - Lý do: đồng nhất, dễ học, dễ kiểm soát quyền truy cập (chỉ cần phân biệt **đọc = GET / ghi = POST**).

## Ghi chú học tập

Lộ trình học CRUD từ số 0 (entity neo: `OptionSet`) nằm ở `../plan.html` → tab **🛠️ Thực hành CRUD**. Hãy làm bài thực hành trên nhánh `practice`.
## Quy tắc trả lời user
- Tất cả các câu trả lời của bạn đều phải xưng hô BẠN - TÔI (viết hoa)
