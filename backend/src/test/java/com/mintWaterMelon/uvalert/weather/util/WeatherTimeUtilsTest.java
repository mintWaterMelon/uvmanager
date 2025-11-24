package com.mintWaterMelon.uvalert.weather.util;

import com.mintWaterMelon.uvalert.home.dto.HomeDateType;
import com.mintWaterMelon.uvalert.weather.util.WeatherTimeUtils.ShortForecastBaseTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherTimeUtilsTest {

    @Test
    @DisplayName("선택 날짜를 계산한다")
    void calculateSelectedDate() {
        LocalDate today = LocalDate.of(2026, 6, 9);

        assertThat(WeatherTimeUtils.calculateSelectedDate(today, HomeDateType.TODAY))
                .isEqualTo(LocalDate.of(2026, 6, 9));
        assertThat(WeatherTimeUtils.calculateSelectedDate(today, HomeDateType.TOMORROW))
                .isEqualTo(LocalDate.of(2026, 6, 10));
        assertThat(WeatherTimeUtils.calculateSelectedDate(today, HomeDateType.DAY_AFTER_TOMORROW))
                .isEqualTo(LocalDate.of(2026, 6, 11));
    }

    @Test
    @DisplayName("단기예보 날짜 형식으로 변환한다")
    void toFcstDate() {
        assertThat(WeatherTimeUtils.toFcstDate(LocalDate.of(2026, 6, 9)))
                .isEqualTo("20260609");
    }

    @Test
    @DisplayName("생활기상 시간은 선택 날짜 00시 기준으로 변환한다")
    void toLivingWeatherTime() {
        assertThat(WeatherTimeUtils.toLivingWeatherTime(LocalDate.of(2026, 6, 10)))
                .isEqualTo("2026061000");
    }

    @Test
    @DisplayName("자외선지수 요청 시간은 현재 시간을 3시간 단위로 내림한다")
    void calculateUvIndexTime() {
        assertThat(WeatherTimeUtils.calculateUvIndexTime(LocalDateTime.of(2026, 6, 9, 10, 30)))
                .isEqualTo("2026060909");
        assertThat(WeatherTimeUtils.calculateUvIndexTime(LocalDateTime.of(2026, 6, 9, 0, 5)))
                .isEqualTo("2026060900");
        assertThat(WeatherTimeUtils.calculateUvIndexTime(LocalDateTime.of(2026, 6, 9, 23, 59)))
                .isEqualTo("2026060921");
    }

    @Test
    @DisplayName("자외선지수 fallback 시간은 오늘 00시이다")
    void calculateUvIndexFallbackTime() {
        assertThat(WeatherTimeUtils.calculateUvIndexFallbackTime(LocalDate.of(2026, 6, 9)))
                .isEqualTo("2026060900");
    }

    @Test
    @DisplayName("02시 10분 전에는 전날 23시 단기예보를 사용한다")
    void calculateShortForecastTimeBefore0210() {
        ShortForecastBaseTime result = WeatherTimeUtils.calculateShortForecastTime(
                LocalDateTime.of(2026, 6, 9, 2, 9)
        );

        assertThat(result.date()).isEqualTo(LocalDate.of(2026, 6, 8));
        assertThat(result.baseTime()).isEqualTo("2300");
    }

    @Test
    @DisplayName("각 발표 가능 시간에 맞는 단기예보 기준 시간을 계산한다")
    void calculateShortForecastTime() {
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 2, 10), "20260609", "0200");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 5, 10), "20260609", "0500");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 8, 10), "20260609", "0800");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 11, 10), "20260609", "1100");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 14, 10), "20260609", "1400");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 17, 10), "20260609", "1700");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 20, 10), "20260609", "2000");
        assertBaseTime(LocalDateTime.of(2026, 6, 9, 23, 10), "20260609", "2300");
    }

    @Test
    @DisplayName("선택 날짜 전날 23시 10분이 현재보다 미래이면 현재 기준 시간을 fallback으로 사용한다")
    void calculateShortForecastFallbackTimeUsesCurrentBaseWhenCandidateIsFuture() {
        ShortForecastBaseTime currentBaseTime = new ShortForecastBaseTime(LocalDate.of(2026, 6, 9), "0800");

        ShortForecastBaseTime result = WeatherTimeUtils.calculateShortForecastFallbackTime(
                LocalDate.of(2026, 6, 11),
                LocalDateTime.of(2026, 6, 9, 10, 0),
                currentBaseTime
        );

        assertThat(result).isEqualTo(currentBaseTime);
    }

    @Test
    @DisplayName("선택 날짜 전날 23시 10분이 현재보다 과거이면 전날 23시를 fallback으로 사용한다")
    void calculateShortForecastFallbackTimeUsesPreviousDay2300WhenAvailable() {
        ShortForecastBaseTime currentBaseTime = new ShortForecastBaseTime(LocalDate.of(2026, 6, 10), "0800");

        ShortForecastBaseTime result = WeatherTimeUtils.calculateShortForecastFallbackTime(
                LocalDate.of(2026, 6, 10),
                LocalDateTime.of(2026, 6, 10, 10, 0),
                currentBaseTime
        );

        assertThat(result.date()).isEqualTo(LocalDate.of(2026, 6, 9));
        assertThat(result.baseTime()).isEqualTo("2300");
    }

    private void assertBaseTime(LocalDateTime now, String expectedBaseDate, String expectedBaseTime) {
        ShortForecastBaseTime result = WeatherTimeUtils.calculateShortForecastTime(now);

        assertThat(result.baseDate()).isEqualTo(expectedBaseDate);
        assertThat(result.baseTime()).isEqualTo(expectedBaseTime);
    }
}
