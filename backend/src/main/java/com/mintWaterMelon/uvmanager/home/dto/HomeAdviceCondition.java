package com.mintWaterMelon.uvmanager.home.dto;

public record HomeAdviceCondition(
        HomeMode mode,
        String representativeWeather,
        int maxUv,
        int maxPrecipitationProbability,
        Integer representativeTemperature
) {
}