package com.factoreal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 설비 생성 요청 DTO
 * 프론트엔드에서 설비 생성 시 필요한 정보를 포함합니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 설비 생성 요청 DTO ( FE -> BE )
public class EquipCreateRequest {
    private String equipName;  // 설비 이름
    private String zoneName;   // 설비가 위치할 공간 이름
}