package com.factoreal.backend.global.kafka.strategy.alarmMessage;

import com.factoreal.backend.global.kafka.strategy.enums.RiskLevel;
import com.factoreal.backend.global.kafka.strategy.enums.SensorType;

public interface RiskMessageProvider {
    String getMessage(SensorType sensorType, RiskLevel riskLevel);
}
