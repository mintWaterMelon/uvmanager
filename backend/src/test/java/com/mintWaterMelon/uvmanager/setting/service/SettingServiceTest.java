package com.mintWaterMelon.uvmanager.setting.service;

import com.mintWaterMelon.uvmanager.area.dto.AreaResponse;
import com.mintWaterMelon.uvmanager.area.service.AreaService;
import com.mintWaterMelon.uvmanager.setting.dto.SettingRequest;
import com.mintWaterMelon.uvmanager.setting.dto.SettingResponse;
import com.mintWaterMelon.uvmanager.setting.entity.AppSetting;
import com.mintWaterMelon.uvmanager.setting.repository.AppSettingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class SettingServiceTest {

    private final AppSettingRepository appSettingRepository =
            Mockito.mock(AppSettingRepository.class);

    private final AreaService areaService =
            Mockito.mock(AreaService.class);

    private final SettingService settingService =
            new SettingService(appSettingRepository, areaService);

    @Test
    @DisplayName("저장된 앱 설정이 없으면 기본 설정을 생성해서 반환한다")
    void getSettingsCreatesDefaultSettingWhenEmpty() {
        AppSetting defaultSetting = new AppSetting(
                "1100000000",
                6,
                true,
                LocalTime.of(8, 0)
        );

        given(appSettingRepository.findAll()).willReturn(List.of());
        given(appSettingRepository.save(Mockito.any(AppSetting.class)))
                .willReturn(defaultSetting);
        given(areaService.getAreaByAreaNo("1100000000"))
                .willReturn(area("1100000000", "서울특별시", "", "", "서울특별시", 60, 127));

        SettingResponse response = settingService.getSettings();

        assertThat(response.defaultAreaNo()).isEqualTo("1100000000");
        assertThat(response.defaultLocationName()).isEqualTo("서울특별시");
        assertThat(response.defaultUvThreshold()).isEqualTo(6);
        assertThat(response.sunscreenAlertEnabled()).isTrue();
        assertThat(response.defaultAlertTime()).isEqualTo(LocalTime.of(8, 0));
        verify(appSettingRepository).save(Mockito.any(AppSetting.class));
    }

    @Test
    @DisplayName("저장된 앱 설정이 있으면 기존 설정을 반환한다")
    void getSettingsReturnsExistingSetting() {
        AppSetting existingSetting = new AppSetting(
                "1168000000",
                8,
                false,
                LocalTime.of(9, 30)
        );

        given(appSettingRepository.findAll()).willReturn(List.of(existingSetting));
        given(areaService.getAreaByAreaNo("1168000000"))
                .willReturn(area("1168000000", "서울특별시", "강남구", "", "서울특별시 강남구", 61, 126));

        SettingResponse response = settingService.getSettings();

        assertThat(response.defaultAreaNo()).isEqualTo("1168000000");
        assertThat(response.defaultLocationName()).isEqualTo("서울특별시 강남구");
        assertThat(response.defaultUvThreshold()).isEqualTo(8);
        assertThat(response.sunscreenAlertEnabled()).isFalse();
        assertThat(response.defaultAlertTime()).isEqualTo(LocalTime.of(9, 30));
        verify(appSettingRepository, Mockito.never()).save(Mockito.any(AppSetting.class));
    }

    @Test
    @DisplayName("앱 설정을 수정한다")
    void updateSettingsUpdatesExistingSetting() {
        AppSetting existingSetting = new AppSetting(
                "1100000000",
                6,
                true,
                LocalTime.of(8, 0)
        );
        SettingRequest request = new SettingRequest(
                "1168000000",
                9,
                false,
                LocalTime.of(21, 15)
        );

        given(appSettingRepository.findAll()).willReturn(List.of(existingSetting));
        given(areaService.getAreaByAreaNo("1168000000"))
                .willReturn(area("1168000000", "서울특별시", "강남구", "", "서울특별시 강남구", 61, 126));

        SettingResponse response = settingService.updateSettings(request);

        assertThat(response.defaultAreaNo()).isEqualTo("1168000000");
        assertThat(response.defaultLocationName()).isEqualTo("서울특별시 강남구");
        assertThat(response.defaultUvThreshold()).isEqualTo(9);
        assertThat(response.sunscreenAlertEnabled()).isFalse();
        assertThat(response.defaultAlertTime()).isEqualTo(LocalTime.of(21, 15));

        assertThat(existingSetting.getDefaultAreaNo()).isEqualTo("1168000000");
        assertThat(existingSetting.getDefaultUvThreshold()).isEqualTo(9);
        assertThat(existingSetting.isSunscreenAlertEnabled()).isFalse();
        assertThat(existingSetting.getDefaultAlertTime()).isEqualTo(LocalTime.of(21, 15));
    }

    @Test
    @DisplayName("앱 설정 수정 시 기존 설정이 없으면 기본 설정을 만든 뒤 수정한다")
    void updateSettingsCreatesDefaultThenUpdatesWhenEmpty() {
        AppSetting savedDefaultSetting = new AppSetting(
                "1100000000",
                6,
                true,
                LocalTime.of(8, 0)
        );
        SettingRequest request = new SettingRequest(
                "1168000000",
                7,
                false,
                LocalTime.of(7, 45)
        );

        given(appSettingRepository.findAll()).willReturn(List.of());
        given(appSettingRepository.save(Mockito.any(AppSetting.class)))
                .willReturn(savedDefaultSetting);
        given(areaService.getAreaByAreaNo("1168000000"))
                .willReturn(area("1168000000", "서울특별시", "강남구", "", "서울특별시 강남구", 61, 126));

        SettingResponse response = settingService.updateSettings(request);

        assertThat(response.defaultAreaNo()).isEqualTo("1168000000");
        assertThat(response.defaultLocationName()).isEqualTo("서울특별시 강남구");
        assertThat(response.defaultUvThreshold()).isEqualTo(7);
        assertThat(response.sunscreenAlertEnabled()).isFalse();
        assertThat(response.defaultAlertTime()).isEqualTo(LocalTime.of(7, 45));
        verify(appSettingRepository).save(Mockito.any(AppSetting.class));
    }

    private AreaResponse area(
            String areaNo,
            String level1,
            String level2,
            String level3,
            String displayName,
            int gridX,
            int gridY
    ) {
        return new AreaResponse(areaNo, level1, level2, level3, displayName, gridX, gridY);
    }
}
