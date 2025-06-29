package com.shinlogis.wms.common.util.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shinlogis.wms.inoutbound.model.IODetail;

/**
 * 입고처리를 엑셀파일로 저장하는 클래스
 * @author YEJIN
 * @since 2025-06-29
 */
public class ExcelExporter {

    // 입고처리 데이터를 기반으로 엑셀 Workbook 생성
    public static Workbook createIODetailWorkbook(List<IODetail> detailList) {
        Workbook workbook = new XSSFWorkbook(); // 엑셀 문서 객체 생성
        Sheet sheet = workbook.createSheet("입고처리목록"); // 시트 생성

        /* 헤더(컬럼명) 스타일 */
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);


        /* 헤더 행 */
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "입고상세ID", "상품코드", "상품명", "예정입고수량", "파손코드",
                "파손수량", "실제입고수량", "저장창고", "처리일자", "담당자"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        /* 데이터 행 */
        int rowNum = 1;
        for (IODetail detail : detailList) {
            Row row = sheet.createRow(rowNum++);

            // 0. 입고상세ID
            row.createCell(0).setCellValue(detail.getIoDetailId());

            // 1. 상품코드
            if (detail.getProductSnapshot() != null) {
                row.createCell(1).setCellValue(detail.getProductSnapshot().getProductCode());
            } else {
                row.createCell(1).setCellValue("");
            }

            // 2. 상품명
            if (detail.getProductSnapshot() != null) {
                row.createCell(2).setCellValue(detail.getProductSnapshot().getProductName());
            } else {
                row.createCell(2).setCellValue("");
            }

            // 3. 예정입고수량
            row.createCell(3).setCellValue(detail.getPlannedQuantity());

            // 4. 파손코드
            if (detail.getDamagedCode() != null) {
                row.createCell(4).setCellValue(detail.getDamagedCode().getName());
            } else {
                row.createCell(4).setCellValue("");
            }

            // 5. 파손수량
            row.createCell(5).setCellValue(detail.getDamageQuantity());

            // 6. 실제입고수량
            row.createCell(6).setCellValue(detail.getActualQuantity());

            // 7. 저장창고 코드
            if (detail.getWarehouse() != null) {
                row.createCell(7).setCellValue(detail.getWarehouse().getWarehouseCode());
            } else {
                row.createCell(7).setCellValue("");
            }

            // 8. 처리일자
            if (detail.getProccessedDate() != null) {
                Cell dateCell = row.createCell(8);
                dateCell.setCellValue(detail.getProccessedDate());

                CellStyle dateStyle = workbook.createCellStyle();
                CreationHelper createHelper = workbook.getCreationHelper();
                dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                dateCell.setCellStyle(dateStyle);
            } else {
                row.createCell(8).setCellValue("");
            }

            // 9. 담당자 ID
            if (detail.getUser() != null) {
                row.createCell(9).setCellValue(detail.getUser().getId());
            } else {
                row.createCell(9).setCellValue("");
            }
        }

        // 컬럼 너비 자동 조정
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    /**
     * 엑셀 파일로 저장하는 메서드
     * @auther 김예진
     * @since 2025-06-29
     * @param detailList
     * @param filePath
     * @return
     */
    public static boolean exportIODetailListToExcel(List<IODetail> detailList, String filePath) {
        try {
            Workbook workbook = createIODetailWorkbook(detailList); // 엑셀 데이터 생성
            FileOutputStream fos = new FileOutputStream(filePath); // 파일 출력 스트림 생성
            workbook.write(fos); // 파일에 엑셀 데이터 쓰기
            return true; // 저장 성공
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 저장 실패
        }
    }
}
