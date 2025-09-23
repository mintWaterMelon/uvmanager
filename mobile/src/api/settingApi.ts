import { apiGet, apiPut } from "./client";

export type SettingRequest = {
    defaultAreaNo: string;
    defaultUvThreshold: number;
    sunscreenAlertEnabled: boolean;
    defaultAlertTime: string;
};

export type SettingResponse = {
    defaultAreaNo: string;
    defaultLocationName: string;
    defaultUvThreshold: number;
    sunscreenAlertEnabled: boolean;
    defaultAlertTime: string;
};

export function getSettings() {
    return apiGet<SettingResponse>("/api/settings");
}

export function updateSettings(request: SettingRequest) {
    return apiPut<SettingResponse, SettingRequest>("/api/settings", request);
}