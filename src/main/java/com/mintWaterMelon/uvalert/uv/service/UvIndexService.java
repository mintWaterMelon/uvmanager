package com.mintWaterMelon.uvalert.uv.service;

import com.mintWaterMelon.uvalert.uv.dto.UvIndexSampleResponse;
import org.springframework.stereotype.Service;

@Service
public class UvIndexService {

    public UvIndexSampleResponse getSampleUvIndex() {
        int uvIndex = 7;
        String level = classifyUvIndex(uvIndex);
        String message = createMessage(level);

        return new UvIndexSampleResponse(
                "Seoul",
                uvIndex,
                level,
                message
        );
    }

    private String classifyUvIndex(int uvIndex) {
        if (uvIndex <= 2) {
            return "LOW";
        }

        if (uvIndex <= 5) {
            return "MODERATE";
        }

        if (uvIndex <= 7) {
            return "HIGH";
        }

        if (uvIndex <= 10) {
            return "VERY_HIGH";
        }

        return "EXTREME";
    }

    private String createMessage(String level) {
        return switch (level) {
            case "LOW" -> "자외선 지수가 낮습니다.";
            case "MODERATE" -> "자외선 지수가 보통입니다. 장시간 외출 시 주의하세요.";
            case "HIGH" -> "자외선 지수가 높습니다. 외출 시 자외선 차단제를 사용하세요.";
            case "VERY_HIGH" -> "자외선 지수가 매우 높습니다. 외출을 줄이고 보호 장비를 착용하세요.";
            case "EXTREME" -> "자외선 지수가 위험 수준입니다. 가능한 실내에 머무르세요.";
            default -> "자외선 지수 정보를 확인할 수 없습니다.";
        };
    }
}
