import { apiGet } from "./client";

export type UvForecast = {
    hourAfter: number;
    forecastTime: string;
    value: number;
    level: string;
    message: string;
};

export type HomeResponse = {
    currentTime: string;
    location: {
        areaNo: string;
        name: string;
    };
    baseTime: string;
    currentUv: UvForecast | null;
    forecasts: UvForecast[];
};

export function getHome(areaNo: string) {
    return apiGet<HomeResponse>(`/api/home?areaNo=${areaNo}`);
}