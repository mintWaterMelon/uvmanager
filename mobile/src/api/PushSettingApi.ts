import { apiGet, apiPut } from "./client";

export type PushSettingRequest = {
    uvAlertEnabled: boolean;
    uvAlertThreshold: number;
    dustAlertEnabled: boolean;
    alertTime: string;
};

export type PushSettingResponse = {
    uvAlertEnabled: boolean;
    uvAlertThreshold: number;
    dustAlertEnabled: boolean;
    alertTime: string;
};

export function getPushSettings() {
    return apiGet<PushSettingResponse>("/api/push-settings");
}

export function updatePushSettings(request: PushSettingRequest) {
    return apiPut<PushSettingResponse, PushSettingRequest>(
        "/api/push-settings",
        request
    );
}