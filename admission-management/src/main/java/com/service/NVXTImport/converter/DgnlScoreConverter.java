package com.service.NVXTImport.converter;

import com.controller.BangQuyDoiController;
import com.entity.BangQuyDoi;
import com.entity.DiemThiXetTuyen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DgnlScoreConverter {
    
    // Map: (phuongThuc_toHop) -> danh sách các khoảng quy đổi
    private final Map<String, QuyDoiRange[]> conversionCache = new HashMap<>();
    private final BangQuyDoiController bangQuyDoiController;
    
    public DgnlScoreConverter() {
        this.bangQuyDoiController = new BangQuyDoiController();
        loadConversionData();
    }
    
    private void loadConversionData() {
        List<BangQuyDoi> allRules = bangQuyDoiController.getAll();
        
        for (BangQuyDoi rule : allRules) {
            // Kiểm tra phương thức hợp lệ
            if (!"DGNL".equals(rule.getPhuongthuc()) && !"VSAT".equals(rule.getPhuongthuc())) {
                continue;
            }
            
            String key = rule.getPhuongthuc() + "_" + rule.getTohop();
            QuyDoiRange newRange = new QuyDoiRange(
                rule.getDiemA(), rule.getDiemB(),
                rule.getDiemC(), rule.getDiemD()
            );
            
            QuyDoiRange[] existing = conversionCache.get(key);
            if (existing == null) {
                conversionCache.put(key, new QuyDoiRange[]{newRange});
            } else {
                QuyDoiRange[] newRanges = new QuyDoiRange[existing.length + 1];
                System.arraycopy(existing, 0, newRanges, 0, existing.length);
                newRanges[existing.length] = newRange;
                conversionCache.put(key, newRanges);
            }
        }
    }
    
    /**
     * Quy đổi điểm DGNL (thang 1200) sang thang 30 cho một tổ hợp
     * @param phuongThuc "DGNL" hoặc "VSAT"
     * @param toHop Mã tổ hợp (VD: "A01", "B03")
     * @param diemGoc Điểm đầu vào (VD: 950, 1000...)
     * @return Điểm đã quy đổi (thang 30)
     */
    public BigDecimal convert(String phuongThuc, String toHop, BigDecimal diemGoc) {
        if (diemGoc == null || diemGoc.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        String key = phuongThuc + "_" + toHop;
        QuyDoiRange[] ranges = conversionCache.get(key);
        if (ranges == null) return BigDecimal.ZERO;
        
        for (QuyDoiRange range : ranges) {
            if (diemGoc.compareTo(range.diemMin) > 0 && diemGoc.compareTo(range.diemMax) <= 0) {
                // Nội suy tuyến tính
                double ratio = (diemGoc.doubleValue() - range.diemMin.doubleValue()) / 
                               (range.diemMax.doubleValue() - range.diemMin.doubleValue());
                double result = range.diemQuyDoiMin.doubleValue() + 
                                ratio * (range.diemQuyDoiMax.doubleValue() - range.diemQuyDoiMin.doubleValue());
                return BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
            }
        }
        
        // Nếu điểm nằm ngoài tất cả các khoảng, xử lý biên
        if (diemGoc.compareTo(ranges[0].diemMin) <= 0) {
            return ranges[0].diemQuyDoiMin;
        }
        if (diemGoc.compareTo(ranges[ranges.length - 1].diemMax) > 0) {
            return ranges[ranges.length - 1].diemQuyDoiMax;
        }
        
        return BigDecimal.ZERO;
    }
    
    private static class QuyDoiRange {
        final BigDecimal diemMin;
        final BigDecimal diemMax;
        final BigDecimal diemQuyDoiMin;
        final BigDecimal diemQuyDoiMax;
        
        QuyDoiRange(BigDecimal diemMin, BigDecimal diemMax, 
                    BigDecimal diemQuyDoiMin, BigDecimal diemQuyDoiMax) {
            this.diemMin = diemMin;
            this.diemMax = diemMax;
            this.diemQuyDoiMin = diemQuyDoiMin;
            this.diemQuyDoiMax = diemQuyDoiMax;
        }
    }
}