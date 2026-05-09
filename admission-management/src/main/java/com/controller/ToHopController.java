package com.controller;

import com.entity.ToHopMonThi;
import com.service.ToHopMonThiService;
import com.service.mapper.SimpleRow;
import com.service.mapper.ToHopRowMapper;
import com.ui.dialog.ToHopDialog;
import com.ui.panel.ToHopPanel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            view.hienThiDuLieuLenBang(list);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleAdd() {
        ToHopDialog dialog = new ToHopDialog(SwingUtilities.getWindowAncestor(view), null);
        dialog.setVisible(true);
        ToHopMonThi result = dialog.getToHopResult();
        if (result != null) {
            try {
                service.themToHop(result);
                loadData();
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void handleEdit() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng để sửa!");
            return;
        }

        DefaultTableModel model = view.getDefaultTableModel();
        String maToHopCu = (String) model.getValueAt(selectedRow, 0);

        ToHopMonThi editData = new ToHopMonThi();

        try {
            List<ToHopMonThi> all = service.getAllToHop();
            for (ToHopMonThi th : all) {
                if (th.getMaToHop().equals(maToHopCu)) {
                    editData.setIdToHop(th.getIdToHop()); // Đúng tên biến idToHop
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        editData.setMaToHop(maToHopCu);
        editData.setTenToHop((String) model.getValueAt(selectedRow, 1));
        editData.setMon1((String) model.getValueAt(selectedRow, 2));
        editData.setMon2((String) model.getValueAt(selectedRow, 3));
        editData.setMon3((String) model.getValueAt(selectedRow, 4));

        ToHopDialog dialog = new ToHopDialog(SwingUtilities.getWindowAncestor(view), editData);
        dialog.setVisible(true);

        ToHopMonThi result = dialog.getToHopResult();
        if (result != null) {
            try {
                service.capNhatToHop(result);
                loadData();
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi cập nhật: " + e.getMessage());
            }
        }
    }

    public void handleDelete() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Chọn một dòng để xóa!");
            return;
        }

        String id = (String) view.getDefaultTableModel().getValueAt(selectedRow, 0);
        if (JOptionPane.showConfirmDialog(view, "Xóa " + id + "?") == JOptionPane.YES_OPTION) {
            try {
                service.xoaToHop(id);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                ToHopRowMapper mapper = new ToHopRowMapper();
                Map<String, ToHopMonThi> toHopMap = new HashMap<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Map<Integer, String> cellData = new HashMap<>();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        cellData.put(j, getCellValueSafe(row.getCell(j)));
                    }
                    SimpleRow simpleRow = new SimpleRow(cellData);
                    ToHopMonThi toHop = mapper.map(simpleRow);

                    if (toHop != null && toHop.getMaToHop() != null) {
                        toHopMap.put(toHop.getMaToHop(), toHop);
                    }
                }

                int successCount = 0;
                for (ToHopMonThi th : toHopMap.values()) {
                    try {
                        service.themToHop(th);
                        successCount++;
                    } catch (Exception ex) {
                        System.out.println("Bỏ qua: " + th.getMaToHop());
                    }
                }

                loadData();
                JOptionPane.showMessageDialog(view, "Đã thêm " + successCount + " tổ hợp!");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
            }
        }
    }

    private String getCellValueSafe(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }
}