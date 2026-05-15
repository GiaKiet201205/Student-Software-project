package com.controller;

import com.entity.DiemThiXetTuyen;
import com.service.DiemThiXetTuyenService;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class DiemThiXetTuyenController {

    private final DiemThiXetTuyenService diemService;

    public DiemThiXetTuyenController() {
        this.diemService = new DiemThiXetTuyenService();
    }

    public List<DiemThiXetTuyen> getAll() {
        return diemService.layTatCaDiem();
    }

    /**
     * Import điểm THPT (Phương thức 3)
     */
    public String importDiemThpt(File file, String phuongThuc) {
        if (file == null || !file.getName().endsWith(".xlsx")) {
            return "File không hợp lệ. Vui lòng chọn file Excel đuôi .xlsx!";
        }
        return diemService.importDiemThpt(file, phuongThuc);
    }

    /**
     * Import điểm V-SAT hoặc ĐGNL (Phương thức 4, 5)
     */
    public String importDiemVsat(File file, String phuongThuc) {
        if (file == null || !file.getName().endsWith(".xlsx")) {
            return "File không hợp lệ. Vui lòng chọn file Excel đuôi .xlsx!";
        }
        return diemService.importDiemVsat(file, phuongThuc);
    }
    public String importDiemDgnl(File file, String phuongThuc) {
        if (file == null || !file.getName().endsWith(".xlsx")) {
            return "File không hợp lệ.";
        }
        return diemService.importDiemDgnl(file, phuongThuc);
    }

    // =====================================================================
    // CÁC HÀM CRUD (THÊM, SỬA, XÓA, TÌM KIẾM, LẤY ID)
    // =====================================================================

    public DiemThiXetTuyen getById(Integer id) {
        if (id == null) return null;
        return diemService.getById(id);
    }

    // =====================================================================
    // HÀM PHỤ TRỢ: KIỂM TRA ĐIỂM MIN (0) VÀ MAX (Tùy phương thức)
    // =====================================================================
    private void kiemTraKhoangDiem(BigDecimal diem, String tenMon, double maxScore) {
        if (diem != null) {
            double value = diem.doubleValue();
            if (value < 0 || value > maxScore) {
                throw new IllegalArgumentException("Điểm môn " + tenMon + " không hợp lệ! (Phải nằm trong khoảng từ 0 đến " + maxScore + ")");
            }
        }
    }

    // =====================================================================
    // HÀM PHỤ TRỢ: BẮT BUỘC PHẢI CÓ ÍT NHẤT 1 ĐIỂM
    // =====================================================================
    private void kiemTraCoItNhatMotDiem(DiemThiXetTuyen entity, String tenPhuongThuc) {
        boolean coBatKyDiemNao = entity.getToan() != null || entity.getVan() != null ||
                entity.getN1Thi() != null || entity.getN1CC() != null ||
                entity.getLy() != null || entity.getHoa() != null ||
                entity.getSinh() != null || entity.getSu() != null ||
                entity.getDia() != null || entity.getKtpl() != null ||
                entity.getDiemNangKhieu1() != null || entity.getDiemNangKhieu2() != null ||
                entity.getDiemNangKhieu3() != null || entity.getDiemNangKhieu4() != null;
        if (!coBatKyDiemNao) {
            throw new IllegalArgumentException("Phương thức " + tenPhuongThuc + " yêu cầu phải nhập ít nhất 1 đầu điểm!");
        }
    }

    // =====================================================================
    // HÀM KIỂM TRA LOGIC CHÍNH
    // =====================================================================
    private void validateDiemTheoPhuongThuc(DiemThiXetTuyen entity) {
        String pt = entity.getPhuongThuc();

        // 1. NẾU LÀ ĐÁNH GIÁ NĂNG LỰC (Mã 4)
        if ("4".equals(pt)) {
            if (entity.getDiemNangLuc() == null) {
                throw new IllegalArgumentException("Phương thức Đánh giá Năng lực yêu cầu phải nhập Điểm ĐGNL!");
            }
            // Dựa vào file ảnh điểm ĐGNL 95.00 của bạn, tôi giả định thang điểm ĐGNL trường bạn xét là 150 (hoặc 100).
            // Bạn có thể đổi số 150.0 thành 1200.0 nếu trường dùng form của ĐHQG TP.HCM nhé!
            kiemTraKhoangDiem(entity.getDiemNangLuc(), "ĐGNL", 1500.0);
        }

        // 2. NẾU LÀ XÉT ĐIỂM THPT (Mã 3) -> Thang điểm 10
        else if ("3".equals(pt)) {
            kiemTraCoItNhatMotDiem(entity, "Xét điểm THPT");

            // Validate thang điểm 10 cho toàn bộ các môn
            kiemTraKhoangDiem(entity.getToan(), "Toán", 10.0);
            kiemTraKhoangDiem(entity.getVan(), "Ngữ Văn", 10.0);
            kiemTraKhoangDiem(entity.getLy(), "Vật Lý", 10.0);
            kiemTraKhoangDiem(entity.getHoa(), "Hóa Học", 10.0);
            kiemTraKhoangDiem(entity.getSinh(), "Sinh Học", 10.0);
            kiemTraKhoangDiem(entity.getSu(), "Lịch Sử", 10.0);
            kiemTraKhoangDiem(entity.getDia(), "Địa Lý", 10.0);
            kiemTraKhoangDiem(entity.getKtpl(), "GDCD / KTPL", 10.0);
            kiemTraKhoangDiem(entity.getN1Thi(), "Ngoại Ngữ", 10.0);

            // Điểm năng khiếu thường cũng trên thang 10
            kiemTraKhoangDiem(entity.getDiemNangKhieu1(), "Năng Khiếu 1", 10.0);
            kiemTraKhoangDiem(entity.getDiemNangKhieu2(), "Năng Khiếu 2", 10.0);
            kiemTraKhoangDiem(entity.getDiemNangKhieu3(), "Năng Khiếu 3", 10.0);
            kiemTraKhoangDiem(entity.getDiemNangKhieu4(), "Năng Khiếu 4", 10.0);
        }

        // 3. NẾU LÀ V-SAT (Mã 5) -> Thang điểm 150 mỗi môn
        else if ("5".equals(pt)) {
            kiemTraCoItNhatMotDiem(entity, "V-SAT");

            // Validate thang điểm 150 cho các môn V-SAT
            kiemTraKhoangDiem(entity.getToan(), "Toán (V-SAT)", 150.0);
            kiemTraKhoangDiem(entity.getLy(), "Vật Lý (V-SAT)", 150.0);
            kiemTraKhoangDiem(entity.getHoa(), "Hóa Học (V-SAT)", 150.0);
            kiemTraKhoangDiem(entity.getSinh(), "Sinh Học (V-SAT)", 150.0);
            kiemTraKhoangDiem(entity.getSu(), "Lịch Sử (V-SAT)", 150.0);
            kiemTraKhoangDiem(entity.getDia(), "Địa Lý (V-SAT)", 150.0);
            kiemTraKhoangDiem(entity.getN1Thi(), "Tiếng Anh (V-SAT)", 150.0);
        }
    }
    public boolean add(DiemThiXetTuyen entity) {
        // 1. Kiểm tra rỗng
        if (entity.getCccd() == null || entity.getCccd().trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống!");
        }
        if (entity.getPhuongThuc() == null || entity.getPhuongThuc().trim().isEmpty()) {
            throw new IllegalArgumentException("Phương thức xét tuyển không được để trống!");
        }
        if (entity.getSoBaoDanh() == null || entity.getSoBaoDanh().trim().isEmpty()) {
            throw new IllegalArgumentException("Số báo danh không được để trống");
        }
        validateDiemTheoPhuongThuc(entity);
        // 2. Gọi Service xử lý
        return diemService.themDiem(entity);
    }

    public boolean update(DiemThiXetTuyen entity) {
        // Kiểm tra xem có đúng là đang sửa một bản ghi đã tồn tại không
        if (entity.getIdDiemThi() == null) {
            throw new IllegalArgumentException("Không xác định được ID hồ sơ cần sửa!");
        }
        if (entity.getCccd() == null || entity.getCccd().trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống!");
        }
        //if (entity.getPhuongThuc() ==)

        return diemService.capNhatDiem(entity);
    }

    public boolean delete(DiemThiXetTuyen entity) {
        if (entity == null || entity.getIdDiemThi() == null) {
            throw new IllegalArgumentException("Dữ liệu xóa không hợp lệ!");
        }
        return diemService.xoaDiem(entity);
    }

    public List<DiemThiXetTuyen> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll(); // Nếu không nhập gì thì load lại toàn bộ bảng
        }
        return diemService.timKiem(keyword.trim());
    }
}