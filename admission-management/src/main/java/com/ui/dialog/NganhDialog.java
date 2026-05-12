package com.ui.dialog;

import com.entity.Nganh;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class NganhDialog extends JDialog {
    private JTextField txtMaNganh, txtTenNganh, txtToHopGoc, txtChiTieu, txtDiemSan, txtDiemChuan;
    
    private JComboBox<String> cbTuyenThang, cbDgnl, cbThpt, cbVsat;
    private JTextField txtSlXtt, txtSlDgnl, txtSlThpt, txtSlVsat;
    
    private Nganh resultNganh;
    private Nganh editData;

    public NganhDialog(Window owner, Nganh editData) {
        super(owner, editData == null ? "Thêm Ngành Mới" : "Sửa Thông Tin Ngành", ModalityType.APPLICATION_MODAL);
        this.editData = editData;
        initializeComponents();
        if (editData != null) fillData();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new GridLayout(14, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        panel.add(new JLabel("Mã ngành:"));
        txtMaNganh = new JTextField(20);
        panel.add(txtMaNganh);

        panel.add(new JLabel("Tên ngành:"));
        txtTenNganh = new JTextField(20);
        panel.add(txtTenNganh);

        panel.add(new JLabel("Tổ hợp gốc:"));
        txtToHopGoc = new JTextField(20);
        panel.add(txtToHopGoc);

        panel.add(new JLabel("Chỉ tiêu:"));
        txtChiTieu = new JTextField("0");
        panel.add(txtChiTieu);

        panel.add(new JLabel("Điểm sàn:"));
        txtDiemSan = new JTextField("0.0");
        panel.add(txtDiemSan);

        panel.add(new JLabel("Điểm chuẩn:"));
        txtDiemChuan = new JTextField("0.0");
        panel.add(txtDiemChuan);

        // 7-14: Các trường mở rộng (Dùng JComboBox cho đẹp)
        String[] optionsYN = {"Y", "N"};

        panel.add(new JLabel("Xét Tuyển Thẳng (Y/N):"));
        cbTuyenThang = new JComboBox<>(optionsYN);
        panel.add(cbTuyenThang);

        panel.add(new JLabel("SL Tuyển Thẳng:"));
        txtSlXtt = new JTextField("0");
        panel.add(txtSlXtt);

        panel.add(new JLabel("Xét ĐGNL (Y/N):"));
        cbDgnl = new JComboBox<>(optionsYN);
        panel.add(cbDgnl);

        panel.add(new JLabel("SL ĐGNL:"));
        txtSlDgnl = new JTextField("0");
        panel.add(txtSlDgnl);

        panel.add(new JLabel("Xét THPT (Y/N):"));
        cbThpt = new JComboBox<>(optionsYN);
        panel.add(cbThpt);

        panel.add(new JLabel("SL THPT:"));
        txtSlThpt = new JTextField("0");
        panel.add(txtSlThpt);

        panel.add(new JLabel("Xét VSAT (Y/N):"));
        cbVsat = new JComboBox<>(optionsYN);
        panel.add(cbVsat);

        panel.add(new JLabel("SL VSAT:"));
        txtSlVsat = new JTextField("0");
        panel.add(txtSlVsat);

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
        txtMaNganh.setText(editData.getMaNganh());
        txtTenNganh.setText(editData.getTenNganh());
        txtToHopGoc.setText(editData.getToHopGoc());
        txtChiTieu.setText(String.valueOf(editData.getChiTieu()));
        txtDiemSan.setText(editData.getDiemSan() != null ? editData.getDiemSan().toString() : "0.0");
        txtDiemChuan.setText(editData.getDiemTrungTuyen() != null ? editData.getDiemTrungTuyen().toString() : "0.0");
        
        cbTuyenThang.setSelectedItem(editData.getTuyenThang() != null ? editData.getTuyenThang() : "N");
        cbDgnl.setSelectedItem(editData.getDgnl() != null ? editData.getDgnl() : "N");
        cbThpt.setSelectedItem(editData.getThpt() != null ? editData.getThpt() : "N");
        cbVsat.setSelectedItem(editData.getVsat() != null ? editData.getVsat() : "N");

        txtSlXtt.setText(editData.getSlXtt() != null ? String.valueOf(editData.getSlXtt()) : "0");
        txtSlDgnl.setText(editData.getSlDgnl() != null ? String.valueOf(editData.getSlDgnl()) : "0");
        txtSlThpt.setText(editData.getSlThpt() != null ? editData.getSlThpt() : "0"); // Trong DB cột này là String
        txtSlVsat.setText(editData.getSlVsat() != null ? String.valueOf(editData.getSlVsat()) : "0");

        txtMaNganh.setEditable(false);
    }

    private void handleSave() {
        try {
            resultNganh = new Nganh();
            
            if (editData != null) {
                resultNganh.setIdNganh(editData.getIdNganh());
            }

            resultNganh.setMaNganh(txtMaNganh.getText().trim());
            resultNganh.setTenNganh(txtTenNganh.getText().trim());
            resultNganh.setToHopGoc(txtToHopGoc.getText().trim());
            
            resultNganh.setChiTieu(Integer.parseInt(txtChiTieu.getText().trim()));
            resultNganh.setDiemSan(new BigDecimal(txtDiemSan.getText().trim()));
            resultNganh.setDiemTrungTuyen(new BigDecimal(txtDiemChuan.getText().trim()));
            
            resultNganh.setTuyenThang((String) cbTuyenThang.getSelectedItem());
            resultNganh.setDgnl((String) cbDgnl.getSelectedItem());
            resultNganh.setThpt((String) cbThpt.getSelectedItem());
            resultNganh.setVsat((String) cbVsat.getSelectedItem());

            resultNganh.setSlXtt(Integer.parseInt(txtSlXtt.getText().trim()));
            resultNganh.setSlDgnl(Integer.parseInt(txtSlDgnl.getText().trim()));
            resultNganh.setSlVsat(Integer.parseInt(txtSlVsat.getText().trim()));
            resultNganh.setSlThpt(txtSlThpt.getText().trim()); // Entity khai báo cột này là String

            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: Các ô Chỉ tiêu, Điểm và Số lượng phải là SỐ!", "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Nganh getNganhResult() {
        return resultNganh;
    }
}