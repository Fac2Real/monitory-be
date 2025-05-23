package com.factoreal.backend.service;

import com.factoreal.backend.domain.zone.entity.ZoneHist;
import com.factoreal.backend.domain.zone.dao.ZoneHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneHistoryService {
    private final ZoneHistoryRepository zoneHistRepository;

    public List<ZoneHist> findByZone_ZoneIdAndExistFlag(String zoneId, Integer existFlag) {
        return zoneHistRepository.findByZone_ZoneIdAndExistFlag(zoneId, existFlag);
    }

    public ZoneHist saveZoneHistory(ZoneHist zoneHist) {
        return zoneHistRepository.save(zoneHist);
    }

    public ZoneHist findByWorker_WorkerIdAndExistFlag(String workerId, Integer existFlag) {
        return zoneHistRepository.findByWorker_WorkerIdAndExistFlag(workerId, existFlag);
    }

    public ZoneHist save(ZoneHist currentLocation) {
        return zoneHistRepository.save(currentLocation);
    }
}
