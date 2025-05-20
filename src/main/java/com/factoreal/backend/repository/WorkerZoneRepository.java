package com.factoreal.backend.repository;

import com.factoreal.backend.entity.WorkerZone;
import com.factoreal.backend.entity.WorkerZoneId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerZoneRepository extends JpaRepository<WorkerZone, WorkerZoneId> {
    
    // 특정 zone_id에 속한 작업자 목록 조회
    List<WorkerZone> findByZoneZoneId(String zoneId);
} 