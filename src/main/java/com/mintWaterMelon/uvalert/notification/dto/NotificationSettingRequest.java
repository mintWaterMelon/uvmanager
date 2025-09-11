package com.mintWaterMelon.uvalert.notification.dto;

public record NotificationSettingRequest(
        String region,
        int uvThreshold,
        boolean enabled
) {
}