import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  Vocabulary,
  VocabularyApiService,
  VocabularyPayload,
} from '../../services/vocabulary-api.service';

/**
 * Kiểu dữ liệu cho FORM trong popup.
 * Mọi field để string (mặc định "") cho dễ bind 2 chiều [(ngModel)] — không lo undefined.
 */
interface VocabForm {
  word: string;
  meaning: string;
  partOfSpeech: string;
  pronunciation: string;
  example: string;
  level: string;
}

/** Tạo 1 form rỗng (dùng khi bấm "Tạo mới"). */
function emptyForm(): VocabForm {
  return {
    word: '',
    meaning: '',
    partOfSpeech: '',
    pronunciation: '',
    example: '',
    level: '',
  };
}

/**
 * AdminComponent — trang /admin để THỰC HÀNH toàn bộ API CRUD của BE.
 *
 * <p>Gồm: bảng danh sách (GET), nút "Tạo mới" mở popup form (POST /create),
 * mỗi dòng có nút Sửa (POST /{id}/update) và Xoá (POST /{id}/delete).
 *
 * <p>Theo quy ước dự án: standalone component + Angular signals cho state;
 * gọi BE qua VocabularyApiService (không tự fetch trong component).
 */
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss',
})
export class AdminComponent {
  private readonly api = inject(VocabularyApiService);

  // ─── Trạng thái BẢNG danh sách ───
  readonly items = signal<Vocabulary[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  keywordText = ''; // ô tìm kiếm (bind ngModel)

  // ─── Trạng thái POPUP (dùng chung cho tạo & sửa) ───
  readonly modalOpen = signal(false);
  readonly editingId = signal<number | null>(null); // null = đang TẠO MỚI
  readonly saving = signal(false);
  readonly formError = signal<string | null>(null);
  readonly modalTitle = computed(() =>
    this.editingId() === null
      ? 'Tạo từ vựng mới'
      : `Sửa từ vựng #${this.editingId()}`
  );

  // form bind [(ngModel)] — dùng object thường (không phải signal) để bind 2 chiều gọn.
  form: VocabForm = emptyForm();

  constructor() {
    this.load();
  }

  /** Tải danh sách: GET /vocabulary (kèm keyword nếu có). */
  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.api.list(this.keywordText).subscribe({
      next: (data) => {
        this.items.set(data);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.error.set(this.readError(err));
        this.loading.set(false);
      },
    });
  }

  /** Bấm tìm kiếm / Enter trong ô search. */
  search(): void {
    this.load();
  }

  // ─── Mở popup ở chế độ TẠO MỚI ───
  openCreate(): void {
    this.editingId.set(null);
    this.form = emptyForm();
    this.formError.set(null);
    this.modalOpen.set(true);
  }

  // ─── Mở popup ở chế độ SỬA (đổ dữ liệu dòng đang chọn vào form) ───
  openEdit(v: Vocabulary): void {
    this.editingId.set(v.vocabularyId);
    this.form = {
      word: v.word ?? '',
      meaning: v.meaning ?? '',
      partOfSpeech: v.partOfSpeech ?? '',
      pronunciation: v.pronunciation ?? '',
      example: v.example ?? '',
      level: v.level ?? '',
    };
    this.formError.set(null);
    this.modalOpen.set(true);
  }

  closeModal(): void {
    if (this.saving()) return; // đang lưu thì không cho đóng
    this.modalOpen.set(false);
  }

  /** Lưu form: TẠO mới hoặc CẬP NHẬT tuỳ editingId. */
  save(): void {
    // Validate phía client: word + meaning bắt buộc (khớp @NotBlank của BE).
    if (!this.form.word.trim() || !this.form.meaning.trim()) {
      this.formError.set('Vui lòng nhập tối thiểu "Word" và "Meaning".');
      return;
    }

    const payload = this.toPayload(this.form);
    this.saving.set(true);
    this.formError.set(null);

    const id = this.editingId();
    const request$ =
      id === null ? this.api.create(payload) : this.api.update(id, payload);

    request$.subscribe({
      next: () => {
        this.saving.set(false);
        this.modalOpen.set(false);
        this.load(); // tải lại bảng để thấy thay đổi
      },
      error: (err: HttpErrorResponse) => {
        this.saving.set(false);
        this.formError.set(this.readError(err));
      },
    });
  }

  /** Xoá 1 bản ghi (có hỏi xác nhận). */
  remove(v: Vocabulary): void {
    const ok = confirm(`Xoá từ "${v.word}" (id ${v.vocabularyId})?`);
    if (!ok) return;
    this.api.remove(v.vocabularyId).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error.set(this.readError(err)),
    });
  }

  // ─── Helpers ───

  /** Chuyển form -> payload: field rỗng thì gửi undefined (BE coi như không có). */
  private toPayload(f: VocabForm): VocabularyPayload {
    const clean = (s: string): string | undefined => {
      const t = s.trim();
      return t.length ? t : undefined;
    };
    return {
      word: f.word.trim(),
      meaning: f.meaning.trim(),
      partOfSpeech: clean(f.partOfSpeech),
      pronunciation: clean(f.pronunciation),
      example: clean(f.example),
      level: clean(f.level),
    };
  }

  /** Lấy message lỗi thân thiện từ response lỗi của BE. */
  private readError(err: HttpErrorResponse): string {
    const body = err.error as
      | { description?: string; keyMessage?: string }
      | null;
    if (body?.description) return body.description;
    if (body?.keyMessage) return body.keyMessage;
    if (err.status === 0) {
      return 'Không kết nối được tới BE (http://localhost:8062). BE đã chạy chưa?';
    }
    return `Lỗi ${err.status}: ${err.message}`;
  }
}
