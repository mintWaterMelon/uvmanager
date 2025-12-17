package com.mintWaterMelon.uvmanager.weather.service;

import com.mintWaterMelon.uvmanager.weather.client.ShortForecastClient;
import com.mintWaterMelon.uvmanager.weather.client.UvIndexClient;
import com.mintWaterMelon.uvmanager.weather.dto.ShortForecastItem;
import com.mintWaterMelon.uvmanager.weather.dto.ShortForecastResponse;
import com.mintWaterMelon.uvmanager.weather.dto.UvIndexResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class WeatherServiceTest {

    private final UvIndexClient uvIndexClient = Mockito.mock(UvIndexClient.class);
    private final ShortForecastClient shortForecastClient = Mockito.mock(ShortForecastClient.class);
    private final WeatherService weatherService = new WeatherService(uvIndexClient, shortForecastClient);

    @Test
    @DisplayName("자외선지수 조회를 UvIndexClient에 위임한다")
    void getUvIndex() {
        UvIndexResponse expected = new UvIndexResponse(
                "1100000000",
                "2026060909",
                Map.of(9, 3, 12, 7)
        );
        given(uvIndexClient.getUvIndex("1100000000", "2026060909"))
                .willReturn(expected);

        UvIndexResponse response = weatherService.getUvIndex("1100000000", "2026060909");

        assertThat(response).isEqualTo(expected);
        verify(uvIndexClient).getUvIndex("1100000000", "2026060909");
    }

    @Test
    @DisplayName("단기예보 조회를 ShortForecastClient에 위임한다")
    void getShortForecast() {
        ShortForecastResponse expected = new ShortForecastResponse(List.of(
                new ShortForecastItem("TMP", "20260609", "1200", "25")
        ));
        given(shortForecastClient.getShortForecast("20260609", "0800", 60, 127))
                .willReturn(expected);

        ShortForecastResponse response = weatherService.getShortForecast("20260609", "0800", 60, 127);

        assertThat(response).isEqualTo(expected);
        verify(shortForecastClient).getShortForecast("20260609", "0800", 60, 127);
    }
}
