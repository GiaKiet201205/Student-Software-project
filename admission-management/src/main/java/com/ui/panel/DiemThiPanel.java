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

        // =====================================================================
        // TÍCH HỢP NÚT IMPORT VỚI MENU THẢ XUỐNG
        // =====================================================================
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        BaseButton btnImport = new BaseButton("📥 Nhập từ Excel ▼", new Color(46, 204, 113));
        JPopupMenu popupImport = new JPopupMenu();

        // Định nghĩa 3 chức năng nạp điểm
        JMenuItem itemTHPT = new JMenuItem("1. Nạp điểm THPT (Cấu trúc Ngang)");
        JMenuItem itemDGNL = new JMenuItem("2. Nạp điểm ĐGNL (Cấu trúc Dọc)");
        JMenuItem itemVSAT = new JMenuItem("3. Nạp điểm V-SAT (Cấu trúc Dọc)");

        // Truyền (Loại File, Mã Phương Thức) xuống hàm xử lý
        itemTHPT.addActionListener(e -> functionImportExcel("THPT", "3"));
        itemDGNL.addActionListener(e -> functionImportExcel("DGNL", "4"));
        itemVSAT.addActionListener(e -> functionImportExcel("VSAT", "5"));

        popupImport.add(itemTHPT);
        popupImport.add(itemDGNL);
        popupImport.add(itemVSAT);

        btnImport.addActionListener(e -> popupImport.show(btnImport, 0, btnImport.getHeight()));

        BaseButton addButton = new BaseButton("Thêm mới");

        actionPanel.add(btnImport);
        actionPanel.add(addButton);
        header.add(actionPanel, BorderLayout.EAST);
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

    // =====================================================================
    // HÀM XỬ LÝ IMPORT EXCEL KẾT HỢP SWING-WORKER
    // =====================================================================
    private void functionImportExcel(String loaiFile, String phuongThuc) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel...");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();

            // Hiện con trỏ chuột xoay tròn báo hiệu hệ thống đang bận
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() throws Exception {
                    // Gọi controller dựa trên loại file
                    if (loaiFile.equals("THPT")) {
                        return controller.importDiemThpt(file, phuongThuc);
                    } else {
                        // ĐGNL và V-SAT dùng chung hàm xử lý cấu trúc Dọc
                        return controller.importDiemDgnlVsat(file, phuongThuc);
                    }
                }

                @Override
                protected void done() {
                    // Trả lại con trỏ chuột bình thường
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        String message = get();
                        JOptionPane.showMessageDialog(DiemThiPanel.this, message, "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                        loadTable(); // Tự động load lại bảng
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(DiemThiPanel.this, "Lỗi nạp file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }

    private void loadTable() {
        List<DiemThiXetTuyen> list = controller.getAll();
        renderTableData(list);
    }

    private void renderTableData(List<DiemThiXetTuyen> list) {
        model.setRowCount(0);
        if (list == null) return;
        for(DiemThiXetTuyen item : list) {
            model.addRow(new Object[]{
                    item.getIdDiemThi(),
                    item.getCccd(),
                    item.getSoBaoDanh(),
                    item.getPhuongThuc()
            });
        }
    }

    // Các hàm functionAddData, functionEditData, functionDeleteData, functionSearchData, getEntityFromForm...
    // Bạn giữ nguyên code cũ của bạn ở các phần này nhé.

    private void functionAddData() { /* Code cũ của bạn */ }
    private void functionEditData() { /* Code cũ của bạn */ }
    private void functionDeleteData() { /* Code cũ của bạn */ }
    private void functionSearchData() { /* Code cũ của bạn */ }
}