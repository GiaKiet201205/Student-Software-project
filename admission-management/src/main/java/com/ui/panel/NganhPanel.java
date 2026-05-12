package com.ui.panel;

import com.config.AppConfig;
import com.controller.NganhController;
import com.entity.Nganh;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class NganhPanel extends BasePanel {
    private DefaultTableModel defaultTableModel;
    private BaseTable baseTable;
    private final NganhController controller;

    // Khai báo thêm 2 nút import mới
    private BaseButton addButton, importNganhBtn, importDiemSanBtn, importChiTieuBtn, editButton, deleteButton;
    
    private JTextField txtSearch;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public NganhPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initializeComponents();
        
        this.controller = new NganhController(this);
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

        JLabel titleLabel = new JLabel(" QUẢN LÝ NGÀNH TUYỂN SINH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Khởi tạo các nút với màu sắc nhận diện khác nhau
        importNganhBtn = new BaseButton(" Nhập Ngành Gốc ", new Color(40, 167, 69)); // Xanh lá
        importDiemSanBtn = new BaseButton(" Nhập Điểm Sàn ", new Color(23, 162, 184)); // Xanh lơ
        importChiTieuBtn = new BaseButton(" Nhập Chỉ Tiêu ", new Color(253, 126, 20)); // Cam
        addButton = new BaseButton(" + Thêm Ngành ");
        
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 32));
        txtSearch.setToolTipText("Nhập từ khóa tìm kiếm...");
        
        JPanel headerActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerActionPanel.setOpaque(false);
        headerActionPanel.add(new JLabel("Tìm kiếm: "));
        headerActionPanel.add(txtSearch);
        headerActionPanel.add(importNganhBtn);
        headerActionPanel.add(importDiemSanBtn);
        headerActionPanel.add(importChiTieuBtn);
        headerActionPanel.add(addButton);
        
        headerPanel.add(headerActionPanel, BorderLayout.EAST);
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] cols = {
            "Mã Ngành", "Tên Ngành", "Tổ Hợp Gốc", "Chỉ Tiêu", 
            "Điểm Sàn", "Điểm Trúng Tuyển", "Tuyển Thẳng", 
            "ĐGNL", "THPT", "V-SAT", "SL XTT", "SL ĐGNL", "SL V-SAT", "SL THPT"
        };
        
        defaultTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        baseTable = new BaseTable(defaultTableModel);
        baseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        rowSorter = new TableRowSorter<>(defaultTableModel);
        baseTable.setRowSorter(rowSorter);
        
        int[] widths = {100, 250, 100, 80, 80, 120, 100, 80, 80, 80, 80, 80, 80, 80};
        for(int i = 0; i < widths.length; i++) {
            baseTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(baseTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setOpaque(false);

        editButton = new BaseButton("Chỉnh sửa");
        deleteButton = new BaseButton("Xóa ngành", AppConfig.COLOR_DANGER);

        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        mainContentPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    public BaseTable getBaseTable() { return baseTable; }
    public DefaultTableModel getDefaultTableModel() { return defaultTableModel; }

    public void hienThiDuLieuLenBang(List<Nganh> list) {
        defaultTableModel.setRowCount(0);
        for (Nganh nganh : list) {
            defaultTableModel.addRow(new Object[]{
                nganh.getMaNganh(), nganh.getTenNganh(), nganh.getToHopGoc(),
                nganh.getChiTieu(), nganh.getDiemSan(), nganh.getDiemTrungTuyen(),
                nganh.getTuyenThang(), nganh.getDgnl(), nganh.getThpt(), nganh.getVsat(),
                nganh.getSlXtt(), nganh.getSlDgnl(), nganh.getSlVsat(), nganh.getSlThpt()
            });
        }
    }

    private void setupActionListeners() {
        addButton.addActionListener(e -> controller.handleAdd());
        editButton.addActionListener(e -> controller.handleEdit());
        deleteButton.addActionListener(e -> controller.handleDelete());
        
        // Cấu hình sự kiện cho 3 nút Import riêng biệt
        importNganhBtn.addActionListener(e -> controller.handleImport());
        importDiemSanBtn.addActionListener(e -> controller.handleImportDiemSan());
        importChiTieuBtn.addActionListener(e -> controller.handleImportChiTieu());
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
    }
    
    private void filterTable() {
        String text = txtSearch.getText();
        if (text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
}