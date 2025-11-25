package com.mintWaterMelon.uvalert.setting.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "앱 설정 수정 요청")
public record SettingRequest(
        @Schema(description = "기본 지역 코드", example = "1100000000")
        @NotBlank(message = "기본 지역 코드는 필수입니다.")
        String defaultAreaNo,

        @Schema(description = "기본 자외선 기준값", example = "6")
        @Min(value = 0, message = "기본 UV 기준값은 0 이상이어야 합니다.")
        @Max(value = 11, message = "기본 UV 기준값은 11 이하여야 합니다.")
        int defaultUvThreshold,

        @Schema(description = "자외선 차단제 알림 활성화 여부", example = "true")
        boolean sunscreenAlertEnabled,

        @Schema(description = "기본 알림 시간", example = "08:00:00")
        @NotNull(message = "기본 알림 시간은 필수입니다.")
        LocalTime defaultAlertTime
) {
}