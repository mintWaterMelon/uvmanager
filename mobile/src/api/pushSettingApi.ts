import { apiGet, apiPut, type ApiRequestOptions } from "./client";

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

export function getPushSettings(options?: ApiRequestOptions) {
    return apiGet<PushSettingResponse>("/api/push-settings", options);
}

export function updatePushSettings(
    request: PushSettingRequest,
    options?: ApiRequestOptions
) {
    return apiPut<PushSettingResponse, PushSettingRequest>(
        "/api/push-settings",
        request,
        options
    );
}
