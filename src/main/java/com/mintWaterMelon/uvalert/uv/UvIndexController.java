package com.mintWaterMelon.uvalert.uv;

import com.mintWaterMelon.uvalert.uv.dto.UvIndexSampleResponse;
import com.mintWaterMelon.uvalert.uv.service.UvIndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UvIndexController {

    private final UvIndexService uvIndexService;

    public UvIndexController(UvIndexService uvIndexService) {
        this.uvIndexService = uvIndexService;
    }

    @GetMapping("/api/uv-index/sample")
    public UvIndexSampleResponse getSampleUvIndex() {
        return uvIndexService.getSampleUvIndex();
    }
}