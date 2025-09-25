package com.mintWaterMelon.uvalert.home.dto;

public record HomeAdviceResponse(
        String title,
        String message,
        String severity
) {
}