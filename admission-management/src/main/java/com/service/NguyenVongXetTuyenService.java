package com.service;

import java.util.List;

import com.entity.NguyenVongXetTuyen;
import com.repository.NguyenVongXetTuyenRepository;

public class NguyenVongXetTuyenService {
    private final NguyenVongXetTuyenRepository repo = new NguyenVongXetTuyenRepository();
    private static final int BATCH_SIZE = 1000;

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
    
     public void saveBatch(List<NguyenVongXetTuyen> list) {
        repo.saveBatchOptimized(list, BATCH_SIZE);
    }
    
    public void updateBatch(List<NguyenVongXetTuyen> list) {
        repo.updateKetQuaBatch(list, BATCH_SIZE);
    }
    
    public void deleteByIds(List<Integer> ids) {
        repo.deleteByIds(ids);
    }
    
    public void deleteByKeys(List<String> keys) {
        repo.deleteByKeys(keys);
    }
    
    public long count() {
        return repo.count();
    }
    
    public long countByCccd(String cccd) {
        return repo.countByCccd(cccd);
    }
}
