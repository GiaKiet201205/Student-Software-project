package com.ui.data_form;

import com.ui.common.BaseButton;
import java.awt.*;
import javax.swing.*;

public class DataFormDiemThi extends JDialog {

    // 1. Khai báo các trường thông tin cơ bản
    public JTextField txtId;
    public JTextField txtCccd;
    public JTextField txtSbd;
    public JComboBox<String> cbxPhuongThuc;

    // 2. Khai báo các trường nhập điểm (Tên biến khớp với Mapper của DiemThiPanel)
    public JTextField txtTO, txtLI, txtHO, txtSI, txtVA, txtSU, txtDI;
    public JTextField txtN1_THI, txtN1_CC, txtCNCN, txtCNNN, txtTI, txtKTPL;
    public JTextField txtNL1, txtNK1, txtNK2, txtNK3, txtNK4;

    // 3. Nút hành động
    public BaseButton btnSave;
    public BaseButton btnCancel;

    public DataFormDiemThi() {
        setTitle("Chi tiết Hồ sơ Điểm Xét Tuyển");
        setSize(750, 650);
        setLocationRelativeTo(null);
        setModal(true); // Khóa cửa sổ nền khi mở form này
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- KHỞI TẠO CÁC COMPONENT ---
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240)); // Màu xám cho ID vì auto-generate

        txtCccd = new JTextField();
        txtSbd = new JTextField();

        // Khởi tạo Dropdown Phương thức
        String[] methods = {
                "Xét điểm THPT (Mã 3)",
                "Đánh giá Năng lực (Mã 4)",
                "Điểm thi V-SAT (Mã 5)"
        };
        cbxPhuongThuc = new JComboBox<>(methods);
        cbxPhuongThuc.setBackground(Color.WHITE);

        // Khởi tạo các ô nhập điểm
        txtTO = new JTextField(); txtLI = new JTextField(); txtHO = new JTextField();
        txtSI = new JTextField(); txtVA = new JTextField(); txtSU = new JTextField();
        txtDI = new JTextField(); txtN1_THI = new JTextField(); txtN1_CC = new JTextField();
        txtCNCN = new JTextField(); txtCNNN = new JTextField(); txtTI = new JTextField();
        txtKTPL = new JTextField(); txtNL1 = new JTextField(); txtNK1 = new JTextField();
        txtNK2 = new JTextField(); txtNK3 = new JTextField(); txtNK4 = new JTextField();

        // --- BỐ CỤC GIAO DIỆN (LAYOUT) ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);

        // PHẦN 1: THÔNG TIN CƠ BẢN
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 20, 0)); // 2 dòng, 2 cột
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin định danh"));

        pnlInfo.add(createRow("ID Hồ Sơ", txtId));
        pnlInfo.add(createRow("Số CCCD (*)", txtCccd));
        pnlInfo.add(createRow("Phương Thức (*)", cbxPhuongThuc));
        pnlInfo.add(createRow("Số Báo Danh", txtSbd));

        mainPanel.add(pnlInfo);
        mainPanel.add(Box.createVerticalStrut(15)); // Khoảng cách

        // PHẦN 2: CHI TIẾT ĐIỂM (Chia 2 cột)
        JPanel pnlScores = new JPanel(new GridLayout(0, 2, 40, 0)); // 2 cột, khoảng cách ngang 40px
        pnlScores.setBackground(Color.WHITE);
        pnlScores.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Chi tiết các đầu điểm"));

        // Cột Trái (Tự nhiên & Ngôn ngữ)
        pnlScores.add(createRow("Toán", txtTO));
        pnlScores.add(createRow("Vật Lý", txtLI));
        pnlScores.add(createRow("Hóa Học", txtHO));
        pnlScores.add(createRow("Sinh Học", txtSI));
        pnlScores.add(createRow("Ngoại Ngữ (Thi)", txtN1_THI));
        pnlScores.add(createRow("Ngoại Ngữ (Quy đổi)", txtN1_CC));
        pnlScores.add(createRow("CN Công Nghiệp", txtCNCN));
        pnlScores.add(createRow("CN Nông Nghiệp", txtCNNN));
        pnlScores.add(createRow("Tin Học", txtTI));

        // Cột Phải (Xã hội, Năng lực & Năng khiếu)
        pnlScores.add(createRow("Ngữ Văn", txtVA));
        pnlScores.add(createRow("Lịch Sử", txtSU));
        pnlScores.add(createRow("Địa Lý", txtDI));
        pnlScores.add(createRow("GDCD / KTPL", txtKTPL));
        pnlScores.add(createRow("Điểm ĐGNL", txtNL1));
        pnlScores.add(createRow("Năng Khiếu 1", txtNK1));
        pnlScores.add(createRow("Năng Khiếu 2", txtNK2));
        pnlScores.add(createRow("Năng Khiếu 3", txtNK3));
        pnlScores.add(createRow("Năng Khiếu 4", txtNK4));

        mainPanel.add(pnlScores);

        // Bọc trong JScrollPane đề phòng màn hình nhỏ
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Lăn chuột mượt hơn
        add(scrollPane, BorderLayout.CENTER);

        // --- PHẦN 3: NÚT ĐIỀU KHIỂN (FOOTER) ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlFooter.setBackground(new Color(245, 245, 245));
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        btnSave = new BaseButton("Lưu dữ liệu", new Color(46, 204, 113));
        btnCancel = new BaseButton("Hủy bỏ", new Color(149, 165, 166));

        btnCancel.addActionListener(e -> dispose()); // Đóng form khi bấm Hủy

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);
        add(pnlFooter, BorderLayout.SOUTH);

        // --- LOGIC UX: TỰ ĐỘNG KHÓA CÁC Ô ĐIỂM DỰA VÀO PHƯƠNG THỨC ---
        cbxPhuongThuc.addActionListener(e -> updateFieldsState());
        // Gọi lần đầu để setup trạng thái mặc định
        updateFieldsState();
    }

    // =====================================================================
    // HÀM PHỤ TRỢ: ÉP THẲNG HÀNG LABEL VÀ TEXTFIELD
    // =====================================================================
    private JPanel createRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(130, 30)); // Cố định chiều rộng nhãn

        component.setPreferredSize(new Dimension(0, 30)); // Chiều cao component

        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Padding bottom

        return row;
    }
// Trong DataFormDiemThi.java

    /**
     * Khóa hoặc mở khóa các trường định danh
     * @param isLocked true nếu là chế độ Sửa (Khóa), false nếu là chế độ Thêm mới (Mở)
     */
    public void lockIdentificationFields(boolean isLocked) {
        Color lockColor = new Color(149, 165, 166); // Màu xám nhạt báo hiệu trạng thái khóa
        Color whiteColor = Color.WHITE;

        // 1. Khóa CCCD
        txtCccd.setEditable(!isLocked);
        txtCccd.setFocusable(!isLocked);
        txtCccd.setBackground(isLocked ? lockColor : whiteColor);

        // 2. Khóa Số báo danh
        txtSbd.setEditable(!isLocked);
        txtSbd.setFocusable(!isLocked);
        txtSbd.setBackground(isLocked ? lockColor : whiteColor);

        // 3. Khóa ComboBox Phương thức
        cbxPhuongThuc.setEnabled(!isLocked);

        // Luôn luôn khóa ID vì đây là trường tự tăng
        txtId.setEditable(false);
        txtId.setFocusable(false);
        txtId.setBackground(lockColor);
    }
    // =====================================================================
    // HÀM PHỤ TRỢ: LÀM MỜ (DISABLED) CÁC Ô KHÔNG LIÊN QUAN hồ
    // =====================================================================
    private void updateFieldsState() {
        int index = cbxPhuongThuc.getSelectedIndex();
        boolean isDGNL = (index == 1); // 1 = ĐGNL

        // Nếu là ĐGNL thì làm mờ tất cả các môn THPT, chỉ mở ô ĐGNL
        Color disabledColor = new Color(149, 165, 166);
        Color enabledColor = Color.WHITE;

        JTextField[] thptFields = {
                txtTO, txtLI, txtHO, txtSI, txtVA, txtSU, txtDI, txtKTPL,
                txtN1_THI, txtN1_CC, txtCNCN, txtCNNN, txtTI
        };

        for (JTextField field : thptFields) {
            field.setEditable(!isDGNL);
            field.setEnabled(!isDGNL);
            field.setFocusable(!isDGNL);
            field.setBackground(isDGNL ? disabledColor : enabledColor);
            if (isDGNL) field.setText(""); // Xóa trắng dữ liệu rác nếu có
        }

        // Ô ĐGNL thì ngược lại
        txtNL1.setFocusable(isDGNL);
        txtNL1.setEditable(isDGNL);
        txtNL1.setBackground(!isDGNL ? disabledColor : enabledColor);
        if (!isDGNL) txtNL1.setText("");

    }
}