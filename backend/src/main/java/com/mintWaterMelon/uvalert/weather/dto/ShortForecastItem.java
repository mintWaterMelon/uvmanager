package com.mintWaterMelon.uvalert.weather.dto;

public record ShortForecastItem(
        String category,
        String fcstDate,
        String fcstTime,
        String fcstValue
) {
}