import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThongBao {
    private String tieuDe;
    private String noiDung;
    private String ngayGui;

    public ThongBao() {
        this.ngayGui = layThoiGianHienTai();
    }

    public ThongBao(String tieuDe, String noiDung) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayGui = layThoiGianHienTai();
    }

    // Getter & Setter
    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayGui() {
        return ngayGui;
    }

    // Phương thức theo UML
    public void guiThongBaoThapTon(SanPham sp) {
        if (sp == null) {
            System.out.println("[ERROR] San pham null, khong the gui thong bao!");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("       [CANH BAO TON KHO THAP]");
        System.out.println("========================================");
        System.out.println("Thoi gian: " + layThoiGianHienTai());
        System.out.println("Ma san pham: " + sp.getMaSP());
        System.out.println("Ten san pham: " + sp.getTenSP());
        System.out.println("So luong ton hien tai: " + sp.getSoLuongTon());
        System.out.println("Nha san xuat: " + sp.getNhaSanXuat());
        System.out.println("========================================");
        System.out.println("=> Can nhap them hang ngay!");
        System.out.println("========================================\n");

        // Gửi email tự động
        String noiDungEmail = "CANH BAO: San pham '" + sp.getTenSP() + 
                            "' (Ma: " + sp.getMaSP() + ") chi con " + 
                            sp.getSoLuongTon() + " san pham trong kho. " +
                            "Vui long nhap them hang.";
        guiEmail(noiDungEmail);
    }

    public void guiEmail(String noiDung) {
        System.out.println("\n+++ GUI EMAIL +++");
        System.out.println("To: admin@nhathuoc.com");
        System.out.println("Subject: Thong bao tu he thong quan ly nha thuoc");
        System.out.println("Time: " + layThoiGianHienTai());
        System.out.println("Content:");
        System.out.println("---");
        System.out.println(noiDung);
        System.out.println("---");
        System.out.println("[*] Email da duoc gui thanh cong!");
        System.out.println("+++++++++++++++++\n");
    }

    // Phương thức bổ sung
    public void guiThongBaoSapHetHan(SanPham sp, String ngayHetHan) {
        if (sp == null) {
            System.out.println("[ERROR] San pham null!");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("    [CANH BAO HAN SU DUNG]");
        System.out.println("========================================");
        System.out.println("Thoi gian: " + layThoiGianHienTai());
        System.out.println("Ma san pham: " + sp.getMaSP());
        System.out.println("Ten san pham: " + sp.getTenSP());
        System.out.println("Han su dung: " + ngayHetHan);
        System.out.println("So luong ton: " + sp.getSoLuongTon());
        System.out.println("========================================");
        System.out.println("=> Can xu ly san pham gan het han!");
        System.out.println("========================================\n");

        String noiDungEmail = "CANH BAO: San pham '" + sp.getTenSP() + 
                            "' (Ma: " + sp.getMaSP() + ") sap het han su dung: " + 
                            ngayHetHan + ". Con " + sp.getSoLuongTon() + 
                            " san pham can xu ly.";
        guiEmail(noiDungEmail);
    }

    public void guiThongBaoChung(String tieuDe, String noiDung) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;

        System.out.println("\n========================================");
        System.out.println("       [THONG BAO]");
        System.out.println("========================================");
        System.out.println("Tieu de: " + tieuDe);
        System.out.println("Thoi gian: " + layThoiGianHienTai());
        System.out.println("Noi dung:");
        System.out.println(noiDung);
        System.out.println("========================================\n");

        guiEmail("THONG BAO: " + tieuDe + "\n\n" + noiDung);
    }

    private String layThoiGianHienTai() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return now.format(formatter);
    }

    public void hienThiThongBao() {
        System.out.println("\n--- THONG BAO ---");
        System.out.println("Tieu de: " + (tieuDe != null ? tieuDe : "N/A"));
        System.out.println("Noi dung: " + (noiDung != null ? noiDung : "N/A"));
        System.out.println("Ngay gui: " + ngayGui);
        System.out.println("-----------------");
    }
}
