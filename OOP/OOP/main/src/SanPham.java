public abstract class SanPham {
    private String MaSP;
    private String TenSP;
    private String DonViTinh;
    private Double GiaNhap;
    private Double GiaBanLe;
    private int SoLuongTon;
    private String NhaSanXuat;
    private static int demSanPham;

    abstract double tinhGiaBan();

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String donViTinh, Double giaNhap, Double giaBanLe, int soLuongTon,
            String nhaSanXuat, int demSanPham) {
        MaSP = maSP;
        TenSP = tenSP;
        DonViTinh = donViTinh;
        GiaNhap = giaNhap;
        setGiaBanLe(giaBanLe);
        setSoLuongTon(soLuongTon);
        NhaSanXuat = nhaSanXuat;
        SanPham.demSanPham = demSanPham;
    }

    public static int getDemSanPham() {
        return demSanPham;
    }

    public String getDonViTinh() {
        return DonViTinh;
    }

    public Double getGiaBanLe() {
        return GiaBanLe;
    }

    public Double getGiaNhap() {
        return GiaNhap;
    }

    public String getMaSP() {
        return MaSP;
    }

    public String getNhaSanXuat() {
        return NhaSanXuat;
    }

    public int getSoLuongTon() {
        return SoLuongTon;
    }

    public String getTenSP() {
        return TenSP;
    }

    // Setter
    public static void setDemSanPham(int demSanPham) {
        SanPham.demSanPham = demSanPham;
    }

    public void setDonViTinh(String donViTinh) {
        DonViTinh = donViTinh;
    }

    public void setGiaBanLe(Double giaBanLe) {
        if (giaBanLe == null || giaBanLe <= 0) {
            throw new IllegalArgumentException("Gia ban le phai lon hon 0.");
        }
        if (GiaNhap != null && giaBanLe < GiaNhap) {
            throw new IllegalArgumentException("Gia ban le phai lon hon gia nhap.");
        }
        GiaBanLe = giaBanLe;
    }

    public void setGiaNhap(Double giaNhap) {
        if (giaNhap == null || giaNhap <= 0) {
            throw new IllegalArgumentException("Gia nhap phai lon hon 0.");
        }
        GiaNhap = giaNhap;
    }

    public void setMaSP(String maSP) {
        if (maSP == null || maSP.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma san pham khong duoc de trong.");
        }
        MaSP = maSP;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        if (nhaSanXuat == null || nhaSanXuat.trim().isEmpty()) {
            throw new IllegalArgumentException("Nha san xuat khong duoc de trong.");
        }
        NhaSanXuat = nhaSanXuat;
    }

    public void setSoLuongTon(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("So luong ton khong duoc am.");
        }
        SoLuongTon = soLuongTon;
    }

    public void setTenSP(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten san pham khong duoc de trong.");
        }
        TenSP = tenSP;
    }
}
