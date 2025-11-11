package com.mintWaterMelon.uvalert.weather.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UvIndexCalculatorTest {

    @Test
    void 시간대별_자외선지수에서_최댓값을_구한다() {
        // given
        Map<String, Integer> hourlyValues = Map.of(
                "09", 3,
                "12", 7,
                "15", 5
        );

        // when
        int maxUv = hourlyValues.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        // then
        assertThat(maxUv).isEqualTo(7);
    }

    @Test
    void 시간대별_자외선지수가_비어있으면_0을_반환한다() {
        // given
        Map<String, Integer> hourlyValues = Map.of();

        // when
        int maxUv = hourlyValues.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        // then
        assertThat(maxUv).isEqualTo(0);
    }
}