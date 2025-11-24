package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundCondition;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundTheme;
import com.mintWaterMelon.uvalert.home.dto.HomeMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeBackgroundServiceTest {

    private final HomeBackgroundService homeBackgroundService = new HomeBackgroundService();

    @Test
    @DisplayName("밤이면 NIGHT 배경을 반환한다")
    void decideNightBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.NIGHT, "맑음", 10, 0, 20)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.NIGHT);
        assertThat(response.color()).isEqualTo("#111827");
    }

    @Test
    @DisplayName("비가 오면 RAINY 배경을 반환한다")
    void decideRainyBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "비", 3, 0, 24)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.RAINY);
        assertThat(response.color()).isEqualTo("#CBD5E1");
    }

    @Test
    @DisplayName("눈이 오면 SNOWY 배경을 반환한다")
    void decideSnowyBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "눈", 3, 0, 0)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.SNOWY);
        assertThat(response.color()).isEqualTo("#E0F2FE");
    }

    @Test
    @DisplayName("낮에 자외선지수가 11 이상이면 DAY_EXTREME_UV 배경을 반환한다")
    void decideExtremeUvBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "맑음", 11, 0, 30)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.DAY_EXTREME_UV);
        assertThat(response.color()).isEqualTo("#FDBA74");
    }

    @Test
    @DisplayName("낮에 자외선지수가 8 이상이면 DAY_VERY_HIGH_UV 배경을 반환한다")
    void decideVeryHighUvBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "맑음", 8, 0, 28)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.DAY_VERY_HIGH_UV);
        assertThat(response.color()).isEqualTo("#FFE7A3");
    }

    @Test
    @DisplayName("강수확률이 60 이상이면 RAINY 배경을 반환한다")
    void decideRainyBackgroundByPrecipitationProbability() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "맑음", 3, 60, 24)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.RAINY);
    }

    @Test
    @DisplayName("흐림이면 CLOUDY 배경을 반환한다")
    void decideCloudyBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "흐림", 3, 0, 24)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.CLOUDY);
        assertThat(response.color()).isEqualTo("#E2E8F0");
    }

    @Test
    @DisplayName("특별한 조건이 없으면 DAY_NORMAL 배경을 반환한다")
    void decideNormalDayBackground() {
        HomeBackgroundResponse response = homeBackgroundService.decideBackground(
                new HomeBackgroundCondition(HomeMode.DAY, "맑음", 2, 0, 22)
        );

        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.DAY_NORMAL);
        assertThat(response.color()).isEqualTo("#E0F2FE");
    }
}
