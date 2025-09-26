package com.mintWaterMelon.uvalert.home.dto;

import java.time.LocalDate;

public record HomeTableCellResponse(
        LocalDate date,
        String time,
        String mainText,
        String subText,
        Integer value,
        String level
) {
}