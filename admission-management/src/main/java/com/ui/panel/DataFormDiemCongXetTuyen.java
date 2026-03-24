package com.ui.panel;

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

public class DataFormDiemCongXetTuyen extends JDialog {

    public JTextField txtIdDiemCong;
    public JTextField txtCccd;
    public JTextField txtMaNganh;
    public JTextField txtMaToHop;
    public JTextField txtPhuongThuc;
    public JTextField txtDiemCC;
    public JTextField txtDiemUtxt;
    public JTextField txtDiemTong;
    public JTextField txtGhiChu;
    public JTextField txtDcKeys;

    public BaseButton btnSave;
    public BaseButton btnCancel;

    private final Color BG_COLOR = new Color(245,247,249);
    private final Color LABEL_COLOR = new Color(52,73,94);
    private final Color BORDER_COLOR = new Color(220,223,225);

    public DataFormDiemCongXetTuyen() {

        setTitle("Thêm mới điểm cộng");
        setSize(520,650);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10,10));

        getContentPane().setBackground(BG_COLOR);

        JPanel formPanel = new JPanel(new GridLayout(10,1,12,12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20,25,10,25));
        formPanel.setBackground(BG_COLOR);

        txtIdDiemCong = new JTextField();
        txtCccd = new JTextField();
        txtMaNganh = new JTextField();
        txtMaToHop = new JTextField();
        txtPhuongThuc = new JTextField();
        txtDiemCC = new JTextField();
        txtDiemUtxt = new JTextField();
        txtDiemTong = new JTextField();
        txtGhiChu = new JTextField();
        txtDcKeys = new JTextField();

        styleTextField(txtIdDiemCong);
        styleTextField(txtCccd);
        styleTextField(txtMaNganh);
        styleTextField(txtMaToHop);
        styleTextField(txtPhuongThuc);
        styleTextField(txtDiemCC);
        styleTextField(txtDiemUtxt);
        styleTextField(txtDiemTong);
        styleTextField(txtGhiChu);
        styleTextField(txtDcKeys);

        formPanel.add(createRow("ID Điểm Cộng", txtIdDiemCong));
        formPanel.add(createRow("TS CCCD", txtCccd));
        formPanel.add(createRow("Mã Ngành", txtMaNganh));
        formPanel.add(createRow("Mã Tổ Hợp", txtMaToHop));
        formPanel.add(createRow("Phương Thức", txtPhuongThuc));
        formPanel.add(createRow("Điểm CC", txtDiemCC));
        formPanel.add(createRow("Điểm Utxt", txtDiemUtxt));
        formPanel.add(createRow("Điểm Tổng", txtDiemTong));
        formPanel.add(createRow("Ghi Chú", txtGhiChu));
        formPanel.add(createRow("DC Keys", txtDcKeys));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15,10,25,10));
        btnSave = new BaseButton("Lưu", new Color(46,204,113));
        btnCancel = new BaseButton("Hủy", new Color(231,76,60));

        btnSave.setPreferredSize(new Dimension(100,32));
        btnCancel.setPreferredSize(new Dimension(100,32));

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
    }

    private JPanel createRow(String label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(10,0));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150,34));
        lbl.setForeground(LABEL_COLOR);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    private void styleTextField(JTextField txt){
        txt.setPreferredSize(new Dimension(260,35));
        txt.setBackground(Color.WHITE);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(6,10,6,10)
        ));
    }
}