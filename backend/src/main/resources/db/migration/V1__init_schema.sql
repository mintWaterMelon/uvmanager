CREATE TABLE app_setting (
                             id BIGSERIAL PRIMARY KEY,
                             default_area_no VARCHAR(20) NOT NULL,
                             default_uv_threshold INTEGER NOT NULL,
                             sunscreen_alert_enabled BOOLEAN NOT NULL,
                             default_alert_time TIME NOT NULL
);

CREATE TABLE push_setting (
                              id BIGSERIAL PRIMARY KEY,
                              uv_alert_enabled BOOLEAN NOT NULL,
                              uv_alert_threshold INTEGER NOT NULL,
                              dust_alert_enabled BOOLEAN NOT NULL,
                              alert_time TIME NOT NULL
);