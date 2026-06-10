package com.smartviet.base.salary.dto.requests;

import com.smartviet.base.salary.common.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * VocabularyUpdateRequest — DTO NHẬN VÀO khi CẬP NHẬT (POST /vocabulary/{id}/update).
 *
 * <p>Theo quy tắc dự án: mọi thao tác GHI (tạo/sửa/xoá) đều dùng HTTP method POST,
 * KHÔNG dùng PUT/DELETE. Vì vậy cập nhật là POST tới /vocabulary/{id}/update.
 *
 * <p>Hiện giống hệt VocabularyCreateRequest về field và ràng buộc, nhưng cố ý tách
 * thành 2 class riêng để: (1) ý nghĩa rõ ràng (tạo mới vs. sửa), và (2) sau này nếu
 * quy tắc tạo/sửa khác nhau thì chỉnh độc lập, không ảnh hưởng lẫn nhau.
 *
 * <p>LƯU Ý: ở đây KHÔNG có field id. id của bản ghi cần sửa lấy từ URL
 * (/vocabulary/{id}/update) qua @PathVariable trong Controller, KHÔNG lấy từ body.
 * (Quy tắc ràng buộc giống Create — xem giải thích chi tiết ở VocabularyCreateRequest.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VocabularyUpdateRequest {

    @NotBlank(message = ResponseMessage.Vocabulary.MISSING_WORD)
    @Size(max = 255, message = ResponseMessage.Vocabulary.INVALID_WORD_LENGTH)
    String word;        // BẮT BUỘC

    @NotBlank(message = ResponseMessage.Vocabulary.MISSING_MEANING)
    @Size(max = 512, message = ResponseMessage.Vocabulary.INVALID_MEANING_LENGTH)
    String meaning;     // BẮT BUỘC

    @Size(max = 50, message = ResponseMessage.Vocabulary.INVALID_PARTOFSPEECH_LENGTH)
    String partOfSpeech;

    @Size(max = 255, message = ResponseMessage.Vocabulary.INVALID_PRONUNCIATION_LENGTH)
    String pronunciation;

    @Size(max = 1000, message = ResponseMessage.Vocabulary.INVALID_EXAMPLE_LENGTH)
    String example;

    @Size(max = 20, message = ResponseMessage.Vocabulary.INVALID_LEVEL_LENGTH)
    String level;
}
