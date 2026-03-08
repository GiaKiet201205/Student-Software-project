
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static DanhSachSanPham dsSanPham = new DanhSachSanPham();
    private static DanhSachKhachHang dsKhachHang = new DanhSachKhachHang();
    private static DanhSachNhanVien dsNhanVien = new DanhSachNhanVien();
    private static DanhSachHoaDon dsHoaDon = new DanhSachHoaDon();
    private static DanhSachPhieuNhap dsPhieuNhap = new DanhSachPhieuNhap();
    private static DanhSachNhaCungCap dsNhaCungCap = new DanhSachNhaCungCap();
    private static DanhSachLoHang dsLoHang = new DanhSachLoHang();
    private static BaoCaoThongKe baoCaoThongKe = new BaoCaoThongKe();
    private static ThongBao thongBao = new ThongBao();
    private static NhanVien nhanVienDangNhap = null;
    private static KhachHang khachHangHienTai = null;

    private static LocalDate currentDate;
    private static int currentYear;

    public static void main(String[] args) {
        currentDate = LocalDate.now();
        currentYear = currentDate.getYear();

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║           HE THONG QUAN LY NHA THUOC              ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        // Đọc dữ liệu từ file
        docDuLieuTuFile();

        // Menu lựa chọn vai trò
        menuLuaChonVaiTro();

        // Lưu dữ liệu khi thoát
        luuDuLieuKhiThoat();
    }

    // ========== ĐỌC DỮ LIỆU TỪ FILE ==========
    public static void docDuLieuTuFile() {
        System.out.println("\n[*] Dang doc du lieu tu file...");

        dsSanPham.docFile();
        dsKhachHang.docFile();
        dsNhanVien.docFile();
        dsNhaCungCap.docFile();
        dsHoaDon.docFile();
        dsPhieuNhap.docFile();

        // Sau khi đọc tất cả, thực hiện liên kết dữ liệu
        dsHoaDon.lienKetKhachHangVaNhanVien(dsKhachHang, dsNhanVien);
        dsHoaDon.docChiTietFile(dsSanPham);
        dsPhieuNhap.lienKetNhaCungCapVaNhanVien(dsNhaCungCap, dsNhanVien);
        dsLoHang.docFile(); // đọc file lohang trước (nếu chưa có)
        dsPhieuNhap.docChiTietFile(dsSanPham, dsLoHang);

        dsHoaDon.setDskhContext(dsKhachHang);
        dsHoaDon.setDsspContext(dsSanPham);

        // Đồng bộ dữ liệu vào báo cáo thống kê
        for (int i = 0; i < dsHoaDon.getSoLuong(); i++) {
            baoCaoThongKe.themHoaDon(dsHoaDon.getHoaDons()[i]);
        }
        for (int i = 0; i < dsPhieuNhap.getSoLuong(); i++) {
            baoCaoThongKe.themPhieuNhap(dsPhieuNhap.getPhieuNhaps()[i]);
        }

        System.out.println("\n[✓] Doc du lieu thanh cong!");
        System.out.println("    - San pham: " + dsSanPham.getSoLuong());
        System.out.println("    - Khach hang: " + dsKhachHang.getSoLuong());
        System.out.println("    - Nhan vien: " + dsNhanVien.getSoLuong());
        System.out.println("    - Nha cung cap: " + dsNhaCungCap.getSoLuong());
        System.out.println("    - Hoa don: " + dsHoaDon.getSoLuong());
        System.out.println("    - Phieu nhap: " + dsPhieuNhap.getSoLuong());

        System.out.println("\n[!] Kiem tra ton kho...");
        boolean coSanPhamThapTon = false;
        for (int i = 0; i < dsSanPham.getSoLuong(); i++) {
            SanPham sp = dsSanPham.getSanPhams()[i];
            if (sp != null && sp.getSoLuongTon() < 10) {
                thongBao.guiThongBaoThapTon(sp);
                coSanPhamThapTon = true;
            }
        }
        if (!coSanPhamThapTon) {
            System.out.println("    [✓] Tat ca san pham deu con du hang!");
        }
    }

    // ========== LƯU DỮ LIỆU KHI THOÁT ==========
    public static void luuDuLieuKhiThoat() {
        System.out.print("\n[?] Ban co muon luu du lieu vao file khong? (Y/N): ");
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("[*] Dang luu du lieu...");
            dsSanPham.ghiFile();
            dsKhachHang.ghiFile();
            dsNhanVien.ghiFile();
            dsNhaCungCap.ghiFile();
            dsHoaDon.ghiFile();
            dsPhieuNhap.ghiFile();
            dsLoHang.ghiFile();
            System.out.println("[✓] Luu du lieu thanh cong!");
        } else {
            System.out.println("[!] Khong luu du lieu.");
        }
    }

    // ========== LƯU DỮ LIỆU RA FILE (CHO NHÂN VIÊN) ==========
    public static void luuDuLieuRaFile() {
        System.out.println("\n[*] Dang luu du lieu vao file...");
        dsSanPham.ghiFile();
        dsKhachHang.ghiFile();
        dsNhanVien.ghiFile();
        dsNhaCungCap.ghiFile();
        dsHoaDon.ghiFile();
        dsPhieuNhap.ghiFile();
        dsLoHang.ghiFile();
        System.out.println("[✓] Luu tat ca du lieu thanh cong!");
    }

    // ========== MENU LỰA CHỌN VAI TRÒ ==========
    public static void menuLuaChonVaiTro() {
        while (true) {
            try {
                System.out.println("\n╔═══════════════════════════════════════════════════╗");
                System.out.println("║           CHON VAI TRO DANG NHAP                  ║");
                System.out.println("╠═══════════════════════════════════════════════════╣");
                System.out.println("║  1. Nhan vien (Dang nhap)                         ║");
                System.out.println("║  2. Khach hang (Mua hang)                         ║");
                System.out.println("║  0. Thoat                                         ║");
                System.out.println("╚═══════════════════════════════════════════════════╝");
                System.out.print("Chon vai tro: ");

                if (!sc.hasNextInt()) {
                    System.out.println("[!] Vui long nhap so!");
                    sc.nextLine(); // Xóa dữ liệu lỗi
                    continue;
                }

                int choice = sc.nextInt();
                sc.nextLine(); // Xóa newline

                switch (choice) {
                    case 1:
                        if (dangNhapNhanVien()) {
                            menuNhanVien();
                        }
                        break;
                    case 2:
                        menuKhachHang();
                        break;
                    case 0:
                        System.out.println("\n[*] Cam on ban da su dung he thong!");
                        System.out.println("[*] Tam biet!");
                        return;
                    default:
                        System.out.println("[!] Lua chon khong hop le!");
                }
            } catch (Exception e) {
                System.out.println("[!] Loi: " + e.getMessage());
                sc.nextLine(); // Xóa dữ liệu lỗi
            }
        }
    }

    // ========== ĐĂNG NHẬP NHÂN VIÊN ==========
    public static boolean dangNhapNhanVien() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║            DANG NHAP NHAN VIEN                    ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");

        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        // Kiểm tra đăng nhập
        for (int i = 0; i < dsNhanVien.getSoLuong(); i++) {
            NhanVien nv = dsNhanVien.getNhanViens()[i];
            if (nv.getTaiKhoan() != null
                    && nv.getTaiKhoan().getUsername().equals(username)
                    && nv.getTaiKhoan().getPassword().equals(password)) {

                nhanVienDangNhap = nv;
                System.out.println("\n[✓] Dang nhap thanh cong!");
                System.out.println("Chao mung: " + nv.getHoTen() + " (" + nv.getChucVu() + ")");
                return true;
            }
        }

        System.out.println("\n[X] Dang nhap that bai! Sai username hoac password.");
        return false;
    }

    // ========== MENU NHÂN VIÊN (ADMIN) ==========
    public static void menuNhanVien() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out
                    .println("║      MENU NHAN VIEN - " + String.format("%-28s", nhanVienDangNhap.getHoTen()) + "║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Quan ly San Pham                              ║");
            System.out.println("║  2. Quan ly Khach Hang                            ║");
            System.out.println("║  3. Quan ly Nhan Vien                             ║");
            System.out.println("║  4. Quan ly Nha Cung Cap                          ║");
            System.out.println("║  5. Quan ly Hoa Don                               ║");
            System.out.println("║  6. Quan ly Phieu Nhap                            ║");
            System.out.println("║  7. Thong Ke va Bao Cao                           ║");
            System.out.println("║  8. Kiem Tra Ton Kho                              ║");
            System.out.println("║  9. Luu du lieu ra file                           ║");
            System.out.println("║  0. Dang xuat                                     ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    menuQuanLySanPham();
                    break;
                case 2:
                    menuQuanLyKhachHang();
                    break;
                case 3:
                    menuQuanLyNhanVien();
                    break;
                case 4:
                    menuQuanLyNhaCungCap();
                    break;
                case 5:
                    menuQuanLyHoaDon();
                    break;
                case 6:
                    menuQuanLyPhieuNhap();
                    break;
                case 7:
                    menuQuanLyBaoCao();
                    break;
                case 8:
                    dsSanPham.kiemTraTonKho();
                    break;
                case 9:
                    luuDuLieuRaFile();
                    break;
                case 0:
                    System.out.println("\n[*] Dang xuat thanh cong!");
                    nhanVienDangNhap = null;
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    // ========== MENU KHÁCH HÀNG (USER) ==========
    public static void menuKhachHang() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║            MENU KHACH HANG                        ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Xem danh sach san pham                        ║");
            System.out.println("║  2. Tim kiem san pham                             ║");
            System.out.println("║  3. Tao hoa don mua hang                          ║");
            System.out.println("║  4. Xem thong tin tai khoan                       ║");
            System.out.println("║  5. Dang ky thanh vien moi                        ║");
            System.out.println("║  6. Xuat thong tin hoa don                        ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    xemDanhSachSanPhamKhachHang();
                    break;
                case 2:
                    timKiemSanPhamKhachHang();
                    break;
                case 3:
                    taoHoaDonMuaHang();
                    break;
                case 4:
                    xemThongTinKhachHang();
                    break;
                case 5:
                    dangKyKhachHangMoi();
                    break;
                case 6:
                    xemThongTinHoaDon();
                    break;
                case 0:
                    khachHangHienTai = null;
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    // ========== CHỨC NĂNG KHÁCH HÀNG ==========
    public static void xemDanhSachSanPhamKhachHang() {
        if (dsSanPham.getSoLuong() == 0) {
            System.out.println("Hien tai chua co san pham nao!");
            return;
        }
        System.out.println("\n" + "=".repeat(114));
        System.out.printf("|%s%s%s|\n",
                " ".repeat(43), "DANH SACH SAN PHAM HIEN CO", " ".repeat(43));
        System.out.println("=" + "=".repeat(112) + "="); 

        System.out.printf("| %-4s | %-10s | %-30s | %-15s | %-10s | %-15s | %-8s |\n",
                "STT", "Ma SP", "Ten San Pham", "Nha San Xuat", "DVT", "Gia Ban (VND)", "SL Ton");

        System.out.println("|" + "-".repeat(6) + "+" + "-".repeat(12) + "+" + "-".repeat(32) + "+"
                + "-".repeat(17) + "+" + "-".repeat(12) + "+" + "-".repeat(17) + "+" + "-".repeat(10) + "|");

        for (int i = 0; i < dsSanPham.getSoLuong(); i++) {
            SanPham sp = dsSanPham.getSanPhams()[i];
            if (sp == null) {
                continue;
            }

            String tenSP = sp.getTenSP();
            if (tenSP.length() > 30) {
                tenSP = tenSP.substring(0, 27) + "...";
            }

            String nsx = sp.getNhaSanXuat();
            if (nsx.length() > 15) {
                nsx = nsx.substring(0, 12) + "...";
            }

            System.out.printf("| %-4d | %-10s | %-30s | %-15s | %-10s | %,15.0f | %-8d |\n",
                    (i + 1),
                    sp.getMaSP(),
                    tenSP,
                    nsx,
                    sp.getDonViTinh(),
                    sp.tinhGiaBan(),
                    sp.getSoLuongTon());
        }
        System.out.println("+" + "=".repeat(112) + "+");
        System.out.println();
    }

    public static void timKiemSanPhamKhachHang() {
        System.out.print("\nNhap tu khoa tim kiem: ");
        String keyword = sc.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("[!] Vui long nhap tu khoa!");
            return;
        }

        boolean found = false;
        int count = 0;

        // --- HEADER BẢNG (TỔNG 114 KÝ TỰ) ---
        System.out.println("\n" + "=".repeat(114));
        System.out.printf("| %-110s |\n", "KET QUA TIM KIEM: \"" + keyword.toUpperCase() + "\"");
        System.out.println("=" + "=".repeat(112) + "=");

        System.out.printf("| %-4s | %-10s | %-30s | %-15s | %-10s | %-15s | %-8s |\n",
                "STT", "Ma SP", "Ten San Pham", "Nha San Xuat", "DVT", "Gia Ban (VND)", "SL Ton");

        System.out.println("|" + "-".repeat(6) + "+" + "-".repeat(12) + "+" + "-".repeat(32) + "+"
                + "-".repeat(17) + "+" + "-".repeat(12) + "+" + "-".repeat(17) + "+" + "-".repeat(10) + "|");


        String fmtDetail = "|          %-101s |\n";

        for (int i = 0; i < dsSanPham.getSoLuong(); i++) {
            SanPham sp = dsSanPham.getSanPhams()[i];
            if (sp == null) {
                continue;
            }

            boolean match = sp.getTenSP().toLowerCase().contains(keyword.toLowerCase())
                    || sp.getMaSP().toLowerCase().contains(keyword.toLowerCase());

            if (match) {
                found = true;
                count++;

                // --- DÒNG 1: THÔNG TIN CƠ BẢN ---
                String tenSP = sp.getTenSP();
                if (tenSP.length() > 30) {
                    tenSP = tenSP.substring(0, 27) + "...";
                }

                String nsx = sp.getNhaSanXuat();
                if (nsx.length() > 15) {
                    nsx = nsx.substring(0, 12) + "...";
                }

                System.out.printf("| %-4d | %-10s | %-30s | %-15s | %-10s | %,15.0f | %-8d |\n",
                        count, sp.getMaSP(), tenSP, nsx, sp.getDonViTinh(), sp.tinhGiaBan(), sp.getSoLuongTon());

                String typeLabel = "";
                if (sp instanceof Thuoc) {
                    typeLabel = "[THUOC]"; 
                }else if (sp instanceof ThucPhamChucNang) {
                    typeLabel = "[TPCN]"; 
                }else if (sp instanceof DungCuYTe) {
                    typeLabel = "[DUNG CU]"; 
                }else if (sp instanceof SanPhamChamSocSacDep) {
                    typeLabel = "[MY PHAM]";
                }

                System.out.printf("|      +-> %-101s |\n", typeLabel);

                // In từng thuộc tính ra từng dòng riêng
                if (sp instanceof Thuoc) {
                    Thuoc t = (Thuoc) sp;
                    System.out.printf(fmtDetail, "- Hoat chat:    " + t.getHoatChat());
                    System.out.printf(fmtDetail, "- Dang bao che: " + t.getDangBaoChe());
                    System.out.printf(fmtDetail, "- Han su dung:  " + t.getHanSuDung());
                } else if (sp instanceof ThucPhamChucNang) {
                    ThucPhamChucNang tpcn = (ThucPhamChucNang) sp;
                    System.out.printf(fmtDetail, "- Thanh phan:   " + tpcn.getThanhPhan()); // Giả sử có hàm này
                    System.out.printf(fmtDetail, "- Cong dung:    " + tpcn.getCongDung());
                    System.out.printf(fmtDetail, "- Bao quan:     " + tpcn.getHinhThucBaoQuan());
                    System.out.printf(fmtDetail, "- Han su dung:  " + tpcn.getHanSuDung());
                } else if (sp instanceof DungCuYTe) {
                    DungCuYTe dc = (DungCuYTe) sp;
                    System.out.printf(fmtDetail, "- Loai dung cu: " + dc.getLoaiDungCu());
                    System.out.printf(fmtDetail, "- Vat lieu:     " + dc.getVatLieu());
                    System.out.printf(fmtDetail, "- Bao hanh:     " + dc.getHanBaoHanh());
                } else if (sp instanceof SanPhamChamSocSacDep) {
                    SanPhamChamSocSacDep mp = (SanPhamChamSocSacDep) sp;
                    System.out.printf(fmtDetail, "- Cong dung:    " + mp.getCongDung());
                    System.out.printf(fmtDetail, "- Loai da:      " + mp.getLoaiDaPhuHop());
                    System.out.printf(fmtDetail, "- Han su dung:  " + mp.getHanSuDung());
                }

                // Đường kẻ ngăn cách
                System.out.println("|" + "-".repeat(112) + "|");
            }
        }

        // --- FOOTER ---
        if (!found) {
            System.out.printf("| %-110s |\n", "Khong tim thay san pham nao phu hop!");
        } else {
            System.out.printf("| %-110s |\n", "Tim thay " + count + " ket qua.");
        }
        System.out.println("+" + "=".repeat(112) + "+");
    }

    public static void taoHoaDonMuaHang() {
        // Kiểm tra hoặc đăng nhập khách hàng
        if (khachHangHienTai == null) {
            System.out.println("\n[*] Vui long dang nhap hoac dang ky thanh vien:");
            System.out.print("Ban co tai khoan? (1-Co, 2-Chua co): ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Nhap ma khach hang: ");
                String maKH = sc.nextLine();
                khachHangHienTai = dsKhachHang.timKiem(maKH);
                if (khachHangHienTai == null) {
                    System.out.println("[X] Khong tim thay khach hang!");
                    return;
                }
                System.out.println("[✓] Xin chao " + khachHangHienTai.getHoTen() + "!");
            } else {
                dangKyKhachHangMoi();
                if (khachHangHienTai == null) {
                    return;
                }
            }
        }

        // Tạo hóa đơn
        String maHD = "HD" + String.format("%03d", dsHoaDon.getSoLuong() + 1);
        HoaDon hd = new HoaDon(maHD,
                java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                0, "Tien mat", 0, khachHangHienTai, null);

        System.out.println("\n========== TAO HOA DON MUA HANG ==========");
        System.out.println("Ma hoa don: " + maHD);
        System.out.println("Khach hang: " + khachHangHienTai.getHoTen());

        while (true) {
            System.out.print("\nNhap ma san pham (0 de ket thuc): ");
            String maSP = sc.nextLine();
            if (maSP.equals("0")) {
                break;
            }
            if (maSP.isEmpty()) {
                System.out.println("[!] Khong duoc de trong!");
                continue;
            }

            SanPham sp = dsSanPham.timKiem(maSP);
            if (sp == null) {
                System.out.println("[X] Khong tim thay san pham!");
                continue;
            }

            System.out.println("San pham: " + sp.getTenSP());
            System.out.println("Gia: " + String.format("%.0f", sp.tinhGiaBan()) + " VND");
            System.out.println("Ton kho: " + sp.getSoLuongTon());
            int soLuong = 0;
            boolean skip = false;
            while (true) {
                System.out.print("So luong mua: ");
                try {
                    soLuong = Integer.parseInt(sc.nextLine());

                    if (soLuong < 0) {
                        System.out.println("[!] So luong mua phai > 0");
                        continue;
                    }
                    if (soLuong == 0) {
                        System.out.println("Bo qua san pham");
                        skip = true;
                        break;
                    }
                    if (soLuong > sp.getSoLuongTon()) {
                        System.out.println("[X] Khong du hang trong kho!");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so luong hop le");
                    continue;
                }
                break;
            }
            if (skip) {
                continue;
            }
            ChiTietGiaoDich ct = new ChiTietGiaoDich(sp.getMaSP(), soLuong, sp.tinhGiaBan(), sp);
            hd.themChiTiet(ct);
            sp.setSoLuongTon(sp.getSoLuongTon() - soLuong);
            System.out.println("[✓] Them san pham thanh cong!");
        }

        if (hd.getSoLuongChiTiet() == 0) {
            System.out.println("[!] Hoa don rong, huy tao!");
            return;
        }

        // Chọn phương thức thanh toán
        System.out.println("\nPhuong thuc thanh toan:");
        System.out.println("1. Tien mat");
        System.out.println("2. Chuyen khoan");
        System.out.println("3. The tin dung");
        int phuongThuc;
        while (true) {
            System.out.print("Chon tu (1-3): ");
            try {
                phuongThuc = Integer.parseInt(sc.nextLine());
                if (phuongThuc < 1 || phuongThuc > 3) {
                    System.out.println("Vui long nhap tu 1-3");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("[!] Vui long nhap tu 1-3");
            }
        }
        String[] pttt = {"", "Tien mat", "Chuyen khoan", "The tin dung"};
        hd.setPhuongThucTT(pttt[phuongThuc]);

        hd.setTongTien(hd.tinhTongTien());

        // Tính tích điểm (1% giá trị hóa đơn)
        int diemThem = (int) (hd.getTongTien() / 100000);
        hd.setChietKhauTichDiem(diemThem);
        khachHangHienTai.setTichDiem(khachHangHienTai.getTichDiem() + diemThem);

        dsHoaDon.them(hd);

        System.out.println("\n========== HOA DON ==========");
        System.out.println("Ma HD: " + hd.getMa());
        System.out.println("Ngay: " + hd.getNgay());
        System.out.println("Khach hang: " + khachHangHienTai.getHoTen());
        System.out.println("So luong SP: " + hd.getSoLuongChiTiet());
        System.out.println("Tong tien: " + String.format("%.0f", hd.getTongTien()) + " VND");
        System.out.println("Phuong thuc TT: " + hd.getPhuongThucTT());
        System.out.println("Diem tich luy: +" + diemThem + " diem");
        System.out.println("Tong diem hien tai: " + khachHangHienTai.getTichDiem() + " diem");
        System.out.println("============================");
        System.out.println("[✓] Tao hoa don thanh cong!");
    }

    public static void xemThongTinHoaDon() {
        if (khachHangHienTai == null) {
            System.out.print("\nNhap ma khach hang can xem: ");
            String maKH = sc.nextLine().trim();
            khachHangHienTai = dsKhachHang.timKiem(maKH);
            if (khachHangHienTai == null) {
                System.out.println("[X] Khong tim thay khach hang co ma: " + maKH);
                return;
            }
        }

        if (dsHoaDon.getSoLuong() == 0) {
            System.out.println("[!] He thong chua co hoa don nao.");
            return;
        }

        HoaDon[] all = dsHoaDon.getHoaDons();
        int n = dsHoaDon.getSoLuong();
        int dem = 0;

        // --- In danh sách hóa đơn ---
        System.out.println("\n" + "=".repeat(40) + " LICH SU MUA HANG: " + khachHangHienTai.getHoTen().toUpperCase() + " " + "=".repeat(40));

        for (int i = 0; i < n; i++) {
            HoaDon hd = all[i];
            if (hd == null || hd.getKhachHang() == null) {
                continue;
            }

            // Chỉ lấy hóa đơn của khách hàng hiện tại
            if (!hd.getKhachHang().getMaKH().equalsIgnoreCase(khachHangHienTai.getMaKH())) {
                continue;
            }

            dem++;
            // Gọi hàm in chi tiết (đã làm đẹp)
            inHoaDonChiTiet(hd, dem);
        }

        if (dem == 0) {
            System.out.println("[!] Khach hang nay chua co hoa don nao.");
        } else {
            System.out.println("\n" + "=".repeat(115));
        }
    }

    public static void inHoaDonChiTiet(HoaDon hd, int sttHD) {
        String tenKH = (hd.getKhachHang() != null) ? hd.getKhachHang().getHoTen() : "Khach vang lai";
        String tenNV = (hd.getNhanVien() != null) ? hd.getNhanVien().getHoTen() : "N/A";
        String pttt = (hd.getPhuongThucTT() != null) ? hd.getPhuongThucTT() : "Khong xac dinh";

        // Cắt tên nếu quá dài
        if (tenKH.length() > 30) {
            tenKH = tenKH.substring(0, 27) + "...";
        }
        if (tenNV.length() > 50) {
            tenNV = tenNV.substring(0, 47) + "...";
        }

        System.out.println("\n+" + "=".repeat(113) + "+");

        String row1Left = String.format("HOA DON #%-3d  Ma: %s", sttHD, hd.getMa());
        System.out.printf("| %-46s | Ngay lap: %-52s |\n",
                row1Left, hd.getNgay());

        System.out.printf("| KH: %-42s | NV: %-58s |\n",
                tenKH, tenNV);

        System.out.println("+" + "-".repeat(113) + "+");

        if (hd.getSoLuongChiTiet() == 0) {
            System.out.printf("| %-113s |\n", "(Chua co san pham nao trong hoa don)");
        } else {
            System.out.printf("| %-4s | %-10s | %-32s | %-8s | %-20s | %-22s |\n",
                    "STT", "Ma SP", "Ten san pham", "SL", "Don gia (VND)", "Thanh tien (VND)");

            System.out.println("|" + "-".repeat(6) + "+" + "-".repeat(12) + "+" + "-".repeat(34) + "+"
                    + "-".repeat(10) + "+" + "-".repeat(22) + "+" + "-".repeat(24) + "|");

            for (int i = 0; i < hd.getSoLuongChiTiet(); i++) {
                ChiTietGiaoDich ct = hd.getChiTiet()[i];
                if (ct == null || ct.getSoLuong() <= 0) {
                    continue;
                }

                String tenSP = ct.getSanPham().getTenSP();
                if (tenSP.length() > 30) {
                    tenSP = tenSP.substring(0, 27) + "...";
                }

                System.out.printf("| %-4d | %-10s | %-32s | %-8d | %,20.0f | %,22.0f |\n",
                        (i + 1), ct.getMaSP(), tenSP, ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien());
            }
        }

        System.out.println("+" + "-".repeat(113) + "+");
        System.out.printf("| PTTT: %-32s | Diem tich: %-12d | TONG CONG: %,29.0f VND |\n",
                pttt, hd.getChietKhauTichDiem(), hd.tinhTongTien());
        System.out.println("+" + "=".repeat(113) + "+");
    }

    public static void xemThongTinKhachHang() {
        if (khachHangHienTai == null) {
            System.out.print("\nNhap ma khach hang: ");
            String maKH = sc.nextLine();
            khachHangHienTai = dsKhachHang.timKiem(maKH);
            if (khachHangHienTai == null) {
                System.out.println("[X] Khong tim thay khach hang!");
                return;
            }
        }

        System.out.println("\n========== THONG TIN TAI KHOAN ==========");
        khachHangHienTai.hienThiThongTin();
        System.out.println("=========================================");
    }

    public static void dangKyKhachHangMoi() {
        System.out.println("\n========== DANG KY THANH VIEN MOI ==========");
        String maKH = "KH" + String.format("%03d", dsKhachHang.getSoLuong() + 1);

        System.out.print("Ho ten: ");
        String hoTen = "";
        while (true) {
            hoTen = sc.nextLine().trim();
            if (hoTen.isEmpty()) {
                System.out.println("[!] Khong duoc de trong!");
                continue;
            }
            if (hoTen.matches(".*\\d.*")) {
                System.out.println("[!] Ho ten khong duoc chua so!");
                continue;
            }
            // Không chứa ký tự đặc biệt lạ
            if (!hoTen.matches("[\\p{L}\\s]+")) {
                System.out.println("[!] Ho ten chi duoc chua chu va khoang trang!");
                continue;
            }
            break;
        }
        System.out.print("So dien thoai: ");
        String soDT = "";
        while (true) {
            soDT = sc.nextLine();
            if (soDT.matches("\\d{10}")) {
                break;
            } else {
                System.out.print("[!] So dien thoai khong hop le. Vui long nhap lai (10 chu so): ");
            }
        }
        String diaChi = "";
        while (true) {
            try {
                System.out.print("Dia chi: ");
                diaChi = sc.nextLine().trim();
                if (diaChi.isEmpty()) {
                    System.out.println("[!] Khong duoc de trong!");
                    continue;
                }
                if (diaChi.matches("\\d+")) {
                    System.out.println("[!] Dia chi khong duoc chi co so!");
                    continue;
                }
                // Không chứa ký tự đặc biệt lạ
                if (!diaChi.matches("[\\p{L}0-9\\s,\\.\\-]+")) {
                    System.out.println("[!] Dia chi chi duoc chua chu, so, dau phay, dau cham va dau gach ngang!");
                    continue;
                }
                // Không toàn là chữ (phải có ít nhất 1 số nhà)
                if (diaChi.matches("[\\p{L}\\s]+")) {
                    System.out.println("[!] Dia chi phai co so nha hoac chi tiet cu the (vi du: 12 Nguyen Van Cu)!");
                    continue;
                }
                if (!diaChi.matches(".*[\\p{L}0-9].*")) {
                    System.out.println("[!] Dia chi phai co chu hoac so hop le (khong duoc toan dau phay, dau cham, dau gach)!");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("[!] Loi nhap dia chi " + e.getMessage());
            }
        }

        KhachHang kh = new KhachHang(maKH, 0, hoTen, soDT, diaChi);
        dsKhachHang.them(kh);
        khachHangHienTai = kh;

        System.out.println("\n[✓] Dang ky thanh cong!");
        System.out.println("Ma khach hang cua ban: " + maKH);
        System.out.println("Vui long ghi nho ma nay de mua hang lan sau!");
    }
    // ========== CÁC MENU QUẢN LÝ CHO NHÂN VIÊN ==========

    public static void menuQuanLySanPham() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║           QUAN LY SAN PHAM                        ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Hien thi tat ca san pham                      ║");
            System.out.println("║  2. Them san pham moi                             ║");
            System.out.println("║  3. Tim kiem san pham                             ║");
            System.out.println("║  4. Sua san pham                                  ║");
            System.out.println("║  5. Xoa san pham                                  ║");
            System.out.println("║  6. Thong ke san pham                             ║");
            System.out.println("║  7. Kiem tra ton kho                              ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsSanPham.XuatDanhSach();
                    break;
                case 2:
                    dsSanPham.NhapDanhSach();
                    break;
                case 3:
                    System.out.print("Nhap tu khoa tim kiem: ");
                    String keyword = sc.nextLine();
                    dsSanPham.timKiemVaHienThi(keyword);
                    break;
                case 4:
                    System.out.print("Nhap ma san pham can sua: ");
                    String maSua = sc.nextLine();
                    dsSanPham.suaTheoMa(maSua);
                    break;
                case 5:
                    System.out.print("Nhap ma san pham can xoa: ");
                    String maXoa = sc.nextLine();
                    dsSanPham.xoaTheoMa(maXoa);
                    break;
                case 6:
                    dsSanPham.thongKe("");
                    break;
                case 7:
                    dsSanPham.kiemTraTonKho();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void menuQuanLyKhachHang() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║          QUAN LY KHACH HANG                       ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Hien thi tat ca khach hang                    ║");
            System.out.println("║  2. Them khach hang moi                           ║");
            System.out.println("║  3. Tim kiem khach hang                           ║");
            System.out.println("║  4. Sua thong tin khach hang                      ║");
            System.out.println("║  5. Xoa khach hang                                ║");
            System.out.println("║  6. Thong ke tich diem                            ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsKhachHang.XuatDanhSach();
                    break;
                case 2:
                    dsKhachHang.NhapDanhSach();
                    break;
                case 3:
                    System.out.print("Nhap tu khoa tim kiem: ");
                    String keyword = sc.nextLine();
                    dsKhachHang.timKiemVaHienThi(keyword);
                    break;
                case 4:
                    System.out.print("Nhap ma khach hang can sua: ");
                    String maSua = sc.nextLine();
                    dsKhachHang.suaTheoMa(maSua);
                    break;
                case 5:
                    System.out.print("Nhap ma khach hang can xoa: ");
                    String maXoa = sc.nextLine();
                    dsKhachHang.xoaTheoMa(maXoa);
                    break;
                case 6:
                    dsKhachHang.thongKeDiem();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void menuQuanLyNhanVien() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║           QUAN LY NHAN VIEN                       ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Hien thi tat ca nhan vien                     ║");
            System.out.println("║  2. Them nhan vien moi                            ║");
            System.out.println("║  3. Tim kiem nhan vien                            ║");
            System.out.println("║  4. Sua thong tin nhan vien                       ║");
            System.out.println("║  5. Xoa nhan vien                                 ║");
            System.out.println("║  6. Thong ke theo chuc vu                         ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsNhanVien.XuatDanhSach();
                    break;
                case 2:
                    dsNhanVien.NhapDanhSach();
                    break;
                case 3:
                    System.out.print("Nhap tu khoa tim kiem: ");
                    String keyword = sc.nextLine();
                    dsNhanVien.timKiemVaHienThi(keyword);
                    break;
                case 4:
                    System.out.print("Nhap ma nhan vien can sua: ");
                    String maSua = sc.nextLine();
                    dsNhanVien.suaTheoMa(maSua);
                    break;
                case 5:
                    System.out.print("Nhap ma nhan vien can xoa: ");
                    String maXoa = sc.nextLine();
                    dsNhanVien.xoaTheoMa(maXoa);
                    break;
                case 6:
                    dsNhanVien.thongKeChucVu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void menuQuanLyHoaDon() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║            QUAN LY HOA DON                        ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Hien thi tat ca hoa don                       ║");
            System.out.println("║  2. Tao hoa don moi                               ║");
            System.out.println("║  3. Tim kiem hoa don                              ║");
            System.out.println("║  4. Xoa hoa don                                   ║");
            System.out.println("║  5. Thong ke theo ngay                            ║");
            System.out.println("║  6. Sua hoa don                                   ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsHoaDon.XuatDanhSach();
                    break;
                case 2:
                    dsHoaDon.setDsspContext(dsSanPham);
                    dsHoaDon.NhapDanhSach(nhanVienDangNhap, dsSanPham);
                    break;
                case 3:
                    System.out.print("Nhap tu khoa tim kiem: ");
                    String keyword = sc.nextLine();
                    dsHoaDon.timKiemVaHienThi(keyword);
                    break;
                case 4:
                    System.out.print("Nhap ma hoa don can xoa: ");
                    String maXoa = sc.nextLine();
                    dsHoaDon.xoaTheoMa(maXoa);
                    break;
                case 5:
                    System.out.print("Nhap ngay can thong ke (dd/mm/yyyy): ");
                    String ngay = sc.nextLine();
                    dsHoaDon.thongKeNgay(ngay);
                    break;
                case 6:
                    System.out.print("Nhap ma hoa don can sua: ");
                    String maSua = sc.nextLine().trim();
                    dsHoaDon.suaTheoMa(maSua);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void menuQuanLyBaoCao() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║          THONG KE VA BAO CAO                      ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Thong ke san pham                             ║");
            System.out.println("║  2. Thong ke khach hang (Tich diem)               ║");
            System.out.println("║  3. Thong ke nhan vien (Chuc vu)                  ║");
            System.out.println("║  4. Thong ke doanh thu thang                      ║");
            System.out.println("║  5. Thong ke chi phi nhap hang thang              ║");
            System.out.println("║  6. Tinh loi nhuan thang                          ║");
            System.out.println("║  7. Bao cao tong hop                              ║");
            System.out.println("║  8. Bao cao chi tiet phieu nhap                   ║");
            System.out.println("║  9. Thong ke theo nha cung cap                    ║");
            System.out.println("║ 10. Top nha cung cap nhap nhieu nhat              ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsSanPham.thongKe("");
                    break;
                case 2:
                    dsKhachHang.thongKeDiem();
                    break;
                case 3:
                    dsNhanVien.thongKeChucVu();
                    break;
                case 4:
                    dsHoaDon.TinhDoanhThuThang();
                    break;
                case 5:
                    System.out.print("Nhap thang (1-12): ");
                    int thang = 0;
                    while (true) {
                        thang = sc.nextInt();
                        if (thang >= 1 && thang <= 12) {
                            break;
                        } else {
                            System.out.print("[!] Thang khong hop le. Vui long nhap lai (1-12): ");
                        }
                    }
                    System.out.print("Nhap nam: ");
                    int nam = 0;
                    while (true) {
                        nam = sc.nextInt();
                        if (nam >= 2000 && nam <= currentYear) {
                            break;
                        } else {
                            System.out.print("[!] Nam khong hop le. Vui long nhap lai (>=2000): ");
                        }
                    }
                    sc.nextLine();
                    double chiPhi = dsPhieuNhap.tinhChiPhiThang(thang, nam);
                    System.out.println("Chi phi nhap hang thang " + thang + "/" + nam + ": "
                            + String.format("%.2f", chiPhi) + " VND");
                    break;
                case 6:
                    System.out.print("Nhap thang (1-12): ");
                    int thangLN = 0;
                    while (true) {
                        thangLN = sc.nextInt();
                        if (thangLN >= 1 && thangLN <= 12) {
                            break;
                        } else {
                            System.out.print("[!] Thang khong hop le. Vui long nhap lai (1-12): ");
                        }
                    }
                    System.out.print("Nhap nam: ");
                    int namLN = 0;
                    while (true) {
                        namLN = sc.nextInt();
                        if (namLN >= 2000 && namLN <= currentYear) {
                            break;
                        } else {
                            System.out.print("[!] Nam khong hop le. Vui long nhap lai (>=2000): ");
                        }
                    }
                    sc.nextLine();
                    baoCaoThongKe.tinhLoiNhuanThang(thangLN, namLN);
                    break;
                case 7:
                    baoCaoThongKe.hienThiBaoCaoTongHop();
                    break;
                case 8:
                    baoCaoThongKe.hienThiBaoCaoChiTietPhieuNhap();
                    break;
                case 9:
                    System.out.print("Nhap ma nha cung cap: ");
                    String maNCC = sc.nextLine();
                    baoCaoThongKe.thongKeTheoNhaCungCap(maNCC);
                    break;
                case 10:
                    System.out.print("Nhap so luong top (VD: 5): ");
                    int top = 0;
                    while (true) {
                        top = sc.nextInt();
                        if (top > 0) {
                            break;
                        } else {
                            System.out.print("[!] So luong phai lon hon 0. Vui long nhap lai: ");
                        }
                    }
                    sc.nextLine();
                    baoCaoThongKe.topNhaCungCapNhapNhieuNhat(top);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void menuQuanLyNhaCungCap() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║          QUAN LY NHA CUNG CAP                     ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Hien thi tat ca nha cung cap                  ║");
            System.out.println("║  2. Them nha cung cap moi                         ║");
            System.out.println("║  3. Tim kiem nha cung cap                         ║");
            System.out.println("║  4. Sua thong tin nha cung cap                    ║");
            System.out.println("║  5. Xoa nha cung cap                              ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsNhaCungCap.XuatDanhSach();
                    break;
                case 2:
                    dsNhaCungCap.NhapDanhSach();
                    break;
                case 3:
                    System.out.print("Nhap tu khoa tim kiem: ");
                    String keyword = sc.nextLine();
                    dsNhaCungCap.timKiemVaHienThi(keyword);
                    break;
                case 4:
                    System.out.print("Nhap ma nha cung cap can sua: ");
                    String maSua = sc.nextLine();
                    dsNhaCungCap.suaTheoMa(maSua);
                    break;
                case 5:
                    System.out.print("Nhap ma nha cung cap can xoa: ");
                    String maXoa = sc.nextLine();
                    dsNhaCungCap.xoa(maXoa);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void menuQuanLyPhieuNhap() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║            QUAN LY PHIEU NHAP                     ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Hien thi tat ca phieu nhap                    ║");
            System.out.println("║  2. Tao phieu nhap moi                            ║");
            System.out.println("║  3. Tim kiem phieu nhap                           ║");
            System.out.println("║  4. Xoa phieu nhap                                ║");
            System.out.println("║  5. Thong ke theo ngay                            ║");
            System.out.println("║  6. Thong ke theo nha cung cap                    ║");
            System.out.println("║  7. Xem danh sach lo hang                         ║");
            System.out.println("║  8. Tim lo hang theo ma / san pham                ║");
            System.out.println("║  0. Quay lai                                      ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.print("Chon chuc nang: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dsPhieuNhap.XuatDanhSach();
                    break;
                case 2:
                    taoPhieuNhapMoi();
                    break;
                case 3:
                    System.out.print("Nhap tu khoa tim kiem: ");
                    String keyword = sc.nextLine();
                    dsPhieuNhap.timKiemVaHienThi(keyword);
                    break;
                case 4:
                    System.out.print("Nhap ma phieu nhap can xoa: ");
                    String maXoa = sc.nextLine();
                    dsPhieuNhap.xoa(maXoa);
                    baoCaoThongKe = new BaoCaoThongKe(); // Reset báo cáo
                    dongBoDuLieuBaoCao();
                    break;
                case 5:
                    System.out.print("Nhap ngay can thong ke (dd/mm/yyyy): ");
                    String ngay = "";
                    while (true) {
                        ngay = sc.nextLine();
                        if (ngay.matches("\\d{2}/\\d{2}/\\d{4}")) {
                            break;
                        } else {
                            System.out.print("[!] Ngay khong hop le. Vui long nhap lai (dd/mm/yyyy): ");
                        }
                    }
                    dsPhieuNhap.thongKeNgay(ngay);
                    break;
                case 6:
                    System.out.print("Nhap ma nha cung cap: ");
                    String maNCC = sc.nextLine();
                    dsPhieuNhap.thongKeTheoNCC(maNCC);
                    break;
                case 7:
                    xemDanhSachLoHang();
                    break;
                case 8:
                    System.out.println("1. Tim theo ma lo  2. Tim theo ma san pham");
                    int sub = 0;
                    try {
                        sub = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        sub = 0;
                    }
                    if (sub == 1) {
                        xemLoHangTheoMa();
                    } else if (sub == 2) {
                        xemLoHangTheoSanPham();
                    } else {
                        System.out.println("[!] Lua chon khong hop le.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("[!] Lua chon khong hop le!");
            }
        }
    }

    public static void taoPhieuNhapMoi() {
        System.out.println("\n========== TAO PHIEU NHAP MOI ==========");

        String maPN = "PN" + String.format("%03d", dsPhieuNhap.getSoLuong() + 1);
        System.out.println("Ma phieu nhap: " + maPN);

        System.out.print("Ngay nhap (dd/mm/yyyy): ");
        String ngay = "";
        while (true) {
            ngay = sc.nextLine();
            if (ngay.matches("\\d{2}/\\d{2}/\\d{4}")) {
                break;
            } else {
                System.out.print("[!] Ngay khong hop le. Vui long nhap lai (dd/mm/yyyy): ");
            }
        }

        // Chọn nhà cung cấp
        System.out.println("\nDanh sach nha cung cap:");
        dsNhaCungCap.XuatDanhSach();
        System.out.print("Nhap ma nha cung cap: ");
        String maNCC = "";
        NhaCungCap ncc = null;
        while (true) {
            maNCC = sc.nextLine();
            ncc = dsNhaCungCap.timKiem(maNCC);
            if (ncc == null) {
                System.out.println("[X] Khong tim thay nha cung cap!");
                System.out.print("Vui long nhap lai ma nha cung cap: ");
            } else {
                break;
            }
        }

        System.out.print("Ghi chu (neu co): ");
        String ghiChu = sc.nextLine();

        PhieuNhap pn = new PhieuNhap(maPN, ngay, 0, maNCC, ncc, nhanVienDangNhap, ghiChu);

        // Thêm chi tiết phiếu nhập
        System.out.println("\n--- Them san pham vao phieu nhap ---");
        while (true) {
            System.out.print("Nhap ma san pham (0 de ket thuc): ");
            String maSP = sc.nextLine();
            if (maSP.equals("0")) {
                break;
            }

            SanPham sp = dsSanPham.timKiem(maSP);
            if (sp == null) {
                System.out.println("[X] Khong tim thay san pham!");
                continue;
            }

            System.out.println("San pham: " + sp.getTenSP());

            System.out.print("So luong nhap: ");
            int soLuong = sc.nextInt();

            System.out.print("Don gia nhap: ");
            double donGia = sc.nextDouble();
            sc.nextLine();

            System.out.print("Ngay san xuat (dd/mm/yyyy): ");
            String ngaySX = "";
            while (true) {
                ngaySX = sc.nextLine();
                if (ngaySX.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    break;
                } else {
                    System.out.print("[!] Ngay khong hop le. Vui long nhap lai (dd/mm/yyyy): ");
                }
            }
            System.out.print("Han su dung (mm/yyyy): ");
            String hanSD = "";
            while (true) {
                hanSD = sc.nextLine();
                if (hanSD.matches("\\d{2}/\\d{4}")) {
                    break;
                } else {
                    System.out.print("[!] Han su dung khong hop le. Vui long nhap lai (mm/yyyy): ");
                }
            }

            // --- thêm tùy chọn ghi chú cho lô hàng (có thể để trống) ---
            System.out.print("Ghi chu lo hang (neu co, ENTER de bo qua): ");
            String ghiChuLo = sc.nextLine().trim();

            // để tránh bắt lỗi trùng mã lô, ta để maLo rỗng để constructor tự sinh mã "LH"+timestamp
            String maLo = ""; // Nếu bạn muốn cho người dùng nhập mã lô, đổi dòng này thành: maLo = sc.nextLine().trim();

            ChiTietPhieuNhap ct = new ChiTietPhieuNhap(maPN, maSP, soLuong, donGia,
                    ngaySX, hanSD, sp, pn, maLo, ghiChuLo);
            pn.themChiTiet(ct);

            // Nếu LoHang vừa tạo chưa có trong danh sách lô, thêm vào dsLoHang
            if (ct.getLoHang() != null) {
                LoHang lh = ct.getLoHang();
                if (dsLoHang.timKiem(lh.getMaLoHang()) == null) {
                    dsLoHang.them(lh);
                }
            }

            // Cập nhật tồn kho theo lô vừa nhập
            ct.capNhatTonKho();

            System.out.println("[✓] Them san pham thanh cong!");
        }

        if (pn.getSoLuongChiTiet() == 0) {
            System.out.println("[!] Phieu nhap rong, huy tao!");
            return;
        }

        dsPhieuNhap.them(pn);
        baoCaoThongKe.themPhieuNhap(pn);

        System.out.println("\n========== PHIEU NHAP ==========");
        pn.hienThiPhieuNhap();
        System.out.println("[✓] Tao phieu nhap thanh cong!");
    }

    public static void xemDanhSachLoHang() {
        dsLoHang.XuatDanhSach();
    }

    public static void xemLoHangTheoMa() {
        System.out.print("\nNhap ma lo hang: ");
        String maLo = sc.nextLine().trim();
        LoHang lh = dsLoHang.timKiem(maLo);
        if (lh == null) {
            System.out.println("[!] Khong tim thay lo hang: " + maLo);
        } else {
            System.out.println("\n--- Chi tiet lo hang ---");
            System.out.println("Ma lo: " + lh.getMaLoHang());
            System.out.println("Ma SP: " + lh.getMaSP());
            System.out.println("Ma PN: " + lh.getMaPN());
            System.out.println("So luong: " + lh.getSoLuong());
            System.out.println("Don gia: " + lh.getDonGia());
            System.out.println("NSX: " + lh.getNgaySanXuat());
            System.out.println("HSD: " + lh.getHanSuDung());
            System.out.println("Ghi chu: " + (lh.getGhiChu() == null ? "" : lh.getGhiChu()));
        }
    }

    public static void xemLoHangTheoSanPham() {
        System.out.print("\nNhap ma san pham: ");
        String maSP = sc.nextLine().trim();
        boolean found = false;
        System.out.println("\n--- Lo hang cua san pham: " + maSP + " ---");
        for (int i = 0; i < dsLoHang.getSoLuong(); i++) {
            LoHang lh = dsLoHang.getLoHangs()[i];
            if (lh != null && lh.getMaSP() != null && lh.getMaSP().equalsIgnoreCase(maSP)) {
                found = true;
                System.out.println("Ma lo: " + lh.getMaLoHang()
                        + " | So luong: " + lh.getSoLuong()
                        + " | NSX: " + lh.getNgaySanXuat()
                        + " | HSD: " + lh.getHanSuDung()
                        + " | Ghi chu: " + (lh.getGhiChu() == null ? "" : lh.getGhiChu()));
            }
        }
        if (!found) {
            System.out.println("[!] Khong tim thay lo hang cho san pham: " + maSP);
        }
    }

    public static void dongBoDuLieuBaoCao() {
        for (int i = 0; i < dsHoaDon.getSoLuong(); i++) {
            baoCaoThongKe.themHoaDon(dsHoaDon.getHoaDons()[i]);
        }
        for (int i = 0; i < dsPhieuNhap.getSoLuong(); i++) {
            baoCaoThongKe.themPhieuNhap(dsPhieuNhap.getPhieuNhaps()[i]);
        }
    }
}
