package com.factoreal.backend.domain.zone.dto;

import com.factoreal.backend.domain.sensor.dto.SensorDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter            // ← 모든 필드 Getter
@Setter            // ← Setter (원하면 삭제)
@Builder           // ← ⭐ Builder 자동 생성
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDto {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("fac_sensor")
    private List<SensorDto> facSensor;
}
