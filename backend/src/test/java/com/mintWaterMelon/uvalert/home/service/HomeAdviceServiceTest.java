package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.home.dto.HomeAdviceCondition;
import com.mintWaterMelon.uvalert.home.dto.HomeAdviceResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeAdviceSeverity;
import com.mintWaterMelon.uvalert.home.dto.HomeMode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeAdviceServiceTest {

    private final HomeAdviceService homeAdviceService = new HomeAdviceService();

    @Test
    void 낮에_자외선지수가_11_이상이면_DANGER_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                11,
                0,
                25
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.DANGER);
        assertThat(response.title()).isEqualTo("자외선이 매우 위험해요");
    }

    @Test
    void 낮에_자외선지수가_8_이상이면_WARNING_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                8,
                0,
                25
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.WARNING);
        assertThat(response.title()).isEqualTo("자외선이 매우 강해요");
    }

    @Test
    void 강수확률이_60_이상이면_비_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                2,
                60,
                25
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("비가 올 가능성이 높아요");
    }

    @Test
    void 날씨가_비이면_흐린날_자외선_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "비",
                2,
                0,
                25
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("비가 와도 자외선 차단은 필요해요");
    }

    @Test
    void 낮에_자외선지수가_6_이상이면_WARNING_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                6,
                0,
                25
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.WARNING);
        assertThat(response.title()).isEqualTo("자외선이 높은 편이에요");
    }

    @Test
    void 낮에_자외선지수가_3_이상이면_INFO_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                3,
                0,
                25
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("자외선 차단을 준비하세요");
    }

    @Test
    void 기온이_30도_이상이면_더위_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                2,
                0,
                30
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("더운 날씨에는 피부 보호를 함께 챙기세요");
    }

    @Test
    void 낮에_특별한_조건이_없으면_NORMAL_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.DAY,
                "맑음",
                1,
                0,
                20
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.NORMAL);
        assertThat(response.title()).isEqualTo("오늘은 자외선 부담이 낮은 편이에요");
    }

    @Test
    void 밤이면_자외선_걱정이_적다는_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.NIGHT,
                "맑음",
                9,
                0,
                20
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.NORMAL);
        assertThat(response.title()).isEqualTo("밤에는 자외선 걱정이 적어요");
    }

    @Test
    void 밤에_강수확률이_60_이상이면_빗길_안내를_반환한다() {
        // given
        HomeAdviceCondition condition = new HomeAdviceCondition(
                HomeMode.NIGHT,
                "맑음",
                9,
                60,
                20
        );

        // when
        HomeAdviceResponse response = homeAdviceService.createAdvice(condition);

        // then
        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("밤에는 빗길을 조심하세요");
    }
}