package com.ui.data_form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.ui.common.BaseButton;

public class DataFormNganhToHop extends JDialog {

    public JTextField txtId, txtMaNganh, txtMaToHop;
    public JTextField txtHsMon1, txtHsMon2, txtHsMon3;
    public JTextField txtTbKeys, txtDoLech;

    public JComboBox<String> cboMon1, cboMon2, cboMon3;

    public BaseButton btnSave, btnCancel;

    private final Color BG_COLOR = new Color(245, 247, 249);
    private final Color LABEL_COLOR = new Color(52, 73, 94);
    private final Color BORDER_COLOR = new Color(220, 223, 225);

    // Danh sách môn học đã bổ sung CNCN, CNNN và đổi tên GD thành KTPL
    public static final String[] SUBJECT_OPTIONS = {
            "Không chọn", "Toán (TO)", "Vật lý (LI)", "Hóa học (HO)",
            "Sinh học (SI)", "Ngữ văn (VA)", "Lịch sử (SU)", "Địa lý (DI)",
            "Ngoại ngữ 1 (N1)", "Tin học (TI)", "KTPL / GDCD (KTPL)",
            "CN Cơ nhiệt (CNCN)", "CN Nông nghiệp (CNNN)", "Khác (KHAC)"
    };

    public DataFormNganhToHop() {
        setTitle("Thêm mới/Cập nhật Ngành - Tổ hợp");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 20, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        formPanel.setBackground(BG_COLOR);

        txtId = createTextField();
        txtMaNganh = createTextField();
        txtMaToHop = createTextField();
        txtTbKeys = createTextField();
        txtHsMon1 = createTextField();
        txtHsMon2 = createTextField();
        txtHsMon3 = createTextField();
        txtDoLech = createTextField();

        // 1. Cấu hình ô ID (Khóa mờ)
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        // 2. Cấu hình ô TB_Keys (Khóa mờ để tự động điền)
        txtTbKeys.setEditable(false);
        txtTbKeys.setBackground(new Color(230, 230, 230));
        txtTbKeys.setToolTipText("Trường này được tạo tự động từ Mã Ngành và Mã Tổ Hợp");

        // 3. Khởi tạo ComboBoxes
        cboMon1 = createComboBox(SUBJECT_OPTIONS);
        cboMon2 = createComboBox(SUBJECT_OPTIONS);
        cboMon3 = createComboBox(SUBJECT_OPTIONS);

        // 4. Logic Placeholder (Chữ mờ) cho ô Độ Lệch
        txtDoLech.setText("0.0");
        txtDoLech.setForeground(Color.GRAY);
        txtDoLech.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtDoLech.getText().equals("0.0")) {
                    txtDoLech.setText("");
                    txtDoLech.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtDoLech.getText().trim().isEmpty()) {
                    txtDoLech.setForeground(Color.GRAY);
                    txtDoLech.setText("0.0");
                }
            }
        });

        // ================= LOGIC TỰ ĐỘNG GHÉP TB_KEYS =================
        DocumentListener keyGeneratorListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { generateTbKeys(); }
            @Override
            public void removeUpdate(DocumentEvent e) { generateTbKeys(); }
            @Override
            public void changedUpdate(DocumentEvent e) { generateTbKeys(); }
        };

        // Gắn bộ lắng nghe sự kiện gõ phím vào ô Mã Ngành và Mã Tổ Hợp
        txtMaNganh.getDocument().addDocumentListener(keyGeneratorListener);
        txtMaToHop.getDocument().addDocumentListener(keyGeneratorListener);
        // ==============================================================

        formPanel.add(createRow("ID", txtId));
        formPanel.add(createRow("Mã Ngành", txtMaNganh));

        formPanel.add(createRow("Mã Tổ Hợp", txtMaToHop));
        formPanel.add(createRow("TB Keys", txtTbKeys));

        formPanel.add(createRow("Chọn Môn 1", cboMon1));
        formPanel.add(createRow("Hệ số Môn 1", txtHsMon1));

        formPanel.add(createRow("Chọn Môn 2", cboMon2));
        formPanel.add(createRow("Hệ số Môn 2", txtHsMon2));

        formPanel.add(createRow("Chọn Môn 3", cboMon3));
        formPanel.add(createRow("Hệ số Môn 3", txtHsMon3));

        formPanel.add(createRow("Độ lệch", txtDoLech));
        formPanel.add(new JLabel("")); // Cân bằng lưới Grid

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
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

    // Hàm tự động ghép chuỗi cho TB_Keys
    private void generateTbKeys() {
        String maNganh = txtMaNganh.getText().trim();
        String maToHop = txtMaToHop.getText().trim();

        if (maNganh.isEmpty() && maToHop.isEmpty()) {
            txtTbKeys.setText("");
        } else if (maToHop.isEmpty()) {
            txtTbKeys.setText(maNganh + "_");
        } else if (maNganh.isEmpty()) {
            txtTbKeys.setText("_" + maToHop);
        } else {
            txtTbKeys.setText(maNganh + "_" + maToHop);
        }
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(Color.WHITE);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return txt;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cbo = new JComboBox<>(items);
        cbo.setPreferredSize(new Dimension(200, 35));
        cbo.setBackground(Color.WHITE);
        cbo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return cbo;
    }

    private JPanel createRow(String label, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 34));
        lbl.setForeground(LABEL_COLOR);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }
}