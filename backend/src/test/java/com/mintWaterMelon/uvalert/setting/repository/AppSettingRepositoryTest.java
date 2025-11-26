package com.mintWaterMelon.uvalert.setting.repository;

import com.mintWaterMelon.uvalert.setting.entity.AppSetting;
import com.mintWaterMelon.uvalert.support.PostgresTestContainerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppSettingRepositoryTest extends PostgresTestContainerSupport {

    @Autowired
    private AppSettingRepository appSettingRepository;

    @Test
    @DisplayName("앱 설정을 저장하고 id로 조회한다")
    void saveAndFindById() {
        AppSetting appSetting = new AppSetting(
                "1100000000",
                6,
                true,
                LocalTime.of(8, 0)
        );

        AppSetting savedSetting = appSettingRepository.save(appSetting);

        Optional<AppSetting> result = appSettingRepository.findById(savedSetting.getId());

        assertThat(result).isPresent();
        AppSetting foundSetting = result.get();
        assertThat(foundSetting.getId()).isNotNull();
        assertThat(foundSetting.getDefaultAreaNo()).isEqualTo("1100000000");
        assertThat(foundSetting.getDefaultUvThreshold()).isEqualTo(6);
        assertThat(foundSetting.isSunscreenAlertEnabled()).isTrue();
        assertThat(foundSetting.getDefaultAlertTime()).isEqualTo(LocalTime.of(8, 0));
    }

    @Test
    @DisplayName("저장된 모든 앱 설정을 조회한다")
    void findAllAppSettings() {
        appSettingRepository.save(new AppSetting("1100000000", 6, true, LocalTime.of(8, 0)));
        appSettingRepository.save(new AppSetting("1168000000", 8, false, LocalTime.of(9, 30)));

        List<AppSetting> result = appSettingRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(AppSetting::getDefaultAreaNo)
                .containsExactlyInAnyOrder("1100000000", "1168000000");
    }

    @Test
    @DisplayName("앱 설정을 수정하면 변경된 값이 저장된다")
    void updateAppSetting() {
        AppSetting savedSetting = appSettingRepository.save(
                new AppSetting("1100000000", 6, true, LocalTime.of(8, 0))
        );

        savedSetting.update("1168000000", 9, false, LocalTime.of(21, 15));
        appSettingRepository.flush();

        Optional<AppSetting> result = appSettingRepository.findById(savedSetting.getId());

        assertThat(result).isPresent();
        AppSetting foundSetting = result.get();
        assertThat(foundSetting.getDefaultAreaNo()).isEqualTo("1168000000");
        assertThat(foundSetting.getDefaultUvThreshold()).isEqualTo(9);
        assertThat(foundSetting.isSunscreenAlertEnabled()).isFalse();
        assertThat(foundSetting.getDefaultAlertTime()).isEqualTo(LocalTime.of(21, 15));
    }

    @Test
    @DisplayName("앱 설정을 삭제한다")
    void deleteAppSetting() {
        AppSetting savedSetting = appSettingRepository.save(
                new AppSetting("1100000000", 6, true, LocalTime.of(8, 0))
        );

        appSettingRepository.delete(savedSetting);

        assertThat(appSettingRepository.findById(savedSetting.getId())).isEmpty();
    }
}
