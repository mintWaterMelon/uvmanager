package com.mintWaterMelon.uvalert.weather.service;

import com.mintWaterMelon.uvalert.weather.client.ShortForecastClient;
import com.mintWaterMelon.uvalert.weather.client.UvIndexClient;
import com.mintWaterMelon.uvalert.weather.dto.ShortForecastResponse;
import com.mintWaterMelon.uvalert.weather.dto.UvIndexResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final UvIndexClient uvIndexClient;
    private final ShortForecastClient shortForecastClient;

    public WeatherService(
            UvIndexClient uvIndexClient,
            ShortForecastClient shortForecastClient
    ) {
        this.uvIndexClient = uvIndexClient;
        this.shortForecastClient = shortForecastClient;
    }

    public UvIndexResponse getUvIndex(String areaNo, String time) {
        return uvIndexClient.getUvIndex(areaNo, time);
    }

    public ShortForecastResponse getShortForecast(
            String baseDate,
            String baseTime,
            int nx,
            int ny
    ) {
        return shortForecastClient.getShortForecast(baseDate, baseTime, nx, ny);
    }
}