package com.service;

import com.entity.ToHopMonThi;
import com.repository.ToHopMonThiRepository;

import java.util.List;

public class ToHopMonThiService {

    private final ToHopMonThiRepository toHopMonThiRepository;

    public ToHopMonThiService() {
        this.toHopMonThiRepository = new ToHopMonThiRepository();
    }

    public List<ToHopMonThi> getAllToHop() {
        return toHopMonThiRepository.findAll();
    }

    public void themToHop(ToHopMonThi toHopMonThi) throws Exception {
        if (toHopMonThi.getMaToHop() == null || toHopMonThi.getMaToHop().trim().isEmpty()) {
            throw new Exception("Mã tổ hợp không được để trống!");
        }
        toHopMonThiRepository.save(toHopMonThi);
    }

    public void capNhatToHop(ToHopMonThi toHopMonThi) throws Exception {
        if (toHopMonThi.getMaToHop() == null || toHopMonThi.getMaToHop().trim().isEmpty()) {
            throw new Exception("Mã tổ hợp không hợp lệ để cập nhật!");
        }
        toHopMonThiRepository.update(toHopMonThi);
    }

    public void xoaToHop(String maToHop) throws Exception {
        if (maToHop == null || maToHop.trim().isEmpty()) {
            throw new Exception("Vui lòng chọn tổ hợp cần xóa!");
        }
        toHopMonThiRepository.delete(maToHop);
    }
}