public class TaiKhoan {
    private String Username;
    private String Password;
    private String Quyen;
    public TaiKhoan(){}
    public TaiKhoan(String Username, String Password, String Quyen){
        this.Username=Username;
        this.Password=Password;
        this.Quyen=Quyen;
    }
    public String getPassword() {
        return Password;
    }
    public String getQuyen() {
        return Quyen;
    }
    public String getUsername() {
        return Username;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public void setQuyen(String quyen) {
        Quyen = quyen;
    }
    public void setUsername(String username) {
        Username = username;
    }
}
