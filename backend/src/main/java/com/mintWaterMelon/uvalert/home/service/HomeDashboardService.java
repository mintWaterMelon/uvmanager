package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import com.mintWaterMelon.uvalert.home.dto.*;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItem;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItemsResponse;
import com.mintWaterMelon.uvalert.weather.dto.WeatherHourlyIndexResponse;
import com.mintWaterMelon.uvalert.weather.service.WeatherService;
import com.mintWaterMelon.uvalert.weather.util.WeatherTimeUtils;
import com.mintWaterMelon.uvalert.weather.util.WeatherTimeUtils.ShortForecastBaseTime;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HomeDashboardService {

    private final AreaService areaService;
    private final WeatherService weatherService;
    private final HomeBackgroundService homeBackgroundService;
    private final HomeAdviceService homeAdviceService;

    public HomeDashboardService(
            AreaService areaService,
            WeatherService weatherService,
            HomeBackgroundService homeBackgroundService,
            HomeAdviceService homeAdviceService
    ) {
        this.areaService = areaService;
        this.weatherService = weatherService;
        this.homeBackgroundService = homeBackgroundService;
        this.homeAdviceService = homeAdviceService;
    }

    private String findRepresentativeWeather(HomeTableRowResponse weatherRow) {
        return weatherRow.cells()
                .stream()
                .filter(cell -> cell.mainText() != null)
                .filter(cell -> !cell.mainText().isBlank())
                .filter(cell -> !"-".equals(cell.mainText()))
                .findFirst()
                .map(HomeTableCellResponse::mainText)
                .orElse("맑음");
    }

    private Integer findRepresentativeTemperature(HomeTableRowResponse weatherRow) {
        return weatherRow.cells()
                .stream()
                .map(HomeTableCellResponse::value)
                .filter(value -> value != null)
                .findFirst()
                .orElse(null);
    }

    private boolean isNight(LocalDateTime currentTime) {
        int hour = currentTime.getHour();
        return hour >= 18 || hour < 6;
    }

    public HomeDashboardResponse getDashboard(
            String areaNo,
            HomeDateType dateType
    ) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDate selectedDate = WeatherTimeUtils.calculateSelectedDate(
                LocalDate.now(),
                dateType
        );

        AreaResponse area = areaService.getAreaByAreaNo(areaNo);

        validateGrid(area);

        List<HomeDashboardSlot> slots = getAllDaySlots(selectedDate);

        ShortForecastBaseTime shortForecastBaseTime =
                WeatherTimeUtils.calculateShortForecastBaseTime(currentTime);

        String livingWeatherTime =
                WeatherTimeUtils.toLivingWeatherTime(selectedDate);

        WeatherApiItemsResponse shortForecast =
                weatherService.getShortForecast(
                        shortForecastBaseTime.baseDate(),
                        shortForecastBaseTime.baseTime(),
                        area.gridX(),
                        area.gridY()
                );

        WeatherHourlyIndexResponse uvIndex =
                weatherService.getUvIndex(
                        area.areaNo(),
                        livingWeatherTime
                );

        WeatherHourlyIndexResponse airStagnation =
                weatherService.getAirStagnationIndex(
                        area.areaNo(),
                        livingWeatherTime
                );

        HomeTableRowResponse weatherRow = createWeatherRow(slots, shortForecast);
        HomeTableRowResponse uvIndexRow = createUvIndexRow(slots, uvIndex);
        HomeTableRowResponse airStagnationRow = createAirStagnationRow(slots, airStagnation);

        HomeTableResponse table = new HomeTableResponse(
                slots.stream()
                        .map(slot -> new HomeTimeSlotResponse(
                                slot.date(),
                                slot.time(),
                                isCurrentSlot(slot, currentTime)
                        ))
                        .toList(),
                List.of(
                        weatherRow,
                        uvIndexRow,
                        airStagnationRow
                )
        );

        int maxUv = uvIndex.hourlyValues()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        int maxAirStagnation = airStagnation.hourlyValues()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        String representativeWeather = findRepresentativeWeather(weatherRow);
        Integer representativeTemperature = findRepresentativeTemperature(weatherRow);

        HomeMode currentMode = isNight(currentTime) ? HomeMode.NIGHT : HomeMode.DAY;

        return new HomeDashboardResponse(
                currentTime,
                selectedDate,
                dateType,
                new HomeLocationResponse(
                        area.areaNo(),
                        area.displayName()
                ),
                homeBackgroundService.decideBackground(
                        new HomeBackgroundCondition(
                                currentMode,
                                representativeWeather,
                                maxUv,
                                maxAirStagnation,
                                representativeTemperature
                        )
                ),
                table,
                homeAdviceService.createAdvice(
                        new HomeAdviceCondition(
                                currentMode,
                                representativeWeather,
                                maxUv,
                                maxAirStagnation,
                                representativeTemperature
                        )
                )
        );
    }

    private void validateGrid(AreaResponse area) {
        if (area.gridX() <= 0 || area.gridY() <= 0) {
            throw new IllegalArgumentException(
                    "단기예보 격자 좌표를 찾을 수 없습니다. areaNo=" + area.areaNo()
            );
        }
    }

    private List<HomeDashboardSlot> getAllDaySlots(LocalDate selectedDate) {
        return List.of(
                new HomeDashboardSlot(selectedDate, "00:00"),
                new HomeDashboardSlot(selectedDate, "03:00"),
                new HomeDashboardSlot(selectedDate, "06:00"),
                new HomeDashboardSlot(selectedDate, "09:00"),
                new HomeDashboardSlot(selectedDate, "12:00"),
                new HomeDashboardSlot(selectedDate, "15:00"),
                new HomeDashboardSlot(selectedDate, "18:00"),
                new HomeDashboardSlot(selectedDate, "21:00")
        );
    }

    private boolean isCurrentSlot(
            HomeDashboardSlot slot,
            LocalDateTime currentTime
    ) {
        if (!slot.date().equals(currentTime.toLocalDate())) {
            return false;
        }

        return slot.time().equals(calculateCurrentSlotTime(currentTime));
    }

    private String calculateCurrentSlotTime(LocalDateTime currentTime) {
        int hour = currentTime.getHour();

        if (hour < 3) {
            return "00:00";
        }

        if (hour < 6) {
            return "03:00";
        }

        if (hour < 9) {
            return "06:00";
        }

        if (hour < 12) {
            return "09:00";
        }

        if (hour < 15) {
            return "12:00";
        }

        if (hour < 18) {
            return "15:00";
        }

        if (hour < 21) {
            return "18:00";
        }

        return "21:00";
    }

    private HomeTableRowResponse createWeatherRow(
            List<HomeDashboardSlot> slots,
            WeatherApiItemsResponse shortForecast
    ) {
        return new HomeTableRowResponse(
                HomeTableRowType.WEATHER,
                "날씨 및 온도",
                slots.stream()
                        .map(slot -> createWeatherCell(
                                slot,
                                shortForecast.items()
                        ))
                        .toList()
        );
    }

    private HomeTableCellResponse createWeatherCell(
            HomeDashboardSlot slot,
            List<WeatherApiItem> items
    ) {
        String fcstDate = WeatherTimeUtils.toFcstDate(slot.date());
        String fcstTime = slot.time().replace(":", "");

        Optional<String> temperature = findForecastValue(
                items,
                fcstDate,
                fcstTime,
                "TMP"
        );

        Optional<String> sky = findForecastValue(
                items,
                fcstDate,
                fcstTime,
                "SKY"
        );

        Optional<String> pty = findForecastValue(
                items,
                fcstDate,
                fcstTime,
                "PTY"
        );

        String weatherText = convertWeatherText(
                sky.orElse(""),
                pty.orElse("")
        );

        String temperatureText = temperature
                .map(value -> value + "℃")
                .orElse("-");

        Integer temperatureValue = temperature
                .map(this::parseIntegerSafely)
                .orElse(null);

        return new HomeTableCellResponse(
                slot.date(),
                slot.time(),
                weatherText,
                temperatureText,
                temperatureValue,
                weatherText
        );
    }

    private Optional<String> findForecastValue(
            List<WeatherApiItem> items,
            String fcstDate,
            String fcstTime,
            String category
    ) {
        return items.stream()
                .filter(item -> item.fcstDate().equals(fcstDate))
                .filter(item -> item.fcstTime().equals(fcstTime))
                .filter(item -> item.category().equals(category))
                .map(WeatherApiItem::fcstValue)
                .findFirst();
    }

    private String convertWeatherText(String sky, String pty) {
        if (!pty.isBlank() && !"0".equals(pty)) {
            return switch (pty) {
                case "1" -> "비";
                case "2" -> "비/눈";
                case "3" -> "눈";
                case "4" -> "소나기";
                default -> "강수";
            };
        }

        return switch (sky) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "-";
        };
    }

    private HomeTableRowResponse createUvIndexRow(
            List<HomeDashboardSlot> slots,
            WeatherHourlyIndexResponse uvIndex
    ) {
        return new HomeTableRowResponse(
                HomeTableRowType.UV_INDEX,
                "자외선 지수",
                slots.stream()
                        .map(slot -> createIndexCell(
                                slot,
                                uvIndex.hourlyValues(),
                                "UV"
                        ))
                        .toList()
        );
    }

    private HomeTableRowResponse createAirStagnationRow(
            List<HomeDashboardSlot> slots,
            WeatherHourlyIndexResponse airStagnation
    ) {
        return new HomeTableRowResponse(
                HomeTableRowType.AIR_STAGNATION,
                "대기정체지수",
                slots.stream()
                        .map(slot -> createIndexCell(
                                slot,
                                airStagnation.hourlyValues(),
                                "AIR"
                        ))
                        .toList()
        );
    }

    private HomeTableCellResponse createIndexCell(
            HomeDashboardSlot slot,
            Map<Integer, Integer> hourlyValues,
            String type
    ) {
        int hour = Integer.parseInt(slot.time().substring(0, 2));
        Integer value = hourlyValues.get(hour);

        if (value == null) {
            return new HomeTableCellResponse(
                    slot.date(),
                    slot.time(),
                    "-",
                    "정보 없음",
                    null,
                    "UNKNOWN"
            );
        }

        String level = "UV".equals(type)
                ? classifyUvLevel(value)
                : classifyAirStagnationLevel(value);

        String levelText = "UV".equals(type)
                ? convertUvLevelText(level)
                : convertAirStagnationLevelText(level);

        return new HomeTableCellResponse(
                slot.date(),
                slot.time(),
                String.valueOf(value),
                levelText,
                value,
                level
        );
    }

    private String classifyUvLevel(int value) {
        if (value <= 2) {
            return "LOW";
        }
        if (value <= 5) {
            return "MODERATE";
        }
        if (value <= 7) {
            return "HIGH";
        }
        if (value <= 10) {
            return "VERY_HIGH";
        }
        return "EXTREME";
    }

    private String convertUvLevelText(String level) {
        return switch (level) {
            case "LOW" -> "낮음";
            case "MODERATE" -> "보통";
            case "HIGH" -> "높음";
            case "VERY_HIGH" -> "매우 높음";
            case "EXTREME" -> "위험";
            default -> "정보 없음";
        };
    }

    private String classifyAirStagnationLevel(int value) {
        if (value <= 1) {
            return "LOW";
        }
        if (value <= 3) {
            return "MODERATE";
        }
        return "HIGH";
    }

    private String convertAirStagnationLevelText(String level) {
        return switch (level) {
            case "LOW" -> "낮음";
            case "MODERATE" -> "보통";
            case "HIGH" -> "높음";
            default -> "정보 없음";
        };
    }

    private Integer parseIntegerSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}