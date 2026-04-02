package com.controller;

import com.entity.Nganh;
import com.service.NganhService;
import com.ui.panel.NganhDialog;
import com.ui.panel.NganhPanel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;

public class NganhController {
    private final NganhPanel view;
    private final NganhService service;

    public NganhController(NganhPanel view) {
        this.view = view;
        this.service = new NganhService();
    }

    public void loadData() {
        try {
            List<Nganh> list = service.getAllNganh();
            view.hienThiDuLieuLenBang(list);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleAdd() {
        NganhDialog dialog = new NganhDialog(SwingUtilities.getWindowAncestor(view), null);
        dialog.setVisible(true);

        Nganh newNganh = dialog.getNganhResult();
        if (newNganh != null) {
            try {
                service.themNganh(newNganh);
                loadData();
                JOptionPane.showMessageDialog(view, "Thêm ngành thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleEdit() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một ngành trên bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = view.getDefaultTableModel();
        Nganh nganhEdit = new Nganh();
        
        nganhEdit.setMaNganh(String.valueOf(model.getValueAt(selectedRow, 0)));
        nganhEdit.setTenNganh(String.valueOf(model.getValueAt(selectedRow, 1)));
        nganhEdit.setToHopGoc(String.valueOf(model.getValueAt(selectedRow, 2)));
        nganhEdit.setChiTieu(Integer.parseInt(String.valueOf(model.getValueAt(selectedRow, 3))));
        nganhEdit.setDiemSan(new BigDecimal(String.valueOf(model.getValueAt(selectedRow, 4))));
        nganhEdit.setDiemTrungTuyen(new BigDecimal(String.valueOf(model.getValueAt(selectedRow, 5))));
        nganhEdit.setTuyenThang(String.valueOf(model.getValueAt(selectedRow, 6)));
        nganhEdit.setDgnl(String.valueOf(model.getValueAt(selectedRow, 7)));
        nganhEdit.setThpt(String.valueOf(model.getValueAt(selectedRow, 8)));
        nganhEdit.setVsat(String.valueOf(model.getValueAt(selectedRow, 9)));
        nganhEdit.setSlXtt(Integer.parseInt(String.valueOf(model.getValueAt(selectedRow, 10))));
        nganhEdit.setSlDgnl(Integer.parseInt(String.valueOf(model.getValueAt(selectedRow, 11))));
        nganhEdit.setSlVsat(Integer.parseInt(String.valueOf(model.getValueAt(selectedRow, 12))));
        nganhEdit.setSlThpt(String.valueOf(model.getValueAt(selectedRow, 13)));

        NganhDialog dialog = new NganhDialog(SwingUtilities.getWindowAncestor(view), nganhEdit);
        dialog.setVisible(true);

        Nganh updatedNganh = dialog.getNganhResult();
        if (updatedNganh != null) {
            try {
                service.capNhatNganh(updatedNganh);
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
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một ngành trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNganh = String.valueOf(view.getDefaultTableModel().getValueAt(selectedRow, 0));
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa ngành " + maNganh + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.xoaNganh(maNganh);
                loadData();
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel (.xlsx) chứa danh sách Ngành");
        
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
                        Nganh nganh = new Nganh();
                        nganh.setMaNganh(row.getCell(0).getStringCellValue());
                        nganh.setTenNganh(row.getCell(1).getStringCellValue());
                        nganh.setToHopGoc(row.getCell(2).getStringCellValue());
                        
                        nganh.setChiTieu((int) row.getCell(3).getNumericCellValue());
                        nganh.setDiemSan(BigDecimal.valueOf(row.getCell(4).getNumericCellValue()));
                        nganh.setDiemTrungTuyen(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
                        
                        nganh.setTuyenThang(row.getCell(6).getStringCellValue());
                        nganh.setDgnl(row.getCell(7).getStringCellValue());
                        nganh.setThpt(row.getCell(8).getStringCellValue());
                        nganh.setVsat(row.getCell(9).getStringCellValue());
                        
                        nganh.setSlXtt((int) row.getCell(10).getNumericCellValue());
                        nganh.setSlDgnl((int) row.getCell(11).getNumericCellValue());
                        nganh.setSlVsat((int) row.getCell(12).getNumericCellValue());
                        
                        if (row.getCell(13).getCellType() == CellType.NUMERIC) {
                            nganh.setSlThpt(String.valueOf((int) row.getCell(13).getNumericCellValue()));
                        } else {
                            nganh.setSlThpt(row.getCell(13).getStringCellValue());
                        }

                        service.themNganh(nganh);
                        successCount++;
                    } catch (Exception rowEx) {
                        System.out.println("Lỗi bỏ qua dòng " + (i + 1) + ": " + rowEx.getMessage());
                    }
                }
                loadData();
                JOptionPane.showMessageDialog(view, "Nhập thành công " + successCount + " ngành từ Excel!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi đọc file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}