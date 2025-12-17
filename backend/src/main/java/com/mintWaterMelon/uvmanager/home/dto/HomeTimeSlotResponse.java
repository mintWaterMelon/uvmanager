package com.mintWaterMelon.uvmanager.home.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 시간 슬롯")
public record HomeTimeSlotResponse(
        @Schema(description = "날짜", example = "2026-06-09")
        LocalDate date,

        @Schema(description = "시간", example = "12:00")
        String time,

        @Schema(description = "현재 시간대 여부", example = "true")
        boolean current
) {
}