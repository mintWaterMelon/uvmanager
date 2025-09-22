package com.mintWaterMelon.uvalert.home.dto;

import com.mintWaterMelon.uvalert.uv.dto.UvForecast;

import java.time.LocalDateTime;
import java.util.List;

public record HomeResponse(
        LocalDateTime currentTime,
        HomeLocationResponse location,
        String baseTime,
        UvForecast currentUv,
        List<UvForecast> forecasts
) {
}
