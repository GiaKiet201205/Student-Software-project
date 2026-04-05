package com.service;

import com.entity.DiemThiXetTuyen;
import com.repository.DiemThiXetTuyenRepository;
import java.util.List;

public class DiemThiXetTuyenService {
    private final DiemThiXetTuyenRepository repo = new DiemThiXetTuyenRepository();

    public List<DiemThiXetTuyen> getAll() { return repo.findAll(); }
    public DiemThiXetTuyen getById(Integer id) { return repo.findById(id); }
    public DiemThiXetTuyen save(DiemThiXetTuyen entity) { return repo.save(entity); }
    public DiemThiXetTuyen update(DiemThiXetTuyen entity) { return repo.update(entity); }
    public void delete(DiemThiXetTuyen entity) { repo.delete(entity); }
    public List<DiemThiXetTuyen> search(String keyword) { return repo.searchByKeyword(keyword); }
}