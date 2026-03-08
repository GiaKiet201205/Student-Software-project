public class ChiTietGiaoDich {
    protected String MaSP;
    protected int SoLuong;
    protected double DonGia;
    protected double ThanhTien;
    protected SanPham sanPham;

    public ChiTietGiaoDich() {
    }

    public ChiTietGiaoDich(String maSP, int soLuong, double donGia, SanPham sanPham) {
        this.MaSP = maSP;
        this.SoLuong = soLuong;
        this.DonGia = donGia;
        this.sanPham = sanPham;
        this.ThanhTien = tinhThanhTien();
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String maSP) {
        MaSP = maSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
        this.ThanhTien = tinhThanhTien();
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double donGia) {
        DonGia = donGia;
        this.ThanhTien = tinhThanhTien();
    }

    public double getThanhTien() {
        return ThanhTien;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public double tinhThanhTien() {
        return SoLuong * DonGia;
    }

    public void hienThiChiTiet() {
        System.out.println("----- Chi tiết giao dịch -----");
        System.out.println("Mã sản phẩm: " + MaSP);

        if (sanPham != null) {
            System.out.println("Tên sản phẩm: " + sanPham.getTenSP());
        } else {
            System.out.println("Tên sản phẩm: (Không xác định)");
        }

        System.out.println("Số lượng: " + SoLuong);
        System.out.println("Đơn giá: " + String.format("%.2f", DonGia) + " VND");
        System.out.println("Thành tiền: " + String.format("%.2f", ThanhTien) + " VND");
        System.out.println("-------------------------------");
    }

}
