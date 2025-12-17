package com.mintWaterMelon.uvmanager.push.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity
public class PushSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean uvAlertEnabled;

    private int uvAlertThreshold;

    private boolean dustAlertEnabled;

    private LocalTime alertTime;

    protected PushSetting() {
    }

    public PushSetting(
            boolean uvAlertEnabled,
            int uvAlertThreshold,
            boolean dustAlertEnabled,
            LocalTime alertTime
    ) {
        this.uvAlertEnabled = uvAlertEnabled;
        this.uvAlertThreshold = uvAlertThreshold;
        this.dustAlertEnabled = dustAlertEnabled;
        this.alertTime = alertTime;
    }

    public void update(
            boolean uvAlertEnabled,
            int uvAlertThreshold,
            boolean dustAlertEnabled,
            LocalTime alertTime
    ) {
        this.uvAlertEnabled = uvAlertEnabled;
        this.uvAlertThreshold = uvAlertThreshold;
        this.dustAlertEnabled = dustAlertEnabled;
        this.alertTime = alertTime;
    }

    public Long getId() {
        return id;
    }

    public boolean isUvAlertEnabled() {
        return uvAlertEnabled;
    }

    public int getUvAlertThreshold() {
        return uvAlertThreshold;
    }

    public boolean isDustAlertEnabled() {
        return dustAlertEnabled;
    }

    public LocalTime getAlertTime() {
        return alertTime;
    }
}