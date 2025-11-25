package com.mintWaterMelon.uvalert.setting.controller;

import com.mintWaterMelon.uvalert.setting.dto.SettingRequest;
import com.mintWaterMelon.uvalert.setting.dto.SettingResponse;
import com.mintWaterMelon.uvalert.setting.service.SettingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "앱 설정 API", description = "기본 지역, 자외선 기준값, 알림 시간 설정 API")
@RestController
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @Operation(
            summary = "앱 설정 조회",
            description = "현재 저장된 앱 기본 설정을 조회합니다."
    )
    @GetMapping("/api/settings")
    public SettingResponse getSettings() {
        return settingService.getSettings();
    }

    @Operation(
            summary = "앱 설정 수정",
            description = "기본 지역, 자외선 기준값, 자외선 차단제 알림 여부, 기본 알림 시간을 수정합니다."
    )
    @PutMapping("/api/settings")
    public SettingResponse updateSettings(
            @Valid @RequestBody SettingRequest request
    ) {
        return settingService.updateSettings(request);
    }
}