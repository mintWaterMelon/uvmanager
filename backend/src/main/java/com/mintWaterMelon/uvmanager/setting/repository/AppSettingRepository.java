package com.mintWaterMelon.uvmanager.setting.repository;

import com.mintWaterMelon.uvmanager.setting.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {
}
