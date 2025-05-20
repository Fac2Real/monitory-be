package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * 공간 정보 전송 객체
 * 공간의 기본 정보(ID, 이름)를 포함합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneDto {
    private String zoneId;    // 공간 고유 ID
    private String zoneName;  // 공간 이름
    
    /**
     * Zone 엔티티를 DTO로 변환
     * @param zone 변환할 공간 엔티티
     * @return 변환된 ZoneDto 객체
     */
    public static ZoneDto fromEntity(Zone zone) {
        if (zone == null) return null;
        
        return ZoneDto.builder()
                .zoneId(zone.getZoneId())
                .zoneName(zone.getZoneName())
                .build();
    }
    
    /**
     * DTO를 Zone 엔티티로 변환
     * @return 변환된 Zone 엔티티
     */
    public Zone toEntity() {
        return new Zone(zoneId, zoneName);
    }
}