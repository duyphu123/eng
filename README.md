# eng — Monorepo (Frontend + Backend)

Một repo Git duy nhất chứa cả frontend và backend của dự án **EngToeic** (ứng dụng học từ vựng & luyện nghe TOEIC).

## Cấu trúc

```
eng/
├── FE/        Frontend — Angular 17 (app TOEIC)
├── BE/        Backend  — Spring Boot (API)   ← sẽ thêm
├── .github/   CI/CD dùng chung (GitHub Actions, deploy Pages)
└── .gitignore .editorconfig  — cấu hình cấp workspace
```

> Mỗi app tự quản lý dependency, build và `CLAUDE.md` riêng. Đây là **1 repo Git** ở gốc — mọi thứ push chung lên `github.com/duyphu123/eng`.

## Chạy Frontend

```bash
cd FE
npm install
npm start            # ng serve → http://localhost:4200
```

## Chạy Backend

```bash
cd BE
./mvnw spring-boot:run   # (sau khi tạo skeleton Spring Boot, port 8080)
```

## Làm việc với Claude Code

Mở terminal riêng cho từng phần — mỗi session tự nạp `CLAUDE.md` của thư mục đó (gộp với rule gốc):

```bash
cd FE && claude     # tập trung frontend
cd BE && claude     # tập trung backend
claude              # ở gốc: việc xuyên suốt cả 2 (vd: API contract)
```

## Deploy

- **FE** → GitHub Pages tự động qua `.github/workflows/deploy-pages.yml` (mỗi lần push lên `main`).
- **BE** → triển khai riêng (server / Render / Railway…), không qua GitHub Pages.
