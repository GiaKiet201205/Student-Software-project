package com.ui.panel;

import java.util.List;
import com.config.AppConfig;
import com.controller.NguyenVongXetTuyenController;
import com.entity.DiemCongXetTuyen;
import com.entity.NguyenVongXetTuyen;
import com.ui.data_form.DataFormDiemCongXetTuyen;
import com.ui.data_form.DataFormNguyenVongXetTuyen;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

public class NguyenVongXetTuyenPanel extends BasePanel {
    private NguyenVongXetTuyenController controller = new NguyenVongXetTuyenController();

    JTextField searchField;
    BaseTable table;
    DefaultTableModel model;

    public NguyenVongXetTuyenPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initComponents();
        loadTable();
    }

    public void initComponents() {
        JPanel contentPanel = new BasePanel(AppConfig.COLOR_BACKGROUND, 0);
        contentPanel.setLayout(new BorderLayout(20,20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //Header
        BasePanel header = new BasePanel(AppConfig.COLOR_WHITE,15);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0,60));

        JLabel titleLabel = new JLabel("NGUYỆN VỌNG XÉT TUYỂN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI",Font.BOLD,20));
        header.add(titleLabel,BorderLayout.WEST);

        //Thanh tìm kiếm
        BasePanel searchPanel = new BasePanel(AppConfig.COLOR_WHITE,0);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,-4));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(500,30));
        searchField.setFont(new Font("Segoe UI",Font.PLAIN,17));

        String placeholder = "Nhập để tìm kiếm...";
        searchField.setText(placeholder);
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if(searchField.getText().equals(placeholder)){
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if(searchField.getText().isEmpty()){
                    searchField.setText(placeholder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        BaseButton searchButton = new BaseButton("Tìm kiếm");
        searchButton.setFont(new Font("Segoe UI",Font.BOLD,17));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        header.add(searchPanel,BorderLayout.CENTER);

        BaseButton addButton = new BaseButton("Thêm mới");
        header.add(addButton,BorderLayout.EAST);
        contentPanel.add(header,BorderLayout.NORTH);

        //table
        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE,15);
        tablePanel.setLayout(new BorderLayout());
        String[] columns = {
                "idnv",
                "nn_cccd",
                "nv_manganh",
                "nv_tt",
                "diem_thxt",
                "diem_utqd",
                "diem_cong",
                "diem_xettuyen",
                "nv_ketqua",
                "nv_keys",
                "tt_phuongthuc",
                "tt_thm"
        };

        model = new DefaultTableModel(columns,0);
        table = new BaseTable(model);

        JTableHeader headerTable = table.getTableHeader();
        headerTable.setBackground(AppConfig.COLOR_PRIMARY);
        headerTable.setForeground(Color.WHITE);
        headerTable.setFont(new Font("Segoe UI",Font.BOLD,14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane,BorderLayout.CENTER);
        contentPanel.add(tablePanel,BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        BaseButton btnEdit = new BaseButton("Chỉnh sửa");
        BaseButton btnDelete = new BaseButton("Xóa", new Color(220,53,69));
        BaseButton btnReadExcel = new BaseButton("Đọc file", new Color(39,174,96));
        BaseButton btnRefresh = new BaseButton("Tải lại danh sách", new Color(41,128,185));

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnReadExcel);
        footer.add(btnRefresh);

        contentPanel.add(footer,BorderLayout.SOUTH);

        addButton.addActionListener(e -> functionAddData());
        btnEdit.addActionListener(e -> functionEditData());
        btnDelete.addActionListener(e -> functionDeleteData());
        searchButton.addActionListener(e -> functionSearchData());
        btnReadExcel.addActionListener(e -> functionReadExcel());
        btnRefresh.addActionListener(e -> loadTable());

        add(contentPanel,BorderLayout.CENTER);
    }

    // load lại bảng sau khi thêm/sửa/xóa
    public void loadTable(){
        model.setRowCount(0);
        controller.getNguyenVongXetTuyen().forEach(nv -> {
            model.addRow(new Object[]{
                    nv.getIdNv(),
                    nv.getCccd(),
                    nv.getMaNganh(),
                    nv.getThuTu(),
                    nv.getDiemThXT(),
                    nv.getDiemUuTienQD(),
                    nv.getDiemCong(),
                    nv.getDiemXetTuyen(),
                    nv.getKetQua(),
                    nv.getNvKeys(),
                    nv.getPhuongThuc(),
                    nv.getTtThm()
            });
        });
    }

    //TODO: chức năng thêm, sửa, xóa, tìm kiếm, đọc file excel, 
    private void functionAddData(){
        DataFormNguyenVongXetTuyen form = new DataFormNguyenVongXetTuyen();
        form.txtIdNv.setText("Hệ thống sẽ tự thêm vào trường này, người dùng không cần nhập!");
        form.btnSave.addActionListener(e->{
            NguyenVongXetTuyen nv = new NguyenVongXetTuyen();
            String cccd = form.txtCccd.getText().trim();
            String maNganh = form.txtMaNganh.getText().trim();
            String thuTu = form.txtThuTu.getText().trim();
            String diemThXT = form.txtDiemThXT.getText().trim();
            String diemUuTienQD = form.txtDiemUuTienQD.getText().trim();
            String diemCong = form.txtDiemCong.getText().trim();
            String diemXetTuyen = form.txtDiemXetTuyen.getText().trim();
            String ketQua = form.txtKetQua.getText().trim();
            String nvKeys = form.txtNvKeys.getText().trim();
            String phuongThuc = form.txtPhuongThuc.getText().trim();
            String ttThm = form.txtTtThm.getText().trim();
            if(validateInput(cccd, maNganh, thuTu, diemThXT, diemUuTienQD, diemCong, diemXetTuyen, ketQua, nvKeys, phuongThuc, ttThm)) {
                nv.setCccd(cccd);
                nv.setMaNganh(maNganh);
                nv.setThuTu(Integer.parseInt(thuTu));
                nv.setDiemThXT(BigDecimal.valueOf(Double.parseDouble(diemThXT)));
                nv.setDiemUuTienQD(BigDecimal.valueOf(Double.parseDouble(diemUuTienQD)));
                nv.setDiemCong(BigDecimal.valueOf(Double.parseDouble(diemCong)));
                nv.setDiemXetTuyen(BigDecimal.valueOf(Double.parseDouble(diemXetTuyen)));
                nv.setKetQua(ketQua);
                nv.setNvKeys(nvKeys);
                nv.setPhuongThuc(phuongThuc);
                nv.setTtThm(ttThm);

                controller.addNguyenVongXetTuyen(nv);
                loadTable();
                form.dispose();
            } else {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ và đúng định dạng các trường dữ liệu!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    private void functionEditData(){
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nguyện vọng để chỉnh sửa!", "Chưa chọn nguyện vọng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer idNv = (Integer) model.getValueAt(selectedRow, 0);
        NguyenVongXetTuyen nv = controller.getNguyenVongXetTuyenById(idNv);
        if(nv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nguyện vọng có ID: " + idNv, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DataFormNguyenVongXetTuyen form = new DataFormNguyenVongXetTuyen();
        form.txtIdNv.setText(String.valueOf(nv.getIdNv()));
        form.txtIdNv.setEditable(false);
        form.txtCccd.setText(nv.getCccd());
        form.txtMaNganh.setText(nv.getMaNganh());
        form.txtThuTu.setText(String.valueOf(nv.getThuTu()));
        form.txtDiemThXT.setText(String.valueOf(nv.getDiemThXT()));
        form.txtDiemUuTienQD.setText(String.valueOf(nv.getDiemUuTienQD()));
        form.txtDiemCong.setText(String.valueOf(nv.getDiemCong()));
        form.txtDiemXetTuyen.setText(String.valueOf(nv.getDiemXetTuyen()));
        form.txtKetQua.setText(nv.getKetQua());
        form.txtNvKeys.setText(nv.getNvKeys());
        form.txtPhuongThuc.setText(nv.getPhuongThuc());
        form.txtTtThm.setText(nv.getTtThm());

        form.btnSave.addActionListener(e->{
            String cccd = form.txtCccd.getText().trim();
            String maNganh = form.txtMaNganh.getText().trim();
            String thuTu = form.txtThuTu.getText().trim();
            String diemThXT = form.txtDiemThXT.getText().trim();
            String diemUuTienQD = form.txtDiemUuTienQD.getText().trim();
            String diemCong = form.txtDiemCong.getText().trim();
            String diemXetTuyen = form.txtDiemXetTuyen.getText().trim();
            String ketQua = form.txtKetQua.getText().trim();
            String nvKeys = form.txtNvKeys.getText().trim();
            String phuongThuc = form.txtPhuongThuc.getText().trim();
            String ttThm = form.txtTtThm.getText().trim();
            if(validateInput(cccd, maNganh, thuTu, diemThXT, diemUuTienQD, diemCong, diemXetTuyen, ketQua, nvKeys, phuongThuc, ttThm)) {
                nv.setCccd(cccd);
                nv.setMaNganh(maNganh);
                nv.setThuTu(Integer.parseInt(thuTu));
                nv.setDiemThXT(BigDecimal.valueOf(Double.parseDouble(diemThXT)));
                nv.setDiemUuTienQD(BigDecimal.valueOf(Double.parseDouble(diemUuTienQD)));
                nv.setDiemCong(BigDecimal.valueOf(Double.parseDouble(diemCong)));
                nv.setDiemXetTuyen(BigDecimal.valueOf(Double.parseDouble(diemXetTuyen)));
                nv.setKetQua(ketQua);
                nv.setNvKeys(nvKeys);
                nv.setPhuongThuc(phuongThuc);
                nv.setTtThm(ttThm);

                controller.updateNguyenVongXetTuyen(nv);
                loadTable();
                form.dispose();
            } else {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ và đúng định dạng các trường dữ liệu!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    private void functionDeleteData(){
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nguyện vọng để xóa!", "Chưa chọn nguyện vọng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer idNv = (Integer) model.getValueAt(selectedRow, 0);
        NguyenVongXetTuyen nv = controller.getNguyenVongXetTuyenById(idNv);
        if(nv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nguyện vọng có ID: " + idNv, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nguyện vọng có ID: " + idNv + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            controller.deleteNguyenVongXetTuyen(nv);
            loadTable();
        }
    }

    private void functionSearchData(){
        String keyword = searchField.getText().trim();
        if(keyword.isEmpty() || keyword.equals("Nhập để tìm kiếm...")) {
            loadTable();
            return;
        }
        model.setRowCount(0);
        List<NguyenVongXetTuyen> searchResults = controller.searchNguyenVongXetTuyen(keyword);
        if(searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nguyện vọng nào khớp với từ khóa: " + keyword, "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
        } else {
            searchResults.forEach(nv -> {
                model.addRow(new Object[]{
                        nv.getIdNv(),
                        nv.getCccd(),
                        nv.getMaNganh(),
                        nv.getThuTu(),
                        nv.getDiemThXT(),
                        nv.getDiemUuTienQD(),
                        nv.getDiemCong(),
                        nv.getDiemXetTuyen(),
                        nv.getKetQua(),
                        nv.getNvKeys(),
                        nv.getPhuongThuc(),
                        nv.getTtThm()
                });
            });
        }
    }

    private void functionReadExcel(){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                int countError = 0;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    String cccd = row.getCell(0).getStringCellValue();
                    String maNganh = row.getCell(1).getStringCellValue();
                    int thuTu = (int) row.getCell(2).getNumericCellValue();
                    double diemThXT = row.getCell(3).getNumericCellValue();
                    double diemUuTienQD = row.getCell(4).getNumericCellValue();
                    double diemCong = row.getCell(5).getNumericCellValue();
                    double diemXetTuyen = row.getCell(6).getNumericCellValue();
                    String ketQua = row.getCell(7).getStringCellValue();
                    String nvKeys = row.getCell(8).getStringCellValue();
                    String phuongThuc = row.getCell(9).getStringCellValue();
                    String ttThm = row.getCell(10).getStringCellValue();
                    
                    if(validateInput(cccd, maNganh, String.valueOf(thuTu), String.valueOf(diemThXT), String.valueOf(diemUuTienQD), String.valueOf(diemCong), String.valueOf(diemXetTuyen), ketQua, nvKeys, phuongThuc, ttThm)) {
                        NguyenVongXetTuyen nv = new NguyenVongXetTuyen();
                        nv.setCccd(cccd);
                        nv.setMaNganh(maNganh);
                        nv.setThuTu(thuTu);
                        nv.setDiemThXT(BigDecimal.valueOf(diemThXT));
                        nv.setDiemUuTienQD(BigDecimal.valueOf(diemUuTienQD));
                        nv.setDiemCong(BigDecimal.valueOf(diemCong));
                        nv.setDiemXetTuyen(BigDecimal.valueOf(diemXetTuyen));
                        nv.setKetQua(ketQua);
                        nv.setNvKeys(nvKeys);
                        nv.setPhuongThuc(phuongThuc);
                        nv.setTtThm(ttThm);

                        controller.addNguyenVongXetTuyen(nv);
                    } else {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Import Excel thành công! Số lỗi: " + countError);
                loadTable();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }

    }

    //kiểm tra dữ liệu nhập vào
    private boolean validateInput(String cccd, String manganh, String thuTu, String diemThXT, String diemUuTienQD, String diemCong, String diemXetTuyen, String ketQua, String nvKeys, String phuongThuc, String ttThm) {
        if (cccd.isEmpty() || manganh.isEmpty() || thuTu.isEmpty() || diemThXT.isEmpty() || diemUuTienQD.isEmpty() || diemXetTuyen.isEmpty() || ketQua.isEmpty() || nvKeys.isEmpty() || phuongThuc.isEmpty() || ttThm.isEmpty()) {
            return false;
        }
        try {
            if(Long.parseLong(cccd) < 0 || cccd.length() != 12) {
                return false;
            }
            if (Integer.parseInt(thuTu) < 0) {
                return false;
            }
            if (Double.parseDouble(diemThXT) < 0 || Double.parseDouble(diemThXT) > 30) {
                return false;
            }
            if (Double.parseDouble(diemUuTienQD) < 0 || Double.parseDouble(diemUuTienQD) > 3) {
                return false;
            }
            if (Double.parseDouble(diemCong) < 0 || Double.parseDouble(diemCong) > 30) {
                return false;
            }
            if (Double.parseDouble(diemXetTuyen) < 0 || Double.parseDouble(diemXetTuyen) > 32.75) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}