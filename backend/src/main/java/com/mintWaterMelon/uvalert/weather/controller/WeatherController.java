package com.mintWaterMelon.uvalert.weather.controller;

import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItemsResponse;
import com.mintWaterMelon.uvalert.weather.dto.WeatherHourlyIndexResponse;
import com.mintWaterMelon.uvalert.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather/uv-index")
    public WeatherHourlyIndexResponse getUvIndex(
            @RequestParam String areaNo,
            @RequestParam String time
    ) {
        return weatherService.getUvIndex(areaNo, time);
    }

    @GetMapping("/api/weather/air-stagnation")
    public WeatherHourlyIndexResponse getAirStagnationIndex(
            @RequestParam String areaNo,
            @RequestParam String time
    ) {
        return weatherService.getAirStagnationIndex(areaNo, time);
    }

    @GetMapping("/api/weather/short-forecast")
    public WeatherApiItemsResponse getShortForecast(
            @RequestParam String baseDate,
            @RequestParam String baseTime,
            @RequestParam int nx,
            @RequestParam int ny
    ) {
        return weatherService.getShortForecast(baseDate, baseTime, nx, ny);
    }
}