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
            if (nv.getIdNv() != null && nv.getIdNv().toString().equals(keyString)
                || nv.getPhuongThuc() != null && nv.getPhuongThuc().toLowerCase().equals(keyString.toLowerCase())
                || nv.getKetQua() != null && nv.getKetQua().toLowerCase().equals(keyString.toLowerCase())
                || nv.getCccd() != null && nv.getCccd().toLowerCase().equals(keyString.toLowerCase())
                || nv.getMaNganh() != null && nv.getMaNganh().toLowerCase().equals(keyString.toLowerCase())
                || nv.getKetQua() != null && nv.getKetQua().toLowerCase().equals(keyString.toLowerCase())
            ) {
                result.add(nv);
            }
            if(keyString.contains(",")){
                String[] parts = keyString.split(",");
                boolean matchAll = true;

                for (String part : parts) {
                    String p = part.trim().toLowerCase();
                    boolean match =
                        (nv.getIdNv() != null && nv.getIdNv().toString().equals(p)) ||
                        (nv.getPhuongThuc() != null && nv.getPhuongThuc().toLowerCase().equals(p)) ||
                        (nv.getKetQua() != null && nv.getKetQua().toLowerCase().equals(p)) ||
                        (nv.getCccd() != null && nv.getCccd().toLowerCase().equals(p)) ||
                        (nv.getMaNganh() != null && nv.getMaNganh().toLowerCase().equals(p));
                    
                        if (!match) {
                        matchAll = false;
                        break;
                    }
                }
                if (matchAll) {
                    result.add(nv);
                }
            }
        }
        return result;
    }
    
      
    public void saveBatch(List<NguyenVongXetTuyen> list) {
        service.saveBatch(list);
    }
    
    public void updateBatch(List<NguyenVongXetTuyen> list) {
        service.updateBatch(list);
    }
    
    public void deleteByIds(List<Integer> ids) {
        service.deleteByIds(ids);
    }
    
    public void deleteByKeys(List<String> keys) {
        service.deleteByKeys(keys);
    }
    
    public long count() {
        return service.count();
    }
    

}
