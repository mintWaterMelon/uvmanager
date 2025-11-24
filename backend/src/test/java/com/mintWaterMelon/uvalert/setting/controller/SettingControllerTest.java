package com.mintWaterMelon.uvalert.setting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintWaterMelon.uvalert.setting.dto.SettingRequest;
import com.mintWaterMelon.uvalert.setting.dto.SettingResponse;
import com.mintWaterMelon.uvalert.setting.service.SettingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingController.class)
class SettingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SettingService settingService;

    @Test
    @DisplayName("앱 설정을 조회한다")
    void getSettings() throws Exception {
        SettingResponse response = new SettingResponse(
                "1100000000",
                "서울특별시",
                6,
                true,
                LocalTime.of(8, 0)
        );

        BDDMockito.given(settingService.getSettings())
                .willReturn(response);

        mockMvc.perform(get("/api/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.defaultAreaNo").value("1100000000"))
                .andExpect(jsonPath("$.defaultLocationName").value("서울특별시"))
                .andExpect(jsonPath("$.defaultUvThreshold").value(6))
                .andExpect(jsonPath("$.sunscreenAlertEnabled").value(true))
                .andExpect(jsonPath("$.defaultAlertTime").value("08:00:00"));
    }

    @Test
    @DisplayName("앱 설정을 수정한다")
    void updateSettings() throws Exception {
        SettingRequest request = new SettingRequest(
                "1168000000",
                8,
                false,
                LocalTime.of(9, 30)
        );

        SettingResponse response = new SettingResponse(
                "1168000000",
                "서울특별시 강남구",
                8,
                false,
                LocalTime.of(9, 30)
        );

        BDDMockito.given(settingService.updateSettings(any(SettingRequest.class)))
                .willReturn(response);

        mockMvc.perform(put("/api/settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.defaultAreaNo").value("1168000000"))
                .andExpect(jsonPath("$.defaultLocationName").value("서울특별시 강남구"))
                .andExpect(jsonPath("$.defaultUvThreshold").value(8))
                .andExpect(jsonPath("$.sunscreenAlertEnabled").value(false))
                .andExpect(jsonPath("$.defaultAlertTime").value("09:30:00"));
    }

    @Test
    @DisplayName("기본 지역 코드가 비어 있으면 400을 반환한다")
    void updateSettingsWithBlankDefaultAreaNo() throws Exception {
        SettingRequest request = new SettingRequest(
                "",
                8,
                true,
                LocalTime.of(9, 30)
        );

        mockMvc.perform(put("/api/settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("기본 자외선 기준값이 11을 초과하면 400을 반환한다")
    void updateSettingsWithInvalidUvThreshold() throws Exception {
        SettingRequest request = new SettingRequest(
                "1100000000",
                12,
                true,
                LocalTime.of(9, 30)
        );

        mockMvc.perform(put("/api/settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("기본 알림 시간이 null이면 400을 반환한다")
    void updateSettingsWithNullDefaultAlertTime() throws Exception {
        SettingRequest request = new SettingRequest(
                "1100000000",
                8,
                true,
                null
        );

        mockMvc.perform(put("/api/settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
