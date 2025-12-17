package com.mintWaterMelon.uvmanager.push.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintWaterMelon.uvmanager.push.dto.PushSettingRequest;
import com.mintWaterMelon.uvmanager.push.dto.PushSettingResponse;
import com.mintWaterMelon.uvmanager.push.service.PushSettingService;
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

@WebMvcTest(PushSettingController.class)
class PushSettingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PushSettingService pushSettingService;

    @Test
    @DisplayName("푸시 설정을 조회한다")
    void getPushSettings() throws Exception {
        PushSettingResponse response = new PushSettingResponse(
                true,
                8,
                false,
                LocalTime.of(8, 0)
        );

        BDDMockito.given(pushSettingService.getPushSettings())
                .willReturn(response);

        mockMvc.perform(get("/api/push-settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uvAlertEnabled").value(true))
                .andExpect(jsonPath("$.uvAlertThreshold").value(8))
                .andExpect(jsonPath("$.dustAlertEnabled").value(false))
                .andExpect(jsonPath("$.alertTime").value("08:00:00"));
    }

    @Test
    @DisplayName("푸시 설정을 수정한다")
    void updatePushSettings() throws Exception {
        PushSettingRequest request = new PushSettingRequest(
                true,
                7,
                false,
                LocalTime.of(9, 30)
        );
        PushSettingResponse response = new PushSettingResponse(
                true,
                7,
                false,
                LocalTime.of(9, 30)
        );

        BDDMockito.given(pushSettingService.updatePushSettings(any(PushSettingRequest.class)))
                .willReturn(response);

        mockMvc.perform(put("/api/push-settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uvAlertEnabled").value(true))
                .andExpect(jsonPath("$.uvAlertThreshold").value(7))
                .andExpect(jsonPath("$.dustAlertEnabled").value(false))
                .andExpect(jsonPath("$.alertTime").value("09:30:00"));
    }

    @Test
    @DisplayName("자외선 알림 기준값이 11을 초과하면 400을 반환한다")
    void updatePushSettingsWithInvalidThreshold() throws Exception {
        PushSettingRequest request = new PushSettingRequest(
                true,
                12,
                false,
                LocalTime.of(9, 30)
        );

        mockMvc.perform(put("/api/push-settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("알림 시간이 null이면 400을 반환한다")
    void updatePushSettingsWithNullAlertTime() throws Exception {
        PushSettingRequest request = new PushSettingRequest(
                true,
                8,
                false,
                null
        );

        mockMvc.perform(put("/api/push-settings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
