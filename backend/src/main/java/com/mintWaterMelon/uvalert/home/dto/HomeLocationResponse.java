package com.mintWaterMelon.uvalert.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 지역 정보")
public record HomeLocationResponse(
        @Schema(description = "지역 코드", example = "1100000000")
        String areaNo,

        @Schema(description = "지역명", example = "서울특별시")
        String name
) {
}