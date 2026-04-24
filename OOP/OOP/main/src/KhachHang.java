public class KhachHang extends ConNguoi {
    private String MaKH;
    private int TichDiem; // Đổi từ String sang int

    public KhachHang() {
    }

    public KhachHang(String MaKH, int TichDiem, String HoTen, String SoDT, String DiaChi) {
        super(HoTen, SoDT, DiaChi);
        this.MaKH = MaKH;
        this.TichDiem = TichDiem;
    }

    public String getMaKH() {
        return MaKH;
    }

    public int getTichDiem() {
        return TichDiem;
    }

    public void setMaKH(String MaKH) {
        if (MaKH == null || MaKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma khach hang khong duoc de trong.");
        }
        this.MaKH = MaKH;
    }

    public void setTichDiem(int TichDiem) {
        if (TichDiem < 0) {
            throw new IllegalArgumentException("Tich diem phai lon hon hoac bang 0.");
        }
        this.TichDiem = TichDiem;
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin(); // Gọi method cha để hiển thị thông tin cơ bản
        System.out.println("Ma khach hang: " + getMaKH() + "\nTich diem: " + getTichDiem());
    }
}
