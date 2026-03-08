import java.io.*;

public class DanhSachLoHang {
    private LoHang[] loHangs;
    private int soLuong;
    private static final int MAX = 2000;
    private static final String FILE_NAME = "E:/DOANOOP/OOP/lohang.txt";

    public DanhSachLoHang() {
        loHangs = new LoHang[MAX];
        soLuong = 0;
    }

    public LoHang[] getLoHangs() {
        return loHangs;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public boolean them(LoHang lh) {
        if (lh == null) return false;
        if (timKiem(lh.getMaLoHang()) != null) {
            return false;
        }
        if (soLuong >= loHangs.length) return false;
        loHangs[soLuong++] = lh;
        return true;
    }

    public LoHang timKiem(String maLo) {
        if (maLo == null) return null;
        for (int i = 0; i < soLuong; i++) {
            if (loHangs[i] != null && loHangs[i].getMaLoHang().equalsIgnoreCase(maLo)) {
                return loHangs[i];
            }
        }
        return null;
    }

    public boolean xoa(String maLo) {
        for (int i = 0; i < soLuong; i++) {
            if (loHangs[i] != null && loHangs[i].getMaLoHang().equalsIgnoreCase(maLo)) {
                for (int j = i; j < soLuong - 1; j++) {
                    loHangs[j] = loHangs[j + 1];
                }
                loHangs[soLuong - 1] = null;
                soLuong--;
                return true;
            }
        }
        return false;
    }

    public void XuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sach lo hang rong!");
            return;
        }
        System.out.println("\n========== DANH SACH LO HANG ==========");
        for (int i = 0; i < soLuong; i++) {
            LoHang lh = loHangs[i];
            if (lh == null) continue;
            System.out.println((i + 1) + ". " + lh.toString());
        }
        System.out.println("=======================================");
    }

    public void docFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            // không có file thì không báo lỗi, chỉ in thông báo nhẹ
            // System.out.println("[i] Khong tim thay file " + FILE_NAME);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            soLuong = 0;
            while ((line = br.readLine()) != null && soLuong < loHangs.length) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",\\s*");
                // Expect: MaLo, MaSP, MaPN, SoLuong, DonGia, NgaySX, HanSD, GhiChu?
                if (parts.length >= 7) {
                    try {
                        String maLo = parts[0].trim();
                        String maSP = parts[1].trim();
                        String maPN = parts[2].trim();
                        int soLuongLH = Integer.parseInt(parts[3].trim());
                        double donGia = Double.parseDouble(parts[4].trim());
                        String ngaySX = parts[5].trim();
                        String hanSD = parts[6].trim();
                        String ghiChu = parts.length >= 8 ? parts[7].trim() : "";

                        LoHang lh = new LoHang(maLo, maSP, maPN, soLuongLH, donGia, ngaySX, hanSD, ghiChu);
                        // Nếu đã tồn tại mã lô trong bộ nhớ thì bỏ qua (không ghi đè)
                        if (timKiem(maLo) == null) {
                            loHangs[soLuong++] = lh;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("[!] Loi dinh dang so o dong lohang: " + line);
                    }
                } else {
                    System.out.println("[!] Dong lohang co format sai (bo qua): " + line);
                }
            }
            System.out.println("[+] Doc file lo hang (lohang.txt) thanh cong. So luong: " + soLuong);
        } catch (IOException e) {
            System.out.println("[!] Loi doc file lohang: " + e.getMessage());
        }
    }

    public void ghiFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < soLuong; i++) {
                LoHang lh = loHangs[i];
                if (lh == null) continue;
                pw.println(lh.toString());
            }
            System.out.println("[+] Ghi file lo hang thanh cong!");
        } catch (IOException e) {
            System.out.println("[!] Loi ghi file lohang: " + e.getMessage());
        }
    }
}
