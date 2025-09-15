package com.mintWaterMelon.uvalert.uv.controller;

import com.mintWaterMelon.uvalert.uv.dto.UvIndexResponse;
import com.mintWaterMelon.uvalert.uv.service.UvIndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UvIndexController {

    private final UvIndexService uvIndexService;

    public UvIndexController(UvIndexService uvIndexService) {
        this.uvIndexService = uvIndexService;
    }

    @GetMapping("/api/uv-index")
    public UvIndexResponse getUvIndex(
            @RequestParam String areaNo,
            @RequestParam String time
    ) {
        return uvIndexService.getUvIndex(areaNo, time);
    }
}
