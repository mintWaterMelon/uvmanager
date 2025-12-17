package com.mintWaterMelon.uvmanager.home.dto;

import java.time.LocalDate;

public record HomeDashboardSlot(
        LocalDate date,
        String time
) {
}