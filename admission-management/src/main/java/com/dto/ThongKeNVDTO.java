package com.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ThongKeNVDTO {

    private int     stt;
    private String  maXetTuyen;
    private String  tenMaXetTuyen;
    private long    chiTieu2025;
    private long    tongNV15;
    private long    nv1, nv2, nv3, nv4, nv5;
    private long    nvLon5;
    private long    tongTatCaNV;
    private boolean isDongTong = false;  // ← thêm field này

    // Constructor rỗng — dùng cho dòng tổng (buildDongTong set từng field)
    public ThongKeNVDTO() {}

    // Constructor map từ Object[] — dùng cho dòng dữ liệu từ query
    public ThongKeNVDTO(Object[] r) {
        this.maXetTuyen    = (String) r[0];
        this.tenMaXetTuyen = (String) r[1];
        this.chiTieu2025   = toLong(r[2]);
        this.tongNV15      = toLong(r[3]);
        this.nv1           = toLong(r[4]);
        this.nv2           = toLong(r[5]);
        this.nv3           = toLong(r[6]);
        this.nv4           = toLong(r[7]);
        this.nv5           = toLong(r[8]);
        this.nvLon5        = toLong(r[9]);
        this.tongTatCaNV   = toLong(r[10]);
        this.isDongTong    = false;
    }

    private long toLong(Object o) {
        return o == null ? 0L : ((Number) o).longValue();
    }

    public void setIsDongTong(boolean v) { this.isDongTong = v; }
}