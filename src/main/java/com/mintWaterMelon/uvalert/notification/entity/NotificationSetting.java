package com.mintWaterMelon.uvalert.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String region;

    private int uvThreshold;

    private boolean enabled;

    protected NotificationSetting() {
    }

    public NotificationSetting(String region, int uvThreshold, boolean enabled) {
        this.region = region;
        this.uvThreshold = uvThreshold;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }

    public int getUvThreshold() {
        return uvThreshold;
    }

    public boolean isEnabled() {
        return enabled;
    }
}