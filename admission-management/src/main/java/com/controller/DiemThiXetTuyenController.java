package com.controller;

import com.entity.DiemThiXetTuyen;
import com.service.DiemThiXetTuyenService;
import java.util.List;

public class DiemThiXetTuyenController {
    private final DiemThiXetTuyenService service = new DiemThiXetTuyenService();

    public List<DiemThiXetTuyen> getAll() { return service.getAll(); }
    public DiemThiXetTuyen getById(Integer id) { return service.getById(id); }

    public boolean add(DiemThiXetTuyen entity) {
        service.save(entity);
        return true;
    }

    public boolean update(DiemThiXetTuyen entity) {
        service.update(entity);
        return true;
    }

    public boolean delete(DiemThiXetTuyen entity) {
        service.delete(entity);
        return true;
    }

    public List<DiemThiXetTuyen> search(String keyword) {
        return service.search(keyword);
    }
}