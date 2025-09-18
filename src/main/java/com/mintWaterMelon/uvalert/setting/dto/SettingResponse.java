package com.mintWaterMelon.uvalert.setting.dto;

import java.time.LocalTime;

public record SettingResponse(
        String defaultAreaNo,
        String defaultLocationName,
        int defaultUvThreshold,
        boolean sunscreenAlertEnabled,
        LocalTime defaultAlertTime
) {
}
