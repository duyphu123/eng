package com.smartviet.base.salary.services;

import com.smartviet.base.salary.common.Constants;
import com.smartviet.base.salary.common.ResponseMessage;
import com.smartviet.base.salary.dto.VocabularyResponse;
import com.smartviet.base.salary.dto.requests.VocabularyCreateRequest;
import com.smartviet.base.salary.dto.requests.VocabularyUpdateRequest;
import com.smartviet.base.salary.entities.Vocabulary;
import com.smartviet.base.salary.exceptions.LogicException;
import com.smartviet.base.salary.repositories.VocabularyRepository;
import com.smartviet.base.salary.services.iservice.VocabularyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * VocabularyServiceImpl — HIỆN THỰC (implementation) của tầng nghiệp vụ.
 *
 * <p>Đây là nơi chứa "logic nghiệp vụ" thật: kiểm tra tồn tại, map dữ liệu, điền cột
 * audit/status, rồi gọi Repository để đọc/ghi DB. Controller gọi xuống đây THÔNG QUA
 * interface VocabularyService (không gọi thẳng class này).
 *
 * <p>Annotation:
 *   @Service                 → báo Spring đây là 1 bean tầng service (Spring tự tạo & quản lý).
 *   @RequiredArgsConstructor → Lombok tự sinh constructor cho các field "final"; Spring dùng
 *                              constructor đó để TIÊM (inject) VocabularyRepository vào (constructor injection).
 *   @Slf4j                   → Lombok tạo sẵn biến logger tên "log" để ghi log khi cần.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyServiceImpl implements VocabularyService {

    // Người tạo/sửa mặc định. Tạm hardcode "system" vì endpoint đang mở (chưa gắn user đăng nhập).
    private static final String SYSTEM_USER = "system";

    // Phụ thuộc vào kho dữ liệu; được Spring tiêm qua constructor (nhờ @RequiredArgsConstructor).
    private final VocabularyRepository vocabularyRepository;

    /**
     * READ - danh sách. @Transactional(readOnly=true): mở transaction CHỈ ĐỌC (nhẹ & an toàn hơn).
     * keyword rỗng -> findAll(); có keyword -> tìm gần đúng theo word HOẶC meaning.
     * Cuối cùng map mỗi entity -> DTO trước khi trả ra (không trả entity ra ngoài API).
     */
    @Override
    @Transactional(readOnly = true)
    public List<VocabularyResponse> getAll(String keyword) {
        List<Vocabulary> vocabularies = StringUtils.isBlank(keyword)   // isBlank: null / "" / toàn khoảng trắng đều coi là rỗng
                ? vocabularyRepository.findAll()
                : vocabularyRepository.findByWordContainingIgnoreCaseOrMeaningContainingIgnoreCase(keyword, keyword);
        return vocabularies.stream().map(this::toResponse).toList();    // chuyển từng entity -> DTO
    }

    /** READ - 1 bản ghi. Không thấy -> findOrThrow ném LogicException (xem cuối file). */
    @Override
    @Transactional(readOnly = true)
    public VocabularyResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    /**
     * CREATE - tạo mới. @Transactional (đọc-ghi): nếu giữa chừng lỗi thì rollback (huỷ) toàn bộ.
     * Dựng entity từ request bằng builder, TỰ điền status + 4 cột audit, rồi save().
     * save() trả về entity đã có id (do DB sinh) -> map sang DTO để trả về.
     */
    @Override
    @Transactional
    public VocabularyResponse create(VocabularyCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();   // dùng chung 1 mốc thời gian cho cả create & update datetime
        Vocabulary vocabulary = Vocabulary.builder()
                .word(request.getWord())
                .meaning(request.getMeaning())
                .partOfSpeech(request.getPartOfSpeech())
                .pronunciation(request.getPronunciation())
                .example(request.getExample())
                .level(request.getLevel())
                .status(Constants.Status.ACTIVE_STR)   // "1" = đang dùng (hằng số trong Constants.Status)
                .createDatetime(now)
                .createUser(SYSTEM_USER)
                .updateDatetime(now)
                .updateUser(SYSTEM_USER)
                .build();
        return toResponse(vocabularyRepository.save(vocabulary));
    }

    /**
     * UPDATE - cập nhật. Endpoint dùng POST (/vocabulary/{id}/update) theo quy tắc dự án, KHÔNG dùng PUT.
     * Trước hết findOrThrow để chắc chắn bản ghi tồn tại (không thì trả lỗi).
     * Gán đè các field từ request lên entity, cập nhật mốc sửa, rồi save().
     * (Chỉ sửa nội dung + cột "update*"; KHÔNG đụng tới createDatetime/createUser.)
     */
    @Override
    @Transactional
    public VocabularyResponse update(Long id, VocabularyUpdateRequest request) {
        Vocabulary vocabulary = findOrThrow(id);
        vocabulary.setWord(request.getWord());
        vocabulary.setMeaning(request.getMeaning());
        vocabulary.setPartOfSpeech(request.getPartOfSpeech());
        vocabulary.setPronunciation(request.getPronunciation());
        vocabulary.setExample(request.getExample());
        vocabulary.setLevel(request.getLevel());
        vocabulary.setUpdateDatetime(LocalDateTime.now());
        vocabulary.setUpdateUser(SYSTEM_USER);
        return toResponse(vocabularyRepository.save(vocabulary));
    }

    /**
     * DELETE - xoá cứng. Endpoint dùng POST (/vocabulary/{id}/delete) theo quy tắc dự án, KHÔNG dùng DELETE.
     * findOrThrow trước (để báo lỗi rõ nếu id không tồn tại), rồi delete().
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Vocabulary vocabulary = findOrThrow(id);
        vocabularyRepository.delete(vocabulary);
    }

    /**
     * Hàm dùng chung: tìm theo id, KHÔNG thấy thì ném LogicException với key i18n NOT_FOUND.
     * LogicException được GlobalExceptionHandler bắt -> trả response lỗi chuẩn cho client.
     * (Gom vào 1 chỗ để getById / update / delete dùng lại, tránh lặp code.)
     */
    private Vocabulary findOrThrow(Long id) {
        return vocabularyRepository.findById(id)
                .orElseThrow(() -> new LogicException(ResponseMessage.Vocabulary.NOT_FOUND));
    }

    /**
     * Map (chuyển đổi) 1 entity Vocabulary -> 1 VocabularyResponse (DTO).
     * Cố ý KHÔNG sao chép createUser/updateUser sang DTO -> không lộ tên người tạo/sửa ra API.
     */
    private VocabularyResponse toResponse(Vocabulary vocabulary) {
        return VocabularyResponse.builder()
                .vocabularyId(vocabulary.getVocabularyId())
                .word(vocabulary.getWord())
                .meaning(vocabulary.getMeaning())
                .partOfSpeech(vocabulary.getPartOfSpeech())
                .pronunciation(vocabulary.getPronunciation())
                .example(vocabulary.getExample())
                .level(vocabulary.getLevel())
                .status(vocabulary.getStatus())
                .createDatetime(vocabulary.getCreateDatetime())
                .updateDatetime(vocabulary.getUpdateDatetime())
                .build();
    }
}
