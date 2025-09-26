package com.mintWaterMelon.uvalert.weather.util;

import com.mintWaterMelon.uvalert.home.dto.HomeDateType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WeatherTimeUtils {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final DateTimeFormatter LIVING_WEATHER_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHH");

    private WeatherTimeUtils() {
    }

    public static LocalDate calculateSelectedDate(
            LocalDate today,
            HomeDateType dateType
    ) {
        return switch (dateType) {
            case TODAY -> today;
            case TOMORROW -> today.plusDays(1);
            case DAY_AFTER_TOMORROW -> today.plusDays(2);
        };
    }

    public static String toShortForecastBaseDate(LocalDate today) {
        return today.format(DATE_FORMATTER);
    }

    public static String toShortForecastBaseTime() {
        return "0200";
    }

    public static String toLivingWeatherTime(LocalDate selectedDate) {
        return selectedDate.atStartOfDay().format(LIVING_WEATHER_TIME_FORMATTER);
    }

    public static String toFcstDate(LocalDate selectedDate) {
        return selectedDate.format(DATE_FORMATTER);
    }
}
