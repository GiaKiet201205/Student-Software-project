public class ChiTietPhieuNhap extends ChiTietGiaoDich {
    private String MaPN;
    private int SoLuongNhap;
    private double DonGiaNhap;
    private String NgaySanXuat;
    private String HanSuDung;
    private PhieuNhap phieuNhap;
    private LoHang loHang;

    public ChiTietPhieuNhap() {
        super();
    }

    public ChiTietPhieuNhap(String MaPN, String MaSP, int SoLuongNhap, double DonGiaNhap,
            String NgaySanXuat, String HanSuDung,
            SanPham sanPham, PhieuNhap phieuNhap, String maLoHang, String ghiChu) {
        super(MaSP, SoLuongNhap, DonGiaNhap, sanPham);
        this.MaPN = MaPN;
        this.SoLuongNhap = SoLuongNhap;
        this.DonGiaNhap = DonGiaNhap;
        this.NgaySanXuat = NgaySanXuat;
        this.HanSuDung = HanSuDung;
        this.phieuNhap = phieuNhap;
        if (maLoHang == null || maLoHang.trim().isEmpty()) {
            maLoHang = "LH" + System.currentTimeMillis();
        }
        this.loHang = new LoHang(maLoHang, MaSP, MaPN, SoLuongNhap, DonGiaNhap, NgaySanXuat, HanSuDung, ghiChu);
    }


    public LoHang getLoHang() { 
        return loHang; 
    }

    public void setLoHang(LoHang loHang) { 
        this.loHang = loHang; 
    }

    public String getMaPN() {
        return MaPN;
    }

    public void setMaPN(String MaPN) {
        this.MaPN = MaPN;
    }

    public int getSoLuongNhap() {
        return SoLuongNhap;
    }

    public void setSoLuongNhap(int SoLuongNhap) {
        this.SoLuongNhap = SoLuongNhap;
        setSoLuong(SoLuongNhap); // Cập nhật ở class cha
    }

    public double getDonGiaNhap() {
        return DonGiaNhap;
    }

    public void setDonGiaNhap(double DonGiaNhap) {
        this.DonGiaNhap = DonGiaNhap;
        setDonGia(DonGiaNhap); // Cập nhật ở class cha
    }

    public String getNgaySanXuat() {
        return NgaySanXuat;
    }

    public void setNgaySanXuat(String NgaySanXuat) {
        this.NgaySanXuat = NgaySanXuat;
    }

    public String getHanSuDung() {
        return HanSuDung;
    }

    public void setHanSuDung(String HanSuDung) {
        this.HanSuDung = HanSuDung;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public void capNhatTonKho() {
        if (getSanPham() == null) return;

        if (loHang == null) {
            int tonHienTai = getSanPham().getSoLuongTon();
            getSanPham().setSoLuongTon(tonHienTai + getSoLuongNhap());
        } 
        else {
            int tonHienTai = getSanPham().getSoLuongTon();
            getSanPham().setSoLuongTon(tonHienTai + loHang.getSoLuong());
        }

        System.out.println("[✓] Da cap nhat ton kho san pham " + getSanPham().getTenSP());
    }


    @Override
    public void hienThiChiTiet() {
        System.out.println("Ma PN: " + MaPN);
        System.out.println(
                "Ma SP: " + getMaSP() + "\nTen SP: " + (getSanPham() != null ? getSanPham().getTenSP() : "N/A") +
                        "\nSo luong nhap: " + SoLuongNhap + "\nDon gia nhap: " + DonGiaNhap +
                        "\nThanh tien: " + tinhThanhTien());
        if (loHang != null) {
            System.out.println("Lo Hang: " + loHang.getMaLoHang() +
                    "\nNgay san xuat: " + loHang.getNgaySanXuat() + 
                    "\nHan su dung: " + loHang.getHanSuDung());
        }
    }

    public boolean kiemTraHetHan() {
        System.out.println("Kiem tra han su dung cho san pham: " + getMaSP());
        return false;
    }
}