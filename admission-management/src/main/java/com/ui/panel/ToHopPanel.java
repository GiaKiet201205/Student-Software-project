package com.ui.panel;

import com.config.AppConfig;
import com.controller.ToHopController;
import com.entity.ToHopMonThi;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ToHopPanel extends BasePanel {
    private DefaultTableModel defaultTableModel;
    private BaseTable baseTable;
    private final ToHopController controller; // Khai báo Controller

    private BaseButton addButton, importButton, editButton, deleteButton;

    public ToHopPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initializeComponents();
        
        // Gắn Controller vào Panel và gọi load data
        this.controller = new ToHopController(this);
        this.controller.loadData();
        setupActionListeners();
    }

    private void initializeComponents() {
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(AppConfig.COLOR_BACKGROUND);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BasePanel headerPanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        headerPanel.setLayout(new BorderLayout(20, 0));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel(" QUẢN LÝ TỔ HỢP MÔN THI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        addButton = new BaseButton(" + Thêm Tổ Hợp ");
        importButton = new BaseButton(" Nhập Excel ", new Color(40, 167, 69));
        
        JPanel headerActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerActionPanel.setOpaque(false);
        headerActionPanel.add(importButton);
        headerActionPanel.add(addButton);
        
        headerPanel.add(headerActionPanel, BorderLayout.EAST);
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] cols = {"Mã Tổ Hợp", "Tên Tổ Hợp", "Môn Thi Thứ 1", "Môn Thi Thứ 2", "Môn Thi Thứ 3"};
        defaultTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        baseTable = new BaseTable(defaultTableModel);
        
        JScrollPane scrollPane = new JScrollPane(baseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setOpaque(false);
        
        editButton = new BaseButton("Chỉnh sửa");
        deleteButton = new BaseButton("Xóa tổ hợp", AppConfig.COLOR_DANGER);
        
        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        mainContentPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    // Các hàm công khai (public) để Controller có thể giao tiếp với View
    public BaseTable getBaseTable() { return baseTable; }
    public DefaultTableModel getDefaultTableModel() { return defaultTableModel; }

    public void hienThiDuLieuLenBang(List<ToHopMonThi> list) {
        defaultTableModel.setRowCount(0);
        for (ToHopMonThi toHop : list) {
            defaultTableModel.addRow(new Object[]{toHop.getMaToHop(), toHop.getTenToHop(), toHop.getMon1(), toHop.getMon2(), toHop.getMon3()});
        }
    }

    // Gắn sự kiện: Bấm nút thì gọi Controller tương ứng
    private void setupActionListeners() {
        addButton.addActionListener(e -> controller.handleAdd());
        editButton.addActionListener(e -> controller.handleEdit());
        deleteButton.addActionListener(e -> controller.handleDelete());
        importButton.addActionListener(e -> controller.handleImport());
    }
}