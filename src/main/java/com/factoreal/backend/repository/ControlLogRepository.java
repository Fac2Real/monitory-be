package com.factoreal.backend.repository;

import com.factoreal.backend.entity.ControlLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 자동제어 로그 저장을 위한 레포지토리
 */
public interface ControlLogRepository extends JpaRepository<ControlLog, Long> {
} 