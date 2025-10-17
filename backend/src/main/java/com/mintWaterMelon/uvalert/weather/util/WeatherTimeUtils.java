package com.mintWaterMelon.uvalert.weather.util;

import com.mintWaterMelon.uvalert.home.dto.HomeDateType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static String toFcstDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String toLivingWeatherTime(LocalDate selectedDate) {
        return selectedDate.atStartOfDay().format(LIVING_WEATHER_TIME_FORMATTER);
    }

    public static ShortForecastBaseTime calculateShortForecastBaseTime(
            LocalDateTime now
    ) {
        LocalDate baseDate = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        if (currentTime.isBefore(LocalTime.of(2, 10))) {
            return new ShortForecastBaseTime(
                    baseDate.minusDays(1),
                    "2300"
            );
        }

        if (currentTime.isBefore(LocalTime.of(5, 10))) {
            return new ShortForecastBaseTime(baseDate, "0200");
        }

        if (currentTime.isBefore(LocalTime.of(8, 10))) {
            return new ShortForecastBaseTime(baseDate, "0500");
        }

        if (currentTime.isBefore(LocalTime.of(11, 10))) {
            return new ShortForecastBaseTime(baseDate, "0800");
        }

        if (currentTime.isBefore(LocalTime.of(14, 10))) {
            return new ShortForecastBaseTime(baseDate, "1100");
        }

        if (currentTime.isBefore(LocalTime.of(17, 10))) {
            return new ShortForecastBaseTime(baseDate, "1400");
        }

        if (currentTime.isBefore(LocalTime.of(20, 10))) {
            return new ShortForecastBaseTime(baseDate, "1700");
        }

        if (currentTime.isBefore(LocalTime.of(23, 10))) {
            return new ShortForecastBaseTime(baseDate, "2000");
        }

        return new ShortForecastBaseTime(baseDate, "2300");
    }

    public static ShortForecastBaseTime calculateSafeFallbackBaseTime(
            LocalDate selectedDate,
            LocalDateTime now,
            ShortForecastBaseTime currentBaseTime
    ) {
        ShortForecastBaseTime candidate = new ShortForecastBaseTime(
                selectedDate.minusDays(1),
                "2300"
        );

        LocalDateTime candidateDateTime = candidate.date()
                .atTime(23, 10);

        if (candidateDateTime.isAfter(now)) {
            return currentBaseTime;
        }

        return candidate;
    }

    public record ShortForecastBaseTime(
            LocalDate date,
            String time
    ) {
        public String baseDate() {
            return date.format(DATE_FORMATTER);
        }

        public String baseTime() {
            return time;
        }
    }
}