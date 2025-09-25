package com.mintWaterMelon.uvalert.home.dto;

public record HomeTableCellResponse(
        String time,
        String mainText,
        String subText,
        Integer value,
        String level
) {
}