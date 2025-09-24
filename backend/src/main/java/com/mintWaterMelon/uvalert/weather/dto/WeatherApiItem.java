package com.mintWaterMelon.uvalert.weather.dto;

public record WeatherApiItem(
        String category,
        String fcstDate,
        String fcstTime,
        String fcstValue
) {
}