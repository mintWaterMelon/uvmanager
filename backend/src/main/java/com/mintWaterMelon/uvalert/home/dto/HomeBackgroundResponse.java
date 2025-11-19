package com.mintWaterMelon.uvalert.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 배경 정보")
public record HomeBackgroundResponse(
        @Schema(description = "배경 테마", example = "DAY_NORMAL")
        HomeBackgroundTheme theme,

        @Schema(description = "배경 색상", example = "#E0F2FE")
        String color,

        @Schema(description = "배경 설명", example = "맑은 낮 배경")
        String description
) {
}