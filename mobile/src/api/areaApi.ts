import { apiGet, type ApiRequestOptions } from "./client";

export type AreaResponse = {
    areaNo: string;
    level1: string;
    level2: string;
    level3: string;
    displayName: string;
    gridX: number;
    gridY: number;
};

export function searchAreas(keyword?: string, options?: ApiRequestOptions) {
    const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : "";

    return apiGet<AreaResponse[]>(`/api/areas${query}`, options);
}
