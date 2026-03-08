public abstract class ConNguoi {
    private String HoTen;
    private String SoDT;
    private String DiaChi;

    public ConNguoi() {
    }

    public ConNguoi(String HoTen, String SoDT, String DiaChi) {
        this.HoTen = HoTen;
        this.SoDT = SoDT;
        this.DiaChi = DiaChi;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getSoDT() {
        return SoDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setHoTen(String HoTen) {
        if (HoTen == null || HoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Ho ten khong duoc de trong.");
        }
        this.HoTen = HoTen;
    }

    public void setSoDT(String SoDT) {
        if (SoDT == null || SoDT.trim().isEmpty()) {
            throw new IllegalArgumentException("So dien thoai khong duoc de trong.");
        }
        if (!SoDT.matches("\\d{10}")) {
            throw new IllegalArgumentException("So dien thoai phai co 10 chu so.");
        }
        this.SoDT = SoDT;
    }

    public void setDiaChi(String DiaChi) {
        if (DiaChi == null || DiaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Dia chi khong duoc de trong.");
        }
        this.DiaChi = DiaChi;
    }

    public void hienThiThongTin() {
        System.out.println("Ho ten: " + HoTen + "\nSo dien thoai: " + SoDT + "\nDia chi: " + DiaChi);
    }
}
