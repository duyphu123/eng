package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * Vocabulary — ENTITY (thực thể) của module học từ vựng.
 *
 * <p>Entity = 1 class Java ánh xạ (mapping) 1-1 với 1 BẢNG trong database.
 * Mỗi object Vocabulary tương ứng 1 DÒNG trong bảng `vocabulary`; mỗi field tương ứng 1 CỘT.
 * JPA/Hibernate đọc các annotation @Table/@Column ở dưới để biết cách đọc–ghi
 * giữa object Java và bảng SQL — nhờ vậy ta KHÔNG phải tự viết câu SQL.
 *
 * <p>VỊ TRÍ TRONG LUỒNG:
 *   Controller → Service → Repository → (Entity) ⇆ bảng `vocabulary`.
 * Đây là tầng "sát DB nhất". Câu lệnh tạo bảng tương ứng nằm ở db/eng_vocabulary.sql.
 *
 * <p>Các annotation Lombok ở đầu class sinh sẵn code lặp lại lúc biên dịch:
 *   @Getter/@Setter                       → tự tạo getX()/setX() cho mọi field.
 *   @NoArgsConstructor/@AllArgsConstructor → constructor rỗng + constructor đủ tham số.
 *   @Builder                              → cho phép tạo object kiểu Vocabulary.builder().word("..").build().
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity                          // Đánh dấu: đây là 1 entity JPA (được Hibernate quản lý & ánh xạ tới DB).
@Table(name = "vocabulary")      // Tên BẢNG trong DB mà entity này ánh xạ tới.
public class Vocabulary {

    /**
     * Khoá chính (primary key) — định danh duy nhất của mỗi dòng trong bảng.
     * @GeneratedValue(IDENTITY): để DB tự tăng dần (AUTO_INCREMENT), ta không cần tự gán id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vocabulary_id", nullable = false)
    private Long vocabularyId;

    // Tên field (camelCase) khác tên cột (snake_case) -> phải khai báo @Column(name=...) để JPA map đúng.
    @Column(name = "word", length = 255)          // từ tiếng Anh, vd "invoice"
    private String word;

    @Column(name = "meaning", length = 512)       // nghĩa tiếng Việt, vd "hoá đơn"
    private String meaning;

    @Column(name = "part_of_speech", length = 50) // từ loại: noun / verb / adj ...
    private String partOfSpeech;

    @Column(name = "pronunciation", length = 255) // phiên âm, vd /ˈɪnvɔɪs/
    private String pronunciation;

    @Column(name = "example", length = 1000)      // câu ví dụ minh hoạ
    private String example;

    @Column(name = "`level`", length = 20)   // backtick: `level` là từ khoá SQL ở vài engine -> quote cho an toàn
    private String level;                    // cấp độ, vd "TOEIC 600"

    @Column(name = "status", length = 1)          // trạng thái: "1" = đang dùng, "0" = ẩn (xem Constants.Status)
    private String status;

    // 4 cột audit dưới đây — ghi lại NGƯỜI nào tạo/sửa và LÚC NÀO. Trong module này được điền TAY ở Service (mặc định "system").
    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "create_user", length = 50)
    private String createUser;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @Column(name = "update_user", length = 50)
    private String updateUser;

}
