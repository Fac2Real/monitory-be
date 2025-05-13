-- ========================
-- V3__alter_sensor_table_add_allowval_column.sql
-- sensor_info 테이블에 column
-- ========================

ALTER TABLE sensor_info
    ADD COLUMN allow_val DOUBLE;
