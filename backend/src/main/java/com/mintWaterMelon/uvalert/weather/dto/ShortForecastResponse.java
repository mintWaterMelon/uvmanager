package com.mintWaterMelon.uvalert.weather.dto;

import java.util.List;

public record ShortForecastResponse(
        List<ShortForecastItem> items
) {
}