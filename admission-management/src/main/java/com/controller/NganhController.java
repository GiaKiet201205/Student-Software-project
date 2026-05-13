package com.controller;

import com.entity.Nganh;
import com.service.NganhService;
import com.service.mapper.NganhRowMapper;
import com.service.mapper.SimpleRow;
import com.ui.dialog.NganhDialog;
import com.ui.panel.NganhPanel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
        }
    }

    public void handleAdd() {
        NganhDialog dialog = new NganhDialog(SwingUtilities.getWindowAncestor(view), null);
        dialog.setVisible(true);
        Nganh result = dialog.getNganhResult();
        if (result != null) {
            try {
                service.themNganh(result);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void handleEdit() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Chọn một ngành!");
            return;
        }

        DefaultTableModel model = view.getDefaultTableModel();
        // Lấy index thực tế trong trường hợp bảng bị filter (tìm kiếm)
        int modelRow = view.getBaseTable().convertRowIndexToModel(selectedRow);
        String maNganhCu = String.valueOf(model.getValueAt(modelRow, 0));

        Nganh nganhEdit = new Nganh();

        // Truy vết ID từ Database
        try {
            List<Nganh> all = service.getAllNganh();
            for (Nganh n : all) {
                if (n.getMaNganh().equals(maNganhCu)) {
                    nganhEdit.setIdNganh(n.getIdNganh()); 
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        nganhEdit.setMaNganh(maNganhCu);
        nganhEdit.setTenNganh(String.valueOf(model.getValueAt(modelRow, 1)));
        nganhEdit.setToHopGoc(String.valueOf(model.getValueAt(modelRow, 2)));
        nganhEdit.setChiTieu(Integer.parseInt(String.valueOf(model.getValueAt(modelRow, 3))));
        nganhEdit.setDiemSan(new BigDecimal(String.valueOf(model.getValueAt(modelRow, 4))));
        nganhEdit.setDiemTrungTuyen(new BigDecimal(String.valueOf(model.getValueAt(modelRow, 5))));
        nganhEdit.setTuyenThang(String.valueOf(model.getValueAt(modelRow, 6)));
        nganhEdit.setDgnl(String.valueOf(model.getValueAt(modelRow, 7)));
        nganhEdit.setThpt(String.valueOf(model.getValueAt(modelRow, 8)));
        nganhEdit.setVsat(String.valueOf(model.getValueAt(modelRow, 9)));
        nganhEdit.setSlXtt(Integer.parseInt(String.valueOf(model.getValueAt(modelRow, 10))));
        nganhEdit.setSlDgnl(Integer.parseInt(String.valueOf(model.getValueAt(modelRow, 11))));
        nganhEdit.setSlVsat(Integer.parseInt(String.valueOf(model.getValueAt(modelRow, 12))));
        nganhEdit.setSlThpt(String.valueOf(model.getValueAt(modelRow, 13)));

        NganhDialog dialog = new NganhDialog(SwingUtilities.getWindowAncestor(view), nganhEdit);
        dialog.setVisible(true);

        Nganh updatedNganh = dialog.getNganhResult();
        if (updatedNganh != null) {
            try {
                service.capNhatNganh(updatedNganh);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
            }
        }
    }

    public void handleDelete() {
        int selectedRow = view.getBaseTable().getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = view.getBaseTable().convertRowIndexToModel(selectedRow);
        String ma = String.valueOf(view.getDefaultTableModel().getValueAt(modelRow, 0));
        
        if (JOptionPane.showConfirmDialog(view, "Xóa " + ma + "?") == JOptionPane.YES_OPTION) {
            try {
                service.xoaNganh(ma);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
            }
        }
    }

    // 1. IMPORT NGÀNH & TỔ HỢP GỐC (Giữ nguyên logic cũ)
// 1. IMPORT NGÀNH & TỔ HỢP GỐC (Đã bổ sung chốt chặn DB)
    public void handleImport() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                Map<String, Nganh> nganhMap = new HashMap<>();
                NganhRowMapper mapper = new NganhRowMapper();
                
                // --- THÊM CHỐT CHẶN: Lấy danh sách mã ngành đã có trong DB ---
                java.util.Set<String> dbMaNganhSet = new java.util.HashSet<>();
                for (Nganh n : service.getAllNganh()) {
                    dbMaNganhSet.add(n.getMaNganh());
                }
                // -------------------------------------------------------------
                
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Map<Integer, String> cellData = new HashMap<>();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        cellData.put(j, getCellValueSafe(row.getCell(j)));
                    }
                    SimpleRow simpleRow = new SimpleRow(cellData);
                    Nganh mappedNganh = mapper.map(simpleRow);

                    if (mappedNganh == null || mappedNganh.getMaNganh() == null) continue;

                    String maNganh = mappedNganh.getMaNganh();

                    // KIỂM TRA: Nếu mã đã có trong DB thì bỏ qua dòng này luôn, không thêm mới
                    if (dbMaNganhSet.contains(maNganh)) {
                        continue;
                    }

                    Nganh existingNganh = nganhMap.get(maNganh);
                    
                    if (existingNganh == null) {
                        nganhMap.put(maNganh, mappedNganh);
                    } else {
                        if (mappedNganh.getToHopGoc() != null && !mappedNganh.getToHopGoc().isEmpty()) {
                            existingNganh.setToHopGoc(mappedNganh.getToHopGoc());
                        }
                    }
                }
                
                int successCount = 0;
                for (Nganh n : nganhMap.values()) {
                    try {
                        service.themNganh(n);
                        successCount++;
                    } catch (Exception ex) {
                        System.out.println("Lỗi khi thêm: " + n.getMaNganh() + " - " + ex.getMessage());
                    }
                }
                
                loadData();
                JOptionPane.showMessageDialog(view, "Đã thêm mới " + successCount + " ngành chưa có trong hệ thống!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
            }
        }
    }

    // 2. IMPORT ĐIỂM SÀN (Đọc theo ảnh thứ 1)
    public void handleImportDiemSan() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                
                // Lấy toàn bộ ngành hiện có để map với file Excel
                List<Nganh> allNganh = service.getAllNganh();
                Map<String, Nganh> dbNganhMap = new HashMap<>();
                for (Nganh n : allNganh) {
                    dbNganhMap.put(n.getMaNganh(), n);
                }

                int successCount = 0;
                // Duyệt từ dòng 1 (bỏ qua header dòng 0)
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    // Mã xét tuyển ở cột 1 (B)
                    String maNganh = getCellValueSafe(row.getCell(1));
                    // Ngưỡng đầu vào ở cột 3 (D)
                    String diemSanStr = getCellValueSafe(row.getCell(3));

                    if (!maNganh.isEmpty() && dbNganhMap.containsKey(maNganh)) {
                        try {
                            // Xử lý dấu phẩy Excel Việt Nam (24,5 -> 24.5)
                            diemSanStr = diemSanStr.replace(",", ".");
                            BigDecimal diemSan = new BigDecimal(diemSanStr);
                            
                            Nganh nganhToUpdate = dbNganhMap.get(maNganh);
                            nganhToUpdate.setDiemSan(diemSan);
                            
                            service.capNhatNganh(nganhToUpdate);
                            successCount++;
                        } catch (Exception ex) {
                            System.out.println("Lỗi parse điểm tại dòng " + i + ": " + ex.getMessage());
                        }
                    }
                }
                
                loadData();
                JOptionPane.showMessageDialog(view, "Đã cập nhật Điểm Sàn cho " + successCount + " ngành!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi đọc file Excel: " + ex.getMessage());
            }
        }
    }

    // 3. IMPORT CHỈ TIÊU (Đọc theo ảnh thứ 2)
    public void handleImportChiTieu() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                
                List<Nganh> allNganh = service.getAllNganh();
                Map<String, Nganh> dbNganhMap = new HashMap<>();
                for (Nganh n : allNganh) {
                    dbNganhMap.put(n.getMaNganh(), n);
                }

                int successCount = 0;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    // Mã CTĐT ở cột 1 (B)
                    String maNganh = getCellValueSafe(row.getCell(1));
                    // Chỉ tiêu chốt ở cột 3 (D)
                    String chiTieuStr = getCellValueSafe(row.getCell(3));

                    if (!maNganh.isEmpty() && dbNganhMap.containsKey(maNganh)) {
                        try {
                            // Ép kiểu sang double trước phòng trường hợp excel lưu số nguyên dạng 40.0
                            int chiTieu = (int) Double.parseDouble(chiTieuStr);
                            
                            Nganh nganhToUpdate = dbNganhMap.get(maNganh);
                            nganhToUpdate.setChiTieu(chiTieu);
                            
                            service.capNhatNganh(nganhToUpdate);
                            successCount++;
                        } catch (Exception ex) {
                            System.out.println("Lỗi parse chỉ tiêu tại dòng " + i + ": " + ex.getMessage());
                        }
                    }
                }
                
                loadData();
                JOptionPane.showMessageDialog(view, "Đã cập nhật Chỉ Tiêu cho " + successCount + " ngành!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi đọc file Excel: " + ex.getMessage());
            }
        }
    }

    // Hàm lấy data an toàn (Đã nâng cấp để giữ lại số thập phân cho Điểm Sàn)
private String getCellValueSafe(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        if (cellType == CellType.NUMERIC) {
            double val = cell.getNumericCellValue();
            if (val == (long) val) {
                return String.valueOf((long) val);
            } else {
                return String.valueOf(val);
            }
        } else if (cellType == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}