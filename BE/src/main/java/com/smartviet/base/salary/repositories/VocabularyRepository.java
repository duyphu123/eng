package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VocabularyRepository — REPOSITORY (kho dữ liệu) của module từ vựng.
 *
 * <p>Đây chỉ là 1 INTERFACE rỗng, nhưng nhờ Spring Data JPA, lúc chạy Spring sẽ
 * TỰ TẠO một class hiện thực (implementation) và TỰ VIẾT câu SQL cho ta.
 * Ta không cần (và không nên) tự viết class kết nối DB hay câu SQL CRUD cơ bản.
 *
 * <p>Khi kế thừa JpaRepository&lt;Vocabulary, Long&gt; ta ĐÃ CÓ SẴN các hàm CRUD:
 *   findAll(), findById(id), save(entity), delete(entity), count() ...
 *   - Vocabulary = kiểu entity mà kho này quản lý.
 *   - Long       = kiểu của khoá chính (vocabularyId).
 *
 * <p>VỊ TRÍ TRONG LUỒNG: Service gọi xuống Repository; Repository nói chuyện trực tiếp với bảng DB.
 */
@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    /**
     * "Derived query" (truy vấn SUY RA TỪ TÊN HÀM): Spring đọc tên method rồi
     * tự sinh câu SQL — ta chỉ cần đặt tên đúng quy ước, KHÔNG phải viết SQL.
     *
     * <p>Tên này được Spring/Hibernate dịch thành (đại ý):
     * <pre>
     *   SELECT * FROM vocabulary
     *   WHERE UPPER(word)    LIKE UPPER(?)   -- WordContainingIgnoreCase
     *      OR UPPER(meaning) LIKE UPPER(?)   -- OrMeaningContainingIgnoreCase
     * </pre>
     * (IgnoreCase -> Hibernate dùng UPPER(...) ở cả 2 vế; dấu % được tự thêm vào GIÁ TRỊ bind,
     *  vd "%keyword%", chứ KHÔNG nằm thẳng trong câu SQL.)
     *
     * <p>Dùng cho chức năng tìm kiếm: gõ 1 từ khoá, khớp ở CẢ cột word lẫn meaning,
     * không phân biệt hoa/thường (IgnoreCase). Vì có 2 điều kiện nên truyền keyword
     * vào 2 tham số (word, meaning) — xem cách gọi trong VocabularyServiceImpl.getAll().
     */
    List<Vocabulary> findByWordContainingIgnoreCaseOrMeaningContainingIgnoreCase(String word, String meaning);

}
