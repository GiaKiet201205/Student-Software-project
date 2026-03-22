package com.ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.config.AppConfig;
import com.entity.DiemCongXetTuyen;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;
import com.ui.common.BaseButton;

public class DiemCongXetTuyenPanel extends BasePanel {
    DiemCongXetTuyen diemCongXetTuyen;
    JTextField searchField;
    BaseButton searchButton;
    BaseButton addButton;
    BaseTable table;
    BaseButton btnEdit;
    BaseButton btnDelete;
    BaseButton btnReadExcel;

    public DiemCongXetTuyenPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initComponents();
        functionAddData();
        functionEditData();
        functionDeleteData();
        functionReadExcel();
        functionSearch();
    }

    public void initComponents() {
        // Main Content Panel
        JPanel contentPanel = new BasePanel(AppConfig.COLOR_BACKGROUND, 0);
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        BasePanel header = new BasePanel(AppConfig.COLOR_WHITE, 15);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 60));
    

        JLabel titleLabel = new JLabel("ĐIỂM CỘNG XÉT TUYỂN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        header.add(titleLabel, BorderLayout.WEST);

        BasePanel searchPanel = new BasePanel(AppConfig.COLOR_WHITE, 0);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -4));
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        searchField.setPreferredSize(new Dimension(500, 30));
        
        String placeholder = "Nhập để tìm kiếm...";
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
        
        searchButton = new BaseButton("Tìm kiếm");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        header.add(searchPanel, BorderLayout.CENTER);

        addButton = new BaseButton("Thêm mới");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.add(addButton, BorderLayout.EAST);
        contentPanel.add(header, BorderLayout.NORTH);

        // Table
        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());
        String[] columns = {"iddiemcong", "ts_cccd", "manganh", "matohop", "phuongthuc", "diemCC", "diemUtxt", "diemTong", "ghichu", "dc_keys"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
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

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        btnEdit = new BaseButton("Chỉnh sửa");
        btnDelete = new BaseButton("Xóa", new Color(220, 53, 69));
        btnReadExcel = new BaseButton("Đọc file", new Color(39, 174, 96));

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnReadExcel);
        contentPanel.add(footer, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }
    
    //TODO: chức năng thêm, sửa, xóa, đọc file excel, tìm kiếm
    public void functionAddData() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void functionEditData() {
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void functionDeleteData() {
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void functionReadExcel() {
        btnReadExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void functionSearch() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }
}
