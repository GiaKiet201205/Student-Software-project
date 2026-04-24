import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DanhSachNhanVien implements INhapXuat, IQuanLy<NhanVien> {
    private NhanVien[] nhanViens;
    private int soLuong;
    private static final int MAX_SIZE = 200;

    public DanhSachNhanVien() {
        nhanViens = new NhanVien[MAX_SIZE];
        soLuong = 0;
    }

    public DanhSachNhanVien(int kichThuoc) {
        nhanViens = new NhanVien[kichThuoc];
        soLuong = 0;
    }

    // Getter
    public NhanVien[] getNhanViens() {
        return nhanViens;
    }

    public int getSoLuong() {
        return soLuong;
    }

    // Implement INhapXuat
    @Override
    public void NhapDanhSach() {
        System.out.print("Nhap so luong nhan vien: ");
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
    public void them(NhanVien nv) {
        themPhanTu(nv);
    }

    @Override
    public void xoa(String ma) {
        xoaTheoMa(ma);
    }

    @Override
    public NhanVien timKiem(String ma) {
        return timKiemTheoMa(ma);
    }

    @Override
    public void hienThiDanhSach() {
        xuatDanhSach();
    }

    // Các phương thức theo UML
    public void nhapDanhSach(int n) {
        System.out.println("Nhap danh sach " + n + " nhan vien:");
        for (int i = 0; i < n && soLuong < nhanViens.length; i++) {
            System.out.println("\n--- Nhan vien thu " + (i + 1) + " ---");
            // Nhập mã nhân viên không được bỏ trống và không trùng
            String maNV = "";
            boolean maNVHopLe = false;
            while (!maNVHopLe) {
                System.out.print("Ma NV (se co san tien to 'NV'): ");
                String maNVNhap = sc.nextLine().trim();

                if (maNVNhap.isEmpty()) {
                    System.out.println("[!] Ma nhan vien khong duoc de trong!");
                    continue;
                }

                maNV = "NV" + maNVNhap;

                // Kiểm tra mã không trùng với mã đã tồn tại
                if (timKiemTheoMa(maNV) != null) {
                    System.out.println("[!] Ma nhan vien '" + maNV + "' da ton tai! Vui long nhap ma khac.");
                    continue;
                }
                if (!maNV.matches("NV\\d+")) {
                    System.out.println("[!] Ma nhan vien phai bat dau bang 'NV' va theo sau la cac chu so!");
                    continue;
                }

                maNVHopLe = true;
            }

            // Nhập họ tên không được bỏ trống
            String hoTen = "";
            boolean hoTenHopLe = false;
            while (!hoTenHopLe) {
                System.out.print("Ho ten: ");
                hoTen = sc.nextLine().trim();
                if (hoTen.isEmpty()) {
                    System.out.println("[!] Ho ten khong duoc de trong!");
                } else if (hoTen.matches(".*\\d.*")) {
                    System.out.println("[!] Ho ten khong duoc chua so!");
                } else if (hoTen.length() < 2) {
                    System.out.println("[!] Ho ten phai co it nhat 2 ky tu!");
                } else if (hoTen.matches(".*[!@#$%^&*()_+=\\[\\]{}|;:'\",.<>?/\\\\].*")) {
                    System.out.println("[!] Ho ten khong duoc chua ky tu dac biet!");
                } else {
                    hoTenHopLe = true;
                }
            }

            // Nhập số điện thoại không được bỏ trống và phải là số
            String soDT = "";
            boolean soDTHopLe = false;
            while (!soDTHopLe) {
                System.out.print("So dien thoai: ");
                soDT = sc.nextLine().trim();
                if (soDT.isEmpty()) {
                    System.out.println("[!] So dien thoai khong duoc de trong!");
                } else if (!soDT.matches("\\d+")) {
                    System.out.println("[!] So dien thoai phai la con so!");
                } else if (soDT.length() < 10 || soDT.length() > 11) {
                    System.out.println("[!] So dien thoai phai co 10-11 chu so!");
                } else if (!soDT.startsWith("0")) {
                    System.out.println("[!] So dien thoai phai bat dau bang so 0!");
                } else {
                    soDTHopLe = true;
                }
            }

            // Nhập địa chỉ không được bỏ trống
            String diaChi = "";
            boolean diaChiHopLe = false;
            while (!diaChiHopLe) {
                System.out.print("Dia chi: ");
                diaChi = sc.nextLine().trim();
                if (diaChi.isEmpty()) {
                    System.out.println("[!] Dia chi khong duoc de trong!");
                } else if (diaChi.length() < 5) {
                    System.out.println("[!] Dia chi phai co it nhat 5 ky tu!");
                } else {
                    diaChiHopLe = true;
                }
            }
String ngaySinh = "";
            LocalDate ngaySinhDate = null;
            LocalDate today = LocalDate.now();
            LocalDate tuoiToiThieu = today.minusYears(18); 
            
            LocalDate tuoiToiDa = today.minusYears(80); 

            while (ngaySinhDate == null) {
                System.out.print("Ngay sinh (dd/mm/yyyy): ");
                ngaySinh = sc.nextLine().trim();

                if (ngaySinh.isEmpty()) {
                    System.out.println("[!] Ngay sinh khong duoc de trong!");
                    continue;
                }

                if (!ngaySinh.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    System.out.println("[!] Ngay sinh phai co dinh dang dd/mm/yyyy!");
                    continue;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    ngaySinhDate = LocalDate.parse(ngaySinh, formatter);

                    if (ngaySinhDate.isAfter(today)) {
                        System.out.println("[!] Ngay sinh khong duoc lon hon ngay hien tai!");
                        ngaySinhDate = null;
                        continue;
                    }

                    if (ngaySinhDate.isAfter(tuoiToiThieu)) {
                        System.out.println("[!] Nhan vien phai du 18 tuoi!");
                        ngaySinhDate = null;
                        continue;
                    }
                    if (ngaySinhDate.isBefore(tuoiToiDa)) {
                        System.out.println("[!] Ngay sinh khong hop le! (Nhan vien da qua 80 tuoi?)");
                        ngaySinhDate = null;
                        continue;
                    }

                } catch (DateTimeException e) {
                    System.out.println("[!] Ngay thang khong hop le! (Vi du: thang 2 khong co ngay 31)");
                    ngaySinhDate = null;
                }
            }

            String chucVu = "";
            int chucvu = -1;

            while (chucvu < 1 || chucvu > 4) {
                System.out.print("Chuc vu (1. Quan ly, 2. Nhan vien, 3. Duoc sy, 4. Khac): ");
                try {
                    chucvu = Integer.parseInt(sc.nextLine().trim());

                    switch (chucvu) {
                        case 1:
                            chucVu = "Quan ly";
                            break;
                        case 2:
                            chucVu = "Nhan vien";
                            break;
                        case 3:
                            chucVu = "Duoc sy";
                            break;
                        case 4:
                            chucVu = "Khac";
                            break;
                        default:
                            System.out.println("[!] Lua chon khong hop le! Vui long chon tu 1-4.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so tu 1-4!");
                }
            }

            System.out.println("Chuc vu da chon: " + chucVu);

            // Nhập lương cơ bản phải lớn hơn 0
            double luong = 0;
            boolean luongHopLe = false;
            while (!luongHopLe) {
                System.out.print("Luong co ban: ");
                String luongStr = sc.nextLine().trim();

                if (luongStr.isEmpty()) {
                    System.out.println("[!] Luong khong duoc de trong!");
                    continue;
                }

                try {
                    luong = Double.parseDouble(luongStr);
                    if (luong <= 0) {
                        System.out.println("[!] Luong co ban phai lon hon 0!");
                    } else if (luong < 1000000) {
                        System.out.println("[!] Luong phai lon hon hoac bang 1,000,000 VND!");
                    } else if (luong > 1000000000) {
                        System.out.println("[!] Luong qua cao! Vui long nhap lai (toi da 1 ty VND).");
                    } else {
                        luongHopLe = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so hop le cho luong co ban!");
                }
            }

            NhanVien nv = new NhanVien(maNV, ngaySinh, chucVu, luong, hoTen, soDT, diaChi);
            themPhanTu(nv);
        }
    }

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach nhan vien rong!");
            return;
        }

        System.out.println(
                "\n======================================================================================================================================================");
        System.out.printf("| %-8s | %-25s | %-12s | %-15s | %-15s | %-15s | %-40s |\n",
                "Ma NV", "Ho Ten", "Ngay Sinh", "Chuc Vu", "Luong Co Ban", "So Dien Thoai", "Dia Chi");
        System.out.println(
                "======================================================================================================================================================");

        for (int i = 0; i < soLuong; i++) {
            NhanVien nv = nhanViens[i];
            System.out.printf("| %-8s | %-25s | %-12s | %-15s | %-15.0f | %-15s | %-40s |\n",
                    nv.getMaNV(),
                    nv.getHoTen(),
                    nv.getNgaySinh(),
                    nv.getChucVu(),
                    nv.getLuongCoBan(),
                    nv.getSoDT(),
                    nv.getDiaChi());
        }
        System.out.println(
                "======================================================================================================================================================");
    }

    public void themPhanTu(NhanVien nv) {
        if (soLuong >= nhanViens.length) {
            System.out.println("Danh sach da day!");
            return;
        }
        nhanViens[soLuong] = nv;
        soLuong++;
        System.out.println("Them nhan vien thanh cong!");
    }

    public void suaTheoMa(String ma) {
        NhanVien nv = timKiemTheoMa(ma);
        if (nv == null) {
            System.out.println("Khong tim thay nhan vien co ma: " + ma);
            return;
        }

        System.out.println("Nhap thong tin moi (Enter de giu nguyen):");

        // Sửa họ tên
        System.out.print("Ho ten [" + nv.getHoTen() + "]: ");
        String ten = sc.nextLine().trim();
        if (!ten.isEmpty()) {
            if (ten.matches(".*\\d.*")) {
                System.out.println("[!] Ho ten khong duoc chua so! Giu nguyen gia tri cu.");
            } else if (ten.length() < 2) {
                System.out.println("[!] Ho ten phai co it nhat 2 ky tu! Giu nguyen gia tri cu.");
            } else {
                nv.setHoTen(ten);
            }
        }

        // Sửa số điện thoại
        System.out.print("So dien thoai [" + nv.getSoDT() + "]: ");
        String sdt = sc.nextLine().trim();
        if (!sdt.isEmpty()) {
            if (!sdt.matches("\\d+")) {
                System.out.println("[!] So dien thoai phai la con so! Giu nguyen gia tri cu.");
            } else if (sdt.length() < 10 || sdt.length() > 11) {
                System.out.println("[!] So dien thoai phai co 10-11 chu so! Giu nguyen gia tri cu.");
            } else if (!sdt.startsWith("0")) {
                System.out.println("[!] So dien thoai phai bat dau bang so 0! Giu nguyen gia tri cu.");
            } else {
                nv.setSoDT(sdt);
            }
        }

        // Sửa địa chỉ
        System.out.print("Dia chi [" + nv.getDiaChi() + "]: ");
        String dc = sc.nextLine().trim();
        if (!dc.isEmpty()) {
            if (dc.length() < 5) {
                System.out.println("[!] Dia chi phai co it nhat 5 ky tu! Giu nguyen gia tri cu.");
            } else {
                nv.setDiaChi(dc);
            }
        }

        System.out.print("Chuc vu [" + nv.getChucVu() + "]: ");
        System.out.println("Chon chuc vu moi (Enter de giu nguyen):\n1. Quan ly\n2. Nhan vien\n3. Duoc sy\n4. Khac");
        String chucVuInput = sc.nextLine().trim();

        if (!chucVuInput.isEmpty()) {
            try {
                int chucvu = Integer.parseInt(chucVuInput);
                switch (chucvu) {
                    case 1:
                        nv.setChucVu("Quan ly");
                        break;
                    case 2:
                        nv.setChucVu("Nhan vien");
                        break;
                    case 3:
                        nv.setChucVu("Duoc sy");
                        break;
                    case 4:
                        nv.setChucVu("Khac");
                        break;
                    default:
                        System.out.println("[!] Lua chon khong hop le! Giu nguyen gia tri cu.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Vui long nhap so tu 1-4! Giu nguyen gia tri cu.");
            }
        }

        System.out.print("Luong co ban [" + nv.getLuongCoBan() + "]: ");
        String luong = sc.nextLine().trim();
        if (!luong.isEmpty()) {
            try {
                double luongMoi = Double.parseDouble(luong);
                if (luongMoi <= 0) {
                    System.out.println("[!] Luong phai lon hon 0! Giu nguyen gia tri cu.");
                } else if (luongMoi < 1000000) {
                    System.out.println("[!] Luong phai lon hon hoac bang 1,000,000 VND! Giu nguyen gia tri cu.");
                } else if (luongMoi > 1000000000) {
                    System.out.println("[!] Luong qua cao (toi da 1 ty VND)! Giu nguyen gia tri cu.");
                } else {
                    nv.setLuongCoBan(luongMoi);
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Luong khong hop le! Giu nguyen gia tri cu.");
            }
        }

        System.out.println("Cap nhat thanh cong!");
    }

    public void xoaTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (nhanViens[i].getMaNV().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuong - 1; j++) {
                    nhanViens[j] = nhanViens[j + 1];
                }
                nhanViens[soLuong - 1] = null;
                soLuong--;
                System.out.println("Xoa nhan vien thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay nhan vien co ma: " + ma);
    }

    public NhanVien timKiemTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (nhanViens[i].getMaNV().equalsIgnoreCase(ma)) {
                return nhanViens[i];
            }
        }
        return null;
    }

    public void timKiemVaHienThi(String key) {
        boolean found = false;
        System.out.println("\n========== KET QUA TIM KIEM ==========");
        for (int i = 0; i < soLuong; i++) {
            if (nhanViens[i].getMaNV().toLowerCase().contains(key.toLowerCase()) ||
                    nhanViens[i].getHoTen().toLowerCase().contains(key.toLowerCase()) ||
                    nhanViens[i].getChucVu().toLowerCase().contains(key.toLowerCase()) ||
                    nhanViens[i].getSoDT().contains(key) ||
                    nhanViens[i].getDiaChi().toLowerCase().contains(key.toLowerCase())) {
                nhanViens[i].hienThiThongTin();
                System.out.println("---");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay nhan vien nao!");
        }
        System.out.println("======================================");
    }

    public void thongKeChucVu() {
        System.out.println("\n========== THONG KE THEO CHUC VU ==========");

        // Đếm số lượng từng chức vụ
        String[] caccv = new String[soLuong];
        int[] soLuongcv = new int[soLuong];
        int socv = 0;

        for (int i = 0; i < soLuong; i++) {
            String chucVu = nhanViens[i].getChucVu();
            boolean daTonTai = false;

            for (int j = 0; j < socv; j++) {
                if (caccv[j].equalsIgnoreCase(chucVu)) {
                    soLuongcv[j]++;
                    daTonTai = true;
                    break;
                }
            }

            if (!daTonTai) {
                caccv[socv] = chucVu;
                soLuongcv[socv] = 1;
                socv++;
            }
        }

        System.out.println("Phan bo nhan vien theo chuc vu:");
        for (int i = 0; i < socv; i++) {
            System.out.println("- " + caccv[i] + ": " + soLuongcv[i] + " nhan vien");
        }

        // Tính lương trung bình
        double tongLuong = 0;
        for (int i = 0; i < soLuong; i++) {
            tongLuong += nhanViens[i].getLuongCoBan();
        }
        System.out.println("\nTong so nhan vien: " + soLuong);
        if (soLuong > 0) {
            System.out.println("Luong trung binh: " + String.format("%.2f", tongLuong / soLuong) + " VND");
        }
        System.out.println("===========================================");
    }

    public void docFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/nhanvien.txt"))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < nhanViens.length) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",\\s*");

                // Format có 2 kiểu:
                // 1. Có tài khoản: NV001, 15/03/1990, Quan ly, 1.5E7, Nguyen Van Khanh,
                // 0981234567, 111 Ly Tu Trong, admin, admin123
                // 2. Không có: NV004, 05/05/1992, Duoc sy, 1.1E7, Pham Thi Nga, 0984567890, 444
                // Le Van Sy
                if (parts.length >= 7 && parts[0].startsWith("NV")) {
                    String maNV = parts[0].trim();
                    String ngaySinh = parts[1].trim();
                    String cv = parts[2].trim();
                    double luong = Double.parseDouble(parts[3].trim());
                    String hoTen = parts[4].trim();
                    String soDT = parts[5].trim();
                    String diaChi = parts[6].trim();

                    NhanVien nv = new NhanVien(maNV, ngaySinh, cv, luong, hoTen, soDT, diaChi);

                    // Kiểm tra có username/password không (nếu có 9 phần)
                    if (parts.length >= 9) {
                        String username = parts[7].trim();
                        String password = parts[8].trim();
                        TaiKhoan tk = new TaiKhoan(username, password, cv);
                        nv.setTaiKhoan(tk);
                    }

                    nhanViens[soLuong] = nv;
                    soLuong++;
                }
            }
            System.out.println("[+] Doc file nhan vien thanh cong! So luong: " + soLuong);
        } catch (IOException e) {
            System.out.println("[!] Loi doc file nhan vien: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[!] Loi dinh dang so trong file nhan vien: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/nhanvien.txt"))) {
            // Ghi thông tin nhân viên (có hoặc không có tài khoản trong cùng dòng)
            for (int i = 0; i < soLuong; i++) {
                NhanVien nv = nhanViens[i];
                pw.print(nv.getMaNV() + ", " + nv.getNgaySinh() + ", " +
                        nv.getChucVu() + ", " + nv.getLuongCoBan() + ", " +
                        nv.getHoTen() + ", " + nv.getSoDT() + ", " +
                        nv.getDiaChi());

                // Nếu có tài khoản, ghi tiếp username và password vào cùng dòng
                if (nv.getTaiKhoan() != null) {
                    TaiKhoan tk = nv.getTaiKhoan();
                    pw.print(", " + tk.getUsername() + ", " + tk.getPassword());
                }

                pw.println(); // Xuống dòng
            }

            System.out.println("[+] Ghi file nhan vien thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi ghi file nhan vien: " + e.getMessage());
        }
    }
}
