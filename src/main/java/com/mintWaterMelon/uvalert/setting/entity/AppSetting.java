package com.mintWaterMelon.uvalert.setting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity
public class AppSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String defaultAreaNo;

    private int defaultUvThreshold;

    private boolean sunscreenAlertEnabled;

    private LocalTime defaultAlertTime;

    protected AppSetting() {
    }

    public AppSetting(
            String defaultAreaNo,
            int defaultUvThreshold,
            boolean sunscreenAlertEnabled,
            LocalTime defaultAlertTime
    ) {
        this.defaultAreaNo = defaultAreaNo;
        this.defaultUvThreshold = defaultUvThreshold;
        this.sunscreenAlertEnabled = sunscreenAlertEnabled;
        this.defaultAlertTime = defaultAlertTime;
    }

    public void update(
            String defaultAreaNo,
            int defaultUvThreshold,
            boolean sunscreenAlertEnabled,
            LocalTime defaultAlertTime
    ) {
        this.defaultAreaNo = defaultAreaNo;
        this.defaultUvThreshold = defaultUvThreshold;
        this.sunscreenAlertEnabled = sunscreenAlertEnabled;
        this.defaultAlertTime = defaultAlertTime;
    }

    public Long getId() {
        return id;
    }

    public String getDefaultAreaNo() {
        return defaultAreaNo;
    }

    public int getDefaultUvThreshold() {
        return defaultUvThreshold;
    }

    public boolean isSunscreenAlertEnabled() {
        return sunscreenAlertEnabled;
    }

    public LocalTime getDefaultAlertTime() {
        return defaultAlertTime;
    }
}
