package com.mintWaterMelon.uvalert.home.dto;

import java.time.LocalDate;

public record HomeDashboardSlot(
        LocalDate date,
        String time
) {
}