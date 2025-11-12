package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.home.dto.HomeTableCellResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeTableRowResponse;
import org.springframework.stereotype.Component;

@Component
public class HomeTableSummaryCalculator {

    public int findMaxValue(HomeTableRowResponse row) {
        if (row == null || row.cells() == null || row.cells().isEmpty()) {
            return 0;
        }

        return row.cells()
                .stream()
                .map(HomeTableCellResponse::value)
                .filter(value -> value != null)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    public String findRepresentativeWeather(HomeTableRowResponse weatherRow) {
        if (weatherRow == null || weatherRow.cells() == null) {
            return "맑음";
        }

        return weatherRow.cells()
                .stream()
                .filter(cell -> cell.mainText() != null)
                .filter(cell -> !cell.mainText().isBlank())
                .filter(cell -> !"-".equals(cell.mainText()))
                .findFirst()
                .map(HomeTableCellResponse::mainText)
                .orElse("맑음");
    }

    public Integer findRepresentativeTemperature(HomeTableRowResponse weatherRow) {
        if (weatherRow == null || weatherRow.cells() == null) {
            return null;
        }

        return weatherRow.cells()
                .stream()
                .map(HomeTableCellResponse::value)
                .filter(value -> value != null)
                .findFirst()
                .orElse(null);
    }
}