package com.service.mapper;

import com.entity.ToHopMonThi;

public class ToHopRowMapper implements RowMapper<ToHopMonThi> {
    @Override
    public ToHopMonThi map(SimpleRow row) {
        String maToHop = row.get(1);
        if (maToHop == null || maToHop.isEmpty()) {
            return null;
        }

        ToHopMonThi toHop = new ToHopMonThi();
        toHop.setMaToHop(maToHop);
        
        toHop.setMon1(row.get(2));
        toHop.setMon2(row.get(3));
        toHop.setMon3(row.get(4));
        
        toHop.setTenToHop(row.get(5));

        return toHop;
    }
}