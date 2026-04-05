package com.ui.panel;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;

import com.config.AppConfig;
import com.controller.NganhToHopController;
import com.entity.NganhToHop;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;
import com.ui.data_form.DataFormNganhToHop;
import com.ui.common.BaseButton;

public class NganhToHopPanel extends BasePanel {
    private NganhToHopController controller = new NganhToHopController();

    JTextField searchField;
    BaseTable table;
    DefaultTableModel model;

    public NganhToHopPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initComponents();
        loadTable();
    }

    public void initComponents() {
        JPanel contentPanel = new BasePanel(AppConfig.COLOR_BACKGROUND, 0);
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BasePanel header = new BasePanel(AppConfig.COLOR_WHITE, 15);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("QUẢN LÝ NGÀNH - TỔ HỢP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(titleLabel, BorderLayout.WEST);

        BasePanel searchPanel = new BasePanel(AppConfig.COLOR_WHITE, 0);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -4));
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        searchField.setPreferredSize(new Dimension(500, 30));

        String placeholder = "Nhập mã ngành/tổ hợp để tìm kiếm...";
        searchField.setText(placeholder);
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        BaseButton searchButton = new BaseButton("Tìm kiếm");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        header.add(searchPanel, BorderLayout.CENTER);

        BaseButton addButton = new BaseButton("Thêm mới");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.add(addButton, BorderLayout.EAST);
        contentPanel.add(header, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());
        String[] columns = {"ID", "Mã Ngành", "Mã Tổ Hợp", "Môn 1", "Môn 2", "Môn 3", "Độ Lệch"};
        model = new DefaultTableModel(columns, 0);
        table = new BaseTable(model);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(AppConfig.COLOR_PRIMARY);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        BaseButton btnEdit = new BaseButton("Chỉnh sửa");
        BaseButton btnDelete = new BaseButton("Xóa", new Color(220, 53, 69));
        BaseButton btnRefresh = new BaseButton("Tải lại", new Color(41, 128, 185));

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnRefresh);
        contentPanel.add(footer, BorderLayout.SOUTH);

        addButton.addActionListener(e -> functionAddData());
        btnEdit.addActionListener(e -> functionEditData());
        btnDelete.addActionListener(e -> functionDeleteData());
        searchButton.addActionListener(e -> functionSearchData());
        btnRefresh.addActionListener(e -> loadTable());

        add(contentPanel, BorderLayout.CENTER);
    }

    private void functionAddData() {
        DataFormNganhToHop form = new DataFormNganhToHop();
        form.txtId.setText(""); // Để trống để biểu thị là thêm mới

        form.btnSave.addActionListener(e -> {
            try {
                NganhToHop entity = getEntityFromForm(form);
                if(controller.add(entity)) {
                    JOptionPane.showMessageDialog(form, "Thêm mới thành công!");
                    loadTable();
                    form.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    private void functionEditData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để chỉnh sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        NganhToHop entity = controller.getById(id);
        if(entity == null) return;

        DataFormNganhToHop form = new DataFormNganhToHop();
        fillFormWithEntity(form, entity);

        form.btnSave.addActionListener(e -> {
            try {
                NganhToHop updatedEntity = getEntityFromForm(form);
                updatedEntity.setId(id); // Giữ nguyên ID cũ
                if(controller.update(updatedEntity)) {
                    JOptionPane.showMessageDialog(form, "Cập nhật thành công!");
                    loadTable();
                    form.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    private void functionDeleteData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa dữ liệu này?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Integer id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            NganhToHop entity = controller.getById(id);
            if(entity != null && controller.delete(entity)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadTable();
            }
        }
    }

    private void functionSearchData() {
        String keyString = searchField.getText();
        if(keyString.isEmpty() || keyString.equals("Nhập mã ngành/tổ hợp để tìm kiếm...")) {
            loadTable();
            return;
        }
        List<NganhToHop> list = controller.search(keyString);
        renderTableData(list);
    }

    private void loadTable() {
        List<NganhToHop> list = controller.getAll();
        renderTableData(list);
    }

    private void renderTableData(List<NganhToHop> list) {
        model.setRowCount(0);
        for(NganhToHop item : list) {
            model.addRow(new Object[]{
                    item.getId(),
                    item.getMaNganh(),
                    item.getMaToHop(),
                    item.getMon1(),
                    item.getMon2(),
                    item.getMon3(),
                    item.getDoLech()
            });
        }
    }

    // ================== LOGIC XỬ LÝ DỮ LIỆU THÔNG MINH ==================

    private NganhToHop getEntityFromForm(DataFormNganhToHop form) {
        // Trích xuất mã môn từ ComboBox (VD: "Toán (TO)" -> "TO")
        String m1 = getSubjectCode((String) form.cboMon1.getSelectedItem());
        String m2 = getSubjectCode((String) form.cboMon2.getSelectedItem());
        String m3 = getSubjectCode((String) form.cboMon3.getSelectedItem());

        return NganhToHop.builder()
                .maNganh(form.txtMaNganh.getText().trim())
                .maToHop(form.txtMaToHop.getText().trim())
                .tbKeys(form.txtTbKeys.getText().trim())
                .mon1(m1).heSoMon1(parseByte(form.txtHsMon1.getText()))
                .mon2(m2).heSoMon2(parseByte(form.txtHsMon2.getText()))
                .mon3(m3).heSoMon3(parseByte(form.txtHsMon3.getText()))

                // Tự động gán Boolean dựa vào 3 môn đã chọn
                .n1(hasSubject("N1", m1, m2, m3))
                .toan(hasSubject("TO", m1, m2, m3))
                .ly(hasSubject("LI", m1, m2, m3))
                .hoa(hasSubject("HO", m1, m2, m3))
                .sinh(hasSubject("SI", m1, m2, m3))
                .van(hasSubject("VA", m1, m2, m3))
                .su(hasSubject("SU", m1, m2, m3))
                .dia(hasSubject("DI", m1, m2, m3))
                .tinHoc(hasSubject("TI", m1, m2, m3))
                .ktpl(hasSubject("KTPL", m1, m2, m3))
                .khac(hasSubject("KHAC", m1, m2, m3))

                .doLech(parseBigDecimal(form.txtDoLech.getText()))
                .build();
    }

    private void fillFormWithEntity(DataFormNganhToHop form, NganhToHop entity) {
        form.txtId.setText(String.valueOf(entity.getId()));
        form.txtMaNganh.setText(entity.getMaNganh());
        form.txtMaToHop.setText(entity.getMaToHop());
        form.txtTbKeys.setText(entity.getTbKeys());

        form.txtHsMon1.setText(entity.getHeSoMon1() != null ? entity.getHeSoMon1().toString() : "1");
        form.txtHsMon2.setText(entity.getHeSoMon2() != null ? entity.getHeSoMon2().toString() : "1");
        form.txtHsMon3.setText(entity.getHeSoMon3() != null ? entity.getHeSoMon3().toString() : "1");

        // Map ngược từ Mã Môn (TO) về tên hiển thị trong ComboBox (Toán (TO))
        form.cboMon1.setSelectedItem(getSubjectNameByCode(entity.getMon1()));
        form.cboMon2.setSelectedItem(getSubjectNameByCode(entity.getMon2()));
        form.cboMon3.setSelectedItem(getSubjectNameByCode(entity.getMon3()));

        form.txtDoLech.setText(entity.getDoLech() != null ? entity.getDoLech().toString() : "0.0");
        form.txtDoLech.setForeground(Color.BLACK); // Hủy trạng thái mờ khi edit
    }

    // ================== CÁC HÀM HELPER TIỆN ÍCH ==================

    // Kiểm tra xem môn học có nằm trong 3 lựa chọn không
    private boolean hasSubject(String code, String m1, String m2, String m3) {
        return code.equals(m1) || code.equals(m2) || code.equals(m3);
    }

    // Bóc tách chữ trong ngoặc: "Toán (TO)" -> "TO"
    private String getSubjectCode(String fullName) {
        if (fullName == null || fullName.equals("Không chọn")) return "";
        int start = fullName.indexOf("(");
        int end = fullName.indexOf(")");
        if (start != -1 && end != -1) {
            return fullName.substring(start + 1, end);
        }
        return fullName;
    }

    // Map ngược mã về tên đầy đủ để hiển thị lại form Edit
    private String getSubjectNameByCode(String code) {
        if (code == null || code.trim().isEmpty()) return "Không chọn";
        for (String opt : DataFormNganhToHop.SUBJECT_OPTIONS) {
            if (opt.contains("(" + code + ")")) return opt;
        }
        return "Không chọn";
    }

    private Byte parseByte(String text) {
        if(text == null || text.trim().isEmpty()) return 1;
        try { return Byte.parseByte(text.trim()); } catch(Exception e) { return 1; }
    }

    private BigDecimal parseBigDecimal(String text) {
        if(text == null || text.trim().isEmpty() || text.trim().equals("0.0")) return BigDecimal.ZERO;
        try { return new BigDecimal(text.trim()); } catch(Exception e) { return BigDecimal.ZERO; }
    }
}