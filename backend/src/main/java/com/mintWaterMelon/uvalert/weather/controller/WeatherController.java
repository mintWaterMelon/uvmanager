package com.mintWaterMelon.uvalert.weather.controller;

import com.mintWaterMelon.uvalert.weather.dto.ShortForecastResponse;
import com.mintWaterMelon.uvalert.weather.dto.UvIndexResponse;
import com.mintWaterMelon.uvalert.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "날씨 API", description = "기상청 자외선지수 및 단기예보 조회 API")
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(
            summary = "자외선 지수 조회",
            description = "기상청 생활기상지수 API를 통해 지역 코드와 기준 시간에 해당하는 자외선 지수를 조회합니다."
    )
    @GetMapping("/api/weather/uv-index")
    public UvIndexResponse getUvIndex(
            @Parameter(description = "지역 코드", example = "1100000000")
            @RequestParam String areaNo,

            @Parameter(description = "조회 기준 시간", example = "2026060906")
            @RequestParam String time
    ) {
        return weatherService.getUvIndex(areaNo, time);
    }

    @Operation(
            summary = "단기예보 조회",
            description = "기상청 단기예보 API를 통해 기준 날짜, 기준 시간, 격자 좌표에 해당하는 예보 정보를 조회합니다."
    )
    @GetMapping("/api/weather/short-forecast")
    public ShortForecastResponse getShortForecast(
            @Parameter(description = "기준 날짜", example = "20260609")
            @RequestParam String baseDate,

            @Parameter(description = "기준 시간", example = "0500")
            @RequestParam String baseTime,

            @Parameter(description = "기상청 격자 X 좌표", example = "60")
            @RequestParam int nx,

            @Parameter(description = "기상청 격자 Y 좌표", example = "127")
            @RequestParam int ny
    ) {
        return weatherService.getShortForecast(baseDate, baseTime, nx, ny);
    }
}