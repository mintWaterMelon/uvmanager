package com.mintWaterMelon.uvalert.setting.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record SettingRequest(
        @NotBlank(message = "기본 지역 코드는 필수입니다.")
        String defaultAreaNo,

        @Min(value = 0, message = "기본 UV 기준값은 0 이상이어야 합니다.")
        @Max(value = 11, message = "기본 UV 기준값은 11 이하여야 합니다.")
        int defaultUvThreshold,

        boolean sunscreenAlertEnabled,

        @NotNull(message = "기본 알림 시간은 필수입니다.")
        LocalTime defaultAlertTime
) {
}