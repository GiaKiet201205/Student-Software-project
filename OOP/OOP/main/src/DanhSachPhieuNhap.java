import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DanhSachPhieuNhap implements INhapXuat, IQuanLy<PhieuNhap>, ITinhTien {
     private static Scanner sc = new Scanner(System.in);
    private PhieuNhap[] phieuNhaps;
    private int soLuong;
    private static final int MAX_SIZE = 1000;

    public DanhSachPhieuNhap() {
        phieuNhaps = new PhieuNhap[MAX_SIZE];
        soLuong = 0;
    }

    public PhieuNhap[] getPhieuNhaps() {
        return phieuNhaps;
    }

    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public void NhapDanhSach() {
        System.out.print("Nhap so luong phieu nhap: ");
        int n = sc.nextInt();
        sc.nextLine();
        nhapDanhSach(n);
    }

    @Override
    public void XuatDanhSach() {
        xuatDanhSach();
    }

    @Override
    public void them(PhieuNhap pn) {
        if (soLuong >= phieuNhaps.length) {
            System.out.println("Danh sach phieu nhap da day!");
            return;
        }
        phieuNhaps[soLuong] = pn;
        soLuong++;
        System.out.println("Them phieu nhap thanh cong!");
    }

    @Override
    public void xoa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (phieuNhaps[i].getMaPN().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuong - 1; j++) {
                    phieuNhaps[j] = phieuNhaps[j + 1];
                }
                phieuNhaps[soLuong - 1] = null;
                soLuong--;
                System.out.println("Xoa phieu nhap thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay phieu nhap co ma: " + ma);
    }

    @Override
    public PhieuNhap timKiem(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (phieuNhaps[i].getMaPN().equalsIgnoreCase(ma)) {
                return phieuNhaps[i];
            }
        }
        return null;
    }

    @Override
    public void hienThiDanhSach() {
        xuatDanhSach();
    }

    @Override
    public double tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < soLuong; i++) {
            tong += phieuNhaps[i].tinhTongTien();
        }
        return tong;
    }

    public void nhapDanhSach(int n) {
        System.out.println("Nhap danh sach " + n + " phieu nhap:");
        for (int i = 0; i < n && soLuong < phieuNhaps.length; i++) {
            System.out.println("\n--- Phieu nhap thu " + (i + 1) + " ---");

            String maPN = "";
            while (true) {
                System.out.print("Ma phieu nhap (PN + so): ");
                maPN = sc.nextLine().trim();
                if (!maPN.isEmpty() && timKiem(maPN) == null) {
                    break;
                }
                System.out.println("[!] Ma phieu nhap khong hop le hoac da ton tai!");
            }

            String ngay = "";
            LocalDate ngayDate = null;
            LocalDate today = LocalDate.now();
            LocalDate minDate = LocalDate.of(2000, 1, 1); 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while (ngayDate == null) {
                System.out.print("Ngay nhap (dd/mm/yyyy): ");
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
                    ngayDate = LocalDate.parse(ngay, formatter); 

                    if (ngayDate.isAfter(today)) {
                        System.out.println("[!] Ngay nhap khong duoc lon hon ngay hien tai!");
                        ngayDate = null;
                    } else if (ngayDate.isBefore(minDate)) {
                        System.out.println("[!] Ngay nhap khong hop le! (Nam phai tu 2000 tro di).");
                        ngayDate = null;
                    }
                } catch (DateTimeParseException e) { 
                    System.out.println("[!] Ngay khong hop le (vi du: 30/02/2024)!");
                    ngayDate = null;
                }
            }

            System.out.print("Ma nha cung cap: ");
            String maNCC = sc.nextLine();

            System.out.print("Ghi chu (neu co): ");
            String ghiChu = sc.nextLine();

            PhieuNhap pn = new PhieuNhap(maPN, ngay, 0, maNCC, null, null, ghiChu);
            them(pn);
        }
    }

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach phieu nhap rong!");
            return;
        }

        System.out.println(
                "\n+--------------------------------------------------------------------------------------------------------------------+");
        System.out.println(
                "|                                         DANH SACH PHIEU NHAP                                                       |");
        System.out.println(
                "+--------------------------------------------------------------------------------------------------------------------+");

        for (int i = 0; i < soLuong; i++) {
            PhieuNhap pn = phieuNhaps[i];
            String tenNCC = (pn.getNhaCungCap() != null) ? pn.getNhaCungCap().getTenNCC() : "(" + pn.getMaNCC() + ")";
            String tenNV = (pn.getNhanVien() != null) ? pn.getNhanVien().getHoTen() : "(khong co)";

            System.out.printf("| Ma PN: %-8s | Ngay: %-10s | NCC: %-25s | NV: %-20s |\n",
                    pn.getMaPN(), pn.getNgay(), tenNCC, tenNV);

            if (pn.getSoLuongChiTiet() == 0) {
                System.out.println(
                        "| -> (Chua co san pham nao trong phieu nhap)                                                                         |");
            } else {
                System.out.println(
                        "|--------------------------------------------------------------------------------------------------------------------|");
                System.out.printf("|   | %-8s | %-20s | %-8s | %-12s | %-12s |\n",
                        "Ma SP", "Ten SP", "So Luong", "Don Gia", "Thanh Tien");
                System.out.println(
                        "|---|----------------------------------------------------------------------------------------------------------------|");

                for (int j = 0; j < pn.getSoLuongChiTiet(); j++) {
                    ChiTietPhieuNhap ct = pn.getDanhSachCTPM()[j];
                    if (ct == null) continue;

                    System.out.printf("|   | %-8s | %-20s | %-8d | %-12.0f | %-12.0f |\n",
                        ct.getMaSP(),
                        (ct.getSanPham() != null ? ct.getSanPham().getTenSP() : "N/A"),
                        ct.getSoLuongNhap(),
                        ct.getDonGiaNhap(),
                        ct.tinhThanhTien());

                    LoHang lh = ct.getLoHang();
                    if (lh != null) {
                        String ghiChu = (lh.getGhiChu() != null && !lh.getGhiChu().isEmpty()) ? lh.getGhiChu() : "(khong)";
                        // truncate ghiChu để tránh vỡ layout
                        if (ghiChu.length() > 30) {
                            ghiChu = ghiChu.substring(0, 27) + "...";
                        }
                        String nsx = (lh.getNgaySanXuat() != null ? lh.getNgaySanXuat() : "(null)");
                        String hsd = (lh.getHanSuDung() != null ? lh.getHanSuDung() : "(null)");
                        System.out.printf("|     -> Lo: %-8s | NSX: %-10s | HSD: %-10s | Ghi chu: %-30s |\n",
                        lh.getMaLoHang(), nsx, hsd, ghiChu);
                    }
                }
            }

            System.out.println(
                    "|--------------------------------------------------------------------------------------------------------------------|");
            System.out.printf(
                    "|                                                                                         TONG TIEN: %-15.0f VND |%n",
                    pn.tinhTongTien());
            System.out.println(
                    "+--------------------------------------------------------------------------------------------------------------------+");
        }
    }

    public void timKiemVaHienThi(String keyword) {
        boolean found = false;
        System.out.println("\n========== KET QUA TIM KIEM ==========");

        for (int i = 0; i < soLuong; i++) {
            if (phieuNhaps[i].getMaPN().toLowerCase().contains(keyword.toLowerCase()) ||
                    phieuNhaps[i].getNgay().contains(keyword)) {

                System.out.println("Ma PN: " + phieuNhaps[i].getMaPN());
                System.out.println("Ngay: " + phieuNhaps[i].getNgay());
                System.out.println("Tong tien: " + String.format("%.2f", phieuNhaps[i].tinhTongTien()));
                System.out.println("---");
                found = true;
            }
        }

        if (!found) {
            System.out.println("Khong tim thay phieu nhap nao!");
        }
        System.out.println("======================================");
    }

    public void thongKeNgay(String ngay) {
        System.out.println("\n========== THONG KE PHIEU NHAP NGAY " + ngay + " ==========");
        int soPhieuNhap = 0;
        double tongChiPhi = 0;

        for (int i = 0; i < soLuong; i++) {
            if (phieuNhaps[i].getNgay().equals(ngay)) {
                soPhieuNhap++;
                tongChiPhi += phieuNhaps[i].tinhTongTien();
                System.out.println("- Ma PN: " + phieuNhaps[i].getMaPN() +
                        " - Tong tien: " + String.format("%.2f", phieuNhaps[i].tinhTongTien()));
            }
        }

        System.out.println("\nTong so phieu nhap: " + soPhieuNhap);
        System.out.println("Tong chi phi: " + String.format("%.2f", tongChiPhi) + " VND");
        if (soPhieuNhap > 0) {
            System.out.println("Trung binh/phieu: " + String.format("%.2f", tongChiPhi / soPhieuNhap) + " VND");
        }
        System.out.println("==========================================================");
    }

    public void thongKeTheoNCC(String maNCC) {
        System.out.println("\n========== THONG KE PHIEU NHAP THEO NHA CUNG CAP ==========");
        int soPhieuNhap = 0;
        double tongChiPhi = 0;

        for (int i = 0; i < soLuong; i++) {
            if (phieuNhaps[i].getMaNCC().equalsIgnoreCase(maNCC)) {
                soPhieuNhap++;
                tongChiPhi += phieuNhaps[i].tinhTongTien();
                System.out.println("- Ma PN: " + phieuNhaps[i].getMaPN() +
                        " - Ngay: " + phieuNhaps[i].getNgay() +
                        " - Tong tien: " + String.format("%.2f", phieuNhaps[i].tinhTongTien()));
            }
        }

        System.out.println("\nMa NCC: " + maNCC);
        System.out.println("Tong so phieu nhap: " + soPhieuNhap);
        System.out.println("Tong chi phi: " + String.format("%.2f", tongChiPhi) + " VND");
        System.out.println("==========================================================");
    }

    public double tinhChiPhiThang(int thang, int nam) {
        double tongChi = 0;
        for (int i = 0; i < soLuong; i++) {
            String ngay = phieuNhaps[i].getNgay();
            if (ngay != null && ngay.length() >= 10) {
                String[] parts = ngay.split("/");
                if (parts.length == 3) {
                    try {
                        int thangPN = Integer.parseInt(parts[1]);
                        int namPN = Integer.parseInt(parts[2]);
                        if (thangPN == thang && namPN == nam) {
                            tongChi += phieuNhaps[i].tinhTongTien();
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu format ngày không đúng
                    }
                }
            }
        }
        return tongChi;
    }

    public void docFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/phieunhap.txt"))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < phieuNhaps.length) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",\\s*");
                // Format: PN001, 15/10/2025, 0, NCC001, NV001, Ghi chu
                if (parts.length >= 5) {
                    String maPN = parts[0].trim();
                    String ngay = parts[1].trim();
                    String maNCC = parts[3].trim();
                    String ghiChu = parts.length >= 6 ? parts[5].trim() : "";

                    phieuNhaps[soLuong] = new PhieuNhap(maPN, ngay, 0, maNCC, null, null, ghiChu);
                    soLuong++;
                }
            }
            System.out.println("[+] Doc file phieu nhap thanh cong! So luong: " + soLuong);
        } catch (IOException e) {
            System.out.println("[!] Loi doc file phieu nhap: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/phieunhap.txt"))) {
            for (int i = 0; i < soLuong; i++) {
                PhieuNhap pn = phieuNhaps[i];
                StringBuilder line = new StringBuilder();
                line.append(pn.getMaPN()).append(", ")
                        .append(pn.getNgay()).append(", ")
                        .append(pn.tinhTongTien()).append(", ")
                        .append(pn.getMaNCC());

                if (pn.getNhanVien() != null) {
                    line.append(", ").append(pn.getNhanVien().getMaNV());
                } else {
                    line.append(", ");
                }

                if (pn.getGhiChu() != null && !pn.getGhiChu().isEmpty()) {
                    line.append(", ").append(pn.getGhiChu());
                }

                pw.println(line.toString());
            }
            System.out.println("[+] Ghi file phieu nhap thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi ghi file phieu nhap: " + e.getMessage());
        }
        ghiChiTietFile();
    }

    public void docChiTietFile(DanhSachSanPham dssp, DanhSachLoHang dsLoHang) {
    try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/chitietphieunhap.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",\\s*");
            // New format: PN, SP, MaLo, SoLuong, DonGia, NgaySX, HanSD, GhiChu?
            if (parts.length >= 7) {
                String maPN = parts[0].trim();
                String maSP = parts[1].trim();
                String maLo = parts.length >= 3 ? parts[2].trim() : "";
                int soLuong = 0;
                double donGia = 0;
                String ngaySX = parts.length >= 6 ? parts[5].trim() : "";
                String hanSDRaw = parts.length >= 7 ? parts[6].trim() : "";
                String hanSD = hanSDRaw;
                if (hanSDRaw.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    // lấy phần MM/yyyy từ dd/MM/yyyy
                    hanSD = hanSDRaw.substring(3); // từ chỉ số 3 tới hết -> "MM/yyyy"
                }

                String ghiChu = parts.length >= 8 ? parts[7].trim() : "";

                try {
                    // parse quantity & price from expected positions
                    if (parts.length >= 5) {
                        soLuong = Integer.parseInt(parts[3].trim());
                        donGia = Double.parseDouble(parts[4].trim());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Loi dinh dang so o dong: " + line);
                    continue;
                }

                PhieuNhap pn = timKiem(maPN);
                SanPham sp = dssp.timKiem(maSP);

                if (pn != null && sp != null) {
                    // create ChiTietPhieuNhap with maLo và ghiChu
                    ChiTietPhieuNhap ct = new ChiTietPhieuNhap(maPN, maSP, soLuong, donGia,
                            ngaySX, hanSD, sp, pn, maLo, ghiChu);
                    pn.themChiTiet(ct);

                    // nếu có dsLoHang, thêm LoHang vào danh sách nếu chưa tồn tại
                    LoHang lh = ct.getLoHang();
                    if (dsLoHang != null && lh != null) {
                        if (dsLoHang.timKiem(lh.getMaLoHang()) == null) {
                            dsLoHang.them(lh);
                        }
                    }

                    // cập nhật tồn kho (theo lô)
                    ct.capNhatTonKho();
                }
            } else if (parts.length == 6) {
                // backward compatibility: old format PN,SP,SoLuong,DonGia,NgaySX,HanSD
                try {
                    String maPN = parts[0].trim();
                    String maSP = parts[1].trim();
                    int soLuong = Integer.parseInt(parts[2].trim());
                    double donGia = Double.parseDouble(parts[3].trim());
                    String ngaySX = parts[4].trim();
                    String hanSD = parts[5].trim();

                    PhieuNhap pn = timKiem(maPN);
                    SanPham sp = dssp.timKiem(maSP);
                    if (pn != null && sp != null) {
                        ChiTietPhieuNhap ct = new ChiTietPhieuNhap(maPN, maSP, soLuong, donGia,
                                ngaySX, hanSD, sp, pn, "", "");
                        pn.themChiTiet(ct);

                        // thêm lô tự sinh nếu có và dsLoHang được cung cấp
                        LoHang lh = ct.getLoHang();
                        if (dsLoHang != null && lh != null) {
                            if (dsLoHang.timKiem(lh.getMaLoHang()) == null) {
                                dsLoHang.them(lh);
                            }
                        }

                        ct.capNhatTonKho();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Loi dinh dang dong (old-format): " + line);
                }
            } else {
                System.out.println("[!] Dong chi tiet co format sai (bo qua): " + line);
            }
        }
        System.out.println("[+] Doc file chi tiet phieu nhap thanh cong!");
    } catch (IOException e) {
        System.out.println("[!] Loi doc file chi tiet phieu nhap: " + e.getMessage());
    }
}

    public void docChiTietFile(DanhSachSanPham dssp) {
        docChiTietFile(dssp, null);
    }

    public void ghiChiTietFile() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/chitietphieunhap.txt"))) {
        for (int i = 0; i < soLuong; i++) {
            PhieuNhap pn = phieuNhaps[i];
            if (pn == null) continue;
            for (int j = 0; j < pn.getSoLuongChiTiet(); j++) {
                ChiTietPhieuNhap ct = pn.getDanhSachCTPM()[j];
                if (ct == null) continue;
                LoHang lh = ct.getLoHang();
                if (lh != null) {
                    pw.println(pn.getMaPN() + ", " + ct.getMaSP() + ", " +
                            lh.getMaLoHang() + ", " +
                            lh.getSoLuong() + ", " +
                            lh.getDonGia() + ", " +
                            lh.getNgaySanXuat() + ", " +
                            lh.getHanSuDung() + ", " +
                            (lh.getGhiChu() != null ? lh.getGhiChu() : ""));
                } else {
                    // fallback: write without maLo/ghiChu (old format)
                    pw.println(pn.getMaPN() + ", " + ct.getMaSP() + ", " +
                            ct.getSoLuongNhap() + ", " + ct.getDonGiaNhap() + ", " +
                            ct.getNgaySanXuat() + ", " + ct.getHanSuDung());
                }
            }
        }
        System.out.println("[+] Ghi file chi tiet phieu nhap thanh cong!");
    } catch (IOException e) {
        System.out.println("[!] Loi ghi file chi tiet phieu nhap: " + e.getMessage());
    }
}


    public void lienKetNhaCungCapVaNhanVien(DanhSachNhaCungCap dsNCC, DanhSachNhanVien dsNV) {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/phieunhap.txt"))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null && index < soLuong) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",\\s*");
                if (parts.length >= 5) {
                    String maNCC = parts[3].trim();
                    String maNV = parts[4].trim();

                    if (!maNCC.isEmpty()) {
                        NhaCungCap ncc = dsNCC.timKiem(maNCC);
                        if (ncc != null) {
                            phieuNhaps[index].setNhaCungCap(ncc);
                        }
                    }

                    if (!maNV.isEmpty()) {
                        NhanVien nv = dsNV.timKiem(maNV);
                        if (nv != null) {
                            phieuNhaps[index].setNhanVien(nv);
                        }
                    }
                    index++;
                }
            }
            System.out.println("[+] Lien ket nha cung cap va nhan vien thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi lien ket: " + e.getMessage());
        }
    }
}
