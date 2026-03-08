
public class HoaDon extends GiaoDich {

    private String PhuongThucTT;
    private int ChietKhauTichDiem;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private ChiTietGiaoDich[] chiTiet;
    private int soLuongChiTiet;

    public HoaDon() {
        super();
        chiTiet = new ChiTietGiaoDich[100];
        soLuongChiTiet = 0;
    }

    public HoaDon(String Ma, String Ngay, double TongTien, String PhuongThucTT, int ChietKhauTichDiem,
            KhachHang khachHang, NhanVien nhanVien) {
        super(Ma, Ngay, TongTien);
        this.PhuongThucTT = PhuongThucTT;
        this.ChietKhauTichDiem = ChietKhauTichDiem;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.chiTiet = new ChiTietGiaoDich[100];
        this.soLuongChiTiet = 0;
    }

    public String getPhuongThucTT() {
        return PhuongThucTT;
    }

    public int getChietKhauTichDiem() {
        return ChietKhauTichDiem;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public ChiTietGiaoDich[] getChiTiet() {
        return chiTiet;
    }

    public int getSoLuongChiTiet() {
        return soLuongChiTiet;
    }

    public void setPhuongThucTT(String PhuongThucTT) {
        this.PhuongThucTT = PhuongThucTT;
    }

    public void setChietKhauTichDiem(int ChietKhauTichDiem) {
        this.ChietKhauTichDiem = ChietKhauTichDiem;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void themChiTiet(ChiTietGiaoDich ct) {
        if (soLuongChiTiet < chiTiet.length) {
            chiTiet[soLuongChiTiet] = ct;
            soLuongChiTiet++;
        } else {
            System.out.println("Danh sach chi tiet da day!");
        }
    }

    @Override
    public double tinhTongTien() {
        double tongTien = 0;
        for (int i = 0; i < soLuongChiTiet; i++) {
            ChiTietGiaoDich ct = chiTiet[i];
            if (ct == null) {
                continue;
            }
            if (ct.getSoLuong() > 0 && ct.getDonGia() >= 0) {
                tongTien += ct.getThanhTien();
            }
        }
        return tongTien;
    }

}
