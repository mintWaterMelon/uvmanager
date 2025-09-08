package com.mintWaterMelon.uvalert.uv.dto;

public record UvIndexSampleResponse(
        String region,
        int uvIndex,
        String level,
        String message
) {
}