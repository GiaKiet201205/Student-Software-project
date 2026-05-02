package com.service.NVXTImport.converter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Chuyển đổi điểm V-SAT sang thang điểm 30
 * Dựa trên bảng quy đổi theo phân vị từ file Quy doi diem thi V-SAT 2025.docx
 */
public class VsatScoreConverter {
    
    // Mỗi môn có bảng quy đổi riêng
    private final Map<String, Range[]> conversionTable = new HashMap<>();
    
    public VsatScoreConverter() {
        initConversionTables();
    }
    
    /**
     * Khởi tạo bảng quy đổi cho từng môn
     */
    private void initConversionTables() {
        // TOÁN
        conversionTable.put("TO", new Range[]{
            new Range(132, 150, 8.5, 10.0),
            new Range(128.5, 132, 8.1, 8.5),
            new Range(122.5, 128.5, 7.75, 8.1),
            new Range(114.5, 122.5, 7.0, 7.75),
            new Range(108, 114.5, 6.6, 7.0),
            new Range(102.5, 108, 6.25, 6.6),
            new Range(97, 102.5, 6.0, 6.25),
            new Range(91, 97, 5.6, 6.0),
            new Range(85, 91, 5.25, 5.6),
            new Range(77, 85, 5.0, 5.25),
            new Range(68, 77, 4.5, 5.0),
            new Range(6, 68, 1.5, 4.5)
        });
        
        // VẬT LÝ
        conversionTable.put("LI", new Range[]{
            new Range(123, 147, 9.5, 10.0),
            new Range(118.5, 123, 9.25, 9.5),
            new Range(112.5, 118.5, 9.0, 9.25),
            new Range(105, 112.5, 8.5, 9.0),
            new Range(99.5, 105, 8.0, 8.5),
            new Range(94.5, 99.5, 7.75, 8.0),
            new Range(90, 94.5, 7.5, 7.75),
            new Range(85, 90, 7.25, 7.5),
            new Range(80, 85, 6.75, 7.25),
            new Range(74, 80, 6.35, 6.75),
            new Range(66.5, 74, 5.75, 6.35),
            new Range(17, 66.5, 3.05, 5.75)
        });
        
        // HÓA HỌC
        conversionTable.put("HO", new Range[]{
            new Range(129, 150, 9.5, 10.0),
            new Range(124.5, 129, 9.25, 9.5),
            new Range(117, 124.5, 8.75, 9.25),
            new Range(107.5, 117, 8.25, 8.75),
            new Range(100.5, 107.5, 7.75, 8.25),
            new Range(94, 100.5, 7.25, 7.75),
            new Range(88, 94, 6.75, 7.25),
            new Range(81.5, 88, 6.25, 6.75),
            new Range(75.5, 81.5, 5.75, 6.25),
            new Range(68.5, 75.5, 5.25, 5.75),
            new Range(59.5, 68.5, 4.6, 5.25),
            new Range(20, 59.5, 1.35, 4.6)
        });
        
        // SINH HỌC
        conversionTable.put("SI", new Range[]{
            new Range(130.5, 150, 9.0, 9.75),
            new Range(126.5, 130.5, 8.75, 9.0),
            new Range(120.5, 126.5, 8.34, 8.75),
            new Range(112.5, 120.5, 7.85, 8.34),
            new Range(105.5, 112.5, 7.5, 7.85),
            new Range(100, 105.5, 7.25, 7.5),
            new Range(94.5, 100, 6.85, 7.25),
            new Range(88.5, 94.5, 6.5, 6.85),
            new Range(82.5, 88.5, 6.25, 6.5),
            new Range(76, 82.5, 5.85, 6.25),
            new Range(66.5, 76, 5.25, 5.85),
            new Range(26.5, 66.5, 2.8, 5.25)
        });
        
        // NGỮ VĂN
        conversionTable.put("VA", new Range[]{
            new Range(129.5, 146, 9.25, 9.75),
            new Range(127.5, 129.5, 9.0, 9.25),
            new Range(124, 127.5, 9.0, 9.0),
            new Range(119.5, 124, 8.75, 9.0),
            new Range(115.5, 119.5, 8.5, 8.75),
            new Range(112.5, 115.5, 8.25, 8.5),
            new Range(109, 112.5, 8.0, 8.25),
            new Range(106, 109, 7.75, 8.0),
            new Range(102, 106, 7.5, 7.75),
            new Range(97, 102, 7.25, 7.5),
            new Range(90, 97, 6.75, 7.25),
            new Range(5, 90, 3.5, 6.75)
        });
        
        // LỊCH SỬ
        conversionTable.put("SU", new Range[]{
            new Range(133.5, 150, 9.75, 10.0),
            new Range(131, 133.5, 9.5, 9.75),
            new Range(126.5, 131, 9.25, 9.5),
            new Range(120.5, 126.5, 9.0, 9.25),
            new Range(115, 120.5, 8.5, 9.0),
            new Range(110, 115, 8.25, 8.5),
            new Range(105.5, 110, 8.0, 8.25),
            new Range(101, 105.5, 7.75, 8.0),
            new Range(95.5, 101, 7.5, 7.75),
            new Range(88.5, 95.5, 7.0, 7.5),
            new Range(79.5, 88.5, 6.35, 7.0),
            new Range(36.5, 79.5, 2.95, 6.35)
        });
        
        // ĐỊA LÝ
        conversionTable.put("DI", new Range[]{
            new Range(124, 141, 10.0, 10.0),
            new Range(120.5, 124, 10.0, 10.0),
            new Range(115.5, 120.5, 9.75, 10.0),
            new Range(108.5, 115.5, 9.25, 9.75),
            new Range(103, 108.5, 9.0, 9.25),
            new Range(98.5, 103, 8.75, 9.0),
            new Range(94, 98.5, 8.5, 8.75),
            new Range(89.5, 94, 8.25, 8.5),
            new Range(84.5, 89.5, 7.75, 8.25),
            new Range(79, 84.5, 7.25, 7.75),
            new Range(71, 79, 6.5, 7.25),
            new Range(31, 71, 3.0, 6.5)
        });
        
        // TIẾNG ANH
        conversionTable.put("TA", new Range[]{
            new Range(131, 150, 7.75, 9.75),
            new Range(127.5, 131, 7.5, 7.75),
            new Range(120.5, 127.5, 7.0, 7.5),
            new Range(112, 120.5, 6.5, 7.0),
            new Range(105, 112, 6.0, 6.5),
            new Range(98.5, 105, 5.75, 6.0),
            new Range(92, 98.5, 5.5, 5.75),
            new Range(85.5, 92, 5.25, 5.5),
            new Range(78.5, 85.5, 5.0, 5.25),
            new Range(70.5, 78.5, 4.5, 5.0),
            new Range(60, 70.5, 4.0, 4.5),
            new Range(20.5, 60, 1.25, 4.0)
        });
    }
    
    /**
     * Chuyển đổi điểm V-SAT (thang 150) sang điểm THPT (thang 10)
     * @param mon Môn học (TO, LI, HO, SI, VA, SU, DI, N1)
     * @param diemVsat Điểm V-SAT (thang 150)
     * @return Điểm đã quy đổi (thang 10)
     */
    public BigDecimal convert(String mon, double diemVsat) {
        if (diemVsat <= 0) return BigDecimal.ZERO;
        
        // Chuẩn hóa tên môn 
        String normalizedMon = mon.toUpperCase();
        if ("N1".equals(normalizedMon)) {
            normalizedMon = "TA";
        }
        
        Range[] ranges = conversionTable.get(normalizedMon);
        if (ranges == null) return BigDecimal.ZERO;
        
        for (Range range : ranges) {
            if (diemVsat > range.vsatMin && diemVsat <= range.vsatMax) {
                double result = interpolate(
                    range.vsatMin, range.vsatMax,
                    range.thptMin, range.thptMax,
                    diemVsat
                );
                return BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
            }
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Nội suy tuyến tính theo công thức:
     * y = c + ((x - a) / (b - a)) * (d - c)
     */
    private double interpolate(double vsatMin, double vsatMax, 
                                double thptMin, double thptMax, 
                                double vsatScore) {
        // Kiểm tra biên
        if (vsatScore <= vsatMin) return thptMin;
        if (vsatScore >= vsatMax) return thptMax;
        
        // Công thức đúng
        double ratio = (vsatScore - vsatMin) / (vsatMax - vsatMin);
        return thptMin + ratio * (thptMax - thptMin);
    }
    
    /**
     * Tính tổng điểm xét tuyển từ điểm V-SAT các môn
     * @param monScores Map mã môn -> điểm V-SAT
     * @param toHop Danh sách môn trong tổ hợp
     * @return Tổng điểm đã quy đổi (thang 30)
     */
    public BigDecimal calculateToHopScore(Map<String, Double> monScores, String[] toHop) {
        double total = 0;
        for (String mon : toHop) {
            Double vsatScore = monScores.get(mon);
            if (vsatScore == null || vsatScore <= 0) {
                return BigDecimal.ZERO;
            }
            BigDecimal converted = convert(mon, vsatScore);
            total += converted.doubleValue();
        }
        return BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
    }
    
    // khoảng quy đổi
    private static class Range {
        final double vsatMin;
        final double vsatMax;
        final double thptMin;
        final double thptMax;
        
        Range(double vsatMin, double vsatMax, double thptMin, double thptMax) {
            this.vsatMin = vsatMin;
            this.vsatMax = vsatMax;
            this.thptMin = thptMin;
            this.thptMax = thptMax;
        }
    }
    
}