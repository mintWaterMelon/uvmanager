package com.mintWaterMelon.uvalert.uv.dto;

public record UvForecast(
        int hourAfter,
        int value,
        String level,
        String message
) {
}
