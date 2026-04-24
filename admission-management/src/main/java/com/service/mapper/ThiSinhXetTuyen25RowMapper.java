package com.service.mapper;

import com.entity.ThiSinhXetTuyen25;

import java.time.LocalDate;

public class ThiSinhXetTuyen25RowMapper implements RowMapper<ThiSinhXetTuyen25> {

    @Override
    public ThiSinhXetTuyen25 map(SimpleRow row){
        String cccd = row.get(1);
        String hoTen = row.get(2);
        String ngaySinh = row.get(3);
        String gioiTinh = row.get(4);
        String doiTuong = row.get(5);
        String khuVuc = row.get(6);
        String noiSinh = row.get(35);
        String [] parts;

        if (cccd == null || cccd.trim().isEmpty()) {
            return null;
        }
        if (hoTen == null || hoTen.trim().isEmpty()) {
            return null;
        }else {
            parts = hoTen.trim().split("_");
        }



        ThiSinhXetTuyen25 ts = new ThiSinhXetTuyen25();
        ts.setCccd(cccd.trim());
        ts.setSoBaoDanh(cccd.trim());
        ts.setHo(parts.length > 0 ? parts[0].trim() : "");
        ts.setTen(parts.length > 1 ? parts[1].trim() : "");
        ts.setNgaySinh(ngaySinh.trim());
        ts.setGioiTinh(gioiTinh.trim());
        ts.setNoiSinh(noiSinh.trim());
        ts.setEmail(hoTen.trim().toLowerCase().replaceAll("[_\\s]+", "") + "@sgu.edu.vn");
        if (doiTuong != null || !doiTuong.trim().isEmpty()) {
            ts.setDoiTuong(doiTuong.trim());
        }
        if (khuVuc != null || !khuVuc.trim().isEmpty()) {
            ts.setKhuVuc(khuVuc.trim());
        }
        ts.setDienThoai("0123456789");
        ts.setPassword(hoTen.trim().trim().toLowerCase().replaceAll("[_\\s]+", "") + "123");
        ts.setUpdatedAt(LocalDate.now());

        return ts;
    }
}
