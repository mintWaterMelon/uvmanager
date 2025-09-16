package com.mintWaterMelon.uvalert.area.loader;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class AreaExcelLoader {

    private static final String AREA_EXCEL_PATH = "data/area-codes.xlsx";

    public List<AreaResponse> loadAreas() {
        try (
                InputStream inputStream = new ClassPathResource(AREA_EXCEL_PATH).getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            List<AreaResponse> areas = new ArrayList<>();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                String areaNo = getCellValueAsString(row.getCell(1));
                String level1 = getCellValueAsString(row.getCell(2));
                String level2 = getCellValueAsString(row.getCell(3));
                String level3 = getCellValueAsString(row.getCell(4));

                if (areaNo.isBlank() || level1.isBlank()) {
                    continue;
                }

                areas.add(new AreaResponse(
                        areaNo,
                        level1,
                        level2,
                        level3,
                        createDisplayName(level1, level2, level3)
                ));
            }

            return areas;
        } catch (Exception e) {
            throw new IllegalStateException("지역 코드 Excel 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private String createDisplayName(String level1, String level2, String level3) {
        return String.join(" ",
                        level1,
                        level2,
                        level3
                )
                .replaceAll("\\s+", " ")
                .trim();
    }
}
