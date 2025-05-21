package com.factoreal.backend.kafka.strategy.alarmMessage;

import com.factoreal.backend.kafka.strategy.enums.RiskLevel;
import com.factoreal.backend.kafka.strategy.enums.SensorType;

public interface RiskMessageProvider {
    String getMessage(SensorType sensorType, RiskLevel riskLevel);
}
