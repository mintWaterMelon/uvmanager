package com.mintWaterMelon.uvmanager.setting.service;

import com.mintWaterMelon.uvmanager.area.dto.AreaResponse;
import com.mintWaterMelon.uvmanager.area.service.AreaService;
import com.mintWaterMelon.uvmanager.setting.dto.SettingRequest;
import com.mintWaterMelon.uvmanager.setting.dto.SettingResponse;
import com.mintWaterMelon.uvmanager.setting.entity.AppSetting;
import com.mintWaterMelon.uvmanager.setting.repository.AppSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
public class SettingService {

    private static final String DEFAULT_AREA_NO = "1100000000";
    private static final int DEFAULT_UV_THRESHOLD = 6;
    private static final boolean DEFAULT_SUNSCREEN_ALERT_ENABLED = true;
    private static final LocalTime DEFAULT_ALERT_TIME = LocalTime.of(8, 0);

    private final AppSettingRepository appSettingRepository;
    private final AreaService areaService;

    public SettingService(
            AppSettingRepository appSettingRepository,
            AreaService areaService
    ) {
        this.appSettingRepository = appSettingRepository;
        this.areaService = areaService;
    }

    @Transactional()
    public SettingResponse getSettings() {
        AppSetting appSetting = findOrCreateDefaultSetting();

        return toResponse(appSetting);
    }

    @Transactional
    public SettingResponse updateSettings(SettingRequest request) {
        AppSetting appSetting = findOrCreateDefaultSetting();

        appSetting.update(
                request.defaultAreaNo(),
                request.defaultUvThreshold(),
                request.sunscreenAlertEnabled(),
                request.defaultAlertTime()
        );

        return toResponse(appSetting);
    }

    private AppSetting findOrCreateDefaultSetting() {
        return appSettingRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> appSettingRepository.save(new AppSetting(
                        DEFAULT_AREA_NO,
                        DEFAULT_UV_THRESHOLD,
                        DEFAULT_SUNSCREEN_ALERT_ENABLED,
                        DEFAULT_ALERT_TIME
                )));
    }

    private SettingResponse toResponse(AppSetting appSetting) {
        AreaResponse area = areaService.getAreaByAreaNo(appSetting.getDefaultAreaNo());

        return new SettingResponse(
                appSetting.getDefaultAreaNo(),
                area.displayName(),
                appSetting.getDefaultUvThreshold(),
                appSetting.isSunscreenAlertEnabled(),
                appSetting.getDefaultAlertTime()
        );
    }
}
