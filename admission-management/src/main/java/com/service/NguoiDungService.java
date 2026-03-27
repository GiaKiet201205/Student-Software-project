package com.service;

import com.entity.NguoiDung;
import com.entity.ThiSinhXetTuyen25;
import com.repository.NguoiDungRepository;

import java.util.List;

public class NguoiDungService {
    private static final NguoiDungRepository repository = new NguoiDungRepository();

    public List<NguoiDung> getAllByPage(int page, int pageSize) {
        return repository.findAllByPage(page, pageSize);
    }

    public long countAll() {
        return repository.countAll();
    }

    public NguoiDung getById(Integer id) {
        return repository.findById(id);
    }

    public NguoiDung create(NguoiDung entity) {
        return repository.save(entity);
    }

    public NguoiDung update(NguoiDung entity) {

        if (repository.existsByUsernameExceptId(entity.getUsername(), entity.getIdNguoiDung())) {
            throw new IllegalArgumentException("Tài khoản đã tồn tại!");
        }

        return repository.update(entity);
    }

    public void toggleActive(Integer id) {
        repository.toggleActive(id);
    }
}
