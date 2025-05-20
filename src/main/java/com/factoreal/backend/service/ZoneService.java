package com.factoreal.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.factoreal.backend.dto.*;
import com.factoreal.backend.entity.Equip;
import com.factoreal.backend.entity.Sensor;
import com.factoreal.backend.repository.EquipRepository;
import com.factoreal.backend.repository.SensorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.factoreal.backend.entity.Zone;
import com.factoreal.backend.repository.ZoneRepository;
import com.factoreal.backend.util.ZoneIdGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository repo;

    private final SensorRepository sensorRepo;
    private final EquipRepository equipRepo;

    private final EquipService equipService;
    
    /**
     * 공간 정보를 생성합니다.
     * @param zoneName 생성할 공간 이름
     * @return 생성된 공간 정보 DTO
     * @throws ResponseStatusException 이미 존재하는 공간명인 경우 예외 발생
     */
    @Transactional
    public ZoneDto createZone(String zoneName) {
        // 1. 공간명 중복 체크
        if (repo.findByZoneName(zoneName).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "이미 존재하는 공간명: " + zoneName);
        }
        // 2. 고유한 공간ID 할당
        String zoneId = ZoneIdGenerator.generateZoneId();
        // 3. save 한 뒤 DTO로 반환
        Zone zone = repo.save(new Zone(zoneId, zoneName));
        // 4. 공간에 대한 센서인 경우, equip_id가 zone_id와 동일하고 설비명이 empty임.
        EquipDto equipDto = EquipDto
                .builder()
                .equipName("empty")
                .zoneId(zoneId)
                .zoneName(zoneName)
                .build();
        equipService.saveEquip(equipDto);
        
        // 5. Entity -> DTO 변환하여 반환
        return ZoneDto.fromEntity(zone);
    }

    /**
     * 공간 정보를 수정합니다.
     * @param zoneName 수정할 공간 이름
     * @param dto 수정 정보가 담긴 DTO
     * @return 수정된 공간 정보 DTO
     * @throws ResponseStatusException 존재하지 않는 공간명이거나 수정할 이름이 이미 존재하는 경우 예외 발생
     */
    @Transactional
    public ZoneDto updateZone(String zoneName, ZoneUpdateDto dto) {
        // 1. 수정할 공간이 존재하는지 확인
        Zone zone = repo.findByZoneName(zoneName)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "존재하지 않는 공간: " + zoneName));

        // 2. 새로운 공간명이 이미 존재하는지 확인
        if (!zone.getZoneName().equals(dto.getZoneName()) && 
            repo.findByZoneName(dto.getZoneName()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "이미 존재하는 공간명: " + dto.getZoneName());
        }

        // 3. 공간명 업데이트
        zone.setZoneName(dto.getZoneName());
        Zone updatedZone = repo.save(zone);

        // 4. Entity -> DTO 변환하여 반환
        return ZoneDto.fromEntity(updatedZone);
    }

    /**
     * 모든 공간 정보를 조회합니다.
     * @return 공간 정보 DTO 리스트
     */
    public List<ZoneDto> getAllZones() {
        return repo.findAll().stream()
                .map(ZoneDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 모든 공간의 설비 및 센서 데이터를 계층적으로 구조화하여 조회합니다.
     * @return 공간별 설비 및 센서 정보가 구조화된 ZoneItemDto 리스트
     */
    @Transactional
    public List<ZoneItemDto> getAllZoneItems() {

        List<Zone> zones = repo.findAll();

        return zones.stream()
                .map(zone -> {
                    List<Sensor> sensors = sensorRepo.findByZone(zone);

                    // 환경 센서
                    List<Sensor> envSensors = sensors.stream()
                            .filter(s -> Objects.equals(s.getZone().getZoneId(), s.getEquip().getEquipId()))
                            .toList();

                    // 1) Sensor 엔티티 → SensorDto 변환
                    List<SensorDto> envSensorDtos = envSensors.stream()      // List<Sensor>
                            .map(SensorDto::fromEntity)                      // Sensor → SensorDto
                            .toList();

                    List<Equip> equips = equipRepo.findEquipsByZone(zone).stream()
                            .filter(e -> e.getEquipName() != null && !e.getEquipName().equalsIgnoreCase("empty"))
                            .toList();   // empty이름을 가진 설비(환경센서)는 설비 목록에서 제외하기

                    // 설비 센서 그룹핑
                    Map<String, List<SensorDto>> facGroup = sensors.stream()
                            .filter(s -> !Objects.equals(s.getZone().getZoneId(), s.getEquip().getEquipId()))
                            .map(SensorDto::fromEntity)                 // Sensor → SensorDto
                            .collect(Collectors.groupingBy(SensorDto::getEquipId));

                    List<FacilityDto> facilities = equips.stream()
                            .map(entry -> {
                                String equipId = entry.getEquipId();
                                String equipName = equipRepo.findEquipNameByEquipId(equipId); // 1-row 조회

                                List<SensorDto> facSensors = facGroup.getOrDefault(equipId, List.of());

                                return FacilityDto.builder()
                                        .name(equipName)
                                        .facSensor(facSensors)
                                        .id(equipId)
                                        .build();
                            })
                            .toList();

                    /* ZoneItemDto 조립 */
                    return ZoneItemDto.builder()
                            .title(zone.getZoneName())
                            .envSensor(envSensorDtos)
                            .facility(facilities)
                            .build();
                })
                .toList();
    }

    /**
     * 공간 ID로 Zone 엔티티를 조회합니다.
     * @param zoneId 조회할 공간 ID
     * @return 조회된 Zone 엔티티
     */
    public Zone getZone(String zoneId) {
        return repo.findByZoneId(zoneId);
    }
}