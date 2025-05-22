package com.factoreal.backend.domain.zone.dto;

import com.factoreal.backend.domain.sensor.dto.response.SensorInfoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public class ZoneItemDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("env_sensor")
    private List<SensorInfoResponse> envSensor;
    @JsonProperty("facility")
    private List<FacilityDto> facility;
}
