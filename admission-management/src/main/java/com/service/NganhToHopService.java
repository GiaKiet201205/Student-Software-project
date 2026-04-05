package com.service;

import com.entity.NganhToHop;
import com.repository.NganhToHopRepository;
import java.util.List;

public class NganhToHopService {
    private final NganhToHopRepository repo = new NganhToHopRepository();

    public List<NganhToHop> getAll() { return repo.findAll(); }
    public NganhToHop getById(Integer id) { return repo.findById(id); }
    public NganhToHop save(NganhToHop entity) { return repo.save(entity); }
    public NganhToHop update(NganhToHop entity) { return repo.update(entity); }
    public void delete(NganhToHop entity) { repo.delete(entity); }
    public List<NganhToHop> search(String keyword) { return repo.searchByKeyword(keyword); }
}