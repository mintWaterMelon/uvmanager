package com.mintWaterMelon.uvalert.push.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record PushSettingRequest(
        boolean uvAlertEnabled,

        @Min(value = 0, message = "자외선 알림 기준값은 0 이상이어야 합니다.")
        @Max(value = 11, message = "자외선 알림 기준값은 11 이하여야 합니다.")
        int uvAlertThreshold,

        boolean dustAlertEnabled,

        @NotNull(message = "알림 시간은 필수입니다.")
        LocalTime alertTime
) {
}