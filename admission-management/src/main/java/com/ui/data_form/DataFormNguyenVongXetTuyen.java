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

public class DataFormNguyenVongXetTuyen extends JDialog {

    public JTextField txtIdNv;
    public JTextField txtCccd;
    public JTextField txtMaNganh;
    public JTextField txtThuTu;
    public JTextField txtDiemThXT;
    public JTextField txtDiemUuTienQD;
    public JTextField txtDiemCong;
    public JTextField txtDiemXetTuyen;
    public JTextField txtKetQua;
    public JTextField txtNvKeys;
    public JTextField txtPhuongThuc;
    public JTextField txtTtThm;

    public BaseButton btnSave;
    public BaseButton btnCancel;

    private final Color BG_COLOR = new Color(245,247,249);
    private final Color LABEL_COLOR = new Color(52,73,94);
    private final Color BORDER_COLOR = new Color(220,223,225);

    public DataFormNguyenVongXetTuyen() {

        setTitle("Thêm mới nguyện vọng xét tuyển");
        setSize(520,720);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10,10));

        getContentPane().setBackground(BG_COLOR);

        JPanel formPanel = new JPanel(new GridLayout(12,1,12,12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20,25,10,25));
        formPanel.setBackground(BG_COLOR);

        txtIdNv = new JTextField();
        txtCccd = new JTextField();
        txtMaNganh = new JTextField();
        txtThuTu = new JTextField();
        txtDiemThXT = new JTextField();
        txtDiemUuTienQD = new JTextField();
        txtDiemCong = new JTextField();
        txtDiemXetTuyen = new JTextField();
        txtKetQua = new JTextField();
        txtNvKeys = new JTextField();
        txtPhuongThuc = new JTextField();
        txtTtThm = new JTextField();

        styleTextField(txtIdNv);
        styleTextField(txtCccd);
        styleTextField(txtMaNganh);
        styleTextField(txtThuTu);
        styleTextField(txtDiemThXT);
        styleTextField(txtDiemUuTienQD);
        styleTextField(txtDiemCong);
        styleTextField(txtDiemXetTuyen);
        styleTextField(txtKetQua);
        styleTextField(txtNvKeys);
        styleTextField(txtPhuongThuc);
        styleTextField(txtTtThm);

        formPanel.add(createRow("ID Nguyện Vọng", txtIdNv));
        formPanel.add(createRow("TS CCCD", txtCccd));
        formPanel.add(createRow("Mã Ngành", txtMaNganh));
        formPanel.add(createRow("Thứ Tự NV", txtThuTu));
        formPanel.add(createRow("Điểm THXT", txtDiemThXT));
        formPanel.add(createRow("Điểm Ưu Tiên QĐ", txtDiemUuTienQD));
        formPanel.add(createRow("Điểm Cộng", txtDiemCong));
        formPanel.add(createRow("Điểm Xét Tuyển", txtDiemXetTuyen));
        formPanel.add(createRow("Kết Quả", txtKetQua));
        formPanel.add(createRow("NV Keys", txtNvKeys));
        formPanel.add(createRow("Phương Thức", txtPhuongThuc));
        formPanel.add(createRow("TT THM", txtTtThm));

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