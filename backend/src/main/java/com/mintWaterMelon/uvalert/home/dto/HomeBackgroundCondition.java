package com.mintWaterMelon.uvalert.home.dto;

public record HomeBackgroundCondition(
        HomeMode mode,
        String representativeWeather,
        int maxUv,
        int maxAirStagnation,
        Integer representativeTemperature
) {
}