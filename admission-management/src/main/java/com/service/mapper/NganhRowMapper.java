package com.service.mapper;

import com.entity.Nganh;
import java.math.BigDecimal;

public class NganhRowMapper implements RowMapper<Nganh> {

    @Override
    public Nganh map(SimpleRow row) {
        String maNganh = row.get(1); 
        
        if (maNganh == null || maNganh.trim().isEmpty()) {
            return null; 
        }

        Nganh nganh = new Nganh();
        nganh.setMaNganh(maNganh);
        
        nganh.setTenNganh(row.get(2)); 

        String isGoc = row.get(6);
        if ("Gốc".equalsIgnoreCase(isGoc) || "Goc".equalsIgnoreCase(isGoc)) {
            nganh.setToHopGoc(row.get(5)); 
        }

        nganh.setChiTieu(0);
        nganh.setDiemSan(new BigDecimal("0"));
        nganh.setDiemTrungTuyen(new BigDecimal("0"));
        nganh.setTuyenThang("N");
        nganh.setDgnl("N");
        nganh.setThpt("N");
        nganh.setVsat("N");
        nganh.setSlXtt(0);
        nganh.setSlDgnl(0);
        nganh.setSlVsat(0);
        nganh.setSlThpt("0");

        return nganh;
    }
}