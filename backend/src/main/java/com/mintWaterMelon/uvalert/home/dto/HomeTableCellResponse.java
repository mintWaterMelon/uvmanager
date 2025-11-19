package com.mintWaterMelon.uvalert.home.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 테이블 셀")
public record HomeTableCellResponse(
        @Schema(description = "날짜", example = "2026-06-09")
        LocalDate date,

        @Schema(description = "시간", example = "12:00")
        String time,

        @Schema(description = "주요 표시 텍스트", example = "7")
        String mainText,

        @Schema(description = "보조 표시 텍스트", example = "높음")
        String subText,

        @Schema(description = "계산용 숫자 값", example = "7")
        Integer value,

        @Schema(description = "상태 레벨", example = "HIGH")
        String level
) {
}