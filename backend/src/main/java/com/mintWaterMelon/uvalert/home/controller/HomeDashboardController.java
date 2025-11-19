package com.mintWaterMelon.uvalert.home.controller;

import com.mintWaterMelon.uvalert.home.dto.HomeDashboardResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeDateType;
import com.mintWaterMelon.uvalert.home.service.HomeDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "홈 API", description = "홈 화면 대시보드 API")
@RestController
public class HomeDashboardController {

    private final HomeDashboardService homeDashboardService;

    public HomeDashboardController(HomeDashboardService homeDashboardService) {
        this.homeDashboardService = homeDashboardService;
    }

    @Operation(
            summary = "홈 대시보드 조회",
            description = "선택한 지역과 날짜 기준으로 날씨, 자외선지수, 강수확률, 배경 정보, 안내 메시지를 조회합니다."
    )
    @GetMapping("/api/home/dashboard")
    public HomeDashboardResponse getDashboard(
            @Parameter(
                    description = "지역 코드. 기본값은 서울특별시입니다.",
                    example = "1100000000"
            )
            @RequestParam(defaultValue = "1100000000") String areaNo,

            @Parameter(
                    description = "조회 날짜 타입. TODAY, TOMORROW, DAY_AFTER_TOMORROW 중 하나를 사용합니다.",
                    example = "TODAY"
            )
            @RequestParam(defaultValue = "TODAY") HomeDateType dateType
    ) {
        return homeDashboardService.getDashboard(areaNo, dateType);
    }
}