package com.mintWaterMelon.uvalert.area.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지역 응답")
public record AreaResponse(
        @Schema(description = "지역 코드", example = "1100000000")
        String areaNo,

        @Schema(description = "시/도", example = "서울특별시")
        String level1,

        @Schema(description = "시/군/구", example = "강남구")
        String level2,

        @Schema(description = "읍/면/동", example = "역삼동")
        String level3,

        @Schema(description = "화면 표시용 지역명", example = "서울특별시 강남구")
        String displayName,

        @Schema(description = "기상청 격자 X 좌표", example = "61")
        int gridX,

        @Schema(description = "기상청 격자 Y 좌표", example = "126")
        int gridY
) {
}