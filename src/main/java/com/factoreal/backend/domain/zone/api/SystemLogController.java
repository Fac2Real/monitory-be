package com.factoreal.backend.domain.zone.api;

import com.factoreal.backend.domain.abnormalLog.dto.AbnormalPagingDto;
import com.factoreal.backend.domain.abnormalLog.dto.SystemLogResponseDto;
import com.factoreal.backend.domain.abnormalLog.application.AbnormalLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시스템 로그 API", description = "공간별 시스템 로그 조회 API")
@RestController
@RequestMapping("/api/system-logs")
@RequiredArgsConstructor
public class SystemLogController {
    private final AbnormalLogService abnormalLogService;

    @Operation(summary = "공간별 시스템 로그 조회", description = "특정 공간(zone)의 시스템 로그를 페이징 처리하여 조회합니다.")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<Page<SystemLogResponseDto>> getSystemLogsByZone(
            @Parameter(description = "조회할 공간 ID", required = true) 
            @PathVariable String zoneId,
            @Parameter(description = "페이징 정보 (page: 페이지 번호, size: 페이지 크기)") 
            @ModelAttribute AbnormalPagingDto pagingDto) {
        Page<SystemLogResponseDto> logs = abnormalLogService.findSystemLogsByZoneId(zoneId, pagingDto);
        return ResponseEntity.ok(logs);
    }
} 