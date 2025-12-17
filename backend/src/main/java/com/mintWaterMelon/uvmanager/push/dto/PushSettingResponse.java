package com.mintWaterMelon.uvmanager.push.dto;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "푸시 설정 응답")
public record PushSettingResponse(
        @Schema(description = "자외선 알림 활성화 여부", example = "true")
        boolean uvAlertEnabled,

        @Schema(description = "자외선 알림 기준값", example = "8")
        int uvAlertThreshold,

        @Schema(description = "미세먼지 알림 활성화 여부", example = "false")
        boolean dustAlertEnabled,

        @Schema(description = "알림 시간", example = "09:00:00")
        LocalTime alertTime
) {
}