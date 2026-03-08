public class NhaCungCap implements INhapXuat {
    private String MANCC;
    private String TenNCC;
    private String DiaChi;
    private String SDT;

    public String getDiaChi() {
        return DiaChi;
    }

    public String getMANCC() {
        return MANCC;
    }

    public String getSDT() {
        return SDT;
    }

    public String getTenNCC() {
        return TenNCC;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public void setMANCC(String mANCC) {
        MANCC = mANCC;
    }

    public void setSDT(String sDT) {
        SDT = sDT;
    }

    public void setTenNCC(String tenNCC) {
        TenNCC = tenNCC;
    }

    public NhaCungCap() {
    }

    public NhaCungCap(String mANCC, String tenNCC, String diaChi, String sDT) {
        MANCC = mANCC;
        TenNCC = tenNCC;
        DiaChi = diaChi;
        SDT = sDT;
    }

    @Override
    public void NhapDanhSach() {
        MANCC = "";
        while (true) {
            String tiento = "NCC";
            String so = "";
            System.out.print("Nhap ma nha cung cap:(da co chu " + tiento + ") ");
            so = sc.nextLine();
            if (so.matches("\\d+")) {
                MANCC = tiento + so;
                break;
            } else {
                System.out.println("[!] Ma nha cung cap khong hop le!");
            }
        }
        System.out.print("Nhap ten nha cung cap: ");
        TenNCC = sc.nextLine();
        System.out.print("Nhap dia chi nha cung cap: ");
        DiaChi = sc.nextLine();
        SDT = "";
        while (true) {
            System.out.print("Nhap so dien thoai nha cung cap: ");
            SDT = sc.nextLine();
            // Kiem tra dinh dang so dien thoai (chi chua chu so va do dai 10)
            if (SDT.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("[!] So dien thoai khong hop le! Vui long nhap lai.");
            }
        }
    }

    @Override
    public void XuatDanhSach() {
        System.out.printf("| %-8s | %-25s | %-30s | %-13s |\n", MANCC, TenNCC, DiaChi, SDT);
    }
}
