package com.mintWaterMelon.uvalert.weather.controller;

import com.mintWaterMelon.uvalert.weather.dto.ShortForecastResponse;
import com.mintWaterMelon.uvalert.weather.dto.UvIndexResponse;
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
    public UvIndexResponse getUvIndex(
            @RequestParam String areaNo,
            @RequestParam String time
    ) {
        return weatherService.getUvIndex(areaNo, time);
    }

    @GetMapping("/api/weather/short-forecast")
    public ShortForecastResponse getShortForecast(
            @RequestParam String baseDate,
            @RequestParam String baseTime,
            @RequestParam int nx,
            @RequestParam int ny
    ) {
        return weatherService.getShortForecast(baseDate, baseTime, nx, ny);
    }
}