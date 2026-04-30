package com.controller;

import com.entity.NganhToHop;
import com.service.NganhToHopService;
import java.util.List;

public class NganhToHopController {
    private final NganhToHopService service = new NganhToHopService();

    public List<NganhToHop> getAll() { return service.getAll(); }
    public NganhToHop getById(Integer id) { return service.getById(id); }

    public boolean add(NganhToHop entity) {
        service.save(entity);
        return true;
    }

    public boolean update(NganhToHop entity) {
        service.update(entity);
        return true;
    }

    public boolean delete(NganhToHop entity) {
        service.delete(entity);
        return true;
    }

    public List<NganhToHop> search(String keyword) {
        return service.search(keyword);
    }
}