package com.sgu.admission.service;

import com.sgu.admission.dto.KetQuaTinhDiemDTO;
import com.sgu.admission.entity.*;
import com.sgu.admission.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiemService {
    
    private final NganhToHopRepository nganhToHopRepository;
    private final NganhRepository nganhRepository;
    private final ToHopMonThiRepository toHopRepository;
    private final BangQuyDoiRepository bangQuyDoiRepository;
    private final DiemThiXetTuyenRepository diemThiRepository;
    private final DiemCongXetTuyenRepository diemCongRepository;
    
    /**
     * Tính điểm ĐGNL (HSA)
     * Phương thức: "4"
     */
    public KetQuaTinhDiemDTO calculateDgnl(
            String cccd,
            String maNganh,
            String maToHop,
            BigDecimal diemHSA,
            String doiTuong,
            String khuVuc) {
        
        KetQuaTinhDiemDTO result = new KetQuaTinhDiemDTO();
        result.setPhuongThuc("4");
        result.setMaNganh(maNganh);
        
        // Lấy info ngành
        Optional<Nganh> nganh = nganhRepository.findByMaNganh(maNganh);
        if (nganh.isEmpty()) {
            result.setGhiChu("Ngành không tồn tại");
            return result;
        }
        
        Nganh n = nganh.get();
        result.setTenNganh(n.getTenNganh());
        result.setMaToHop(maToHop);
        result.setDiemVsat(diemHSA);
        
        // Lấy thông tin tổ hợp
        if (maToHop != null) {
            Optional<ToHopMonThi> toHop = toHopRepository.findByMaToHop(maToHop);
            if (toHop.isPresent()) {
                result.setTenToHop(toHop.get().getTenToHop());
            }
        }
        
        // Lấy điểm cộng nếu có
        List<DiemCongXetTuyen> diemCongs = diemCongRepository.findByCccd(cccd);
        BigDecimal diemCong = BigDecimal.ZERO;
        for (DiemCongXetTuyen dc : diemCongs) {
            if (dc.getMaNganh().equals(maNganh) && dc.getPhuongThuc().equals("4")) {
                diemCong = dc.getDiemCC() != null ? dc.getDiemCC() : BigDecimal.ZERO;
                break;
            }
        }
        
        // Tính điểm xét tuyển = HSA + điểm cộng
        BigDecimal diemXetTuyen = diemHSA.add(diemCong);
        result.setDiemXetTuyen(diemXetTuyen);
        
        // So sánh với điểm chuẩn
        BigDecimal diemChuan = n.getDiemSan() != null ? n.getDiemSan() : BigDecimal.ZERO;
        result.setDiemChuanNganh(diemChuan);
        
        // Kiểm tra đạt
        if (diemXetTuyen.compareTo(diemChuan) >= 0) {
            result.setKetQua("Đạt");
        } else {
            result.setKetQua("Không đạt");
        }
        
        return result;
    }
    
    /**
     * Tính điểm THPT
     * Phương thức: "3"
     * Tính trung bình 3 môn theo tổ hợp
     */
    public KetQuaTinhDiemDTO calculateThpt(
            String cccd,
            String maNganh,
            String maToHop,
            BigDecimal diemMon1,
            BigDecimal diemMon2,
            BigDecimal diemMon3,
            String doiTuong,
            String khuVuc) {
        
        KetQuaTinhDiemDTO result = new KetQuaTinhDiemDTO();
        result.setPhuongThuc("3");
        result.setMaNganh(maNganh);
        result.setMaToHop(maToHop);
        
        // Lấy info ngành
        Optional<Nganh> nganh = nganhRepository.findByMaNganh(maNganh);
        if (nganh.isEmpty()) {
            result.setGhiChu("Ngành không tồn tại");
            return result;
        }
        
        Nganh n = nganh.get();
        result.setTenNganh(n.getTenNganh());
        
        // Lấy thông tin tổ hợp
        Optional<ToHopMonThi> toHop = toHopRepository.findByMaToHop(maToHop);
        if (toHop.isEmpty()) {
            result.setGhiChu("Tổ hợp không tồn tại");
            return result;
        }
        
        ToHopMonThi th = toHop.get();
        result.setTenToHop(th.getTenToHop());
        
        // Lấy mapping Nganh-Toàn để biết hệ số môn
        Optional<NganhToHop> mapping = nganhToHopRepository.findByMaNganhAndMaToHop(maNganh, maToHop);
        if (mapping.isEmpty()) {
            result.setGhiChu("Ngành không chấp nhận tổ hợp này");
            return result;
        }
        
        NganhToHop nToHop = mapping.get();
        
        // Tính điểm tổ hợp với hệ số
        BigDecimal diemTong = BigDecimal.ZERO;
        
        if (diemMon1 != null && nToHop.getHeSoMon1() != null) {
            diemTong = diemTong.add(diemMon1.multiply(new BigDecimal(nToHop.getHeSoMon1())));
        }
        if (diemMon2 != null && nToHop.getHeSoMon2() != null) {
            diemTong = diemTong.add(diemMon2.multiply(new BigDecimal(nToHop.getHeSoMon2())));
        }
        if (diemMon3 != null && nToHop.getHeSoMon3() != null) {
            diemTong = diemTong.add(diemMon3.multiply(new BigDecimal(nToHop.getHeSoMon3())));
        }
        
        // Chia cho tổng hệ số
        int totalHeS = (nToHop.getHeSoMon1() != null ? nToHop.getHeSoMon1() : 0) +
                       (nToHop.getHeSoMon2() != null ? nToHop.getHeSoMon2() : 0) +
                       (nToHop.getHeSoMon3() != null ? nToHop.getHeSoMon3() : 0);
        
        if (totalHeS > 0) {
            diemTong = diemTong.divide(new BigDecimal(totalHeS), 2, RoundingMode.HALF_UP);
        }
        
        result.setDiemTongToHop(diemTong);
        
        // Lấy điểm cộng nếu có
        List<DiemCongXetTuyen> diemCongs = diemCongRepository.findByCccd(cccd);
        BigDecimal diemCong = BigDecimal.ZERO;
        for (DiemCongXetTuyen dc : diemCongs) {
            if (dc.getMaNganh().equals(maNganh) && dc.getPhuongThuc().equals("3")) {
                diemCong = dc.getDiemCC() != null ? dc.getDiemCC() : BigDecimal.ZERO;
                break;
            }
        }
        
        // Tính điểm xét tuyển = điểm tổ hợp + điểm cộng
        BigDecimal diemXetTuyen = diemTong.add(diemCong);
        result.setDiemXetTuyen(diemXetTuyen);
        
        // So sánh với điểm chuẩn
        BigDecimal diemChuan = n.getDiemSan() != null ? n.getDiemSan() : BigDecimal.ZERO;
        result.setDiemChuanNganh(diemChuan);
        
        // Kiểm tra đạt
        if (diemXetTuyen.compareTo(diemChuan) >= 0) {
            result.setKetQua("Đạt");
        } else {
            result.setKetQua("Không đạt");
        }
        
        return result;
    }
    
    /**
     * Tính điểm V-SAT
     * Phương thức: "5"
     * Quy đổi từ thang 150 về thang 30
     */
    public KetQuaTinhDiemDTO calculateVsat(
            String cccd,
            String maNganh,
            String maToHop,
            BigDecimal diemVsatThang150,  // Điểm VSAT thang 150
            String doiTuong,
            String khuVuc) {
        
        KetQuaTinhDiemDTO result = new KetQuaTinhDiemDTO();
        result.setPhuongThuc("5");
        result.setMaNganh(maNganh);
        result.setMaToHop(maToHop);
        
        // Lấy info ngành
        Optional<Nganh> nganh = nganhRepository.findByMaNganh(maNganh);
        if (nganh.isEmpty()) {
            result.setGhiChu("Ngành không tồn tại");
            return result;
        }
        
        Nganh n = nganh.get();
        result.setTenNganh(n.getTenNganh());
        
        // Lấy thông tin tổ hợp
        Optional<ToHopMonThi> toHop = toHopRepository.findByMaToHop(maToHop);
        if (toHop.isEmpty()) {
            result.setGhiChu("Tổ hợp không tồn tại");
            return result;
        }
        
        result.setTenToHop(toHop.get().getTenToHop());
        result.setDiemVsat(diemVsatThang150);
        
        // Quy đổi từ thang 150 về thang 30
        // Công thức: Điểm thang 30 = (Điểm thang 150 / 150) * 30
        BigDecimal diemThang30 = diemVsatThang150
                .divide(new BigDecimal("150"), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("30"))
                .setScale(2, RoundingMode.HALF_UP);
        
        result.setDiemTongToHop(diemThang30);
        
        // Lấy điểm cộng nếu có
        List<DiemCongXetTuyen> diemCongs = diemCongRepository.findByCccd(cccd);
        BigDecimal diemCong = BigDecimal.ZERO;
        for (DiemCongXetTuyen dc : diemCongs) {
            if (dc.getMaNganh().equals(maNganh) && dc.getPhuongThuc().equals("5")) {
                diemCong = dc.getDiemCC() != null ? dc.getDiemCC() : BigDecimal.ZERO;
                break;
            }
        }
        
        // Tính điểm xét tuyển = điểm thang 30 + điểm cộng
        BigDecimal diemXetTuyen = diemThang30.add(diemCong);
        result.setDiemXetTuyen(diemXetTuyen);
        
        // So sánh với điểm chuẩn
        BigDecimal diemChuan = n.getDiemSan() != null ? n.getDiemSan() : BigDecimal.ZERO;
        result.setDiemChuanNganh(diemChuan);
        
        // Kiểm tra đạt
        if (diemXetTuyen.compareTo(diemChuan) >= 0) {
            result.setKetQua("Đạt");
        } else {
            result.setKetQua("Không đạt");
        }
        
        return result;
    }
}
