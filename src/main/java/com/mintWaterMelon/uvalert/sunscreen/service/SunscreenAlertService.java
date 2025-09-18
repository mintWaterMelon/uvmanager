package com.mintWaterMelon.uvalert.sunscreen.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertCheckResponse;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertRequest;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertResponse;
import com.mintWaterMelon.uvalert.sunscreen.entity.SunscreenAlert;
import com.mintWaterMelon.uvalert.sunscreen.repository.SunscreenAlertRepository;
import com.mintWaterMelon.uvalert.uv.dto.UvForecast;
import com.mintWaterMelon.uvalert.uv.dto.UvIndexResponse;
import com.mintWaterMelon.uvalert.uv.service.UvIndexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class SunscreenAlertService {

    private final SunscreenAlertRepository sunscreenAlertRepository;
    private final AreaService areaService;
    private final UvIndexService uvIndexService;

    public SunscreenAlertService(
            SunscreenAlertRepository sunscreenAlertRepository,
            AreaService areaService,
            UvIndexService uvIndexService
    ) {
        this.sunscreenAlertRepository = sunscreenAlertRepository;
        this.areaService = areaService;
        this.uvIndexService = uvIndexService;
    }

    @Transactional
    public SunscreenAlertResponse createSunscreenAlert(SunscreenAlertRequest request) {
        SunscreenAlert sunscreenAlert = new SunscreenAlert(
                request.areaNo(),
                request.alertTime(),
                request.uvThreshold(),
                request.enabled()
        );

        SunscreenAlert savedSunscreenAlert = sunscreenAlertRepository.save(sunscreenAlert);

        return toResponse(savedSunscreenAlert);
    }

    @Transactional(readOnly = true)
    public List<SunscreenAlertResponse> getSunscreenAlerts() {
        return sunscreenAlertRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SunscreenAlertResponse getSunscreenAlert(Long id) {
        SunscreenAlert sunscreenAlert = findSunscreenAlertById(id);

        return toResponse(sunscreenAlert);
    }

    @Transactional
    public SunscreenAlertResponse updateSunscreenAlert(
            Long id,
            SunscreenAlertRequest request
    ) {
        SunscreenAlert sunscreenAlert = findSunscreenAlertById(id);

        sunscreenAlert.update(
                request.areaNo(),
                request.alertTime(),
                request.uvThreshold(),
                request.enabled()
        );

        return toResponse(sunscreenAlert);
    }

    @Transactional
    public void deleteSunscreenAlert(Long id) {
        SunscreenAlert sunscreenAlert = findSunscreenAlertById(id);

        sunscreenAlertRepository.delete(sunscreenAlert);
    }

    @Transactional(readOnly = true)
    public SunscreenAlertCheckResponse checkSunscreenAlert(Long id) {
        SunscreenAlert sunscreenAlert = findSunscreenAlertById(id);

        UvIndexResponse uvIndexResponse =
                uvIndexService.getCurrentUvIndex(sunscreenAlert.getAreaNo());

        UvForecast currentUv = findCurrentUv(
                uvIndexResponse.forecasts(),
                LocalDateTime.now()
        );

        if (currentUv == null) {
            throw new IllegalStateException("현재 시간에 해당하는 UV 지수 정보를 찾을 수 없습니다.");
        }

        boolean shouldNotify =
                sunscreenAlert.isEnabled()
                        && currentUv.value() >= sunscreenAlert.getUvThreshold();

        AreaResponse area = areaService.getAreaByAreaNo(sunscreenAlert.getAreaNo());

        return new SunscreenAlertCheckResponse(
                sunscreenAlert.getId(),
                sunscreenAlert.getAreaNo(),
                area.displayName(),
                sunscreenAlert.isEnabled(),
                sunscreenAlert.getUvThreshold(),
                currentUv.value(),
                currentUv.level(),
                shouldNotify,
                createAlertMessage(currentUv.value(), shouldNotify)
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

    private String createAlertMessage(int currentUvValue, boolean shouldNotify) {
        if (shouldNotify) {
            return "현재 자외선 지수가 " + currentUvValue + "입니다. 선크림을 바르세요.";
        }

        return "현재 자외선 지수가 " + currentUvValue + "입니다. 아직 알림 기준에 도달하지 않았습니다.";
    }

    private SunscreenAlert findSunscreenAlertById(Long id) {
        return sunscreenAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("선크림 알림 설정을 찾을 수 없습니다. id=" + id));
    }

    private SunscreenAlertResponse toResponse(SunscreenAlert sunscreenAlert) {
        AreaResponse area = areaService.getAreaByAreaNo(sunscreenAlert.getAreaNo());

        return new SunscreenAlertResponse(
                sunscreenAlert.getId(),
                sunscreenAlert.getAreaNo(),
                area.displayName(),
                sunscreenAlert.getAlertTime(),
                sunscreenAlert.getUvThreshold(),
                sunscreenAlert.isEnabled()
        );
    }
}