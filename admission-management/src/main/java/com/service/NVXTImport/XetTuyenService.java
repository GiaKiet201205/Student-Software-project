package com.service.NVXTImport;

import com.config.HibernateUtil;
import com.controller.NguyenVongXetTuyenController;
import com.entity.NguyenVongXetTuyen;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
                
                // Đọc danh sách tổ hợp (cột D - index 3)
                String toHopList = getCellString(row.getCell(3));
                if (toHopList == null || toHopList.isEmpty()) continue;
                
                // Đọc điểm chuẩn (cột E - index 4)
                BigDecimal diemChuan = BigDecimal.valueOf(getCellDouble(row.getCell(4)));
                
                String key = maNganh + "_" + phuongThuc;
                Map<String, BigDecimal> toHopMap = diemChuanTheoToHop.computeIfAbsent(key, k -> new HashMap<>());
                
                // Phân tích danh sách tổ hợp (cách nhau bằng dấu ; hoặc ,)
                String[] toHops = toHopList.split("[;, ]+");
                for (String toHop : toHops) {
                    toHop = toHop.trim();
                    if (!toHop.isEmpty()) {
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
     * Xét tuyển theo quy tắc: mỗi thí sinh chỉ chọn 1 tổ hợp và 1 phương thức tốt nhất
     */
    public void xetTuyen() {
        System.out.println("=== BẮT ĐẦU XÉT TUYỂN ===");
        long startTime = System.currentTimeMillis();
        
        List<NguyenVongXetTuyen> allNguyenVong = nguyenVongController.getNguyenVongXetTuyen();
        System.out.println("Tổng số nguyện vọng (các tổ hợp): " + allNguyenVong.size());
        
        // Bước 1: Lọc ảo - chọn tổ hợp tốt nhất cho mỗi (CCCD + Mã ngành + Phương thức)
        List<NguyenVongXetTuyen> afterToHopFilter = filterBestToHop(allNguyenVong);
        System.out.println("Sau lọc tổ hợp: " + afterToHopFilter.size() + " nguyện vọng");
        
        // Bước 2: Lọc phương thức - chỉ giữ lại phương thức có điểm cao nhất cho mỗi (CCCD + Mã ngành)
        List<NguyenVongXetTuyen> afterPhuongThucFilter = filterBestPhuongThuc(afterToHopFilter);
        System.out.println("Sau lọc phương thức: " + afterPhuongThucFilter.size() + " nguyện vọng");
        
        // Bước 3: Xét điểm chuẩn
        List<NguyenVongXetTuyen> nvToUpdate = new ArrayList<>();
        
        for (NguyenVongXetTuyen nv : afterPhuongThucFilter) {
            String key = nv.getMaNganh() + "_" + nv.getPhuongThuc();
            Map<String, BigDecimal> toHopDiemChuan = diemChuanTheoToHop.getOrDefault(key, new HashMap<>());
            BigDecimal diemChuan = toHopDiemChuan.get(nv.getTtThm());
            
            if (diemChuan == null) {
                nv.setKetQua("NO_DIEM_CHUAN");
            } else if (nv.getDiemXetTuyen().compareTo(diemChuan) >= 0) {
                nv.setKetQua("YES");
            } else {
                nv.setKetQua("NO");
            }
            nvToUpdate.add(nv);
        }
        
        // Bước 4: Xếp hạng và xét chỉ tiêu (chỉ những NV có kết quả YES)
        Map<String, List<NguyenVongXetTuyen>> byNganh = nvToUpdate.stream()
                .filter(nv -> "YES".equals(nv.getKetQua()))
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getMaNganh));
        
        Map<String, Integer> currentChiTieu = new ConcurrentHashMap<>(chiTieuMap);
        
        for (List<NguyenVongXetTuyen> nvList : byNganh.values()) {
            String maNganh = nvList.get(0).getMaNganh();
            int chiTieu = currentChiTieu.getOrDefault(maNganh, 0);
            
            nvList.sort((a, b) -> b.getDiemXetTuyen().compareTo(a.getDiemXetTuyen()));
            for (int i = 0; i < nvList.size(); i++) {
                if (i < chiTieu) {
                    nvList.get(i).setKetQua("YES");
                    nvList.get(i).setThuTuXetTuyen(i + 1);
                } else {
                    nvList.get(i).setKetQua("NO");
                }
            }
        }
        
        // Bước 5: Xét thứ tự nguyện vọng
        Map<String, List<NguyenVongXetTuyen>> byCccd = nvToUpdate.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getCccd));
        
        for (List<NguyenVongXetTuyen> nvList : byCccd.values()) {
            nvList.sort(Comparator.comparing(NguyenVongXetTuyen::getThuTu));
            boolean daDo = false;
            for (NguyenVongXetTuyen nv : nvList) {
                if (daDo) {
                    nv.setKetQua("KHONG_XET");
                } else if ("YES".equals(nv.getKetQua())) {
                    daDo = true;
                }
            }
        }
        
        // Bước 6: UPDATE BATCH
        updateBatchOptimized(nvToUpdate);
        
        // Bước 7: Thống kê
        Map<String, Long> thongKe = nvToUpdate.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getKetQua, Collectors.counting()));
        
        System.out.println("\n=== KẾT QUẢ XÉT TUYỂN ===");
        thongKe.forEach((ketQua, count) -> System.out.println("  " + ketQua + ": " + count));
        System.out.println("Tổng thời gian: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * Lọc phương thức: chỉ giữ lại phương thức có điểm cao nhất cho mỗi (CCCD + Mã ngành)
     * @param list Danh sách sau khi lọc tổ hợp
     * @return Danh sách sau khi lọc phương thức
     */
    private List<NguyenVongXetTuyen> filterBestPhuongThuc(List<NguyenVongXetTuyen> list) {
        // Nhóm theo (CCCD + Mã ngành) - không phân biệt phương thức
        Map<String, List<NguyenVongXetTuyen>> groupByCccdNganh = list.stream()
                .collect(Collectors.groupingBy(nv -> nv.getCccd() + "_" + nv.getMaNganh()));
        
        List<NguyenVongXetTuyen> result = new ArrayList<>();
        List<Integer> idsToDelete = new ArrayList<>();
        
        for (List<NguyenVongXetTuyen> nvList : groupByCccdNganh.values()) {
            if (nvList.isEmpty()) continue;
            
            // Nếu chỉ có 1 phương thức, giữ nguyên
            if (nvList.size() == 1) {
                result.add(nvList.get(0));
                continue;
            }
            
            // Chọn phương thức có điểm xét tuyển cao nhất
            NguyenVongXetTuyen best = nvList.stream()
                    .max(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen))
                    .orElse(nvList.get(0));
            result.add(best);
            
            // Đánh dấu các phương thức còn lại để xóa
            for (NguyenVongXetTuyen nv : nvList) {
                if (nv != best) {
                    idsToDelete.add(nv.getIdNv());
                }
            }
        }
        
        // Xóa các bản ghi không được chọn
        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("Đã xóa " + idsToDelete.size() + " bản ghi phương thức không được chọn");
        }
        
        return result;
    }

    // Sửa method filterBestToHop - xóa batch bằng repository
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
            
            List<NguyenVongXetTuyen> validList = nvList.stream()
                    .filter(nv -> nv.getTtThm() != null && toHopDiemChuan.containsKey(nv.getTtThm()))
                    .collect(Collectors.toList());
            
            if (validList.isEmpty()) {
                NguyenVongXetTuyen first = nvList.get(0);
                result.add(first);
                for (int i = 1; i < nvList.size(); i++) {
                    idsToDelete.add(nvList.get(i).getIdNv());
                }
            } else {
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
        
        // Xóa batch bằng repository
        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("Đã xóa " + idsToDelete.size() + " bản ghi tổ hợp không được chọn");
        }
        
        return result;
    }

    // Update batch tối ưu - dùng repository
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