package com.service;

import com.dto.ThiSinhTrungTuyenDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThiSinhTrungTuyenNganhExporter {

    // ===================== PUBLIC ENTRY POINT =====================

    /**
     * Gọi từ nút "Xuất DS trúng tuyển theo ngành" trong Swing panel
     */
    public static void exportWithChooser(List<ThiSinhTrungTuyenDTO> data, String tenNganh, Component parent) {
        String sanitizedName = sanitizeFileName(tenNganh);
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu file Excel danh sách thí sinh trúng tuyển");
        fc.setSelectedFile(new File("DS_TrungTuyen_" + sanitizedName + "_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))
                + ".xlsx"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx"));

        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx"))
            file = new File(file.getAbsolutePath() + ".xlsx");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            new ThiSinhTrungTuyenNganhExporter().export(data, tenNganh, fos);
            JOptionPane.showMessageDialog(parent,
                    "Xuất Excel thành công!\n" + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Lỗi xuất Excel: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== CORE EXPORT =====================

    private static final String[] HEADERS = {
            "TT", "CCCD", "Họ", "Tên", "Ngày sinh", "Email", "Số điện thoại", "Giới tính", "Nơi sinh"
    };

    private static final int[] COL_WIDTHS = {
            5, 15, 30, 30, 15, 20, 15, 12, 30
    };

    public void export(List<ThiSinhTrungTuyenDTO> data, String tenNganh, OutputStream out) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("DS Trúng tuyển");
            sheet.setDefaultRowHeightInPoints(18);

            // Tạo styles
            Styles s = new Styles(wb);

            // Dòng 0: Tiêu đề chính
            writeTitleRow(sheet, s, tenNganh);

            // Dòng 1: Ngày xuất
            writeSubtitleRow(sheet, s);

            // Dòng 2: Header cột
            writeHeaderRow(sheet, s);

            // Dòng 3+: Dữ liệu
            writeDataRows(sheet, s, data);

            // Độ rộng cột
            for (int i = 0; i < COL_WIDTHS.length; i++)
                sheet.setColumnWidth(i, COL_WIDTHS[i] * 256);

            // Freeze header
            sheet.createFreezePane(0, 3);

            wb.write(out);
        }
    }

    // ===================== WRITE ROWS =====================

    private void writeTitleRow(Sheet sheet, Styles s, String tenNganh) {
        Row row = sheet.createRow(0);
        row.setHeightInPoints(32);
        Cell cell = row.createCell(0);
        cell.setCellValue("DANH SÁCH THÍ SINH TRÚNG TUYỂN - NGÀNH: " + tenNganh + " - NĂM 2025");
        cell.setCellStyle(s.title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADERS.length - 1));
    }

    private void writeSubtitleRow(Sheet sheet, Styles s) {
        Row row = sheet.createRow(1);
        row.setHeightInPoints(16);
        Cell cell = row.createCell(0);
        cell.setCellValue("Ngày xuất: "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cell.setCellStyle(s.subtitle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, HEADERS.length - 1));
    }

    private void writeHeaderRow(Sheet sheet, Styles s) {
        Row row = sheet.createRow(2);
        row.setHeightInPoints(40);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(s.header);
        }
    }

    private void writeDataRows(Sheet sheet, Styles s, List<ThiSinhTrungTuyenDTO> data) {
        int rowIdx = 3;
        for (int i = 0; i < data.size(); i++, rowIdx++) {
            ThiSinhTrungTuyenDTO d = data.get(i);
            Row row = sheet.createRow(rowIdx);
            row.setHeightInPoints(18);

            boolean alt = (i % 2 != 0);

            // Chọn style theo dòng
            CellStyle textSt = alt ? s.altText : s.dataText;

            // Ghi dữ liệu
            setCell(row, 0, String.valueOf(i + 1), textSt);  // TT
            setCell(row, 1, d.getCccd(), textSt);            // CCCD
            setCell(row, 2, d.getHo(), textSt);              // Họ
            setCell(row, 3, d.getTen(), textSt);             // Tên
            setCell(row, 4, d.getNgaySinh(), textSt);        // Ngày sinh
            setCell(row, 5, d.getEmail(), textSt);           // Email
            setCell(row, 6, d.getDienThoai(), textSt);       // Số điện thoại
            setCell(row, 7, d.getGioiTinh(), textSt);        // Giới tính
            setCell(row, 8, d.getNoiSinh(), textSt);         // Nơi sinh
        }
    }

    // ===================== CELL HELPERS =====================

    private void setCell(Row row, int col, String val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val == null ? "" : val);
        c.setCellStyle(style);
    }

    // ===================== HELPER METHODS =====================

    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    // ===================== STYLES (inner class) =====================

    private static class Styles {
        CellStyle title, subtitle, header;
        CellStyle dataText, altText;

        // Màu sắc
        private static final String DARK_BLUE  = "1F4E79";
        private static final String MID_BLUE   = "2E75B6";
        private static final String LIGHT_BLUE = "DEEAF1";
        private static final String ALT_BLUE   = "EBF3FB";
        private static final String WHITE      = "FFFFFF";

        Styles(XSSFWorkbook wb) {
            title     = buildTitle(wb);
            subtitle  = buildSubtitle(wb);
            header    = buildHeader(wb);
            dataText  = buildText(wb, WHITE,     false);
            altText   = buildText(wb, ALT_BLUE,  false);
        }

        private CellStyle buildTitle(XSSFWorkbook wb) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(DARK_BLUE), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.CENTER);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFFont f = wb.createFont();
            f.setFontName("Arial"); f.setBold(true);
            f.setFontHeightInPoints((short) 13);
            f.setColor(new XSSFColor(hexToBytes(WHITE), null));
            st.setFont(f);
            return st;
        }

        private CellStyle buildSubtitle(XSSFWorkbook wb) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(LIGHT_BLUE), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.CENTER);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFFont f = wb.createFont();
            f.setFontName("Arial"); f.setItalic(true);
            f.setFontHeightInPoints((short) 9);
            st.setFont(f);
            return st;
        }

        private CellStyle buildHeader(XSSFWorkbook wb) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(MID_BLUE), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.CENTER);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            st.setWrapText(true);
            setBorder(st, IndexedColors.WHITE);
            XSSFFont f = wb.createFont();
            f.setFontName("Arial"); f.setBold(true);
            f.setFontHeightInPoints((short) 10);
            f.setColor(new XSSFColor(hexToBytes(WHITE), null));
            st.setFont(f);
            return st;
        }

        private CellStyle buildText(XSSFWorkbook wb, String bg, boolean bold) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(bg), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.LEFT);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorder(st, IndexedColors.PALE_BLUE);
            XSSFFont f = wb.createFont();
            f.setFontName("Arial"); f.setBold(bold);
            f.setFontHeightInPoints((short) 10);
            st.setFont(f);
            return st;
        }

        private void setBorder(XSSFCellStyle st, IndexedColors color) {
            st.setBorderTop(BorderStyle.THIN);
            st.setBorderBottom(BorderStyle.THIN);
            st.setBorderLeft(BorderStyle.THIN);
            st.setBorderRight(BorderStyle.THIN);
            st.setTopBorderColor(color.getIndex());
            st.setBottomBorderColor(color.getIndex());
            st.setLeftBorderColor(color.getIndex());
            st.setRightBorderColor(color.getIndex());
        }

        private byte[] hexToBytes(String hex) {
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new byte[]{(byte) r, (byte) g, (byte) b};
        }
    }
}
