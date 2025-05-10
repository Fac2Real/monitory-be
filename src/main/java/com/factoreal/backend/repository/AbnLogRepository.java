package com.factoreal.backend.repository;

import com.factoreal.backend.entity.AbnormalLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbnLogRepository extends JpaRepository<AbnormalLog,Long> {
    Page<AbnormalLog> findAbnormalLogsByAbnormalType(String abnormalType, Pageable pageable);

    Page<AbnormalLog> findAbnormalLogsByTargetTypeAndTargetId(String targetType, String targetId, Pageable pageable);
    long countByIsReadFalse(); // 읽지 않은 로그의 개수 반환

    Page<AbnormalLog> findAllByIsReadIsFalse(Pageable pageable);
}