package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Worker;
import com.factoreal.backend.entity.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 공간 담당자 정보 조회 시 사용되는 DTO (BE -> FE)
public class ZoneManagerResponseDto {
    private String workerId;          // 작업자 ID
    private String name;              // 작업자 이름
    private String phoneNumber;       // 연락처
    private String email;             // 이메일
    private String currentZoneId;     // 현재 위치한 공간 ID
    private String currentZoneName;   // 현재 위치한 공간 이름

    public static ZoneManagerResponseDto fromEntity(Worker worker, Zone currentZone) {
        return ZoneManagerResponseDto.builder()
                .workerId(worker.getWorkerId())
                .name(worker.getName())
                .phoneNumber(worker.getPhoneNumber())
                .email(worker.getEmail())
                .currentZoneId(currentZone != null ? currentZone.getZoneId() : null)
                .currentZoneName(currentZone != null ? currentZone.getZoneName() : null)
                .build();
    }
} 