package com.mintWaterMelon.uvalert.area.controller;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AreaController.class)
class AreaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AreaService areaService;

    @Test
    @DisplayName("지역 목록을 조회한다")
    void searchAreas() throws Exception {
        // given
        AreaResponse seoul = new AreaResponse(
                "1100000000",
                "서울특별시",
                "",
                "",
                "서울특별시",
                60,
                127
        );

        BDDMockito.given(areaService.searchAreas(null))
                .willReturn(List.of(seoul));

        // when & then
        mockMvc.perform(get("/api/areas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].areaNo").value("1100000000"))
                .andExpect(jsonPath("$[0].displayName").value("서울특별시"))
                .andExpect(jsonPath("$[0].gridX").value(60))
                .andExpect(jsonPath("$[0].gridY").value(127));
    }

    @Test
    @DisplayName("키워드로 지역을 검색한다")
    void searchAreasByKeyword() throws Exception {
        // given
        AreaResponse gangnam = new AreaResponse(
                "1168000000",
                "서울특별시",
                "강남구",
                "",
                "서울특별시 강남구",
                61,
                126
        );

        BDDMockito.given(areaService.searchAreas(eq("강남")))
                .willReturn(List.of(gangnam));

        // when & then
        mockMvc.perform(get("/api/areas")
                        .param("keyword", "강남"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].areaNo").value("1168000000"))
                .andExpect(jsonPath("$[0].level1").value("서울특별시"))
                .andExpect(jsonPath("$[0].level2").value("강남구"))
                .andExpect(jsonPath("$[0].displayName").value("서울특별시 강남구"));
    }
}