package com.mintWaterMelon.uvalert.sunscreen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity
public class SunscreenAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String areaNo;

    private LocalTime alertTime;

    private int uvThreshold;

    private boolean enabled;

    protected SunscreenAlert() {
    }

    public SunscreenAlert(
            String areaNo,
            LocalTime alertTime,
            int uvThreshold,
            boolean enabled
    ) {
        this.areaNo = areaNo;
        this.alertTime = alertTime;
        this.uvThreshold = uvThreshold;
        this.enabled = enabled;
    }

    public void update(
            String areaNo,
            LocalTime alertTime,
            int uvThreshold,
            boolean enabled
    ) {
        this.areaNo = areaNo;
        this.alertTime = alertTime;
        this.uvThreshold = uvThreshold;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public LocalTime getAlertTime() {
        return alertTime;
    }

    public int getUvThreshold() {
        return uvThreshold;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
