package com.factoreal.backend.service;

import com.factoreal.backend.dto.abnormalLog.AbnormalLogDto;
import com.factoreal.backend.dto.abnormalLog.AbnormalPagingDto;
import com.factoreal.backend.dto.abnormalLog.LogType;
import com.factoreal.backend.dto.abnormalLog.SystemLogResponseDto;
import com.factoreal.backend.dto.SensorKafkaDto;
import com.factoreal.backend.entity.AbnormalLog;
import com.factoreal.backend.entity.Zone;
import com.factoreal.backend.repository.AbnLogRepository;
import com.factoreal.backend.sender.WebSocketSender;
import com.factoreal.backend.strategy.RiskMessageProvider;
import com.factoreal.backend.strategy.enums.RiskLevel;
import com.factoreal.backend.strategy.enums.SensorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AbnormalLogService {
    private final AbnLogRepository abnLogRepository;
    private final ZoneService zoneService;
    private final RiskMessageProvider riskMessageProvider;
    private final ObjectMapper objectMapper;
    private final WebSocketSender webSocketSender;

    // 알람 객체를 받아와서 로그 객체 생성.
    @Transactional(rollbackFor = Exception.class)
    public AbnormalLog saveAbnormalLogFromKafkaDto(
            SensorKafkaDto sensorKafkaDto,
            SensorType sensorType,
            RiskLevel riskLevel,
            LogType targetType
    ) throws Exception{
        Zone zone = zoneService.getZone(sensorKafkaDto.getZoneId());

        log.info(">>>>>> zone : {} " ,zone);

        // DTO의 severity (AlarmEvent.RiskLevel)를 Entity RiskLevel로 매핑
//        RiskLevel entityRiskLevel = mapDtoSeverityToEntityRiskLevel(riskLevel);
        // [TODO] 현재는 스프린트 1 웹 푸쉬, 대시보드 히트 맵 알림 로그만 구현되있음. worker, equip 로그용 구현 필요.
        AbnormalLog abnormalLog = AbnormalLog.builder()
                .targetId(sensorKafkaDto.getSensorId())
                .targetType(targetType)
                .abnormalType(riskMessageProvider.getMessage(sensorType,riskLevel))
                .abnVal(sensorKafkaDto.getVal())
                .zone(zone)
                .detectedAt(LocalDateTime.parse(sensorKafkaDto.getTime()))
                .isRead(false)
                .build();

        return abnLogRepository.save(abnormalLog);
    }


    public Page<AbnormalLogDto> findAllAbnormalLogs(AbnormalPagingDto abnormalPagingDto) {
        // 한번에 DB전체를 주는 것이 아닌 구간 나눠서 전달하기 위함
        Pageable pageable = getPageable(abnormalPagingDto);
        Page<AbnormalLog> abnormalLogs = abnLogRepository.findAll(pageable);
        return abnormalLogs.map(abn_log ->
                AbnormalLogDto.builder()
                        .id(abn_log.getId())
                        .targetType(abn_log.getTargetType())
                        .targetId(abn_log.getTargetId())
                        .abnormalType(abn_log.getAbnormalType())
                        .abnVal(abn_log.getAbnVal())
                        .detectedAt(abn_log.getDetectedAt())
                        .zoneId(abn_log.getZone().getZoneId())
                        .zoneName(abn_log.getZone().getZoneName())
                        .build()
        );
    }

    public Page<AbnormalLogDto> findAllAbnormalLogsUnRead(AbnormalPagingDto abnormalPagingDto) {
        // 한번에 DB전체를 주는 것이 아닌 구간 나눠서 전달하기 위함
        Pageable pageable = getPageable(abnormalPagingDto);
        Page<AbnormalLog> abnormalLogs = abnLogRepository.findAllByIsReadIsFalse(pageable);
        return abnormalLogs.map(abn_log ->
                AbnormalLogDto.builder()
                        .id(abn_log.getId())
                        .targetType(abn_log.getTargetType())
                        .targetId(abn_log.getTargetId())
                        .abnormalType(abn_log.getAbnormalType())
                        .abnVal(abn_log.getAbnVal())
                        .detectedAt(abn_log.getDetectedAt())
                        .zoneId(abn_log.getZone().getZoneId())
                        .zoneName(abn_log.getZone().getZoneName())
                        .build()
        );
    }

    public Page<AbnormalLogDto> findAbnormalLogsByAbnormalType(AbnormalPagingDto abnormalPagingDto, String abnormalType){
        // 한번에 DB전체를 주는 것이 아닌 구간 나눠서 전달하기 위함
        Pageable pageable = getPageable(abnormalPagingDto);
        Page<AbnormalLog> abnormalLogs = abnLogRepository.findAbnormalLogsByAbnormalType(abnormalType,pageable);
        return abnormalLogs.map(abn_log ->
                AbnormalLogDto.builder()
                        .id(abn_log.getId())
                        .targetType(abn_log.getTargetType())
                        .targetId(abn_log.getTargetId())
                        .abnormalType(abn_log.getAbnormalType())
                        .abnVal(abn_log.getAbnVal())
                        .detectedAt(abn_log.getDetectedAt())
                        .zoneId(abn_log.getZone().getZoneId())
                        .zoneName(abn_log.getZone().getZoneName())
                        .build()
        );
    }

    //
    public Page<AbnormalLogDto> findAbnormalLogsByTargetId(AbnormalPagingDto abnormalPagingDto, String targetType, String targetId){
        // 한번에 DB전체를 주는 것이 아닌 구간 나눠서 전달하기 위함
        Pageable pageable = getPageable(abnormalPagingDto);
        Page<AbnormalLog> abnormalLogs = abnLogRepository.findAbnormalLogsByTargetTypeAndTargetId(
                targetType,
                targetId,
                pageable);
        return abnormalLogs.map(
                abn_log -> objectMapper.convertValue(abn_log, AbnormalLogDto.class)
        );
    }


    // FE에서 알람을 클릭한 경우 읽음으로 수정
    @Transactional
    public boolean readCheck(Long abnormalLogId){
        AbnormalLog abnormalLog = abnLogRepository.findById(abnormalLogId).orElse(null);
        if(abnormalLog == null){
            return false;
        }

        abnormalLog.setIsRead(true);
        abnLogRepository.save(abnormalLog);
        readRequired();
        return true;
    }
    @Transactional(readOnly = true)
    // 읽지 않은 알람이 몇개인지 반환
    public Long readRequired(){
        Long count =  abnLogRepository.countByIsReadFalse();
        webSocketSender.sendUnreadCount(count);
        return count;
    }

    @Transactional(readOnly = true)
    public Page<SystemLogResponseDto> findSystemLogsByZoneId(String zoneId, AbnormalPagingDto pagingDto) {
        log.info("공간 ID: {}의 시스템 로그 조회", zoneId);
        Pageable pageable = getPageable(pagingDto);
        
        // zoneId로 직접 필터링된 로그 조회
        Page<AbnormalLog> logs = abnLogRepository.findByZone_ZoneIdOrderByDetectedAtDesc(zoneId, pageable);
        return logs.map(SystemLogResponseDto::fromEntity);
    }

    private Pageable getPageable(AbnormalPagingDto abnormalPagingDto){
        return PageRequest.of(
                abnormalPagingDto.getPage(),
                abnormalPagingDto.getSize()
        );
    }
}
