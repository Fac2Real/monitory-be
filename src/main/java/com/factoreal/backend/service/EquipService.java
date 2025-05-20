package com.factoreal.backend.service;

import com.factoreal.backend.dto.EquipCreateRequest;
import com.factoreal.backend.dto.EquipDto;
import com.factoreal.backend.dto.EquipUpdateDto;
import com.factoreal.backend.entity.Equip;
import com.factoreal.backend.entity.Zone;
import com.factoreal.backend.repository.EquipRepository;
import com.factoreal.backend.repository.ZoneRepository;
import com.factoreal.backend.util.EquipIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipService {
    private final EquipRepository equipRepo;
    private final ZoneRepository zoneRepo;

    /**
     * 설비 기본 정보를 저장합니다.
     * @param dto 저장할 설비 정보가 담긴 DTO
     * @return 저장된 Equip 엔티티
     * @throws ResponseStatusException 존재하지 않는 공간 ID인 경우 예외 발생
     */
    @Transactional
    public Equip saveEquip(EquipDto dto) {
        Zone zone = zoneRepo.findById(dto.getZoneId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "존재하지 않는 공간 ID: " + dto.getZoneId()));
        
        // DTO -> Entity 변환 후 저장
        return equipRepo.save(dto.toEntity(zone));
    }

    /**
     * 설비 정보를 생성합니다.
     * @param req 설비 생성 요청 객체 (설비명, 공간명 포함)
     * @return 생성된 설비 정보 DTO
     * @throws ResponseStatusException 존재하지 않는 공간명인 경우 예외 발생
     */
    @Transactional
    public EquipDto createEquip(EquipCreateRequest req) {
        // 1. UI에서 입력받은 zoneName으로 zoneId 조회
        Zone zone = zoneRepo.findByZoneName(req.getZoneName())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "존재하지 않는 공간명: " + req.getZoneName()));
    
        // 2. 고유한 설비ID 생성
        String equipId = EquipIdGenerator.generateEquipId();

        // 3. 설비 정보 저장
        Equip equip = new Equip(equipId, req.getEquipName(), zone);
        Equip savedEquip = equipRepo.save(equip);

        // 4. Entity -> DTO 변환하여 반환
        return EquipDto.fromEntity(savedEquip, zone.getZoneName());
    }

    /**
     * 설비 정보를 수정합니다.
     * @param equipId 수정할 설비 ID
     * @param dto 수정 정보가 담긴 DTO
     * @return 수정된 설비 정보 DTO
     * @throws ResponseStatusException 존재하지 않는 설비 ID인 경우 예외 발생
     */
    @Transactional
    public EquipDto updateEquip(String equipId, EquipUpdateDto dto) {
        // 1. 수정할 설비가 존재하는지 확인
        Equip equip = equipRepo.findById(equipId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "존재하지 않는 설비 ID: " + equipId));

        // 2. 설비명 업데이트
        equip.setEquipName(dto.getEquipName());
        Equip updated = equipRepo.save(equip);

        // 3. Entity -> DTO 변환하여 반환
        Zone zone = zoneRepo.findById(updated.getZone().getZoneId())
            .orElse(new Zone("", "미등록 공간"));
            
        return EquipDto.fromEntity(updated, zone.getZoneName());
    }
    
    /**
     * 모든 설비 정보를 조회합니다.
     * @return 설비 정보 DTO 리스트
     */
    public List<EquipDto> getAllEquips() {
        return equipRepo.findAll().stream()
            .map(equip -> {
                Zone zone = zoneRepo.findById(equip.getZone().getZoneId())
                    .orElse(new Zone("", "미등록 공간"));
                return EquipDto.fromEntity(equip, zone.getZoneName());
            })
            .collect(Collectors.toList());
    }
}
