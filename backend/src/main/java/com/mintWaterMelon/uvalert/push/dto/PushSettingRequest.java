package com.mintWaterMelon.uvalert.push.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "푸시 설정 수정 요청")
public record PushSettingRequest(
        @Schema(description = "자외선 알림 활성화 여부", example = "true")
        boolean uvAlertEnabled,

        @Schema(description = "자외선 알림 기준값", example = "8")
        @Min(value = 0, message = "자외선 알림 기준값은 0 이상이어야 합니다.")
        @Max(value = 11, message = "자외선 알림 기준값은 11 이하여야 합니다.")
        int uvAlertThreshold,

        @Schema(description = "미세먼지 알림 활성화 여부", example = "false")
        boolean dustAlertEnabled,

        @Schema(description = "알림 시간", example = "09:00:00")
        @NotNull(message = "알림 시간은 필수입니다.")
        LocalTime alertTime
) {
}