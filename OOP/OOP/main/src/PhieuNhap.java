public class PhieuNhap extends GiaoDich {
    private String MaPN;
    private String MaNCC;
    private NhaCungCap nhaCungCap; 
    private NhanVien nhanVien; 
    private ChiTietPhieuNhap[] danhSachCTPM; 
    private int soLuongChiTiet;
    private String GhiChu;

    public PhieuNhap() {
        super();
        danhSachCTPM = new ChiTietPhieuNhap[100];
        soLuongChiTiet = 0;
    }

    public PhieuNhap(String MaPN, String Ngay, double TongTien, String MaNCC, 
                     NhaCungCap nhaCungCap, NhanVien nhanVien, String GhiChu) {
        super(MaPN, Ngay, TongTien);
        this.MaPN = MaPN;
        this.MaNCC = MaNCC;
        this.nhaCungCap = nhaCungCap;
        this.nhanVien = nhanVien;
        this.GhiChu = GhiChu;
        this.danhSachCTPM = new ChiTietPhieuNhap[100];
        this.soLuongChiTiet = 0;
    }

//get,set
    public String getMaPN() {
        return MaPN;
    }

    public void setMaPN(String MaPN) {
        this.MaPN = MaPN;
    }

    public String getMaNCC() {
        return MaNCC;
    }

    public void setMaNCC(String MaNCC) {
        this.MaNCC = MaNCC;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public ChiTietPhieuNhap[] getDanhSachCTPM() {
        return danhSachCTPM;
    }

    public int getSoLuongChiTiet() {
        return soLuongChiTiet;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }

    public void themChiTiet(ChiTietPhieuNhap chiTiet) {
        if (soLuongChiTiet < danhSachCTPM.length) {
            danhSachCTPM[soLuongChiTiet] = chiTiet;
            soLuongChiTiet++;
            setTongTien(tinhTongTien());
        } else {
            System.out.println("Danh sach chi tiet phieu nhap da day!");
        }
    }

    public void xoaChiTiet(String maSP) {
        for (int i = 0; i < soLuongChiTiet; i++) {
            if (danhSachCTPM[i] != null && danhSachCTPM[i].getMaSP().equals(maSP)) {
                for (int j = i; j < soLuongChiTiet - 1; j++) {
                    danhSachCTPM[j] = danhSachCTPM[j + 1];
                }
                danhSachCTPM[soLuongChiTiet - 1] = null;
                soLuongChiTiet--;
                setTongTien(tinhTongTien());
                System.out.println("Da xoa chi tiet san pham: " + maSP);
                return;
            }
        }
        System.out.println("Khong tim thay san pham: " + maSP);
    }

    public ChiTietPhieuNhap timChiTiet(String maSP) {
        for (int i = 0; i < soLuongChiTiet; i++) {
            if (danhSachCTPM[i] != null && danhSachCTPM[i].getMaSP().equals(maSP)) {
                return danhSachCTPM[i];
            }
        }
        return null;
    }

    @Override
    public double tinhTongTien() {
        double tongTien = 0;
        for (int i = 0; i < soLuongChiTiet; i++) {
            tongTien += danhSachCTPM[i].tinhThanhTien();
        }
        return tongTien;
    }

    public void hienThiPhieuNhap() {
        System.out.println("Ma phieu nhap: " + MaPN + "\nNgay nhap: " + getNgay() + 
                         "\nNha cung cap: " + (nhaCungCap != null ? nhaCungCap.getTenNCC() : "N/A") +
                         "\nNhan vien: " + (nhanVien != null ? nhanVien.getHoTen() : "N/A") +
                         "\nGhi chu: " + (GhiChu != null ? GhiChu : ""));
        System.out.println("\nChi tiet phieu nhap:");
        for (int i = 0; i < soLuongChiTiet; i++) {
            danhSachCTPM[i].hienThiChiTiet();
        }
        System.out.println("Tong tien: " + tinhTongTien() + " VND");
    }
}