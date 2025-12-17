package com.mintWaterMelon.uvmanager.weather.service;

import com.mintWaterMelon.uvmanager.weather.client.ShortForecastClient;
import com.mintWaterMelon.uvmanager.weather.client.UvIndexClient;
import com.mintWaterMelon.uvmanager.weather.dto.ShortForecastResponse;
import com.mintWaterMelon.uvmanager.weather.dto.UvIndexResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WeatherService {

    private final UvIndexClient uvIndexClient;
    private final ShortForecastClient shortForecastClient;
    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(
            UvIndexClient uvIndexClient,
            ShortForecastClient shortForecastClient
    ) {
        this.uvIndexClient = uvIndexClient;
        this.shortForecastClient = shortForecastClient;
    }

    @Cacheable(
            value = "uvIndex",
            key = "#areaNo + ':' + #time"
    )
    public UvIndexResponse getUvIndex(String areaNo, String time) {
        log.info("Call KMA UV API. areaNo={}, time={}", areaNo, time);
        return uvIndexClient.getUvIndex(areaNo, time);
    }

    @Cacheable(
            value = "shortForecast",
            key = "#baseDate + ':' + #baseTime + ':' + #nx + ':' + #ny"
    )
    public ShortForecastResponse getShortForecast(
            String baseDate,
            String baseTime,
            int nx,
            int ny
    ) {
        log.info(
                "Call KMA Short Forecast API. baseDate={}, baseTime={}, nx={}, ny={}",
                baseDate,
                baseTime,
                nx,
                ny
        );
        return shortForecastClient.getShortForecast(baseDate, baseTime, nx, ny);
    }
}