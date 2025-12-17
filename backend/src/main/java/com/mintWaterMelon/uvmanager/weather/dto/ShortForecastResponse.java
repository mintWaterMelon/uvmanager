package com.mintWaterMelon.uvmanager.weather.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "단기예보 응답")
public record ShortForecastResponse(
        @Schema(description = "단기예보 항목 목록")
        List<ShortForecastItem> items
) {
}