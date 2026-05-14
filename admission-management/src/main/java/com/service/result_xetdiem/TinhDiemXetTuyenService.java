package com.service.result_xetdiem;

import com.config.SessionManager;
import com.entity.BangQuyDoi;
import com.entity.DiemThiXetTuyen;
import com.entity.NganhToHop;
import com.entity.NguyenVongXetTuyen;
import com.exception.AppException;

import org.hibernate.query.Query;

import java.util.*;

/**
 * Service điều phối việc tính điểm xét tuyển cho danh sách NguyenVongXetTuyen.
 *
 * Luồng xử lý:
 *   1. Load toàn bộ DiemThiXetTuyen vào HashMap (key: cccd_phuongthuc)
 *   2. Load toàn bộ NganhToHop vào HashMap (key: maNganh_maToHop)
 *   3. Load toàn bộ BangQuyDoi vào HashMap (key: phuongthuc → List)
 *   4. Với mỗi NguyenVong → gọi DiemXetTuyenCalculator.tinhDiem()
 *   5. Batch update kết quả về DB
 */
public class TinhDiemXetTuyenService {

    private final DiemXetTuyenCalculator calculator = new DiemXetTuyenCalculator();

    // ================================================================
    // ENTRY POINT: Tính điểm cho 1 danh sách nguyện vọng
    // ================================================================

    public TinhDiemResult tinhDiemBatch(List<NguyenVongXetTuyen> danhSachNV) {
        if (danhSachNV == null || danhSachNV.isEmpty()) {
            return new TinhDiemResult(0, 0, Collections.emptyList());
        }

        // Load cache từ DB một lần duy nhất
        Map<String, DiemThiXetTuyen> cacheDiemThi = loadCacheDiemThi();
        Map<String, NganhToHop>      cacheNganhToHop = loadCacheNganhToHop();
        Map<String, List<BangQuyDoi>> cacheBangQuyDoi = loadCacheBangQuyDoi();

        int success = 0, error = 0;
        List<String> errors = new ArrayList<>();
        List<NguyenVongXetTuyen> daTinhXong = new ArrayList<>();

        for (NguyenVongXetTuyen nv : danhSachNV) {
            try {
                // Key tra DiemThiXetTuyen: cccd + "_" + phuongthuc
                // Nguồn: DiemThiXetTuyen.java — unique constraint (cccd, d_phuongthuc)
                String keyDiemThi = nv.getCccd() + "_" + nv.getPhuongThuc();
                DiemThiXetTuyen diemThi = cacheDiemThi.get(keyDiemThi);

                // Key tra NganhToHop: maNganh + "_" + maToHop
                // Nguồn: NganhToHop.java — field tbKeys = "manganh_matohop"
                String keyNganhToHop = nv.getMaNganh() + "_" + nv.getMaToHop();
                NganhToHop nganhToHop = cacheNganhToHop.get(keyNganhToHop);

                // BangQuyDoi theo phương thức
                // Nguồn: BangQuyDoi.phuongthuc — "DGNL" / "VSAT" / "THPT"
                String tenPhuongThuc = mapMaPhuongThucToTen(nv.getPhuongThuc());
                List<BangQuyDoi> bangQuyDois = cacheBangQuyDoi
                        .getOrDefault(tenPhuongThuc, Collections.emptyList());

                // Tính điểm
                calculator.tinhDiem(nv, diemThi, nganhToHop, bangQuyDois);

                daTinhXong.add(nv);
                success++;

            } catch (AppException ae) {
                error++;
                errors.add("NV id=" + nv.getIdNv()
                        + " CCCD=" + nv.getCccd()
                        + " maNganh=" + nv.getMaNganh()
                        + ": " + ae.getMessage());
            } catch (Exception e) {
                error++;
                errors.add("NV id=" + nv.getIdNv() + ": Lỗi không xác định - " + e.getMessage());
            }
        }

        // Batch update về DB
        if (!daTinhXong.isEmpty()) {
            batchUpdateNguyenVong(daTinhXong);
        }

        return new TinhDiemResult(success, error, errors);
    }

    // ================================================================
    // LOAD CACHE
    // ================================================================

    /**
     * Load toàn bộ DiemThiXetTuyen vào HashMap.
     * Key = cccd + "_" + phuongThuc
     * Nguồn key: DiemThiXetTuyen unique constraint (cccd, d_phuongthuc)
     */
    private Map<String, DiemThiXetTuyen> loadCacheDiemThi() {
        SessionManager sm = new SessionManager();
        try {
            sm.begin();
            List<DiemThiXetTuyen> list = sm.getSession()
                    .createQuery("FROM DiemThiXetTuyen", DiemThiXetTuyen.class)
                    .list();
            sm.commit();

            Map<String, DiemThiXetTuyen> cache = new HashMap<>();
            for (DiemThiXetTuyen d : list) {
                cache.put(d.getCccd() + "_" + d.getPhuongThuc(), d);
            }
            return cache;
        } catch (Exception e) {
            sm.rollback();
            throw new AppException("Lỗi load DiemThiXetTuyen!", e);
        } finally {
            sm.close();
        }
    }

    /**
     * Load toàn bộ NganhToHop vào HashMap.
     * Key = maNganh + "_" + maToHop
     * Nguồn key: NganhToHop.tbKeys = "manganh_matohop"
     */
    private Map<String, NganhToHop> loadCacheNganhToHop() {
        SessionManager sm = new SessionManager();
        try {
            sm.begin();
            List<NganhToHop> list = sm.getSession()
                    .createQuery("FROM NganhToHop", NganhToHop.class)
                    .list();
            sm.commit();

            Map<String, NganhToHop> cache = new HashMap<>();
            for (NganhToHop nth : list) {
                cache.put(nth.getMaNganh() + "_" + nth.getMaToHop(), nth);
            }
            return cache;
        } catch (Exception e) {
            sm.rollback();
            throw new AppException("Lỗi load NganhToHop!", e);
        } finally {
            sm.close();
        }
    }

    /**
     * Load toàn bộ BangQuyDoi vào HashMap theo phương thức.
     * Key = tên phương thức ("DGNL" / "VSAT" / "THPT")
     * Nguồn: BangQuyDoi.phuongthuc
     */
    private Map<String, List<BangQuyDoi>> loadCacheBangQuyDoi() {
        SessionManager sm = new SessionManager();
        try {
            sm.begin();
            List<BangQuyDoi> list = sm.getSession()
                    .createQuery("FROM BangQuyDoi", BangQuyDoi.class)
                    .list();
            sm.commit();

            Map<String, List<BangQuyDoi>> cache = new HashMap<>();
            for (BangQuyDoi bqd : list) {
                String key = bqd.getPhuongthuc();
                if (key != null) {
                    cache.computeIfAbsent(key, k -> new ArrayList<>()).add(bqd);
                }
            }
            return cache;
        } catch (Exception e) {
            sm.rollback();
            throw new AppException("Lỗi load BangQuyDoi!", e);
        } finally {
            sm.close();
        }
    }

    // ================================================================
    // BATCH UPDATE
    // ================================================================

    private void batchUpdateNguyenVong(List<NguyenVongXetTuyen> list) {
        SessionManager sm = new SessionManager();
        try {
            sm.begin();
            int batchSize = 500;
            for (int i = 0; i < list.size(); i++) {
                sm.getSession().merge(list.get(i));
                if (i > 0 && i % batchSize == 0) {
                    sm.getSession().flush();
                    sm.getSession().clear();
                }
            }
            sm.getSession().flush();
            sm.getSession().clear();
            sm.commit();
        } catch (Exception e) {
            sm.rollback();
            throw new AppException("Lỗi batch update NguyenVong!", e);
        } finally {
            sm.close();
        }
    }

    // ================================================================
    // HELPER: Map mã phương thức → tên trong BangQuyDoi
    // Nguồn mã: DiemThiXetTuyen.java comment — "3"=THPT, "4"=ĐGNL, "5"=V-SAT
    // Nguồn tên: BangQuyDoi.phuongthuc = "DGNL" / "VSAT" / "THPT"
    // ================================================================

    private String mapMaPhuongThucToTen(String maPhuongThuc) {
        return switch (maPhuongThuc) {
            case "3" -> "THPT";
            case "4" -> "DGNL";
            case "5" -> "VSAT";
            default  -> throw new AppException("Mã phương thức không hợp lệ: " + maPhuongThuc);
        };
    }

    // ================================================================
    // INNER CLASS: Kết quả
    // ================================================================

    public static class TinhDiemResult {
        public final int success;
        public final int error;
        public final List<String> errorDetails;

        public TinhDiemResult(int success, int error, List<String> errorDetails) {
            this.success      = success;
            this.error        = error;
            this.errorDetails = errorDetails;
        }

        @Override
        public String toString() {
            return String.format("✅ Thành công: %d  |  ❌ Lỗi: %d", success, error);
        }
    }
}