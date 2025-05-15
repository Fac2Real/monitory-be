-- sensor_info 테이블의 sensor_thres 컬럼 타입 변경 (INT → DOUBLE)
ALTER TABLE sensor_info
MODIFY COLUMN sensor_thres DOUBLE;