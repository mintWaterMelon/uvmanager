package com.mintWaterMelon.uvalert.notification.controller;

import com.mintWaterMelon.uvalert.notification.dto.NotificationSettingRequest;
import com.mintWaterMelon.uvalert.notification.dto.NotificationSettingResponse;
import com.mintWaterMelon.uvalert.notification.service.NotificationSettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    public NotificationSettingController(NotificationSettingService notificationSettingService) {
        this.notificationSettingService = notificationSettingService;
    }

    @PostMapping("/api/notification-settings")
    public NotificationSettingResponse createNotificationSetting(
            @RequestBody NotificationSettingRequest request
    ) {
        return notificationSettingService.createNotificationSetting(request);
    }

    @GetMapping("/api/notification-settings")
    public List<NotificationSettingResponse> getNotificationSettings() {
        return notificationSettingService.getNotificationSettings();
    }
}