import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SanPhamChamSocSacDep extends SanPham {

    private String CongDung;
    private String LoaiDaPhuHop;
    private String HanSuDung;

    public SanPhamChamSocSacDep() {
        super();
        CongDung = "";
        LoaiDaPhuHop = "";
        HanSuDung = "";
    }

    public SanPhamChamSocSacDep(String maSP, String tenSP, String donViTinh, Double giaNhap, Double giaBanLe,
            int soLuongTon, String nhaSanXuat, int demSanPham, String congDung, String loaiDaPhuHop,
            String hanSuDung) {
        super(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, demSanPham);
        CongDung = congDung;
        LoaiDaPhuHop = loaiDaPhuHop;
        setHanSuDung(hanSuDung);
    }

    public String getCongDung() {
        return CongDung;
    }

    public String getLoaiDaPhuHop() {
        return LoaiDaPhuHop;
    }

    public String getHanSuDung() {
        return HanSuDung;
    }

    public void setCongDung(String congDung) {
        CongDung = congDung;
    }

    public void setLoaiDaPhuHop(String loaiDaPhuHop) {
        LoaiDaPhuHop = loaiDaPhuHop;
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

    @Override
    double tinhGiaBan() {
        double giaBan = getGiaNhap() * 1.4;
        return giaBan;
    }
}
