package com.mintWaterMelon.uvalert.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mintWaterMelon.uvalert.weather.dto.WeatherHourlyIndexResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class KmaUvIndexClient {

    private final RestClient restClient;
    private final String serviceKey;
    private final String uvPath;

    public KmaUvIndexClient(
            RestClient.Builder restClientBuilder,
            @Value("${kma.living-weather.base-url}") String baseUrl,
            @Value("${kma.service-key}") String serviceKey,
            @Value("${kma.living-weather.uv-path}") String uvPath
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.serviceKey = serviceKey;
        this.uvPath = uvPath;
    }

    public WeatherHourlyIndexResponse getUvIndex(String areaNo, String time) {
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(uvPath)
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

        if ("03".equals(resultCode) || "NO_DATA".equalsIgnoreCase(resultMsg)) {
            return new WeatherHourlyIndexResponse(
                    areaNo,
                    time,
                    new LinkedHashMap<>()
            );
        }

        validateResponse(response, "자외선지수 API");

        JsonNode item = extractFirstItem(response);

        return new WeatherHourlyIndexResponse(
                item.path("areaNo").asText(),
                item.path("date").asText(),
                extractHourlyValues(item)
        );
    }

    private void validateResponse(JsonNode response, String apiName) {
        JsonNode header = response.path("response").path("header");

        String resultCode = header.path("resultCode").asText();
        String resultMsg = header.path("resultMsg").asText();

        if (!"00".equals(resultCode)) {
            throw new IllegalStateException(apiName + " 호출 실패: " + resultCode + " / " + resultMsg);
        }
    }

    private JsonNode extractFirstItem(JsonNode response) {
        JsonNode itemNode = response.path("response")
                .path("body")
                .path("items")
                .path("item");

        JsonNode item = itemNode.isArray() ? itemNode.get(0) : itemNode;

        if (item == null || item.isMissingNode() || item.isEmpty()) {
            throw new IllegalStateException("자외선지수 API 응답에 item 데이터가 없습니다.");
        }

        return item;
    }

    private Map<Integer, Integer> extractHourlyValues(JsonNode item) {
        Map<Integer, Integer> hourlyValues = new LinkedHashMap<>();

        for (int hour = 0; hour <= 75; hour += 3) {
            String fieldName = "h" + hour;
            String valueText = item.path(fieldName).asText();

            if (valueText == null || valueText.isBlank()) {
                continue;
            }

            hourlyValues.put(hour, Integer.parseInt(valueText));
        }

        return hourlyValues;
    }
}