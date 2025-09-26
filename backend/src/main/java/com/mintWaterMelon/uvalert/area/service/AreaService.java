package com.mintWaterMelon.uvalert.area.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.loader.AreaExcelLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private final List<AreaResponse> areas;

    public AreaService(AreaExcelLoader areaExcelLoader) {
        this.areas = areaExcelLoader.loadAreas();
    }

    public List<AreaResponse> searchAreas(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return areas;
        }

        String normalizedKeyword = keyword.trim();

        return areas.stream()
                .filter(area -> containsKeyword(area, normalizedKeyword))
                .toList();
    }

    public AreaResponse getAreaByAreaNo(String areaNo) {
        return areas.stream()
                .filter(area -> area.areaNo().equals(areaNo))
                .findFirst()
                .orElse(new AreaResponse(
                        areaNo,
                        "",
                        "",
                        "",
                        "알 수 없는 지역",
                        0,
                        0
                ));
    }

    private boolean containsKeyword(AreaResponse area, String keyword) {
        return area.areaNo().contains(keyword)
                || area.level1().contains(keyword)
                || area.level2().contains(keyword)
                || area.level3().contains(keyword)
                || area.displayName().contains(keyword);
    }
}