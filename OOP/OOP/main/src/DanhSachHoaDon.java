
import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DanhSachHoaDon implements INhapXuat, IQuanLy<HoaDon>, IThongKe, ITinhTien {

    private NhanVien nhanVienContext;
    private DanhSachSanPham dsspContext;
    private DanhSachKhachHang dskhContext;
    private HoaDon[] hoaDons;
    private int soLuong;
    private static final int MAX_SIZE = 1000;

    public DanhSachHoaDon() {
        hoaDons = new HoaDon[MAX_SIZE];
        soLuong = 0;
    }

    public DanhSachHoaDon(int kichThuoc) {
        hoaDons = new HoaDon[kichThuoc];
        soLuong = 0;
    }

    // Getter
    public HoaDon[] getHoaDons() {
        return hoaDons;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setDskhContext(DanhSachKhachHang dskh) {
        this.dskhContext = dskh;
    }

    public void setDsspContext(DanhSachSanPham dssp) {
        this.dsspContext = dssp;
    }

    // Implement INhapXuat
    @Override
    public void NhapDanhSach() {
        System.out.print("Nhap so luong hoa don: ");
        int n;
        while (true) {
            n = sc.nextInt();
            if (n <= 0) {
                System.out.println("[!] Khong the nhap so luong <= 0");
                continue;
            }
            break;
        }
        sc.nextLine();
        nhapDanhSach(n);
    }

    @Override
    public void XuatDanhSach() {
        xuatDanhSach();
    }

    // Implement IQuanLy
    @Override
    public void them(HoaDon hd) {
        themPhanTu(hd);
    }

    @Override
    public void xoa(String ma) {
        xoaTheoMa(ma);
    }

    @Override
    public HoaDon timKiem(String ma) {
        return timKiemTheoMa(ma);
    }

    @Override
    public void hienThiDanhSach() {
        xuatDanhSach();
    }

    // Implement IThongKe
    @Override
    public void TinhDoanhThuThang() {
        System.out.print("Nhap thang (1-12): ");
        int thang = 0;
        while (true) {
            thang = sc.nextInt();
            if (thang >= 1 && thang <= 12) {
                break;
            } else {
                System.out.print("[!] Thang khong hop le! Vui long nhap lai (1-12): ");
            }
        }
        System.out.print("Nhap nam: ");
        int nam = 0;
        while (true) {
            nam = sc.nextInt();
            if (nam > 0 && nam < 2026) {
                break;
            } else {
                System.out.print("[!] Nam khong hop le! Vui long nhap lai (nam > 0): ");
            }
        }
        sc.nextLine();

        double doanhThu = tinhDoanhThuThang(thang, nam);
        System.out.println("Doanh thu thang " + thang + "/" + nam + ": "
                + String.format("%.2f", doanhThu) + " VND");
    }

    //
    @Override
    public void TinhLoiNhuan() {
        double tongDoanhThu = 0;
        for (int i = 0; i < soLuong; i++) {
            tongDoanhThu += hoaDons[i].tinhTongTien();
        }
        System.out.println("Tong doanh thu tu hoa don: " + String.format("%.2f", tongDoanhThu) + " VND");
        System.out.println("(Luu y: Can tru di chi phi nhap hang de tinh loi nhuan chinh xac)");
    }

    // Implement ITinhTien
    @Override
    public double tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < soLuong; i++) {
            tong += hoaDons[i].tinhTongTien();
        }
        return tong;
    }

    public void NhapDanhSach(NhanVien nvDangNhap, DanhSachSanPham dssp) {
        this.nhanVienContext = nvDangNhap;
        this.dsspContext = dssp;
        this.NhapDanhSach(); // gọi flow cũ hỏi số lượng rồi nhapDanhSach(n)
    }

    public void nhapDanhSach(int n) {
        System.out.println("Nhap danh sach " + n + " hoa don:");
        System.out.println("(Luu y: Can co du lieu khach hang va nhan vien truoc)");
        for (int i = 0; i < n && soLuong < hoaDons.length; i++) {
            System.out.println("\n--- Hoa don thu " + (i + 1) + " ---");
            // 1. Nhập mã hóa đơn
            String maHD = "";
            boolean maHDHopLe = false;
            while (!maHDHopLe) {
                System.out.print("Ma HD (se co san tien to 'HD'): ");
                String maHDNhap = sc.nextLine().trim();

                if (maHDNhap.isEmpty()) {
                    System.out.println("[!] Ma hoa don khong duoc de trong!");
                    continue;
                }
                if (maHDNhap.toUpperCase().startsWith("HD")) {
                    maHD = maHDNhap;
                } else {
                    maHD = "HD" + maHDNhap;
                }

                // Kiểm tra mã không trùng
                if (timKiemTheoMa(maHD) != null) {
                    System.out.println("[!] Ma hoa don '" + maHD + "' da ton tai! Vui long nhap ma khac.");
                    continue;
                }

                maHDHopLe = true;
            }

            String ngay = "";
            LocalDate ngayDate = null;
            LocalDate today = LocalDate.now();
            
            LocalDate minDate = LocalDate.of(2020, 1, 1); 

            while (ngayDate == null) {
                System.out.print("Ngay (dd/mm/yyyy): ");
                ngay = sc.nextLine().trim();

                if (ngay.isEmpty()) {
                    System.out.println("[!] Ngay khong duoc de trong!");
                    continue;
                }

                if (!ngay.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    System.out.println("[!] Ngay phai co dinh dang dd/mm/yyyy!");
                    continue;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    ngayDate = LocalDate.parse(ngay, formatter);

                    if (ngayDate.isAfter(today)) {
                        System.out.println("[!] Ngay khong duoc lon hon ngay hien tai!");
                        ngayDate = null;
                    // <<< THAY ĐỔI (thêm else if) >>>
                    } else if (ngayDate.isBefore(minDate)) {
                        System.out.println("[!] Ngay khong hop le! Nam phai tu 2020 tro di.");
                        ngayDate = null;
                    }
                } catch (DateTimeException e) {
                    System.out.println("[!] Ngay khong hop le (vi du: 30/02/2024)!");
                    ngayDate = null;
                }
            }

            // 3. Nhập phương thức thanh toán (FIX: thêm validation)
            String phuongThuc = "";
            int phuongThucOpt = -1;

            while (phuongThucOpt < 1 || phuongThucOpt > 4) {
                System.out.print("Phuong thuc thanh toan (1. Tien mat, 2. The tin dung, 3. Chuyen khoan, 4. Khac): ");
                try {
                    phuongThucOpt = Integer.parseInt(sc.nextLine().trim());

                    switch (phuongThucOpt) {
                        case 1:
                            phuongThuc = "Tien mat";
                            break;
                        case 2:
                            phuongThuc = "The tin dung";
                            break;
                        case 3:
                            phuongThuc = "Chuyen khoan";
                            break;
                        case 4:
                            phuongThuc = "Khac";
                            break;
                        default:
                            System.out.println("[!] Lua chon khong hop le! Vui long chon tu 1-4.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so tu 1-4!");
                }
            }

            // 4. Nhập chiết khấu (FIX: thêm try-catch)
            int chietKhau = -1;

            while (chietKhau < 0 || chietKhau > 100) {
                System.out.print("Chiet khau tich diem: ");
                try {
                    chietKhau = Integer.parseInt(sc.nextLine().trim());

                    if (chietKhau < 0 || chietKhau > 100) {
                        System.out.println("[!] Chiet khau phai trong khoang 0 - 100 ! Vui long nhap lai.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so nguyen hop le!");
                }
            }

            // 5. Tạo hóa đơn
            HoaDon hd = new HoaDon(maHD, ngay, 0, phuongThuc, chietKhau, null, null);
            if (this.nhanVienContext != null) {
                hd.setNhanVien(this.nhanVienContext);
            }
            // --- nhập và gán Khách hàng (nếu đã set dskhContext)
            if (this.dskhContext != null) {
                while (true) {
                    System.out.print("Ma KH (bo trong neu khong co): ");
                    String maKH = sc.nextLine().trim();

                    if (maKH.isEmpty()) { // cho phép bỏ qua
                        System.out.println("[i] Bo qua gan khach hang cho hoa don.");
                        break;
                    }

                    KhachHang kh = dskhContext.timKiem(maKH);
                    if (kh == null) {
                        System.out.println("[X] Khong tim thay khach hang " + maKH + ". Thu lai!");
                        continue;
                    }

                    hd.setKhachHang(kh);
                    System.out.println("[✓] Gan KH thanh cong: " + kh.getMaKH() + " - " + kh.getHoTen());
                    break;
                }
            } else {
                System.out.println("[!] Chua set dskhContext -> khong the gan khach hang cho hoa don " + maHD);
            }

            themPhanTu(hd);
            System.out.println("Da them hoa don thanh cong!");
            if (this.dsspContext == null) {
                System.out.println("[!] Chua set dsspContext -> khong the nhap san pham cho hoa don " + maHD);
            } else {
                nhapChiTietChoHoaDonBangMa(maHD, this.dsspContext);
                hd.setTongTien(hd.tinhTongTien());
                System.out.println("[✓] Cap nhat tong tien HD " + maHD + ": " + (long) hd.getTongTien() + " VND");
            }
        }
    }

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach hoa don rong!");
            return;
        }

        // --- TIÊU ĐỀ LỚN ---
        System.out.println("\n" + "=".repeat(106));
        System.out.printf("|%s%s%s|\n",
                " ".repeat(43), "DANH SACH HOA DON", " ".repeat(44));
        System.out.println("=".repeat(106));

        for (int i = 0; i < soLuong; i++) {
            HoaDon hd = hoaDons[i];
            if (hd == null) {
                continue;
            }

            String tenKH = (hd.getKhachHang() != null) ? hd.getKhachHang().getHoTen() : "Khach vang lai";
            String tenNV = (hd.getNhanVien() != null) ? hd.getNhanVien().getHoTen() : "N/A";
            String maKH = (hd.getKhachHang() != null) ? hd.getKhachHang().getMaKH() : "N/A";
            String maNV = (hd.getNhanVien() != null) ? hd.getNhanVien().getMaNV() : "N/A";
            String pttt = (hd.getPhuongThucTT() != null) ? hd.getPhuongThucTT() : "Chua xac dinh";

            // --- HEADER HÓA ĐƠN (Tổng 106 ký tự) ---
            System.out.println("+" + "=".repeat(104) + "+");
            System.out.printf("| Ma HD: %-27s | Ngay lap: %-55s |\n", hd.getMa(), hd.getNgay());
            if (tenKH.length() > 20) {
                tenKH = tenKH.substring(0, 17) + "...";
            }
            // Tên Nhân viên được nới rộng thoải mái
            if (tenNV.length() > 55) {
                tenNV = tenNV.substring(0, 52) + "...";
            }
            System.out.printf("| KH:    %-6s %-20s | NV: %-6s %-54s |\n",
                    maKH, tenKH, maNV, tenNV);
            System.out.println("+" + "-".repeat(104) + "+");

            // --- DANH SÁCH SẢN PHẨM ---
            if (hd.getSoLuongChiTiet() == 0) {
                System.out.printf("| %-102s |\n", "(Chua co san pham nao trong hoa don)");
            } else {
                System.out.printf("| %-4s | %-10s | %-33s | %-6s | %-16s | %-18s |\n",
                        "STT", "Ma SP", "Ten San Pham", "SL", "Don Gia (VND)", "Thanh Tien (VND)");
                System.out.println("|" + "-".repeat(6) + "+" + "-".repeat(12) + "+" + "-".repeat(35) + "+"
                        + "-".repeat(8) + "+" + "-".repeat(18) + "+" + "-".repeat(20) + "|");

                int stt = 1;
                for (int j = 0; j < hd.getSoLuongChiTiet(); j++) {
                    ChiTietGiaoDich ct = hd.getChiTiet()[j];
                    if (ct == null || ct.getSoLuong() <= 0 || ct.getDonGia() < 0) {
                        continue;
                    }

                    String tenSP = ct.getSanPham().getTenSP();
                    // Cắt tên nếu dài quá 33 ký tự để không vỡ khung
                    if (tenSP.length() > 33) {
                        tenSP = tenSP.substring(0, 30) + "...";
                    }

                    System.out.printf("| %-4d | %-10s | %-33s | %-6d | %,16.0f | %,18.0f |\n",
                            stt++,
                            ct.getMaSP(),
                            tenSP,
                            ct.getSoLuong(),
                            ct.getDonGia(),
                            ct.getThanhTien());
                }
            }

            // --- FOOTER TỔNG TIỀN (Tổng 106 ký tự) ---
            System.out.println("+" + "-".repeat(104) + "+");
            System.out.printf("| PTTT: %-28s | Diem tich: %-9d | TONG CONG: %,27.0f VND |\n",
                    pttt,
                    hd.getChietKhauTichDiem(),
                    hd.tinhTongTien());
            System.out.println("+" + "=".repeat(104) + "+");
            System.out.println();
        }
    }
// Thêm vào DanhSachHoaDon

    public void nhapChiTietChoHoaDonBangMa(String maHD, DanhSachSanPham dssp) {
        HoaDon hd = timKiemTheoMa(maHD);
        if (hd == null) {
            System.out.println("[X] Khong tim thay hoa don: " + maHD);
            return;
        }

        System.out.println("\n--- NHAP SAN PHAM CHO HOA DON " + maHD + " (0 de ket thuc) ---");
        while (true) {
            System.out.print("Ma san pham: ");
            String maSP = sc.nextLine().trim();
            if (maSP.equals("0")) {
                break;
            }
            if (maSP.isEmpty()) {
                System.out.println("[!] Khong duoc de trong!");
                continue;
            }

            SanPham sp = dssp.timKiem(maSP);
            if (sp == null) {
                System.out.println("[X] Khong tim thay san pham!");
                continue;
            }
            System.out.println("Ten: " + sp.getTenSP() + " | Gia ban: " + (long) sp.tinhGiaBan() + " | Ton: " + sp.getSoLuongTon());

            int soLuong;
            boolean skip = false;
            while (true) {
                System.out.print("So luong mua: ");
                try {
                    soLuong = Integer.parseInt(sc.nextLine().trim());
                    if (soLuong < 0) {
                        System.out.println("[!] So luong phai > 0");
                        continue;
                    }
                    if (soLuong == 0) {
                        System.out.println("[!] Bo qua san pham");
                        skip = true;
                        break;
                    }
                    if (soLuong > sp.getSoLuongTon()) {
                        System.out.println("[X] Khong du hang. Con: " + sp.getSoLuongTon());
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so nguyen!");
                }
            }

            // thêm dòng + trừ tồn kho
            if (skip == false) {
                ChiTietGiaoDich ct = new ChiTietGiaoDich(sp.getMaSP(), soLuong, sp.tinhGiaBan(), sp);
                hd.themChiTiet(ct);
                sp.setSoLuongTon(sp.getSoLuongTon() - soLuong);
                System.out.println("[✓] Da them: " + sp.getTenSP() + " x" + soLuong);

                // cập nhật tổng tiền (nếu bạn muốn ghi ngay)
                hd.setTongTien(hd.tinhTongTien());
            }
        }

        System.out.println("[✓] Tong tien hien tai: " + (long) hd.tinhTongTien() + " VND");
    }

    public void themPhanTu(HoaDon hd) {
        if (soLuong >= hoaDons.length) {
            System.out.println("Danh sach da day!");
            return;
        }
        hoaDons[soLuong] = hd;
        soLuong++;
        System.out.println("Them hoa don thanh cong!");
    }

    public void xoaTheoMa(String ma) {
        ma = ma.trim();
        for (int i = 0; i < soLuong; i++) {
            if (hoaDons[i].getMa().equalsIgnoreCase(ma)) {
                HoaDon hd = hoaDons[i];
                // 1) Trả hàng về kho
                for (int j = 0; j < hd.getSoLuongChiTiet(); j++) {
                    ChiTietGiaoDich ct = hd.getChiTiet()[j];
                    if (ct != null && ct.getSanPham() != null) {
                        SanPham sp = ct.getSanPham();
                        sp.setSoLuongTon(sp.getSoLuongTon() + ct.getSoLuong());
                    }
                }
                // 2) Trừ điểm tích lũy
                if (hd.getKhachHang() != null) {
                    int diem = (int) (hd.tinhTongTien() / 100000);
                    hd.getKhachHang().setTichDiem(Math.max(0, hd.getKhachHang().getTichDiem() - diem));
                }

                for (int j = i; j < soLuong - 1; j++) {
                    hoaDons[j] = hoaDons[j + 1];
                }
                hoaDons[soLuong - 1] = null;
                soLuong--;
                System.out.println("Xoa hoa don + hoan kho/diem thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay hoa don co ma: " + ma);
    }

    public void suaTheoMa(String Ma) {
        if (Ma == null) {
            System.out.println("[!] Ma khong hop le");
            return;
        }
        Ma = Ma.trim();
        HoaDon hd = timKiemTheoMa(Ma);
        if (hd == null) {
            System.out.println("[X] Khong tim thay hoa don " + Ma);
            return;
        }
        while (true) {
            String khMa = (hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "(khong)");
            String khTen = (hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "(khong)");
            String nvMa = (hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "(khong)");
            String nvTen = (hd.getNhanVien() != null ? hd.getNhanVien().getHoTen() : "(khong)");
            System.out.println("\n--- SUA HOA DON " + hd.getMa() + " ---");
            System.out.printf("Hien tai -> Ma: %s | Ngay: %s | PTTT: %s | CK: %d | KH: %s - %s | NV: %s - %s%n",
                    hd.getMa(), hd.getNgay(),
                    (hd.getPhuongThucTT() == null ? "(khong)" : hd.getPhuongThucTT()),
                    hd.getChietKhauTichDiem(), khMa, khTen, nvMa, nvTen);
            System.out.println("------------------------------------------");
            System.out.println("1. Ma hoa don");
            System.out.println("2. Ngay (dd/MM/yyyy)");
            System.out.println("3. Chiet khau tich diem (0-100)");
            System.out.println("4. Phuong thuc thanh toan");
            System.out.println("5. Ma khach hang");
            System.out.println("6. Nhan vien");
            System.out.println("0. Luu & thoat");
            System.out.print("Lua chon (0-6): ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(" Vui long nhap tu (0-6)");
                continue;
            }
            if (choice == 0) {
                hd.setTongTien(hd.tinhTongTien());
                System.out.println("[✓] Da cap nhat hoa don!");
                return;
            }
            switch (choice) {
                case 1:
                    System.out.print("Nhap ma moi (khong can 'HD'): ");
                    String newMa = sc.nextLine().trim();
                    if (newMa.isEmpty()) {
                        System.out.println("[!] Khong duoc de trong.");
                        break;
                    }
                    if (!newMa.toUpperCase().startsWith("HD")) {
                        newMa = "HD" + newMa;
                    }
                    HoaDon existed = timKiemTheoMa(newMa);
                    if (existed != null && existed != hd) {
                        System.out.println("[X] Ma " + newMa + " da ton tai!");
                        break;
                    }
                    hd.setMa(newMa);
                    System.out.println("[✓] Doi ma thanh: " + newMa);
                    break;
                case 2:
                    LocalDate now = LocalDate.now();
                    LocalDate minDate = LocalDate.of(2020, 1, 1); // Đặt mốc thời gian tối thiểu
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    
                    while (true) {
                        System.out.print("Nhap ngay (dd/MM/yyyy): ");
                        String s = sc.nextLine().trim();
                        if (!s.matches("\\d{2}/\\d{2}/\\d{4}")) {
                            System.out.println("[!] Dinh dang sai.");
                            continue;
                        }
                        try {
                            LocalDate ngayDate = LocalDate.parse(s, formatter);
                            if (ngayDate.isAfter(now)) {
                                System.out.println("[!] Khong duoc nhap sau ngay hom nay");
                                continue;
                            }
                            // <<< THÊM MỚI >>>
                            if (ngayDate.isBefore(minDate)) {
                                System.out.println("[!] Ngay khong hop le! Nam phai tu 2020 tro di.");
                                continue;
                            }
                            
                            hd.setNgay(s);
                            System.out.println("[✓] Cap nhat ngay: " + s);
                            break;
                        } catch (DateTimeException e) {
                            System.out.println("[!] Ngay khong hop le (vi du: 30/02/2024)");
                        }
                    }
                    break;
                case 3: //ChietKhau
                    System.out.print("Nhap chiet khau tich diem (0-100): ");
                    try {
                        int ck = Integer.parseInt(sc.nextLine().trim());
                        if (ck < 0 || ck > 100) {
                            System.out.println("[!] Phai trong 0-100.");
                            break;
                        }
                        hd.setChietKhauTichDiem(ck);
                        System.out.println("[✓] Cap nhat chiet khau/diem: " + ck);
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Vui long nhap so nguyen.");
                    }
                    break;
                case 4: //PTTT
                    System.out.println("1. Tien mat");
                    System.out.println("2. The tin dung");
                    System.out.println("3. Chuyen khoan");
                    System.out.println("4. Khac");
                    System.out.print("Chon (1-4): ");
                    try {
                        int opt = Integer.parseInt(sc.nextLine().trim());
                        String pt = "";
                        switch (opt) {
                            case 1:
                                pt = "Tien mat";
                                break;
                            case 2:
                                pt = "The tin dung";
                                break;
                            case 3:
                                pt = "Chuyen khoan";
                                break;
                            case 4:
                                pt = "Khac";
                                break;
                            default:
                                System.out.println("[!] Khong hop le.");
                                break;
                        }
                        if (opt >= 1 && opt <= 4) {
                            hd.setPhuongThucTT(pt);
                            System.out.println("[✓] Cap nhat PTTT: " + pt);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Vui long nhap tu 1-4");
                    }
                    break;
                case 5:
                    if (dskhContext == null) {
                        System.out.println("[!] Chua set dskhContext -> khong the sua KH.");
                        break;
                    }
                    System.out.print("Nhap ma KH (de trong de xoa KH): ");
                    String mkh = sc.nextLine().trim();
                    if (mkh.isEmpty()) {
                        hd.setKhachHang(null);
                        System.out.println("[i] Da xoa gan KH.");
                        break;
                    }
                    KhachHang kh = dskhContext.timKiem(mkh);
                    if (kh == null) {
                        System.out.println("[X] Khong tim thay KH: " + mkh);
                    } else {
                        hd.setKhachHang(kh);
                        System.out.println("[✓] Gan KH: " + kh.getMaKH() + " - " + kh.getHoTen());
                    }
                    break;
                case 6:
                    if (nhanVienContext != null) {
                        hd.setNhanVien(nhanVienContext);
                        System.out.println("[✓] Gan NV hien dang dang nhap: " + nhanVienContext.getMaNV()
                                + " - " + nhanVienContext.getHoTen());
                    } else {
                        System.out.println("[!] Khong co nhanVienContext. (Can bo sung setter hoac truyen DSNV neu muon chon NV bat ky)");
                    }
                    break;
                default:
                    System.out.println("[!] Lua chon khong hop le.");
            }
        }
    }

    public HoaDon timKiemTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (hoaDons[i].getMa().equalsIgnoreCase(ma)) {
                return hoaDons[i];
            }
        }
        return null;
    }

    public double tinhDoanhThuThang(int thang, int nam) {
        double doanhThu = 0;
        for (int i = 0; i < soLuong; i++) {
            String ngay = hoaDons[i].getNgay();
            if (ngay != null && ngay.length() >= 10) {
                String[] parts = ngay.split("/");
                if (parts.length == 3) {
                    try {
                        int thangHD = Integer.parseInt(parts[1]);
                        int namHD = Integer.parseInt(parts[2]);
                        if (thangHD == thang && namHD == nam) {
                            doanhThu += hoaDons[i].tinhTongTien();
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu format ngày không đúng
                    }
                }
            }
        }
        return doanhThu;
    }

    public void timKiemVaHienThi(String key) {
        boolean found = false;
        String keyLower = (key == null) ? "" : key.toLowerCase();

        // --- HEADER KẾT QUẢ TÌM KIẾM ---
        System.out.println("\n" + "=".repeat(115));
        System.out.printf("| %-111s |\n", "KET QUA TIM KIEM TU KHOA: \"" + key.toUpperCase() + "\"");
        System.out.println("=".repeat(115));

        int countResult = 0;

        for (int i = 0; i < soLuong; i++) {
            HoaDon hd = hoaDons[i];
            if (hd == null) {
                continue;
            }

            // Lấy thông tin an toàn
            KhachHang kh = hd.getKhachHang();
            NhanVien nv = hd.getNhanVien();

            String maHD = (hd.getMa() == null) ? "" : hd.getMa();
            String ngay = (hd.getNgay() == null) ? "" : hd.getNgay();
            String pttt = (hd.getPhuongThucTT() == null) ? "" : hd.getPhuongThucTT();

            String khMa = (kh != null && kh.getMaKH() != null) ? kh.getMaKH() : "";
            String khTen = (kh != null && kh.getHoTen() != null) ? kh.getHoTen() : "";
            String nvMa = (nv != null && nv.getMaNV() != null) ? nv.getMaNV() : "";
            String nvTen = (nv != null && nv.getHoTen() != null) ? nv.getHoTen() : "";

            // Logic tìm kiếm (giữ nguyên của bạn)
            boolean match = maHD.toLowerCase().contains(keyLower)
                    || ngay.contains(key)
                    || pttt.toLowerCase().contains(keyLower)
                    || khMa.toLowerCase().contains(keyLower)
                    || khTen.toLowerCase().contains(keyLower)
                    || nvMa.toLowerCase().contains(keyLower)
                    || nvTen.toLowerCase().contains(keyLower);

            if (!match) {
                continue;
            }

            found = true;
            countResult++;

            // --- HIỂN THỊ KẾT QUẢ THEO FORM CHUẨN 115 KÝ TỰ ---
            // Xử lý chuỗi dài
            if (khTen.length() > 30) {
                khTen = khTen.substring(0, 27) + "...";
            }
            if (nvTen.length() > 50) {
                nvTen = nvTen.substring(0, 47) + "...";
            }

            // HEADER HÓA ĐƠN
            System.out.println("+" + "=".repeat(113) + "+");

            // Dòng 1: Tiêu đề + Mã + Ngày
            String row1Left = String.format("KET QUA #%-3d  Ma: %s", countResult, maHD);
            System.out.printf("| %-46s | Ngay lap: %-52s |\n", row1Left, ngay);

            // Dòng 2: Khách hàng + Nhân viên
            System.out.printf("| KH: %-42s | NV: %-58s |\n", khTen, nvTen);

            System.out.println("+" + "-".repeat(113) + "+");

            // BODY: DANH SÁCH SẢN PHẨM
            if (hd.getSoLuongChiTiet() <= 0) {
                System.out.printf("| %-113s |\n", "(Chua co san pham nao)");
            } else {
                // Header bảng
                System.out.printf("| %-4s | %-10s | %-32s | %-8s | %-20s | %-22s |\n",
                        "STT", "Ma SP", "Ten SP", "So luong", "Don gia", "Thanh tien");
                System.out.println("|" + "-".repeat(6) + "+" + "-".repeat(12) + "+" + "-".repeat(34) + "+"
                        + "-".repeat(10) + "+" + "-".repeat(22) + "+" + "-".repeat(24) + "|");

                for (int j = 0; j < hd.getSoLuongChiTiet(); j++) {
                    ChiTietGiaoDich ct = hd.getChiTiet()[j];
                    if (ct == null || ct.getSoLuong() <= 0) {
                        continue;
                    }

                    String tenSP = (ct.getSanPham() != null) ? ct.getSanPham().getTenSP() : "(khong ten)";
                    if (tenSP.length() > 30) {
                        tenSP = tenSP.substring(0, 27) + "...";
                    }

                    System.out.printf("| %-4d | %-10s | %-32s | %-8d | %,20.0f | %,22.0f |\n",
                            (j + 1),
                            ct.getMaSP(),
                            tenSP,
                            ct.getSoLuong(),
                            ct.getDonGia(),
                            ct.getThanhTien());
                }
            }

            // FOOTER: TỔNG TIỀN
            System.out.println("+" + "-".repeat(113) + "+");
            System.out.printf("| PTTT: %-32s | Diem tich: %-12d | TONG CONG: %,29.0f VND |\n",
                    pttt, hd.getChietKhauTichDiem(), hd.tinhTongTien());
            System.out.println("+" + "=".repeat(113) + "+");
            System.out.println(); // Dòng trống ngăn cách giữa các kết quả
        }

        if (!found) {
            System.out.printf("| %-111s |\n", "Khong tim thay hoa don nao phu hop!");
            System.out.println("=".repeat(115));
        }
    }

    public void thongKeNgay(String ngay) {
        System.out.println("\n========== THONG KE NGAY " + ngay + " ==========");
        int soHoaDon = 0;
        double tongDoanhThu = 0;
        for (int i = 0; i < soLuong; i++) {
            if (hoaDons[i].getNgay().equals(ngay)) {
                soHoaDon++;
                tongDoanhThu += hoaDons[i].tinhTongTien();
                System.out.println("- Ma HD: " + hoaDons[i].getMa()
                        + " - Tong tien: " + String.format("%.2f", hoaDons[i].tinhTongTien()));
            }
        }

        System.out.println("\nTong so hoa don: " + soHoaDon);
        System.out.println("Tong doanh thu: " + String.format("%.2f", tongDoanhThu) + " VND");
        if (soHoaDon > 0) {
            System.out.println("Trung binh/hoa don: " + String.format("%.2f", tongDoanhThu / soHoaDon) + " VND");
        }
        System.out.println("=============================================");
    }

    public void docFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/hoadon.txt"))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < hoaDons.length) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Giữ phần tử trống ở cuối
                String[] parts = line.split(",\\s*", -1);

                if (parts.length >= 5) { // đủ để tạo HD (KH/NV liên kết sau)
                    String ma = parts[0].trim();
                    String ngay = parts[1].trim();
                    String phuongThuc = parts[3].trim();
                    int chietKhau = 0;
                    try {
                        chietKhau = Integer.parseInt(parts[4].trim());
                    } catch (NumberFormatException e) {
                        chietKhau = 0; // fallback an toàn
                    }
                    hoaDons[soLuong++] = new HoaDon(ma, ngay, 0, phuongThuc, chietKhau, null, null);
                }
            }
            System.out.println("[+] Doc file hoa don thanh cong! So luong: " + soLuong);
        } catch (IOException e) {
            System.out.println("[!] Loi doc file hoa don: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/hoadon.txt"))) {
            for (int i = 0; i < soLuong; i++) {
                HoaDon hd = hoaDons[i];
                StringBuilder line = new StringBuilder();
                line.append(hd.getMa()).append(", ")
                        .append(hd.getNgay()).append(", ")
                        .append(hd.tinhTongTien()).append(", ")
                        .append(hd.getPhuongThucTT()).append(", ")
                        .append(hd.getChietKhauTichDiem());

                if (hd.getKhachHang() != null) {
                    line.append(", ").append(hd.getKhachHang().getMaKH());
                } else {
                    line.append(", ");
                }

                if (hd.getNhanVien() != null) {
                    line.append(", ").append(hd.getNhanVien().getMaNV());
                } else {
                    line.append(", ");
                }
                pw.println(line.toString());
            }
            System.out.println("[+] Ghi file hoa don thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi ghi file hoa don: " + e.getMessage());
        }
        ghiChiTietFile();
    }

    public void docChiTietFile(DanhSachSanPham dssp) {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/chitiethoadon.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

                    String maHD = parts[0];
                    String maSP = parts[1];
                    int soLuong = Integer.parseInt(parts[2]);
                    double donGia = Double.parseDouble(parts[3]);

                    HoaDon hd = timKiemTheoMa(maHD);
                    SanPham sp = dssp.timKiem(maSP);

                    if (hd != null && sp != null) {
                        ChiTietGiaoDich ct = new ChiTietGiaoDich(maSP, soLuong, donGia, sp);
                        hd.themChiTiet(ct);
                    }
                }
            }
            System.out.println("Doc file chi tiet hoa don thanh cong!");
        } catch (IOException e) {
            System.out.println("Loi doc file chi tiet hoa don: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Loi dinh dang so trong file chi tiet hoa don: " + e.getMessage());
        }
    }

    public void ghiChiTietFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/chitiethoadon.txt"))) {
            for (int i = 0; i < soLuong; i++) {
                HoaDon hd = hoaDons[i];
                for (int j = 0; j < hd.getSoLuongChiTiet(); j++) {
                    ChiTietGiaoDich ct = hd.getChiTiet()[j];
                    pw.println(hd.getMa() + "," + ct.getMaSP() + "," + ct.getSoLuong() + "," + ct.getDonGia());
                }
            }
            System.out.println("Ghi file chi tiet hoa don thanh cong!");
        } catch (IOException e) {
            System.out.println("Loi ghi file chi tiet hoa don: " + e.getMessage());
        }
    }

    public void lienKetKhachHangVaNhanVien(DanhSachKhachHang dsKH, DanhSachNhanVien dsNV) {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/hoadon.txt"))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null && index < soLuong) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

                    String maKH = parts[5];
                    String maNV = parts[6];

                    if (!maKH.isEmpty()) {
                        KhachHang kh = dsKH.timKiem(maKH);
                        if (kh != null) {
                            hoaDons[index].setKhachHang(kh);
                        }
                    }

                    if (!maNV.isEmpty()) {
                        NhanVien nv = dsNV.timKiem(maNV);
                        if (nv != null) {
                            hoaDons[index].setNhanVien(nv);
                        }
                    }
                    index++;
                }
            }
            System.out.println("Lien ket khach hang va nhan vien thanh cong!");
        } catch (IOException e) {
            System.out.println("Loi lien ket: " + e.getMessage());
        }
    }
}
