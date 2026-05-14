package com.controller;

import com.entity.BangQuyDoi;
import com.exception.AppException;
import com.service.BangQuyDoiService;

import java.io.File;
import java.util.List;

public class BangQuyDoiController {

    private final BangQuyDoiService service;

    public BangQuyDoiController() {
        service = new BangQuyDoiService();
    }

    // ================= READ =================

    public List<BangQuyDoi> getAll() {
        return service.getAll();
    }

    public List<BangQuyDoi> search(String keyword) {
        return service.search(keyword);
    }

    public List<BangQuyDoi> getByPhuongThuc(String phuongthuc) {
        return service.getByPhuongThuc(phuongthuc);
    }

    public List<BangQuyDoi> getByPhuongThucAndToHop(
            String phuongthuc,
            String tohop
    ) {
        return service.getByPhuongThucAndToHop(
                phuongthuc,
                tohop
        );
    }

    public List<BangQuyDoi> getByPhuongThucAndMon(
            String phuongthuc,
            String mon
    ) {
        return service.getByPhuongThucAndMon(
                phuongthuc,
                mon
        );
    }

    public BangQuyDoi getById(int id) {
        return service.getById(id);
    }

    public List<String> getDistinctPhuongThuc() {
        return service.getDistinctPhuongThuc();
    }

    // ================= CREATE =================

    public String add(BangQuyDoi bangQuyDoi) {
        try {
            service.add(bangQuyDoi);
            return null;
        } catch (AppException e) {
            return e.getMessage();
        }
    }

    // ================= UPDATE =================

    public String update(BangQuyDoi bangQuyDoi) {
        try {
            service.update(bangQuyDoi);
            return null;
        } catch (AppException e) {
            return e.getMessage();
        }
    }

    // ================= DELETE =================

    public String delete(int id) {
        try {
            service.delete(id);
            return null;
        } catch (AppException e) {
            return e.getMessage();
        }
    }

    // ================= IMPORT =================

    public BangQuyDoiService.ImportSummary importExcel(File file)
            throws Exception {

        return service.importFromExcel(file);
    }
}