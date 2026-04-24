package com.controller;

import java.util.ArrayList;
import java.util.List;

import com.entity.DiemCongXetTuyen;
import com.service.DiemCongXetTuyenService;

public class DiemCongXetTuyenController {
    private final DiemCongXetTuyenService service = new DiemCongXetTuyenService();
    
    public List<DiemCongXetTuyen> getDiemCongXetTuyen() {
        return  service.getAll();
    }

    public DiemCongXetTuyen getDiemCongXetTuyenById(int id) {
        return service.getById(id);
    }

    public boolean addDiemCongXetTuyen(DiemCongXetTuyen entity) {
        if(entity != null){
            service.add(entity);
            return true;
        }
        return false;
    }

    public boolean saveDiemCongXetTuyen(DiemCongXetTuyen entity) {
        if(entity != null){
            service.add(entity);
            return true;
        }
        return false;
    }

    public boolean updateDiemCongXetTuyen(DiemCongXetTuyen entity) {
        if(entity != null){
            service.update(entity);
            return true;
        }
        return false;
    }

    public void deleteDiemCongXetTuyen(DiemCongXetTuyen entity) {
        service.delete(entity);
    }

    public List<DiemCongXetTuyen> searchDiemCongXetTuyen(String keyString) {
        List<DiemCongXetTuyen> result = new ArrayList<>();
        for (DiemCongXetTuyen dc : service.getAll()) {
            if (dc.getIdDiemCong() != null && dc.getIdDiemCong().toString().contains(keyString)
                || dc.getMaNganh() != null && dc.getMaNganh().toLowerCase().contains(keyString.toLowerCase())
                || dc.getMaToHop() != null && dc.getMaToHop().toLowerCase().contains(keyString.toLowerCase())
                || dc.getCccd() != null && dc.getCccd().toLowerCase().contains(keyString.toLowerCase())
            ) {
                result.add(dc);
            }
        }
        return result;
    }
}
