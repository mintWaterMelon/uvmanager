import { apiGet, apiPut, type ApiRequestOptions } from "./client";

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

export function getSettings(options?: ApiRequestOptions) {
    return apiGet<SettingResponse>("/api/settings", options);
}

export function updateSettings(
    request: SettingRequest,
    options?: ApiRequestOptions
) {
    return apiPut<SettingResponse, SettingRequest>(
        "/api/settings",
        request,
        options
    );
}
