package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import com.mintWaterMelon.uvalert.home.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HomeDashboardService {

    private final AreaService areaService;

    public HomeDashboardService(AreaService areaService) {
        this.areaService = areaService;
    }

    public HomeDashboardResponse getDashboard(
            String areaNo,
            HomeDateType dateType,
            HomeMode mode
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDate selectedDate = calculateSelectedDate(LocalDate.now(), dateType);

        AreaResponse area = areaService.getAreaByAreaNo(areaNo);

        List<String> times = getTimeSlots(mode);
        String currentSlot = findCurrentSlot(currentTime, mode);

        HomeTableResponse table = new HomeTableResponse(
                times.stream()
                        .map(time -> new HomeTimeSlotResponse(
                                time,
                                time.equals(currentSlot)
                        ))
                        .toList(),
                List.of(
                        createWeatherRow(times),
                        createUvIndexRow(times),
                        createAirStagnationRow(times)
                )
        );

        return new HomeDashboardResponse(
                currentTime,
                selectedDate,
                dateType,
                mode,
                new HomeLocationResponse(
                        area.areaNo(),
                        area.displayName()
                ),
                new HomeBackgroundResponse(
                        "DAY_HIGH_UV",
                        "#FFE7A3",
                        "낮 시간대이며 자외선이 강한 상태"
                ),
                table,
                new HomeAdviceResponse(
                        "오늘은 자외선이 강해요",
                        "외출 전 선크림을 바르고, 2~3시간마다 덧바르는 것을 추천합니다.",
                        "WARNING"
                )
        );
    }

    private LocalDate calculateSelectedDate(LocalDate today, HomeDateType dateType) {
        return switch (dateType) {
            case TODAY -> today;
            case TOMORROW -> today.plusDays(1);
            case DAY_AFTER_TOMORROW -> today.plusDays(2);
        };
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

    private HomeTableRowResponse createWeatherRow(List<String> times) {
        return new HomeTableRowResponse(
                HomeTableRowType.WEATHER,
                "날씨 및 온도",
                times.stream()
                        .map(time -> new HomeTableCellResponse(
                                time,
                                "맑음",
                                "24℃",
                                24,
                                "SUNNY"
                        ))
                        .toList()
        );
    }

    private HomeTableRowResponse createUvIndexRow(List<String> times) {
        return new HomeTableRowResponse(
                HomeTableRowType.UV_INDEX,
                "자외선 지수",
                times.stream()
                        .map(time -> new HomeTableCellResponse(
                                time,
                                "8",
                                "매우 높음",
                                8,
                                "VERY_HIGH"
                        ))
                        .toList()
        );
    }

    private HomeTableRowResponse createAirStagnationRow(List<String> times) {
        return new HomeTableRowResponse(
                HomeTableRowType.AIR_STAGNATION,
                "대기정체지수",
                times.stream()
                        .map(time -> new HomeTableCellResponse(
                                time,
                                "보통",
                                "환기 주의",
                                3,
                                "MODERATE"
                        ))
                        .toList()
        );
    }
}