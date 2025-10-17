package com.mintWaterMelon.uvalert.home.dto;

import java.time.LocalDate;

public record HomeDashboardSlot(
        LocalDate date,
        String displayTime,     //앱에 보여줄 시간 익일00시 -> 24시
        String apiTime
) {
}