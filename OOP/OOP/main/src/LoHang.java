public class LoHang {
    private String maLoHang;     
    private String maSP;         
    private String maPN;         
    private int soLuong;         
    private double donGia;       
    private String ngaySanXuat;  
    private String hanSuDung;    
    private String ghiChu;       

    public LoHang() {}

    public LoHang(String maLoHang, String maSP, String maPN, int soLuong, double donGia,
                  String ngaySanXuat, String hanSuDung, String ghiChu) {
        this.maLoHang = maLoHang;
        this.maSP = maSP;
        this.maPN = maPN;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
        this.ghiChu = ghiChu;
    }

// getters / setters
    public String getMaLoHang() { 
        return maLoHang; 
    }
    public void setMaLoHang(String maLoHang) { 
        this.maLoHang = maLoHang; 
    }

    public String getMaSP() { 
        return maSP; 
    }
    public void setMaSP(String maSP) { 
        this.maSP = maSP; 
    }

    public String getMaPN() { 
        return maPN; 
    }
    public void setMaPN(String maPN) { 
        this.maPN = maPN; 
    }

    public int getSoLuong() { 
        return soLuong; 
    }
    public void setSoLuong(int soLuong) { 
        this.soLuong = soLuong; 
    }

    public double getDonGia() { 
        return donGia; 
    }
    public void setDonGia(double donGia) { 
        this.donGia = donGia; 
    }

    public String getNgaySanXuat() { 
        return ngaySanXuat; 
    }
    public void setNgaySanXuat(String ngaySanXuat) { 
        this.ngaySanXuat = ngaySanXuat; 
    }

    public String getHanSuDung() { 
        return hanSuDung; 
    }
    public void setHanSuDung(String hanSuDung) { 
        this.hanSuDung = hanSuDung; 
    }

    public String getGhiChu() { 
        return ghiChu; 
    }
    public void setGhiChu(String ghiChu) { 
        this.ghiChu = ghiChu; 
    }

    public double tinhThanhTien() {
        return soLuong * donGia;
    }

    @Override
    public String toString() {
        return maLoHang + ", " + maSP + ", " + maPN + ", " + soLuong + ", " +
               donGia + ", " + ngaySanXuat + ", " + hanSuDung + (ghiChu != null && !ghiChu.isEmpty() ? ", " + ghiChu : "");
    }
}
