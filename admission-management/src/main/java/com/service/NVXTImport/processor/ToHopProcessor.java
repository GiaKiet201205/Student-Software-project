package com.service.NVXTImport.processor;

import com.entity.DiemThiXetTuyen;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ToHopProcessor {
    
    private final Map<String, List<String[]>> toHopCache = new ConcurrentHashMap<>();
    private final Map<String, String[]> toHopMonMapping = new ConcurrentHashMap<>();
    
    public ToHopProcessor() {
        initToHopMonMapping();
    }
    
    private void initToHopMonMapping() {
        // A00-A07
        toHopMonMapping.put("A00", new String[]{"TO", "LI", "HO"});
        toHopMonMapping.put("A01", new String[]{"TO", "LI", "N1"});
        toHopMonMapping.put("A02", new String[]{"TO", "LI", "SI"});
        toHopMonMapping.put("A03", new String[]{"TO", "LI", "SU"});
        toHopMonMapping.put("A04", new String[]{"TO", "LI", "DI"});
        toHopMonMapping.put("A05", new String[]{"TO", "HO", "SU"});
        toHopMonMapping.put("A06", new String[]{"TO", "HO", "DI"});
        toHopMonMapping.put("A07", new String[]{"TO", "SU", "DI"});
        
        // B00-B08
        toHopMonMapping.put("B00", new String[]{"TO", "HO", "SI"});
        toHopMonMapping.put("B01", new String[]{"TO", "SI", "SU"});
        toHopMonMapping.put("B02", new String[]{"TO", "SI", "DI"});
        toHopMonMapping.put("B03", new String[]{"TO", "VA", "SI"});
        toHopMonMapping.put("B08", new String[]{"TO", "SI", "N1"});
        
        // C00-C13
        toHopMonMapping.put("C00", new String[]{"VA", "SU", "DI"});
        toHopMonMapping.put("C01", new String[]{"TO", "VA", "LI"});
        toHopMonMapping.put("C02", new String[]{"TO", "VA", "HO"});
        toHopMonMapping.put("C03", new String[]{"TO", "VA", "SU"});
        toHopMonMapping.put("C04", new String[]{"TO", "VA", "DI"});
        toHopMonMapping.put("C05", new String[]{"VA", "LI", "HO"});
        toHopMonMapping.put("C06", new String[]{"VA", "LI", "SI"});
        toHopMonMapping.put("C07", new String[]{"VA", "LI", "SU"});
        toHopMonMapping.put("C08", new String[]{"VA", "HO", "SI"});
        toHopMonMapping.put("C09", new String[]{"VA", "LI", "DI"});
        toHopMonMapping.put("C10", new String[]{"VA", "HO", "SU"});
        toHopMonMapping.put("C11", new String[]{"VA", "HO", "DI"});
        toHopMonMapping.put("C12", new String[]{"VA", "SI", "SU"});
        toHopMonMapping.put("C13", new String[]{"VA", "SI", "DI"});
        
        // D01-D15
        toHopMonMapping.put("D01", new String[]{"TO", "VA", "N1"});
        toHopMonMapping.put("D07", new String[]{"TO", "HO", "N1"});
        toHopMonMapping.put("D09", new String[]{"TO", "SU", "N1"});
        toHopMonMapping.put("D10", new String[]{"TO", "DI", "N1"});
        toHopMonMapping.put("D11", new String[]{"VA", "LI", "N1"});
        toHopMonMapping.put("D12", new String[]{"VA", "HO", "N1"});
        toHopMonMapping.put("D13", new String[]{"VA", "SI", "N1"});
        toHopMonMapping.put("D14", new String[]{"VA", "SU", "N1"});
        toHopMonMapping.put("D15", new String[]{"VA", "DI", "N1"});
        
        // Tổ hợp đặc biệt
        toHopMonMapping.put("M01", new String[]{"NK1", "NK2", "VA"});
        toHopMonMapping.put("M02", new String[]{"NK1", "TO", "NK2"});
        toHopMonMapping.put("H00", new String[]{"VA", "NK3", "NK4"});
        toHopMonMapping.put("N01", new String[]{"VA", "NK5", "NK6"});
        toHopMonMapping.put("NL1", new String[]{"NL1"});
        
        // Tổ hợp X
        for (int i = 1; i <= 81; i++) {
            String code = String.format("X%02d", i);
            toHopMonMapping.putIfAbsent(code, new String[]{code});
        }
    }
    
    public void loadToHopFromFile(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {
            
            Sheet sheet = wb.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> header = getHeaderMap(headerRow);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String maNganh = getCellString(row, header, "MANGANH");
                String rawToHop = getCellString(row, header, "MA_TO_HOP");
                
                if (maNganh == null || rawToHop == null) continue;
                
                String maToHop = parseToHopCode(rawToHop);
                if (maToHop != null) {
                    toHopCache.computeIfAbsent(maNganh, k -> new ArrayList<>()).add(new String[]{maToHop});
                }
            }
        }
    }
    
    public List<String[]> getToHopsForNganh(String maNganh) {
        return toHopCache.getOrDefault(maNganh, new ArrayList<>());
    }
    
    public String[] getMonListForToHop(String maToHop) {
        return toHopMonMapping.get(maToHop);
    }
    
    public BigDecimal calculateThxt(DiemThiXetTuyen diemThi, String[] toHop) {
        if (diemThi == null || toHop == null || toHop.length == 0) {
            return BigDecimal.ZERO;
        }
        
        // 🔥 Lấy mã tổ hợp (toHop[0]) và tra danh sách môn
        String maToHop = toHop[0];
        String[] monList = getMonListForToHop(maToHop);
        
        if (monList == null || monList.length == 0) {
            // Debug
            if ("33417".equals(diemThi.getCccd() != null ? diemThi.getCccd().replace("TS_", "") : "")) {
                System.out.println("    Không tìm thấy mapping cho tổ hợp: " + maToHop);
            }
            return BigDecimal.ZERO;
        }
        
        double total = 0;
        for (String mon : monList) {
            BigDecimal diem = diemThi.getDiemByMaMon(mon);
            if (diem == null || diem.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            total += diem.doubleValue();
        }
        
        return BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
    }
    
    public ToHopResult findBestToHop(DiemThiXetTuyen diemThi, String maNganh) {
        List<String[]> toHops = toHopCache.getOrDefault(maNganh, new ArrayList<>());
        
        boolean debug = "33417".equals(diemThi.getCccd() != null ? diemThi.getCccd().replace("TS_", "") : "");
        
        if (debug) {
            System.out.println("=== DEBUG ToHopProcessor ===");
            System.out.println("  CCCD: " + diemThi.getCccd());
            System.out.println("  Mã ngành: " + maNganh);
            System.out.println("  Số tổ hợp: " + toHops.size());
            System.out.println("  Điểm TO: " + diemThi.getToan());
            System.out.println("  Điểm VA: " + diemThi.getVan());
            System.out.println("  Điểm SU: " + diemThi.getSu());
        }
        
        BigDecimal bestScore = BigDecimal.ZERO;
        String bestToHop = null;
        
        for (String[] toHop : toHops) {
            String maToHop = toHop[0];
            String[] monList = getMonListForToHop(maToHop);
            
            if (monList == null) {
                if (debug) {
                    System.out.println("  Tổ hợp " + maToHop + ": không có mapping");
                }
                continue;
            }
            
            BigDecimal score = calculateThxt(diemThi, toHop);
            if (debug && score.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("  Tổ hợp " + maToHop + " (" + Arrays.toString(monList) + ") = " + score);
            }
            if (score.compareTo(bestScore) > 0) {
                bestScore = score;
                bestToHop = maToHop;
            }
        }
        
        if (debug) {
            System.out.println("  Kết quả: " + (bestToHop != null ? bestToHop + " = " + bestScore : "KHÔNG có tổ hợp"));
        }
        
        if (bestToHop == null) return null;
        return new ToHopResult(bestToHop, bestScore);
    }
    
    private String parseToHopCode(String raw) {
        try {
            int start = raw.indexOf("(");
            if (start == -1) return null;
            return raw.substring(0, start).trim();
        } catch (Exception e) {
            return null;
        }
    }
    
    private Map<String, Integer> getHeaderMap(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : headerRow) {
            String name = cell.getStringCellValue().trim().toLowerCase().replaceAll("\\s+", "");
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
    
    public static class ToHopResult {
        public final String maToHop;
        public final BigDecimal diemThxt;
        
        public ToHopResult(String maToHop, BigDecimal diemThxt) {
            this.maToHop = maToHop;
            this.diemThxt = diemThxt;
        }
    }
}