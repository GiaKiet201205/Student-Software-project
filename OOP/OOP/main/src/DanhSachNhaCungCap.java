import java.io.*;

public class DanhSachNhaCungCap implements INhapXuat, IQuanLy<NhaCungCap> {
    private NhaCungCap[] nhaCungCaps;
    private int soLuong;
    private static final int MAX_SIZE = 100;

    public DanhSachNhaCungCap() {
        nhaCungCaps = new NhaCungCap[MAX_SIZE];
        soLuong = 0;
    }

    public NhaCungCap[] getNhaCungCaps() {
        return nhaCungCaps;
    }

    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public void NhapDanhSach() {
        System.out.print("Nhap so luong nha cung cap: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n && soLuong < nhaCungCaps.length; i++) {
            System.out.println("\n--- Nha cung cap thu " + (i + 1) + " ---");
            NhaCungCap ncc = new NhaCungCap();
            ncc.NhapDanhSach();
            them(ncc);
        }
    }

    @Override
    public void XuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach nha cung cap rong!");
            return;
        }

        System.out
                .println(
                        "\n+----------+---------------------------+----------------------------------+---------------+");
        System.out.println("|                            DANH SACH NHA CUNG CAP                                    |");
        System.out
                .println("+----------+---------------------------+----------------------------------+---------------+");
        System.out.printf("| %-8s | %-25s | %-32s | %-13s |\n", "Ma NCC", "Ten NCC", "Dia Chi", "So DT");
        System.out
                .println("+----------+---------------------------+----------------------------------+---------------+");

        for (int i = 0; i < soLuong; i++) {
            System.out.printf("| %-8s | %-25s | %-32s | %-13s |\n",
                    nhaCungCaps[i].getMANCC(),
                    nhaCungCaps[i].getTenNCC(),
                    nhaCungCaps[i].getDiaChi(),
                    nhaCungCaps[i].getSDT());
        }
        System.out
                .println("+----------+---------------------------+----------------------------------+---------------+");
    }

    @Override
    public void them(NhaCungCap ncc) {
        if (soLuong >= nhaCungCaps.length) {
            System.out.println("Danh sach nha cung cap da day!");
            return;
        }
        nhaCungCaps[soLuong] = ncc;
        soLuong++;
        System.out.println("Them nha cung cap thanh cong!");
    }

    @Override
    public void xoa(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (nhaCungCaps[i].getMANCC().equalsIgnoreCase(ma)) {
                for (int j = i; j < soLuong - 1; j++) {
                    nhaCungCaps[j] = nhaCungCaps[j + 1];
                }
                nhaCungCaps[soLuong - 1] = null;
                soLuong--;
                System.out.println("Xoa nha cung cap thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay nha cung cap co ma: " + ma);
    }

    @Override
    public NhaCungCap timKiem(String ma) {
        for (int i = 0; i < soLuong; i++) {
            if (nhaCungCaps[i].getMANCC().equalsIgnoreCase(ma)) {
                return nhaCungCaps[i];
            }
        }
        return null;
    }

    @Override
    public void hienThiDanhSach() {
        XuatDanhSach();
    }

    public void suaTheoMa(String ma) {
        NhaCungCap ncc = timKiem(ma);
        if (ncc == null) {
            System.out.println("Khong tim thay nha cung cap co ma: " + ma);
            return;
        }

        System.out.println("\n--- Thong tin hien tai ---");
        System.out
                .println("+----------+---------------------------+----------------------------------+---------------+");
        System.out.printf("| %-8s | %-25s | %-32s | %-13s |\n", "Ma NCC", "Ten NCC", "Dia Chi", "So DT");
        System.out
                .println("+----------+---------------------------+----------------------------------+---------------+");
        System.out.printf("| %-8s | %-25s | %-32s | %-13s |\n",
                ncc.getMANCC(),
                ncc.getTenNCC(),
                ncc.getDiaChi(),
                ncc.getSDT());
        System.out
                .println("+----------+---------------------------+----------------------------------+---------------+");

        System.out.println("\n--- Nhap thong tin moi (Enter de giu nguyen) ---");

        System.out.print("Ten nha cung cap moi: ");
        String tenMoi = sc.nextLine();
        if (!tenMoi.isEmpty()) {
            ncc.setTenNCC(tenMoi);
        }

        System.out.print("Dia chi moi: ");
        String diaChiMoi = sc.nextLine();
        if (!diaChiMoi.isEmpty()) {
            ncc.setDiaChi(diaChiMoi);
        }

        System.out.print("So dien thoai moi: ");
        String sdtMoi = sc.nextLine();
        if (!sdtMoi.isEmpty()) {
            ncc.setSDT(sdtMoi);
        }

        System.out.println("Cap nhat nha cung cap thanh cong!");
    }

    public void timKiemVaHienThi(String keyword) {
        boolean found = false;
        System.out.println("\n========== KET QUA TIM KIEM ==========");

        for (int i = 0; i < soLuong; i++) {
            if (nhaCungCaps[i].getMANCC().toLowerCase().contains(keyword.toLowerCase()) ||
                    nhaCungCaps[i].getTenNCC().toLowerCase().contains(keyword.toLowerCase())) {

                if (!found) {
                    System.out.println(
                            "+----------+---------------------------+----------------------------------+---------------+");
                    System.out.printf("| %-8s | %-25s | %-32s | %-13s |\n", "Ma NCC", "Ten NCC", "Dia Chi", "So DT");
                    System.out.println(
                            "+----------+---------------------------+----------------------------------+---------------+");
                    found = true;
                }
                System.out.printf("| %-8s | %-25s | %-32s | %-13s |\n",
                        nhaCungCaps[i].getMANCC(),
                        nhaCungCaps[i].getTenNCC(),
                        nhaCungCaps[i].getDiaChi(),
                        nhaCungCaps[i].getSDT());
            }
        }

        if (found) {
            System.out.println(
                    "+----------+---------------------------+----------------------------------+---------------+");
        }

        if (!found) {
            System.out.println("Khong tim thay nha cung cap nao!");
        }
        System.out.println("======================================");
    }

    public void docFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("E:/DOANOOP/OOP/nhacungcap.txt"))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < nhaCungCaps.length) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",\\s*");
                if (parts.length >= 5) {
                    String maNCC = parts[0].trim();
                    String tenNCC = parts[1].trim();
                    String diaChi = parts[2].trim() + ", " + parts[3].trim(); // Ghép địa chỉ và thành phố
                    String sdt = parts[4].trim();

                    nhaCungCaps[soLuong] = new NhaCungCap(maNCC, tenNCC, diaChi, sdt);
                    soLuong++;
                }
            }
            System.out.println("[+] Doc file nha cung cap thanh cong! So luong: " + soLuong);
        } catch (IOException e) {
            System.out.println("[!] Loi doc file nha cung cap: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("E:/DOANOOP/OOP/nhacungcap.txt"))) {
            for (int i = 0; i < soLuong; i++) {
                NhaCungCap ncc = nhaCungCaps[i];
                pw.println(ncc.getMANCC() + ", " + ncc.getTenNCC() + ", " +
                        ncc.getDiaChi() + ", " + ncc.getSDT());
            }
            System.out.println("[+] Ghi file nha cung cap thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi ghi file nha cung cap: " + e.getMessage());
        }
    }
}
