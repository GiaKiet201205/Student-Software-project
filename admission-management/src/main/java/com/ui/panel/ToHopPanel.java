package com.ui.panel;

import com.config.AppConfig;
import com.entity.ToHopMonThi;
import com.service.ToHopMonThiService;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class ToHopPanel extends BasePanel {
    private DefaultTableModel defaultTableModel;
    private BaseTable baseTable;
    private final ToHopMonThiService toHopMonThiService;

    // Khai báo các nút bấm
    private BaseButton addButton;
    private BaseButton importButton; // Nút Import Excel
    private BaseButton editButton;
    private BaseButton deleteButton;

    public ToHopPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        this.toHopMonThiService = new ToHopMonThiService();
        setLayout(new BorderLayout());
        initializeComponents();
        loadDataIntoTable();
        setupActionListeners();
    }

    private void initializeComponents() {
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(AppConfig.COLOR_BACKGROUND);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Header Panel ---
        BasePanel headerPanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        headerPanel.setLayout(new BorderLayout(20, 0));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel(" QUẢN LÝ TỔ HỢP MÔN THI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Nút Thêm và Nút Nhập Excel
        addButton = new BaseButton(" + Thêm Tổ Hợp ");
        importButton = new BaseButton(" Nhập Excel ", new Color(40, 167, 69)); // Nút màu xanh lá
        
        JPanel headerActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerActionPanel.setOpaque(false);
        headerActionPanel.add(importButton);
        headerActionPanel.add(addButton);
        
        headerPanel.add(headerActionPanel, BorderLayout.EAST);
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] tableColumnNames = {"Mã Tổ Hợp", "Tên Tổ Hợp", "Môn Thi Thứ 1", "Môn Thi Thứ 2", "Môn Thi Thứ 3"};
        defaultTableModel = new DefaultTableModel(tableColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        baseTable = new BaseTable(defaultTableModel);
        
        JScrollPane scrollPane = new JScrollPane(baseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);

        // --- Footer Panel ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setOpaque(false);
        
        editButton = new BaseButton("Chỉnh sửa");
        deleteButton = new BaseButton("Xóa tổ hợp", AppConfig.COLOR_DANGER);
        
        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        mainContentPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void loadDataIntoTable() {
        defaultTableModel.setRowCount(0);
        try {
            List<ToHopMonThi> danhSachToHop = toHopMonThiService.getAllToHop();
            for (ToHopMonThi toHop : danhSachToHop) {
                Object[] rowData = {toHop.getMaToHop(), toHop.getTenToHop(), toHop.getMon1(), toHop.getMon2(), toHop.getMon3()};
                defaultTableModel.addRow(rowData);
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + exception.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupActionListeners() {
        // 1. Chức năng Thêm
        addButton.addActionListener(e -> {
            ToHopDialog dialog = new ToHopDialog(SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);

            ToHopMonThi newToHop = dialog.getToHopResult();
            if (newToHop != null) {
                try {
                    toHopMonThiService.themToHop(newToHop);
                    loadDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Thêm tổ hợp thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 2. Chức năng Sửa
        editButton.addActionListener(e -> {
            int selectedRow = baseTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tổ hợp trên bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ToHopMonThi toHopEdit = new ToHopMonThi();
            toHopEdit.setMaToHop((String) defaultTableModel.getValueAt(selectedRow, 0));
            toHopEdit.setTenToHop((String) defaultTableModel.getValueAt(selectedRow, 1));
            toHopEdit.setMon1((String) defaultTableModel.getValueAt(selectedRow, 2));
            toHopEdit.setMon2((String) defaultTableModel.getValueAt(selectedRow, 3));
            toHopEdit.setMon3((String) defaultTableModel.getValueAt(selectedRow, 4));

            ToHopDialog dialog = new ToHopDialog(SwingUtilities.getWindowAncestor(this), toHopEdit);
            dialog.setVisible(true);

            ToHopMonThi updatedToHop = dialog.getToHopResult();
            if (updatedToHop != null) {
                try {
                    toHopMonThiService.capNhatToHop(updatedToHop);
                    loadDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 3. Chức năng Xóa
        deleteButton.addActionListener(e -> {
            int selectedRow = baseTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tổ hợp trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maToHop = (String) defaultTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tổ hợp " + maToHop + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    toHopMonThiService.xoaToHop(maToHop);
                    loadDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 4. Chức năng Import Excel
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel (.xlsx) chứa danh sách Tổ hợp");
            int result = fileChooser.showOpenDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (FileInputStream fis = new FileInputStream(selectedFile);
                     Workbook workbook = new XSSFWorkbook(fis)) {
                    
                    Sheet sheet = workbook.getSheetAt(0);
                    int successCount = 0;
                    
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row == null) continue;

                        try {
                            ToHopMonThi toHop = new ToHopMonThi();
                            toHop.setMaToHop(row.getCell(0).getStringCellValue());
                            toHop.setTenToHop(row.getCell(1).getStringCellValue());
                            toHop.setMon1(row.getCell(2).getStringCellValue());
                            toHop.setMon2(row.getCell(3).getStringCellValue());
                            toHop.setMon3(row.getCell(4).getStringCellValue());

                            toHopMonThiService.themToHop(toHop);
                            successCount++;
                        } catch (Exception rowEx) {
                            System.out.println("Lỗi ở dòng " + (i + 1) + " trong file Excel: " + rowEx.getMessage());
                        }
                    }
                    
                    loadDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Nhập thành công " + successCount + " tổ hợp từ Excel!");
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi đọc file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}