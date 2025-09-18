package com.mintWaterMelon.uvalert.sunscreen.dto;

public record SunscreenAlertCheckResponse(
        Long alertId,
        String areaNo,
        String locationName,
        boolean enabled,
        int uvThreshold,
        int currentUvValue,
        String currentUvLevel,
        boolean shouldNotify,
        String message
) {
}