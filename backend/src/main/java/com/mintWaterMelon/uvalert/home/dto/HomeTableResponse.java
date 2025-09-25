package com.mintWaterMelon.uvalert.home.dto;

import java.util.List;

public record HomeTableResponse(
        List<HomeTimeSlotResponse> timeSlots,
        List<HomeTableRowResponse> rows
) {
}