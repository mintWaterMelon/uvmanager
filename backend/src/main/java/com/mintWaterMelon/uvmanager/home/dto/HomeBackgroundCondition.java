package com.mintWaterMelon.uvmanager.home.dto;

public record HomeBackgroundCondition(
        HomeMode mode,
        String representativeWeather,
        int maxUv,
        int maxPrecipitationProbability,
        Integer representativeTemperature
) {
}