package com.mintWaterMelon.uvalert.sunscreen.controller;

import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertCheckResponse;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertRequest;
import com.mintWaterMelon.uvalert.sunscreen.dto.SunscreenAlertResponse;
import com.mintWaterMelon.uvalert.sunscreen.service.SunscreenAlertService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SunscreenAlertController {

    private final SunscreenAlertService sunscreenAlertService;

    public SunscreenAlertController(SunscreenAlertService sunscreenAlertService) {
        this.sunscreenAlertService = sunscreenAlertService;
    }

    @PostMapping("/api/sunscreen-alerts")
    public SunscreenAlertResponse createSunscreenAlert(
            @Valid @RequestBody SunscreenAlertRequest request
    ) {
        return sunscreenAlertService.createSunscreenAlert(request);
    }

    @GetMapping("/api/sunscreen-alerts")
    public List<SunscreenAlertResponse> getSunscreenAlerts() {
        return sunscreenAlertService.getSunscreenAlerts();
    }

    @GetMapping("/api/sunscreen-alerts/{id}")
    public SunscreenAlertResponse getSunscreenAlert(
            @PathVariable Long id
    ) {
        return sunscreenAlertService.getSunscreenAlert(id);
    }

    @PutMapping("/api/sunscreen-alerts/{id}")
    public SunscreenAlertResponse updateSunscreenAlert(
            @PathVariable Long id,
            @Valid @RequestBody SunscreenAlertRequest request
    ) {
        return sunscreenAlertService.updateSunscreenAlert(id, request);
    }

    @DeleteMapping("/api/sunscreen-alerts/{id}")
    public ResponseEntity<Void> deleteSunscreenAlert(
            @PathVariable Long id
    ) {
        sunscreenAlertService.deleteSunscreenAlert(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/sunscreen-alerts/{id}/check")
    public SunscreenAlertCheckResponse checkSunscreenAlert(
            @PathVariable Long id
    ) {
        return sunscreenAlertService.checkSunscreenAlert(id);
    }
}