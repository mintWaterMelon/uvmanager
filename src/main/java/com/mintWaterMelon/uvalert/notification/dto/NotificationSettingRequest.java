package com.mintWaterMelon.uvalert.notification.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record NotificationSettingRequest(
        @NotBlank(message = "지역은 필수입니다.")
        String region,

        @Min(value = 0, message = "UV 기준값은 0 이상이어야 합니다.")
        @Max(value = 11, message = "UV 기준값은 11 이하여야 합니다.")
        int uvThreshold,

        boolean enabled
) {
}