import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ThucPhamChucNang extends SanPham {
    private String ThanhPhan;
    private String CongDung;
    private String HinhThucBaoQuan;
    private String HanSuDung;

    public ThucPhamChucNang() {
        super();
        ThanhPhan = "";
        CongDung = "";
        HinhThucBaoQuan = "";
    }

    public ThucPhamChucNang(String maSP, String tenSP, String donViTinh, Double giaNhap, Double giaBanLe,
            int soLuongTon, String nhaSanXuat, int demSanPham, String thanhPhan, String congDung,
            String hinhThucBaoQuan, String hanSuDung) {
        super(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, demSanPham);
        ThanhPhan = thanhPhan;
        CongDung = congDung;
        HinhThucBaoQuan = hinhThucBaoQuan;
        setHanSuDung(hanSuDung);
    }

    public String getThanhPhan() {
        return ThanhPhan;
    }

    public String getCongDung() {
        return CongDung;
    }

    public String getHinhThucBaoQuan() {
        return HinhThucBaoQuan;
    }

    public String getHanSuDung() {
        return HanSuDung;
    }

    public void setHanSuDung(String hanSuDung) {
        try {
            LocalDate expiry;
            if (hanSuDung.contains("/")) {
                String[] parts = hanSuDung.split("/");
                if (parts.length == 2) {
                    int month = Integer.parseInt(parts[0]);
                    int year = Integer.parseInt(parts[1]);
                    expiry = LocalDate.of(year, month, 1);
                } else {
                    throw new IllegalArgumentException("Dinh dang han su dung khong hop le. Su dung dinh dang MM/YYYY.");
                }
            } else if (hanSuDung.contains("-")) {
                 String[] parts = hanSuDung.split("-");
                if (parts.length == 2) {
                    int month = Integer.parseInt(parts[0]);
                    int year = Integer.parseInt(parts[1]);
                    expiry = LocalDate.of(year, month, 1);
                } else if (parts.length == 3) {
                    expiry = LocalDate.parse(hanSuDung);
                }
                else {
                    throw new IllegalArgumentException("Dinh dang han su dung khong hop le. Su dung dinh dang MM-YYYY or YYYY-MM-DD.");
                }
            }
            else {
                throw new IllegalArgumentException("Dinh dang han su dung khong hop le. Su dung dinh dang YYYY-MM-DD, MM/YYYY, or MM-YYYY.");
            }

            if (expiry.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Han su dung phai lon hon ngay hien tai.");
            }
            this.HanSuDung = hanSuDung;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Dinh dang han su dung khong hop le. Su dung dinh dang YYYY-MM-DD.");
        }
    }

    public void setThanhPhan(String thanhPhan) {
        ThanhPhan = thanhPhan;
    }

    public void setCongDung(String congDung) {
        CongDung = congDung;
    }

    public void setHinhThucBaoQuan(String hinhThucBaoQuan) {
        HinhThucBaoQuan = hinhThucBaoQuan;
    }

    @Override
    double tinhGiaBan() {
        double giaBan = getGiaNhap() * 1.5;
        return giaBan;
    }
}
