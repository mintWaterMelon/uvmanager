package com.mintWaterMelon.uvalert.push.repository;

import com.mintWaterMelon.uvalert.push.entity.PushSetting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PushSettingRepositoryTest {

    @Autowired
    private PushSettingRepository pushSettingRepository;

    @Test
    @DisplayName("푸시 설정을 저장하고 id로 조회한다")
    void saveAndFindById() {
        // given
        PushSetting pushSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        PushSetting savedSetting = pushSettingRepository.save(pushSetting);

        // when
        Optional<PushSetting> result =
                pushSettingRepository.findById(savedSetting.getId());

        // then
        assertThat(result).isPresent();

        PushSetting foundSetting = result.get();
        assertThat(foundSetting.getId()).isNotNull();
        assertThat(foundSetting.isUvAlertEnabled()).isTrue();
        assertThat(foundSetting.getUvAlertThreshold()).isEqualTo(8);
        assertThat(foundSetting.isDustAlertEnabled()).isFalse();
        assertThat(foundSetting.getAlertTime()).isEqualTo(LocalTime.of(8, 0));
    }

    @Test
    @DisplayName("저장된 모든 푸시 설정을 조회한다")
    void findAllPushSettings() {
        // given
        PushSetting morningSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        PushSetting eveningSetting = new PushSetting(
                false,
                6,
                true,
                LocalTime.of(18, 30)
        );

        pushSettingRepository.save(morningSetting);
        pushSettingRepository.save(eveningSetting);

        // when
        List<PushSetting> result = pushSettingRepository.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(PushSetting::getUvAlertThreshold)
                .containsExactlyInAnyOrder(8, 6);
    }

    @Test
    @DisplayName("푸시 설정을 수정하면 변경된 값이 저장된다")
    void updatePushSetting() {
        // given
        PushSetting pushSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        PushSetting savedSetting = pushSettingRepository.save(pushSetting);

        // when
        savedSetting.update(
                false,
                5,
                true,
                LocalTime.of(21, 15)
        );

        pushSettingRepository.flush();

        // then
        Optional<PushSetting> result =
                pushSettingRepository.findById(savedSetting.getId());

        assertThat(result).isPresent();

        PushSetting foundSetting = result.get();
        assertThat(foundSetting.isUvAlertEnabled()).isFalse();
        assertThat(foundSetting.getUvAlertThreshold()).isEqualTo(5);
        assertThat(foundSetting.isDustAlertEnabled()).isTrue();
        assertThat(foundSetting.getAlertTime()).isEqualTo(LocalTime.of(21, 15));
    }

    @Test
    @DisplayName("푸시 설정을 삭제한다")
    void deletePushSetting() {
        // given
        PushSetting pushSetting = new PushSetting(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        PushSetting savedSetting = pushSettingRepository.save(pushSetting);

        // when
        pushSettingRepository.delete(savedSetting);

        // then
        Optional<PushSetting> result =
                pushSettingRepository.findById(savedSetting.getId());

        assertThat(result).isEmpty();
    }
}