package com.service;

import com.dto.ThongKeSLTrungTuyenDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.*;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThongKeSLTrungTuyenExporter {

    private static final String[] HEADERS = {
            "TT", "Mã ngành", "Tên ngành", "Phương thức", "Số lượng trúng tuyển"
    };

    private static final int[] COL_WIDTHS = { 6, 15, 40, 15, 25 };

    public static void exportWithChooser(List<ThongKeSLTrungTuyenDTO> data, Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu file Excel thống kê");
        fc.setSelectedFile(new File("SL_TrungTuyen_PT_Nganh_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            new ThongKeSLTrungTuyenExporter().export(data, fos);
            JOptionPane.showMessageDialog(parent, "Xuất dữ liệu thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Lỗi xuất dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void export(List<ThongKeSLTrungTuyenDTO> data, java.io.OutputStream out) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("SL Trúng Tuyển");
            sheet.setDefaultRowHeightInPoints(18);

            CellStyle titleStyle = createHeaderStyle(wb, true, 13);
            CellStyle headerStyle = createHeaderStyle(wb, false, 11);
            CellStyle dataStyle = createDataStyle(wb);

            Row rowTitle = sheet.createRow(0);
            rowTitle.setHeightInPoints(30);
            Cell cellTitle = rowTitle.createCell(0);
            cellTitle.setCellValue("THỐNG KÊ SỐ LƯỢNG TRÚNG TUYỂN THEO PHƯƠNG THỨC VÀ NGÀNH");
            cellTitle.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADERS.length - 1));

            Row rowHeader = sheet.createRow(2);
            rowHeader.setHeightInPoints(25);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = rowHeader.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 3;
            long totalAll = 0;
            for (int i = 0; i < data.size(); i++) {
                ThongKeSLTrungTuyenDTO d = data.get(i);
                Row row = sheet.createRow(rowIdx++);
                
                createCell(row, 0, String.valueOf(d.getStt()), dataStyle);
                createCell(row, 1, d.getMaNganh(), dataStyle);
                createCell(row, 2, d.getTenNganh(), dataStyle);
                createCell(row, 3, d.getPhuongThuc(), dataStyle);
                
                Cell numCell = row.createCell(4);
                numCell.setCellValue(d.getSoLuongTrungTuyen());
                numCell.setCellStyle(dataStyle);
                
                totalAll += d.getSoLuongTrungTuyen();
            }

            Row rowTotal = sheet.createRow(rowIdx);
            rowTotal.setHeightInPoints(20);
            Cell cellTextTotal = rowTotal.createCell(0);
            cellTextTotal.setCellValue("Tổng cộng");
            cellTextTotal.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 3));
            
            Cell cellNumTotal = rowTotal.createCell(4);
            cellNumTotal.setCellValue(totalAll);
            cellNumTotal.setCellStyle(headerStyle);

            for (int i = 0; i < COL_WIDTHS.length; i++) {
                sheet.setColumnWidth(i, COL_WIDTHS[i] * 256);
            }

            wb.write(out);
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook wb, boolean isMainTitle, int fontSize) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = wb.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeightInPoints((short) fontSize);
        style.setFont(font);
        if (!isMainTitle) {
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        XSSFFont font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }

    private void createCell(Row row, int col, String val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val != null ? val : "");
        cell.setCellStyle(style);
    }
}