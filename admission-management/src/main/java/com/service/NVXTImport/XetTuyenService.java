package com.service.NVXTImport;

import com.controller.NguyenVongXetTuyenController;
import com.entity.NguyenVongXetTuyen;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class XetTuyenService {

    private final NguyenVongXetTuyenController nguyenVongController;
    
    // Map: mã ngành + phương thức -> Map<tổ hợp, điểm chuẩn>
    private final Map<String, Map<String, BigDecimal>> diemChuanTheoToHop = new ConcurrentHashMap<>();
    
    // Map: mã ngành -> chỉ tiêu
    private final Map<String, Integer> chiTieuMap = new ConcurrentHashMap<>();

    public XetTuyenService() {
        this.nguyenVongController = new NguyenVongXetTuyenController();
    }

    public void loadDiemChuan(File fileThpt, File fileDgnl, File fileVsat) throws Exception {
        loadDiemChuanFromFile(fileThpt, "3");
        loadDiemChuanFromFile(fileDgnl, "4");
        loadDiemChuanFromFile(fileVsat, "5");
        System.out.println("✓ Đã load điểm chuẩn: " + diemChuanTheoToHop.size() + " ngành");
    }
    
    public void loadChiTieu(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {
            
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String maNganh = getCellString(row.getCell(1));
                if (maNganh == null || maNganh.isEmpty()) continue;
                
                int chiTieu = (int) getCellDouble(row.getCell(3));
                chiTieuMap.put(maNganh, chiTieu);
            }
        }
        System.out.println("✓ Đã load chỉ tiêu: " + chiTieuMap.size() + " ngành");
    }
    
    private void loadDiemChuanFromFile(File file, String phuongThuc) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {
            
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String maNganh = getCellString(row.getCell(1));
                if (maNganh == null || maNganh.isEmpty()) continue;
                
                String toHopList = getCellString(row.getCell(3));
                if (toHopList == null || toHopList.isEmpty()) continue;
                
                BigDecimal diemChuan = BigDecimal.valueOf(getCellDouble(row.getCell(4)));
                
                String key = maNganh + "_" + phuongThuc;
                Map<String, BigDecimal> toHopMap = diemChuanTheoToHop.computeIfAbsent(key, k -> new HashMap<>());
                
                String[] toHops = toHopList.split("[;, ]+");
                for (String toHop : toHops) {
                    toHop = toHop.trim();
                    if (!toHop.isEmpty()) {
                        // Lấy điểm chuẩn cao nhất nếu có nhiều dòng cho cùng tổ hợp
                        BigDecimal existing = toHopMap.get(toHop);
                        if (existing == null || diemChuan.compareTo(existing) > 0) {
                            toHopMap.put(toHop, diemChuan);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Quy trình xét tuyển:
     * 1. Lọc tổ hợp: mỗi (CCCD + Mã ngành + Phương thức) chỉ giữ 1 tổ hợp tốt nhất
     * 2. Lọc phương thức: mỗi (CCCD + Mã ngành) chỉ giữ 1 phương thức tốt nhất
     * 3. Xét điểm chuẩn: xác định thí sinh đạt sàn (DAT_SAN)
     * 4. Xếp hạng theo ngành: sắp xếp theo điểm giảm dần
     * 5. Xét chỉ tiêu: xác định YES/NO dựa trên hạng và chỉ tiêu
     * 6. Xét thứ tự nguyện vọng: mỗi thí sinh chỉ giữ 1 YES duy nhất
     * 7. Cập nhật database
     */
    public void xetTuyen() {
        System.out.println("=== BẮT ĐẦU XÉT TUYỂN ===");
        long startTime = System.currentTimeMillis();
        
        List<NguyenVongXetTuyen> allNguyenVong = nguyenVongController.getNguyenVongXetTuyen();
        System.out.println("Tổng số nguyện vọng (các tổ hợp): " + allNguyenVong.size());
        
        // ==================== BƯỚC 1: LỌC TỔ HỢP ====================
        List<NguyenVongXetTuyen> afterToHopFilter = filterBestToHop(allNguyenVong);
        System.out.println("Sau lọc tổ hợp: " + afterToHopFilter.size());
        
        // ==================== BƯỚC 2: LỌC PHƯƠNG THỨC ====================
        List<NguyenVongXetTuyen> afterMethodFilter = filterBestPhuongThuc(afterToHopFilter);
        System.out.println("Sau lọc phương thức: " + afterMethodFilter.size());
        
        // ==================== BƯỚC 3: XÉT ĐIỂM CHUẨN (đánh dấu DAT_SAN) ====================
        for (NguyenVongXetTuyen nv : afterMethodFilter) {
            String key = nv.getMaNganh() + "_" + nv.getPhuongThuc();
            Map<String, BigDecimal> toHopDiemChuan = diemChuanTheoToHop.getOrDefault(key, new HashMap<>());
            BigDecimal diemChuan = toHopDiemChuan.get(nv.getMaToHop());
            
            if (diemChuan == null) {
                nv.setKetQua("NO_DIEM_CHUAN");
            } else if (nv.getDiemXetTuyen().compareTo(diemChuan) >= 0) {
                nv.setKetQua("DAT_SAN");  // Đạt sàn, chưa phải trúng tuyển
            } else {
                nv.setKetQua("NO");
            }
        }
        
        // ==================== BƯỚC 4: XẾP HẠNG THEO NGÀNH ====================
        // Chỉ xếp hạng những thí sinh đạt sàn (DAT_SAN)
        Map<String, List<NguyenVongXetTuyen>> byNganh = afterMethodFilter.stream()
                .filter(nv -> "DAT_SAN".equals(nv.getKetQua()))
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getMaNganh));
        
        for (List<NguyenVongXetTuyen> nvList : byNganh.values()) {
            // Sắp xếp theo điểm giảm dần (cao nhất lên đầu)
            nvList.sort((a, b) -> b.getDiemXetTuyen().compareTo(a.getDiemXetTuyen()));
            for (int i = 0; i < nvList.size(); i++) {
                nvList.get(i).setThuTuXetTuyen(i + 1);
            }
        }
        
        // ==================== BƯỚC 5: XÉT CHỈ TIÊU ====================
        // Tạo bản sao chỉ tiêu để theo dõi số lượng còn lại
        Map<String, Integer> remainingChiTieu = new ConcurrentHashMap<>(chiTieuMap);
        
        for (List<NguyenVongXetTuyen> nvList : byNganh.values()) {
            String maNganh = nvList.get(0).getMaNganh();
            int chiTieu = remainingChiTieu.getOrDefault(maNganh, 0);
            
            for (int i = 0; i < nvList.size(); i++) {
                NguyenVongXetTuyen nv = nvList.get(i);
                if (i < chiTieu) {
                    nv.setKetQua("YES");
                } else {
                    nv.setKetQua("NO");
                }
            }
        }
        
        // ==================== BƯỚC 6: XÉT THỨ TỰ NGUYỆN VỌNG ====================
        // Quan trọng: Xử lý theo thứ tự ưu tiên của từng thí sinh
        Map<String, List<NguyenVongXetTuyen>> byCccd = afterMethodFilter.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getCccd));
        
        // Đếm lại chỉ tiêu thực tế sau khi xét thứ tự NV
        Map<String, Integer> actualChiTieu = new ConcurrentHashMap<>(chiTieuMap);
        
        for (List<NguyenVongXetTuyen> nvList : byCccd.values()) {
            // Sắp xếp theo thứ tự nguyện vọng tăng dần (1, 2, 3...)
            nvList.sort(Comparator.comparing(NguyenVongXetTuyen::getThuTu));
            
            boolean daTrungTuyen = false;
            
            for (NguyenVongXetTuyen nv : nvList) {
                if (daTrungTuyen) {
                    // Đã trúng tuyển nguyện vọng trước, các NV sau không xét
                    nv.setKetQua("KHONG_XET");
                } else if ("YES".equals(nv.getKetQua())) {
                    // Kiểm tra còn chỉ tiêu không
                    String maNganh = nv.getMaNganh();
                    int chiTieuConLai = actualChiTieu.getOrDefault(maNganh, 0);
                    
                    if (chiTieuConLai > 0) {
                        actualChiTieu.put(maNganh, chiTieuConLai - 1);
                        daTrungTuyen = true;
                    } else {
                        nv.setKetQua("NO");
                    }
                }
            }
        }
        
        // ==================== BƯỚC 7: CẬP NHẬT DATABASE ====================
        updateBatchOptimized(afterMethodFilter);
        
        // ==================== BƯỚC 8: THỐNG KÊ ====================
        Map<String, Long> thongKe = afterMethodFilter.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getKetQua, Collectors.counting()));
        
        System.out.println("\n=== KẾT QUẢ XÉT TUYỂN ===");
        thongKe.forEach((ketQua, count) -> System.out.println("  " + ketQua + ": " + count));
        
        long yesCount = thongKe.getOrDefault("YES", 0L);
        System.out.println("\n TỔNG SỐ TRÚNG TUYỂN: " + yesCount);
        System.out.println("Tổng thời gian: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    /**
     * Lọc tổ hợp: chỉ giữ 1 tổ hợp có điểm cao nhất cho mỗi (CCCD + Mã ngành + Phương thức)
     */
    private List<NguyenVongXetTuyen> filterBestToHop(List<NguyenVongXetTuyen> allNguyenVong) {
        Map<String, List<NguyenVongXetTuyen>> groupByKey = allNguyenVong.stream()
                .collect(Collectors.groupingBy(nv -> 
                    nv.getCccd() + "_" + nv.getMaNganh() + "_" + nv.getPhuongThuc()));
        
        List<NguyenVongXetTuyen> result = new ArrayList<>();
        List<Integer> idsToDelete = new ArrayList<>();
        
        for (List<NguyenVongXetTuyen> nvList : groupByKey.values()) {
            if (nvList.isEmpty()) continue;
            
            String maNganh = nvList.get(0).getMaNganh();
            String phuongThuc = nvList.get(0).getPhuongThuc();
            String key = maNganh + "_" + phuongThuc;
            Map<String, BigDecimal> toHopDiemChuan = diemChuanTheoToHop.getOrDefault(key, new HashMap<>());
            
            // Lọc các tổ hợp có trong danh sách xét tuyển
            List<NguyenVongXetTuyen> validList = nvList.stream()
                    .filter(nv -> nv.getMaToHop() != null && toHopDiemChuan.containsKey(nv.getMaToHop()))
                    .collect(Collectors.toList());
            
            if (validList.isEmpty()) {
                // Không có tổ hợp nào được xét tuyển, giữ bản ghi đầu tiên
                result.add(nvList.get(0));
                for (int i = 1; i < nvList.size(); i++) {
                    idsToDelete.add(nvList.get(i).getIdNv());
                }
            } else {
                // Chọn tổ hợp có điểm cao nhất
                NguyenVongXetTuyen best = validList.stream()
                        .max(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen))
                        .orElse(validList.get(0));
                result.add(best);
                
                for (NguyenVongXetTuyen nv : nvList) {
                    if (nv != best) {
                        idsToDelete.add(nv.getIdNv());
                    }
                }
            }
        }
        
        // Xóa các bản ghi không được chọn
        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("Đã xóa " + idsToDelete.size() + " bản ghi tổ hợp không được chọn");
        }
        
        return result;
    }
    
    /**
     * Lọc phương thức: chỉ giữ 1 phương thức có điểm cao nhất cho mỗi (CCCD + Mã ngành)
     */
    private List<NguyenVongXetTuyen> filterBestPhuongThuc(List<NguyenVongXetTuyen> list) {
        Map<String, List<NguyenVongXetTuyen>> groupByCccdNganh = list.stream()
                .collect(Collectors.groupingBy(nv -> nv.getCccd() + "_" + nv.getMaNganh()));
        
        List<NguyenVongXetTuyen> result = new ArrayList<>();
        List<Integer> idsToDelete = new ArrayList<>();
        
        for (List<NguyenVongXetTuyen> nvList : groupByCccdNganh.values()) {
            if (nvList.isEmpty()) continue;
            
            if (nvList.size() == 1) {
                result.add(nvList.get(0));
                continue;
            }
            
            // Chọn phương thức có điểm xét tuyển cao nhất
            NguyenVongXetTuyen best = nvList.stream()
                    .max(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen))
                    .orElse(nvList.get(0));
            result.add(best);
            
            for (NguyenVongXetTuyen nv : nvList) {
                if (nv != best) {
                    idsToDelete.add(nv.getIdNv());
                }
            }
        }
        
        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("Đã xóa " + idsToDelete.size() + " bản ghi phương thức không được chọn");
        }
        
        return result;
    }
    
    private void updateBatchOptimized(List<NguyenVongXetTuyen> list) {
        if (list.isEmpty()) return;
        nguyenVongController.updateBatch(list);
    }
    
    private String getCellString(Cell cell) {
        if (cell == null) return null;
        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell).trim();
        return value.isEmpty() ? null : value;
    }
    
    private double getCellDouble(Cell cell) {
        if (cell == null) return 0;
        DataFormatter formatter = new DataFormatter();
        try {
            return Double.parseDouble(formatter.formatCellValue(cell).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}