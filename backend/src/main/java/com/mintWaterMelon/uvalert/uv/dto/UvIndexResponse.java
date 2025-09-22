package com.mintWaterMelon.uvalert.uv.dto;

import java.util.List;

public record UvIndexResponse(
        String areaNo,
        String baseTime,
        List<UvForecast> forecasts
) {
}
