-- ========================
-- V3__add_is_read_to_abnormal_log.sql
-- abnormal_log 테이블에 is_read 컬럼 추가
-- ========================

ALTER TABLE abn_log
    ADD COLUMN is_read BOOLEAN NOT NULL DEFAULT FALSE;
