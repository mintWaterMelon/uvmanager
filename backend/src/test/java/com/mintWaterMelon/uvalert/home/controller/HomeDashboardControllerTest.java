package com.mintWaterMelon.uvalert.home.controller;

import com.mintWaterMelon.uvalert.home.dto.HomeAdviceResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeAdviceSeverity;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeBackgroundTheme;
import com.mintWaterMelon.uvalert.home.dto.HomeDashboardResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeDateType;
import com.mintWaterMelon.uvalert.home.dto.HomeLocationResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeTableResponse;
import com.mintWaterMelon.uvalert.home.service.HomeDashboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeDashboardController.class)
class HomeDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HomeDashboardService homeDashboardService;

    @Test
    @DisplayName("홈 대시보드를 조회한다")
    void getDashboard() throws Exception {
        HomeDashboardResponse response = new HomeDashboardResponse(
                LocalDateTime.of(2026, 6, 9, 10, 0),
                LocalDate.of(2026, 6, 9),
                HomeDateType.TODAY,
                new HomeLocationResponse("1100000000", "서울특별시"),
                new HomeBackgroundResponse(HomeBackgroundTheme.DAY_NORMAL, "#E0F2FE", "일반적인 낮 시간대 배경입니다."),
                new HomeTableResponse(List.of(), List.of()),
                new HomeAdviceResponse("오늘은 자외선 부담이 낮은 편이에요", "일상적인 외출은 큰 부담이 적습니다.", HomeAdviceSeverity.NORMAL)
        );

        BDDMockito.given(homeDashboardService.getDashboard(eq("1100000000"), eq(HomeDateType.TODAY)))
                .willReturn(response);

        mockMvc.perform(get("/api/home/dashboard")
                        .param("areaNo", "1100000000")
                        .param("dateType", "TODAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateType").value("TODAY"))
                .andExpect(jsonPath("$.location.areaNo").value("1100000000"))
                .andExpect(jsonPath("$.location.name").value("서울특별시"))
                .andExpect(jsonPath("$.background.theme").value("DAY_NORMAL"))
                .andExpect(jsonPath("$.advice.severity").value("NORMAL"));
    }
}
