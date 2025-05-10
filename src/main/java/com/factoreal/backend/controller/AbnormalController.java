package com.factoreal.backend.controller;

import com.factoreal.backend.dto.abnormalLog.AbnormalLogDto;
import com.factoreal.backend.dto.abnormalLog.AbnormalPagingDto;
import com.factoreal.backend.service.AbnormalLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/abnormal")
@RequiredArgsConstructor
public class AbnormalController {

    private final AbnormalLogService abnormalLogService;

    // 전체 로그 조회
    @GetMapping
    public Page<AbnormalLogDto> getAllAbnormalLogs(@ModelAttribute AbnormalPagingDto pagingDto) {
        return abnormalLogService.findAllAbnormalLogs(pagingDto);
    }

    @GetMapping("/unread")
    public Page<AbnormalLogDto> getAllAbnormalLogsUnRead(@ModelAttribute AbnormalPagingDto pagingDto) {
        return abnormalLogService.findAllAbnormalLogsUnRead(pagingDto);
    }

    // 특정 이상 유형으로 필터링
    @GetMapping("/type/{abnormalType}")
    public Page<AbnormalLogDto> getAbnormalLogsByType(
            @ModelAttribute AbnormalPagingDto pagingDto,
            @PathVariable String abnormalType) {
        return abnormalLogService.findAbnormalLogsByAbnormalType(pagingDto, abnormalType);
    }

    // 특정 타겟 ID로 필터링
    @GetMapping("/target/{targetType}/{targetId}")
    public Page<AbnormalLogDto> getAbnormalLogsByTarget(
            @ModelAttribute AbnormalPagingDto pagingDto,
            @PathVariable String targetType,
            @PathVariable String targetId) {
        return abnormalLogService.findAbnormalLogsByTargetId(pagingDto, targetType, targetId);
    }

    // 알람 읽음 처리
    @PostMapping("/{abnormalId}/read")
    public ResponseEntity<Void> markAlarmAsRead(@PathVariable Long abnormalId) {
        boolean success = abnormalLogService.readCheck(abnormalId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // 읽지 않은 알람 개수 반환 -> websocket으로 전부 보내주기
    @GetMapping("/unread-count")
    public ResponseEntity<Object> getUnreadAlarmCount() {
        abnormalLogService.readRequired();
        return ResponseEntity.ok().build();
    }
}
