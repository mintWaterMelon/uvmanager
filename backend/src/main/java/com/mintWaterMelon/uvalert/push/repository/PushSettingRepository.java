package com.mintWaterMelon.uvalert.push.repository;

import com.mintWaterMelon.uvalert.push.entity.PushSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushSettingRepository extends JpaRepository<PushSetting, Long> {
}