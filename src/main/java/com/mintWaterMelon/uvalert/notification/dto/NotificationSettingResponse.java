package com.mintWaterMelon.uvalert.notification.dto;

public record NotificationSettingResponse(
        Long id,
        String region,
        int uvThreshold,
        boolean enabled
) {
}