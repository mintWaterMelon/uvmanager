package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundCondition;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundTheme;
import com.mintWaterMelon.uvalert.home.dto.HomeMode;
import org.springframework.stereotype.Service;

@Service
public class HomeBackgroundService {

    public HomeBackgroundResponse decideBackground(HomeBackgroundCondition condition) {
        if (condition.mode() == HomeMode.NIGHT) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.NIGHT,
                    "#111827",
                    "밤 시간대입니다. 어두운 배경을 사용합니다."
            );
        }

        if (isRainy(condition.representativeWeather())) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.RAINY,
                    "#CBD5E1",
                    "비가 오는 날씨입니다."
            );
        }

        if (isSnowy(condition.representativeWeather())) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.SNOWY,
                    "#E0F2FE",
                    "눈이 오는 날씨입니다."
            );
        }

        if (condition.maxUv() >= 11) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.DAY_EXTREME_UV,
                    "#FDBA74",
                    "자외선이 위험 수준인 낮 시간대입니다."
            );
        }

        if (condition.maxUv() >= 8) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.DAY_VERY_HIGH_UV,
                    "#FFE7A3",
                    "자외선이 매우 강한 낮 시간대입니다."
            );
        }

        if (condition.maxAirStagnation() >= 4) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.AIR_STAGNATION_HIGH,
                    "#E5E7EB",
                    "대기정체지수가 높아 공기 흐름이 좋지 않을 수 있습니다."
            );
        }

        if (isCloudy(condition.representativeWeather())) {
            return new HomeBackgroundResponse(
                    HomeBackgroundTheme.CLOUDY,
                    "#E2E8F0",
                    "구름이 많은 낮 시간대입니다."
            );
        }

        return new HomeBackgroundResponse(
                HomeBackgroundTheme.DAY_NORMAL,
                "#E0F2FE",
                "일반적인 낮 시간대 배경입니다."
        );
    }

    private boolean isRainy(String weather) {
        return "비".equals(weather)
                || "비/눈".equals(weather)
                || "소나기".equals(weather)
                || "강수".equals(weather);
    }

    private boolean isSnowy(String weather) {
        return "눈".equals(weather);
    }

    private boolean isCloudy(String weather) {
        return "흐림".equals(weather)
                || "구름많음".equals(weather);
    }
}