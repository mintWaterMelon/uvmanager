package com.mintWaterMelon.uvalert.weather.dto;

import java.util.List;

public record WeatherApiItemsResponse(
        List<WeatherApiItem> items
) {
}