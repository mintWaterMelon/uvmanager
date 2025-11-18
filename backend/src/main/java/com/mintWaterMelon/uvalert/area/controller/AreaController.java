package com.mintWaterMelon.uvalert.area.controller;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.service.AreaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지역 API", description = "지역 검색 API")
@RestController
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @Operation(
            summary = "지역 검색",
            description = "키워드로 지역을 검색합니다. 키워드가 없으면 전체 지역 목록을 조회합니다."
    )
    @GetMapping("/api/areas")
    public List<AreaResponse> searchAreas(
            @RequestParam(required = false) String keyword
    ) {
        return areaService.searchAreas(keyword);
    }
}