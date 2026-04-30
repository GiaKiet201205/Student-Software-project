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

    public boolean add(DiemThiXetTuyen entity) {
        // 1. Kiểm tra rỗng
        if (entity.getCccd() == null || entity.getCccd().trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được để trống!");
        }
        if (entity.getPhuongThuc() == null || entity.getPhuongThuc().trim().isEmpty()) {
            throw new IllegalArgumentException("Phương thức xét tuyển không được để trống!");
        }

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