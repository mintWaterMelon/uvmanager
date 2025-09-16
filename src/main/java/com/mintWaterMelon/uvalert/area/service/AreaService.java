package com.mintWaterMelon.uvalert.area.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private static final List<AreaResponse> AREAS = List.of(
            new AreaResponse(
                    "1100000000",
                    "서울특별시",
                    "",
                    "",
                    "서울특별시"
            ),
            new AreaResponse(
                    "2600000000",
                    "부산광역시",
                    "",
                    "",
                    "부산광역시"
            ),
            new AreaResponse(
                    "2700000000",
                    "대구광역시",
                    "",
                    "",
                    "대구광역시"
            ),
            new AreaResponse(
                    "2800000000",
                    "인천광역시",
                    "",
                    "",
                    "인천광역시"
            ),
            new AreaResponse(
                    "2900000000",
                    "광주광역시",
                    "",
                    "",
                    "광주광역시"
            ),
            new AreaResponse(
                    "3000000000",
                    "대전광역시",
                    "",
                    "",
                    "대전광역시"
            ),
            new AreaResponse(
                    "3100000000",
                    "울산광역시",
                    "",
                    "",
                    "울산광역시"
            ),
            new AreaResponse(
                    "4100000000",
                    "경기도",
                    "",
                    "",
                    "경기도"
            )
    );

    public List<AreaResponse> searchAreas(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return AREAS;
        }

        String normalizedKeyword = keyword.trim();

        return AREAS.stream()
                .filter(area -> containsKeyword(area, normalizedKeyword))
                .toList();
    }

    private boolean containsKeyword(AreaResponse area, String keyword) {
        return area.areaNo().contains(keyword)
                || area.level1().contains(keyword)
                || area.level2().contains(keyword)
                || area.level3().contains(keyword)
                || area.displayName().contains(keyword);
    }

    public AreaResponse getAreaByAreaNo(String areaNo) {
        return AREAS.stream()
                .filter(area -> area.areaNo().equals(areaNo))
                .findFirst()
                .orElse(new AreaResponse(
                        areaNo,
                        "",
                        "",
                        "",
                        "알 수 없는 지역"
                ));
    }
}
