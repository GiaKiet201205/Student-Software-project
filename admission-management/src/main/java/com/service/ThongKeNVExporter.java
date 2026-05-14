package com.service;

import com.dto.ThongKeNVDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThongKeNVExporter {

    // ===================== PUBLIC ENTRY POINT =====================

    /**
     * Gọi từ nút "Xuất Excel" trong Swing panel
     */
    public static void exportWithChooser(List<ThongKeNVDTO> data, Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu file Excel");
        fc.setSelectedFile(new File("thong_ke_nguyen_vong_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))
                + ".xlsx"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx"));

        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx"))
            file = new File(file.getAbsolutePath() + ".xlsx");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            new ThongKeNVExporter().export(data, fos);
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
            "TT", "Mã xét tuyển", "Tên mã xét tuyển",
            "Chỉ tiêu\n2025", "Tổng NV\n1-5",
            "NV 1", "NV 2", "NV 3", "NV 4", "NV 5", ">5",
            "Tổng tất cả NV"
    };

    private static final int[] COL_WIDTHS = {
            6, 15, 38, 11, 11, 8, 8, 8, 8, 8, 8, 15
    };

    public void export(List<ThongKeNVDTO> data, OutputStream out) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Thống kê NV");
            sheet.setDefaultRowHeightInPoints(18);

            // Tạo styles
            Styles s = new Styles(wb);

            // Dòng 0: Tiêu đề chính
            writeTitleRow(sheet, s, wb);

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

    private void writeTitleRow(Sheet sheet, Styles s, XSSFWorkbook wb) {
        Row row = sheet.createRow(0);
        row.setHeightInPoints(32);
        Cell cell = row.createCell(0);
        cell.setCellValue("THỐNG KÊ NGUYỆN VỌNG XÉT TUYỂN THEO NGÀNH - NĂM 2025");
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

    private void writeDataRows(Sheet sheet, Styles s, List<ThongKeNVDTO> data) {
        int rowIdx = 3;
        for (int i = 0; i < data.size(); i++, rowIdx++) {
            ThongKeNVDTO d = data.get(i);
            Row row = sheet.createRow(rowIdx);
            row.setHeightInPoints(d.isDongTong() ? 22 : 18);

            boolean alt = (i % 2 != 0) && !d.isDongTong();

            // Chọn style theo loại dòng
            CellStyle textSt = d.isDongTong() ? s.totalText
                    : alt            ? s.altText
                      :                  s.dataText;
            CellStyle numSt  = d.isDongTong() ? s.totalNum
                    : alt            ? s.altNum
                      :                  s.dataNum;

            // TT
            setCell(row, 0, d.isDongTong() ? "" : String.valueOf(d.getStt()), textSt);
            // Mã, Tên
            setCell(row, 1, d.getMaXetTuyen(),    textSt);
            setCell(row, 2, d.getTenMaXetTuyen(), textSt);
            // Số liệu
            setNum(row,  3, d.getChiTieu2025(), numSt);
            setNum(row,  4, d.getTongNV15(),    numSt);
            setNum(row,  5, d.getNv1(),         numSt);
            setNum(row,  6, d.getNv2(),         numSt);
            setNum(row,  7, d.getNv3(),         numSt);
            setNum(row,  8, d.getNv4(),         numSt);
            setNum(row,  9, d.getNv5(),         numSt);
            setNum(row, 10, d.getNvLon5(),      numSt);
            setNum(row, 11, d.getTongTatCaNV(), numSt);
        }
    }

    // ===================== CELL HELPERS =====================

    private void setCell(Row row, int col, String val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val == null ? "" : val);
        c.setCellStyle(style);
    }

    private void setNum(Row row, int col, long val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val);
        c.setCellStyle(style);
    }

    // ===================== STYLES (inner class) =====================

    private static class Styles {
        CellStyle title, subtitle, header;
        CellStyle dataText, dataNum;
        CellStyle altText,  altNum;
        CellStyle totalText, totalNum;

        // Màu sắc
        private static final String DARK_BLUE  = "1F4E79";
        private static final String MID_BLUE   = "2E75B6";
        private static final String LIGHT_BLUE = "DEEAF1";
        private static final String ALT_BLUE   = "EBF3FB";
        private static final String TOTAL_BG   = "BDD7EE";
        private static final String WHITE      = "FFFFFF";

        Styles(XSSFWorkbook wb) {
            DataFormat fmt = wb.createDataFormat();
            short numFmt   = fmt.getFormat("#,##0");

            title     = buildTitle(wb);
            subtitle  = buildSubtitle(wb);
            header    = buildHeader(wb);
            dataText  = buildText(wb, WHITE,     false);
            dataNum   = buildNum (wb, WHITE,     false, numFmt);
            altText   = buildText(wb, ALT_BLUE,  false);
            altNum    = buildNum (wb, ALT_BLUE,  false, numFmt);
            totalText = buildText(wb, TOTAL_BG,  true);
            totalNum  = buildNum (wb, TOTAL_BG,  true,  numFmt);
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

        private CellStyle buildNum(XSSFWorkbook wb, String bg,
                                   boolean bold, short numFmt) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(bg), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.RIGHT);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            st.setDataFormat(numFmt);
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