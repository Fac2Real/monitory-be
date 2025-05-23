package com.factoreal.backend.service;

import com.factoreal.backend.entity.AbnormalLog;
import com.factoreal.backend.entity.ControlLog;
import com.factoreal.backend.entity.Zone;
import com.factoreal.backend.repository.ControlLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ControlLogService {
    
    private final ControlLogRepository controlLogRepository;

    @Transactional
    public ControlLog saveControlLog(AbnormalLog abnormalLog, String controlType, Double controlVal, Integer controlStat, Zone zone) {
        ControlLog controlLog = ControlLog.builder()
                .abnormalLog(abnormalLog)
                .controlType(controlType)
                .controlVal(controlVal)
                .controlStat(controlStat)
                .executedAt(LocalDateTime.now())
                .zone(zone)
                .build();

        return controlLogRepository.save(controlLog);
    }
} 