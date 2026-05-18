package com.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ThiSinhTrungTuyenDTO {

    private String cccd;
    private String ho;
    private String ten;
    private String ngaySinh;
    private String email;
    private String dienThoai;
    private String gioiTinh;
    private String noiSinh;

    // Constructor rỗng
    public ThiSinhTrungTuyenDTO() {}

    // Constructor từ Object[] (từ HQL query)
    public ThiSinhTrungTuyenDTO(Object[] r) {
        this.cccd = (String) r[0];
        this.ho = (String) r[1];
        this.ten = (String) r[2];
        this.ngaySinh = (String) r[3];
        this.email = (String) r[4];
        this.dienThoai = (String) r[5];
        this.gioiTinh = (String) r[6];
        this.noiSinh = (String) r[7];
    }

    // Constructor từng parameter
    public ThiSinhTrungTuyenDTO(String cccd, String ho, String ten, String ngaySinh,
                                String email, String dienThoai, String gioiTinh, String noiSinh) {
        this.cccd = cccd;
        this.ho = ho;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.dienThoai = dienThoai;
        this.gioiTinh = gioiTinh;
        this.noiSinh = noiSinh;
    }
}
