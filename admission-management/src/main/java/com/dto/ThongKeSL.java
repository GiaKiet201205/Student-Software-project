package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThongKeSL {

    private int stt;
    private String maNganh;
    private String tenNganh;
    private long soLuongPT3;
    private long soLuongPT4;
    private long soLuongPT5;
    private long soLuongTong;
    private boolean isDongTong = false;

    public void setIsDongTong(boolean v) { this.isDongTong = v; }
}
