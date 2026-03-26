package com.ui.panel.QuyDoi;

import com.config.AppConfig;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BangQuyDoiPanel extends BasePanel {

    private BaseTable table;
    private DefaultTableModel model;

    public BangQuyDoiPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBackground(AppConfig.COLOR_BACKGROUND);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Header Section ---
        BasePanel headerPanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ BẢNG QUY ĐỔI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnGroup.setOpaque(false);
        BaseButton btnAdd = new BaseButton(" + Thêm mới ", new Color(39, 174, 96));
        BaseButton btnImport = new BaseButton(" Import Excel ", new Color(241, 196, 15));
        btnGroup.add(btnImport);
        btnGroup.add(btnAdd);
        headerPanel.add(btnGroup, BorderLayout.EAST);

        // --- Table Section (Khớp với ảnh mẫu) ---
        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Cấu trúc cột dựa trên file SQL xt_bangquydoi
        String[] columns = {
            "STT", "Phương thức", "Tổ hợp", "Môn", 
            "Điểm A", "Điểm B", "Điểm C", "Điểm D", 
            "Mã quy đổi", "Phân vị"
        };

        model = new DefaultTableModel(columns, 0);
        
        // Thêm dữ liệu mẫu để test giao diện
        model.addRow(new Object[]{"1", "V-SAT", "V00", "Toán", "9.50", "8.00", "7.50", "6.00", "VSAT_T_2025", "P99"});
        model.addRow(new Object[]{"2", "Học bạ", "A01", "Anh văn", "10.0", "9.00", "8.00", "7.00", "HB_AV_2025", "P95"});

        table = new BaseTable(model);
        
        // Tùy chỉnh thêm để giống ảnh: Header màu xanh đậm
        table.getTableHeader().setBackground(new Color(41, 128, 185)); 
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // --- Footer Section (Nút thao tác) ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        BaseButton btnEdit = new BaseButton(" Chỉnh sửa ", AppConfig.COLOR_PRIMARY);
        BaseButton btnDelete = new BaseButton(" Xóa dữ liệu ", new Color(192, 57, 43));
        footer.add(btnEdit);
        footer.add(btnDelete);

        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(tablePanel, BorderLayout.CENTER);
        mainContent.add(footer, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
        // 1. Sự kiện cho nút Thêm mới
btnAdd.addActionListener(e -> {
    // Tìm cửa sổ cha (Frame chính) để làm "chủ" cho Dialog
    Window owner = SwingUtilities.getWindowAncestor(this);
    
    // Khởi tạo Dialog (mình sẽ viết code lớp này ở dưới)
    BangQuyDoiDialog dialog = new BangQuyDoiDialog((Frame) owner);
    dialog.setVisible(true); // Hiển thị popup
});

// 2. Sự kiện Import Excel
btnImport.addActionListener(e -> {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Chọn file danh sách quy đổi (Excel)");
    
    // Chỉ cho phép chọn file .xlsx hoặc .xls
    int userSelection = fileChooser.showOpenDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        
        // Giao diện thông báo tạm thời
        JOptionPane.showMessageDialog(this, 
            "Đang chuẩn bị import file: " + filePath + 
            "\n(Logic xử lý thư viện Apache POI sẽ viết ở đây)");
    }
});
    }
}