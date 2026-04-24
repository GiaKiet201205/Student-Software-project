package com.ui.panel;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;

import java.io.File;
import java.math.BigDecimal;

import com.config.AppConfig;
import com.controller.DiemCongXetTuyenController;
import com.entity.DiemCongXetTuyen;
import com.service.ExcelImportDCXTService;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;
import com.ui.data_form.DataFormDiemCongXetTuyen;
import com.ui.common.BaseButton;

public class DiemCongXetTuyenPanel extends BasePanel {
    private DiemCongXetTuyenController controller = new DiemCongXetTuyenController();

    JTextField searchField;
    BaseTable table;
    DefaultTableModel model;

    public DiemCongXetTuyenPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initComponents();
        loadTable();
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
        
        BaseButton searchButton = new BaseButton("Tìm kiếm");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        header.add(searchPanel, BorderLayout.CENTER);

        BaseButton addButton = new BaseButton("Thêm mới");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.add(addButton, BorderLayout.EAST);
        contentPanel.add(header, BorderLayout.NORTH);

        // Table
        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());
        String[] columns = {"iddiemcong", "ts_cccd", "manganh", "matohop", "phuongthuc", "diemCC", "diemUtxt", "diemTong", "ghichu", "dc_keys"};
        model = new DefaultTableModel(columns, 0);
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

        BaseButton btnEdit = new BaseButton("Chỉnh sửa");
        BaseButton btnDelete = new BaseButton("Xóa", new Color(220, 53, 69));
        BaseButton btnReadExcel = new BaseButton("Đọc file", new Color(39, 174, 96));
        BaseButton btnRefresh = new BaseButton("Tải lại danh sách", new Color(41, 128, 185));

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnReadExcel);
        footer.add(btnRefresh);
        contentPanel.add(footer, BorderLayout.SOUTH);

        addButton.addActionListener(e -> functionAddData());
        btnEdit.addActionListener(e -> functionEditData());
        btnDelete.addActionListener(e -> functionDeleteData());
        searchButton.addActionListener(e -> functionSearchData());
        btnReadExcel.addActionListener(e -> functionReadExcel());
        btnRefresh.addActionListener(e -> loadTable());

        add(contentPanel, BorderLayout.CENTER);
    }
    
    //TODO: chức năng thêm, sửa, xóa, tìm kiếm, đọc file excel, load lại bảng sau khi thêm/sửa/xóa
    private void functionAddData() {
        DataFormDiemCongXetTuyen form = new DataFormDiemCongXetTuyen();
        form.txtIdDiemCong.setEditable(false); form.txtIdDiemCong.setFocusable(false);
        form.txtIdDiemCong.setBackground(Color.GRAY);
        form.btnSave.addActionListener(e -> {
            String cccd = form.txtCccd.getText().trim();
            String manganh = form.txtMaNganh.getText().trim();
            String matohop = form.txtMaToHop.getText().trim();
            String phuongthuc = form.txtPhuongThuc.getText().trim();
            String diemCC = form.txtDiemCC.getText().trim();
            if(diemCC.isEmpty()) diemCC = "0";
            String diemUtxt = form.txtDiemUtxt.getText().trim();
            if(diemUtxt.isEmpty()) diemUtxt = "0";
            String diemTong = form.txtDiemTong.getText().trim();
            String ghichu = form.txtGhiChu.getText().trim();
            String dcKeys = form.txtDcKeys.getText().trim();

            if(!validateInput(cccd, manganh, matohop, diemCC, diemUtxt, phuongthuc, diemTong, dcKeys)) {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ và đúng định dạng các trường dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DiemCongXetTuyen entity = new DiemCongXetTuyen();
            entity.setCccd(cccd);
            entity.setMaNganh(manganh);
            entity.setMaToHop(matohop);
            entity.setPhuongThuc(phuongthuc);
            entity.setDiemCC(BigDecimal.valueOf(Double.parseDouble(diemCC)));
            entity.setDiemUuTienXT(BigDecimal.valueOf(Double.parseDouble(diemUtxt)));
            entity.setDiemTong(BigDecimal.valueOf(Double.parseDouble(diemTong)));
            entity.setGhiChu(ghichu);
            entity.setDcKeys(dcKeys);

            Boolean isAdded = controller.addDiemCongXetTuyen(entity);
            if(!isAdded) {
                JOptionPane.showMessageDialog(form, "Thêm mới thất bại! Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loadTable();
            form.dispose();
        });
        form.setVisible(true);
    }

    private void functionEditData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để chỉnh sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idDiemCongStr = model.getValueAt(selectedRow, 0).toString();
        DiemCongXetTuyen entity = controller.getDiemCongXetTuyenById(Integer.parseInt(idDiemCongStr));
        if(entity == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu điểm cộng cần chỉnh sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataFormDiemCongXetTuyen form = new DataFormDiemCongXetTuyen();
        form.txtIdDiemCong.setText(entity.getIdDiemCong().toString());
        form.txtIdDiemCong.setBackground(Color.GRAY);
        form.txtIdDiemCong.setEditable(false); form.txtIdDiemCong.setFocusable(false);
        form.txtCccd.setText(entity.getCccd());
        form.txtMaNganh.setText(entity.getMaNganh());
        form.txtMaToHop.setText(entity.getMaToHop());
        form.txtPhuongThuc.setText(entity.getPhuongThuc());
        form.txtDiemCC.setText(entity.getDiemCC().toString());
        form.txtDiemUtxt.setText(entity.getDiemUuTienXT().toString());
        form.txtDiemTong.setText(entity.getDiemTong().toString());
        form.txtGhiChu.setText(entity.getGhiChu());
        form.txtDcKeys.setText(entity.getDcKeys());

        form.btnSave.addActionListener(e -> {
            String cccd = form.txtCccd.getText();
            String manganh = form.txtMaNganh.getText();
            String matohop = form.txtMaToHop.getText();
            String phuongthuc = form.txtPhuongThuc.getText();
            String diemCC = form.txtDiemCC.getText();
            if(diemCC.isEmpty()) diemCC = "0";
            String diemUtxt = form.txtDiemUtxt.getText();
            if(diemUtxt.isEmpty()) diemUtxt = "0";
            String diemTong = form.txtDiemTong.getText();
            String ghichu = form.txtGhiChu.getText();
            String dcKeys = form.txtDcKeys.getText();

            if(!validateInput(cccd, manganh, matohop, diemCC, diemUtxt, phuongthuc, diemTong, dcKeys)) {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ và đúng định dạng các trường dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            entity.setCccd(cccd);
            entity.setMaNganh(manganh);
            entity.setMaToHop(matohop);
            entity.setPhuongThuc(phuongthuc);
            entity.setDiemCC(BigDecimal.valueOf(Double.parseDouble(diemCC)));
            entity.setDiemUuTienXT(BigDecimal.valueOf(Double.parseDouble(diemUtxt)));
            entity.setDiemTong(BigDecimal.valueOf(Double.parseDouble(diemTong)));
            entity.setGhiChu(ghichu);
            entity.setDcKeys(dcKeys);

            Boolean isUpdated = controller.updateDiemCongXetTuyen(entity);
            if(!isUpdated) {
                JOptionPane.showMessageDialog(form, "Cập nhật thất bại! Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            loadTable();
            form.dispose();
        });
        form.setVisible(true);
    }

    private void functionDeleteData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa dữ liệu điểm cộng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String idDiemCongStr = model.getValueAt(selectedRow, 0).toString();
        DiemCongXetTuyen entity = controller.getDiemCongXetTuyenById(Integer.parseInt(idDiemCongStr));
        if(entity == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu điểm cộng cần xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.deleteDiemCongXetTuyen(entity);
        loadTable();
    }

    private void functionSearchData() {
        String keyString = searchField.getText();
        if(keyString.isEmpty() || keyString.equals("Nhập để tìm kiếm...")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            loadTable();
            return;
        }

        List<DiemCongXetTuyen> searchResults = controller.searchDiemCongXetTuyen(keyString);
        if(searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào phù hợp với từ khóa: " + keyString, "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
            return;
        } else {
            model.setRowCount(0);
            for(DiemCongXetTuyen dc : searchResults){
                model.addRow(new Object[]{
                        dc.getIdDiemCong(),
                        dc.getCccd(),
                        dc.getMaNganh(),
                        dc.getMaToHop(),
                        dc.getPhuongThuc(),
                        dc.getDiemCC(),
                        dc.getDiemUuTienXT(),
                        dc.getDiemTong(),
                        dc.getGhiChu(),
                        dc.getDcKeys()
                });
            }
        }
     }
    
    private void functionReadExcel() {
        JFileChooser chooser = new JFileChooser();

        try {
            JOptionPane.showMessageDialog(this, "Chọn file DANH SÁCH THÍ SINH");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileThiSinh = chooser.getSelectedFile();

            JOptionPane.showMessageDialog(this, "Chọn file ƯU TIÊN XÉT TUYỂN");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileUT = chooser.getSelectedFile();

            JOptionPane.showMessageDialog(this, "Chọn file NGUYỆN VỌNG");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileNV = chooser.getSelectedFile();

            JOptionPane.showMessageDialog(this, "Chọn file TỔ HỢP MÔN");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileTH = chooser.getSelectedFile();

            // ===== Progress Dialog =====
            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đang xử lý...", true);
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            JLabel lblStatus = new JLabel("Đang khởi tạo...");
            
            progressDialog.setLayout(new BorderLayout());
            progressDialog.add(progressBar, BorderLayout.CENTER);
            progressDialog.add(lblStatus, BorderLayout.SOUTH);
            progressDialog.setSize(400, 100);
            progressDialog.setLocationRelativeTo(this);
            
            SwingWorker<Integer, String> worker = new SwingWorker<>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    ExcelImportDCXTService service = new ExcelImportDCXTService();
                    
                    return service.processAllWithProgress(fileThiSinh, fileUT, fileNV, fileTH, 
                        progress -> {
                            publish("Đang xử lý: " + progress + "%");
                            progressBar.setValue(progress);
                        },
                        controller  
                    );
                }
                
                @Override
                protected void process(List<String> chunks) {
                    if (!chunks.isEmpty()) {
                        lblStatus.setText(chunks.get(chunks.size() - 1));
                    }
                }
                
                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        int count = get();
                        JOptionPane.showMessageDialog(DiemCongXetTuyenPanel.this,
                            "Import hoàn tất!\nĐã lưu " + count + " bản ghi vào database.",
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadTable();  
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(DiemCongXetTuyenPanel.this, 
                            "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
            progressDialog.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    private void loadTable() {
        model.setRowCount(0);
        List<DiemCongXetTuyen> list = controller.getDiemCongXetTuyen();
        for(DiemCongXetTuyen dc : list){
            model.addRow(new Object[]{
                    dc.getIdDiemCong(),
                    dc.getCccd(),
                    dc.getMaNganh(),
                    dc.getMaToHop(),
                    dc.getPhuongThuc(),
                    dc.getDiemCC(),
                    dc.getDiemUuTienXT(),
                    dc.getDiemTong(),
                    dc.getGhiChu(),
                    dc.getDcKeys()
            });
        }
    }

    //kiểm tra dữ liệu nhập vào
    private boolean validateInput(String cccd, String manganh, String matohop, String diemCC, String diemUtxt, String phuongthuc, String diemTong, String dcKeys) {
        if (cccd.isEmpty() || manganh.isEmpty() || matohop.isEmpty() || phuongthuc.isEmpty() || diemTong.isEmpty() || dcKeys.isEmpty()) {
            return false;
        }
        try {
            if(Long.parseLong(cccd) < 0 || cccd.length() != 12) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        if(!diemCC.isEmpty()) {
            try {
                if(Double.parseDouble(diemCC) < 0 || Double.parseDouble(diemCC) > 2) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if(!diemUtxt.isEmpty()) {
            try {
                if(Double.parseDouble(diemUtxt) < 0 || Double.parseDouble(diemUtxt) > 0.75) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        try {
            if(Double.parseDouble(diemTong) < 0 || Double.parseDouble(diemTong) > 32.75) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
