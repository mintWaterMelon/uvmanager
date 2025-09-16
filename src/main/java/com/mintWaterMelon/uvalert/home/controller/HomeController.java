package com.mintWaterMelon.uvalert.home.controller;

import com.mintWaterMelon.uvalert.home.dto.HomeResponse;
import com.mintWaterMelon.uvalert.home.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/api/home")
    public HomeResponse getHome(
            @RequestParam String areaNo
    ) {
        return homeService.getHome(areaNo);
    }
}
