public class BaoCaoThongKe implements IThongKe {
    private double DoanhThuNgay;
    private double DoanhThuThang;
    private double DoanhThuNam;
    private double TongChi;
    private String ThuocSapHetHan;
    private String LoiNhuan;
    private HoaDon[] danhSachHoaDon;
    private int soLuongHoaDon;
    private PhieuNhap[] danhSachPhieuNhap;
    private int soLuongPhieuNhap;

    public BaoCaoThongKe() {
        this.danhSachHoaDon = new HoaDon[1000];
        this.danhSachPhieuNhap = new PhieuNhap[1000];
        this.soLuongHoaDon = 0;
        this.soLuongPhieuNhap = 0;
        this.DoanhThuNgay = 0;
        this.DoanhThuThang = 0;
        this.DoanhThuNam = 0;
        this.TongChi = 0;
    }

    public double getDoanhThuNgay() {
        return DoanhThuNgay;
    }

    public void setDoanhThuNgay(double doanhThuNgay) {
        DoanhThuNgay = doanhThuNgay;
    }

    public double getDoanhThuThang() {
        return DoanhThuThang;
    }

    public void setDoanhThuThang(double doanhThuThang) {
        DoanhThuThang = doanhThuThang;
    }

    public double getDoanhThuNam() {
        return DoanhThuNam;
    }

    public void setDoanhThuNam(double doanhThuNam) {
        DoanhThuNam = doanhThuNam;
    }

    public double getTongChi() {
        return TongChi;
    }

    public void setTongChi(double tongChi) {
        TongChi = tongChi;
    }

    public String getThuocSapHetHan() {
        return ThuocSapHetHan;
    }

    public void setThuocSapHetHan(String thuocSapHetHan) {
        ThuocSapHetHan = thuocSapHetHan;
    }

    public String getLoiNhuan() {
        return LoiNhuan;
    }

    public void setLoiNhuan(String loiNhuan) {
        LoiNhuan = loiNhuan;
    }

    public HoaDon[] getDanhSachHoaDon() {
        return danhSachHoaDon;
    }

    public int getSoLuongHoaDon() {
        return soLuongHoaDon;
    }

    public PhieuNhap[] getDanhSachPhieuNhap() {
        return danhSachPhieuNhap;
    }

    public int getSoLuongPhieuNhap() {
        return soLuongPhieuNhap;
    }

    public void themHoaDon(HoaDon hoaDon) {
        if (soLuongHoaDon < danhSachHoaDon.length) {
            danhSachHoaDon[soLuongHoaDon] = hoaDon;
            soLuongHoaDon++;
        } else {
            System.out.println("Danh sach hoa don da day!");
        }
    }

    public void themPhieuNhap(PhieuNhap phieuNhap) {
        if (soLuongPhieuNhap < danhSachPhieuNhap.length) {
            danhSachPhieuNhap[soLuongPhieuNhap] = phieuNhap;
            soLuongPhieuNhap++;
        } else {
            System.out.println("Danh sach phieu nhap da day!");
        }
    }

    public double tinhDoanhThuNgay(String ngay) {
        double doanhThu = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].getNgay().equals(ngay)) {
                doanhThu += danhSachHoaDon[i].tinhTongTien();
            }
        }
        this.DoanhThuNgay = doanhThu;
        return doanhThu;
    }

    @Override
    public void TinhDoanhThuThang() {
        double doanhThu = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            doanhThu += danhSachHoaDon[i].tinhTongTien();
        }
        this.DoanhThuThang = doanhThu;
        System.out.println("Tong doanh thu thang: " + DoanhThuThang + " VND");
    }

    public double tinhDoanhThuThang(int thang, int nam) {
        double doanhThu = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            String ngay = danhSachHoaDon[i].getNgay();
            if (ngay != null && ngay.length() >= 10) {
                String[] parts = ngay.split("/");
                if (parts.length == 3) {
                    int thangHD = Integer.parseInt(parts[1]);
                    int namHD = Integer.parseInt(parts[2]);
                    if (thangHD == thang && namHD == nam) {
                        doanhThu += danhSachHoaDon[i].tinhTongTien();
                    }
                }
            }
        }
        this.DoanhThuThang = doanhThu;
        return doanhThu;
    }

    public double tinhTongChi() {
        double tongChi = 0;
        for (int i = 0; i < soLuongPhieuNhap; i++) {
            tongChi += danhSachPhieuNhap[i].tinhTongTien();
        }
        this.TongChi = tongChi;
        return tongChi;
    }

    public double tinhTongChiThang(int thang, int nam) {
        double tongChi = 0;
        for (int i = 0; i < soLuongPhieuNhap; i++) {
            String ngay = danhSachPhieuNhap[i].getNgay();
            if (ngay != null && ngay.length() >= 10) {
                String[] parts = ngay.split("/");
                if (parts.length == 3) {
                    int thangPN = Integer.parseInt(parts[1]);
                    int namPN = Integer.parseInt(parts[2]);
                    if (thangPN == thang && namPN == nam) {
                        tongChi += danhSachPhieuNhap[i].tinhTongTien();
                    }
                }
            }
        }
        return tongChi;
    }

    @Override
    public void TinhLoiNhuan() {
        double doanhThu = 0;
        double chiPhi = 0;

        for (int i = 0; i < soLuongHoaDon; i++) {
            doanhThu += danhSachHoaDon[i].tinhTongTien();
        }

        for (int i = 0; i < soLuongPhieuNhap; i++) {
            chiPhi += danhSachPhieuNhap[i].tinhTongTien();
        }

        double loiNhuan = doanhThu - chiPhi;
        this.LoiNhuan = String.valueOf(loiNhuan);

        System.out.println("Tong doanh thu: " + doanhThu + " VND\nTong chi phi: " + chiPhi +
                " VND\nLoi nhuan: " + loiNhuan + " VND");
    }

    public void tinhLoiNhuanThang(int thang, int nam) {
        double doanhThu = tinhDoanhThuThang(thang, nam);
        double chiPhi = tinhTongChiThang(thang, nam);
        double loiNhuan = doanhThu - chiPhi;

        System.out.println("Bao cao loi nhuan thang " + thang + "/" + nam +
                "\nDoanh thu: " + doanhThu + " VND\nChi phi: " + chiPhi +
                " VND\nLoi nhuan: " + loiNhuan + " VND");
    }

    public void hienThiBaoCaoTongHop() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║           BAO CAO TONG HOP                        ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  So luong hoa don: " + String.format("%-28d", soLuongHoaDon) + "║");
        System.out.println("║  So luong phieu nhap: " + String.format("%-25d", soLuongPhieuNhap) + "║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  Tong doanh thu: " + String.format("%-30.2f", tinhTongDoanhThu()) + " VND ║");
        System.out.println("║  Tong chi phi: " + String.format("%-32.2f", tinhTongChi()) + " VND ║");
        System.out
                .println("║  Loi nhuan: " + String.format("%-35.2f", (tinhTongDoanhThu() - tinhTongChi())) + " VND ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    public void hienThiBaoCaoChiTietPhieuNhap() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║       BAO CAO CHI TIET PHIEU NHAP                 ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  Tong so phieu nhap: " + String.format("%-27d", soLuongPhieuNhap) + "║");
        System.out.println("║  Tong chi phi nhap hang: " + String.format("%-23.2f", tinhTongChi()) + " VND ║");

        if (soLuongPhieuNhap > 0) {
            double trungBinh = tinhTongChi() / soLuongPhieuNhap;
            System.out.println("║  Chi phi TB/phieu: " + String.format("%-28.2f", trungBinh) + " VND ║");
        }
        System.out.println("╚═══════════════════════════════════════════════════╝");

        if (soLuongPhieuNhap > 0) {
            System.out.println("\nChi tiet cac phieu nhap:");
            for (int i = 0; i < soLuongPhieuNhap; i++) {
                PhieuNhap pn = danhSachPhieuNhap[i];
                System.out.println("  [" + (i + 1) + "] " + pn.getMaPN() +
                        " - Ngay: " + pn.getNgay() +
                        " - Tong: " + String.format("%.2f", pn.tinhTongTien()) + " VND");
                System.out.println(
                        "      NCC: " + (pn.getNhaCungCap() != null ? pn.getNhaCungCap().getTenNCC() : pn.getMaNCC()));
                System.out.println("      So mat hang: " + pn.getSoLuongChiTiet());
            }
        }
    }

    public void thongKeTheoNhaCungCap(String maNCC) {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║       THONG KE THEO NHA CUNG CAP                  ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        int soPhieuNhap = 0;
        double tongChiPhi = 0;
        String tenNCC = "";

        for (int i = 0; i < soLuongPhieuNhap; i++) {
            PhieuNhap pn = danhSachPhieuNhap[i];
            if (pn.getMaNCC().equalsIgnoreCase(maNCC)) {
                soPhieuNhap++;
                tongChiPhi += pn.tinhTongTien();
                if (tenNCC.isEmpty() && pn.getNhaCungCap() != null) {
                    tenNCC = pn.getNhaCungCap().getTenNCC();
                }
            }
        }

        System.out.println("Ma NCC: " + maNCC);
        System.out.println("Ten NCC: " + (tenNCC.isEmpty() ? "N/A" : tenNCC));
        System.out.println("So phieu nhap: " + soPhieuNhap);
        System.out.println("Tong chi phi: " + String.format("%.2f", tongChiPhi) + " VND");
        if (soPhieuNhap > 0) {
            System.out.println("Trung binh/phieu: " + String.format("%.2f", tongChiPhi / soPhieuNhap) + " VND");
        }
    }

    public void topNhaCungCapNhapNhieuNhat(int top) {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║   TOP NHA CUNG CAP NHAP NHIEU NHAT                ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        // Tạo mảng lưu thống kê theo NCC
        String[] danhSachMaNCC = new String[soLuongPhieuNhap];
        double[] chiPhiTheoNCC = new double[soLuongPhieuNhap];
        int[] soLanNhapTheoNCC = new int[soLuongPhieuNhap];
        int soNCC = 0;

        // Thống kê
        for (int i = 0; i < soLuongPhieuNhap; i++) {
            PhieuNhap pn = danhSachPhieuNhap[i];
            String maNCC = pn.getMaNCC();

            // Tìm xem NCC đã có trong danh sách chưa
            int viTri = -1;
            for (int j = 0; j < soNCC; j++) {
                if (danhSachMaNCC[j].equals(maNCC)) {
                    viTri = j;
                    break;
                }
            }

            if (viTri == -1) {
                // NCC mới
                danhSachMaNCC[soNCC] = maNCC;
                chiPhiTheoNCC[soNCC] = pn.tinhTongTien();
                soLanNhapTheoNCC[soNCC] = 1;
                soNCC++;
            } else {
                // NCC đã có
                chiPhiTheoNCC[viTri] += pn.tinhTongTien();
                soLanNhapTheoNCC[viTri]++;
            }
        }

        // Sắp xếp theo chi phí giảm dần
        for (int i = 0; i < soNCC - 1; i++) {
            for (int j = i + 1; j < soNCC; j++) {
                if (chiPhiTheoNCC[i] < chiPhiTheoNCC[j]) {
                    // Swap chi phí
                    double tempCP = chiPhiTheoNCC[i];
                    chiPhiTheoNCC[i] = chiPhiTheoNCC[j];
                    chiPhiTheoNCC[j] = tempCP;

                    // Swap mã NCC
                    String tempMa = danhSachMaNCC[i];
                    danhSachMaNCC[i] = danhSachMaNCC[j];
                    danhSachMaNCC[j] = tempMa;

                    // Swap số lần nhập
                    int tempSL = soLanNhapTheoNCC[i];
                    soLanNhapTheoNCC[i] = soLanNhapTheoNCC[j];
                    soLanNhapTheoNCC[j] = tempSL;
                }
            }
        }

        // Hiển thị top
        int soHienThi = Math.min(top, soNCC);
        System.out.printf("%-10s %-12s %-15s\n", "Ma NCC", "So lan nhap", "Tong chi phi");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < soHienThi; i++) {
            System.out.printf("%-10s %-12d %-15.2f VND\n",
                    danhSachMaNCC[i], soLanNhapTheoNCC[i], chiPhiTheoNCC[i]);
        }
    }

    private double tinhTongDoanhThu() {
        double tongDoanhThu = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            tongDoanhThu += danhSachHoaDon[i].tinhTongTien();
        }
        return tongDoanhThu;
    }
}