package com.service.mapper;

import com.entity.NguoiDung;

public class NguoiDungRowMapper implements RowMapper<NguoiDung> {

    @Override
    public NguoiDung map(SimpleRow row) {
        String username = row.get(1);

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        NguoiDung nd = new NguoiDung();
        nd.setUsername(username.trim());

        return nd;
    }
}