package com.mintWaterMelon.uvmanager.push.service;

import com.mintWaterMelon.uvmanager.push.dto.PushSettingRequest;
import com.mintWaterMelon.uvmanager.push.dto.PushSettingResponse;
import com.mintWaterMelon.uvmanager.push.entity.PushSetting;
import com.mintWaterMelon.uvmanager.push.repository.PushSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
public class PushSettingService {

    private static final boolean DEFAULT_UV_ALERT_ENABLED = true;
    private static final int DEFAULT_UV_ALERT_THRESHOLD = 8;
    private static final boolean DEFAULT_DUST_ALERT_ENABLED = false;
    private static final LocalTime DEFAULT_ALERT_TIME = LocalTime.of(8, 0);

    private final PushSettingRepository pushSettingRepository;

    public PushSettingService(PushSettingRepository pushSettingRepository) {
        this.pushSettingRepository = pushSettingRepository;
    }

    @Transactional
    public PushSettingResponse getPushSettings() {
        PushSetting pushSetting = findOrCreateDefaultSetting();

        return toResponse(pushSetting);
    }

    @Transactional
    public PushSettingResponse updatePushSettings(PushSettingRequest request) {
        PushSetting pushSetting = findOrCreateDefaultSetting();

        pushSetting.update(
                request.uvAlertEnabled(),
                request.uvAlertThreshold(),
                request.dustAlertEnabled(),
                request.alertTime()
        );

        return toResponse(pushSetting);
    }

    private PushSetting findOrCreateDefaultSetting() {
        return pushSettingRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> pushSettingRepository.save(new PushSetting(
                        DEFAULT_UV_ALERT_ENABLED,
                        DEFAULT_UV_ALERT_THRESHOLD,
                        DEFAULT_DUST_ALERT_ENABLED,
                        DEFAULT_ALERT_TIME
                )));
    }

    private PushSettingResponse toResponse(PushSetting pushSetting) {
        return new PushSettingResponse(
                pushSetting.isUvAlertEnabled(),
                pushSetting.getUvAlertThreshold(),
                pushSetting.isDustAlertEnabled(),
                pushSetting.getAlertTime()
        );
    }
}