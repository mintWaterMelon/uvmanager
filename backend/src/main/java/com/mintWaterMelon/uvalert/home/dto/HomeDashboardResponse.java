package com.mintWaterMelon.uvalert.home.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HomeDashboardResponse(
        LocalDateTime currentTime,
        LocalDate selectedDate,
        HomeDateType dateType,
        HomeLocationResponse location,
        HomeBackgroundResponse background,
        HomeTableResponse table,
        HomeAdviceResponse advice
) {
}