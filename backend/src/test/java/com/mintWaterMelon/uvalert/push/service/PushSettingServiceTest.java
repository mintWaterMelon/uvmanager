package com.mintWaterMelon.uvalert.push.service;

import com.mintWaterMelon.uvalert.push.dto.PushSettingRequest;
import com.mintWaterMelon.uvalert.push.dto.PushSettingResponse;
import com.mintWaterMelon.uvalert.push.entity.PushSetting;
import com.mintWaterMelon.uvalert.push.repository.PushSettingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class PushSettingServiceTest {

    private final PushSettingRepository pushSettingRepository =
            Mockito.mock(PushSettingRepository.class);

    private final PushSettingService pushSettingService =
            new PushSettingService(pushSettingRepository);

    @Test
    @DisplayName("저장된 푸시 설정이 없으면 기본 설정을 생성해서 반환한다")
    void getPushSettingsCreatesDefaultSettingWhenEmpty() {
        // given
        PushSetting defaultSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        given(pushSettingRepository.findAll())
                .willReturn(List.of());

        given(pushSettingRepository.save(Mockito.any(PushSetting.class)))
                .willReturn(defaultSetting);

        // when
        PushSettingResponse response = pushSettingService.getPushSettings();

        // then
        assertThat(response.uvAlertEnabled()).isTrue();
        assertThat(response.uvAlertThreshold()).isEqualTo(8);
        assertThat(response.dustAlertEnabled()).isFalse();
        assertThat(response.alertTime()).isEqualTo(LocalTime.of(8, 0));

        verify(pushSettingRepository).save(Mockito.any(PushSetting.class));
    }

    @Test
    @DisplayName("저장된 푸시 설정이 있으면 기존 설정을 반환한다")
    void getPushSettingsReturnsExistingSetting() {
        // given
        PushSetting existingSetting = new PushSetting(
                false,
                6,
                true,
                LocalTime.of(9, 30)
        );

        given(pushSettingRepository.findAll())
                .willReturn(List.of(existingSetting));

        // when
        PushSettingResponse response = pushSettingService.getPushSettings();

        // then
        assertThat(response.uvAlertEnabled()).isFalse();
        assertThat(response.uvAlertThreshold()).isEqualTo(6);
        assertThat(response.dustAlertEnabled()).isTrue();
        assertThat(response.alertTime()).isEqualTo(LocalTime.of(9, 30));

        verify(pushSettingRepository, Mockito.never())
                .save(Mockito.any(PushSetting.class));
    }

    @Test
    @DisplayName("푸시 설정을 수정한다")
    void updatePushSettingsUpdatesExistingSetting() {
        // given
        PushSetting existingSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        PushSettingRequest request = new PushSettingRequest(
                false,
                5,
                true,
                LocalTime.of(21, 15)
        );

        given(pushSettingRepository.findAll())
                .willReturn(List.of(existingSetting));

        // when
        PushSettingResponse response =
                pushSettingService.updatePushSettings(request);

        // then
        assertThat(response.uvAlertEnabled()).isFalse();
        assertThat(response.uvAlertThreshold()).isEqualTo(5);
        assertThat(response.dustAlertEnabled()).isTrue();
        assertThat(response.alertTime()).isEqualTo(LocalTime.of(21, 15));

        assertThat(existingSetting.isUvAlertEnabled()).isFalse();
        assertThat(existingSetting.getUvAlertThreshold()).isEqualTo(5);
        assertThat(existingSetting.isDustAlertEnabled()).isTrue();
        assertThat(existingSetting.getAlertTime()).isEqualTo(LocalTime.of(21, 15));
    }

    @Test
    @DisplayName("푸시 설정 수정 시 기존 설정이 없으면 기본 설정을 만든 뒤 수정한다")
    void updatePushSettingsCreatesDefaultThenUpdatesWhenEmpty() {
        // given
        PushSetting savedDefaultSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        PushSettingRequest request = new PushSettingRequest(
                false,
                4,
                false,
                LocalTime.of(7, 45)
        );

        given(pushSettingRepository.findAll())
                .willReturn(List.of());

        given(pushSettingRepository.save(Mockito.any(PushSetting.class)))
                .willReturn(savedDefaultSetting);

        // when
        PushSettingResponse response =
                pushSettingService.updatePushSettings(request);

        // then
        assertThat(response.uvAlertEnabled()).isFalse();
        assertThat(response.uvAlertThreshold()).isEqualTo(4);
        assertThat(response.dustAlertEnabled()).isFalse();
        assertThat(response.alertTime()).isEqualTo(LocalTime.of(7, 45));

        verify(pushSettingRepository).save(Mockito.any(PushSetting.class));
    }
}