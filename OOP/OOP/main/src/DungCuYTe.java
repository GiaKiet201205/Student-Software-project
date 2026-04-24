public class DungCuYTe extends SanPham {

    private String LoaiDungCu;
    private String VatLieu;
    private String HanBaoHanh;

    public DungCuYTe() {
        super();
        LoaiDungCu = "";
        VatLieu = "";
        HanBaoHanh = "";
    }

    public DungCuYTe(String maSP, String tenSP, String donViTinh, Double giaNhap, Double giaBanLe, int soLuongTon,
            String nhaSanXuat, int demSanPham, String loaiDungCu, String vatLieu, String hanBaoHanh) {
        super(maSP, tenSP, donViTinh, giaNhap, giaBanLe, soLuongTon, nhaSanXuat, demSanPham);
        LoaiDungCu = loaiDungCu;
        VatLieu = vatLieu;
        HanBaoHanh = hanBaoHanh;
    }

    public String getLoaiDungCu() {
        return LoaiDungCu;
    }

    public String getVatLieu() {
        return VatLieu;
    }

    public String getHanBaoHanh() {
        return HanBaoHanh;
    }

    public void setLoaiDungCu(String loaiDungCu) {
        LoaiDungCu = loaiDungCu;
    }

    public void setVatLieu(String vatLieu) {
        VatLieu = vatLieu;
    }

    public void setHanBaoHanh(String hanBaoHanh) {
        HanBaoHanh = hanBaoHanh;
    }

    @Override
    double tinhGiaBan() {
        double giaBan = getGiaNhap() * 1.3;
        return giaBan;
    }
}
