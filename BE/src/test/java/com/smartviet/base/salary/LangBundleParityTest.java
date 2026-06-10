package com.smartviet.base.salary;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Đảm bảo 4 bundle ngôn ngữ (en/vi/lo/zh) có CÙNG tập key.
 * Lý do: ResourceConfig bật setUseCodeAsDefaultMessage(true) -> key thiếu KHÔNG ném lỗi
 * mà lộ "raw key" (vd: "vocabulary.not.found") ra response. Test này thay cho cảnh báo đó.
 * Không cần Spring context / DB -> chạy rất nhanh.
 *
 * <p>Liên quan module Vocabulary: mỗi khi thêm key lỗi mới (vd vocabulary.missing.word) ở
 * ResponseMessage, phải thêm ĐỦ câu dịch ở CẢ 4 file lang_*.properties — nếu quên file nào,
 * test này sẽ "fail" và chỉ ra file/key còn thiếu.
 */
class LangBundleParityTest {

    // 4 mã ngôn ngữ, tương ứng 4 file static/lang_<mã>.properties
    private static final String[] LANGS = {"en", "vi", "lo", "zh"};

    /** Đọc 1 file lang_<lang>.properties trong classpath thành đối tượng Properties (đọc UTF-8 để không lỗi dấu tiếng Việt). */
    private Properties load(String lang) throws Exception {
        String path = "static/lang_" + lang + ".properties";
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {   // try-with-resources: tự đóng stream khi xong
            if (in == null) {
                fail("Không tìm thấy bundle: " + path);   // thiếu hẳn file -> fail luôn
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        return props;
    }

    @Test
    void allBundlesHaveSameKeys() throws Exception {
        // Bước 1: gộp TẤT CẢ key của 4 file thành 1 tập "union" (TreeSet -> tự sắp xếp & loại trùng).
        Set<String> union = new TreeSet<>();
        Properties[] all = new Properties[LANGS.length];
        for (int i = 0; i < LANGS.length; i++) {
            all[i] = load(LANGS[i]);
            union.addAll(all[i].stringPropertyNames());
        }

        // Bước 2: với từng file, tìm xem nó THIẾU key nào so với union -> gom vào thông báo lỗi.
        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < LANGS.length; i++) {
            Set<String> missing = new TreeSet<>(union);
            missing.removeAll(all[i].stringPropertyNames());   // union trừ đi key của file đang xét = phần file đó còn thiếu
            if (!missing.isEmpty()) {
                errors.append("\n  lang_").append(LANGS[i]).append(" thiếu key: ").append(missing);
            }
        }

        // Bước 3: nếu có bất kỳ file nào thiếu key -> đánh fail test kèm danh sách thiếu.
        if (errors.length() > 0) {
            fail("Các bundle ngôn ngữ lệch key:" + errors);
        }
    }
}
