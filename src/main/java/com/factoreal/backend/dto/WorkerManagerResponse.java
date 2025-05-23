package com.factoreal.backend.dto;

import com.factoreal.backend.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 공간 담당자 후보 목록 응답 DTO (BE -> FE)
public class WorkerManagerResponse {
    private String workerId; // 직원 아이디
    private String name; // 직원 이름
    private Boolean isManager; // 공간담당자 여부

    public static WorkerManagerResponse fromEntity(Worker worker, Boolean isManager) {
        return WorkerManagerResponse.builder()
                .workerId(worker.getWorkerId())
                .name(worker.getName())
                .isManager(isManager)
                .build();
    }
} 