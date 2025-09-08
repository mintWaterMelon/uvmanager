package com.mintWaterMelon.uvalert.health;

import com.mintWaterMelon.uvalert.health.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public HealthResponse health() {
        return new HealthResponse(
                "UP",
                "UV Alert server is running"
        );
    }
}
