package com.factoreal.backend.service;

import com.factoreal.backend.dto.WorkerDto;
import com.factoreal.backend.entity.Worker;
import com.factoreal.backend.entity.WorkerZone;
import com.factoreal.backend.repository.WorkerRepository;
import com.factoreal.backend.repository.WorkerZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final WorkerZoneRepository workerZoneRepository;

    public List<WorkerDto> getAllWorkers() {
        log.info("전체 작업자 목록 조회");
        List<Worker> workers = workerRepository.findAll();
        
        return workers.stream()
                .map(worker -> {
                    // 기본값으로는 관리자임 (zoneId랑 매핑되어 있으니까)
                    return WorkerDto.fromEntity(worker, true);
                })
                .collect(Collectors.toList());
    }

    public List<WorkerDto> getWorkersByZoneId(String zoneId) {
        log.info("공간 ID: {}의 작업자 목록 조회", zoneId);
        List<WorkerZone> workerZones = workerZoneRepository.findByZoneZoneId(zoneId);
        
        return workerZones.stream()
                .map(workerZone -> {
                    Worker worker = workerZone.getWorker();
                    Boolean isManager = workerZone.getManageYn();
                    return WorkerDto.fromEntity(worker, isManager);
                })
                .collect(Collectors.toList());
    }
} 