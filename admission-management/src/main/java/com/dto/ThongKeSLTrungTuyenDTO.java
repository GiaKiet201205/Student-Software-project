package com.dto;

public class ThongKeSLTrungTuyenDTO {
    private int stt;
    private String maNganh;
    private String tenNganh;
    private String phuongThuc;
    private long soLuongTrungTuyen;

    public ThongKeSLTrungTuyenDTO() {}

    public int getStt() { return stt; }
    public void setStt(int stt) { this.stt = stt; }

    public String getMaNganh() { return maNganh; }
    public void setMaNganh(String maNganh) { this.maNganh = maNganh; }

    public String getTenNganh() { return tenNganh; }
    public void setTenNganh(String tenNganh) { this.tenNganh = tenNganh; }

    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }

    public long getSoLuongTrungTuyen() { return soLuongTrungTuyen; }
    public void setSoLuongTrungTuyen(long soLuongTrungTuyen) { this.soLuongTrungTuyen = soLuongTrungTuyen; }
}