package com.mintWaterMelon.uvalert.area.controller;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/api/areas")
    public List<AreaResponse> searchAreas(
            @RequestParam(required = false) String keyword
    ) {
        return areaService.searchAreas(keyword);
    }
}
