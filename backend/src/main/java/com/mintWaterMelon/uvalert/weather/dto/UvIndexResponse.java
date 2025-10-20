package com.mintWaterMelon.uvalert.weather.dto;

import java.util.Map;

public record UvIndexResponse(
        String areaNo,
        String baseTime,
        Map<Integer, Integer> hourlyValues
) {
}