package com.smartviet.base.salary.controllers;

import com.smartviet.base.salary.common.Constants;
import com.smartviet.base.salary.dto.VocabularyResponse;
import com.smartviet.base.salary.dto.common.ExecutionResult;
import com.smartviet.base.salary.dto.requests.VocabularyCreateRequest;
import com.smartviet.base.salary.dto.requests.VocabularyUpdateRequest;
import com.smartviet.base.salary.services.iservice.VocabularyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * VocabularyController — TẦNG TIẾP NHẬN HTTP (cổng vào API) của module từ vựng.
 *
 * <p>Nhiệm vụ: nhận request HTTP, (validate nếu cần), gọi Service xử lý, rồi bọc kết quả vào
 * "vỏ" ExecutionResult và trả JSON về. Controller KHÔNG chứa logic nghiệp vụ — chỉ điều phối
 * (nghiệp vụ nằm ở Service).
 *
 * <p>Annotation:
 *   @RestController                → mỗi method trả thẳng dữ liệu (JSON), không trả tên view/HTML.
 *   @RequestMapping("/vocabulary") → tiền tố URL chung cho mọi endpoint trong class này.
 *   @RequiredArgsConstructor       → Lombok sinh constructor để Spring tiêm VocabularyService vào.
 *
 * <p>"Vỏ bọc" ExecutionResult&lt;T&gt; là QUY ƯỚC chung của dự án: MỌI endpoint đều trả
 * ResponseEntity&lt;ExecutionResult&lt;T&gt;&gt; gồm responseCode + data, để client xử lý đồng nhất.
 *
 * <p>LƯU Ý bảo mật: /vocabulary/** đang được mở TẠM trong SecurityFilter (WHITE_LIST_URL) để tiện
 * học CRUD; khi hoàn thiện sẽ siết lại (yêu cầu đăng nhập / JWT).
 */
@Slf4j
@RestController
@RequestMapping("/vocabulary")
@RequiredArgsConstructor
public class VocabularyController {

    // Controller chỉ phụ thuộc INTERFACE service (không phụ thuộc class Impl). Spring tiêm bản Impl lúc chạy.
    private final VocabularyService vocabularyService;

    // GET /vocabulary           -> tất cả
    // GET /vocabulary?keyword=x -> tìm theo word/meaning
    @GetMapping
    public ResponseEntity<ExecutionResult<List<VocabularyResponse>>> getAll(
            @RequestParam(value = "keyword", required = false) String keyword) {   // ?keyword=... ; required=false -> được phép bỏ trống
        return ResponseEntity.ok(                                  // 200 OK + body bên dưới
                ExecutionResult.<List<VocabularyResponse>>builder()
                        .responseCode(Constants.ExecutionCode.SUCCESS)   // "0" = thành công (theo Constants.ExecutionCode)
                        .data(vocabularyService.getAll(keyword))         // dữ liệu thật do Service trả về
                        .build()
        );
    }

    // GET /vocabulary/{id}  -> 1 bản ghi (không thấy -> Service ném LogicException -> trả lỗi)
    @GetMapping("/{id}")
    public ResponseEntity<ExecutionResult<VocabularyResponse>> getById(@PathVariable("id") Long id) {  // {id} trên URL -> tham số id
        return ResponseEntity.ok(
                ExecutionResult.<VocabularyResponse>builder()
                        .responseCode(Constants.ExecutionCode.SUCCESS)
                        .data(vocabularyService.getById(id))
                        .build()
        );
    }

    // POST /vocabulary  -> tạo mới (@Valid: dữ liệu sai ràng buộc -> 400 TỰ ĐỘNG qua GlobalExceptionHandler)
    @PostMapping("/create")
    public ResponseEntity<ExecutionResult<VocabularyResponse>> create(
            @Valid @RequestBody VocabularyCreateRequest request) {   // @RequestBody: đọc JSON body thành object; @Valid: bật kiểm tra ràng buộc
        return ResponseEntity.ok(
                ExecutionResult.<VocabularyResponse>builder()
                        .responseCode(Constants.ExecutionCode.SUCCESS)
                        .data(vocabularyService.create(request))
                        .build()
        );
    }

    // POST /vocabulary/{id}/update  -> CẬP NHẬT. Theo quy tắc dự án: mọi thao tác GHI đều dùng POST
    //   (KHÔNG dùng PUT). id lấy từ URL, dữ liệu mới lấy từ JSON body; @Valid: sai ràng buộc -> 400 tự động.
    @PostMapping("/{id}/update")
    public ResponseEntity<ExecutionResult<VocabularyResponse>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody VocabularyUpdateRequest request) {
        return ResponseEntity.ok(
                ExecutionResult.<VocabularyResponse>builder()
                        .responseCode(Constants.ExecutionCode.SUCCESS)
                        .data(vocabularyService.update(id, request))
                        .build()
        );
    }

    // POST /vocabulary/{id}/delete  -> XOÁ cứng. Dùng POST thay cho DELETE; trả về id đã xoá.
    @PostMapping("/{id}/delete")
    public ResponseEntity<ExecutionResult<Long>> delete(@PathVariable("id") Long id) {
        vocabularyService.delete(id);
        return ResponseEntity.ok(
                ExecutionResult.<Long>builder()
                        .responseCode(Constants.ExecutionCode.SUCCESS)
                        .data(id)
                        .build()
        );
    }

}
