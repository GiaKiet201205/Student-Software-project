public abstract class GiaoDich {
    private String Ma;
    private String Ngay;
    private double TongTien;

    public GiaoDich(){}
    public GiaoDich(String Ma,String Ngay,double TongTien){
        this.Ma=Ma;
        this.Ngay=Ngay;
        this.TongTien=TongTien;
    }
    public String getMa(){
        return Ma;
    }
    public String getNgay(){
        return Ngay;
    }
    public double getTongTien(){
        return TongTien;
    }
    public void setMa(String Ma){
        this.Ma=Ma;
    }
    public void setNgay(String Ngay){
        this.Ngay=Ngay;
    }
    public void setTongTien(double TongTien){
        this.TongTien=TongTien;
    }
    public abstract double tinhTongTien();
}
