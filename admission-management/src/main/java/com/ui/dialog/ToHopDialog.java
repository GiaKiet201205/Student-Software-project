package com.ui.dialog;

import com.entity.ToHopMonThi;
import javax.swing.*;
import java.awt.*;

public class ToHopDialog extends JDialog {
    private JTextField txtMaToHop, txtTenToHop, txtMon1, txtMon2, txtMon3;
    private ToHopMonThi resultToHop;
    private ToHopMonThi editData;

    public ToHopDialog(Window owner, ToHopMonThi editData) {
        super(owner, editData == null ? "Thêm Tổ Hợp" : "Sửa Tổ Hợp", ModalityType.APPLICATION_MODAL);
        this.editData = editData;
        initializeComponents();
        if (editData != null) fillData();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Mã tổ hợp:"));
        txtMaToHop = new JTextField(20);
        panel.add(txtMaToHop);

        panel.add(new JLabel("Tên tổ hợp:"));
        txtTenToHop = new JTextField(20);
        panel.add(txtTenToHop);

        panel.add(new JLabel("Môn 1:"));
        txtMon1 = new JTextField(10);
        panel.add(txtMon1);

        panel.add(new JLabel("Môn 2:"));
        txtMon2 = new JTextField(10);
        panel.add(txtMon2);

        panel.add(new JLabel("Môn 3:"));
        txtMon3 = new JTextField(10);
        panel.add(txtMon3);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void fillData() {
        txtMaToHop.setText(editData.getMaToHop());
        txtTenToHop.setText(editData.getTenToHop());
        txtMon1.setText(editData.getMon1());
        txtMon2.setText(editData.getMon2());
        txtMon3.setText(editData.getMon3());
        txtMaToHop.setEditable(false);
    }

    private void handleSave() {
        if (txtMaToHop.getText().isEmpty() || txtTenToHop.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã và Tên không được để trống!");
            return;
        }

        resultToHop = new ToHopMonThi();
        
        if (editData != null) {
            resultToHop.setIdToHop(editData.getIdToHop());
        }

        resultToHop.setMaToHop(txtMaToHop.getText().trim());
        resultToHop.setTenToHop(txtTenToHop.getText().trim());
        resultToHop.setMon1(txtMon1.getText().trim());
        resultToHop.setMon2(txtMon2.getText().trim());
        resultToHop.setMon3(txtMon3.getText().trim());

        dispose();
    }

    public ToHopMonThi getToHopResult() {
        return resultToHop;
    }
}