package com.mintWaterMelon.uvmanager.push.repository;

import com.mintWaterMelon.uvmanager.push.entity.PushSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushSettingRepository extends JpaRepository<PushSetting, Long> {
}