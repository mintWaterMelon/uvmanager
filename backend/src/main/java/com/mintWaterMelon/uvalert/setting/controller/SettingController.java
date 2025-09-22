package com.mintWaterMelon.uvalert.setting.controller;

import com.mintWaterMelon.uvalert.setting.dto.SettingRequest;
import com.mintWaterMelon.uvalert.setting.dto.SettingResponse;
import com.mintWaterMelon.uvalert.setting.service.SettingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("/api/settings")
    public SettingResponse getSettings() {
        return settingService.getSettings();
    }

    @PutMapping("/api/settings")
    public SettingResponse updateSettings(
            @Valid @RequestBody SettingRequest request
    ) {
        return settingService.updateSettings(request);
    }
}