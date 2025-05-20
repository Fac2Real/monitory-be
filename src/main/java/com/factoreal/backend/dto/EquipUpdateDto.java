package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Equip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 설비 정보 수정용 DTO
 * 설비의 이름을 변경하기 위한 데이터를 포함합니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipUpdateDto {
    private String equipName; // 수정할 설비명
    
    /**
     * Equip 엔티티로부터 업데이트용 DTO 생성
     * @param equip 변환할 설비 엔티티
     * @return 변환된 EquipUpdateDto 객체
     */
    public static EquipUpdateDto fromEntity(Equip equip) {
        if (equip == null) return null;
        
        return EquipUpdateDto.builder()
                .equipName(equip.getEquipName())
                .build();
    }
}
