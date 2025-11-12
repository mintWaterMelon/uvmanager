package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.home.dto.HomeTableCellResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeTableRowResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeTableRowType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HomeTableSummaryCalculatorTest {

    private final HomeTableSummaryCalculator calculator =
            new HomeTableSummaryCalculator();

    @Test
    void row의_cell_value_중_최댓값을_구한다() {
        // given
        HomeTableRowResponse row = new HomeTableRowResponse(
                HomeTableRowType.UV_INDEX,
                "자외선 지수",
                List.of(
                        cell("09:00", 3),
                        cell("12:00", 7),
                        cell("15:00", 5)
                )
        );

        // when
        int maxValue = calculator.findMaxValue(row);

        // then
        assertThat(maxValue).isEqualTo(7);
    }

    @Test
    void null_value는_최댓값_계산에서_제외한다() {
        // given
        HomeTableRowResponse row = new HomeTableRowResponse(
                HomeTableRowType.UV_INDEX,
                "자외선 지수",
                List.of(
                        cell("09:00", null),
                        cell("12:00", 6),
                        cell("15:00", null)
                )
        );

        // when
        int maxValue = calculator.findMaxValue(row);

        // then
        assertThat(maxValue).isEqualTo(6);
    }

    @Test
    void 모든_value가_null이면_0을_반환한다() {
        // given
        HomeTableRowResponse row = new HomeTableRowResponse(
                HomeTableRowType.UV_INDEX,
                "자외선 지수",
                List.of(
                        cell("09:00", null),
                        cell("12:00", null)
                )
        );

        // when
        int maxValue = calculator.findMaxValue(row);

        // then
        assertThat(maxValue).isEqualTo(0);
    }

    @Test
    void 대표_날씨는_첫번째_유효한_mainText를_사용한다() {
        // given
        HomeTableRowResponse row = new HomeTableRowResponse(
                HomeTableRowType.WEATHER,
                "날씨 및 온도",
                List.of(
                        weatherCell("09:00", "-", 20),
                        weatherCell("12:00", "구름많음", 23),
                        weatherCell("15:00", "맑음", 25)
                )
        );

        // when
        String weather = calculator.findRepresentativeWeather(row);

        // then
        assertThat(weather).isEqualTo("구름많음");
    }

    @Test
    void 대표_기온은_첫번째_유효한_value를_사용한다() {
        // given
        HomeTableRowResponse row = new HomeTableRowResponse(
                HomeTableRowType.WEATHER,
                "날씨 및 온도",
                List.of(
                        weatherCell("09:00", "맑음", null),
                        weatherCell("12:00", "맑음", 24),
                        weatherCell("15:00", "맑음", 27)
                )
        );

        // when
        Integer temperature = calculator.findRepresentativeTemperature(row);

        // then
        assertThat(temperature).isEqualTo(24);
    }

    private HomeTableCellResponse cell(String time, Integer value) {
        return new HomeTableCellResponse(
                LocalDate.of(2026, 6, 9),
                time,
                value == null ? "-" : String.valueOf(value),
                value == null ? "정보 없음" : "보통",
                value,
                value == null ? "UNKNOWN" : "MODERATE"
        );
    }

    private HomeTableCellResponse weatherCell(
            String time,
            String mainText,
            Integer value
    ) {
        return new HomeTableCellResponse(
                LocalDate.of(2026, 6, 9),
                time,
                mainText,
                value == null ? "-" : value + "℃",
                value,
                mainText
        );
    }
}