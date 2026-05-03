package com.service;

import com.controller.DiemCongXetTuyenController;
import com.entity.DiemCongXetTuyen;
import com.repository.DiemCongXetTuyenRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelImportDCXTService {

    // ================== MODEL TẠM ==================
    static class ThiSinh {
        String cccd;
        String khuVuc;
        String doiTuong;
        Map<String, Double> diemMon = new HashMap<>();

        public ThiSinh(String cccd, String khuVuc, String doiTuong) {
            this.cccd = cccd;
            this.khuVuc = khuVuc;
            this.doiTuong = doiTuong;
        }
    }

    // ================== READ FILE ==================

    private Map<String, ThiSinh> readThiSinh(File file) {
        Map<String, ThiSinh> map = new HashMap<>();        
        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = new XSSFWorkbook(fis)) {
            
            Sheet sheet = wb.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> header = getHeaderMap(headerRow);
            
            int count = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String raw = getCellString(row, header, "CCCD");
                String cccd = normalizeCCCD(raw);
                if (cccd == null || cccd.isEmpty()) continue;
                
                String kv = getCellString(row, header, "KVƯT");
                String dt = getCellString(row, header, "ĐTƯT");
                
                ThiSinh ts = new ThiSinh(cccd, kv, dt);
                
                // Điểm các môn văn hóa
                putIfNotNull(ts.diemMon, "TO", getCellDouble(row, header, "TO"));
                putIfNotNull(ts.diemMon, "LI", getCellDouble(row, header, "LI"));
                putIfNotNull(ts.diemMon, "HO", getCellDouble(row, header, "HO"));
                putIfNotNull(ts.diemMon, "VA", getCellDouble(row, header, "VA"));
                putIfNotNull(ts.diemMon, "SU", getCellDouble(row, header, "SU"));
                putIfNotNull(ts.diemMon, "DI", getCellDouble(row, header, "DI"));
                putIfNotNull(ts.diemMon, "GDCD", getCellDouble(row, header, "GDCD"));
                putIfNotNull(ts.diemMon, "NN", getCellDouble(row, header, "NN"));
                
                // Điểm năng khiếu
                putIfNotNull(ts.diemMon, "NK1", getCellDouble(row, header, "NK1"));
                putIfNotNull(ts.diemMon, "NK2", getCellDouble(row, header, "NK2"));
                putIfNotNull(ts.diemMon, "NK3", getCellDouble(row, header, "NK3"));
                putIfNotNull(ts.diemMon, "NK4", getCellDouble(row, header, "NK4"));
                putIfNotNull(ts.diemMon, "NK5", getCellDouble(row, header, "NK5"));
                putIfNotNull(ts.diemMon, "NK6", getCellDouble(row, header, "NK6"));
                
                // Điểm các môn khác
                putIfNotNull(ts.diemMon, "KTPL", getCellDouble(row, header, "KTPL"));
                putIfNotNull(ts.diemMon, "TI", getCellDouble(row, header, "TI"));
                putIfNotNull(ts.diemMon, "CNCN", getCellDouble(row, header, "CNCN"));
                putIfNotNull(ts.diemMon, "CNNN", getCellDouble(row, header, "CNNN"));
                
                map.put(cccd, ts);
                count++;
                
            }
            System.out.println("Tổng số thí sinh đọc được: " + count);            
        } catch (Exception e) {
            e.printStackTrace();
        }       
        return map;
    }

    private void putIfNotNull(Map<String, Double> map, String key, Double val) {
        if (val != null) {
            map.put(key, val);
        }
    }

    private Map<String, Double> readUuTien(File file) {
        Map<String, Double> map = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> header = getHeaderMap(headerRow);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String raw = getCellString(row, header, "CCCD");
                String cccd = normalizeCCCD(raw);

                Double diem = getCellDouble(row, header, "Điểm cộng cho môn đạt giải");
                if (cccd != null && diem != null) {
                    map.put(cccd.trim(), diem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map<String, List<String>> readNguyenVong(File file) {
        Map<String, List<String>> map = new HashMap<>();       
        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = new XSSFWorkbook(fis)) {
            
            DataFormatter formatter = new DataFormatter();            
            for (int sheetIdx = 0; sheetIdx < wb.getNumberOfSheets(); sheetIdx++) {
                Sheet sheet = wb.getSheetAt(sheetIdx);
                String sheetName = sheet.getSheetName();
                
                if (sheetName.toLowerCase().contains("tk") || sheetName.toLowerCase().contains("thống kê")) {
                    System.out.println("Bỏ qua sheet: " + sheetName);
                    continue;
                }
                
                System.out.println("Đang đọc sheet: " + sheetName);
                
                int headerRowIndex = -1;
                for (int i = 0; i <= 10; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    for (Cell cell : row) {
                        String value = formatter.formatCellValue(cell);
                        if ("CCCD".equalsIgnoreCase(value.trim())) {
                            headerRowIndex = i;
                            break;
                        }
                    }
                    if (headerRowIndex != -1) break;
                }
                
                if (headerRowIndex == -1) {
                    System.out.println("Không tìm thấy header trong sheet: " + sheetName);
                    continue;
                }
                
                Row headerRow = sheet.getRow(headerRowIndex);
                int colCccd = -1;
                int colMaNganh = -1;
                
                for (Cell cell : headerRow) {
                    String value = formatter.formatCellValue(cell).trim();
                    if ("CCCD".equalsIgnoreCase(value)) {
                        colCccd = cell.getColumnIndex();
                    } else if ("Mã xét tuyển".equalsIgnoreCase(value)) {
                        colMaNganh = cell.getColumnIndex();
                    }
                }
                
                int count = 0;
                for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    String rawCccd = formatter.formatCellValue(row.getCell(colCccd));
                    String maNganh = formatter.formatCellValue(row.getCell(colMaNganh));
                    
                    if (rawCccd == null || rawCccd.trim().isEmpty()) continue;
                    if (maNganh == null || maNganh.trim().isEmpty()) continue;
                    
                    String cccd = normalizeCCCD(rawCccd);
                    maNganh = maNganh.trim();
                                        
                    map.computeIfAbsent(cccd, k -> new ArrayList<>()).add(maNganh);
                    count++;
                }                
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }                
        return map;
    }

    private String[] parseToHop(String raw) {
        try {
            int start = raw.indexOf("(");           
            if (start == -1) {
                System.out.println("WARN: Không tìm thấy '(' trong: " + raw);
                return new String[0];
            }          
            String maToHop = raw.substring(0, start).trim();           
            return new String[]{maToHop};
            
        } catch (Exception e) {
            System.out.println("ERROR parseToHop: " + raw);
            return new String[0];
        }
    }

    private Map<String, List<String[]>> readToHop(File file) {
        Map<String, List<String[]>> map = new HashMap<>();
        
        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = new XSSFWorkbook(fis)) {           
            Sheet sheet = wb.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> header = getHeaderMap(headerRow);
            
            int count = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String maNganh = getCellString(row, header, "MANGANH");
                String rawToHop = getCellString(row, header, "MA_TO_HOP");
                
                if (maNganh == null || rawToHop == null) continue;
                
                String[] toHop = parseToHop(rawToHop);
                
                if (toHop.length == 0) continue;
                
                map.computeIfAbsent(maNganh, k -> new ArrayList<>()).add(toHop);
                count++;
            }
            
            System.out.println("Tổng số tổ hợp đọc được: " + count);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return map;
    }

    // ================== LOGIC ==================

    private String normalizeCCCD(String cccd) {
        if (cccd == null) return null;
        return cccd.replace("TS_", "").trim();
    }

    private Map<String, Integer> getHeaderMap(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : headerRow) {
            String name = cell.getStringCellValue()
                    .trim()
                    .toLowerCase()
                    .replaceAll("\\s+", ""); 
            map.put(name, cell.getColumnIndex());
        }
        return map;
    }

    private String getCellString(Row row, Map<String, Integer> header, String colName) {
        String key = colName.toLowerCase().replaceAll("\\s+", "");
        Integer idx = header.get(key);
        if (idx == null) return null;

        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        return new DataFormatter().formatCellValue(cell).trim();
    }

    private Double getCellDouble(Row row, Map<String, Integer> header, String colName) {
        try {
            String val = getCellString(row, header, colName);
            return (val == null || val.isEmpty()) ? null : Double.parseDouble(val);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidToHop(String[] toHop, Map<String, Double> diemMon) {
        String[] requiredMons = getMonListForToHop(toHop[0]);

        if (requiredMons == null) {
            return false;
        }

        for (String mon : requiredMons) {
            Double diem = diemMon.get(mon);
            if (diem == null || diem <= 0) {
                return false; 
            }
        }
        return true;
    }

    private String[] getMonListForToHop(String maToHop) {
        Map<String, String[]> mapping = new HashMap<>();

        // Các tổ hợp A00, A01,...
        mapping.put("A00", new String[]{"TO", "LI", "HO"});
        mapping.put("A01", new String[]{"TO", "LI", "NN"});
        mapping.put("A02", new String[]{"TO", "LI", "SI"});
        mapping.put("A03", new String[]{"TO", "LI", "SU"});
        mapping.put("A04", new String[]{"TO", "LI", "DI"});
        mapping.put("A05", new String[]{"TO", "HO", "SU"});
        mapping.put("A06", new String[]{"TO", "HO", "DI"});
        mapping.put("A07", new String[]{"TO", "SU", "DI"});

        // Các tổ hợp B00, B01,...
        mapping.put("B00", new String[]{"TO", "HO", "SI"});
        mapping.put("B01", new String[]{"TO", "SI", "SU"});
        mapping.put("B02", new String[]{"TO", "SI", "DI"});
        mapping.put("B03", new String[]{"TO", "VA", "SI"});
        mapping.put("B08", new String[]{"TO", "SI", "NN"});

        // Các tổ hợp C00, C01,...
        mapping.put("C00", new String[]{"VA", "SU", "DI"});
        mapping.put("C01", new String[]{"TO", "VA", "LI"});
        mapping.put("C02", new String[]{"TO", "VA", "HO"});
        mapping.put("C03", new String[]{"TO", "VA", "SU"});
        mapping.put("C04", new String[]{"TO", "VA", "DI"});
        mapping.put("C05", new String[]{"VA", "LI", "HO"});
        mapping.put("C06", new String[]{"VA", "LI", "SI"});
        mapping.put("C07", new String[]{"VA", "LI", "SU"});
        mapping.put("C08", new String[]{"VA", "HO", "SI"});
        mapping.put("C09", new String[]{"VA", "LI", "DI"});
        mapping.put("C10", new String[]{"VA", "HO", "SU"});
        mapping.put("C11", new String[]{"VA", "HO", "DI"});
        mapping.put("C12", new String[]{"VA", "SI", "SU"});
        mapping.put("C13", new String[]{"VA", "SI", "DI"});

        // Các tổ hợp D01, D07,...
        mapping.put("D01", new String[]{"TO", "VA", "NN"});
        mapping.put("D07", new String[]{"TO", "HO", "NN"});
        mapping.put("D09", new String[]{"TO", "SU", "NN"});
        mapping.put("D10", new String[]{"TO", "DI", "NN"});
        mapping.put("D11", new String[]{"VA", "LI", "NN"});
        mapping.put("D12", new String[]{"VA", "HO", "NN"});
        mapping.put("D13", new String[]{"VA", "SI", "NN"});
        mapping.put("D14", new String[]{"VA", "SU", "NN"});
        mapping.put("D15", new String[]{"VA", "DI", "NN"});

        // Các tổ hợp M01, M02 (năng khiếu)
        mapping.put("M01", new String[]{"NK1", "NK2", "VA"});
        mapping.put("M02", new String[]{"NK1", "TO", "NK2"});

        // Các tổ hợp H00 (mỹ thuật)
        mapping.put("H00", new String[]{"VA", "NK3", "NK4"});

        // Các tổ hợp N01 (âm nhạc)
        mapping.put("N01", new String[]{"VA", "NK5", "NK6"});

        // Các tổ hợp X (tổ hợp tự do)
        mapping.put("X01", new String[]{"TO", "VA", "KTPL"});
        mapping.put("X02", new String[]{"TO", "VA", "TI"});
        mapping.put("X03", new String[]{"TO", "VA", "CNCN"});
        mapping.put("X04", new String[]{"TO", "VA", "CNNN"});
        mapping.put("X05", new String[]{"TO", "LI", "KTPL"});
        mapping.put("X06", new String[]{"TO", "LI", "TI"});
        mapping.put("X07", new String[]{"TO", "LI", "CNCN"});
        mapping.put("X08", new String[]{"TO", "LI", "CNNN"});
        mapping.put("X09", new String[]{"TO", "HO", "KTPL"});
        mapping.put("X10", new String[]{"TO", "HO", "TI"});
        mapping.put("X11", new String[]{"TO", "HO", "CNCN"});
        mapping.put("X12", new String[]{"TO", "HO", "CNNN"});
        mapping.put("X13", new String[]{"TO", "SI", "KTPL"});
        mapping.put("X14", new String[]{"TO", "SI", "TI"});
        mapping.put("X15", new String[]{"TO", "SI", "CNCN"});
        mapping.put("X16", new String[]{"TO", "SI", "CNNN"});
        mapping.put("X17", new String[]{"TO", "SU", "KTPL"});
        mapping.put("X18", new String[]{"TO", "SU", "TI"});
        mapping.put("X19", new String[]{"TO", "SU", "CNCN"});
        mapping.put("X20", new String[]{"TO", "SU", "CNNN"});
        mapping.put("X21", new String[]{"TO", "DI", "KTPL"});
        mapping.put("X22", new String[]{"TO", "DI", "TI"});
        mapping.put("X23", new String[]{"TO", "DI", "CNCN"});
        mapping.put("X24", new String[]{"TO", "DI", "CNNN"});
        mapping.put("X25", new String[]{"TO", "NN", "KTPL"});
        mapping.put("X26", new String[]{"TO", "NN", "TI"});
        mapping.put("X27", new String[]{"TO", "NN", "CNCN"});
        mapping.put("X28", new String[]{"TO", "NN", "CNNN"});
        mapping.put("X53", new String[]{"TO", "KTPL", "TI"});
        mapping.put("X54", new String[]{"TO", "KTPL", "CNCN"});
        mapping.put("X55", new String[]{"TO", "KTPL", "CNNN"});
        mapping.put("X56", new String[]{"TO", "TI", "CNCN"});
        mapping.put("X57", new String[]{"TO", "TI", "CNNN"});
        mapping.put("X58", new String[]{"VA", "LI", "KTPL"});
        mapping.put("X59", new String[]{"VA", "LI", "TI"});
        mapping.put("X60", new String[]{"VA", "LI", "CNCN"});
        mapping.put("X61", new String[]{"VA", "LI", "CNNN"});
        mapping.put("X62", new String[]{"VA", "HO", "KTPL"});
        mapping.put("X63", new String[]{"VA", "HO", "TI"});
        mapping.put("X64", new String[]{"VA", "HO", "CNCN"});
        mapping.put("X65", new String[]{"VA", "HO", "CNNN"});
        mapping.put("X66", new String[]{"VA", "SI", "KTPL"});
        mapping.put("X67", new String[]{"VA", "SI", "TI"});
        mapping.put("X68", new String[]{"VA", "SI", "CNCN"});
        mapping.put("X69", new String[]{"VA", "SI", "CNNN"});
        mapping.put("X70", new String[]{"VA", "SU", "KTPL"});
        mapping.put("X71", new String[]{"VA", "SU", "TI"});
        mapping.put("X72", new String[]{"VA", "SU", "CNCN"});
        mapping.put("X73", new String[]{"VA", "SU", "CNNN"});
        mapping.put("X74", new String[]{"VA", "DI", "KTPL"});
        mapping.put("X75", new String[]{"VA", "DI", "TI"});
        mapping.put("X76", new String[]{"VA", "DI", "CNCN"});
        mapping.put("X77", new String[]{"VA", "DI", "CNNN"});
        mapping.put("X78", new String[]{"VA", "NN", "KTPL"});
        mapping.put("X79", new String[]{"VA", "NN", "TI"});
        mapping.put("X80", new String[]{"VA", "NN", "CNCN"});
        mapping.put("X81", new String[]{"VA", "NN", "CNNN"});

        return mapping.get(maToHop);
    }

    private double tinhDiemUuTien(String kv, String dt) {
        double diemKV = 0;
        if (kv != null) {
            switch (kv.trim()) {
                case "1":
                    diemKV = 0.75;
                    break;
                case "2NT":
                    diemKV = 0.5;
                    break;
                case "2":
                    diemKV = 0.25;
                    break;
                case "3":
                    diemKV = 0;
                    break;
                default:
                    diemKV = 0;
                    break;
            }
        }
        
        double diemDT = 0;
        if (dt != null) {
            switch (dt.trim()) {
                case "01":
                case "02":
                case "03":
                case "04":
                case "05":
                    diemDT = 2;
                    break;
                case "06":
                case "07":
                    diemDT = 1;
                    break;
                default:
                    diemDT = 0;
                    break;
            }
        }
        
        double tong = diemKV + diemDT;
        return Math.min(tong, 2.75);
    }

    // ==================== TỐI ƯU: SỬ DỤNG BATCH INSERT ====================
    
    /**
     * Import điểm cộng xét tuyển với batch insert tối ưu
     */
    public int processAllWithProgress(
            File fThiSinh,
            File fUT,
            File fNV,
            File fToHop,
            java.util.function.Consumer<Integer> progressCallback,
            DiemCongXetTuyenController controller
    ) throws Exception {
        
        System.gc();
        
        Map<String, ThiSinh> mapTS = null;
        Map<String, Double> mapUT = null;
        Map<String, List<String>> mapNV = null;
        Map<String, List<String[]>> mapTH = null;
        
        try {
            if (progressCallback != null) progressCallback.accept(10);
            mapTH = readToHop(fToHop);
            System.gc();
            
            if (progressCallback != null) progressCallback.accept(25);
            mapUT = readUuTien(fUT);
            System.gc();
            
            if (progressCallback != null) progressCallback.accept(40);
            mapTS = readThiSinh(fThiSinh);
            System.gc();
            
            if (progressCallback != null) progressCallback.accept(60);
            mapNV = readNguyenVong(fNV);
            System.gc();
            
        } catch (OutOfMemoryError e) {
            System.gc();
            throw new Exception("Không đủ bộ nhớ!");
        }
        
        int totalSaved = 0;
        int batchSize = 2000; // Tăng batch size lên 2000
        List<DiemCongXetTuyen> batch = new ArrayList<>(batchSize);
        
        int totalCCCD = mapNV.size();
        int processed = 0;
        
        System.out.println("Bắt đầu xử lý và lưu vào database...");
        
        // Xóa dữ liệu cũ trước khi import (tùy chọn)
        // controller.truncateAll(); // Nếu muốn xóa hết dữ liệu cũ
        
        DiemCongXetTuyenRepository repo = new DiemCongXetTuyenRepository();
        
        for (String cccd : mapNV.keySet()) {
            ThiSinh ts = mapTS.get(cccd);
            if (ts == null) continue;
            
            double diemUuTien = tinhDiemUuTien(ts.khuVuc, ts.doiTuong);
            
            Double diemChungChi = mapUT.get(cccd);
            if (diemChungChi == null) diemChungChi = 0.0;

            if (diemUuTien == 0 && diemChungChi == 0) continue;
            
            double diemTong = diemUuTien + diemChungChi;
            
            for (String maNganh : mapNV.get(cccd)) {
                List<String[]> toHops = mapTH.get(maNganh);
                if (toHops == null) continue;
                
                for (String[] th : toHops) {
                    if (!isValidToHop(th, ts.diemMon)) continue;
                    
                    DiemCongXetTuyen dc = new DiemCongXetTuyen();
                    dc.setCccd(cccd);
                    dc.setMaNganh(maNganh);
                    dc.setMaToHop(th[0]);
                    dc.setDiemCC(BigDecimal.valueOf(diemChungChi));
                    dc.setDiemUuTienXT(BigDecimal.valueOf(diemUuTien));
                    dc.setDiemTong(BigDecimal.valueOf(diemTong));
                    dc.setPhuongThuc(null);
                    dc.setGhiChu(null);
                    dc.setDcKeys(cccd + "_" + maNganh + "_" + th[0]);
                    
                    batch.add(dc);
                    totalSaved++;
                    
                    if (batch.size() >= batchSize) {
                        // Sử dụng batch insert từ repository
                        repo.saveBatchOptimized(batch, batchSize);
                        batch.clear();
                        
                        if (progressCallback != null && totalSaved % 10000 == 0) {
                            int percent = 60 + (int)((double)processed / totalCCCD * 40);
                            progressCallback.accept(Math.min(percent, 99));
                        }
                        
                        if (totalSaved % 50000 == 0) {
                            System.gc();
                            System.out.println("Đã lưu " + totalSaved + " bản ghi...");
                        }
                    }
                }
            }
            
            processed++;
            if (progressCallback != null && processed % 500 == 0) {
                int percent = 60 + (int)((double)processed / totalCCCD * 40);
                progressCallback.accept(Math.min(percent, 99));
            }
        }
        
        // Lưu batch cuối cùng
        if (!batch.isEmpty()) {
            repo.saveBatchOptimized(batch, batchSize);
            batch.clear();
        }
        
        mapTS = null;  mapUT = null;  mapNV = null;  mapTH = null;
        System.gc();
        
        if (progressCallback != null) progressCallback.accept(100);
        
        System.out.println("Hoàn thành! Đã lưu " + totalSaved + " bản ghi vào database.");
        return totalSaved;
    }
}