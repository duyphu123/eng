-- ============================================================
--  eng — schema khởi tạo cho module Vocabulary (CRUD đầu tiên)
--  Chạy trong DBeaver (đã kết nối MariaDB). Chạy từng khối bằng Ctrl+Enter,
--  hoặc chạy cả file bằng Alt+X.
-- ============================================================

-- 1) Tạo database mới `eng`
CREATE DATABASE IF NOT EXISTS `eng`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `eng`;

-- 2) Bảng vocabulary — KHỚP 1-1 với entity Vocabulary.java
--    (cột snake_case <-> field camelCase; @Column(name=...) đã map sẵn)
CREATE TABLE IF NOT EXISTS `vocabulary` (
    `vocabulary_id`   bigint(18)    NOT NULL AUTO_INCREMENT,
    `word`            varchar(255)  DEFAULT NULL,
    `meaning`         varchar(512)  DEFAULT NULL,
    `part_of_speech`  varchar(50)   DEFAULT NULL,
    `pronunciation`   varchar(255)  DEFAULT NULL,
    `example`         varchar(1000) DEFAULT NULL,
    `level`           varchar(20)   DEFAULT NULL,
    `status`          varchar(1)    DEFAULT '1',
    `create_datetime` datetime      DEFAULT current_timestamp(),
    `create_user`     varchar(50)   DEFAULT NULL,
    `update_datetime` datetime      DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `update_user`     varchar(50)   DEFAULT NULL,
    PRIMARY KEY (`vocabulary_id`),
    KEY `idx_vocabulary_word` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3) Vài dòng data test (để GET có dữ liệu xem ngay)
INSERT INTO `vocabulary`
    (`word`, `meaning`, `part_of_speech`, `pronunciation`, `example`, `level`, `status`, `create_user`, `update_user`)
VALUES
    ('invoice',   'hóa đơn',   'noun', '/ˈɪnvɔɪs/',    'Please send me the invoice by Friday.',     'TOEIC 600', '1', 'system', 'system'),
    ('negotiate', 'đàm phán',  'verb', '/nɪˈɡoʊʃieɪt/', 'We need to negotiate the contract terms.',  'TOEIC 700', '1', 'system', 'system'),
    ('warehouse', 'nhà kho',   'noun', '/ˈwerhaʊs/',    'The goods are stored in the warehouse.',    'TOEIC 500', '1', 'system', 'system');

-- 4) Kiểm tra
SELECT * FROM `vocabulary`;
