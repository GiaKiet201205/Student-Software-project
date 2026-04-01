package com.controller;

import com.entity.NguoiDung;
import com.service.NguoiDungService;

import java.util.List;

public class NguoiDungController {
    private final NguoiDungService service = new NguoiDungService();

    public List<NguoiDung> getAllByPage(int page, int pageSize) {
        return service.getAllByPage(page, pageSize);
    }

    public long countAll() {
        return service.countAll();
    }

    public NguoiDung getById(Integer id) {
        return service.getById(id);
    }

    public NguoiDung create(NguoiDung entity) {
        return service.create(entity);
    }

    public NguoiDung update(NguoiDung entity) {
        return service.update(entity);
    }

    public void toggleActive(Integer id) {
        service.toggleActive(id);
    }
}
