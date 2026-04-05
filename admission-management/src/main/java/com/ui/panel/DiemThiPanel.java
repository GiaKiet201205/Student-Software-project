package com.ui.panel;

import java.awt.*;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.config.AppConfig;
import com.controller.DiemThiXetTuyenController;
import com.entity.DiemThiXetTuyen;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;
import com.ui.common.BaseButton;
import com.ui.data_form.DataFormDiemThi;

public class DiemThiPanel extends BasePanel {

    private DiemThiXetTuyenController controller = new DiemThiXetTuyenController();

    JTextField searchField;
    BaseTable table;
    DefaultTableModel model;

    public DiemThiPanel() {
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

        JLabel titleLabel = new JLabel("QUẢN LÝ ĐIỂM THÍ SINH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(titleLabel, BorderLayout.WEST);

        BasePanel searchPanel = new BasePanel(AppConfig.COLOR_WHITE, 0);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -4));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30));

        BaseButton searchButton = new BaseButton("Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        header.add(searchPanel, BorderLayout.CENTER);

        BaseButton addButton = new BaseButton("Thêm mới");
        header.add(addButton, BorderLayout.EAST);
        contentPanel.add(header, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] columns = {"ID", "CCCD", "Số Báo Danh", "Phương Thức"};
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

        BaseButton btnEdit = new BaseButton("Chỉnh sửa / Chi tiết");
        BaseButton btnDelete = new BaseButton("Xóa", new Color(220, 53, 69));
        BaseButton btnRefresh = new BaseButton("Tải lại", new Color(41, 128, 185));

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnRefresh);
        contentPanel.add(footer, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> functionAddData());
        btnEdit.addActionListener(e -> functionEditData());
        btnDelete.addActionListener(e -> functionDeleteData());
        searchButton.addActionListener(e -> functionSearchData());
        btnRefresh.addActionListener(e -> loadTable());

        add(contentPanel, BorderLayout.CENTER);
    }

    private void functionAddData() {
        DataFormDiemThi form = new DataFormDiemThi();
        form.txtId.setText("Tự động tạo");
        form.txtId.setEditable(false);

        form.btnSave.addActionListener(e -> {
            try {
                DiemThiXetTuyen entity = getEntityFromForm(form);
                if(controller.add(entity)) {
                    JOptionPane.showMessageDialog(form, "Thêm mới điểm thí sinh thành công!");
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
        int row = table.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một dòng để xem chi tiết/sửa!");
            return;
        }

        Integer id = Integer.parseInt(model.getValueAt(row, 0).toString());
        DiemThiXetTuyen entity = controller.getById(id);
        if(entity == null) return;

        DataFormDiemThi form = new DataFormDiemThi();
        fillFormWithEntity(form, entity);

        form.btnSave.addActionListener(e -> {
            try {
                DiemThiXetTuyen updatedEntity = getEntityFromForm(form);
                updatedEntity.setIdDiemThi(id); // Giữ ID
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

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa dữ liệu điểm này?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Integer id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            DiemThiXetTuyen entity = controller.getById(id);
            if(entity != null && controller.delete(entity)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadTable();
            }
        }
    }

    private void functionSearchData() {
        String keyString = searchField.getText();
        if(keyString.isEmpty()) {
            loadTable();
            return;
        }
        List<DiemThiXetTuyen> list = controller.search(keyString);
        renderTableData(list);
    }

    private void loadTable() {
        List<DiemThiXetTuyen> list = controller.getAll();
        renderTableData(list);
    }

    private void renderTableData(List<DiemThiXetTuyen> list) {
        model.setRowCount(0);
        for(DiemThiXetTuyen item : list) {
            model.addRow(new Object[]{
                    item.getIdDiemThi(),
                    item.getCccd(),
                    item.getSoBaoDanh(),
                    item.getPhuongThuc()
            });
        }
    }

    // --- Mapper ---
    private DiemThiXetTuyen getEntityFromForm(DataFormDiemThi form) {
        return DiemThiXetTuyen.builder()
                .cccd(form.txtCccd.getText().trim())
                .soBaoDanh(form.txtSbd.getText().trim())
                .phuongThuc(form.txtPhuongThuc.getText().trim())
                .toan(parseBD(form.txtTO.getText()))
                .ly(parseBD(form.txtLI.getText()))
                .hoa(parseBD(form.txtHO.getText()))
                .sinh(parseBD(form.txtSI.getText()))
                .van(parseBD(form.txtVA.getText()))
                .su(parseBD(form.txtSU.getText()))
                .dia(parseBD(form.txtDI.getText()))
                .n1Thi(parseBD(form.txtN1_THI.getText()))
                .n1CC(parseBD(form.txtN1_CC.getText()))
                .cncn(parseBD(form.txtCNCN.getText()))
                .cnnn(parseBD(form.txtCNNN.getText()))
                .tinHoc(parseBD(form.txtTI.getText()))
                .ktpl(parseBD(form.txtKTPL.getText()))
                .nl1(parseBD(form.txtNL1.getText()))
                .nk1(parseBD(form.txtNK1.getText()))
                .nk2(parseBD(form.txtNK2.getText()))
                .build();
    }

    private void fillFormWithEntity(DataFormDiemThi form, DiemThiXetTuyen entity) {
        form.txtId.setText(String.valueOf(entity.getIdDiemThi()));
        form.txtId.setEditable(false);
        form.txtCccd.setText(entity.getCccd());
        form.txtSbd.setText(entity.getSoBaoDanh());
        form.txtPhuongThuc.setText(entity.getPhuongThuc());

        form.txtTO.setText(formatBD(entity.getToan()));
        form.txtLI.setText(formatBD(entity.getLy()));
        form.txtHO.setText(formatBD(entity.getHoa()));
        form.txtSI.setText(formatBD(entity.getSinh()));
        form.txtVA.setText(formatBD(entity.getVan()));
        form.txtSU.setText(formatBD(entity.getSu()));
        form.txtDI.setText(formatBD(entity.getDia()));
        form.txtN1_THI.setText(formatBD(entity.getN1Thi()));
        form.txtN1_CC.setText(formatBD(entity.getN1CC()));
        form.txtCNCN.setText(formatBD(entity.getCncn()));
        form.txtCNNN.setText(formatBD(entity.getCnnn()));
        form.txtTI.setText(formatBD(entity.getTinHoc()));
        form.txtKTPL.setText(formatBD(entity.getKtpl()));
        form.txtNL1.setText(formatBD(entity.getNl1()));
        form.txtNK1.setText(formatBD(entity.getNk1()));
        form.txtNK2.setText(formatBD(entity.getNk2()));
    }

    private BigDecimal parseBD(String text) {
        if(text == null || text.trim().isEmpty()) return BigDecimal.ZERO;
        try { return new BigDecimal(text.trim()); } catch(Exception e) { return BigDecimal.ZERO; }
    }

    private String formatBD(BigDecimal bd) {
        return bd != null ? bd.toString() : "0.00";
    }
}