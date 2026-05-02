package com.service;

import java.util.List;

import com.entity.NguyenVongXetTuyen;
import com.repository.NguyenVongXetTuyenRepository;

public class NguyenVongXetTuyenService {
    private final NguyenVongXetTuyenRepository repo = new NguyenVongXetTuyenRepository();

    public List<NguyenVongXetTuyen> getAll() {
        return repo.findAll();
    }

    public NguyenVongXetTuyen getById(int id) {
        return repo.findById(id);
    }

    public NguyenVongXetTuyen add(NguyenVongXetTuyen entity) {
        return repo.save(entity);
    }

    public NguyenVongXetTuyen update(NguyenVongXetTuyen entity) {
        return repo.update(entity);
    }

    public void delete(NguyenVongXetTuyen entity) {
        repo.delete(entity);
    }
    
}
