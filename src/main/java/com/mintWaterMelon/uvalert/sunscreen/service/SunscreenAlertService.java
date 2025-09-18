package com.mintWaterMelon.uvalert.sunscreen.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertRequest;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertResponse;
import com.mintWaterMelon.uvalert.sunscreen.entity.SunscreenAlert;
import com.mintWaterMelon.uvalert.sunscreen.repository.SunscreenAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SunscreenAlertService {

    private final SunscreenAlertRepository sunscreenAlertRepository;
    private final AreaService areaService;

    public SunscreenAlertService(
            SunscreenAlertRepository sunscreenAlertRepository,
            AreaService areaService
    ) {
        this.sunscreenAlertRepository = sunscreenAlertRepository;
        this.areaService = areaService;
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
