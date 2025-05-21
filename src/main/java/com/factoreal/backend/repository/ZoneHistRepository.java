package com.factoreal.backend.repository;

import com.factoreal.backend.entity.ZoneHist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneHistRepository extends JpaRepository<ZoneHist, Long> {
    // 특정 공간에 현재 있는 작업자들 조회 (existFlag = 1)
    List<ZoneHist> findByZone_ZoneIdAndExistFlag(String zoneId, Integer existFlag);
    
    // 특정 작업자의 현재 위치 조회 (existFlag = 1)
    ZoneHist findByWorker_WorkerIdAndExistFlag(String workerId, Integer existFlag);
}