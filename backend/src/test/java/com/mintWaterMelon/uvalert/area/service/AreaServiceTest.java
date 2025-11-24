package com.mintWaterMelon.uvalert.area.service;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import com.mintWaterMelon.uvalert.area.loader.AreaExcelLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class AreaServiceTest {

    private final AreaExcelLoader areaExcelLoader = Mockito.mock(AreaExcelLoader.class);

    @Test
    @DisplayName("키워드가 없으면 전체 지역 목록을 반환한다")
    void searchAreasReturnsAllWhenKeywordIsBlank() {
        given(areaExcelLoader.loadAreas()).willReturn(List.of(
                area("1100000000", "서울특별시", "", "", "서울특별시", 60, 127),
                area("1168000000", "서울특별시", "강남구", "", "서울특별시 강남구", 61, 126)
        ));
        AreaService areaService = new AreaService(areaExcelLoader);

        assertThat(areaService.searchAreas(null)).hasSize(2);
        assertThat(areaService.searchAreas(" ")).hasSize(2);
    }

    @Test
    @DisplayName("키워드가 포함된 지역만 검색한다")
    void searchAreasByKeyword() {
        given(areaExcelLoader.loadAreas()).willReturn(List.of(
                area("1100000000", "서울특별시", "", "", "서울특별시", 60, 127),
                area("1168000000", "서울특별시", "강남구", "", "서울특별시 강남구", 61, 126)
        ));
        AreaService areaService = new AreaService(areaExcelLoader);

        List<AreaResponse> result = areaService.searchAreas("강남");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).areaNo()).isEqualTo("1168000000");
    }

    @Test
    @DisplayName("areaNo로 지역을 조회한다")
    void getAreaByAreaNo() {
        given(areaExcelLoader.loadAreas()).willReturn(List.of(
                area("1100000000", "서울특별시", "", "", "서울특별시", 60, 127)
        ));
        AreaService areaService = new AreaService(areaExcelLoader);

        AreaResponse result = areaService.getAreaByAreaNo("1100000000");

        assertThat(result.displayName()).isEqualTo("서울특별시");
    }

    @Test
    @DisplayName("없는 areaNo는 알 수 없는 지역을 반환한다")
    void getUnknownAreaByAreaNo() {
        given(areaExcelLoader.loadAreas()).willReturn(List.of());
        AreaService areaService = new AreaService(areaExcelLoader);

        AreaResponse result = areaService.getAreaByAreaNo("9999999999");

        assertThat(result.areaNo()).isEqualTo("9999999999");
        assertThat(result.displayName()).isEqualTo("알 수 없는 지역");
        assertThat(result.gridX()).isZero();
        assertThat(result.gridY()).isZero();
    }

    private AreaResponse area(
            String areaNo,
            String level1,
            String level2,
            String level3,
            String displayName,
            int gridX,
            int gridY
    ) {
        return new AreaResponse(areaNo, level1, level2, level3, displayName, gridX, gridY);
    }
}
