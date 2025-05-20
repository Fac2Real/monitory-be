package com.factoreal.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 공간별 센서/설비 구조화 DTO
 * 하나의 공간에 포함된 환경센서와 설비(및 설비 센서) 정보를 계층적으로 구조화합니다.
 */
@Getter
@Builder
public class ZoneItemDto {
    @JsonProperty("title")
    private String title;       // 공간 이름
    
    @JsonProperty("env_sensor")
    private List<SensorDto> envSensor;    // 환경 센서 목록
    
    @JsonProperty("facility")
    private List<FacilityDto> facility;   // 설비 목록
}
