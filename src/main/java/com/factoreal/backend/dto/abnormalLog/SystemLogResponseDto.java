package com.factoreal.backend.dto.abnormalLog;

import com.factoreal.backend.entity.AbnormalLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogResponseDto {
    private String zoneId;
    private String zoneName;
    private String sensorType;
    private int dangerLevel;
    private double value;
    private LocalDateTime timestamp;
    private String abnormalType;
    private String targetId;

    public static SystemLogResponseDto fromEntity(AbnormalLog abnormalLog) {
        return SystemLogResponseDto.builder()
                .zoneId(abnormalLog.getZone().getZoneId())
                .zoneName(abnormalLog.getZone().getZoneName())
                .sensorType(abnormalLog.getTargetType().toString())
                .dangerLevel(calculateDangerLevel(abnormalLog.getAbnormalType()))
                .value(abnormalLog.getAbnVal())
                .timestamp(abnormalLog.getDetectedAt())
                .abnormalType(abnormalLog.getAbnormalType())
                .targetId(abnormalLog.getTargetId())
                .build();
    }

    private static int calculateDangerLevel(String abnormalType) {
        if (abnormalType.contains("위험")) return 2;
        if (abnormalType.contains("주의")) return 1;
        return 0;
    }
} 

/**
 * {
  "content": [
    {
      "zoneId": "zone123",
      "zoneName": "작업장 A",
      "sensorType": "TEMPERATURE",
      "dangerLevel": 2,
      "value": 35.5,
      "timestamp": "2024-03-20T14:30:00",
      "abnormalType": "온도 위험",
      "targetId": "sensor456"
    }
    // ... 더 많은 로그
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 50,
  "totalPages": 5
}
 */