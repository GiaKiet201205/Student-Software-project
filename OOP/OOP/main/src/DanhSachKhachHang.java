
import java.io.*;

public class DanhSachKhachHang implements INhapXuat, IQuanLy<KhachHang> {

    private KhachHang[] khachHangs;
    private int soLuong;
    private static final int MAX_SIZE = 500;
    private static java.util.Scanner sc = new java.util.Scanner(System.in);

    public DanhSachKhachHang() {
        khachHangs = new KhachHang[MAX_SIZE];
        soLuong = 0;
    }

    public DanhSachKhachHang(int kichThuoc) {
        khachHangs = new KhachHang[kichThuoc];
        soLuong = 0;
    }

    // Getter
    public KhachHang[] getKhachHangs() {
        return khachHangs;
    }

    public int getSoLuong() {
        return soLuong;
    }

    // Implement INhapXuat
    @Override
    public void NhapDanhSach() {
        System.out.print("Nhap so luong khach hang: ");
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
    public void them(KhachHang kh) {
        themPhanTu(kh);
    }

    @Override
    public void xoa(String ma) {
        xoaTheoMa(ma);
    }

    @Override
    public KhachHang timKiem(String ma) {
        return timKiemTheoMa(ma);
    }

    @Override
    public void hienThiDanhSach() {
        xuatDanhSach();
    }

    // Các phương thức theo UML
    public void nhapDanhSach(int n) {
        System.out.println("Nhap danh sach " + n + " khach hang:");
        for (int i = 0; i < n && soLuong < khachHangs.length; i++) {
            System.out.println("\n--- Khach hang thu " + (i + 1) + " ---");

            // Nhập mã khách hàng không được bỏ trống và không trùng
            String maKH = "";
            boolean maKHHopLe = false;
            while (!maKHHopLe) {
                System.out.print("Ma KH (se co san tien to 'KH'): ");
                String maKHNhap = sc.nextLine().trim();

                if (maKHNhap.isEmpty()) {
                    System.out.println("[!] Ma khach hang khong duoc de trong!");
                    continue;
                } else if (maKHNhap.matches(".*[a-zA-Z]+.*")) {
                    System.out.println("[!] Ma khach hang chi duoc chua so! Vui long nhap lai.");
                    continue;
                }

                maKH = "KH" + maKHNhap;

                // Kiểm tra mã không trùng với mã đã tồn tại
                if (timKiemTheoMa(maKH) != null) {
                    System.out.println("[!] Ma khach hang '" + maKH + "' da ton tai! Vui long nhap ma khac.");
                    continue;
                }

                maKHHopLe = true;
            }

            // === BẮT ĐẦU SỬA ĐỔI (YÊU CẦU 3) ===
            // Nhập họ tên không được bỏ trống và chỉ chứa chữ
            String hoTen = "";
            boolean hoTenHopLe = false;
            while (!hoTenHopLe) {
                System.out.print("Ho ten: ");
                hoTen = sc.nextLine().trim();
                if (hoTen.isEmpty()) {
                    System.out.println("[!] Ho ten khong duoc de trong!");
                } else if (!hoTen.matches("^[\\p{L} ]+$")) { // Kiểm tra chỉ chứa chữ cái (Unicode) và khoảng trắng
                    System.out.println("[!] Ho ten chi duoc phep chua chu cai va khoang trang!");
                } else {
                    hoTenHopLe = true;
                }
            }
            // === KẾT THÚC SỬA ĐỔI (YÊU CẦU 3) ===

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
                } else if (soDT.length() < 10) {
                    System.out.println("[!] So dien thoai phai co it nhat 10 chu so!");
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
                } else {
                    diaChiHopLe = true;
                }
            }

            // Nhập tích điểm không được bỏ trống, phải >= 0
            int tichDiem = -1;
            boolean tichDiemHopLe = false;
            while (!tichDiemHopLe) {
                System.out.print("Tich diem: ");
                String tichDiemStr = sc.nextLine().trim();

                if (tichDiemStr.isEmpty()) {
                    System.out.println("[!] Tich diem khong duoc de trong!");
                    continue;
                }

                try {
                    tichDiem = Integer.parseInt(tichDiemStr);
                    if (tichDiem < 0) {
                        System.out.println("[!] Tich diem phai >= 0!");
                    } else {
                        tichDiemHopLe = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Vui long nhap so!");
                }
            }

            KhachHang kh = new KhachHang(maKH, tichDiem, hoTen, soDT, diaChi);
            themPhanTu(kh);
        }
    }

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach khach hang rong!");
            return;
        }

        System.out.println(
                "\n======================================================================================================================");
        System.out.printf("| %-8s | %-25s | %-15s | %-40s | %-10s |\n",
                "Ma KH", "Ho Ten", "So Dien Thoai", "Dia Chi", "Tich Diem");
        System.out.println(
                "======================================================================================================================");

        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = khachHangs[i];
            System.out.printf("| %-8s | %-25s | %-15s | %-40s | %-10d |\n",
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getSoDT(),
                    kh.getDiaChi(),
                    kh.getTichDiem());
        }
        System.out.println(
                "======================================================================================================================");
    }

    public void themPhanTu(KhachHang kh) {
        if (soLuong >= khachHangs.length) {
            System.out.println("Danh sach da day!");
            return;
        }
        khachHangs[soLuong] = kh;
        soLuong++;
        System.out.println("Them khach hang thanh cong!");
    }

    public void suaTheoMa(String ma) {
        KhachHang kh = timKiemTheoMa(ma);
        if (kh == null) {
            System.out.println("Khong tim thay khach hang co ma: " + ma);
            return;
        }

        System.out.println("Nhap thong tin moi (Enter de giu nguyen):");

        // === BẮT ĐẦU SỬA ĐỔI (YÊU CẦU 3 - Kiểm tra khi sửa) ===
        // Sửa họ tên
        System.out.print("Ho ten [" + kh.getHoTen() + "]: ");
        String ten = sc.nextLine().trim();
        if (!ten.isEmpty()) {
            if (ten.matches("^[\\p{L} ]+$")) {
                kh.setHoTen(ten);
            } else {
                System.out.println("[!] Ho ten chi duoc phep chua chu cai! Giu nguyen gia tri cu.");
            }
        }
        // === KẾT THÚC SỬA ĐỔI ===

        // Sửa số điện thoại
        System.out.print("So dien thoai [" + kh.getSoDT() + "]: ");
        String sdt = sc.nextLine().trim();
        if (!sdt.isEmpty()) {
            if (sdt.matches("\\d+") && sdt.length() >= 10) {
                kh.setSoDT(sdt);
            } else {
                System.out.println("[!] So dien thoai khong hop le! Giu nguyen gia tri cu.");
            }
        }

        // Sửa địa chỉ
        System.out.print("Dia chi [" + kh.getDiaChi() + "]: ");
        String dc = sc.nextLine().trim();
        if (!dc.isEmpty()) {
            kh.setDiaChi(dc);
        }

        // Sửa tích điểm
        System.out.print("Tich diem [" + kh.getTichDiem() + "]: ");
        String td = sc.nextLine().trim();
        if (!td.isEmpty()) {
            try {
                int diem = Integer.parseInt(td);
                if (diem >= 0) {
                    kh.setTichDiem(diem);
                } else {
                    System.out.println("[!] Tich diem phai >= 0! Giu nguyen gia tri cu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Tich diem khong hop le! Giu nguyen gia tri cu.");
            }
        }

        System.out.println("Cap nhat thanh cong!");
    }

    public void xoaTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (khachHangs[i].getMaKH().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuong - 1; j++) {
                    khachHangs[j] = khachHangs[j + 1];
                }
                khachHangs[soLuong - 1] = null;
                soLuong--;
                System.out.println("Xoa khach hang thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay khach hang co ma: " + ma);
    }

    public KhachHang timKiemTheoMa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (khachHangs[i].getMaKH().equalsIgnoreCase(ma)) {
                return khachHangs[i];
            }
        }
        return null;
    }

    public void timKiemVaHienThi(String key) {
        boolean found = false;
        System.out.println("\n========== KET QUA TIM KIEM ==========");
        for (int i = 0; i < soLuong; i++) {
            if (khachHangs[i].getMaKH().toLowerCase().contains(key.toLowerCase())
                    || khachHangs[i].getHoTen().toLowerCase().contains(key.toLowerCase())
                    || khachHangs[i].getSoDT().contains(key)) {
                khachHangs[i].hienThiThongTin();
                System.out.println("---");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay khach hang nao!");
        }
        System.out.println("======================================");
    }

    public void thongKeDiem() {
        System.out.println("\n========== THONG KE TICH DIEM ==========");

        // Sắp xếp theo tích điểm giảm dần
        for (int i = 0; i < soLuong - 1; i++) {
            for (int j = i + 1; j < soLuong; j++) {
                if (khachHangs[i].getTichDiem() < khachHangs[j].getTichDiem()) {
                    KhachHang temp = khachHangs[i];
                    khachHangs[i] = khachHangs[j];
                    khachHangs[j] = temp;
                }
            }
        }

        System.out.println("Top khach hang co tich diem cao:");
        int top = Math.min(10, soLuong);
        for (int i = 0; i < top; i++) {
            System.out.println((i + 1) + ". " + khachHangs[i].getHoTen()
                    + " - Ma: " + khachHangs[i].getMaKH()
                    + " - Diem: " + khachHangs[i].getTichDiem());
        }

        // Thống kê tổng điểm
        int tongDiem = 0;
        for (int i = 0; i < soLuong; i++) {
            tongDiem += khachHangs[i].getTichDiem();
        }
        System.out.println("\nTong so khach hang: " + soLuong);
        System.out.println("Tong tich diem: " + tongDiem);
        if (soLuong > 0) {
            System.out.println("Trung binh diem/khach: " + (tongDiem / soLuong));
        }
        System.out.println("========================================");
    }

    public void docFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/khachhang.txt"))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < khachHangs.length) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",\\s*", -1);
                if (parts.length >= 5) {
                    String maKH = parts[0].trim();
                    int tichDiem = Integer.parseInt(parts[1].trim());
                    String hoTen = parts[2].trim();
                    String soDT = parts[3].trim();
                    String diaChi = parts[4].trim();

                    khachHangs[soLuong] = new KhachHang(maKH, tichDiem, hoTen, soDT, diaChi);
                    soLuong++;
                }
            }
            System.out.println("[+] Doc file khach hang thanh cong! So luong: " + soLuong);
        } catch (IOException e) {
            System.out.println("[!] Loi doc file khach hang: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[!] Loi dinh dang so trong file khach hang: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/khachhang.txt"))) {
            for (int i = 0; i < soLuong; i++) {
                KhachHang kh = khachHangs[i];
                pw.println(kh.getMaKH() + ", " + kh.getTichDiem() + ", "
                        + kh.getHoTen() + ", " + kh.getSoDT() + ", "
                        + kh.getDiaChi());
            }
            System.out.println("[+] Ghi file khach hang thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi ghi file khach hang: " + e.getMessage());
        }
    }
}
