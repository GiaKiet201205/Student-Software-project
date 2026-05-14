package com.service.result_xetdiem;

import com.entity.BangQuyDoi;
import com.entity.DiemThiXetTuyen;
import com.entity.NganhToHop;
import com.entity.NguyenVongXetTuyen;
import com.exception.AppException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service tính điểm xét tuyển cho một NguyenVongXetTuyen.
 *
 * Mục tiêu: quy đổi điểm về cùng hệ quy chiếu (thang 30 của THPT)
 * để dễ so sánh xét tuyển giữa các phương thức.
 *
 * Output sau khi tính:
 *   - diemThXT  = ĐTHXT  (điểm tổ hợp xét tuyển, trước quy đổi tổ hợp gốc)
 *   - diemUuTienQD = ĐTHGXT (điểm tổ hợp gốc xét tuyển = ĐTHXT - doLech)
 *   - diemCong  = điểm cộng chứng chỉ Tiếng Anh (nếu có, nếu tổ hợp không có môn TA)
 *
 * Nguồn công thức:
 *   - Nội suy tuyến tính: Quy_doi_diem_thi_V-SAT_2025.pdf trang 1
 *   - ĐTHXT THPT/VSAT: cac_cong_thuc_tinh.pdf trang 2-3
 *   - ĐTHGXT + doLech: cac_cong_thuc_tinh.pdf trang 1 + NganhToHop.doLech
 *   - Điểm cộng TA: bangQUyDoiTA_2025.pdf
 */
public class DiemXetTuyenCalculator {

    // Mã phương thức (nguồn: DiemThiXetTuyen.java comment)
    private static final String PT_THPT = "3";
    private static final String PT_DGNL = "4";
    private static final String PT_VSAT = "5";

    // ================================================================
    // ENTRY POINT CHÍNH
    // ================================================================

    /**
     * Tính điểm cho một nguyện vọng và cập nhật trực tiếp vào object.
     *
     * @param nv          NguyenVongXetTuyen cần tính (sẽ được set diemThXT, diemUuTienQD, diemCong)
     * @param diemThi     DiemThiXetTuyen của thí sinh (cùng CCCD, cùng phương thức)
     * @param nganhToHop  NganhToHop tương ứng (maNganh + maToHop)
     * @param bangQuyDois Toàn bộ BangQuyDoi của phương thức đó (load sẵn từ DB)
     */
    public void tinhDiem(
            NguyenVongXetTuyen nv,
            DiemThiXetTuyen diemThi,
            NganhToHop nganhToHop,
            List<BangQuyDoi> bangQuyDois) {

        validate(nv, diemThi, nganhToHop);

        String phuongThuc = nv.getPhuongThuc();

        BigDecimal dthxt;
        BigDecimal dthgxt;

        switch (phuongThuc) {
            case PT_THPT -> {
                dthxt  = tinhDTHXT_THPT(diemThi, nganhToHop);
                dthgxt = dthxt.subtract(safeDoLech(nganhToHop));
            }
            case PT_VSAT -> {
                dthxt  = tinhDTHXT_VSAT(diemThi, nganhToHop, bangQuyDois);
                dthgxt = dthxt.subtract(safeDoLech(nganhToHop));
            }
            case PT_DGNL -> {
                // Nguồn: cac_cong_thuc_tinh.pdf trang 3
                // ĐTHGXTĐGNL = ĐTHXTĐGNL (không quy đổi tổ hợp gốc)
                dthxt  = tinhDTHXT_DGNL(diemThi, nganhToHop, bangQuyDois);
                dthgxt = dthxt;
            }
            default -> throw new AppException(
                    "Phương thức không hợp lệ: " + phuongThuc + ". Chỉ hỗ trợ 3 (THPT), 4 (ĐGNL), 5 (VSAT).");
        }

        // Làm tròn 5 chữ số thập phân
        dthxt  = dthxt.setScale(5, RoundingMode.HALF_UP);
        dthgxt = dthgxt.setScale(5, RoundingMode.HALF_UP);

        // Điểm cộng chứng chỉ TA
        // Nguồn: bangQUyDoiTA_2025.pdf + NganhToHop.n1
        BigDecimal diemCongTA = tinhDiemCongTA(diemThi, nganhToHop, phuongThuc);

        // Ghi kết quả vào NguyenVongXetTuyen
        nv.setDiemThXT(dthxt);
        nv.setDiemUuTienQD(dthgxt);
        nv.setDiemCong(diemCongTA);
    }

    // ================================================================
    // PHƯƠNG THỨC THPT
    // Nguồn: cac_cong_thuc_tinh.pdf trang 3
    // ĐTHXT = [(d1×w1 + d2×w2 + d3×w3) / W] × 3
    // d1,d2,d3: điểm thi gốc thang 10
    // w1,w2,w3: hệ số môn từ NganhToHop.heSoMon1/2/3
    // ================================================================

    private BigDecimal tinhDTHXT_THPT(DiemThiXetTuyen diemThi, NganhToHop nganhToHop) {
        // Lấy điểm 3 môn theo tên môn trong NganhToHop
        // → chỉ lấy đúng môn của tổ hợp, bỏ qua môn khác dù = 0
        BigDecimal d1 = diemThi.getDiemByMaMon(nganhToHop.getMon1());
        BigDecimal d2 = diemThi.getDiemByMaMon(nganhToHop.getMon2());
        BigDecimal d3 = diemThi.getDiemByMaMon(nganhToHop.getMon3());

        int w1 = nganhToHop.getHeSoMon1() != null ? nganhToHop.getHeSoMon1() : 1;
        int w2 = nganhToHop.getHeSoMon2() != null ? nganhToHop.getHeSoMon2() : 1;
        int w3 = nganhToHop.getHeSoMon3() != null ? nganhToHop.getHeSoMon3() : 1;
        int W  = w1 + w2 + w3;

        // [(d1×w1 + d2×w2 + d3×w3) / W] × 3
        BigDecimal tongDiem = d1.multiply(BigDecimal.valueOf(w1))
                .add(d2.multiply(BigDecimal.valueOf(w2)))
                .add(d3.multiply(BigDecimal.valueOf(w3)));

        return tongDiem
                .divide(BigDecimal.valueOf(W), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(3));
    }

    // ================================================================
    // PHƯƠNG THỨC VSAT
    // Nguồn: cac_cong_thuc_tinh.pdf trang 2
    // ĐTHXT = [(d1_qd×w1 + d2_qd×w2 + d3_qd×w3) / W] × 3
    // d_qd: điểm VSAT đã quy đổi về thang 10 qua nội suy bảng BangQuyDoi
    // ================================================================

    private BigDecimal tinhDTHXT_VSAT(
            DiemThiXetTuyen diemThi,
            NganhToHop nganhToHop,
            List<BangQuyDoi> bangQuyDois) {

        // Quy đổi từng môn VSAT → thang 10
        BigDecimal d1 = quyDoiVSAT(diemThi.getDiemByMaMon(nganhToHop.getMon1()),
                nganhToHop.getMon1(), bangQuyDois);
        BigDecimal d2 = quyDoiVSAT(diemThi.getDiemByMaMon(nganhToHop.getMon2()),
                nganhToHop.getMon2(), bangQuyDois);
        BigDecimal d3 = quyDoiVSAT(diemThi.getDiemByMaMon(nganhToHop.getMon3()),
                nganhToHop.getMon3(), bangQuyDois);

        int w1 = nganhToHop.getHeSoMon1() != null ? nganhToHop.getHeSoMon1() : 1;
        int w2 = nganhToHop.getHeSoMon2() != null ? nganhToHop.getHeSoMon2() : 1;
        int w3 = nganhToHop.getHeSoMon3() != null ? nganhToHop.getHeSoMon3() : 1;
        int W  = w1 + w2 + w3;

        BigDecimal tongDiem = d1.multiply(BigDecimal.valueOf(w1))
                .add(d2.multiply(BigDecimal.valueOf(w2)))
                .add(d3.multiply(BigDecimal.valueOf(w3)));

        return tongDiem
                .divide(BigDecimal.valueOf(W), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(3));
    }

    // ================================================================
    // PHƯƠNG THỨC ĐGNL
    // Nguồn: cac_cong_thuc_tinh.pdf trang 3
    // ĐTHXT = nội suy điểm ĐGNL → thang 30 qua BangQuyDoi (DGNL + tohop ngành)
    // Lấy điểm ĐGNL từ DiemThiXetTuyen.diemNangLuc (field NL1)
    // Nguồn field: DiemThiXetTuyen.java comment "Điểm tổng bài thi ĐGNL. Thang điểm 1200"
    // ================================================================

    private BigDecimal tinhDTHXT_DGNL(
            DiemThiXetTuyen diemThi,
            NganhToHop nganhToHop,
            List<BangQuyDoi> bangQuyDois) {

        BigDecimal diemDGNL = diemThi.getDiemNangLuc();
        if (diemDGNL == null || diemDGNL.compareTo(BigDecimal.ZERO) == 0) {
            throw new AppException("Thí sinh CCCD=" + diemThi.getCccd()
                    + " không có điểm ĐGNL (NL1 = null hoặc 0).");
        }

        // Tra bảng BangQuyDoi với phuongthuc='DGNL', tohop=maToHop của NganhToHop
        // Nguồn: xt_bachphanvi.pdf — bảng bách phân vị ĐGNL → THPT theo từng tổ hợp
        return noiSuyTuBang(diemDGNL, bangQuyDois,
                "ĐGNL tohop=" + nganhToHop.getMaToHop());
    }

    // ================================================================
    // NỘI SUY TUYẾN TÍNH
    // Nguồn công thức: Quy_doi_diem_thi_V-SAT_2025.pdf trang 1
    //   y = c + ((x - a) / (b - a)) × (d - c)
    // Trong đó:
    //   x     = điểm đầu vào (VSAT hoặc ĐGNL)
    //   [a,b] = khoảng nguồn (diemA, diemB trong BangQuyDoi)
    //   [c,d] = khoảng đích  (diemC, diemD trong BangQuyDoi)
    // ================================================================

    /**
     * Quy đổi 1 môn VSAT về thang 10 bằng nội suy tuyến tính.
     * Tra bảng BangQuyDoi với phuongthuc='VSAT', mon=maMon.
     *
     * Lưu ý: môn "N1" trong NganhToHop map với field n1CC trong DiemThiXetTuyen
     * (đã được xử lý sẵn trong getDiemByMaMon → lấy max(n1Thi, n1CC))
     * Nguồn: DiemThiXetTuyen.java comment field N1_CC
     */
    private BigDecimal quyDoiVSAT(BigDecimal diemVSAT, String maMon,
                                   List<BangQuyDoi> bangQuyDois) {
        if (diemVSAT == null || diemVSAT.compareTo(BigDecimal.ZERO) == 0) {
            // Môn không thi hoặc 0 — trả về 0
            // Logic: NganhToHop đã xác định đây là môn của tổ hợp,
            // nếu điểm = 0 thì tính 0 vào công thức (thí sinh thi được 0)
            return BigDecimal.ZERO;
        }
        return noiSuyTuBang(diemVSAT, bangQuyDois, "VSAT mon=" + maMon);
    }

    /**
     * Thực hiện nội suy tuyến tính từ danh sách BangQuyDoi.
     * Tìm khoảng [diemA, diemB] chứa x, rồi nội suy ra y trong [diemC, diemD].
     *
     * Công thức: y = c + ((x - a) / (b - a)) × (d - c)
     * Nguồn: Quy_doi_diem_thi_V-SAT_2025.pdf trang 1
     *
     * Lưu ý về điều kiện khoảng: a < x ≤ b (bao gồm cận trên, không bao cận dưới)
     * Nguồn: Quy_doi_diem_thi_V-SAT_2025.pdf: "a < x ≤ b"
     */
    private BigDecimal noiSuyTuBang(BigDecimal x, List<BangQuyDoi> bangQuyDois,
                                     String moTa) {
        for (BangQuyDoi row : bangQuyDois) {
            BigDecimal a = row.getDiemA(); // cận dưới khoảng nguồn
            BigDecimal b = row.getDiemB(); // cận trên khoảng nguồn
            BigDecimal c = row.getDiemC(); // cận dưới khoảng đích
            BigDecimal d = row.getDiemD(); // cận trên khoảng đích

            if (a == null || b == null || c == null || d == null) continue;

            // Điều kiện: a < x ≤ b
            boolean trongKhoang = x.compareTo(a) > 0 && x.compareTo(b) <= 0;

            if (trongKhoang) {
                BigDecimal bMinusA = b.subtract(a);

                // Tránh chia 0 khi a == b (khoảng điểm điểm)
                if (bMinusA.compareTo(BigDecimal.ZERO) == 0) {
                    return c.add(d).divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP);
                }

                // y = c + ((x - a) / (b - a)) × (d - c)
                BigDecimal ty_le = x.subtract(a)
                        .divide(bMinusA, 10, RoundingMode.HALF_UP);
                BigDecimal dMinusC = d.subtract(c);

                return c.add(ty_le.multiply(dMinusC))
                        .setScale(5, RoundingMode.HALF_UP);
            }
        }

        // Không tìm thấy khoảng phù hợp
        throw new AppException("Không tìm thấy khoảng quy đổi cho " + moTa
                + " với điểm = " + x
                + ". Kiểm tra lại dữ liệu bảng quy đổi.");
    }

    // ================================================================
    // ĐIỂM CỘNG CHỨNG CHỈ TIẾNG ANH
    // Nguồn: bangQUyDoiTA_2025.pdf
    // Chỉ cộng khi tổ hợp KHÔNG có môn Tiếng Anh
    // Nguồn xác định: NganhToHop.n1 = false/null → không có môn TA
    // Điểm cộng lưu sẵn trong DiemThiXetTuyen.n1CC
    //   (n1CC = max(n1Thi, điểm quy đổi chứng chỉ) — nguồn: DiemThiXetTuyen.java)
    // Quy tắc: nếu n1CC > 0 và tổ hợp không có N1 → tính điểm cộng
    // ================================================================

    private BigDecimal tinhDiemCongTA(DiemThiXetTuyen diemThi,
                                       NganhToHop nganhToHop,
                                       String phuongThuc) {
        // Tổ hợp có môn Tiếng Anh → không cộng thêm
        // Nguồn: NganhToHop.n1 (Boolean)
        if (Boolean.TRUE.equals(nganhToHop.getN1())) {
            return BigDecimal.ZERO;
        }

        // Lấy điểm quy đổi chứng chỉ TA
        // Nguồn: DiemThiXetTuyen.n1CC — "max(N1_THI, điểm quy đổi chứng chỉ NN)"
        BigDecimal n1CC = diemThi.getN1CC();
        if (n1CC == null || n1CC.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // Tra bảng quy đổi điểm cộng theo phương thức
        // Nguồn: bangQUyDoiTA_2025.pdf
        // n1CC là điểm thi môn TA thang 10 (8.0 / 9.0 / 10.0)
        // → map sang điểm cộng theo phương thức
        return switch (phuongThuc) {
            case PT_THPT -> {
                // THPT: n1CC là điểm thang 10
                // 8.0 → +1.0 | 9.0 → +1.5 | 10.0 → +2.0
                if (n1CC.compareTo(BigDecimal.valueOf(10.0)) >= 0)
                    yield BigDecimal.valueOf(2.0);
                else if (n1CC.compareTo(BigDecimal.valueOf(9.0)) >= 0)
                    yield BigDecimal.valueOf(1.5);
                else if (n1CC.compareTo(BigDecimal.valueOf(8.0)) >= 0)
                    yield BigDecimal.valueOf(1.0);
                else yield BigDecimal.ZERO;
            }
            case PT_VSAT -> {
                // VSAT: n1CC là điểm đã quy đổi về thang 10
                // dùng cùng ngưỡng như THPT
                if (n1CC.compareTo(BigDecimal.valueOf(10.0)) >= 0)
                    yield BigDecimal.valueOf(2.0);
                else if (n1CC.compareTo(BigDecimal.valueOf(9.0)) >= 0)
                    yield BigDecimal.valueOf(1.5);
                else if (n1CC.compareTo(BigDecimal.valueOf(8.0)) >= 0)
                    yield BigDecimal.valueOf(1.0);
                else yield BigDecimal.ZERO;
            }
            case PT_DGNL -> {
                // ĐGNL: điểm cộng thẳng vào thang 1200 trước khi quy đổi
                // Nguồn: bangQUyDoiTA_2025.pdf: Mức 1=40, Mức 2=60, Mức 3=80
                // n1CC ở đây là điểm thang 10 (đã quy đổi từ chứng chỉ)
                // → map ngưỡng 8.0/9.0/10.0 tương tự
                if (n1CC.compareTo(BigDecimal.valueOf(10.0)) >= 0)
                    yield BigDecimal.valueOf(80);
                else if (n1CC.compareTo(BigDecimal.valueOf(9.0)) >= 0)
                    yield BigDecimal.valueOf(60);
                else if (n1CC.compareTo(BigDecimal.valueOf(8.0)) >= 0)
                    yield BigDecimal.valueOf(40);
                else yield BigDecimal.ZERO;
            }
            default -> BigDecimal.ZERO;
        };
    }

    // ================================================================
    // HELPERS
    // ================================================================

    /**
     * Lấy doLech từ NganhToHop, trả về 0 nếu null.
     * Nguồn: NganhToHop.doLech — độ lệch đã tính sẵn cho cặp (maToHop → tohopGoc ngành)
     * Công thức dùng: ĐTHGXT = ĐTHXT - doLech
     * Nguồn công thức: cac_cong_thuc_tinh.pdf trang 1
     */
    private BigDecimal safeDoLech(NganhToHop nganhToHop) {
        return nganhToHop.getDoLech() != null
                ? nganhToHop.getDoLech()
                : BigDecimal.ZERO;
    }

    private void validate(NguyenVongXetTuyen nv, DiemThiXetTuyen diemThi,
                           NganhToHop nganhToHop) {
        if (nv.getPhuongThuc() == null) {
            throw new AppException("NguyenVong id=" + nv.getIdNv() + " thiếu phương thức.");
        }
        if (diemThi == null) {
            throw new AppException("Không tìm thấy điểm thi cho CCCD=" + nv.getCccd()
                    + " phương thức=" + nv.getPhuongThuc());
        }
        if (nganhToHop == null) {
            throw new AppException("Không tìm thấy NganhToHop cho maNganh="
                    + nv.getMaNganh() + " maToHop=" + nv.getMaToHop());
        }
        if (nganhToHop.getMon1() == null || nganhToHop.getMon2() == null
                || nganhToHop.getMon3() == null) {
            throw new AppException("NganhToHop maNganh=" + nganhToHop.getMaNganh()
                    + " maToHop=" + nganhToHop.getMaToHop() + " thiếu thông tin môn.");
        }
    }
}