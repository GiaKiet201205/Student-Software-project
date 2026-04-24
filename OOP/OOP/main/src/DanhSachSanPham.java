import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DanhSachSanPham implements INhapXuat, IQuanLy<SanPham> {
    private SanPham[] sanPhams;
    private int soLuong;
    private static final int MAX_SIZE = 1000;
    private static java.util.Scanner sc = new java.util.Scanner(System.in);

    public DanhSachSanPham() {
        sanPhams = new SanPham[MAX_SIZE];
        soLuong = 0;
    }

    public DanhSachSanPham(int kichThuoc) {
        sanPhams = new SanPham[kichThuoc];
        soLuong = 0;
    }

    // Getter
    public SanPham[] getSanPhams() {
        return sanPhams;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong > 0) {
            this.soLuong = soLuong;
        }
    }

    // Implement INhapXuat
    @Override
    public void NhapDanhSach() {
        System.out.print("Nhap so luong san pham: ");
        int n = sc.nextInt();
        sc.nextLine();
        nhapDanhSach(n);
    }

    @Override
    public void XuatDanhSach() {
        xuatDanhSach();
    }

    // Implement IQuanLy
    @Override
    public void them(SanPham sp) {
        themPhanTu(sp);
    }

    @Override
    public void xoa(String ma) {
        xoaTheoMa(ma);
    }

    @Override
    public SanPham timKiem(String ma) {
        return timKiemTheoMa(ma);
    }

    @Override
    public void hienThiDanhSach() {
        xuatDanhSach();
    }

    // Các phương thức theo UML
    public void nhapDanhSach(int n) {
        System.out.println("Nhap danh sach " + n + " san pham:");
        for (int i = 0; i < n && soLuong < sanPhams.length; i++) {
            try {
                System.out.println("\n--- San pham thu " + (i + 1) + " ---");

                // Nhập loại sản phẩm với ràng buộc 1-4
                int loai = -1;
                while (loai < 1 || loai > 4) {
                    System.out.print(
                            "Loai san pham (1-Thuoc, 2-ThucPhamChucNang, 3-DungCuYTe, 4-SanPhamChamSocSacDep): ");
                    if (sc.hasNextInt()) {
                        loai = sc.nextInt();
                        sc.nextLine();
                        if (loai < 1 || loai > 4) {
                            System.out.println("[!] Vui long chon so tu 1 den 4!");
                        }
                    } else {
                        System.out.println("[!] Vui long nhap so!");
                        sc.nextLine();
                    }
                }

                // Nhập mã sản phẩm với tiền tố tự động
                String maSP = "";
                String tieuTo = "";
                switch (loai) {
                    case 1:
                        tieuTo = "T";
                        break;
                    case 2:
                        tieuTo = "TPCN";
                        break;
                    case 3:
                        tieuTo = "DC";
                        break;
                    case 4:
                        tieuTo = "MP";
                        break;
                }

                boolean maSPHopLe = false;
                while (!maSPHopLe) {
                    System.out.print("Ma SP (se co san tien to '" + tieuTo + "'): ");
                    String maSPNhap = sc.nextLine().trim();

                    if (maSPNhap.isEmpty()) {
                        System.out.println("[!] Ma san pham khong duoc de trong!");
                        continue;
                    }
                    // neu ma co chua chu thi nhap lai
                    else if (maSPNhap.matches(".*[a-zA-Z]+.*")) {
                        System.out.println("[!] Ma san pham chi duoc chua so! Vui long nhap lai.");
                        continue;
                    }

                    maSP = tieuTo + maSPNhap;

                    // Kiểm tra mã không trùng với mã đã tồn tại
                    if (timKiemTheoMa(maSP) != null) {
                        System.out.println("[!] Ma san pham '" + maSP + "' da ton tai! Vui long nhap ma khac.");
                        continue;
                    }

                    maSPHopLe = true;
                }
                // Nhập tên sản phẩm không được bỏ trống
                boolean tenSPHopLe = false;
                String tenSP = "";
                while (!tenSPHopLe) {
                    System.out.print("Ten SP: ");
                    tenSP = sc.nextLine().trim();
                    if (tenSP.isEmpty()) {
                        System.out.println("[!] Ten san pham khong duoc de trong!");
                    } else {
                        tenSPHopLe = true;
                    }
                }

                // Nhập đơn vị tính không được bỏ trống
                boolean donViTinhHopLe = false;
                String donViTinh = "";
                while (!donViTinhHopLe) {
                    System.out.print("Don vi tinh: ");
                    donViTinh = sc.nextLine().trim();
                    if (donViTinh.isEmpty()) {
                        System.out.println("[!] Don vi tinh khong duoc de trong!");
                    } else {
                        donViTinhHopLe = true;
                    }
                }

                // Nhập giá nhập không được bỏ trống và > 0
                Double giaNhap = -1.0;
                while (giaNhap <= 0) {
                    System.out.print("Gia nhap: ");
                    String giaNhapStr = sc.nextLine().trim();

                    if (giaNhapStr.isEmpty()) {
                        System.out.println("[!] Gia nhap khong duoc de trong!");
                        continue;
                    }

                    try {
                        giaNhap = Double.parseDouble(giaNhapStr);
                        if (giaNhap <= 0) {
                            System.out.println("[!] Gia nhap phai lon hon 0!");
                            giaNhap = -1.0;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Vui long nhap so!");
                        giaNhap = -1.0;
                    }
                }

                // Nhập giá bán lẻ không được bỏ trống, > 0 và > giá nhập
                Double giaBanLe = -1.0;
                while (giaBanLe <= giaNhap) {
                    System.out.print("Gia ban le (phai lon hon " + giaNhap + "): ");
                    String giaBanLeStr = sc.nextLine().trim();

                    if (giaBanLeStr.isEmpty()) {
                        System.out.println("[!] Gia ban le khong duoc de trong!");
                        continue;
                    }

                    try {
                        giaBanLe = Double.parseDouble(giaBanLeStr);
                        if (giaBanLe <= giaNhap) {
                            System.out.println("[!] Gia ban le phai lon hon gia nhap (" + giaNhap + ")!");
                            giaBanLe = -1.0;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Vui long nhap so!");
                        giaBanLe = -1.0;
                    }
                }
                // Nhập số lượng tồn không được bỏ trống và > 0
                int soLuongTon = -1;
                while (soLuongTon <= 0) {
                    System.out.print("So luong ton: ");
                    String soLuongTonStr = sc.nextLine().trim();

                    if (soLuongTonStr.isEmpty()) {
                        System.out.println("[!] So luong ton khong duoc de trong!");
                        continue;
                    }

                    try {
                        soLuongTon = Integer.parseInt(soLuongTonStr);
                        if (soLuongTon <= 0) {
                            System.out.println("[!] So luong ton phai lon hon 0!");
                            soLuongTon = -1;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Vui long nhap so!");
                        soLuongTon = -1;
                    }
                }

                System.out.print("Nha san xuat: ");
                String nhaSanXuat = sc.nextLine();

                SanPham sp = null;
                switch (loai) {
                    case 1: // Thuoc
                        System.out.print("Hoat chat: ");
                        String hoatChat = sc.nextLine();
                        System.out.print("Dang bao che: ");
                        String dangBaoChe = sc.nextLine();

                        // Nhập hạn sử dụng với ràng buộc > ngày hiện tại
                        String hanSuDung = "";
                        boolean hanSuDungHopLe = false;
                        while (!hanSuDungHopLe) {
                            System.out.print("Han su dung (MM/YYYY): ");
                            hanSuDung = sc.nextLine();
                            if (kiemTraHanSuDung(hanSuDung)) {
                                hanSuDungHopLe = true;
                            } else {
                                System.out.println(
                                        "[!] Vui long nhap dung dinh dang MM/YYYY va phai lon hon ngay hien tai!");
                            }
                        }

                        sp = new Thuoc(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, 0, hoatChat,
                                dangBaoChe, hanSuDung);
                        break;

                    case 2: // ThucPhamChucNang
                        System.out.print("Thanh phan: ");
                        String thanhPhan = sc.nextLine();
                        System.out.print("Cong dung: ");
                        String congDung = sc.nextLine();
                        System.out.print("Hinh thuc bao quan: ");
                        String hinhThucBaoQuan = sc.nextLine();

                        // Nhập hạn sử dụng với ràng buộc > ngày hiện tại
                        String hanSD = "";
                        boolean hanSDHopLe = false;
                        while (!hanSDHopLe) {
                            System.out.print("Han su dung (MM/YYYY): ");
                            hanSD = sc.nextLine();
                            if (kiemTraHanSuDung(hanSD)) {
                                hanSDHopLe = true;
                            } else {
                                System.out.println(
                                        "[!] Vui long nhap dung dinh dang MM/YYYY va phai lon hon ngay hien tai!");
                            }
                        }

                        sp = new ThucPhamChucNang(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, 0,
                                thanhPhan, congDung, hinhThucBaoQuan, hanSD);
                        break;

                    case 3: // DungCuYTe
                        System.out.print("Loai dung cu: ");
                        String loaiDungCu = sc.nextLine();
                        System.out.print("Vat lieu: ");
                        String vatLieu = sc.nextLine();
                        System.out.print("Han bao hanh: ");
                        String hanBaoHanh = sc.nextLine();

                        sp = new DungCuYTe(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, 0,
                                loaiDungCu, vatLieu, hanBaoHanh);
                        break;

                    case 4: // SanPhamChamSocSacDep
                        System.out.print("Cong dung: ");
                        String cd = sc.nextLine();
                        System.out.print("Loai da phu hop: ");
                        String loaiDa = sc.nextLine();

                        // Nhập hạn sử dụng với ràng buộc > ngày hiện tại
                        String hsd = "";
                        boolean hsdHopLe = false;
                        while (!hsdHopLe) {
                            System.out.print("Han su dung (MM/YYYY): ");
                            hsd = sc.nextLine();
                            if (kiemTraHanSuDung(hsd)) {
                                hsdHopLe = true;
                            } else {
                                System.out.println(
                                        "[!] Vui long nhap dung dinh dang MM/YYYY va phai lon hon ngay hien tai!");
                            }
                        }

                        sp = new SanPhamChamSocSacDep(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat,
                                0, cd, loaiDa, hsd);
                        break;

                    default:
                        System.out.println("Loai san pham khong hop le. Vui long chon lai.");
                        i--;
                        continue;
                }

                if (sp != null) {
                    themPhanTu(sp);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Loi: " + e.getMessage());
                System.out.println("Vui long nhap lai thong tin san pham.");
                i--;
            }
        }
    }

    // Phương thức kiểm tra hạn sử dụng
    private boolean kiemTraHanSuDung(String hanSuDung) {
        if (hanSuDung == null || hanSuDung.trim().isEmpty()) {
            return false;
        }

        try {
            String[] parts = hanSuDung.split("/");
            if (parts.length != 2) {
                return false;
            }

            int thang = Integer.parseInt(parts[0].trim());
            int nam = Integer.parseInt(parts[1].trim());

            // Kiểm tra tháng hợp lệ
            if (thang < 1 || thang > 12) {
                return false;
            }

            // Kiểm tra năm hợp lệ
            if (nam < 1000 || nam > 9999) {
                return false;
            }

            // Lấy ngày hiện tại
            LocalDate hienTai = LocalDate.now();
            int tháng_hiện_tại = hienTai.getMonthValue();
            int năm_hiện_tại = hienTai.getYear();

            // Kiểm tra hạn sử dụng > ngày hiện tại
            if (nam > năm_hiện_tại) {
                return true;
            } else if (nam == năm_hiện_tại && thang > tháng_hiện_tại) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach san pham rong!");
            return;
        }

        System.out.println(
                "\n======================================================================================================================================================================");
        System.out.printf("| %-8s | %-30s | %-10s | %-12s | %-10s | %-15s | %-70s |\n",
                "Ma SP", "Ten SP", "Don vi tinh", "Gia ban le", "So luong ton", "Nha san xuat", "Chi tiet");
        System.out.println(
                "======================================================================================================================================================================");

        for (int i = 0; i < soLuong; i++) {
            SanPham sp = sanPhams[i];
            String chiTiet = "";

            if (sp instanceof Thuoc) {
                Thuoc thuoc = (Thuoc) sp;
                chiTiet = String.format("Hoat chat: %s, Dang bao che: %s, HSD: %s", thuoc.getHoatChat(),
                        thuoc.getDangBaoChe(), thuoc.getHanSuDung());
            } else if (sp instanceof ThucPhamChucNang) {
                ThucPhamChucNang tpcn = (ThucPhamChucNang) sp;
                chiTiet = String.format("Thanh phan: %s, Cong dung: %s, HSD: %s", tpcn.getThanhPhan(),
                        tpcn.getCongDung(), tpcn.getHanSuDung());
            } else if (sp instanceof DungCuYTe) {
                DungCuYTe dcyt = (DungCuYTe) sp;
                chiTiet = String.format("Loai: %s, Vat lieu: %s, HBH: %s", dcyt.getLoaiDungCu(), dcyt.getVatLieu(),
                        dcyt.getHanBaoHanh());
            } else if (sp instanceof SanPhamChamSocSacDep) {
                SanPhamChamSocSacDep spcsd = (SanPhamChamSocSacDep) sp;
                chiTiet = String.format("Cong dung: %s, Loai da: %s, HSD: %s", spcsd.getCongDung(),
                        spcsd.getLoaiDaPhuHop(), spcsd.getHanSuDung());
            }

            System.out.printf("| %-8s | %-30s | %-10s | %-12.0f | %-10d | %-15s | %-70s |\n",
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getDonViTinh(),
                    sp.getGiaBanLe(),
                    sp.getSoLuongTon(),
                    sp.getNhaSanXuat(),
                    chiTiet);
        }
        System.out.println(
                "======================================================================================================================================================================");
    }

    public void themPhanTu(SanPham sp) {
        if (soLuong >= sanPhams.length) {
            System.out.println("Danh sach da day!");
            return;
        }
        sanPhams[soLuong] = sp;
        soLuong++;
        System.out.println("Them san pham thanh cong!");
    }

    public void suaTheoMa(String ma) {
        SanPham sp = timKiemTheoMa(ma);
        if (sp == null) {
            System.out.println("Khong tim thay san pham co ma: " + ma);
            return;
        }

        try {
            System.out.println("Nhap thong tin moi (Enter de giu nguyen):");
            System.out.print("Ten SP [" + sp.getTenSP() + "]: ");
            String ten = sc.nextLine();
            if (!ten.isEmpty())
                sp.setTenSP(ten);

            System.out.print("Don vi tinh [" + sp.getDonViTinh() + "]: ");
            String dvt = sc.nextLine();
            if (!dvt.isEmpty())
                sp.setDonViTinh(dvt);

            System.out.print("Gia nhap [" + sp.getGiaNhap() + "]: ");
            String gn = sc.nextLine();
            if (!gn.isEmpty())
                sp.setGiaNhap(Double.parseDouble(gn));

            // === BẮT ĐẦU SỬA ĐỔI (YÊU CẦU 2) ===
            System.out.print("Gia ban le [" + sp.getGiaBanLe() + "]: ");
            String gbl = sc.nextLine();
            if (!gbl.isEmpty())
                sp.setGiaBanLe(Double.parseDouble(gbl)); // Setter sẽ tự động kiểm tra > GiaNhap (đã sửa)
            // === KẾT THÚC SỬA ĐỔI (YÊU CẦU 2) ===

            System.out.print("So luong ton [" + sp.getSoLuongTon() + "]: ");
            String slt = sc.nextLine();
            if (!slt.isEmpty())
                sp.setSoLuongTon(Integer.parseInt(slt));

            System.out.println("Cap nhat thanh cong!");
        } catch (IllegalArgumentException e) { 
            System.out.println("Loi cap nhat: " + e.getMessage());
        }
    }

    public void xoaTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (sanPhams[i].getMaSP().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuong - 1; j++) {
                    sanPhams[j] = sanPhams[j + 1];
                }
                sanPhams[soLuong - 1] = null;
                soLuong--;
                System.out.println("Xoa san pham thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay san pham co ma: " + ma);
    }

    public SanPham timKiemTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (sanPhams[i].getMaSP().equalsIgnoreCase(ma)) {
                return sanPhams[i];
            }
        }
        return null;
    }

    // === BẮT ĐẦU SỬA ĐỔI (YÊU CẦU 1) ===
    public void timKiemVaHienThi(String key) {
        boolean found = false;
        System.out.println("\n========== KET QUA TIM KIEM ==========");
        for (int i = 0; i < soLuong; i++) {
            SanPham sp = sanPhams[i]; // Lấy sản phẩm
            if (sp.getMaSP().toLowerCase().contains(key.toLowerCase()) ||
                    sp.getTenSP().toLowerCase().contains(key.toLowerCase()) ||
                    sp.getNhaSanXuat().toLowerCase().contains(key.toLowerCase())) {

                // Logic hiển thị chi tiết (tương tự Main.java)
                String chiTiet = "";
                if (sp instanceof Thuoc) {
                    Thuoc t = (Thuoc) sp;
                    chiTiet = String.format("Hoat chat: %s, Dang bao che: %s, HSD: %s", t.getHoatChat(),
                            t.getDangBaoChe(), t.getHanSuDung());
                } else if (sp instanceof ThucPhamChucNang) {
                    ThucPhamChucNang tpcn = (ThucPhamChucNang) sp;
                    chiTiet = String.format("Thanh phan: %s, Cong dung: %s, Hinh thuc bao quan: %s, HSD: %s",
                            tpcn.getThanhPhan(), tpcn.getCongDung(), tpcn.getHinhThucBaoQuan(), tpcn.getHanSuDung());
                } else if (sp instanceof DungCuYTe) {
                    DungCuYTe dc = (DungCuYTe) sp;
                    chiTiet = String.format("Loai: %s, Vat lieu: %s, HBH: %s", dc.getLoaiDungCu(), dc.getVatLieu(),
                            dc.getHanBaoHanh());
                } else if (sp instanceof SanPhamChamSocSacDep) {
                    SanPhamChamSocSacDep mp = (SanPhamChamSocSacDep) sp;
                    chiTiet = String.format("Cong dung: %s, Loai da: %s, HSD: %s", mp.getCongDung(),
                            mp.getLoaiDaPhuHop(), mp.getHanSuDung());
                }

                // Hiển thị đầy đủ thông tin
                System.out.println("\n- " + sp.getTenSP());
                System.out.println("  Ma: " + sp.getMaSP());
                System.out.println("  Gia Nhap: " + String.format("%.0f", sp.getGiaNhap()) + " VND");
                System.out.println("  Gia Ban: " + String.format("%.0f", sp.tinhGiaBan()) + " VND");
                System.out.println("  Ton: " + sp.getSoLuongTon() + " " + sp.getDonViTinh());
                System.out.println("  NSX: " + sp.getNhaSanXuat());
                System.out.println("  Chi tiet: " + chiTiet);

                found = true;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay san pham nao!");
        }
        System.out.println("======================================");
    }
    // === KẾT THÚC SỬA ĐỔI (YÊU CẦU 1) ===

    public void thongKe(String key) {
        System.out.println("\n========== THONG KE SAN PHAM ==========");
        System.out.println("Tong so san pham: " + soLuong);

        int tongTonKho = 0;
        double tongGiaTri = 0;
        for (int i = 0; i < soLuong; i++) {
            tongTonKho += sanPhams[i].getSoLuongTon();
            tongGiaTri += sanPhams[i].getSoLuongTon() * sanPhams[i].getGiaNhap();
        }

        System.out.println("Tong so luong ton kho: " + tongTonKho);
        System.out.println("Tong gia tri hang ton: " + String.format("%.2f", tongGiaTri) + " VND");
        System.out.println("=======================================");
    }

    public void kiemTraTonKho() {
        System.out.println("\n========== KIEM TRA TON KHO ==========");
        System.out.print("Nhap nguong canh bao (mac dinh 10): ");
        int nguong = 10;
        while (true) {
            nguong = sc.nextInt();
            if (nguong < 0) {
                System.out.print("[!] Nguong phai la so duong. Vui long nhap lai: ");
            } else {
                break;
            }
        }

        boolean coSapHet = false;
        for (int i = 0; i < soLuong; i++) {
            if (sanPhams[i].getSoLuongTon() < nguong) {
                System.out.println("\n[CANH BAO] Ma SP: " + sanPhams[i].getMaSP());
                System.out.println("Ten SP: " + sanPhams[i].getTenSP());
                System.out.println("So luong ton: " + sanPhams[i].getSoLuongTon());
                coSapHet = true;
            }
        }

        if (!coSapHet) {
            System.out.println("Tat ca san pham deu con du hang!");
        }
        System.out.println("======================================");
    }

    public void docFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/sanpham.txt"))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < sanPhams.length) {
                try {
                    line = line.trim();
                    if (line.isEmpty())
                        continue;

                    String[] parts = line.split(",");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

                    if (parts.length < 8)
                        continue;

                    String maSP = parts[0];
                    String tenSP = parts[1];
                    String donViTinh = parts[2];
                    double giaNhap = Double.parseDouble(parts[3]);
                    double giaBanLe = Double.parseDouble(parts[4]);
                    int soLuongTon = Integer.parseInt(parts[5]);
                    String nhaSanXuat = parts[6];
                    int soLuongBan = Integer.parseInt(parts[7]);

                    SanPham sp = null;

                    if (maSP.startsWith("T") && !maSP.startsWith("TPCN")) {
                        if (parts.length >= 11) {
                            String hoatChat = parts[8];
                            String dangBaoChe = parts[9];
                            String hanSuDung = parts[10];
                            sp = new Thuoc(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat,
                                    soLuongBan, hoatChat, dangBaoChe, hanSuDung);
                        }
                    } else if (maSP.startsWith("TPCN")) {
                        if (parts.length >= 12) {
                            String thanhPhan = parts[8];
                            String congDung = parts[9];
                            String hinhThucBaoQuan = parts[10];
                            String hanSuDung = parts[11];
                            sp = new ThucPhamChucNang(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat,
                                    soLuongBan, thanhPhan, congDung, hinhThucBaoQuan, hanSuDung);
                        }
                    } else if (maSP.startsWith("DC")) {
                        if (parts.length >= 10) {
                            String loaiDungCu = parts[8];
                            String vatLieu = parts[9];
                            String hanBaoHanh = (parts.length >= 11) ? parts[10] : "";
                            sp = new DungCuYTe(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat,
                                    soLuongBan, loaiDungCu, vatLieu, hanBaoHanh);
                        }
                    } else if (maSP.startsWith("MP")) {
                        if (parts.length >= 11) {
                            String congDung = parts[8];
                            String loaiDa = parts[9];
                            String hanSuDung = parts[10];
                            sp = new SanPhamChamSocSacDep(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon,
                                    nhaSanXuat, soLuongBan, congDung, loaiDa, hanSuDung);
                        }
                    }

                    if (sp != null) {
                        sanPhams[soLuong++] = sp;
                    } else {
                        System.out
                                .println("Loi doc file: San pham co ma " + maSP + " co dinh dang du lieu khong dung.");
                    }
                } catch (Exception e) {
                    System.out.println("Loi doc file: Dong du lieu \"" + line + "\" co loi. " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Loi khong the mo file sanpham.txt: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/sanpham.txt"))) {
            for (int i = 0; i < soLuong; i++) {
                SanPham sp = sanPhams[i];

                if (sp instanceof Thuoc) {
                    Thuoc t = (Thuoc) sp;
                    pw.println(t.getMaSP() + ", " + t.getTenSP() + ", " + t.getDonViTinh() + ", " +
                            t.getGiaNhap() + ", " + t.getGiaBanLe() + ", " + t.getSoLuongTon() + ", " +
                            t.getNhaSanXuat() + ", 0, " + t.getHoatChat() + ", " + t.getDangBaoChe() + ", " +
                            t.getHanSuDung());
                } else if (sp instanceof ThucPhamChucNang) {
                    ThucPhamChucNang tpcn = (ThucPhamChucNang) sp;
                    pw.println(tpcn.getMaSP() + ", " + tpcn.getTenSP() + ", " + tpcn.getDonViTinh() + ", " +
                            tpcn.getGiaNhap() + ", " + tpcn.getGiaBanLe() + ", " + tpcn.getSoLuongTon() + ", " +
                            tpcn.getNhaSanXuat() + ", 0, " + tpcn.getThanhPhan() + ", " + tpcn.getCongDung() + ", " +
                            tpcn.getHinhThucBaoQuan() + ", " + tpcn.getHanSuDung());
                } else if (sp instanceof DungCuYTe) {
                    DungCuYTe dc = (DungCuYTe) sp;
                    pw.println(dc.getMaSP() + ", " + dc.getTenSP() + ", " + dc.getDonViTinh() + ", " +
                            dc.getGiaNhap() + ", " + dc.getGiaBanLe() + ", " + dc.getSoLuongTon() + ", " +
                            dc.getNhaSanXuat() + ", 0, " + dc.getLoaiDungCu() + ", " + dc.getVatLieu() + ", " +
                            (dc.getHanBaoHanh() != null ? dc.getHanBaoHanh() : ""));
                } else if (sp instanceof SanPhamChamSocSacDep) {
                    SanPhamChamSocSacDep mp = (SanPhamChamSocSacDep) sp;
                    pw.println(mp.getMaSP() + ", " + mp.getTenSP() + ", " + mp.getDonViTinh() + ", " +
                            mp.getGiaNhap() + ", " + mp.getGiaBanLe() + ", " + mp.getSoLuongTon() + ", " +
                            mp.getNhaSanXuat() + ", 0, " + mp.getCongDung() + ", " + mp.getLoaiDaPhuHop() + ", " +
                            mp.getHanSuDung());
                }
            }
            System.out.println("[+] Ghi file thanh cong!");
        } catch (

        IOException e) {
            System.out.println("[!] Loi ghi file: " + e.getMessage());
        }
    }
}