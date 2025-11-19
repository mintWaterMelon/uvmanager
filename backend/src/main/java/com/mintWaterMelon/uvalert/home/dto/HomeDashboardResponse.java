package com.mintWaterMelon.uvalert.home.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 대시보드 응답")
public record HomeDashboardResponse(
        @Schema(description = "현재 서버 기준 시간", example = "2026-06-09T14:30:00")
        LocalDateTime currentTime,

        @Schema(description = "선택된 날짜", example = "2026-06-09")
        LocalDate selectedDate,

        @Schema(description = "조회 날짜 타입", example = "TODAY")
        HomeDateType dateType,

        @Schema(description = "조회 지역 정보")
        HomeLocationResponse location,

        @Schema(description = "홈 화면 배경 정보")
        HomeBackgroundResponse background,

        @Schema(description = "홈 화면 시간대별 테이블")
        HomeTableResponse table,

        @Schema(description = "사용자 안내 메시지")
        HomeAdviceResponse advice
) {
}