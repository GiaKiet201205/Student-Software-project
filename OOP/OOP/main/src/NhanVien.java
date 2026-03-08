public class NhanVien extends ConNguoi {
    private String MaNV;
    private String NgaySinh;
    private String ChucVu;
    private double LuongCoBan;
    private TaiKhoan taiKhoan; // Quan hệ 1-1 với TaiKhoan

    public NhanVien() {
        super();
    }

    public NhanVien(String MaNV, String NgaySinh, String ChucVu, double LuongCoBan, String HoTen, String SoDT,
            String DiaChi) {
        super(HoTen, SoDT, DiaChi);
        this.MaNV = MaNV;
        this.NgaySinh = NgaySinh;
        this.ChucVu = ChucVu;
        this.LuongCoBan = LuongCoBan;
    }

    public NhanVien(String MaNV, String NgaySinh, String ChucVu, double LuongCoBan, String HoTen, String SoDT,
            String DiaChi, TaiKhoan taiKhoan) {
        super(HoTen, SoDT, DiaChi);
        this.MaNV = MaNV;
        this.NgaySinh = NgaySinh;
        this.ChucVu = ChucVu;
        this.LuongCoBan = LuongCoBan;
        this.taiKhoan = taiKhoan;
    }

    public String getMaNV() {
        return MaNV;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public double getLuongCoBan() {
        return LuongCoBan;
    }

    public void setMaNV(String MaNV) {
        if (MaNV == null || MaNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Ma nhan vien khong duoc de trong.");
        }
        this.MaNV = MaNV;
    }

    public void setNgaySinh(String NgaySinh) {
        if (NgaySinh == null || NgaySinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngay sinh khong duoc de trong.");
        }
        this.NgaySinh = NgaySinh;
    }

    public void setChucVu(String ChucVu) {
        if (ChucVu == null || ChucVu.trim().isEmpty()) {
            throw new IllegalArgumentException("Chuc vu khong duoc de trong.");
        }
        this.ChucVu = ChucVu;
    }

    public void setLuongCoBan(double LuongCoBan) {
        if (LuongCoBan <= 0) {
            throw new IllegalArgumentException("Luong co ban phai lon hon 0.");
        }
        this.LuongCoBan = LuongCoBan;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Ma nhan vien: " + MaNV + "\nNgay sinh: " + NgaySinh + "\nLuong co ban: " + LuongCoBan +
                "\nChuc vu: " + ChucVu + "\nTai khoan: " + (taiKhoan != null ? taiKhoan.getUsername() : "Chua co"));
    }
}
