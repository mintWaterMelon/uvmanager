package com.mintWaterMelon.uvalert.notification.repository;

import com.mintWaterMelon.uvalert.notification.entity.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}