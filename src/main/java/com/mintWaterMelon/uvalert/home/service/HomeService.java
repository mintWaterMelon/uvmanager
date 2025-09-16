package com.mintWaterMelon.uvalert.home.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import com.mintWaterMelon.uvalert.home.dto.HomeLocationResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeResponse;
import com.mintWaterMelon.uvalert.uv.dto.UvForecast;
import com.mintWaterMelon.uvalert.uv.dto.UvIndexResponse;
import com.mintWaterMelon.uvalert.uv.service.UvIndexService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class HomeService {

    private final UvIndexService uvIndexService;
    private final AreaService areaService;

    public HomeService(
            UvIndexService uvIndexService,
            AreaService areaService
    ) {
        this.uvIndexService = uvIndexService;
        this.areaService = areaService;
    }

    public HomeResponse getHome(String areaNo) {
        LocalDateTime currentTime = LocalDateTime.now();

        UvIndexResponse uvIndexResponse = uvIndexService.getCurrentUvIndex(areaNo);

        UvForecast currentUv = findCurrentUv(
                uvIndexResponse.forecasts(),
                currentTime
        );

        AreaResponse area = areaService.getAreaByAreaNo(areaNo);

        HomeLocationResponse location = new HomeLocationResponse(
                area.areaNo(),
                area.displayName()
        );

        return new HomeResponse(
                currentTime,
                location,
                uvIndexResponse.baseTime(),
                currentUv,
                uvIndexResponse.forecasts()
        );
    }

    private UvForecast findCurrentUv(
            List<UvForecast> forecasts,
            LocalDateTime currentTime
    ) {
        return forecasts.stream()
                .filter(forecast -> !forecast.forecastTime().isAfter(currentTime))
                .max(Comparator.comparing(UvForecast::forecastTime))
                .orElseGet(() -> forecasts.isEmpty() ? null : forecasts.get(0));
    }
}