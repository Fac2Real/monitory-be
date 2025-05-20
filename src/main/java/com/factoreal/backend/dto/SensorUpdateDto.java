package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Sensor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 센서 정보 수정용 DTO
 * 센서의 임계치와 허용치를 업데이트하기 위한 데이터를 포함합니다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class SensorUpdateDto {
    // private String sensorPurpose;  // 센서목적
    // private String location;       // 위치
    private Double sensorThres;      // 임계치
    private Double allowVal;       // 허용치(오차범위)

    public SensorUpdateDto() {}
    
    /**
     * 센서 엔티티에서 업데이트 관련 필드만 추출하여 DTO로 변환
     * @param sensor 변환할 센서 엔티티
     * @return 변환된 SensorUpdateDto 객체
     */
    public static SensorUpdateDto fromEntity(Sensor sensor) {
        if (sensor == null) return null;
        
        return SensorUpdateDto.builder()
                .sensorThres(sensor.getSensorThres())
                .allowVal(sensor.getAllowVal())
                .build();
    }
}