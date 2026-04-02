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
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        initComponents();
        loadData();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(300, 35));

        txtSurName = new JTextField();
        txtSurName.setPreferredSize(new Dimension(300, 35));

        txtFirstName = new JTextField();
        txtFirstName.setPreferredSize(new Dimension(300, 35));

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(300, 35));

        txtPhone = new JTextField();
        txtPhone.setPreferredSize(new Dimension(300, 35));

        txtDateOfBirth = new JTextField();
        txtDateOfBirth.setPreferredSize(new Dimension(300, 35));

        comboBoxGender = new JComboBox<>(options);
        comboBoxGender.setPreferredSize(new Dimension(300, 35));

        txtAddress = new JTextField();
        txtAddress.setPreferredSize(new Dimension(300, 35));

        txtCCCD = new JTextField();
        txtCCCD.setPreferredSize(new Dimension(300, 35));

        txtPassword = new JTextField();
        txtPassword.setPreferredSize(new Dimension(300, 35));

        txtSBD = new JTextField();
        txtSBD.setPreferredSize(new Dimension(300, 35));

        txtDoiTuongUuTien = new JTextField();
        txtDoiTuongUuTien.setPreferredSize(new Dimension(300, 35));

        txtUpdateAt = new JTextField();
        txtUpdateAt.setEditable(false);
        txtUpdateAt.setPreferredSize(new Dimension(300, 35));

        BaseButton btnSave = new BaseButton("Lưu");
        BaseButton btnCancel = new BaseButton("Hủy");
        btnSave.addActionListener(e -> updateUser());
        btnCancel.addActionListener(e -> dispose());

        // Panel chính với padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Tạo các hàng input
        mainPanel.add(createInputRow("ID:", txtId));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Họ:", txtSurName));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Tên:", txtFirstName));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Email:", txtEmail));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Số điện thoại:", txtPhone));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Ngày sinh:", txtDateOfBirth));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Giới tính:", comboBoxGender));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Địa chỉ:", txtAddress));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("CCCD:", txtCCCD));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Mật khẩu:", txtPassword));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Số báo danh:", txtSBD));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Đối tượng ưu tiên:", txtDoiTuongUuTien));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createInputRow("Cập nhật lúc:", txtUpdateAt));
        mainPanel.add(Box.createVerticalGlue());

        // Tạo JScrollPane để có thể cuộn nếu cần
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        // Panel cho buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputRow(String labelText, JComponent inputComponent) {
        JPanel row = new JPanel();
        row.setLayout(new BorderLayout(15, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 35));
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        inputComponent.setFont(new Font("Arial", Font.PLAIN, 14));

        row.add(label, BorderLayout.WEST);
        row.add(inputComponent, BorderLayout.CENTER);

        return row;
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
