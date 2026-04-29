package com.controller;

import com.entity.DiemThiXetTuyen;
import com.service.DiemThiXetTuyenService;

import java.io.File;
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
    public String importDiemDgnlVsat(File file, String phuongThuc) {
        if (file == null || !file.getName().endsWith(".xlsx")) {
            return "File không hợp lệ. Vui lòng chọn file Excel đuôi .xlsx!";
        }
        return diemService.importDiemDgnlVsat(file, phuongThuc);
    }

    // Các hàm thêm, sửa, xóa, tìm kiếm của bạn giữ nguyên bên dưới...
    // public boolean add(DiemThiXetTuyen entity) { ... }
    // public boolean update(DiemThiXetTuyen entity) { ... }
    // public boolean delete(DiemThiXetTuyen entity) { ... }
    // public List<DiemThiXetTuyen> search(String keyword) { ... }
}