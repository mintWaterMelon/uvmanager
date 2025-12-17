package com.mintWaterMelon.uvmanager.home.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면 테이블 행")
public record HomeTableRowResponse(
        @Schema(description = "행 타입", example = "UV_INDEX")
        HomeTableRowType type,

        @Schema(description = "행 라벨", example = "자외선 지수")
        String label,

        @Schema(description = "셀 목록")
        List<HomeTableCellResponse> cells
) {
}