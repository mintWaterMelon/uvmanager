package com.mintWaterMelon.uvalert.home.controller;

import com.mintWaterMelon.uvalert.home.dto.HomeDashboardResponse;
import com.mintWaterMelon.uvalert.home.dto.HomeDateType;
import com.mintWaterMelon.uvalert.home.dto.HomeMode;
import com.mintWaterMelon.uvalert.home.service.HomeDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeDashboardController {

    private final HomeDashboardService homeDashboardService;

    public HomeDashboardController(HomeDashboardService homeDashboardService) {
        this.homeDashboardService = homeDashboardService;
    }

    @GetMapping("/api/home/dashboard")
    public HomeDashboardResponse getDashboard(
            @RequestParam(defaultValue = "1100000000") String areaNo,
            @RequestParam(defaultValue = "TODAY") HomeDateType dateType,
            @RequestParam(defaultValue = "DAY") HomeMode mode
    ) {
        return homeDashboardService.getDashboard(areaNo, dateType, mode);
    }
}