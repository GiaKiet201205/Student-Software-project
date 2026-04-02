package com.controller;

import com.entity.ToHopMonThi;
import com.service.ToHopMonThiService;
import com.ui.panel.ToHopDialog;
import com.ui.panel.ToHopPanel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class ToHopController {
    private final ToHopPanel view;
    private final ToHopMonThiService service;

    public ToHopController(ToHopPanel view) {
        this.view = view;
        this.service = new ToHopMonThiService();
    }

    public void loadData() {
        try {
            List<ToHopMonThi> list = service.getAllToHop();
            view.hienThiDuLieuLenBang(list); // Gọi hàm của View để vẽ dữ liệu
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleAdd() {
        ToHopDialog dialog = new ToHopDialog(SwingUtilities.getWindowAncestor(view), null);
        dialog.setVisible(true);

        ToHopMonThi newToHop = dialog.getToHopResult();
        if (newToHop != null) {
            try {
                service.themToHop(newToHop);
                loadData();
                JOptionPane.showMessageDialog(view, "Thêm tổ hợp thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleEdit() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một tổ hợp trên bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = view.getDefaultTableModel();
        ToHopMonThi toHopEdit = new ToHopMonThi();
        toHopEdit.setMaToHop((String) model.getValueAt(selectedRow, 0));
        toHopEdit.setTenToHop((String) model.getValueAt(selectedRow, 1));
        toHopEdit.setMon1((String) model.getValueAt(selectedRow, 2));
        toHopEdit.setMon2((String) model.getValueAt(selectedRow, 3));
        toHopEdit.setMon3((String) model.getValueAt(selectedRow, 4));

        ToHopDialog dialog = new ToHopDialog(SwingUtilities.getWindowAncestor(view), toHopEdit);
        dialog.setVisible(true);

        ToHopMonThi updatedToHop = dialog.getToHopResult();
        if (updatedToHop != null) {
            try {
                service.capNhatToHop(updatedToHop);
                loadData();
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleDelete() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một tổ hợp trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maToHop = (String) view.getDefaultTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa tổ hợp " + maToHop + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.xoaToHop(maToHop);
                loadData();
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel (.xlsx) chứa danh sách Tổ hợp");
        
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
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

                        service.themToHop(toHop);
                        successCount++;
                    } catch (Exception rowEx) {
                        System.out.println("Lỗi bỏ qua dòng " + (i + 1) + ": " + rowEx.getMessage());
                    }
                }
                loadData();
                JOptionPane.showMessageDialog(view, "Nhập thành công " + successCount + " tổ hợp từ Excel!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi đọc file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}