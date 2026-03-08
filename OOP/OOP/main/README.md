# HỆ THỐNG QUẢN LÝ NHÀ THUỐC

## 📋 Mô tả

Hệ thống quản lý nhà thuốc với đầy đủ tính năng quản lý sản phẩm, khách hàng, nhân viên, hóa đơn và thống kê.

**🔥 CẬP NHẬT MỚI:** Hệ thống tự động đọc dữ liệu từ file khi khởi động, không còn tạo dữ liệu mẫu!

## 🚀 Cách chạy chương trình

### Cách 1: Sử dụng Terminal (PowerShell)

```powershell
# Di chuyển vào thư mục chính
cd d:\OOP\OOP\main

# Biên dịch tất cả file Java
javac -encoding UTF-8 -d bin src/*.java

# Chạy chương trình
java -cp bin Main
```

### Cách 2: Sử dụng VS Code

1. Mở file `Main.java`
2. Nhấn `F5` hoặc click nút `Run` ở góc trên phải
3. Chương trình sẽ tự động biên dịch và chạy

## 💾 Quản lý File Dữ liệu

### 📂 Cấu trúc file (trong thư mục `bin/`)

- `sanpham.txt` - Danh sách sản phẩm (4 loại)
- `E:\\\\\\\\\\\\\\\\DOANOOP\\\\\\\\\\\\\\\\OOP\\\\\\\\\\\\\\\\khachhang.txt` - Danh sách khách hàng
- `E:\\\\\\\\\\\\\\\\DOANOOP\\\\\\\\\\\\\\\\OOP\\\\\\\\\\\\\\\\nhanvien.txt` - Danh sách nhân viên + tài khoản
- `E:\\\\\\\\\\\\\\\\DOANOOP\\\\\\\\\\\\\\\\OOP\\\\\\\\\\\\\\\\E:\\\\\\\\\\\\\\\\DOANOOP\\\\\\\\\\\\\\\\OOP\\\\\\\\\\\\\\\\hoadon.txt` - Danh sách hóa đơn

### 🔄 Tự động đọc file khi khởi động

Khi chạy chương trình, hệ thống sẽ:

1. ✅ Đọc tất cả dữ liệu từ file `bin/*.txt`
2. ✅ Tự động liên kết dữ liệu (hóa đơn ↔ khách hàng ↔ nhân viên)
3. ✅ Kiểm tra và cảnh báo tồn kho thấp
4. ✅ Hiển thị thống kê nhanh

### 💾 Lưu dữ liệu

**Cách 1 (Menu Nhân viên):** Chọn `7. Luu du lieu ra file`
**Cách 2 (Khi thoát):** Hệ thống hỏi "Có muốn lưu không?" → Chọn Y

> ⚠️ **Lưu ý:** Nhớ lưu dữ liệu trước khi thoát để không mất thông tin mới!

## 📊 Dữ liệu mẫu có sẵn

### Sản phẩm (16 sản phẩm)

- **5 loại Thuốc**: Paracetamol, Amoxicillin, Vitamin C, Aspirin (⚠️ tồn kho thấp), Omeprazole
- **3 Thực phẩm chức năng**: Omega 3, Bio C, Ginkgo Biloba
- **3 Dụng cụ y tế**: Nhiệt kế, Máy đo huyết áp, Băng gạ
- **3 Sản phẩm làm đẹp**: Kem dưỡng ẩm, Sữa rửa mặt, Kem chống nắng

### Khách hàng (6 khách hàng)

- KH001 - Nguyễn Văn An (150 điểm)
- KH002 - Trần Thị Bình (80 điểm)
- KH003 - Lê Văn Cường (200 điểm - VIP ⭐)
- KH004 - Phạm Thị Dung (45 điểm)
- KH005 - Hoàng Văn Em (120 điểm)
- KH006 - Vũ Thị Phương (30 điểm)

### Nhân viên (5 nhân viên + Tài khoản)

- NV001 - Nguyễn Văn Khánh (Quản lý) - `admin/admin123`
- NV002 - Trần Thị Lan (Dược sỹ) - `duocsy1/duocsy123`
- NV003 - Lê Văn Minh (Thu ngân) - `thungan1/thungan123`
- NV004 - Phạm Thị Nga (Dược sỹ)
- NV005 - Hoàng Văn Tuấn (Thủ kho)

### Hóa đơn (5 hóa đơn)

- HD001-HD005 từ ngày 20/10 đến 25/10/2025

## 🎯 Các chức năng chính

### 1️⃣ Quản lý Sản phẩm

✅ Hiển thị | ✅ Thêm mới | ✅ Tìm kiếm | ✅ Sửa | ✅ Xóa | ✅ Thống kê | ✅ Kiểm tra tồn kho

### 2️⃣ Quản lý Khách hàng

✅ CRUD đầy đủ | ✅ Thống kê tích điểm (Top VIP) | ✅ Tìm kiếm

### 3️⃣ Quản lý Nhân viên

✅ CRUD đầy đủ | ✅ Thống kê theo chức vụ | ✅ Tính lương TB

### 4️⃣ Quản lý Hóa đơn

✅ CRUD | ✅ Thống kê theo ngày | ✅ Tự động tính tổng tiền

### 5️⃣ Thống kê & Báo cáo

✅ Doanh thu theo tháng | ✅ Lợi nhuận | ✅ Tổng doanh thu | ✅ Báo cáo chi tiết

### 6️⃣ Kiểm tra Tồn kho

✅ Cảnh báo tự động | ✅ Email thông báo | ✅ Danh sách sắp hết

## ⚠️ Cảnh báo tự động khi khởi động

Hệ thống sẽ **tự động kiểm tra và cảnh báo**:

- **Aspirin 100mg (T004)**: Chỉ còn **8 sản phẩm** (< 10) ⚠️
- Gửi email thông báo tự động

## 💡 Hướng dẫn test nhanh

### ✅ Test 1: Kiểm tra cảnh báo tồn kho

```
Menu → 6 → Nhập ngưỡng: 10 → Enter
Kết quả: Hiển thị Aspirin (8 sản phẩm)
```

### ✅ Test 2: Top khách VIP

```
Menu → 2 → 6
Kết quả: Lê Văn Cường (200 điểm) đứng đầu
```

### ✅ Test 3: Doanh thu tháng 10/2025

```
Menu → 5 → 4 → Nhập: 10, 2025
Kết quả: Tổng doanh thu từ 5 hóa đơn
```

### ✅ Test 4: Tìm sản phẩm

```
Menu → 1 → 3 → Nhập: "vita"
Kết quả: Vitamin C 1000mg
```

### ✅ Test 5: Thống kê chức vụ

```
Menu → 3 → 6
Kết quả: 1 Quản lý, 2 Dược sỹ, 1 Thu ngân, 1 Thủ kho
```

## 📁 Cấu trúc dự án (21 files)

```
src/
├── 📦 Abstract Classes (3)
│   ├── ConNguoi.java
│   ├── GiaoDich.java
│   └── SanPham.java
│
├── 👥 People (2)
│   ├── KhachHang.java
│   └── NhanVien.java
│
├── 🧾 Transactions (4)
│   ├── HoaDon.java
│   ├── PhieuNhap.java
│   ├── ChiTietGiaoDich.java
│   └── ChiTietPhieuNhap.java
│
├── 💊 Products (4)
│   ├── Thuoc.java
│   ├── ThucPhamChucNang.java
│   ├── DungCuYTe.java
│   └── SanPhamChamSocSacDep.java
│
├── 📋 Managers (4)
│   ├── DanhSachSanPham.java
│   ├── DanhSachKhachHang.java
│   ├── DanhSachNhanVien.java
│   └── DanhSachHoaDon.java
│
├── 🔧 Others (3)
│   ├── DanhMucThuoc.java
│   ├── NhaCungCap.java
│   ├── TaiKhoan.java
│   ├── BaoCaoThongKe.java
│   └── ThongBao.java
│
├── 🔌 Interfaces (4)
│   ├── INhapXuat.java
│   ├── IThongKe.java
│   ├── IQuanLy.java (Generic)
│   └── ITinhTien.java
│
└── ▶️ Main.java
```

## 🎨 Giao diện Menu

```
╔═══════════════════════════════════════════════════╗
║              MENU CHINH                           ║
╠═══════════════════════════════════════════════════╣
║  1. Quan ly San Pham                              ║
║  2. Quan ly Khach Hang                            ║
║  3. Quan ly Nhan Vien                             ║
║  4. Quan ly Hoa Don                               ║
║  5. Thong Ke va Bao Cao                           ║
║  6. Kiem Tra Ton Kho                              ║
║  0. Thoat                                         ║
╚═══════════════════════════════════════════════════╝
```

## 🔧 Yêu cầu hệ thống

- ☕ Java JDK 8+
- 💻 VS Code (hoặc bất kỳ IDE nào)
- 🖥️ Terminal hỗ trợ UTF-8

## 🌟 Điểm nổi bật

✨ **Design Pattern**: Generic Interface (IQuanLy<T>)  
✨ **OOP**: Abstract classes, Inheritance, Polymorphism, Encapsulation  
✨ **Tính năng thông minh**: Tự động cảnh báo, email, thống kê  
✨ **UI/UX**: Menu đẹp mắt với box drawing  
✨ **Dữ liệu mẫu**: Sẵn sàng test ngay

---

**Chúc bạn test thành công! 🎉**

## Getting Started (Original)

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
