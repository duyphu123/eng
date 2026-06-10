package com.smartviet.base.salary.dto.requests;

import com.smartviet.base.salary.common.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * VocabularyCreateRequest — DTO NHẬN VÀO khi TẠO MỚI (POST /vocabulary).
 *
 * <p>Đây là "khuôn" dữ liệu mà client phải gửi lên trong JSON body. Khác với entity:
 * KHÔNG có id (DB tự sinh) và KHÔNG có các cột audit/status (Service tự điền).
 *
 * <p>RÀNG BUỘC dữ liệu (Bean Validation) được đặt ngay bằng annotation. Khi Controller
 * đánh dấu tham số bằng @Valid, Spring TỰ kiểm tra trước khi vào Service:
 *   - @NotBlank      : bắt buộc có, không được rỗng / chỉ toàn khoảng trắng.
 *   - @Size(max=...) : giới hạn độ dài (khớp với length của cột bên entity/bảng).
 *   - message = ResponseMessage.Vocabulary.* : KHÔNG phải câu lỗi viết thẳng, mà là 1 KEY
 *       -> được tra ra câu tiếng Việt/Anh... tương ứng trong file static/lang_*.properties (i18n).
 * Nếu sai, request bị chặn và GlobalExceptionHandler tự trả lỗi 400 — ta không phải tự viết if-check.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VocabularyCreateRequest {

    @NotBlank(message = ResponseMessage.Vocabulary.MISSING_WORD)
    @Size(max = 255, message = ResponseMessage.Vocabulary.INVALID_WORD_LENGTH)
    String word;        // BẮT BUỘC

    @NotBlank(message = ResponseMessage.Vocabulary.MISSING_MEANING)
    @Size(max = 512, message = ResponseMessage.Vocabulary.INVALID_MEANING_LENGTH)
    String meaning;     // BẮT BUỘC

    // 4 field dưới KHÔNG có @NotBlank -> không bắt buộc; chỉ giới hạn độ dài NẾU có nhập.
    @Size(max = 50, message = ResponseMessage.Vocabulary.INVALID_PARTOFSPEECH_LENGTH)
    String partOfSpeech;

    @Size(max = 255, message = ResponseMessage.Vocabulary.INVALID_PRONUNCIATION_LENGTH)
    String pronunciation;

    @Size(max = 1000, message = ResponseMessage.Vocabulary.INVALID_EXAMPLE_LENGTH)
    String example;

    @Size(max = 20, message = ResponseMessage.Vocabulary.INVALID_LEVEL_LENGTH)
    String level;
}
