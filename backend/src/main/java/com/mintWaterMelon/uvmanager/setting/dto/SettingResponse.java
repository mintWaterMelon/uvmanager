package com.mintWaterMelon.uvmanager.setting.dto;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "앱 설정 응답")
public record SettingResponse(
        @Schema(description = "기본 지역 코드", example = "1100000000")
        String defaultAreaNo,

        @Schema(description = "기본 지역명", example = "서울특별시")
        String defaultLocationName,

        @Schema(description = "기본 자외선 기준값", example = "6")
        int defaultUvThreshold,

        @Schema(description = "자외선 차단제 알림 활성화 여부", example = "true")
        boolean sunscreenAlertEnabled,

        @Schema(description = "기본 알림 시간", example = "08:00:00")
        LocalTime defaultAlertTime
) {
}
