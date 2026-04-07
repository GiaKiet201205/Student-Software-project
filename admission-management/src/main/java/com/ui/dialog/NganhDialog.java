package com.ui.dialog;

import com.config.AppConfig;
import com.entity.Nganh;
import com.ui.common.BaseButton;

import javax.swing.*;
import java.awt.*;

public class NganhDialog extends JDialog {
    private JTextField txtMaNganh, txtTenNganh, txtToHopGoc;
    private JTextField txtChiTieu, txtDiemSan, txtDiemTrungTuyen;
    private JComboBox<String> cbTuyenThang, cbDgnl, cbThpt, cbVsat;
    private JTextField txtSlXtt, txtSlDgnl, txtSlVsat, txtSlThpt;

    private Nganh nganhResult = null;
    private boolean isEditMode = false;

    public NganhDialog(Window owner, Nganh nganhEdit) {
        super(owner, "Thông tin Ngành Tuyển Sinh", Dialog.ModalityType.APPLICATION_MODAL);
        this.isEditMode = (nganhEdit != null);

        initComponents();

        if (isEditMode) {
            txtMaNganh.setText(nganhEdit.getMaNganh());
            txtMaNganh.setEditable(false); // Khóa không cho sửa Mã Ngành
            txtTenNganh.setText(nganhEdit.getTenNganh());
            txtToHopGoc.setText(nganhEdit.getToHopGoc());
            
            txtChiTieu.setText(String.valueOf(nganhEdit.getChiTieu()));
            txtDiemSan.setText(String.valueOf(nganhEdit.getDiemSan()));
            txtDiemTrungTuyen.setText(String.valueOf(nganhEdit.getDiemTrungTuyen()));

            cbTuyenThang.setSelectedItem(nganhEdit.getTuyenThang());
            cbDgnl.setSelectedItem(nganhEdit.getDgnl());
            cbThpt.setSelectedItem(nganhEdit.getThpt());
            cbVsat.setSelectedItem(nganhEdit.getVsat());

            txtSlXtt.setText(String.valueOf(nganhEdit.getSlXtt()));
            txtSlDgnl.setText(String.valueOf(nganhEdit.getSlDgnl()));
            txtSlVsat.setText(String.valueOf(nganhEdit.getSlVsat()));
            txtSlThpt.setText(String.valueOf(nganhEdit.getSlThpt()));
        }

        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        // Tạo lưới 7 hàng, 4 cột (mỗi bên là 1 cặp Label - Input)
        JPanel panel = new JPanel(new GridLayout(7, 4, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(AppConfig.COLOR_WHITE);

        String[] yesNoOptions = {"Y", "N"};

        // Hàng 1
        panel.add(new JLabel("Mã Ngành:")); txtMaNganh = new JTextField(); panel.add(txtMaNganh);
        panel.add(new JLabel("Tên Ngành:")); txtTenNganh = new JTextField(); panel.add(txtTenNganh);
        // Hàng 2
        panel.add(new JLabel("Tổ Hợp Gốc:")); txtToHopGoc = new JTextField(); panel.add(txtToHopGoc);
        panel.add(new JLabel("Chỉ Tiêu:")); txtChiTieu = new JTextField("0"); panel.add(txtChiTieu);
        // Hàng 3
        panel.add(new JLabel("Điểm Sàn:")); txtDiemSan = new JTextField("0.0"); panel.add(txtDiemSan);
        panel.add(new JLabel("Điểm Trúng Tuyển:")); txtDiemTrungTuyen = new JTextField("0.0"); panel.add(txtDiemTrungTuyen);
        // Hàng 4
        panel.add(new JLabel("Tuyển Thẳng (Y/N):")); cbTuyenThang = new JComboBox<>(yesNoOptions); panel.add(cbTuyenThang);
        panel.add(new JLabel("ĐGNL (Y/N):")); cbDgnl = new JComboBox<>(yesNoOptions); panel.add(cbDgnl);
        // Hàng 5
        panel.add(new JLabel("THPT (Y/N):")); cbThpt = new JComboBox<>(yesNoOptions); panel.add(cbThpt);
        panel.add(new JLabel("V-SAT (Y/N):")); cbVsat = new JComboBox<>(yesNoOptions); panel.add(cbVsat);
        // Hàng 6
        panel.add(new JLabel("SL Xét Tuyển Thẳng:")); txtSlXtt = new JTextField("0"); panel.add(txtSlXtt);
        panel.add(new JLabel("SL ĐGNL:")); txtSlDgnl = new JTextField("0"); panel.add(txtSlDgnl);
        // Hàng 7
        panel.add(new JLabel("SL V-SAT:")); txtSlVsat = new JTextField("0"); panel.add(txtSlVsat);
        panel.add(new JLabel("SL THPT:")); txtSlThpt = new JTextField("0"); panel.add(txtSlThpt);

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
        if (txtMaNganh.getText().trim().isEmpty() || txtTenNganh.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã và Tên ngành!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            nganhResult = new Nganh();
            nganhResult.setMaNganh(txtMaNganh.getText().trim());
            nganhResult.setTenNganh(txtTenNganh.getText().trim());
            nganhResult.setToHopGoc(txtToHopGoc.getText().trim());
            
            // Ép kiểu các trường số
nganhResult.setChiTieu(Integer.parseInt(txtChiTieu.getText().trim()));
            nganhResult.setDiemSan(new java.math.BigDecimal(txtDiemSan.getText().trim()));
            nganhResult.setDiemTrungTuyen(new java.math.BigDecimal(txtDiemTrungTuyen.getText().trim()));

            nganhResult.setTuyenThang((String) cbTuyenThang.getSelectedItem());
            nganhResult.setDgnl((String) cbDgnl.getSelectedItem());
            nganhResult.setThpt((String) cbThpt.getSelectedItem());
            nganhResult.setVsat((String) cbVsat.getSelectedItem());
            
            nganhResult.setSlXtt(Integer.parseInt(txtSlXtt.getText().trim()));
            nganhResult.setSlDgnl(Integer.parseInt(txtSlDgnl.getText().trim()));
            nganhResult.setSlVsat(Integer.parseInt(txtSlVsat.getText().trim()));
            
            // Theo cấu trúc database init.sql của bạn, trường sl_thpt là kiểu varchar(45) thay vì int
            // Nên mình để set thẳng text vào nhé
            nganhResult.setSlThpt(txtSlThpt.getText().trim());

            dispose(); // Đóng cửa sổ sau khi gán xong

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho Chỉ tiêu, Điểm và Số lượng!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            nganhResult = null; // Tránh lưu rác
        }
    }

    public Nganh getNganhResult() {
        return nganhResult;
    }
}