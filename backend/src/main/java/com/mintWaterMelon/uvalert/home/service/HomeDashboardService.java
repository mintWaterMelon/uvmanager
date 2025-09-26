package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import com.mintWaterMelon.uvalert.home.dto.*;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItem;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItemsResponse;
import com.mintWaterMelon.uvalert.weather.dto.WeatherHourlyIndexResponse;
import com.mintWaterMelon.uvalert.weather.service.WeatherService;
import com.mintWaterMelon.uvalert.weather.util.WeatherTimeUtils;
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

        List<String> times = getTimeSlots(mode);
        String currentSlot = findCurrentSlot(currentTime, mode);

        String shortForecastBaseDate =
                WeatherTimeUtils.toShortForecastBaseDate(LocalDate.now());

        String shortForecastBaseTime =
                WeatherTimeUtils.toShortForecastBaseTime();

        String livingWeatherTime =
                WeatherTimeUtils.toLivingWeatherTime(selectedDate);

        WeatherApiItemsResponse shortForecast =
                weatherService.getShortForecast(
                        shortForecastBaseDate,
                        shortForecastBaseTime,
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
                times.stream()
                        .map(time -> new HomeTimeSlotResponse(
                                time,
                                time.equals(currentSlot)
                        ))
                        .toList(),
                List.of(
                        createWeatherRow(times, selectedDate, shortForecast),
                        createUvIndexRow(times, uvIndex),
                        createAirStagnationRow(times, airStagnation)
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

    private List<String> getTimeSlots(HomeMode mode) {
        return switch (mode) {
            case DAY -> List.of("06:00", "09:00", "12:00", "15:00", "18:00");
            case NIGHT -> List.of("18:00", "21:00", "00:00", "03:00", "06:00");
        };
    }

    private String findCurrentSlot(LocalDateTime currentTime, HomeMode mode) {
        int hour = currentTime.getHour();

        if (mode == HomeMode.DAY) {
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
            return "18:00";
        }

        if (hour >= 18 && hour < 21) {
            return "18:00";
        }
        if (hour >= 21) {
            return "21:00";
        }
        if (hour < 3) {
            return "00:00";
        }
        if (hour < 6) {
            return "03:00";
        }
        return "06:00";
    }

    private HomeTableRowResponse createWeatherRow(
            List<String> times,
            LocalDate selectedDate,
            WeatherApiItemsResponse shortForecast
    ) {
        return new HomeTableRowResponse(
                HomeTableRowType.WEATHER,
                "날씨 및 온도",
                times.stream()
                        .map(time -> createWeatherCell(
                                time,
                                selectedDate,
                                shortForecast.items()
                        ))
                        .toList()
        );
    }

    private HomeTableCellResponse createWeatherCell(
            String time,
            LocalDate selectedDate,
            List<WeatherApiItem> items
    ) {
        String fcstDate = WeatherTimeUtils.toFcstDate(selectedDate);
        String fcstTime = time.replace(":", "");

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
                time,
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
            List<String> times,
            WeatherHourlyIndexResponse uvIndex
    ) {
        return new HomeTableRowResponse(
                HomeTableRowType.UV_INDEX,
                "자외선 지수",
                times.stream()
                        .map(time -> createIndexCell(
                                time,
                                uvIndex.hourlyValues(),
                                "UV"
                        ))
                        .toList()
        );
    }

    private HomeTableRowResponse createAirStagnationRow(
            List<String> times,
            WeatherHourlyIndexResponse airStagnation
    ) {
        return new HomeTableRowResponse(
                HomeTableRowType.AIR_STAGNATION,
                "대기정체지수",
                times.stream()
                        .map(time -> createIndexCell(
                                time,
                                airStagnation.hourlyValues(),
                                "AIR"
                        ))
                        .toList()
        );
    }

    private HomeTableCellResponse createIndexCell(
            String time,
            Map<Integer, Integer> hourlyValues,
            String type
    ) {
        int hour = Integer.parseInt(time.substring(0, 2));
        Integer value = hourlyValues.get(hour);

        if (value == null) {
            return new HomeTableCellResponse(
                    time,
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
                time,
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