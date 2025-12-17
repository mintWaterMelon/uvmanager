package com.mintWaterMelon.uvmanager.weather.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "자외선 지수 응답")
public record UvIndexResponse(
        @Schema(description = "지역 코드", example = "1100000000")
        String areaNo,

        @Schema(description = "조회 기준 시간", example = "2026060906")
        String baseTime,

        @Schema(description = "시간별 자외선 지수 값", example = "{\"0\":1,\"3\":1,\"6\":2,\"9\":5,\"12\":8,\"15\":7,\"18\":3,\"21\":1}")
        Map<Integer, Integer> hourlyValues
) {
}