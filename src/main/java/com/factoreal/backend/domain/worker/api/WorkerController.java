package com.factoreal.backend.domain.worker.api;

import com.factoreal.backend.domain.worker.dto.WorkerDto;
import com.factoreal.backend.domain.zone.dto.ZoneManagerResponseDto;
import com.factoreal.backend.domain.worker.application.WorkerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "작업자 API", description = "작업자 조회 API")
public class WorkerController {
    private final WorkerService workerService;
    
    @Operation(summary = "전체 작업자 목록 조회", description = "전체 작업자 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<WorkerDto>> getAllWorkers() {
        log.info("전체 작업자 목록 조회 요청");
        List<WorkerDto> workers = workerService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }
    
    @Operation(summary = "공간별 작업자 목록 조회", description = "공간 ID를 기반으로 현재 해당 공간에 들어가있는 작업자 리스트를 조회합니다.")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<WorkerDto>> getWorkersByZoneId(@PathVariable String zoneId) {
        log.info("공간 ID: {}의 작업자 목록 조회 요청", zoneId);
        List<WorkerDto> zoneWorkers = workerService.getWorkersByZoneId(zoneId);
        return ResponseEntity.ok(zoneWorkers);
    }

    @Operation(summary = "공간 담당자 정보 조회", 
              description = "공간 ID를 기반으로 해당 공간의 담당자와 현재 위치 정보를 조회합니다.")
    @GetMapping("/zone/{zoneId}/manager")
    public ResponseEntity<ZoneManagerResponseDto> getZoneManager(@PathVariable String zoneId) {
        log.info("공간 ID: {}의 담당자 정보 조회 요청", zoneId);
        ZoneManagerResponseDto manager = workerService.getZoneManagerWithLocation(zoneId);
        return ResponseEntity.ok(manager);
    }
} 