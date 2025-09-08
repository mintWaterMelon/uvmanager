package com.mintWaterMelon.uvalert.uv;

import com.mintWaterMelon.uvalert.uv.dto.UvIndexSampleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UvIndexController {

    @GetMapping("/api/uv-index/sample")
    public UvIndexSampleResponse getSampleUvIndex() {
        return new UvIndexSampleResponse(
                "Seoul",
                7,
                "HIGH",
                "자외선 지수가 높습니다. 외출 시 자외선 차단제를 사용하세요."
        );
    }
}