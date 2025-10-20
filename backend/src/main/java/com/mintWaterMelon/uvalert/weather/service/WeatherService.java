package com.mintWaterMelon.uvalert.weather.service;

import com.mintWaterMelon.uvalert.weather.client.KmaShortForecastClient;
import com.mintWaterMelon.uvalert.weather.client.KmaUvIndexClient;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItemsResponse;
import com.mintWaterMelon.uvalert.weather.dto.WeatherHourlyIndexResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final KmaUvIndexClient kmaUvIndexClient;
    private final KmaShortForecastClient kmaShortForecastClient;

    public WeatherService(
            KmaUvIndexClient kmaUvIndexClient,
            KmaShortForecastClient kmaShortForecastClient
    ) {
        this.kmaUvIndexClient = kmaUvIndexClient;
        this.kmaShortForecastClient = kmaShortForecastClient;
    }

    public WeatherHourlyIndexResponse getUvIndex(String areaNo, String time) {
        return kmaUvIndexClient.getUvIndex(areaNo, time);
    }

    public WeatherApiItemsResponse getShortForecast(
            String baseDate,
            String baseTime,
            int nx,
            int ny
    ) {
        return kmaShortForecastClient.getShortForecast(baseDate, baseTime, nx, ny);
    }
}