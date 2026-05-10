package com.controller;

import com.entity.ThiSinhXetTuyen25;
import com.service.ThiSinhXetTuyen25Service;

import java.io.File;
import java.util.List;

public class ThiSinhXetTuyen25Controller {

    private final ThiSinhXetTuyen25Service service = new ThiSinhXetTuyen25Service();

    public List<ThiSinhXetTuyen25> getAllByPage(int page, int pageSize) {
        return service.getAllByPage(page, pageSize);
    }

    public long countAll() {
        return service.countAll();
    }

    public List<ThiSinhXetTuyen25> searchByCccdOrNameByPage(String keyword, int page, int pageSize) {
        return service.searchByCccdOrNameByPage(keyword, page, pageSize);
    }

    public long countSearch(String keyword) {
        return service.countSearch(keyword);
    }

    public ThiSinhXetTuyen25 getById(Integer id) {
        return service.getById(id);
    }

    public ThiSinhXetTuyen25 create(ThiSinhXetTuyen25 entity) {
        return service.create(entity);
    }

    public ThiSinhXetTuyen25 update(ThiSinhXetTuyen25 entity) {
        return service.update(entity);
    }

    public void delete(Integer id) {
        service.delete(id);
    }

    // Import Excel
    public int importFromExcel(File file) {
        return service.importFromExcel(file);
    }

    public boolean importFromExcelFast(File file){
        return service.importFromExcelFast(file);
    }
}
