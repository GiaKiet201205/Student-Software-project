package com.service.NVXTImport;

import com.controller.NganhToHopController;
import com.controller.NguyenVongXetTuyenController;
import com.entity.NganhToHop;
import com.entity.NguyenVongXetTuyen;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service xuất điểm chuẩn trực tiếp từ database
 */
public class DiemChuanExportService {

    private final NguyenVongXetTuyenController nguyenVongController;
    private final NganhToHopController nganhToHopController;

    public DiemChuanExportService() {
        this.nguyenVongController = new NguyenVongXetTuyenController();
        this.nganhToHopController = new NganhToHopController();
    }

    /**
     * Xuất file điểm chuẩn
     * @param outputFile File đầu ra
     * @throws Exception Nếu không có dữ liệu hoặc lỗi ghi file
     */
    public void exportDiemChuan(File outputFile) throws Exception {
        // 1. Lấy danh sách thí sinh trúng tuyển (YES) từ database
        List<NguyenVongXetTuyen> allRecords = nguyenVongController.getNguyenVongXetTuyen();
        
        List<NguyenVongXetTuyen> trungTuyen = allRecords.stream()
                .filter(nv -> "YES".equals(nv.getKetQua()))
                .collect(Collectors.toList());
        
        if (trungTuyen.isEmpty()) {
            throw new Exception("Không có dữ liệu thí sinh trúng tuyển! Vui lòng chạy xét tuyển trước.");
        }
        
        // 2. Tính điểm chuẩn cho từng ngành (điểm thấp nhất trong số thí sinh trúng tuyển)
        Map<String, BigDecimal> diemChuanMap = new HashMap<>();
        Map<String, List<NguyenVongXetTuyen>> trungTuyenTheoNganh = trungTuyen.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getMaNganh));
        
        for (Map.Entry<String, List<NguyenVongXetTuyen>> entry : trungTuyenTheoNganh.entrySet()) {
            String maNganh = entry.getKey();
            BigDecimal diemThapNhat = entry.getValue().stream()
                    .map(NguyenVongXetTuyen::getDiemXetTuyen)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            diemChuanMap.put(maNganh, diemThapNhat);
        }
        
        // 3. Lấy danh sách tổ hợp của từng ngành từ bảng xt_nganh_tohop
        List<NganhToHop> allNganhToHop = nganhToHopController.getAll();
        Map<String, List<String>> toHopTheoNganh = allNganhToHop.stream()
                .collect(Collectors.groupingBy(
                    NganhToHop::getMaNganh,
                    Collectors.mapping(NganhToHop::getMaToHop, Collectors.toList())
                ));
        
        // 4. Tạo file Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Điểm chuẩn 2025");
            
            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Mã ngành", "Các tổ hợp xét tuyển", "Điểm chuẩn"};
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Dữ liệu (sắp xếp theo mã ngành)
            List<Map.Entry<String, BigDecimal>> sorted = diemChuanMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList());
            
            CellStyle dataStyle = createDataStyle(workbook);
            int rowNum = 1;
            
            for (Map.Entry<String, BigDecimal> entry : sorted) {
                String maNganh = entry.getKey();
                Row row = sheet.createRow(rowNum);
                
                row.createCell(0).setCellValue(rowNum);
                row.createCell(1).setCellValue(maNganh);
                
                List<String> toHops = toHopTheoNganh.getOrDefault(maNganh, new ArrayList<>());
                String toHopString = toHops.isEmpty() ? "" : String.join(", ", toHops);
                row.createCell(2).setCellValue(toHopString);
                
                row.createCell(3).setCellValue(entry.getValue().doubleValue());
                
                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
                
                rowNum++;
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }
        }
        
        System.out.println("✓ Đã xuất điểm chuẩn thành công: " + outputFile.getAbsolutePath());
        System.out.println("  - Số ngành có điểm chuẩn: " + diemChuanMap.size());
        System.out.println("  - Số thí sinh trúng tuyển: " + trungTuyen.size());
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}