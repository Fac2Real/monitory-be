package com.factoreal.backend.domain.zone.api;

import com.factoreal.backend.domain.worker.dto.WorkerLocationRequest;
import com.factoreal.backend.domain.zone.application.WorkerLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "작업자 위치 API", description = "작업자의 실시간 위치 정보를 관리하는 API")
@RestController
@RequestMapping("/api/worker-locations")
@RequiredArgsConstructor
// 웨어러블 디바이스에서 받아오는 데이터를 업데이트하는 컨트롤러
public class WorkerLocationController {

    private final WorkerLocationService workerLocationService;

    @Operation(summary = "작업자 위치 업데이트", description = "웨어러블 디바이스로부터 받은 작업자의 위치 정보를 업데이트합니다.")
    @PostMapping("/update")
    public ResponseEntity<Void> updateWorkerLocation(@RequestBody WorkerLocationRequest request) {
        log.info("작업자 위치 업데이트 요청: {}", request);
        workerLocationService.updateWorkerLocation(
                request.getWorkerId(),
                request.getZoneId(),
                request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now()
        );
        return ResponseEntity.ok().build(); // 200 OK 응답
    }
}