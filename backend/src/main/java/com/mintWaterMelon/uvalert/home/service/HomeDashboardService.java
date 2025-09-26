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

    public HomeDashboardService(
            AreaService areaService,
            WeatherService weatherService
    ) {
        this.areaService = areaService;
        this.weatherService = weatherService;
    }

    public HomeDashboardResponse getDashboard(
            String areaNo,
            HomeDateType dateType,
            HomeMode mode
    ) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDate selectedDate = WeatherTimeUtils.calculateSelectedDate(
                LocalDate.now(),
                dateType
        );

        AreaResponse area = areaService.getAreaByAreaNo(areaNo);

        validateGrid(area);

        List<HomeDashboardSlot> slots = getSlots(selectedDate, mode);

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

        HomeTableResponse table = new HomeTableResponse(
                slots.stream()
                        .map(slot -> new HomeTimeSlotResponse(
                                slot.date(),
                                slot.time(),
                                isCurrentSlot(slot, currentTime)
                        ))
                        .toList(),
                List.of(
                        createWeatherRow(slots, shortForecast),
                        createUvIndexRow(slots, uvIndex),
                        createAirStagnationRow(slots, airStagnation)
                )
        );

        int maxUv = uvIndex.hourlyValues()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        return new HomeDashboardResponse(
                currentTime,
                selectedDate,
                dateType,
                mode,
                new HomeLocationResponse(
                        area.areaNo(),
                        area.displayName()
                ),
                createBackground(mode, maxUv),
                table,
                createAdvice(maxUv)
        );
    }

    private void validateGrid(AreaResponse area) {
        if (area.gridX() <= 0 || area.gridY() <= 0) {
            throw new IllegalArgumentException(
                    "단기예보 격자 좌표를 찾을 수 없습니다. areaNo=" + area.areaNo()
            );
        }
    }

    private List<HomeDashboardSlot> getSlots(
            LocalDate selectedDate,
            HomeMode mode
    ) {
        return switch (mode) {
            case DAY -> List.of(
                    new HomeDashboardSlot(selectedDate, "06:00"),
                    new HomeDashboardSlot(selectedDate, "09:00"),
                    new HomeDashboardSlot(selectedDate, "12:00"),
                    new HomeDashboardSlot(selectedDate, "15:00"),
                    new HomeDashboardSlot(selectedDate, "18:00")
            );
            case NIGHT -> List.of(
                    new HomeDashboardSlot(selectedDate, "18:00"),
                    new HomeDashboardSlot(selectedDate, "21:00"),
                    new HomeDashboardSlot(selectedDate.plusDays(1), "00:00"),
                    new HomeDashboardSlot(selectedDate.plusDays(1), "03:00"),
                    new HomeDashboardSlot(selectedDate.plusDays(1), "06:00")
            );
        };
    }

    private boolean isCurrentSlot(
            HomeDashboardSlot slot,
            LocalDateTime currentTime
    ) {
        if (!slot.date().equals(currentTime.toLocalDate())) {
            return false;
        }

        String currentSlotTime = calculateCurrentSlotTime(currentTime);

        return slot.time().equals(currentSlotTime);
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

    private HomeBackgroundResponse createBackground(HomeMode mode, int maxUv) {
        if (mode == HomeMode.NIGHT) {
            return new HomeBackgroundResponse(
                    "NIGHT",
                    "#111827",
                    "밤 시간대 배경"
            );
        }

        if (maxUv >= 8) {
            return new HomeBackgroundResponse(
                    "DAY_HIGH_UV",
                    "#FFE7A3",
                    "낮 시간대이며 자외선이 강한 상태"
            );
        }

        return new HomeBackgroundResponse(
                "DAY_NORMAL",
                "#E0F2FE",
                "낮 시간대 일반 배경"
        );
    }

    private HomeAdviceResponse createAdvice(int maxUv) {
        if (maxUv >= 11) {
            return new HomeAdviceResponse(
                    "자외선이 매우 위험해요",
                    "가능하면 실내에 머무르고, 외출 시 긴 옷과 모자, 선글라스를 착용하세요.",
                    "DANGER"
            );
        }

        if (maxUv >= 8) {
            return new HomeAdviceResponse(
                    "오늘은 자외선이 강해요",
                    "외출 전 선크림을 바르고, 2~3시간마다 덧바르는 것을 추천합니다.",
                    "WARNING"
            );
        }

        if (maxUv >= 3) {
            return new HomeAdviceResponse(
                    "자외선 차단을 준비하세요",
                    "야외 활동 시간이 길다면 선크림을 바르는 것이 좋습니다.",
                    "INFO"
            );
        }

        return new HomeAdviceResponse(
                "자외선이 낮은 편이에요",
                "일상적인 외출은 큰 부담이 적지만, 장시간 외출 시에는 기본적인 보호를 권장합니다.",
                "NORMAL"
        );
    }

    private Integer parseIntegerSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}