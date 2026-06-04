-- ============================================================
--  FLOW SCHEDULER – MariaDB Complete Implementation
--  Mô tả: Hệ thống lên lịch luồng xử lý tuần tự nhiều bước,
--         mỗi bước gồm nhiều event chạy song song.
--  Database : MariaDB >= 10.4
--  Ngày tạo : 2026-04-22
-- ============================================================

-- Đặt database đang dùng (thay tên cho phù hợp)
-- USE salary_service;

-- Bật Event Scheduler (cần quyền SUPER hoặc SYSTEM_VARIABLES_ADMIN)
SET GLOBAL event_scheduler = ON;

-- ============================================================
--  SECTION 1: CREATE TABLES
-- ============================================================

-- 1.1 Định nghĩa luồng xử lý
CREATE TABLE IF NOT EXISTS flow_definition (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    flow_code   VARCHAR(100)    NOT NULL COMMENT 'Mã luồng – unique',
    flow_name   VARCHAR(255)    NOT NULL COMMENT 'Tên luồng',
    description TEXT                     COMMENT 'Mô tả',
    is_active   TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '1=active',
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_flow_code (flow_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='Định nghĩa các luồng xử lý';

-- 1.2 Định nghĩa các bước (tuần tự) trong luồng
CREATE TABLE IF NOT EXISTS flow_step_definition (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    flow_id         BIGINT          NOT NULL COMMENT 'FK → flow_definition.id',
    step_order      INT             NOT NULL COMMENT 'Thứ tự bước (1,2,3…)',
    step_code       VARCHAR(100)    NOT NULL COMMENT 'Mã bước',
    step_name       VARCHAR(255)    NOT NULL COMMENT 'Tên bước',
    description     TEXT                     COMMENT 'Mô tả bước',
    timeout_minutes INT             NOT NULL DEFAULT 60 COMMENT 'Timeout cả bước (phút)',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_flow_step_order (flow_id, step_order),
    UNIQUE KEY uq_flow_step_code  (flow_id, step_code),
    CONSTRAINT fk_fsd_flow FOREIGN KEY (flow_id)
        REFERENCES flow_definition(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='Định nghĩa các bước tuần tự trong luồng';

-- 1.3 Định nghĩa các event (song song) trong mỗi bước
CREATE TABLE IF NOT EXISTS event_definition (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    step_id             BIGINT          NOT NULL COMMENT 'FK → flow_step_definition.id',
    event_code          VARCHAR(100)    NOT NULL COMMENT 'Mã event',
    event_name          VARCHAR(255)    NOT NULL COMMENT 'Tên event',
    procedure_name      VARCHAR(255)    NOT NULL COMMENT 'Stored procedure sẽ được gọi',
    default_parameters  TEXT                     COMMENT 'Tham số mặc định (JSON)',
    timeout_minutes     INT             NOT NULL DEFAULT 30 COMMENT 'Timeout event (phút)',
    is_active           TINYINT(1)      NOT NULL DEFAULT 1,
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_step_event_code (step_id, event_code),
    CONSTRAINT fk_ed_step FOREIGN KEY (step_id)
        REFERENCES flow_step_definition(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='Định nghĩa các event song song trong mỗi bước';

-- 1.4 Instance thực thi của luồng
--     STATUS: PENDING | RUNNING | COMPLETED | FAILED | CANCELLED
CREATE TABLE IF NOT EXISTS flow_instance (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    flow_id             BIGINT          NOT NULL COMMENT 'FK → flow_definition.id',
    flow_code           VARCHAR(100)    NOT NULL COMMENT 'Snapshot mã luồng',
    instance_key        VARCHAR(255)    NOT NULL COMMENT 'Key unique cho lần chạy (vd: SALARY_2026-03-01)',
    current_step_order  INT             NOT NULL DEFAULT 0 COMMENT '0=chưa bắt đầu',
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING'
                            COMMENT 'PENDING|RUNNING|COMPLETED|FAILED|CANCELLED',
    input_parameters    TEXT                     COMMENT 'Tham số đầu vào JSON',
    scheduled_at        DATETIME                 COMMENT 'Thời điểm lên lịch chạy',
    started_at          DATETIME                 COMMENT 'Thời điểm bắt đầu thực sự',
    completed_at        DATETIME                 COMMENT 'Thời điểm kết thúc',
    error_message       TEXT                     COMMENT 'Thông báo lỗi nếu FAILED',
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_instance_key (instance_key),
    KEY idx_fi_status  (status),
    KEY idx_fi_flow_id (flow_id),
    CONSTRAINT fk_fi_flow FOREIGN KEY (flow_id)
        REFERENCES flow_definition(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='Instance thực thi của luồng';

-- 1.5 Log thực thi từng event
--     STATUS: PENDING | RUNNING | SUCCESS | FAILED | TIMEOUT | SKIPPED
CREATE TABLE IF NOT EXISTS event_log (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    flow_instance_id    BIGINT          NOT NULL COMMENT 'FK → flow_instance.id',
    event_definition_id BIGINT          NOT NULL COMMENT 'FK → event_definition.id',
    step_order          INT             NOT NULL COMMENT 'Bước số mấy',
    mariadb_event_name  VARCHAR(255)             COMMENT 'Tên MariaDB EVENT tạo động',
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING'
                            COMMENT 'PENDING|RUNNING|SUCCESS|FAILED|TIMEOUT|SKIPPED',
    input_parameters    TEXT                     COMMENT 'Tham số thực tế JSON',
    result              TEXT                     COMMENT 'Kết quả trả về JSON',
    error_message       TEXT                     COMMENT 'Thông báo lỗi',
    retry_count         INT             NOT NULL DEFAULT 0,
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at          DATETIME,
    completed_at        DATETIME,
    PRIMARY KEY (id),
    KEY idx_el_flow_instance (flow_instance_id),
    KEY idx_el_status        (status),
    KEY idx_el_step          (flow_instance_id, step_order),
    CONSTRAINT fk_el_instance FOREIGN KEY (flow_instance_id)
        REFERENCES flow_instance(id),
    CONSTRAINT fk_el_event_def FOREIGN KEY (event_definition_id)
        REFERENCES event_definition(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  COMMENT='Log thực thi của từng event';


-- ============================================================
--  SECTION 2: STORED PROCEDURES
-- ============================================================

DELIMITER $$

-- ──────────────────────────────────────────────────────────
-- 2.1 sp_create_flow_instance
--     Tạo một instance mới cho luồng, lên lịch chạy
-- ──────────────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS sp_create_flow_instance$$
CREATE PROCEDURE sp_create_flow_instance(
    IN  p_flow_code         VARCHAR(100),
    IN  p_instance_key      VARCHAR(255),
    IN  p_input_parameters  TEXT,       -- JSON
    IN  p_scheduled_at      DATETIME,   -- NULL = chạy ngay
    OUT p_instance_id       BIGINT
)
BEGIN
    DECLARE v_flow_id   BIGINT  DEFAULT NULL;
    DECLARE v_now       DATETIME DEFAULT NOW();

    SELECT id INTO v_flow_id
    FROM   flow_definition
    WHERE  flow_code = p_flow_code
      AND  is_active = 1
    LIMIT 1;

    IF v_flow_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Flow code không tồn tại hoặc đã bị vô hiệu hóa';
    END IF;

    INSERT INTO flow_instance (
        flow_id, flow_code, instance_key,
        current_step_order, status,
        input_parameters, scheduled_at,
        created_at
    ) VALUES (
        v_flow_id, p_flow_code, p_instance_key,
        0, 'PENDING',
        p_input_parameters,
        IFNULL(p_scheduled_at, v_now),
        v_now
    );

    SET p_instance_id = LAST_INSERT_ID();
END$$


-- ──────────────────────────────────────────────────────────
-- 2.2 sp_create_step_events
--     Tạo event_log và MariaDB EVENT động cho từng event trong bước
-- ──────────────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS sp_create_step_events$$
CREATE PROCEDURE sp_create_step_events(
    IN p_flow_instance_id   BIGINT,
    IN p_step_order         INT
)
BEGIN
    DECLARE v_done              INT DEFAULT 0;
    DECLARE v_event_def_id      BIGINT;
    DECLARE v_procedure_name    VARCHAR(255);
    DECLARE v_default_params    TEXT;
    DECLARE v_timeout_min       INT;
    DECLARE v_event_log_id      BIGINT;
    DECLARE v_event_name        VARCHAR(255);
    DECLARE v_input_params      TEXT;
    DECLARE v_flow_id           BIGINT;

    DECLARE cur_events CURSOR FOR
        SELECT  ed.id,
                ed.procedure_name,
                ed.default_parameters,
                ed.timeout_minutes
        FROM    event_definition ed
        JOIN    flow_step_definition fsd ON fsd.id = ed.step_id
        JOIN    flow_instance fi         ON fi.flow_id = fsd.flow_id
        WHERE   fi.id           = p_flow_instance_id
          AND   fsd.step_order  = p_step_order
          AND   ed.is_active    = 1
        ORDER BY ed.id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

    -- Lấy tham số đầu vào từ flow_instance
    SELECT input_parameters, flow_id
    INTO   v_input_params, v_flow_id
    FROM   flow_instance
    WHERE  id = p_flow_instance_id;

    OPEN cur_events;
    event_loop: LOOP
        FETCH cur_events INTO
            v_event_def_id,
            v_procedure_name,
            v_default_params,
            v_timeout_min;

        IF v_done = 1 THEN
            LEAVE event_loop;
        END IF;

        -- Tạo bản ghi event_log
        INSERT INTO event_log (
            flow_instance_id, event_definition_id,
            step_order, status,
            input_parameters, created_at
        ) VALUES (
            p_flow_instance_id, v_event_def_id,
            p_step_order, 'PENDING',
            IFNULL(v_input_params, v_default_params),
            NOW()
        );

        SET v_event_log_id = LAST_INSERT_ID();

        -- Tên event động: evt_run_{flow_instance_id}_{event_log_id}
        SET v_event_name = CONCAT('evt_run_', p_flow_instance_id, '_', v_event_log_id);

        -- Ghi lại tên event vào event_log
        UPDATE event_log
        SET    mariadb_event_name = v_event_name
        WHERE  id = v_event_log_id;

        -- Tạo MariaDB EVENT chạy ngay sau 1 giây
        SET @_sql = CONCAT(
            'CREATE EVENT IF NOT EXISTS `', v_event_name, '` ',
            'ON SCHEDULE AT NOW() + INTERVAL 1 SECOND ',
            'ON COMPLETION NOT PRESERVE ',
            'ENABLE ',
            'DO CALL sp_execute_event(', v_event_log_id, ');'
        );
        PREPARE _stmt FROM @_sql;
        EXECUTE _stmt;
        DEALLOCATE PREPARE _stmt;

    END LOOP;
    CLOSE cur_events;
END$$


-- ──────────────────────────────────────────────────────────
-- 2.3 sp_execute_event
--     Được gọi bởi MariaDB EVENT động.
--     Thực thi stored procedure nghiệp vụ tương ứng.
-- ──────────────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS sp_execute_event$$
CREATE PROCEDURE sp_execute_event(
    IN p_event_log_id BIGINT
)
BEGIN
    DECLARE v_procedure_name    VARCHAR(255);
    DECLARE v_input_params      TEXT;
    DECLARE v_error_msg         TEXT DEFAULT NULL;
    DECLARE v_status            VARCHAR(20);

    -- Handler bắt mọi exception → đánh dấu FAILED
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1
            v_error_msg = MESSAGE_TEXT;

        UPDATE event_log
        SET    status        = 'FAILED',
               error_message = v_error_msg,
               completed_at  = NOW()
        WHERE  id = p_event_log_id;
    END;

    -- Kiểm tra event_log còn PENDING không (tránh chạy lại)
    SELECT status INTO v_status
    FROM   event_log
    WHERE  id = p_event_log_id
    FOR UPDATE;

    IF v_status != 'PENDING' THEN
        LEAVE sp_execute_event;  -- Đã chạy hoặc bị hủy
    END IF;

    -- Lấy thông tin procedure cần gọi
    SELECT  ed.procedure_name,
            el.input_parameters
    INTO    v_procedure_name,
            v_input_params
    FROM    event_log el
    JOIN    event_definition ed ON ed.id = el.event_definition_id
    WHERE   el.id = p_event_log_id;

    -- Đánh dấu đang chạy
    UPDATE event_log
    SET    status     = 'RUNNING',
           started_at = NOW()
    WHERE  id = p_event_log_id;

    -- Gọi stored procedure nghiệp vụ động
    -- Convention: sp_xxx(p_event_log_id BIGINT, p_parameters TEXT)
    SET @_event_log_id = p_event_log_id;
    SET @_params       = IFNULL(v_input_params, '{}');
    SET @_call_sql     = CONCAT('CALL `', v_procedure_name, '`(@_event_log_id, @_params)');

    PREPARE _call_stmt FROM @_call_sql;
    EXECUTE _call_stmt;
    DEALLOCATE PREPARE _call_stmt;

    -- Đánh dấu SUCCESS (nếu procedure không tự cập nhật)
    UPDATE event_log
    SET    status       = 'SUCCESS',
           completed_at = NOW()
    WHERE  id     = p_event_log_id
      AND  status = 'RUNNING';

END$$


-- ──────────────────────────────────────────────────────────
-- 2.4 sp_check_and_advance_flows   ← MASTER PROCEDURE
--     Gọi mỗi 1 phút bởi evt_master_flow_scheduler.
--     Xử lý: PENDING → khởi động, RUNNING → kiểm tra tiến bước.
-- ──────────────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS sp_check_and_advance_flows$$
CREATE PROCEDURE sp_check_and_advance_flows()
BEGIN
    DECLARE v_instance_id       BIGINT;
    DECLARE v_flow_id           BIGINT;
    DECLARE v_current_step      INT;
    DECLARE v_total_events      INT DEFAULT 0;
    DECLARE v_success_count     INT DEFAULT 0;
    DECLARE v_failed_count      INT DEFAULT 0;
    DECLARE v_running_count     INT DEFAULT 0;
    DECLARE v_next_step_order   INT DEFAULT NULL;
    DECLARE v_done_pending      INT DEFAULT 0;
    DECLARE v_done_running      INT DEFAULT 0;
    DECLARE v_lock_result       INT DEFAULT 0;

    -- Cursor: flow đang PENDING và đến giờ chạy
    DECLARE cur_pending CURSOR FOR
        SELECT id, flow_id
        FROM   flow_instance
        WHERE  status       = 'PENDING'
          AND  scheduled_at <= NOW()
        ORDER BY scheduled_at;

    -- Cursor: flow đang RUNNING
    DECLARE cur_running CURSOR FOR
        SELECT id, flow_id, current_step_order
        FROM   flow_instance
        WHERE  status = 'RUNNING'
        ORDER BY id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND
    BEGIN
        SET v_done_pending = 1;
        SET v_done_running = 1;
    END;

    -- Dùng advisory lock để tránh 2 session chạy đồng thời
    SET v_lock_result = GET_LOCK('salary_flow_scheduler_lock', 0);
    IF v_lock_result != 1 THEN
        -- Đang có session khác chạy → bỏ qua lần này
        LEAVE sp_check_and_advance_flows;
    END IF;

    -- ─────────────────────────────────────────────────
    -- BƯỚC A: Phát hiện và đánh dấu TIMEOUT
    --   Áp dụng cho mọi event đang PENDING/RUNNING quá giờ
    -- ─────────────────────────────────────────────────
    UPDATE event_log el
    JOIN   event_definition ed ON ed.id = el.event_definition_id
    JOIN   flow_instance fi    ON fi.id = el.flow_instance_id
    SET    el.status        = 'TIMEOUT',
           el.error_message = CONCAT('Vượt quá timeout ', ed.timeout_minutes, ' phút'),
           el.completed_at  = NOW()
    WHERE  fi.status   = 'RUNNING'
      AND  el.status   IN ('PENDING', 'RUNNING')
      AND  el.created_at < NOW() - INTERVAL ed.timeout_minutes MINUTE;

    -- ─────────────────────────────────────────────────
    -- BƯỚC B: Khởi động flow PENDING → RUNNING
    -- ─────────────────────────────────────────────────
    SET v_done_pending = 0;
    OPEN cur_pending;
    loop_pending: LOOP
        FETCH cur_pending INTO v_instance_id, v_flow_id;
        IF v_done_pending = 1 THEN LEAVE loop_pending; END IF;

        -- Lấy bước đầu tiên
        SELECT MIN(step_order) INTO v_next_step_order
        FROM   flow_step_definition
        WHERE  flow_id = v_flow_id;

        IF v_next_step_order IS NOT NULL THEN
            UPDATE flow_instance
            SET    status             = 'RUNNING',
                   current_step_order = v_next_step_order,
                   started_at         = NOW()
            WHERE  id = v_instance_id;

            CALL sp_create_step_events(v_instance_id, v_next_step_order);
        ELSE
            -- Flow không có bước → hoàn thành ngay
            UPDATE flow_instance
            SET    status       = 'COMPLETED',
                   started_at   = NOW(),
                   completed_at = NOW()
            WHERE  id = v_instance_id;
        END IF;

    END LOOP;
    CLOSE cur_pending;

    -- ─────────────────────────────────────────────────
    -- BƯỚC C: Kiểm tra và tiến bước flow RUNNING
    -- ─────────────────────────────────────────────────
    SET v_done_running = 0;
    OPEN cur_running;
    loop_running: LOOP
        FETCH cur_running INTO v_instance_id, v_flow_id, v_current_step;
        IF v_done_running = 1 THEN LEAVE loop_running; END IF;

        -- Đếm trạng thái event của bước hiện tại
        SELECT
            COUNT(*)                                                         AS total,
            SUM(CASE WHEN status = 'SUCCESS'              THEN 1 ELSE 0 END) AS cnt_success,
            SUM(CASE WHEN status IN ('FAILED', 'TIMEOUT') THEN 1 ELSE 0 END) AS cnt_failed,
            SUM(CASE WHEN status IN ('PENDING', 'RUNNING')THEN 1 ELSE 0 END) AS cnt_running
        INTO
            v_total_events,
            v_success_count,
            v_failed_count,
            v_running_count
        FROM event_log
        WHERE flow_instance_id = v_instance_id
          AND step_order       = v_current_step;

        -- CÓ EVENT FAILED/TIMEOUT → flow thất bại
        IF v_failed_count > 0 THEN
            UPDATE flow_instance
            SET    status        = 'FAILED',
                   completed_at  = NOW(),
                   error_message = CONCAT(
                       'Bước ', v_current_step,
                       ': có ', v_failed_count,
                       ' event thất bại. Kiểm tra bảng event_log để biết chi tiết.'
                   )
            WHERE  id = v_instance_id;

        -- TẤT CẢ SUCCESS → tiến bước tiếp theo
        ELSEIF v_total_events > 0
           AND v_success_count = v_total_events
           AND v_running_count = 0 THEN

            -- Tìm bước tiếp theo
            SET v_next_step_order = NULL;
            SELECT step_order INTO v_next_step_order
            FROM   flow_step_definition
            WHERE  flow_id    = v_flow_id
              AND  step_order > v_current_step
            ORDER BY step_order
            LIMIT 1;

            IF v_next_step_order IS NOT NULL THEN
                -- Tiến sang bước mới
                UPDATE flow_instance
                SET    current_step_order = v_next_step_order
                WHERE  id = v_instance_id;

                CALL sp_create_step_events(v_instance_id, v_next_step_order);
            ELSE
                -- Không còn bước → COMPLETED
                UPDATE flow_instance
                SET    status       = 'COMPLETED',
                       completed_at = NOW()
                WHERE  id = v_instance_id;
            END IF;

        END IF;
        -- Nếu v_running_count > 0 → còn event đang chạy → chờ vòng sau

    END LOOP;
    CLOSE cur_running;

    -- Giải phóng lock
    DO RELEASE_LOCK('salary_flow_scheduler_lock');

END$$


-- ──────────────────────────────────────────────────────────
-- 2.5 sp_cancel_flow_instance
--     Hủy một flow đang chạy hoặc đang chờ
-- ──────────────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS sp_cancel_flow_instance$$
CREATE PROCEDURE sp_cancel_flow_instance(
    IN  p_instance_id   BIGINT,
    IN  p_reason        TEXT,
    OUT p_result        VARCHAR(100)
)
BEGIN
    DECLARE v_status    VARCHAR(20);
    DECLARE v_done      INT DEFAULT 0;
    DECLARE v_ev_name   VARCHAR(255);

    DECLARE cur_events CURSOR FOR
        SELECT mariadb_event_name
        FROM   event_log
        WHERE  flow_instance_id = p_instance_id
          AND  status IN ('PENDING', 'RUNNING')
          AND  mariadb_event_name IS NOT NULL;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

    SELECT status INTO v_status
    FROM   flow_instance
    WHERE  id = p_instance_id;

    IF v_status IS NULL THEN
        SET p_result = 'ERROR: Instance không tồn tại';
        LEAVE sp_cancel_flow_instance;
    END IF;

    IF v_status IN ('COMPLETED', 'FAILED', 'CANCELLED') THEN
        SET p_result = CONCAT('ERROR: Không thể hủy, trạng thái hiện tại là ', v_status);
        LEAVE sp_cancel_flow_instance;
    END IF;

    -- Đánh dấu flow CANCELLED
    UPDATE flow_instance
    SET    status        = 'CANCELLED',
           completed_at  = NOW(),
           error_message = IFNULL(p_reason, 'Hủy thủ công')
    WHERE  id = p_instance_id;

    -- Đánh dấu các event đang PENDING/RUNNING là SKIPPED
    UPDATE event_log
    SET    status        = 'SKIPPED',
           completed_at  = NOW(),
           error_message = 'Flow bị hủy'
    WHERE  flow_instance_id = p_instance_id
      AND  status IN ('PENDING', 'RUNNING');

    -- Drop MariaDB EVENT động còn tồn tại
    OPEN cur_events;
    drop_loop: LOOP
        FETCH cur_events INTO v_ev_name;
        IF v_done = 1 THEN LEAVE drop_loop; END IF;

        SET @_drop_sql = CONCAT('DROP EVENT IF EXISTS `', v_ev_name, '`');
        PREPARE _drop_stmt FROM @_drop_sql;
        EXECUTE _drop_stmt;
        DEALLOCATE PREPARE _drop_stmt;

    END LOOP;
    CLOSE cur_events;

    SET p_result = CONCAT('OK: Flow instance ', p_instance_id, ' đã bị hủy');
END$$


-- ──────────────────────────────────────────────────────────
-- 2.6 sp_retry_failed_event
--     Retry một event bị FAILED/TIMEOUT
-- ──────────────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS sp_retry_failed_event$$
CREATE PROCEDURE sp_retry_failed_event(
    IN  p_event_log_id  BIGINT,
    OUT p_result        VARCHAR(100)
)
BEGIN
    DECLARE v_status        VARCHAR(20);
    DECLARE v_ev_name       VARCHAR(255);
    DECLARE v_retry_count   INT;
    DECLARE v_instance_id   BIGINT;
    DECLARE v_step_order    INT;

    SELECT status, retry_count, mariadb_event_name,
           flow_instance_id, step_order
    INTO   v_status, v_retry_count, v_ev_name,
           v_instance_id, v_step_order
    FROM   event_log
    WHERE  id = p_event_log_id;

    IF v_status IS NULL THEN
        SET p_result = 'ERROR: Event log không tồn tại';
        LEAVE sp_retry_failed_event;
    END IF;

    IF v_status NOT IN ('FAILED', 'TIMEOUT') THEN
        SET p_result = CONCAT('ERROR: Chỉ retry được FAILED/TIMEOUT, hiện tại: ', v_status);
        LEAVE sp_retry_failed_event;
    END IF;

    -- Reset trạng thái về PENDING
    SET v_ev_name = CONCAT('evt_retry_', v_instance_id, '_', p_event_log_id, '_', v_retry_count + 1);

    UPDATE event_log
    SET    status             = 'PENDING',
           mariadb_event_name = v_ev_name,
           error_message      = NULL,
           started_at         = NULL,
           completed_at       = NULL,
           retry_count        = retry_count + 1,
           created_at         = NOW()       -- Reset timeout timer
    WHERE  id = p_event_log_id;

    -- Đưa flow về RUNNING nếu đang FAILED
    UPDATE flow_instance
    SET    status        = 'RUNNING',
           error_message = NULL
    WHERE  id     = v_instance_id
      AND  status = 'FAILED';

    -- Tạo lại MariaDB EVENT để chạy lại
    SET @_retry_sql = CONCAT(
        'CREATE EVENT IF NOT EXISTS `', v_ev_name, '` ',
        'ON SCHEDULE AT NOW() + INTERVAL 1 SECOND ',
        'ON COMPLETION NOT PRESERVE ',
        'ENABLE ',
        'DO CALL sp_execute_event(', p_event_log_id, ');'
    );
    PREPARE _retry_stmt FROM @_retry_sql;
    EXECUTE _retry_stmt;
    DEALLOCATE PREPARE _retry_stmt;

    SET p_result = CONCAT('OK: Event ', p_event_log_id, ' đang được retry lần ', v_retry_count + 1);
END$$


DELIMITER ;


-- ============================================================
--  SECTION 3: MASTER EVENT SCHEDULER
-- ============================================================

DROP EVENT IF EXISTS evt_master_flow_scheduler;

DELIMITER $$
CREATE EVENT IF NOT EXISTS evt_master_flow_scheduler
ON SCHEDULE EVERY 1 MINUTE
STARTS NOW()
ON COMPLETION PRESERVE
ENABLE
COMMENT 'Master scheduler: kiểm tra và tiến bước luồng mỗi 1 phút'
DO
BEGIN
    CALL sp_check_and_advance_flows();
END$$
DELIMITER ;


-- ============================================================
--  SECTION 4: MONITORING VIEWS
-- ============================================================

-- 4.1 View tổng quan flow instance đang chạy
CREATE OR REPLACE VIEW v_flow_running_status AS
SELECT
    fi.id                                                   AS instance_id,
    fi.flow_code,
    fi.instance_key,
    fi.status                                               AS flow_status,
    fi.current_step_order,
    fsd.step_name                                           AS current_step_name,
    fi.scheduled_at,
    fi.started_at,
    fi.completed_at,
    TIMESTAMPDIFF(MINUTE, fi.started_at, IFNULL(fi.completed_at, NOW())) AS elapsed_minutes,
    fi.error_message
FROM flow_instance fi
LEFT JOIN flow_step_definition fsd
    ON  fsd.flow_id    = fi.flow_id
    AND fsd.step_order = fi.current_step_order
ORDER BY fi.created_at DESC;

-- 4.2 View chi tiết event theo từng bước
CREATE OR REPLACE VIEW v_event_log_detail AS
SELECT
    el.id                                                   AS event_log_id,
    fi.instance_key,
    fi.flow_code,
    el.step_order,
    fsd.step_name,
    ed.event_code,
    ed.event_name,
    ed.procedure_name,
    el.status                                               AS event_status,
    el.mariadb_event_name,
    el.retry_count,
    el.created_at,
    el.started_at,
    el.completed_at,
    TIMESTAMPDIFF(SECOND, el.started_at, IFNULL(el.completed_at, NOW())) AS elapsed_seconds,
    el.error_message,
    el.result
FROM event_log el
JOIN flow_instance fi        ON fi.id = el.flow_instance_id
JOIN event_definition ed     ON ed.id = el.event_definition_id
JOIN flow_step_definition fsd
    ON  fsd.flow_id    = fi.flow_id
    AND fsd.step_order = el.step_order
ORDER BY el.flow_instance_id, el.step_order, el.id;

-- 4.3 View tổng hợp tiến trình theo từng bước
CREATE OR REPLACE VIEW v_step_progress AS
SELECT
    el.flow_instance_id,
    fi.instance_key,
    fi.flow_code,
    el.step_order,
    fsd.step_name,
    COUNT(*)                                                             AS total_events,
    SUM(CASE WHEN el.status = 'SUCCESS'               THEN 1 ELSE 0 END) AS success_count,
    SUM(CASE WHEN el.status IN ('FAILED', 'TIMEOUT')  THEN 1 ELSE 0 END) AS failed_count,
    SUM(CASE WHEN el.status IN ('PENDING', 'RUNNING') THEN 1 ELSE 0 END) AS running_count,
    ROUND(
        SUM(CASE WHEN el.status = 'SUCCESS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 1
    )                                                                    AS success_pct
FROM event_log el
JOIN flow_instance fi ON fi.id = el.flow_instance_id
JOIN flow_step_definition fsd
    ON  fsd.flow_id    = fi.flow_id
    AND fsd.step_order = el.step_order
GROUP BY el.flow_instance_id, fi.instance_key, fi.flow_code, el.step_order, fsd.step_name
ORDER BY el.flow_instance_id, el.step_order;


-- ============================================================
--  SECTION 5: SEED DATA – Ví dụ luồng tính lương tháng
-- ============================================================

-- 5.1 Định nghĩa luồng
INSERT INTO flow_definition (flow_code, flow_name, description)
VALUES (
    'SALARY_MONTHLY_CALC',
    'Tính Lương Tháng',
    'Luồng tự động tính và xuất báo cáo lương hàng tháng'
);

-- 5.2 Định nghĩa 4 bước (tuần tự)
INSERT INTO flow_step_definition (flow_id, step_order, step_code, step_name, description, timeout_minutes)
VALUES
    (LAST_INSERT_ID(), 1, 'IMPORT_DATA',   'Bước 1: Import dữ liệu nguồn',  'Import song song 3 loại dữ liệu', 60),
    (LAST_INSERT_ID(), 2, 'CALC_SALARY',   'Bước 2: Tính toán lương',        'Tính song song 3 loại lương',    120),
    (LAST_INSERT_ID(), 3, 'VALIDATE',      'Bước 3: Kiểm tra & đối soát',    'Validate kết quả tính lương',     60),
    (LAST_INSERT_ID(), 4, 'EXPORT_REPORT', 'Bước 4: Xuất báo cáo tổng hợp', 'Xuất báo cáo và gửi email',       30);

-- (Lấy step_id của từng bước để seed event_definition)
-- Bước 1: 3 event song song – import dữ liệu
INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'IMPORT_HH_SALE', 'Import HH_SALE Data', 'sp_import_hh_sale_data', 30
FROM flow_step_definition WHERE step_code = 'IMPORT_DATA';

INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'IMPORT_HH_N3', 'Import HH_N3 Data', 'sp_import_hh_n3_data', 30
FROM flow_step_definition WHERE step_code = 'IMPORT_DATA';

INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'IMPORT_OTHER', 'Import Other Data', 'sp_import_other_data', 30
FROM flow_step_definition WHERE step_code = 'IMPORT_DATA';

-- Bước 2: 3 event song song – tính lương
INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'CALC_HH_SALE', 'Tính lương HH_SALE', 'sp_calc_salary_hh_sale', 60
FROM flow_step_definition WHERE step_code = 'CALC_SALARY';

INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'CALC_HH_N3', 'Tính lương HH_N3', 'sp_calc_salary_hh_n3', 60
FROM flow_step_definition WHERE step_code = 'CALC_SALARY';

INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'CALC_OTHER', 'Tính lương Other', 'sp_calc_salary_other', 60
FROM flow_step_definition WHERE step_code = 'CALC_SALARY';

-- Bước 3: 2 event song song – validate
INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'VALIDATE_AMOUNT',   'Kiểm tra tổng tiền',    'sp_validate_total_amount',   30
FROM flow_step_definition WHERE step_code = 'VALIDATE';

INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'VALIDATE_EMPLOYEE', 'Kiểm tra danh sách NV', 'sp_validate_employee_list',  30
FROM flow_step_definition WHERE step_code = 'VALIDATE';

-- Bước 4: 1 event – xuất báo cáo
INSERT INTO event_definition (step_id, event_code, event_name, procedure_name, timeout_minutes)
SELECT id, 'EXPORT_ALL', 'Xuất báo cáo tổng hợp', 'sp_export_salary_report', 20
FROM flow_step_definition WHERE step_code = 'EXPORT_REPORT';


-- ============================================================
--  SECTION 6: STORED PROCEDURES NGHIỆP VỤ (STUB / EXAMPLE)
--  Mỗi procedure nhận (p_event_log_id BIGINT, p_parameters TEXT)
-- ============================================================

DELIMITER $$

-- Stub: Import HH_SALE data (thay bằng logic thực tế)
DROP PROCEDURE IF EXISTS sp_import_hh_sale_data$$
CREATE PROCEDURE sp_import_hh_sale_data(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    DECLARE v_salary_month VARCHAR(20);
    -- Lấy salary_month từ JSON parameter
    SET v_salary_month = JSON_VALUE(p_parameters, '$.salary_month');

    -- TODO: Thêm logic import thực tế tại đây
    -- Ví dụ: INSERT INTO salary_hh_sale SELECT ... FROM source_table WHERE month = v_salary_month

    -- Giả lập thành công và ghi kết quả
    UPDATE event_log
    SET    result = JSON_OBJECT('imported_rows', 1250, 'salary_month', v_salary_month)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Import HH_N3 data
DROP PROCEDURE IF EXISTS sp_import_hh_n3_data$$
CREATE PROCEDURE sp_import_hh_n3_data(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    DECLARE v_salary_month VARCHAR(20);
    SET v_salary_month = JSON_VALUE(p_parameters, '$.salary_month');
    -- TODO: logic import HH_N3
    UPDATE event_log
    SET    result = JSON_OBJECT('imported_rows', 430, 'salary_month', v_salary_month)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Import Other data
DROP PROCEDURE IF EXISTS sp_import_other_data$$
CREATE PROCEDURE sp_import_other_data(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    DECLARE v_salary_month VARCHAR(20);
    SET v_salary_month = JSON_VALUE(p_parameters, '$.salary_month');
    -- TODO: logic import Other
    UPDATE event_log
    SET    result = JSON_OBJECT('imported_rows', 195, 'salary_month', v_salary_month)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Calculate HH_SALE salary
DROP PROCEDURE IF EXISTS sp_calc_salary_hh_sale$$
CREATE PROCEDURE sp_calc_salary_hh_sale(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    -- TODO: Logic tính lương HH_SALE
    UPDATE event_log
    SET    result = JSON_OBJECT('status', 'calculated', 'total_amount', 7200000000)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Calculate HH_N3 salary
DROP PROCEDURE IF EXISTS sp_calc_salary_hh_n3$$
CREATE PROCEDURE sp_calc_salary_hh_n3(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    -- TODO: Logic tính lương HH_N3
    UPDATE event_log
    SET    result = JSON_OBJECT('status', 'calculated', 'total_amount', 5600000000)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Calculate Other salary
DROP PROCEDURE IF EXISTS sp_calc_salary_other$$
CREATE PROCEDURE sp_calc_salary_other(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    -- TODO: Logic tính lương Other
    UPDATE event_log
    SET    result = JSON_OBJECT('status', 'calculated', 'total_amount', 3000000000)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Validate total amount
DROP PROCEDURE IF EXISTS sp_validate_total_amount$$
CREATE PROCEDURE sp_validate_total_amount(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    -- TODO: Logic kiểm tra tổng tiền
    UPDATE event_log
    SET    result = JSON_OBJECT('validated', TRUE, 'total', 15800000000)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Validate employee list
DROP PROCEDURE IF EXISTS sp_validate_employee_list$$
CREATE PROCEDURE sp_validate_employee_list(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    -- TODO: Logic kiểm tra danh sách nhân viên
    UPDATE event_log
    SET    result = JSON_OBJECT('validated', TRUE, 'total_employees', 1875)
    WHERE  id = p_event_log_id;
END$$


-- Stub: Export salary report
DROP PROCEDURE IF EXISTS sp_export_salary_report$$
CREATE PROCEDURE sp_export_salary_report(
    IN p_event_log_id   BIGINT,
    IN p_parameters     TEXT
)
BEGIN
    -- TODO: Logic xuất báo cáo
    UPDATE event_log
    SET    result = JSON_OBJECT('file', 'salary_report_2026_03.xlsx', 'status', 'exported')
    WHERE  id = p_event_log_id;
END$$


DELIMITER ;


-- ============================================================
--  SECTION 7: SCRIPT SỬ DỤNG (USAGE EXAMPLES)
-- ============================================================

-- ── Tạo instance chạy ngay ──
-- CALL sp_create_flow_instance(
--     'SALARY_MONTHLY_CALC',
--     'SALARY_2026-03-01',
--     '{"salary_month": "2026-03-01", "run_by": "admin"}',
--     NOW(),
--     @instance_id
-- );
-- SELECT @instance_id;

-- ── Tạo instance lên lịch chạy lúc 23:00 ──
-- CALL sp_create_flow_instance(
--     'SALARY_MONTHLY_CALC',
--     'SALARY_2026-03-01_NIGHT',
--     '{"salary_month": "2026-03-01"}',
--     '2026-03-31 23:00:00',
--     @instance_id
-- );

-- ── Hủy một flow đang chạy ──
-- CALL sp_cancel_flow_instance(1, 'Hủy theo yêu cầu', @result);
-- SELECT @result;

-- ── Retry một event bị lỗi ──
-- CALL sp_retry_failed_event(5, @result);
-- SELECT @result;

-- ── Monitoring ──
-- SELECT * FROM v_flow_running_status;
-- SELECT * FROM v_step_progress WHERE instance_id = 1;
-- SELECT * FROM v_event_log_detail WHERE instance_id = 1;

-- ── Xem MariaDB events động đang tồn tại ──
-- SELECT EVENT_NAME, STATUS, EXECUTE_AT, LAST_EXECUTED
-- FROM information_schema.EVENTS
-- WHERE EVENT_SCHEMA = DATABASE()
-- ORDER BY EVENT_NAME;
