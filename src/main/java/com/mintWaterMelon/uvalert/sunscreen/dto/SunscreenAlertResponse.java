package com.mintWaterMelon.uvalert.sunscreen.dto;

import java.time.LocalTime;

public record SunscreenAlertResponse(
        Long id,
        String areaNo,
        String locationName,
        LocalTime alertTime,
        int uvThreshold,
        boolean enabled
) {
}
