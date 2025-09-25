package com.mintWaterMelon.uvalert.home.dto;

import java.util.List;

public record HomeTableRowResponse(
        HomeTableRowType type,
        String label,
        List<HomeTableCellResponse> cells
) {
}