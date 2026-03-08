import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Thuoc extends SanPham {
    private String HoatChat;      
    private String DangBaoChe;   
    private String HanSuDung;    

    public Thuoc() {
        super();
        HoatChat = "";
        DangBaoChe = "";
        HanSuDung = "";
    }

    public Thuoc(String maSP, String tenSP, String donViTinh, Double giaNhap, Double giaBanLe,
                 int soLuongTon, String nhaSanXuat, int demSanPham,
                 String HoatChat, String dangBaoChe, String hanSuDung) {
        super(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, demSanPham);
        this.HoatChat = HoatChat;
        this.DangBaoChe = dangBaoChe;
        setHanSuDung(hanSuDung);
    }

    public String getHoatChat() {
        return HoatChat;
    }

    public void setHoatChat(String HoatChat) {
        this.HoatChat = HoatChat;
    }

    public String getDangBaoChe() {
        return DangBaoChe;
    }

    public void setDangBaoChe(String dangBaoChe) {
        this.DangBaoChe = dangBaoChe;
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

    @Override
    double tinhGiaBan() {
        double giaBan = getGiaNhap() * 1.25;
        return giaBan;
    }
}
