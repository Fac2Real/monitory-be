package com.factoreal.backend.service;

import com.factoreal.backend.dto.WorkerDto;
import com.factoreal.backend.entity.Worker;
import com.factoreal.backend.entity.ZoneHist;
import com.factoreal.backend.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final WorkerLocationService workerLocationService;

    @Transactional(readOnly = true)
    public List<WorkerDto> getAllWorkers() {
        log.info("전체 작업자 목록 조회");
        List<Worker> workers = workerRepository.findAll();
        return workers.stream()
                .map(worker -> WorkerDto.fromEntity(worker, false))
                .collect(Collectors.toList());
    }

    /**
     * 특정 공간에 현재 들어가있는 작업자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<WorkerDto> getWorkersByZoneId(String zoneId) {
        log.info("공간 ID: {}의 현재 작업자 목록 조회", zoneId);
        
        // 현재 해당 공간에 있는 작업자 이력 조회 (existFlag = 1)
        List<ZoneHist> currentWorkers = workerLocationService.getCurrentWorkersByZoneId(zoneId);
        
        // ZoneHist에서 Worker 정보만 추출하여 DTO로 변환
        return currentWorkers.stream()
                .map(zoneHist -> WorkerDto.fromEntity(zoneHist.getWorker(), false))
                .collect(Collectors.toList());
    }
}

// TODO. 수정되어야 할 로직. 현재는 WorkerZone 테이블에서 공간id로 필터링 되는 모든 작업자를 끌고왔는데,
// 사실 현재 그 공간에서 실제로 작업하고 있는, 즉 들어
// public List<WorkerDto> getWorkersByZoneId(String zoneId) {
//     log.info("공간 ID: {}의 작업자 목록 조회", zoneId);
//     List<WorkerZone> workerZones = workerZoneRepository.findByZoneZoneId(zoneId);
    
//     return workerZones.stream()
//             .map(workerZone -> {
//                 Worker worker = workerZone.getWorker();
//                 Boolean isManager = workerZone.getManageYn();
//                 return WorkerDto.fromEntity(worker, isManager);
//             })
//             .collect(Collectors.toList());
// }