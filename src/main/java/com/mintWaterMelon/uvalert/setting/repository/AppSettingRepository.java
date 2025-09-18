package com.mintWaterMelon.uvalert.setting.repository;

import com.mintWaterMelon.uvalert.setting.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {
}
