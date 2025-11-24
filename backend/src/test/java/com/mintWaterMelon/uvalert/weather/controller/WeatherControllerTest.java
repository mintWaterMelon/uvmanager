package com.mintWaterMelon.uvalert.weather.controller;

import com.mintWaterMelon.uvalert.weather.dto.ShortForecastItem;
import com.mintWaterMelon.uvalert.weather.dto.ShortForecastResponse;
import com.mintWaterMelon.uvalert.weather.dto.UvIndexResponse;
import com.mintWaterMelon.uvalert.weather.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    @DisplayName("자외선지수를 조회한다")
    void getUvIndex() throws Exception {
        UvIndexResponse response = new UvIndexResponse(
                "1100000000",
                "2026060909",
                Map.of(9, 3, 12, 7)
        );

        BDDMockito.given(weatherService.getUvIndex(eq("1100000000"), eq("2026060909")))
                .willReturn(response);

        mockMvc.perform(get("/api/weather/uv-index")
                        .param("areaNo", "1100000000")
                        .param("time", "2026060909"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.areaNo").value("1100000000"))
                .andExpect(jsonPath("$.baseTime").value("2026060909"))
                .andExpect(jsonPath("$.hourlyValues['9']").value(3))
                .andExpect(jsonPath("$.hourlyValues['12']").value(7));
    }

    @Test
    @DisplayName("단기예보를 조회한다")
    void getShortForecast() throws Exception {
        ShortForecastResponse response = new ShortForecastResponse(List.of(
                new ShortForecastItem("TMP", "20260609", "1200", "25"),
                new ShortForecastItem("SKY", "20260609", "1200", "1")
        ));

        BDDMockito.given(weatherService.getShortForecast(
                        eq("20260609"),
                        eq("0800"),
                        eq(60),
                        eq(127)
                ))
                .willReturn(response);

        mockMvc.perform(get("/api/weather/short-forecast")
                        .param("baseDate", "20260609")
                        .param("baseTime", "0800")
                        .param("nx", "60")
                        .param("ny", "127"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].category").value("TMP"))
                .andExpect(jsonPath("$.items[0].fcstDate").value("20260609"))
                .andExpect(jsonPath("$.items[0].fcstTime").value("1200"))
                .andExpect(jsonPath("$.items[0].fcstValue").value("25"));
    }
}
