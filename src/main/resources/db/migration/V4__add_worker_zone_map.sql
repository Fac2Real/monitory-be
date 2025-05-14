-- 외래키 제약조건 먼저 제거
ALTER TABLE worker_info DROP FOREIGN KEY FK_role_info_TO_worker_info;

-- worker_info에서 role_id, zone_id 컬럼 제거
ALTER TABLE worker_info DROP COLUMN role_id;
ALTER TABLE worker_info DROP COLUMN zone_id;

-- role_info 테이블 삭제
DROP TABLE role_info;

-- worker_zone 테이블 생성
CREATE TABLE worker_zone (
    worker_id VARCHAR(100) NOT NULL,
    zone_id VARCHAR(100) NOT NULL,
    manage_yn BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY (worker_id, zone_id),

    CONSTRAINT FK_worker_zone_to_worker_info
        FOREIGN KEY (worker_id) REFERENCES worker_info(worker_id),

    CONSTRAINT FK_worker_zone_to_zone_info
        FOREIGN KEY (zone_id) REFERENCES zone_info(zone_id)
);