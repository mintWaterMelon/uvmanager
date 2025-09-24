package com.mintWaterMelon.uvalert.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItem;
import com.mintWaterMelon.uvalert.weather.dto.WeatherApiItemsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class KmaShortForecastClient {

    private final RestClient restClient;
    private final String serviceKey;
    private final String shortForecastPath;

    public KmaShortForecastClient(
            RestClient.Builder restClientBuilder,
            @Value("${kma.short-forecast.base-url}") String baseUrl,
            @Value("${kma.service-key}") String serviceKey,
            @Value("${kma.short-forecast.path}") String shortForecastPath
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.serviceKey = serviceKey;
        this.shortForecastPath = shortForecastPath;
    }

    public WeatherApiItemsResponse getShortForecast(
            String baseDate,
            String baseTime,
            int nx,
            int ny
    ) {
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(shortForecastPath)
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 1000)
                        .queryParam("dataType", "JSON")
                        .queryParam("base_date", baseDate)
                        .queryParam("base_time", baseTime)
                        .queryParam("nx", nx)
                        .queryParam("ny", ny)
                        .build()
                )
                .retrieve()
                .body(JsonNode.class);

        validateResponse(response, "단기예보 API");

        List<WeatherApiItem> items = extractItems(response);

        return new WeatherApiItemsResponse(items);
    }

    private void validateResponse(JsonNode response, String apiName) {
        JsonNode header = response.path("response").path("header");

        String resultCode = header.path("resultCode").asText();
        String resultMsg = header.path("resultMsg").asText();

        if (!"00".equals(resultCode)) {
            throw new IllegalStateException(apiName + " 호출 실패: " + resultCode + " / " + resultMsg);
        }
    }

    private List<WeatherApiItem> extractItems(JsonNode response) {
        JsonNode itemNode = response.path("response")
                .path("body")
                .path("items")
                .path("item");

        List<WeatherApiItem> items = new ArrayList<>();

        if (itemNode.isArray()) {
            for (JsonNode node : itemNode) {
                items.add(toItem(node));
            }
        } else if (!itemNode.isMissingNode() && !itemNode.isEmpty()) {
            items.add(toItem(itemNode));
        }

        return items;
    }

    private WeatherApiItem toItem(JsonNode node) {
        return new WeatherApiItem(
                node.path("category").asText(),
                node.path("fcstDate").asText(),
                node.path("fcstTime").asText(),
                node.path("fcstValue").asText()
        );
    }
}