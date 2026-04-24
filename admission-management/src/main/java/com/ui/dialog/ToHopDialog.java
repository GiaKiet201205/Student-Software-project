package com.ui.dialog;

import com.config.AppConfig;
import com.entity.ToHopMonThi;
import com.ui.common.BaseButton;

import javax.swing.*;
import java.awt.*;

public class ToHopDialog extends JDialog {
    private JTextField txtMaToHop, txtTenToHop, txtMon1, txtMon2, txtMon3;
    private ToHopMonThi toHopResult = null; // Chứa kết quả sau khi nhập
    private boolean isEditMode = false;

    // Constructor dùng cho cả Thêm và Sửa
    public ToHopDialog(Window owner, ToHopMonThi toHopEdit) {
        super(owner, "Thông tin Tổ Hợp Môn", Dialog.ModalityType.APPLICATION_MODAL);
        this.isEditMode = (toHopEdit != null);

        initComponents();

        // Nếu là chế độ Sửa, điền sẵn dữ liệu cũ vào form và khóa ô Mã
        if (isEditMode) {
            txtMaToHop.setText(toHopEdit.getMaToHop());
            txtMaToHop.setEditable(false); // Không cho sửa khóa chính
            txtTenToHop.setText(toHopEdit.getTenToHop());
            txtMon1.setText(toHopEdit.getMon1());
            txtMon2.setText(toHopEdit.getMon2());
            txtMon3.setText(toHopEdit.getMon3());
        }

        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(AppConfig.COLOR_WHITE);

        panel.add(new JLabel("Mã Tổ Hợp (VD: A00):"));
        txtMaToHop = new JTextField();
        panel.add(txtMaToHop);

        panel.add(new JLabel("Tên Tổ Hợp:"));
        txtTenToHop = new JTextField();
        panel.add(txtTenToHop);

        panel.add(new JLabel("Môn Thi 1:"));
        txtMon1 = new JTextField();
        panel.add(txtMon1);

        panel.add(new JLabel("Môn Thi 2:"));
        txtMon2 = new JTextField();
        panel.add(txtMon2);

        panel.add(new JLabel("Môn Thi 3:"));
        txtMon3 = new JTextField();
        panel.add(txtMon3);

        BaseButton btnSave = new BaseButton("Lưu");
        BaseButton btnCancel = new BaseButton("Hủy", AppConfig.COLOR_DANGER);

        btnSave.addActionListener(e -> saveAction());
        btnCancel.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(AppConfig.COLOR_WHITE);
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void saveAction() {
        // Kiểm tra rỗng cơ bản
        if (txtMaToHop.getText().trim().isEmpty() || txtTenToHop.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã và Tên tổ hợp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo object chứa dữ liệu mới
        toHopResult = new ToHopMonThi();
        toHopResult.setMaToHop(txtMaToHop.getText().trim());
        toHopResult.setTenToHop(txtTenToHop.getText().trim());
        toHopResult.setMon1(txtMon1.getText().trim());
        toHopResult.setMon2(txtMon2.getText().trim());
        toHopResult.setMon3(txtMon3.getText().trim());

        dispose(); // Đóng form
    }

    // Hàm để lấy dữ liệu sau khi cửa sổ đóng lại
    public ToHopMonThi getToHopResult() {
        return toHopResult;
    }
}