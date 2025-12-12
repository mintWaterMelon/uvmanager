import { apiGet, type ApiRequestOptions } from "./client";

export type HomeDateType = "TODAY" | "TOMORROW" | "DAY_AFTER_TOMORROW";

export type HomeBackground = {
    theme: string;
    color: string;
    description: string;
};

export type HomeLocation = {
    areaNo: string;
    name: string;
};

export type HomeTimeSlot = {
    date: string;
    time: string;
    current: boolean;
};

export type HomeTableCell = {
    date: string;
    time: string;
    mainText: string;
    subText: string;
    value: number | null;
    level: string;
};

export type HomeTableRow = {
    type: "WEATHER" | "UV_INDEX" | "AIR_STAGNATION";
    label: string;
    cells: HomeTableCell[];
};

export type HomeTable = {
    timeSlots: HomeTimeSlot[];
    rows: HomeTableRow[];
};

export type HomeAdvice = {
    title: string;
    message: string;
    severity: "NORMAL" | "INFO" | "WARNING" | "DANGER" | string;
};

export type HomeDashboardResponse = {
    currentTime: string;
    selectedDate: string;
    dateType: HomeDateType;
    location: HomeLocation;
    background: HomeBackground;
    table: HomeTable;
    advice: HomeAdvice;
};

export function getHomeDashboard(
    areaNo: string,
    dateType: HomeDateType,
    options?: ApiRequestOptions
) {
    const query = `areaNo=${areaNo}&dateType=${dateType}`;

    return apiGet<HomeDashboardResponse>(`/api/home/dashboard?${query}`, options);
}