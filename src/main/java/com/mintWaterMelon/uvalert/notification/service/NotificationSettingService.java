package com.mintWaterMelon.uvalert.notification.service;

import com.mintWaterMelon.uvalert.notification.dto.NotificationSettingRequest;
import com.mintWaterMelon.uvalert.notification.dto.NotificationSettingResponse;
import com.mintWaterMelon.uvalert.notification.entity.NotificationSetting;
import com.mintWaterMelon.uvalert.notification.repository.NotificationSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSettingService {

    private final NotificationSettingRepository notificationSettingRepository;

    public NotificationSettingService(NotificationSettingRepository notificationSettingRepository) {
        this.notificationSettingRepository = notificationSettingRepository;
    }

    public NotificationSettingResponse createNotificationSetting(NotificationSettingRequest request) {
        NotificationSetting notificationSetting = new NotificationSetting(
                request.region(),
                request.uvThreshold(),
                request.enabled()
        );

        NotificationSetting savedNotificationSetting =
                notificationSettingRepository.save(notificationSetting);

        return toResponse(savedNotificationSetting);
    }

    public List<NotificationSettingResponse> getNotificationSettings() {
        return notificationSettingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationSettingResponse toResponse(NotificationSetting notificationSetting) {
        return new NotificationSettingResponse(
                notificationSetting.getId(),
                notificationSetting.getRegion(),
                notificationSetting.getUvThreshold(),
                notificationSetting.isEnabled()
        );
    }
}