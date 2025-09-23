import { apiDelete, apiGet, apiPost, apiPut } from "./client";

export type SunscreenAlertRequest = {
    areaNo: string;
    alertTime: string;
    uvThreshold: number;
    enabled: boolean;
};

export type SunscreenAlertResponse = {
    id: number;
    areaNo: string;
    locationName: string;
    alertTime: string;
    uvThreshold: number;
    enabled: boolean;
};

export type SunscreenAlertCheckResponse = {
    alertId: number;
    areaNo: string;
    locationName: string;
    enabled: boolean;
    uvThreshold: number;
    currentUvValue: number;
    currentUvLevel: string;
    shouldNotify: boolean;
    message: string;
};

export function createSunscreenAlert(request: SunscreenAlertRequest) {
    return apiPost<SunscreenAlertResponse, SunscreenAlertRequest>(
        "/api/sunscreen-alerts",
        request
    );
}

export function getSunscreenAlerts() {
    return apiGet<SunscreenAlertResponse[]>("/api/sunscreen-alerts");
}

export function getSunscreenAlert(id: number) {
    return apiGet<SunscreenAlertResponse>(`/api/sunscreen-alerts/${id}`);
}

export function updateSunscreenAlert(
    id: number,
    request: SunscreenAlertRequest
) {
    return apiPut<SunscreenAlertResponse, SunscreenAlertRequest>(
        `/api/sunscreen-alerts/${id}`,
        request
    );
}

export function deleteSunscreenAlert(id: number) {
    return apiDelete(`/api/sunscreen-alerts/${id}`);
}

export function checkSunscreenAlert(id: number) {
    return apiGet<SunscreenAlertCheckResponse>(
        `/api/sunscreen-alerts/${id}/check`
    );
}