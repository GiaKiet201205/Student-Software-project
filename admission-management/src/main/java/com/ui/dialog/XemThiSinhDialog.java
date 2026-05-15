package com.ui.dialog;

import com.controller.ThiSinhXetTuyen25Controller;
import com.entity.ThiSinhXetTuyen25;
import com.ui.common.BaseButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XemThiSinhDialog extends JDialog {

    private final ThiSinhXetTuyen25Controller controller = new ThiSinhXetTuyen25Controller();
    private JTextField txtFirstName, txtEmail, txtPhone, txtSurName,
            txtAddress, txtDateOfBirth, txtGender, txtDoiTuongUuTien, txtCCCD, txtPassword, txtSBD, txtId, txtUpdateAt;
    private final Integer userId;

    public XemThiSinhDialog(Integer id, boolean modal) {
        this.userId = id;

        setTitle("Chi Tiết Thí Sinh");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setModal(modal);
        setResizable(false);

        initComponents();
        loadData();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setPreferredSize(new Dimension(300, 35));

        txtSurName = new JTextField();
        txtSurName.setEditable(false);
        txtSurName.setPreferredSize(new Dimension(300, 35));

        txtFirstName = new JTextField();
        txtFirstName.setEditable(false);
        txtFirstName.setPreferredSize(new Dimension(300, 35));

        txtEmail = new JTextField();
        txtEmail.setEditable(false);
        txtEmail.setPreferredSize(new Dimension(300, 35));

        txtPhone = new JTextField();
        txtPhone.setEditable(false);
        txtPhone.setPreferredSize(new Dimension(300, 35));

        txtDateOfBirth = new JTextField();
        txtDateOfBirth.setEditable(false);
        txtDateOfBirth.setPreferredSize(new Dimension(300, 35));

        txtGender = new JTextField();
        txtGender.setEditable(false);
        txtGender.setPreferredSize(new Dimension(300, 35));

        txtAddress = new JTextField();
        txtAddress.setEditable(false);
        txtAddress.setPreferredSize(new Dimension(300, 35));

        txtCCCD = new JTextField();
        txtCCCD.setEditable(false);
        txtCCCD.setPreferredSize(new Dimension(300, 35));

        txtPassword = new JTextField();
        txtPassword.setEditable(false);
        txtPassword.setPreferredSize(new Dimension(300, 35));

        txtSBD = new JTextField();
        txtSBD.setEditable(false);
        txtSBD.setPreferredSize(new Dimension(300, 35));

        txtDoiTuongUuTien = new JTextField();
        txtDoiTuongUuTien.setEditable(false);
        txtDoiTuongUuTien.setPreferredSize(new Dimension(300, 35));

        txtUpdateAt = new JTextField();
        txtUpdateAt.setEditable(false);
        txtUpdateAt.setPreferredSize(new Dimension(300, 35));

        BaseButton btnViewScore = new BaseButton("Xem điểm");
        BaseButton btnCancel = new BaseButton("Hủy");
        btnViewScore.addActionListener(e -> {
            viewScore();
        });
        btnCancel.addActionListener(e -> dispose());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

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

        mainPanel.add(createInputRow("Giới tính:", txtGender));
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

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(btnViewScore);
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
            txtGender.setText(ts.getGioiTinh() != null ? ts.getGioiTinh() : "");
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

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date == null ? "" : date.format(formatter);
    }

    private void viewScore() {
        String cccd = txtCCCD.getText();
        if (cccd == null || cccd.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "CCCD không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new DiemThiThiSinhDialog(cccd, true).setVisible(true);
    }
}
