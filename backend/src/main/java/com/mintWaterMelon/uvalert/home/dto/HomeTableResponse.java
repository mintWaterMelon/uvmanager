package com.mintWaterMelon.uvalert.home.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 테이블 응답")
public record HomeTableResponse(
        @Schema(description = "시간 슬롯 목록")
        List<HomeTimeSlotResponse> timeSlots,

        @Schema(description = "테이블 행 목록")
        List<HomeTableRowResponse> rows
) {
}