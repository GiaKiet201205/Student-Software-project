package com.controller;

import java.util.List;

import com.entity.NguyenVongXetTuyen;
import com.service.NguyenVongXetTuyenService;

public class NguyenVongXetTuyenController {
    private final NguyenVongXetTuyenService service = new NguyenVongXetTuyenService();
    
    public List<NguyenVongXetTuyen> getNguyenVongXetTuyen() {
        return  service.getAll();
    }

    public NguyenVongXetTuyen getNguyenVongXetTuyenById(int id) {
        return service.getById(id);
    }

    public boolean addNguyenVongXetTuyen(NguyenVongXetTuyen entity) {
        if(entity != null){
            service.add(entity);
            return true;
        }
        return false;
    }

    public boolean saveNguyenVongXetTuyen(NguyenVongXetTuyen entity) {
        if(entity != null){
            service.add(entity);
            return true;
        }
        return false;
    }

    public boolean updateNguyenVongXetTuyen(NguyenVongXetTuyen entity) {
        if(entity != null){
            service.update(entity);
            return true;
        }
        return false;
    }

    public void deleteNguyenVongXetTuyen(NguyenVongXetTuyen entity) {
        service.delete(entity);
    }

    public List<NguyenVongXetTuyen> searchNguyenVongXetTuyen(String keyString) {
        List<NguyenVongXetTuyen> result = new java.util.ArrayList<>();
        for (NguyenVongXetTuyen nv : service.getAll()) {
            if (nv.getIdNv() != null && nv.getIdNv().toString().contains(keyString)
                || nv.getPhuongThuc() != null && nv.getPhuongThuc().toLowerCase().contains(keyString.toLowerCase())
                || nv.getKetQua() != null && nv.getKetQua().toLowerCase().contains(keyString.toLowerCase())
                || nv.getCccd() != null && nv.getCccd().toLowerCase().contains(keyString.toLowerCase())
                || nv.getMaNganh() != null && nv.getMaNganh().toLowerCase().contains(keyString.toLowerCase())
            ) {
                result.add(nv);
            }
        }
        return result;
    }
    
}
