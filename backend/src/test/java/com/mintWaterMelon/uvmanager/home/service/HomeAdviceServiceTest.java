package com.mintWaterMelon.uvmanager.home.service;

import com.mintWaterMelon.uvmanager.home.dto.HomeAdviceCondition;
import com.mintWaterMelon.uvmanager.home.dto.HomeAdviceResponse;
import com.mintWaterMelon.uvmanager.home.dto.HomeAdviceSeverity;
import com.mintWaterMelon.uvmanager.home.dto.HomeMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeAdviceServiceTest {

    private final HomeAdviceService homeAdviceService = new HomeAdviceService();

    @Test
    @DisplayName("낮에 자외선지수가 11 이상이면 DANGER 안내를 반환한다")
    void createDangerAdviceForExtremeUv() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 11, 0, 25)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.DANGER);
        assertThat(response.title()).isEqualTo("자외선이 매우 위험해요");
    }

    @Test
    @DisplayName("낮에 자외선지수가 8 이상이면 WARNING 안내를 반환한다")
    void createWarningAdviceForVeryHighUv() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 8, 0, 25)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.WARNING);
        assertThat(response.title()).isEqualTo("자외선이 매우 강해요");
    }

    @Test
    @DisplayName("강수확률이 60 이상이면 비 안내를 반환한다")
    void createRainProbabilityAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 2, 60, 25)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("비가 올 가능성이 높아요");
    }

    @Test
    @DisplayName("날씨가 비이면 흐린날 자외선 안내를 반환한다")
    void createRainyWeatherAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "비", 2, 0, 25)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("비가 와도 자외선 차단은 필요해요");
    }

    @Test
    @DisplayName("날씨가 눈이면 반사 자외선 안내를 반환한다")
    void createSnowyWeatherAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "눈", 2, 0, 0)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("눈 오는 날에는 반사 자외선도 주의하세요");
    }

    @Test
    @DisplayName("낮에 자외선지수가 6 이상이면 WARNING 안내를 반환한다")
    void createWarningAdviceForHighUv() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 6, 0, 25)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.WARNING);
        assertThat(response.title()).isEqualTo("자외선이 높은 편이에요");
    }

    @Test
    @DisplayName("낮에 자외선지수가 3 이상이면 INFO 안내를 반환한다")
    void createInfoAdviceForModerateUv() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 3, 0, 25)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("자외선 차단을 준비하세요");
    }

    @Test
    @DisplayName("기온이 30도 이상이면 더위 안내를 반환한다")
    void createHotWeatherAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 2, 0, 30)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("더운 날씨에는 피부 보호를 함께 챙기세요");
    }

    @Test
    @DisplayName("낮에 특별한 조건이 없으면 NORMAL 안내를 반환한다")
    void createNormalAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.DAY, "맑음", 1, 0, 20)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.NORMAL);
        assertThat(response.title()).isEqualTo("오늘은 자외선 부담이 낮은 편이에요");
    }

    @Test
    @DisplayName("밤이면 자외선 걱정이 적다는 안내를 반환한다")
    void createNightAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.NIGHT, "맑음", 9, 0, 20)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.NORMAL);
        assertThat(response.title()).isEqualTo("밤에는 자외선 걱정이 적어요");
    }

    @Test
    @DisplayName("밤에 강수확률이 60 이상이면 빗길 안내를 반환한다")
    void createRainyNightAdvice() {
        HomeAdviceResponse response = homeAdviceService.createAdvice(
                new HomeAdviceCondition(HomeMode.NIGHT, "맑음", 9, 60, 20)
        );

        assertThat(response.severity()).isEqualTo(HomeAdviceSeverity.INFO);
        assertThat(response.title()).isEqualTo("밤에는 빗길을 조심하세요");
    }
}
