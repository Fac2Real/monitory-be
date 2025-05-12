package com.factoreal.backend.dto.abnormalLog;

import lombok.Data;

@Data
public class AbnormalTargetTypeDto {
    LogType targetType;
    String targetId;
}