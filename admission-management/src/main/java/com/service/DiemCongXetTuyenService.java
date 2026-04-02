package com.service;

import java.util.List;
import com.entity.DiemCongXetTuyen;
import com.repository.DiemCongXetTuyenRepository;

public class DiemCongXetTuyenService {
    private final DiemCongXetTuyenRepository repo = new DiemCongXetTuyenRepository();

    public List<DiemCongXetTuyen> getAll() {
        return repo.findAll();
    }

    public DiemCongXetTuyen getById(int id) {
        return repo.findById(id);
    }

    public DiemCongXetTuyen add(DiemCongXetTuyen entity) {
        return repo.save(entity);
    }

    public DiemCongXetTuyen update(DiemCongXetTuyen entity) {
        return repo.update(entity);
    }

    public void delete(DiemCongXetTuyen entity) {
        repo.delete(entity);
    }
}
