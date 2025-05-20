package com.factoreal.backend.controller;

import com.factoreal.backend.dto.WorkerDto;
import com.factoreal.backend.service.WorkerService;

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
    
    @Operation(summary = "공간별 작업자 목록 조회", description = "공간 ID를 기반으로 공간별 작업자 목록을 조회합니다.")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<WorkerDto>> getWorkersByZoneId(@PathVariable String zoneId) {
        log.info("공간 ID: {}의 작업자 목록 조회 요청", zoneId);
        List<WorkerDto> zoneWorkers = workerService.getWorkersByZoneId(zoneId);
        return ResponseEntity.ok(zoneWorkers);
    }
} 