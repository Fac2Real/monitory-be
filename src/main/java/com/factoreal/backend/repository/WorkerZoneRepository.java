package com.factoreal.backend.repository;

import com.factoreal.backend.entity.WorkerZone;
import com.factoreal.backend.entity.WorkerZoneId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerZoneRepository extends JpaRepository<WorkerZone, WorkerZoneId> {
    
    // 특정 zone_id에 속한 작업자 목록 조회
    List<WorkerZone> findByZoneZoneId(String zoneId);
    
    // 특정 zone_id의 담당자 조회 (manageYn = true)
    Optional<WorkerZone> findByZoneZoneIdAndManageYnIsTrue(String zoneId);
    
    // 특정 공간을 제외한 다른 공간의 담당자 목록 조회
    List<WorkerZone> findByZoneZoneIdNotAndManageYnIsTrue(String zoneId);
} 