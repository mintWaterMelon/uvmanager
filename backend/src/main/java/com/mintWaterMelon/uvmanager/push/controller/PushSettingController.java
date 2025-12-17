package com.mintWaterMelon.uvmanager.push.controller;

import com.mintWaterMelon.uvmanager.push.dto.PushSettingRequest;
import com.mintWaterMelon.uvmanager.push.dto.PushSettingResponse;
import com.mintWaterMelon.uvmanager.push.service.PushSettingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "푸시 설정 API", description = "자외선 및 알림 설정 API")
@RestController
public class PushSettingController {

    private final PushSettingService pushSettingService;

    public PushSettingController(PushSettingService pushSettingService) {
        this.pushSettingService = pushSettingService;
    }

    @Operation(
            summary = "푸시 설정 조회",
            description = "현재 저장된 푸시 알림 설정을 조회합니다."
    )
    @GetMapping("/api/push-settings")
    public PushSettingResponse getPushSettings() {
        return pushSettingService.getPushSettings();
    }

    @Operation(
            summary = "푸시 설정 수정",
            description = "자외선 알림 여부, 알림 기준값, 알림 시간을 수정합니다."
    )
    @PutMapping("/api/push-settings")
    public PushSettingResponse updatePushSettings(
            @Valid @RequestBody PushSettingRequest request
    ) {
        return pushSettingService.updatePushSettings(request);
    }
}