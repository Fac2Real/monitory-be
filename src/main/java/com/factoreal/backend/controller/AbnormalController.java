package com.factoreal.backend.controller;

import com.factoreal.backend.dto.abnormalLog.AbnormalLogDto;
import com.factoreal.backend.dto.abnormalLog.AbnormalPagingDto;
import com.factoreal.backend.service.AbnormalLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/abnormal")
@RequiredArgsConstructor
@Tag(name = "이상 로그 API", description = "이상 상황 로그 조회 및 관리 API")
public class AbnormalController {

    private final AbnormalLogService abnormalLogService;

    // 전체 로그 조회
    @GetMapping
    @Operation(summary = "전체 이상 로그 조회", description = "모든 이상 로그를 페이징하여 조회합니다.")
    public Page<AbnormalLogDto> getAllAbnormalLogs(@ModelAttribute AbnormalPagingDto pagingDto) {
        return abnormalLogService.findAllAbnormalLogs(pagingDto);
    }

    @GetMapping("/unread")
    @Operation(summary = "읽지 않은 이상 로그 조회", description = "읽지 않은 이상 로그를 페이징하여 조회합니다.")
    public Page<AbnormalLogDto> getAllAbnormalLogsUnRead(@ModelAttribute AbnormalPagingDto pagingDto) {
        return abnormalLogService.findAllAbnormalLogsUnRead(pagingDto);
    }

    // 특정 이상 유형으로 필터링
    @GetMapping("/type/{abnormalType}")
    @Operation(summary = "이상 유형별 로그 조회", description = "특정 이상 유형으로 로그를 필터링하여 조회합니다.")
    public Page<AbnormalLogDto> getAbnormalLogsByType(
            @ModelAttribute AbnormalPagingDto pagingDto,
            @PathVariable String abnormalType) {
        return abnormalLogService.findAbnormalLogsByAbnormalType(pagingDto, abnormalType);
    }

    // 특정 타겟 ID로 필터링
    @GetMapping("/target/{targetType}/{targetId}")
    @Operation(summary = "타겟별 로그 조회", description = "특정 타겟 타입과 ID로 로그를 필터링하여 조회합니다.")
    public Page<AbnormalLogDto> getAbnormalLogsByTarget(
            @ModelAttribute AbnormalPagingDto pagingDto,
            @PathVariable String targetType,
            @PathVariable String targetId) {
        return abnormalLogService.findAbnormalLogsByTargetId(pagingDto, targetType, targetId);
    }

    // 알람 읽음 처리
    @PostMapping("/{abnormalId}/read")
    @Operation(summary = "알람 읽음 처리", description = "특정 알람을 읽음 상태로 표시합니다.")
    public ResponseEntity<Void> markAlarmAsRead(@PathVariable Long abnormalId) {
        boolean success = abnormalLogService.readCheck(abnormalId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // 읽지 않은 알람 개수 반환 -> websocket으로 전부 보내주기
    @GetMapping("/unread-count")
    @Operation(summary = "읽지 않은 알람 개수 조회", description = "현재 읽지 않은 알람의 총 개수를 반환합니다.")
    public ResponseEntity<Long> getUnreadAlarmCount() {
        Long count = abnormalLogService.readRequired();
        return ResponseEntity.ok(count);
    }
}
