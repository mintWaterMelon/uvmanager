import { apiGet } from "./client";

export type AreaResponse = {
    areaNo: string;
    level1: string;
    level2: string;
    level3: string;
    displayName: string;
    gridX: number;
    gridY: number;
};

export function searchAreas(keyword?: string) {
    const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : "";

    return apiGet<AreaResponse[]>(`/api/areas${query}`);
}