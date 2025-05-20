package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공간 정보 수정용 DTO
 * 공간의 이름을 변경하기 위한 데이터를 포함합니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneUpdateDto {
    private String zoneName; // 수정할 공간명
    
    /**
     * Zone 엔티티로부터 업데이트용 DTO 생성
     * @param zone 변환할 공간 엔티티
     * @return 변환된 ZoneUpdateDto 객체
     */
    public static ZoneUpdateDto fromEntity(Zone zone) {
        if (zone == null) return null;
        
        return ZoneUpdateDto.builder()
                .zoneName(zone.getZoneName())
                .build();
    }
}
