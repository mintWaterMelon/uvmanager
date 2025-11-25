package com.mintWaterMelon.uvalert.weather.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "단기예보 항목")
public record ShortForecastItem(
        @Schema(description = "예보 항목 코드", example = "TMP")
        String category,

        @Schema(description = "예보 날짜", example = "20260609")
        String fcstDate,

        @Schema(description = "예보 시간", example = "1200")
        String fcstTime,

        @Schema(description = "예보 값", example = "27")
        String fcstValue
) {
}