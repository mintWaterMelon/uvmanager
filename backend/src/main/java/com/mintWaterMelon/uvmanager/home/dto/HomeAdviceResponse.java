package com.mintWaterMelon.uvmanager.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 안내 메시지")
public record HomeAdviceResponse(
        @Schema(description = "안내 제목", example = "자외선 차단을 준비하세요")
        String title,

        @Schema(description = "안내 상세 메시지", example = "외출 전 자외선 차단제를 바르는 것이 좋아요.")
        String message,

        @Schema(description = "안내 심각도", example = "INFO")
        HomeAdviceSeverity severity
) {
}