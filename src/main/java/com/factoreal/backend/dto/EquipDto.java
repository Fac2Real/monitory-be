package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Equip;
import com.factoreal.backend.entity.Zone;
import lombok.*;

/**
 * 설비 정보 전송 객체
 * 설비의 기본 정보와 소속 공간 정보를 포함합니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 설비 정보 DTO
public class EquipDto {
    private String equipId;    // 설비 고유 ID
    private String equipName;  // 설비 이름
    private String zoneName;   // 소속 공간 이름
    private String zoneId;     // 소속 공간 ID
    
    /**
     * Equip 엔티티를 DTO로 변환
     * @param equip 변환할 설비 엔티티
     * @param zoneName 설비가 속한 공간 이름 (선택적)
     * @return 변환된 EquipDto 객체
     */
    public static EquipDto fromEntity(Equip equip, String zoneName) {
        if (equip == null) return null;
        
        return EquipDto.builder()
                .equipId(equip.getEquipId())
                .equipName(equip.getEquipName())
                .zoneName(zoneName != null ? zoneName : equip.getZone().getZoneName())
                .zoneId(equip.getZone().getZoneId())
                .build();
    }
    
    /**
     * DTO를 Equip 엔티티로 변환
     * @param dto 변환할 설비 DTO
     * @param zone 설비가 속할 Zone 엔티티
     * @return 변환된 Equip 엔티티
     */
    public static Equip toEntity(EquipDto dto, Zone zone) {
        if (dto == null) return null;
        return new Equip(dto.getEquipId(), dto.getEquipName(), zone);
    }
}