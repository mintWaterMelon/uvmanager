package com.mintWaterMelon.uvalert.area.loader;

import com.mintWaterMelon.uvalert.area.dto.AreaResponse;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class AreaExcelLoader {

    private static final String AREA_EXCEL_PATH = "data/forecast-grid.xlsx";

    public List<AreaResponse> loadAreas() {
        ZipSecureFile.setMinInflateRatio(0.001);

        try (
                InputStream inputStream = new ClassPathResource(AREA_EXCEL_PATH).getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            List<AreaResponse> areas = new ArrayList<>();

            DataFormatter formatter = new DataFormatter();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                String areaNo = getCellValueAsString(row.getCell(1), formatter);
                String level1 = getCellValueAsString(row.getCell(2), formatter);
                String level2 = getCellValueAsString(row.getCell(3), formatter);
                String level3 = getCellValueAsString(row.getCell(4), formatter);
                int gridX = getCellValueAsInt(row.getCell(5), formatter);
                int gridY = getCellValueAsInt(row.getCell(6), formatter);

                if (areaNo.isBlank() || level1.isBlank()) {
                    continue;
                }

                areas.add(new AreaResponse(
                        areaNo,
                        level1,
                        level2,
                        level3,
                        createDisplayName(level1, level2, level3),
                        gridX,
                        gridY
                ));
            }

            return areas;
        } catch (Exception e) {
            throw new IllegalStateException("지역 격자 Excel 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private String getCellValueAsString(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }

        return formatter.formatCellValue(cell).trim();
    }

    private int getCellValueAsInt(Cell cell, DataFormatter formatter) {
        String value = getCellValueAsString(cell, formatter);

        if (value.isBlank()) {
            return 0;
        }

        return Integer.parseInt(value.replace(".0", ""));
    }

    private String createDisplayName(String level1, String level2, String level3) {
        return String.join(" ", level1, level2, level3)
                .replaceAll("\\s+", " ")
                .trim();
    }
}