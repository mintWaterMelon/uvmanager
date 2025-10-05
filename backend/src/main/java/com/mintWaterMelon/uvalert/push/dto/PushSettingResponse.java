package com.mintWaterMelon.uvalert.push.dto;

import java.time.LocalTime;

public record PushSettingResponse(
        boolean uvAlertEnabled,
        int uvAlertThreshold,
        boolean dustAlertEnabled,
        LocalTime alertTime
) {
}