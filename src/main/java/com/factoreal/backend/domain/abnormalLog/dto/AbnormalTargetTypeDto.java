package com.factoreal.backend.domain.abnormalLog.dto;

import lombok.Data;

@Data
public class AbnormalTargetTypeDto {
    LogType targetType;
    String targetId;
}