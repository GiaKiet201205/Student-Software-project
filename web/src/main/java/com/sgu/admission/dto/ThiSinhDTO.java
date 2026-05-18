package com.sgu.admission.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThiSinhDTO {
    private String cccd;
    private String ho;
    private String ten;
    private String ngaySinh;
    private String dienThoai;
    private String email;
    private String gioiTinh;
    private String doiTuong;
    private String khuVuc;
}
