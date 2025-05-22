package com.factoreal.backend.domain.abnormalLog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "페이징 정보 DTO")
public class AbnormalPagingDto {
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private int page;
    
    @Schema(description = "페이지 크기", example = "10")
    private int size;
}
