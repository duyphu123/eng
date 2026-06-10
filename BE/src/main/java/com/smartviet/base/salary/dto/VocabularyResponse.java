package com.smartviet.base.salary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * VocabularyResponse — DTO TRẢ VỀ (response Data Transfer Object).
 *
 * <p>DTO = object dùng riêng để TRUYỀN DỮ LIỆU ra/vào API, TÁCH biệt khỏi entity.
 * Vì sao không trả thẳng entity Vocabulary cho client?
 *   - Ẩn bớt field không cần lộ ra ngoài (ở đây KHÔNG trả createUser/updateUser).
 *   - Tránh dính chặt JSON API vào cấu trúc bảng DB; API và DB tiến hoá độc lập.
 *
 * <p>VỊ TRÍ TRONG LUỒNG: Service map entity -> DTO này (hàm toResponse) rồi trả lên Controller;
 * Controller bọc nó trong ExecutionResult và trả JSON về cho client.
 *
 * <p>Giải thích annotation:
 *   @JsonInclude(NON_NULL)        → field nào null thì BỎ khỏi JSON cho gọn.
 *   @FieldDefaults(level=PRIVATE) → mọi field mặc định là private (đỡ phải gõ "private").
 *   @Data                         → Lombok gộp getter/setter/toString/equals/hashCode.
 *   @Builder                      → tạo object theo kiểu builder (dùng trong toResponse).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VocabularyResponse {
    Long vocabularyId;              // id để client dùng cho các lời gọi GET/PUT/DELETE sau này
    String word;
    String meaning;
    String partOfSpeech;
    String pronunciation;
    String example;
    String level;
    String status;
    LocalDateTime createDatetime;   // chỉ trả THỜI ĐIỂM tạo...
    LocalDateTime updateDatetime;   // ...và THỜI ĐIỂM sửa (cố ý KHÔNG trả tên user tạo/sửa)
}
