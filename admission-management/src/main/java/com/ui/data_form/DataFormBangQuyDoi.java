package com.ui.data_form;

import com.controller.BangQuyDoiController;
import com.entity.BangQuyDoi;
import com.ui.common.BaseButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Dialog dùng chung cho Thêm và Sửa bản ghi BangQuyDoi.
 * Cách dùng:
 *   DataFormBangQuyDoi form = new DataFormBangQuyDoi(parentFrame, null, controller);          // thêm mới
 *   DataFormBangQuyDoi form = new DataFormBangQuyDoi(parentFrame, selectedRecord, controller); // sửa
 *   if (form.isSaved()) { // reload table }
 */
public class DataFormBangQuyDoi extends JDialog {

    private final BangQuyDoiController controller;
    private final BangQuyDoi existing; // null = chế độ thêm mới
    private boolean saved = false;

    // Form fields
    private JComboBox<String> cbPhuongThuc;
    private JTextField tfToHop;
    private JTextField tfMon;
    private JTextField tfDiemA;
    private JTextField tfDiemB;
    private JTextField tfDiemC;
    private JTextField tfDiemD;
    private JTextField tfMaQuyDoi;
    private JTextField tfPhanVi;

    private static final String[] PHUONG_THUC_OPTIONS = {"DGNL", "VSAT", "THPT"};
    private static final Color PRIMARY_COLOR = new Color(0x1565C0);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    public DataFormBangQuyDoi(Frame parent, BangQuyDoi existing, BangQuyDoiController controller) {
        super(parent, existing == null ? "Thêm bản ghi quy đổi" : "Sửa bản ghi quy đổi", true);
        this.existing = existing;
        this.controller = controller;
        initUI();
        if (existing != null) populateFields();
        pack();
        setMinimumSize(new Dimension(480, 0));
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Color.WHITE);

        // ---- Header ----
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel title = new JLabel(existing == null ? "➕ Thêm bản ghi quy đổi" : "✏️ Sửa bản ghi quy đổi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // ---- Form fields ----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Phương thức (combobox)
        cbPhuongThuc = new JComboBox<>(PHUONG_THUC_OPTIONS);
        cbPhuongThuc.setFont(FIELD_FONT);
        cbPhuongThuc.addActionListener(e -> onPhuongThucChange());

        // Tổ hợp
        tfToHop = createField("VD: A00, D01 (để trống nếu VSAT)");
        // Môn
        tfMon = createField("VD: TO, VA, LI (để trống nếu ĐGNL)");
        // Điểm A, B, C, D
        tfDiemA = createField("VD: 800.00");
        tfDiemB = createField("VD: 850.00");
        tfDiemC = createField("VD: 22.50");
        tfDiemD = createField("VD: 23.00");
        // Mã quy đổi
        tfMaQuyDoi = createField("VD: DGNL_A01_1");
        // Phân vị
        tfPhanVi = createField("VD: 1, 2, ... 99");

        addRow(form, gbc, 0, "Phương thức *", cbPhuongThuc);
        addRow(form, gbc, 1, "Tổ hợp", tfToHop);
        addRow(form, gbc, 2, "Môn", tfMon);
        addRow(form, gbc, 3, "Điểm A (dải dưới nguồn) *", tfDiemA);
        addRow(form, gbc, 4, "Điểm B (dải trên nguồn) *", tfDiemB);
        addRow(form, gbc, 5, "Điểm C (dải dưới đích) *", tfDiemC);
        addRow(form, gbc, 6, "Điểm D (dải trên đích) *", tfDiemD);
        addRow(form, gbc, 7, "Mã quy đổi *", tfMaQuyDoi);
        addRow(form, gbc, 8, "Phân vị", tfPhanVi);

        root.add(form, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xDDDDDD)));

        BaseButton btnCancel = new BaseButton("Hủy");
        btnCancel.setBackground(new Color(0xEEEEEE));
        btnCancel.setForeground(Color.DARK_GRAY);
        btnCancel.addActionListener(e -> dispose());

        BaseButton btnSave = new BaseButton(existing == null ? "Thêm" : "Lưu");
        btnSave.setBackground(PRIMARY_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> onSave());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        root.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(root);

        // Trigger initial state
        onPhuongThucChange();
    }

    private void onPhuongThucChange() {
        String pt = (String) cbPhuongThuc.getSelectedItem();
        if ("DGNL".equals(pt)) {
            // ĐGNL: có tổ hợp, không có môn
            tfToHop.setEnabled(true);
            tfMon.setEnabled(false);
            tfMon.setText("");
        } else if ("VSAT".equals(pt)) {
            // VSAT: không có tổ hợp, có môn
            tfToHop.setEnabled(false);
            tfToHop.setText("");
            tfMon.setEnabled(true);
        } else {
            // THPT
            tfToHop.setEnabled(true);
            tfMon.setEnabled(true);
        }
    }

    private void populateFields() {

        for (int i = 0; i < PHUONG_THUC_OPTIONS.length; i++) {

            if (PHUONG_THUC_OPTIONS[i].equals(existing.getPhuongthuc())) {

                cbPhuongThuc.setSelectedIndex(i);
                break;
            }
        }

        tfToHop.setText(
                nullToEmpty(existing.getTohop())
        );

        tfMon.setText(
                nullToEmpty(existing.getMon())
        );

        tfDiemA.setText(
                existing.getDiemA() != null
                        ? existing.getDiemA().toPlainString()
                        : ""
        );

        tfDiemB.setText(
                existing.getDiemB() != null
                        ? existing.getDiemB().toPlainString()
                        : ""
        );

        tfDiemC.setText(
                existing.getDiemC() != null
                        ? existing.getDiemC().toPlainString()
                        : ""
        );

        tfDiemD.setText(
                existing.getDiemD() != null
                        ? existing.getDiemD().toPlainString()
                        : ""
        );

        tfMaQuyDoi.setText(
                nullToEmpty(existing.getMaQuydoi())
        );

        tfPhanVi.setText(
                nullToEmpty(existing.getPhanVi())
        );

        onPhuongThucChange();
    }

    private void onSave() {

        try {

            BangQuyDoi bqd = (existing != null)
                    ? existing
                    : new BangQuyDoi();

            bqd.setPhuongthuc(
                    (String) cbPhuongThuc.getSelectedItem()
            );

            bqd.setTohop(
                    emptyToNull(tfToHop.getText())
            );

            bqd.setMon(
                    emptyToNull(tfMon.getText())
            );

            bqd.setDiemA(
                    parseBD(tfDiemA.getText(), "Điểm A")
            );

            bqd.setDiemB(
                    parseBD(tfDiemB.getText(), "Điểm B")
            );

            bqd.setDiemC(
                    parseBD(tfDiemC.getText(), "Điểm C")
            );

            bqd.setDiemD(
                    parseBD(tfDiemD.getText(), "Điểm D")
            );

            bqd.setMaQuydoi(
                    tfMaQuyDoi.getText().trim()
            );

            bqd.setPhanVi(
                    emptyToNull(tfPhanVi.getText())
            );

            String error;

            if (existing == null) {

                error = controller.add(bqd);

            } else {

                error = controller.update(bqd);
            }

            if (error != null) {

                JOptionPane.showMessageDialog(
                        this,
                        error,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            saved = true;

            JOptionPane.showMessageDialog(
                    this,
                    existing == null
                            ? "Thêm dữ liệu thành công!"
                            : "Cập nhật dữ liệu thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Đã xảy ra lỗi: " + e.getMessage(),
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ===================== HELPERS =====================

    private JTextField createField(String placeholder) {
        JTextField tf = new JTextField(20);
        tf.setFont(FIELD_FONT);
        tf.setToolTipText(placeholder);
        return tf;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        lbl.setPreferredSize(new Dimension(220, 28));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private BigDecimal parseBD(String text, String fieldName) {
        text = text.trim().replace(",", ".");
        if (text.isEmpty()) throw new IllegalArgumentException(fieldName + " không được để trống.");
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ: '" + text + "'");
        }
    }

    private String nullToEmpty(String s) { return s == null ? "" : s; }
    private String emptyToNull(String s) { s = s.trim(); return s.isEmpty() ? null : s; }

    public boolean isSaved() { return saved; }
}