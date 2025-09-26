package com.mintWaterMelon.uvalert.home.dto;

public record HomeAdviceCondition(
        HomeMode mode,
        String representativeWeather,
        int maxUv,
        int maxAirStagnation,
        Integer representativeTemperature
) {
}