package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerDto {
    private String workerId;
    private String name;
    private String phoneNumber;
    private String email;
    private Boolean isManager; // 관리자 여부

    // Entity -> DTO 변환
    public static WorkerDto fromEntity(Worker worker, Boolean isManager) {
        return WorkerDto.builder()
                .workerId(worker.getWorkerId())
                .name(worker.getName())
                .phoneNumber(worker.getPhoneNumber())
                .email(worker.getEmail())
                .isManager(isManager)
                .build();
    }
} 