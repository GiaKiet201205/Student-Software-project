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
                
                // Đọc danh sách tổ hợp 
                String toHopList = getCellString(row.getCell(3));
                if (toHopList == null || toHopList.isEmpty()) continue;
                
                // Đọc điểm chuẩn
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
    
   public void xetTuyen() {
        System.out.println("=== BẮT ĐẦU XÉT TUYỂN ===");
        long startTime = System.currentTimeMillis();
        
        List<NguyenVongXetTuyen> allNguyenVong = nguyenVongController.getNguyenVongXetTuyen();
        System.out.println("Tổng số nguyện vọng: " + allNguyenVong.size());
        
        // Lọc tổ hợp và phương thức
        List<NguyenVongXetTuyen> afterFilter = filterBestToHop(allNguyenVong);
        afterFilter = filterBestPhuongThuc(afterFilter);
        System.out.println("Sau lọc: " + afterFilter.size() + " nguyện vọng");
        
        // Sắp xếp theo thứ tự ưu tiên để xử lý tuần tự
        // Phải xử lý theo đúng thứ tự nguyện vọng của từng thí sinh
        Map<String, List<NguyenVongXetTuyen>> byCccd = afterFilter.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getCccd));
        
        Set<String> daTrungTuyen = new HashSet<>();
        
        // Danh sách kết quả cuối cùng
        List<NguyenVongXetTuyen> finalResults = new ArrayList<>();
        
        for (Map.Entry<String, List<NguyenVongXetTuyen>> entry : byCccd.entrySet()) {
            String cccd = entry.getKey();
            List<NguyenVongXetTuyen> nvList = entry.getValue();
            
            // Sắp xếp theo thứ tự nguyện vọng
            nvList.sort(Comparator.comparing(NguyenVongXetTuyen::getThuTu));
            
            boolean daDo = false;
            for (NguyenVongXetTuyen nv : nvList) {
                if (daDo) {
                    nv.setKetQua("KHONG_XET");
                    finalResults.add(nv);
                    continue;
                }
                
                // Kiểm tra điểm chuẩn
                String key = nv.getMaNganh() + "_" + nv.getPhuongThuc();
                Map<String, BigDecimal> toHopDiemChuan = diemChuanTheoToHop.getOrDefault(key, new HashMap<>());
                BigDecimal diemChuan = toHopDiemChuan.get(nv.getMaToHop());
                
                if (diemChuan == null) {
                    nv.setKetQua("NO_DIEM_CHUAN");
                    finalResults.add(nv);
                    continue;
                }
                
                if (nv.getDiemXetTuyen().compareTo(diemChuan) < 0) {
                    nv.setKetQua("NO");
                    finalResults.add(nv);
                    continue;
                }
                
                // Đạt sàn, cần xét chỉ tiêu
                int chiTieu = chiTieuMap.getOrDefault(nv.getMaNganh(), 0);
                int daChon = countAlreadySelected(finalResults, nv.getMaNganh());
                
                if (daChon < chiTieu) {
                    nv.setKetQua("YES");
                    daDo = true;
                    daTrungTuyen.add(cccd);
                } else {
                    nv.setKetQua("NO");
                }
                finalResults.add(nv);
            }
        }       
        updateBatchOptimized(finalResults);
        
        // Thống kê
        Map<String, Long> thongKe = finalResults.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getKetQua, Collectors.counting()));
        
        System.out.println("\n=== KẾT QUẢ XÉT TUYỂN ===");
        thongKe.forEach((ketQua, count) -> System.out.println("  " + ketQua + ": " + count));
        System.out.println("Tổng thời gian: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private int countAlreadySelected(List<NguyenVongXetTuyen> list, String maNganh) {
        return (int) list.stream()
                .filter(nv -> nv.getMaNganh().equals(maNganh) && "YES".equals(nv.getKetQua()))
                .count();
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
                    .filter(nv -> nv.getMaToHop() != null && toHopDiemChuan.containsKey(nv.getMaToHop()))
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
        
        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("Đã xóa " + idsToDelete.size() + " bản ghi tổ hợp không được chọn");
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