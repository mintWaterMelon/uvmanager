package com.mintWaterMelon.uvalert.uv.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mintWaterMelon.uvalert.uv.dto.UvForecast;
import com.mintWaterMelon.uvalert.uv.dto.UvIndexResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class KmaUvIndexClient {

    private final RestClient restClient;
    private final String serviceKey;

    public KmaUvIndexClient(
            RestClient.Builder restClientBuilder,
            @Value("${kma.uv.base-url}") String baseUrl,
            @Value("${kma.uv.service-key}") String serviceKey
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.serviceKey = serviceKey;
    }

    public UvIndexResponse getUvIndex(String areaNo, String time) {
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getUVIdxV5")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("areaNo", areaNo)
                        .queryParam("time", time)
                        .queryParam("dataType", "JSON")
                        .build()
                )
                .retrieve()
                .body(JsonNode.class);

        JsonNode header = response.path("response").path("header");

        String resultCode = header.path("resultCode").asText();
        String resultMsg = header.path("resultMsg").asText();

        if (!"00".equals(resultCode)) {
            throw new IllegalStateException("기상청 API 호출 실패: " + resultCode + " / " + resultMsg);
        }

        JsonNode itemNode = response.path("response")
                .path("body")
                .path("items")
                .path("item");

        JsonNode item = itemNode.isArray() ? itemNode.get(0) : itemNode;

        if (item == null || item.isMissingNode() || item.isEmpty()) {
            throw new IllegalStateException("기상청 API 응답에 자외선 지수 데이터가 없습니다.");
        }

        List<UvForecast> forecasts = extractForecasts(item);

        return new UvIndexResponse(
                item.path("areaNo").asText(),
                item.path("date").asText(),
                forecasts
        );
    }

    private List<UvForecast> extractForecasts(JsonNode item) {
        List<UvForecast> forecasts = new ArrayList<>();

        for (int hour = 0; hour <= 75; hour += 3) {
            String fieldName = "h" + hour;
            JsonNode valueNode = item.path(fieldName);

            if (valueNode.isMissingNode() || valueNode.isNull()) {
                continue;
            }

            String valueText = valueNode.asText();

            if (valueText == null || valueText.isBlank()) {
                continue;
            }

            int value = Integer.parseInt(valueText);

            forecasts.add(new UvForecast(
                    hour,
                    value,
                    classifyUvIndex(value),
                    createMessage(value)
            ));
        }

        return forecasts;
    }

    private String classifyUvIndex(int value) {
        if (value <= 2) {
            return "LOW";
        }

        if (value <= 5) {
            return "MODERATE";
        }

        if (value <= 7) {
            return "HIGH";
        }

        if (value <= 10) {
            return "VERY_HIGH";
        }

        return "EXTREME";
    }

    private String createMessage(int value) {
        if (value <= 2) {
            return "자외선 지수가 낮습니다.";
        }

        if (value <= 5) {
            return "자외선 지수가 보통입니다. 장시간 외출 시 주의하세요.";
        }

        if (value <= 7) {
            return "자외선 지수가 높습니다. 외출 시 자외선 차단제를 사용하세요.";
        }

        if (value <= 10) {
            return "자외선 지수가 매우 높습니다. 외출을 줄이고 보호 장비를 착용하세요.";
        }

        return "자외선 지수가 위험 수준입니다. 가능한 실내에 머무르세요.";
    }
}