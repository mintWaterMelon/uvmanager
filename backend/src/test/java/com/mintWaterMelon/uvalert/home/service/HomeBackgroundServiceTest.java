package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundCondition;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundTheme;
import com.mintWaterMelon.uvalert.home.dto.HomeMode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeBackgroundServiceTest {

    private final HomeBackgroundService homeBackgroundService =
            new HomeBackgroundService();

    @Test
    void 밤이면_NIGHT_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.NIGHT,
                "맑음",
                10,
                0,
                20
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.NIGHT);
        assertThat(response.color()).isEqualTo("#111827");
    }

    @Test
    void 비가_오면_RAINY_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "비",
                3,
                0,
                24
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.RAINY);
        assertThat(response.color()).isEqualTo("#CBD5E1");
    }

    @Test
    void 소나기이면_RAINY_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "소나기",
                3,
                0,
                24
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.RAINY);
    }

    @Test
    void 눈이_오면_SNOWY_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "눈",
                3,
                0,
                0
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.SNOWY);
        assertThat(response.color()).isEqualTo("#E0F2FE");
    }

    @Test
    void 낮에_자외선지수가_11_이상이면_DAY_EXTREME_UV_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "맑음",
                11,
                0,
                30
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.DAY_EXTREME_UV);
        assertThat(response.color()).isEqualTo("#FDBA74");
    }

    @Test
    void 낮에_자외선지수가_8_이상이면_DAY_VERY_HIGH_UV_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "맑음",
                8,
                0,
                28
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.DAY_VERY_HIGH_UV);
        assertThat(response.color()).isEqualTo("#FFE7A3");
    }

    @Test
    void 강수확률이_60_이상이면_RAINY_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "맑음",
                3,
                60,
                24
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.RAINY);
    }

    @Test
    void 흐림이면_CLOUDY_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "흐림",
                3,
                0,
                24
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.CLOUDY);
        assertThat(response.color()).isEqualTo("#E2E8F0");
    }

    @Test
    void 특별한_조건이_없으면_DAY_NORMAL_배경을_반환한다() {
        // given
        HomeBackgroundCondition condition = new HomeBackgroundCondition(
                HomeMode.DAY,
                "맑음",
                2,
                0,
                22
        );

        // when
        HomeBackgroundResponse response =
                homeBackgroundService.decideBackground(condition);

        // then
        assertThat(response.theme()).isEqualTo(HomeBackgroundTheme.DAY_NORMAL);
        assertThat(response.color()).isEqualTo("#E0F2FE");
    }
}