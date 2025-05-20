package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Sensor;
import com.factoreal.backend.entity.Zone;
import com.factoreal.backend.entity.Equip;
import com.factoreal.backend.strategy.enums.SensorType;
import lombok.*;

/**
 * 센서 정보 전송 객체 (BE -> FE)
 * 센서 기본 정보와 임계치/허용치를 포함합니다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDto {
    private String sensorId;      // 센서 고유 ID
    private String sensorType;    // 센서 종류 (temp, humid, vibration, dust, voc, current)
    private String zoneId;        // 센서가 위치한 공간 ID
    private String equipId;       // 센서가 속한 설비 ID (환경 센서는 zoneId와 동일)
    private Double sensorThres;   // 센서 임계치
    private Double allowVal;      // 허용치 (오차 범위)

    /**
     * Sensor 엔티티를 DTO로 변환
     * @param sensor 변환할 센서 엔티티
     * @return 변환된 SensorDto 객체
     */
    public static SensorDto fromEntity(Sensor sensor) {
        if (sensor == null) return null;

        return SensorDto.builder()
                .sensorId(sensor.getSensorId())
                .sensorType(sensor.getSensorType().toString())
                .zoneId(sensor.getZone().getZoneId())
                .equipId(sensor.getEquip().getEquipId())
                .sensorThres(sensor.getSensorThres())
                .allowVal(sensor.getAllowVal())
                .build();
    }

    /**
     * DTO를 Sensor 엔티티로 변환 (새 엔티티 생성)
     * @param dto 변환할 센서 DTO
     * @param zoneEntity 센서가 속할 Zone 엔티티
     * @param equipEntity 센서가 속할 Equip 엔티티
     * @return 변환된 Sensor 엔티티 (저장 전)
     */
    public static Sensor toEntity(SensorDto dto, Zone zoneEntity, Equip equipEntity) {
        if (dto == null) return null;
        
        Sensor sensor = new Sensor();
        sensor.setSensorId(dto.getSensorId());
        sensor.setSensorType(SensorType.valueOf(dto.getSensorType()));
        sensor.setZone(zoneEntity);
        sensor.setEquip(equipEntity);
        sensor.setSensorThres(dto.getSensorThres());
        sensor.setAllowVal(dto.getAllowVal());
        return sensor;
    }
}