package com.ui.data_form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.ui.common.BaseButton;

public class DataFormDiemThi extends JDialog {

    // Khai báo 20 trường dữ liệu
    public JTextField txtId, txtCccd, txtSbd, txtPhuongThuc;
    public JTextField txtTO, txtLI, txtHO, txtSI, txtSU, txtDI, txtVA;
    public JTextField txtN1_THI, txtN1_CC, txtCNCN, txtCNNN, txtTI, txtKTPL, txtNL1, txtNK1, txtNK2, txtNK3,txtNK4;

    public BaseButton btnSave, btnCancel;

    public DataFormDiemThi() {
        setTitle("Cập nhật Điểm Thí Sinh");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 249));

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 20, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        formPanel.setOpaque(false);

        txtId = createTextField(); txtCccd = createTextField();
        txtSbd = createTextField(); txtPhuongThuc = createTextField();
        txtTO = createTextField(); txtLI = createTextField();
        txtHO = createTextField(); txtSI = createTextField();
        txtSU = createTextField(); txtDI = createTextField();
        txtVA = createTextField(); txtN1_THI = createTextField();
        txtN1_CC = createTextField(); txtCNCN = createTextField();
        txtCNNN = createTextField(); txtTI = createTextField();
        txtKTPL = createTextField(); txtNL1 = createTextField();
        txtNK1 = createTextField(); txtNK2 = createTextField();
        txtNK3 = createTextField(); txtNK4 = createTextField();

        formPanel.add(createRow("ID Điểm Thi", txtId));
        formPanel.add(createRow("CCCD", txtCccd));
        formPanel.add(createRow("Số Báo Danh", txtSbd));
        formPanel.add(createRow("Phương Thức", txtPhuongThuc));
        formPanel.add(createRow("Điểm Toán", txtTO));
        formPanel.add(createRow("Điểm Vật Lý", txtLI));
        formPanel.add(createRow("Điểm Hóa Học", txtHO));
        formPanel.add(createRow("Điểm Sinh Học", txtSI));
        formPanel.add(createRow("Điểm Ngữ Văn", txtVA));
        formPanel.add(createRow("Điểm Lịch Sử", txtSU));
        formPanel.add(createRow("Điểm Địa Lý", txtDI));
        formPanel.add(createRow("Điểm N1 (Thi)", txtN1_THI));
        formPanel.add(createRow("Điểm N1 (Chứng chỉ)", txtN1_CC));
        formPanel.add(createRow("Điểm CNCN", txtCNCN));
        formPanel.add(createRow("Điểm CNNN", txtCNNN));
        formPanel.add(createRow("Điểm Tin Học", txtTI));
        formPanel.add(createRow("Điểm KTPL", txtKTPL));
        formPanel.add(createRow("Điểm DGNL 1", txtNL1));
        formPanel.add(createRow("Điểm Năng khiếu 1", txtNK1));
        formPanel.add(createRow("Điểm Năng khiếu 2", txtNK2));
        formPanel.add(createRow("Điểm Năng khiếu 3",txtNK3));
        formPanel.add(createRow("Điểm năng khiếu 4", txtNK4));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));
        btnSave = new BaseButton("Lưu", new Color(46, 204, 113));
        btnCancel = new BaseButton("Hủy", new Color(231, 76, 60));

        btnSave.setPreferredSize(new Dimension(100, 32));
        btnCancel.setPreferredSize(new Dimension(100, 32));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(Color.WHITE);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 223, 225)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return txt;
    }

    private JPanel createRow(String label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 34));
        lbl.setForeground(new Color(52, 73, 94));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }
}