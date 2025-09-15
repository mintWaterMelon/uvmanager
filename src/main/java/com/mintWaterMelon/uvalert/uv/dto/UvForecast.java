package com.mintWaterMelon.uvalert.uv.dto;

import java.time.LocalDateTime;

public record UvForecast(
        int hourAfter,
        LocalDateTime forecastTime,
        int value,
        String level,
        String message
) {
}
