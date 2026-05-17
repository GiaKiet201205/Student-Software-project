package com.service;

import com.dto.ThongKeSL;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThongKeSLExporter {

    /**
     * Gọi từ nút "Xuất SL trúng tuyển PT/Ngành" trong Swing panel
     */
    public static void exportWithChooser(List<ThongKeSL> data, Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu file Excel");
        fc.setSelectedFile(new File("thong_ke_sl_trung_tuyen_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))
                + ".xlsx"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx"));

        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".xlsx"))
            file = new File(file.getAbsolutePath() + ".xlsx");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            new ThongKeSLExporter().export(data, fos);
            JOptionPane.showMessageDialog(parent,
                    "Xuất Excel thành công!\n" + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Lỗi xuất Excel: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final String[] HEADERS = {
            "TT", "Mã ngành", "Tên ngành",
            "PT 3", "PT 4", "PT 5", "Tổng"
    };

    private static final int[] COL_WIDTHS = {
            6, 14, 38, 10, 10, 10, 12
    };

    public void export(List<ThongKeSL> data, OutputStream out) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("TK SL PT-Nganh");
            sheet.setDefaultRowHeightInPoints(18);

            Styles s = new Styles(wb);

            writeTitleRow(sheet, s);
            writeSubtitleRow(sheet, s);
            writeHeaderRow(sheet, s);
            writeDataRows(sheet, s, data);

            for (int i = 0; i < COL_WIDTHS.length; i++)
                sheet.setColumnWidth(i, COL_WIDTHS[i] * 256);

            sheet.createFreezePane(0, 3);

            wb.write(out);
        }
    }

    private void writeTitleRow(Sheet sheet, Styles s) {
        Row row = sheet.createRow(0);
        row.setHeightInPoints(32);
        Cell cell = row.createCell(0);
        cell.setCellValue("Thống kê số lượng theo ngành và phương thức");
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
        row.setHeightInPoints(32);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(s.header);
        }
    }

    private void writeDataRows(Sheet sheet, Styles s, List<ThongKeSL> data) {
        int rowIdx = 3;
        for (int i = 0; i < data.size(); i++, rowIdx++) {
            ThongKeSL d = data.get(i);
            Row row = sheet.createRow(rowIdx);
            row.setHeightInPoints(d.isDongTong() ? 22 : 18);

            boolean alt = (i % 2 != 0) && !d.isDongTong();

            CellStyle textSt = d.isDongTong() ? s.totalText
                    : alt            ? s.altText
                    :                  s.dataText;
            CellStyle numSt  = d.isDongTong() ? s.totalNum
                    : alt            ? s.altNum
                    :                  s.dataNum;

            setCell(row, 0, d.isDongTong() ? "" : String.valueOf(d.getStt()), textSt);
            setCell(row, 1, d.getMaNganh(), textSt);
            setCell(row, 2, d.getTenNganh(), textSt);
            setNum(row, 3, d.getSoLuongPT3(), numSt);
            setNum(row, 4, d.getSoLuongPT4(), numSt);
            setNum(row, 5, d.getSoLuongPT5(), numSt);
            setNum(row, 6, d.getSoLuongTong(), numSt);
        }
    }

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

    private static class Styles {
        CellStyle title, subtitle, header;
        CellStyle dataText, dataNum;
        CellStyle altText, altNum;
        CellStyle totalText, totalNum;

        private static final String DARK_BLUE = "1F4E79";
        private static final String MID_BLUE = "2E75B6";
        private static final String LIGHT_BLUE = "DEEAF1";
        private static final String ALT_BLUE = "EBF3FB";
        private static final String TOTAL_BG = "BDD7EE";
        private static final String WHITE = "FFFFFF";

        Styles(XSSFWorkbook wb) {
            DataFormat fmt = wb.createDataFormat();
            short numFmt = fmt.getFormat("#,##0");

            title = buildTitle(wb);
            subtitle = buildSubtitle(wb);
            header = buildHeader(wb);
            dataText = buildText(wb, WHITE, false);
            dataNum = buildNum(wb, WHITE, false, numFmt);
            altText = buildText(wb, ALT_BLUE, false);
            altNum = buildNum(wb, ALT_BLUE, false, numFmt);
            totalText = buildText(wb, TOTAL_BG, true);
            totalNum = buildNum(wb, TOTAL_BG, true, numFmt);
        }

        private CellStyle buildTitle(XSSFWorkbook wb) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(DARK_BLUE), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.CENTER);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFFont f = wb.createFont();
            f.setFontName("Arial");
            f.setBold(true);
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
            f.setFontName("Arial");
            f.setItalic(true);
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
            f.setFontName("Arial");
            f.setBold(true);
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
            f.setFontName("Arial");
            f.setBold(bold);
            f.setFontHeightInPoints((short) 10);
            st.setFont(f);
            return st;
        }

        private CellStyle buildNum(XSSFWorkbook wb, String bg, boolean bold, short numFmt) {
            XSSFCellStyle st = wb.createCellStyle();
            st.setFillForegroundColor(new XSSFColor(hexToBytes(bg), null));
            st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            st.setAlignment(HorizontalAlignment.RIGHT);
            st.setVerticalAlignment(VerticalAlignment.CENTER);
            st.setDataFormat(numFmt);
            setBorder(st, IndexedColors.PALE_BLUE);
            XSSFFont f = wb.createFont();
            f.setFontName("Arial");
            f.setBold(bold);
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
