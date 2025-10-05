package com.mintWaterMelon.uvalert.push.controller;

import com.mintWaterMelon.uvalert.push.dto.PushSettingRequest;
import com.mintWaterMelon.uvalert.push.dto.PushSettingResponse;
import com.mintWaterMelon.uvalert.push.service.PushSettingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class PushSettingController {

    private final PushSettingService pushSettingService;

    public PushSettingController(PushSettingService pushSettingService) {
        this.pushSettingService = pushSettingService;
    }

    @GetMapping("/api/push-settings")
    public PushSettingResponse getPushSettings() {
        return pushSettingService.getPushSettings();
    }

    @PutMapping("/api/push-settings")
    public PushSettingResponse updatePushSettings(
            @Valid @RequestBody PushSettingRequest request
    ) {
        return pushSettingService.updatePushSettings(request);
    }
}