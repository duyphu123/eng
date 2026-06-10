import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';

/**
 * VocabularyApiService — tầng GỌI API tới BE (salary-service) cho module Từ vựng.
 *
 * <p>Đây là "cầu nối" giữa FE Angular và BE Spring Boot. Component chỉ gọi các method
 * ở đây (list/create/update/remove), KHÔNG tự dựng URL hay xử lý "vỏ bọc" response.
 *
 * <p>Mọi response của BE đều bọc trong ExecutionResult&lt;T&gt; (data + responseCode + ...).
 * Các method dưới đây tự "bóc" lấy `data` rồi trả ra cho gọn (qua toán tử map của RxJS).
 */

/** 1 bản ghi từ vựng trả về (khớp VocabularyResponse phía Java). */
export interface Vocabulary {
  vocabularyId: number;
  word: string;
  meaning: string;
  partOfSpeech?: string;
  pronunciation?: string;
  example?: string;
  level?: string;
  status?: string;
  createDatetime?: string;
  updateDatetime?: string;
}

/** Dữ liệu GỬI LÊN khi tạo/sửa (khớp VocabularyCreateRequest/UpdateRequest). */
export interface VocabularyPayload {
  word: string;
  meaning: string;
  partOfSpeech?: string;
  pronunciation?: string;
  example?: string;
  level?: string;
}

/** "Vỏ bọc" chuẩn của MỌI response BE — dữ liệu thật nằm trong `data`. */
export interface ExecutionResult<T> {
  data: T;
  responseCode: string; // "0" = thành công
  keyMessage?: string;
  description?: string;
  timestamp?: string;
  path?: string;
}

@Injectable({ providedIn: 'root' })
export class VocabularyApiService {
  private readonly http = inject(HttpClient);

  // URL gốc của BE = port 8062 + context-path /api/cab-service + /vocabulary.
  // (URL DEV cố định; nếu sau này tách môi trường thì chuyển vào environment.)
  private readonly baseUrl = 'http://localhost:8062/api/cab-service/vocabulary';

  /** GET /vocabulary (kèm ?keyword= nếu có) — lấy danh sách / tìm kiếm. */
  list(keyword?: string): Observable<Vocabulary[]> {
    let params = new HttpParams();
    if (keyword && keyword.trim()) {
      params = params.set('keyword', keyword.trim());
    }
    return this.http
      .get<ExecutionResult<Vocabulary[]>>(this.baseUrl, { params })
      .pipe(map((res) => res.data));
  }

  /** GET /vocabulary/{id} — lấy chi tiết 1 bản ghi. */
  getById(id: number): Observable<Vocabulary> {
    return this.http
      .get<ExecutionResult<Vocabulary>>(`${this.baseUrl}/${id}`)
      .pipe(map((res) => res.data));
  }

  /** POST /vocabulary/create — tạo mới (quy tắc dự án: thao tác GHI = POST). */
  create(payload: VocabularyPayload): Observable<Vocabulary> {
    return this.http
      .post<ExecutionResult<Vocabulary>>(`${this.baseUrl}/create`, payload)
      .pipe(map((res) => res.data));
  }

  /** POST /vocabulary/{id}/update — cập nhật. */
  update(id: number, payload: VocabularyPayload): Observable<Vocabulary> {
    return this.http
      .post<ExecutionResult<Vocabulary>>(`${this.baseUrl}/${id}/update`, payload)
      .pipe(map((res) => res.data));
  }

  /** POST /vocabulary/{id}/delete — xoá; BE trả về id đã xoá. */
  remove(id: number): Observable<number> {
    return this.http
      .post<ExecutionResult<number>>(`${this.baseUrl}/${id}/delete`, {})
      .pipe(map((res) => res.data));
  }
}
