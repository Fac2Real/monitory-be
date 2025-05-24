package com.factoreal.backend.service;

import com.factoreal.backend.dto.CreateWorkerRequest;
import com.factoreal.backend.dto.WorkerDto;
import com.factoreal.backend.dto.WorkerDetailResponse;
import com.factoreal.backend.dto.ZoneManagerResponseDto;
import com.factoreal.backend.entity.Worker;
import com.factoreal.backend.entity.WorkerZone;
import com.factoreal.backend.entity.WorkerZoneId;
import com.factoreal.backend.entity.Zone;
import com.factoreal.backend.entity.ZoneHist;
import com.factoreal.backend.repository.WorkerRepository;
import com.factoreal.backend.repository.WorkerZoneRepository;
import com.factoreal.backend.repository.ZoneRepository;
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
    private final WorkerZoneRepository workerZoneRepository;
    private final ZoneRepository zoneRepository;

    @Transactional(readOnly = true)
    public List<WorkerDetailResponse> getAllWorkers() {
        log.info("전체 작업자 목록 조회");
        List<Worker> workers = workerRepository.findAll();
        return workers.stream()
                .map(worker -> {
                    // 해당 작업자가 어떤 공간의 담당자인지 확인
                    boolean isManager = workerZoneRepository.findByWorkerWorkerIdAndManageYnIsTrue(worker.getWorkerId()).isPresent();
                    
                    // 작업자의 현재 위치 정보 조회
                    ZoneHist currentLocation = workerLocationService.getCurrentWorkerLocation(worker.getWorkerId());
                    
                    // 작업자 상태와 위치 정보 초기 설정
                    // TODO: 작업자 상태 추가 필요
                    String status = "Stable";
                    String currentZoneId = null;
                    String currentZoneName = null;
                    
                    if (currentLocation != null) {
                        Zone currentZone = currentLocation.getZone();
                        currentZoneId = currentZone.getZoneId();
                        currentZoneName = currentZone.getZoneName();
                    }
                    
                    return WorkerDetailResponse.fromEntity(worker, isManager, status, currentZoneId, currentZoneName);
                })
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

    /**
     * 특정 공간의 담당자와 현재 위치 정보 조회
     */
    @Transactional(readOnly = true)
    public ZoneManagerResponseDto getZoneManagerWithLocation(String zoneId) {
        log.info("공간 ID: {}의 담당자 정보 조회", zoneId);
        
        // 1. 해당 공간의 담당자 조회 (manageYn = true)
        WorkerZone zoneManager = workerZoneRepository.findByZoneZoneIdAndManageYnIsTrue(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공간의 담당자를 찾을 수 없습니다: " + zoneId));
        
        Worker manager = zoneManager.getWorker();
        
        // 2. 담당자의 현재 위치 조회 (existFlag = 1)
        ZoneHist currentLocation = workerLocationService.getCurrentWorkerLocation(manager.getWorkerId());
        
        // 3. 현재 위치한 공간 정보 (없을 수 있음)
        Zone currentZone = currentLocation != null ? currentLocation.getZone() : null;
        
        // 4. DTO 변환 및 반환
        return ZoneManagerResponseDto.fromEntity(manager, currentZone);
    }

    /**
     * 작업자 생성 및 출입 가능 공간 설정
     */
    @Transactional
    public void createWorker(CreateWorkerRequest request) {
        log.info("작업자 생성 요청: {}", request);
        
        // 1. 작업자 정보 저장
        Worker worker = Worker.builder()
                .workerId(request.getWorkerId())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
        
        workerRepository.save(worker); // 작업자 정보 저장
        
        // 2. 각 공간명으로 Zone 조회 및 WorkerZone 생성
        for (String zoneName : request.getZoneNames()) {
            Zone zone = zoneRepository.findByZoneName(zoneName)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간명입니다: " + zoneName));
            
            // WorkerZone 생성 (기본적으로 관리자 권한은 없음)
            WorkerZone workerZone = WorkerZone.builder()
                    .id(new WorkerZoneId(worker.getWorkerId(), zone.getZoneId())) // 복합키 생성
                    .worker(worker)
                    .zone(zone)
                    .manageYn(false) // 담당자 권한은 없음이 default
                    .build();
            
            workerZoneRepository.save(workerZone); // WorkerZone 저장
        }
        
        log.info("작업자 생성 완료 - workerId: {}", worker.getWorkerId());
    }
}