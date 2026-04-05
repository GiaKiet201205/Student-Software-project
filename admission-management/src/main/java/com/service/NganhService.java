package com.service;

import com.entity.Nganh;
import com.repository.NganhRepository;

import java.util.List;

public class NganhService {

    private final NganhRepository nganhRepository;

    public NganhService() {
        this.nganhRepository = new NganhRepository();
    }

    public List<Nganh> getAllNganh() {
        return nganhRepository.findAll();
    }

    public void themNganh(Nganh nganh) throws Exception {
        if (nganh.getMaNganh() == null || nganh.getMaNganh().trim().isEmpty()) {
            throw new Exception("Mã ngành không được để trống!");
        }
        nganhRepository.save(nganh);
    }

    public void capNhatNganh(Nganh nganh) throws Exception {
        if (nganh.getMaNganh() == null || nganh.getMaNganh().trim().isEmpty()) {
            throw new Exception("Mã ngành không hợp lệ để cập nhật!");
        }
        nganhRepository.update(nganh);
    }

    public void xoaNganh(String maNganh) throws Exception {
        if (maNganh == null || maNganh.trim().isEmpty()) {
            throw new Exception("Vui lòng chọn ngành cần xóa!");
        }
        nganhRepository.delete(maNganh);
    }
}