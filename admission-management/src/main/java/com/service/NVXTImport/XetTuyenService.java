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

/**
 * Mỗi vòng lặp:
 *   1. Duyệt đồng loạt tất cả thí sinh chưa có chỗ (theo thứ tự NV ưu tiên).
 *   2. Ai đủ điểm & còn chỉ tiêu → được "giữ tạm" ở nguyện vọng đó.
 *   3. Nếu một thí sinh đang giữ tạm ở NV thấp hơn nhưng vừa được chấp nhận
 *      ở NV cao hơn → nhả chỗ ở NV thấp, chỉ tiêu đó được hoàn lại.
 *   4. Người xếp ngay sau được đẩy lên lấp chỗ trống.
 *   5. Lặp đến khi không còn thay đổi nào (hội tụ).
 *
 * Kết quả: mỗi thí sinh chỉ trúng tuyển tại đúng 1 nguyện vọng cao nhất
 * mà họ đủ điều kiện
 */
public class XetTuyenService {

    private final NguyenVongXetTuyenController nguyenVongController;

    /** Chỉ tiêu gốc của từng ngành (maNganh -> chiTieu) */
    private Map<String, Integer> chiTieuMap = new ConcurrentHashMap<>();

    public XetTuyenService() {
        this.nguyenVongController = new NguyenVongXetTuyenController();
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
        int totalChiTieu = chiTieuMap.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("✓ Tổng chỉ tiêu: " + totalChiTieu);
    }

    public void xetTuyen() {
        System.out.println("\n=== BẮT ĐẦU XÉT TUYỂN ===");
        long startTime = System.currentTimeMillis();

        // --- Chuẩn bị: Lấy dữ liệu & lọc trùng tổ hợp / phương thức ---
        List<NguyenVongXetTuyen> allNguyenVong = nguyenVongController.getNguyenVongXetTuyen();
        List<NguyenVongXetTuyen> afterFilter = filterBestToHop(allNguyenVong);
        afterFilter = filterBestPhuongThuc(afterFilter);
        System.out.println("✓ Tổng nguyện vọng sau lọc: " + afterFilter.size());

        int maxThuTu = afterFilter.stream()
                .mapToInt(NguyenVongXetTuyen::getThuTu)
                .max().orElse(0);

        // =====================================================================
        // BƯỚC 1: Mỗi ngành xét tuyển độc lập theo điểm — BỎ QUA thứ tự NV
        // =====================================================================
        // Gom tất cả NV của cùng 1 ngành lại, sắp xếp điểm giảm dần,
        // quét từ trên xuống đến hết chỉ tiêu → đánh dấu "đỗ dự kiến".
        // Một thí sinh điểm cao có thể "đỗ dự kiến" ở nhiều ngành cùng lúc.
        //
        // idNv -> true nếu NV này nằm trong top chỉ tiêu của ngành đó
        Set<Integer> doDuKien = new HashSet<>();

        // Nhóm tất cả NV theo ngành
        Map<String, List<NguyenVongXetTuyen>> nvTheoNganh = afterFilter.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getMaNganh));

        for (Map.Entry<String, List<NguyenVongXetTuyen>> entry : nvTheoNganh.entrySet()) {
            String maNganh = entry.getKey();
            int chiTieu = chiTieuMap.getOrDefault(maNganh, 0);
            if (chiTieu <= 0) continue;

            // Sắp xếp theo điểm GIẢM DẦN — thứ tự NV không quan trọng ở bước này
            List<NguyenVongXetTuyen> sorted = entry.getValue().stream()
                    .sorted(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen).reversed())
                    .collect(Collectors.toList());

            // Quét từ trên xuống đến hết chỉ tiêu
            int dem = 0;
            for (NguyenVongXetTuyen nv : sorted) {
                if (dem >= chiTieu) break;
                doDuKien.add(nv.getIdNv());
                dem++;
            }
        }

        System.out.println("✓ Bước 1 xong — số lượt đỗ dự kiến: " + doDuKien.size()
                + " (1 thí sinh có thể đỗ nhiều ngành)");

        // =====================================================================
        // BƯỚC 2: Lọc ảo — dùng thứ tự NV để chọn 1 ngành duy nhất
        // =====================================================================
        // Với mỗi thí sinh đang "đỗ dự kiến" ở nhiều ngành:
        //   → Chỉ giữ ngành có thứ tự NV ưu tiên CAO NHẤT (thuTu nhỏ nhất)
        //   → Nhả chỗ ở các ngành còn lại
        //   → Người xếp sau trong ngành vừa nhả được đẩy lên (nếu đang đỗ dự kiến)
        // Lặp đến khi không còn thay đổi (hội tụ).
        //
        // CCCD -> NV đang được giữ chỗ chính thức (NV ưu tiên cao nhất mà thí sinh đỗ dự kiến)
        Map<String, NguyenVongXetTuyen> dangGiuCho = new HashMap<>();

        boolean changed = true;
        int vong = 0;
        int maxVong = maxThuTu + 10;

        while (changed && vong < maxVong) {
            changed = false;
            vong++;

            //  Với mỗi thí sinh: tìm NV ưu tiên cao nhất mà họ đang đỗ dự kiến ---
            // Nhóm các NV đỗ dự kiến theo CCCD
            Map<String, NguyenVongXetTuyen> nvTotNhat = new HashMap<>();
            for (NguyenVongXetTuyen nv : afterFilter) {
                if (!doDuKien.contains(nv.getIdNv())) continue;
                String cccd = nv.getCccd();
                NguyenVongXetTuyen cur = nvTotNhat.get(cccd);
                // Giữ NV có thuTu nhỏ nhất (ưu tiên cao nhất)
                if (cur == null || nv.getThuTu() < cur.getThuTu()) {
                    nvTotNhat.put(cccd, nv);
                }
            }

            //  Cập nhật dangGiuCho và phát hiện thay đổi ---
            for (Map.Entry<String, NguyenVongXetTuyen> e : nvTotNhat.entrySet()) {
                String cccd = e.getKey();
                NguyenVongXetTuyen nvMoi = e.getValue();
                NguyenVongXetTuyen nvCu = dangGiuCho.get(cccd);
                if (nvCu == null || !nvCu.getIdNv().equals(nvMoi.getIdNv())) {
                    dangGiuCho.put(cccd, nvMoi);
                    changed = true;
                }
            }

            //  Xóa khỏi doDuKien những NV mà thí sinh KHÔNG chọn ---
            // (thí sinh đỗ dự kiến ở NV2 nhưng đang giữ NV1 → nhả NV2)
            // Sau đó bổ sung người kế tiếp vào ngành vừa nhả
            Set<Integer> dangGiuIdNv = dangGiuCho.values().stream()
                    .map(NguyenVongXetTuyen::getIdNv)
                    .collect(Collectors.toSet());

            // Với mỗi CCCD đang giữ chỗ: xóa các NV đỗ dự kiến khác của họ khỏi doDuKien
            Map<String, Integer> chiTieuDangDung = new HashMap<>();
            for (NguyenVongXetTuyen nv : dangGiuCho.values()) {
                chiTieuDangDung.merge(nv.getMaNganh(), 1, Integer::sum);
            }

            // Tìm các ngành còn chỗ trống (chỉ tiêu gốc > số đang giữ)
            // và bổ sung thí sinh điểm cao tiếp theo chưa đỗ dự kiến ở ngành đó
            Set<String> cccdDaDuoc = dangGiuCho.keySet();

            for (Map.Entry<String, List<NguyenVongXetTuyen>> entry : nvTheoNganh.entrySet()) {
                String maNganh = entry.getKey();
                int chiTieuGoc = chiTieuMap.getOrDefault(maNganh, 0);
                int dangDung = chiTieuDangDung.getOrDefault(maNganh, 0);
                int conTrong = chiTieuGoc - dangDung;
                if (conTrong <= 0) continue;

                // Lấy danh sách thí sinh của ngành này, sắp xếp điểm giảm dần
                // Chỉ xét những người CHƯA có chỗ tạm (chưa đỗ dự kiến ở ngành nào được giữ)
                List<NguyenVongXetTuyen> ungVien = entry.getValue().stream()
                        .filter(nv -> !doDuKien.contains(nv.getIdNv())) // chưa đỗ dự kiến ngành này
                        .filter(nv -> !cccdDaDuoc.contains(nv.getCccd())) // chưa có chỗ tạm nào
                        .sorted(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen).reversed())
                        .collect(Collectors.toList());

                for (int i = 0; i < conTrong && i < ungVien.size(); i++) {
                    doDuKien.add(ungVien.get(i).getIdNv());
                    changed = true;
                }
            }

            // Xóa khỏi doDuKien các NV của thí sinh đã có chỗ tạm nhưng không phải NV đang giữ
            for (NguyenVongXetTuyen nv : afterFilter) {
                if (!doDuKien.contains(nv.getIdNv())) continue;
                String cccd = nv.getCccd();
                NguyenVongXetTuyen nvGiu = dangGiuCho.get(cccd);
                if (nvGiu != null && !nvGiu.getIdNv().equals(nv.getIdNv())) {
                    doDuKien.remove(nv.getIdNv());
                    changed = true;
                }
            }

            System.out.println("[Vòng " + vong + "] Đang giữ chỗ: " + dangGiuCho.size() + " thí sinh");
        }

        System.out.println("\n✓ Hội tụ sau " + vong + " vòng lặp");

        // --- Bước 6: Xác định kết quả cuối cùng ---
        //
        // Quy tắc gán kết quả cho từng nguyện vọng của mỗi thí sinh:
        //
        //   NV1  NV2  NV3(YES)  NV4  NV5
        //   NO   NO   YES       KHONG_XET  KHONG_XET
        //
        //   - YES      : NV mà thí sinh trúng tuyển (nguyện vọng giữ chỗ cuối cùng)
        //   - NO       : các NV có ưu tiên CAO HƠN NV đạt (thuTu < thuTu_NV_dat)
        //                → thí sinh đã xét nhưng không đủ điều kiện (hết chỉ tiêu / dưới sàn)
        //   - KHONG_XET: các NV có ưu tiên THẤP HƠN NV đạt (thuTu > thuTu_NV_dat)
        //                → không cần xét vì đã trúng ở NV ưu tiên cao hơn rồi
        //   - Thí sinh trượt HOÀN TOÀN (không có NV nào đạt): tất cả NV đều là NO

        // Map CCCD -> NV đạt (để tra thuTu của NV đạt)
        Map<String, NguyenVongXetTuyen> nvDatTheoCccd = new HashMap<>(dangGiuCho);

        // Gán kết quả cho toàn bộ nguyện vọng
        List<NguyenVongXetTuyen> ketQua = new ArrayList<>();
        for (NguyenVongXetTuyen nv : afterFilter) {
            String cccd = nv.getCccd();
            NguyenVongXetTuyen nvDat = nvDatTheoCccd.get(cccd);

            if (nvDat == null) {
                // Thí sinh trượt hoàn toàn → tất cả NV đều NO
                nv.setKetQua("NO");

            } else if (nv.getIdNv().equals(nvDat.getIdNv())) {
                // Đúng NV trúng tuyển
                nv.setKetQua("YES");

            } else if (nv.getThuTu() < nvDat.getThuTu()) {
                // NV ưu tiên cao hơn NV đạt → thí sinh đã xét nhưng trượt
                nv.setKetQua("NO");

                } else {
                // NV ưu tiên thấp hơn NV đạt → không cần xét
                nv.setKetQua("KHONG_XET");
            }

            ketQua.add(nv);
        }
        // --- Bước 8: Lưu kết quả vào DB ---
        updateBatchOptimized(ketQua);

        // --- Thống kê ---
        Map<String, Long> thongKe = ketQua.stream()
                .collect(Collectors.groupingBy(NguyenVongXetTuyen::getKetQua, Collectors.counting()));

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("\n=== KẾT QUẢ XÉT TUYỂN ===");
        System.out.println(" ĐẠT (YES)            : " + thongKe.getOrDefault("YES", 0L));
        System.out.println(" TRƯỢT (NO)            : " + thongKe.getOrDefault("NO", 0L));
        System.out.println(" KHÔNG XÉT (KHONG_XET) : " + thongKe.getOrDefault("KHONG_XET", 0L));
        System.out.printf("⏱ Thời gian xử lý: %.2f giây%n", elapsed / 1000.0);
    }

    /**
     * Lọc tổ hợp: chỉ giữ 1 tổ hợp có điểm cao nhất cho mỗi (CCCD + Mã ngành + Phương thức).
     * Các bản ghi còn lại bị xóa khỏi DB.
     */
    private List<NguyenVongXetTuyen> filterBestToHop(List<NguyenVongXetTuyen> allNguyenVong) {
        Map<String, List<NguyenVongXetTuyen>> groupByKey = allNguyenVong.stream()
                .collect(Collectors.groupingBy(nv ->
                        nv.getCccd() + "_" + nv.getMaNganh() + "_" + nv.getPhuongThuc()));

        List<NguyenVongXetTuyen> result = new ArrayList<>();
        List<Integer> idsToDelete = new ArrayList<>();

        for (List<NguyenVongXetTuyen> nvList : groupByKey.values()) {
            if (nvList.isEmpty()) continue;

            NguyenVongXetTuyen best = nvList.stream()
                    .max(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen))
                    .orElse(nvList.get(0));
            result.add(best);

            for (NguyenVongXetTuyen nv : nvList) {
                if (!nv.getIdNv().equals(best.getIdNv())) {
                    idsToDelete.add(nv.getIdNv());
                }
            }
        }

        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("✓ Đã xóa " + idsToDelete.size() + " bản ghi tổ hợp không được chọn");
        }

        return result;
    }

    /**
     * Lọc phương thức: chỉ giữ 1 phương thức có điểm cao nhất cho mỗi (CCCD + Mã ngành).
     * Các bản ghi còn lại bị xóa khỏi DB.
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

            NguyenVongXetTuyen best = nvList.stream()
                    .max(Comparator.comparing(NguyenVongXetTuyen::getDiemXetTuyen))
                    .orElse(nvList.get(0));
            result.add(best);

            for (NguyenVongXetTuyen nv : nvList) {
                if (!nv.getIdNv().equals(best.getIdNv())) {
                    idsToDelete.add(nv.getIdNv());
                }
            }
        }

        if (!idsToDelete.isEmpty()) {
            nguyenVongController.deleteByIds(idsToDelete);
            System.out.println("✓ Đã xóa " + idsToDelete.size() + " bản ghi phương thức không được chọn");
        }

        return result;
    }


    private void updateBatchOptimized(List<NguyenVongXetTuyen> list) {
        if (list.isEmpty()) return;
        nguyenVongController.updateBatch(list);
        System.out.println("✓ Đã cập nhật " + list.size() + " bản ghi vào DB");
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