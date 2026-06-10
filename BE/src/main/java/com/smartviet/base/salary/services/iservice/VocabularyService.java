package com.smartviet.base.salary.services.iservice;

import com.smartviet.base.salary.dto.VocabularyResponse;
import com.smartviet.base.salary.dto.requests.VocabularyCreateRequest;
import com.smartviet.base.salary.dto.requests.VocabularyUpdateRequest;

import java.util.List;

/**
 * VocabularyService — HỢP ĐỒNG (interface) của tầng nghiệp vụ (service layer).
 *
 * <p>Theo "house style" của dự án, tầng service tách làm 2 phần:
 *   - interface (file NÀY) ở package services/iservice/ : KHAI BÁO "làm được những gì".
 *   - class hiện thực VocabularyServiceImpl ở package services/ : VIẾT "làm thế nào".
 *
 * <p>Controller chỉ phụ thuộc vào interface này, KHÔNG phụ thuộc vào class cụ thể.
 * Nhờ đó có thể đổi cách hiện thực (vd thêm cache) mà không phải sửa Controller
 * — đây là kỹ thuật "lập trình hướng giao diện" (program to an interface).
 *
 * <p>Mỗi method tương ứng 1 thao tác CRUD và sẽ được 1 endpoint trong Controller gọi tới.
 */
public interface VocabularyService {

    /** Lấy danh sách từ vựng. keyword rỗng -> lấy tất cả; có keyword -> tìm theo word/meaning. */
    List<VocabularyResponse> getAll(String keyword);

    /** Lấy 1 từ vựng theo id. Không tìm thấy -> ném LogicException (phần Impl xử lý). */
    VocabularyResponse getById(Long id);

    /** Tạo mới 1 từ vựng từ dữ liệu client gửi lên. */
    VocabularyResponse create(VocabularyCreateRequest request);

    /** Cập nhật từ vựng có id cho trước bằng dữ liệu mới. */
    VocabularyResponse update(Long id, VocabularyUpdateRequest request);

    /** Xoá 1 từ vựng theo id (xoá cứng khỏi DB). */
    void delete(Long id);

}
