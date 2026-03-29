package com.ui.panel;

import com.controller.ThiSinhXetTuyen25Controller;
import com.entity.ThiSinhXetTuyen25;
import com.service.ThiSinhXetTuyen25Service;
import com.ui.common.BaseButton;
import com.utils.ValidateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditExamineeDialog extends JDialog {

    private final ThiSinhXetTuyen25Controller controller = new ThiSinhXetTuyen25Controller();
    private JTextField txtFirstName, txtEmail, txtPhone, txtSurName,
    txtAddress, txtDateOfBirth, txtGender, txtDoiTuongUuTien, txtCCCD, txtPassword, txtSBD, txtId, txtUpdateAt;
    private Integer userId;
    private String[] options = {"Nam", "Nữ", "Khác"};
    private JComboBox<String> comboBoxGender;

    public EditExamineeDialog(Integer id) {
        this.userId = id;

        setTitle("Sửa Thí Sinh");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setModal(true);

        initComponents();
        loadData();
    }

    private void initComponents() {
        txtFirstName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtSurName = new JTextField();
        txtAddress = new JTextField();
        txtDateOfBirth = new JTextField();
        comboBoxGender = new JComboBox<>(options);
        txtDoiTuongUuTien = new JTextField();
        txtCCCD = new JTextField();
        txtPassword = new JTextField();
        txtSBD = new JTextField();
        txtId = new JTextField();
        txtUpdateAt = new JTextField();
        txtId.setEditable(false);
        txtUpdateAt.setEditable(false);

        BaseButton btnSave = new BaseButton("Lưu");
        BaseButton btnCancel = new BaseButton("Hủy");
        btnSave.addActionListener(e -> updateUser());
        btnCancel.addActionListener(e -> dispose());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 2, 10, 10));

        mainPanel.add(new JLabel("ID:"));
        mainPanel.add(txtId);
        mainPanel.add(new JLabel("Họ:"));
        mainPanel.add(txtSurName);
        mainPanel.add(new JLabel("Tên:"));
        mainPanel.add(txtFirstName);
        mainPanel.add(new JLabel("Email:"));
        mainPanel.add(txtEmail);
        mainPanel.add(new JLabel("Số điện thoại:"));
        mainPanel.add(txtPhone);
        mainPanel.add(new JLabel("Ngày sinh:"));
        mainPanel.add(txtDateOfBirth);
        mainPanel.add(new JLabel("Giới tính:"));
        mainPanel.add(comboBoxGender);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(6, 2, 10, 10));

        panel2.add(new JLabel("Địa chỉ:"));
        panel2.add(txtAddress);
        panel2.add(new JLabel("CCCD:"));
        panel2.add(txtCCCD);
        panel2.add(new JLabel("Mật khẩu:"));
        panel2.add(txtPassword);
        panel2.add(new JLabel("SBD:"));
        panel2.add(txtSBD);
        panel2.add(new JLabel("Đối tượng ưu tiên:"));
        panel2.add(txtDoiTuongUuTien);
        panel2.add(new JLabel("Cập nhật lúc:"));
        panel2.add(txtUpdateAt);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(mainPanel, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        ThiSinhXetTuyen25 ts = controller.getById(userId);

        if (ts != null) {
            txtId.setText(ts.getIdThiSinh().toString());
            txtSurName.setText(ts.getHo() != null ? ts.getHo() : "");
            txtFirstName.setText(ts.getTen() != null ? ts.getTen() : "");
            txtEmail.setText(ts.getEmail() != null ? ts.getEmail() : "");
            txtPhone.setText(ts.getDienThoai() != null ? ts.getDienThoai() : "");
            txtDateOfBirth.setText(ts.getNgaySinh() != null ? ts.getNgaySinh() : "");
            comboBoxGender.setSelectedItem(ts.getGioiTinh() != null ? ts.getGioiTinh() : "Khác");
            txtAddress.setText(ts.getNoiSinh() != null ? ts.getNoiSinh() : "");
            txtCCCD.setText(ts.getCccd() != null ? ts.getCccd() : "");
            txtPassword.setText(ts.getPassword() != null ? ts.getPassword() : "");
            txtSBD.setText(ts.getSoBaoDanh() != null ? ts.getSoBaoDanh() : "");
            txtDoiTuongUuTien.setText(ts.getDoiTuong() != null ? ts.getDoiTuong() : "");
            txtUpdateAt.setText(ts.getUpdatedAt() != null ? formatDate(ts.getUpdatedAt()) : "");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thí sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void updateUser() {
        try {
            if (txtSurName.getText().trim().isEmpty()) {
                showError("Họ không được để trống"); return;
            }
            if (txtFirstName.getText().trim().isEmpty()) {
                showError("Tên không được để trống"); return;
            }
            if (txtEmail.getText().trim().isEmpty()) {
                showError("Email không được để trống"); return;
            }
            if (!ValidateUtil.isValidEmail(txtEmail.getText().trim())) {
                showError("Email không hợp lệ"); return;
            }
            if (txtPhone.getText().trim().isEmpty()) {
                showError("Số điện thoại không được để trống"); return;
            }
            if (!ValidateUtil.isNumber(txtPhone.getText().trim())) {
                showError("Số điện thoại phải là số"); return;
            }
            if (!ValidateUtil.isValidPhoneNumber(txtPhone.getText().trim())) {
                showError("Số điện thoại phải có số 0 và 10 chữ số"); return;
            }
            if (txtDateOfBirth.getText().trim().isEmpty()) {
                showError("Ngày sinh không được để trống"); return;
            }
            if (!ValidateUtil.isValidDate(txtDateOfBirth.getText().trim())) {
                showError("Ngày sinh phải có định dạng dd/MM/yyyy"); return;
            }
            if (txtAddress.getText().trim().isEmpty()) {
                showError("Nơi sinh không được để trống"); return;
            }
            if (txtCCCD.getText().trim().isEmpty()) {
                showError("CCCD không được để trống"); return;
            }
            if (!ValidateUtil.isNumber(txtCCCD.getText().trim())) {
                showError("CCCD phải là số"); return;
            }
            if (txtPassword.getText().trim().isEmpty()) {
                showError("Mật khẩu không được để trống"); return;
            }
            if (txtSBD.getText().trim().isEmpty()) {
                showError("Số báo danh không được để trống"); return;
            }
            if (!ValidateUtil.isNumber(txtSBD.getText().trim())) {
                showError("Số báo danh phải là số"); return;
            }
            if (txtDoiTuongUuTien.getText().trim().isEmpty()) {
                showError("Đối tượng ưu tiên không được để trống"); return;
            }

            ThiSinhXetTuyen25 ts = controller.getById(userId);

            if (ts == null) {
                JOptionPane.showMessageDialog(this, "Thí sinh không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ts.setHo(txtSurName.getText().trim());
            ts.setTen(txtFirstName.getText().trim());
            ts.setEmail(txtEmail.getText().trim());
            ts.setDienThoai(txtPhone.getText().trim());
            ts.setNgaySinh(txtDateOfBirth.getText().trim());
            ts.setGioiTinh(comboBoxGender.getSelectedItem().toString());
            ts.setNoiSinh(txtAddress.getText().trim());
            ts.setCccd(txtCCCD.getText().trim());
            ts.setPassword(txtPassword.getText().trim());
            ts.setSoBaoDanh(txtSBD.getText().trim());
            ts.setDoiTuong(txtDoiTuongUuTien.getText().trim());
            ts.setUpdatedAt(LocalDate.now());
            controller.update(ts);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date == null ? "" : date.format(formatter);
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }


}
