package com.factoreal.backend.domain.equip.dto.response;

import com.factoreal.backend.domain.sensor.dto.response.SensorInfoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipDetailResponse {
    private String equipId;
    private String equipName;
    private List<SensorInfoResponse> facSensor;
}
