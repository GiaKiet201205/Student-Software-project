package com.ui.panel;

import java.util.List;
import com.config.AppConfig;
import com.controller.NguyenVongXetTuyenController;
import com.entity.NguyenVongXetTuyen;
import com.service.NVXTImport.DiemChuanExportService;
import com.service.NVXTImport.NguyenVongImportService;
import com.service.NVXTImport.XetTuyenService;
import com.service.NVXTImport.model.ImportResult;
import com.ui.data_form.DataFormNguyenVongXetTuyen;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;

public class NguyenVongXetTuyenPanel extends BasePanel {
    private NguyenVongXetTuyenController controller = new NguyenVongXetTuyenController();
    private XetTuyenService xetTuyenService = null;
    /** Nút xuất điểm chuẩn — chỉ bật sau khi xét tuyển hoàn tất */
    private BaseButton btnExportDiemChuan;

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

        BasePanel headerButtonPanel = new BasePanel(AppConfig.COLOR_WHITE,0);
        headerButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,0));
        BaseButton addButton = new BaseButton("Thêm mới");
        BaseButton btnRefresh = new BaseButton("Tải lại");
        headerButtonPanel.add(btnRefresh);
        headerButtonPanel.add(addButton);

        header.add(headerButtonPanel,BorderLayout.EAST);
        contentPanel.add(header,BorderLayout.NORTH);

        //table
        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE,15);
        tablePanel.setLayout(new BorderLayout());
        String[] columns = {
                "Id",
                "CCCD",
                "Mã ngành",
                "Thứ tự",
                "Điểm THXT",
                "Điểm UTQD",
                "Điểm Cộng",
                "Điểm Xét Tuyển",
                "Kết Quả",
                "Keys",
                "Phương Thức",
                "TT_THM"
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

        BaseButton btnXetTuyen = new BaseButton("Xét tuyển", new Color(39,174,96));
        BaseButton btnReadExcel = new BaseButton("Đọc file", new Color(39,174,96));
        BaseButton btnEdit = new BaseButton("Chỉnh sửa");
        BaseButton btnDelete = new BaseButton("Xóa", new Color(220,53,69));
        btnExportDiemChuan = new BaseButton("Xuất điểm chuẩn", new Color(41,128,185));
        btnExportDiemChuan.setEnabled(false); // chỉ bật sau khi xét tuyển xong

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnReadExcel);
        footer.add(btnXetTuyen);
        footer.add(btnExportDiemChuan);

        contentPanel.add(footer,BorderLayout.SOUTH);

        addButton.addActionListener(e -> functionAddData());
        btnEdit.addActionListener(e -> functionEditData());
        btnDelete.addActionListener(e -> functionDeleteData());
        searchButton.addActionListener(e -> functionSearchData());
        btnReadExcel.addActionListener(e -> functionReadExcel());
        btnXetTuyen.addActionListener(e -> functionXetTuyen());
        btnRefresh.addActionListener(e -> loadTable());
        btnExportDiemChuan.addActionListener(e -> functionExportDiemChuan());

        add(contentPanel,BorderLayout.CENTER);
    }

    // load lại bảng sau khi thêm/sửa/xóa
    public void loadTable(){
        model.setRowCount(0);
        List<NguyenVongXetTuyen> list = controller.getNguyenVongXetTuyen();
        if(list.isEmpty()) { 
            btnExportDiemChuan.setEnabled(false); 
            return; 
        }
        list.forEach(nv -> {
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
        btnExportDiemChuan.setEnabled(hasValidXetTuyenResults());
    }

    //TODO: chức năng thêm, sửa, xóa, tìm kiếm, đọc file excel, 
    private void functionAddData(){
        DataFormNguyenVongXetTuyen form = new DataFormNguyenVongXetTuyen();
        form.txtIdNv.setEditable(false);    form.txtIdNv.setFocusable(false);
        form.txtIdNv.setBackground(Color.GRAY);
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
        form.txtIdNv.setEditable(false); form.txtIdNv.setFocusable(false);
        form.txtIdNv.setBackground(Color.GRAY);
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

    private void functionReadExcel() {
        JFileChooser chooser = new JFileChooser();
        
        try {
            JOptionPane.showMessageDialog(this, "Chọn file NGUYỆN VỌNG XÉT TUYỂN");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileNguyenVong = chooser.getSelectedFile();
            
            JOptionPane.showMessageDialog(this, "Chọn file TỔ HỢP MÔN");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileToHop = chooser.getSelectedFile();
            
            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đang xử lý...", true);
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            progressDialog.add(progressBar, BorderLayout.CENTER);
            progressDialog.setSize(400, 80);
            progressDialog.setLocationRelativeTo(this);
            
            SwingWorker<ImportResult, Integer> worker = new SwingWorker<>() {
                @Override
                protected ImportResult doInBackground() throws Exception {
                    NguyenVongImportService service = new NguyenVongImportService();
                    return service.importNguyenVong(fileNguyenVong, fileToHop, 
                        progress -> {
                            publish(progress);
                            progressBar.setValue(progress);
                        });
                }
                
                @Override
                protected void process(List<Integer> chunks) {
                    int latest = chunks.get(chunks.size() - 1);
                    progressBar.setValue(latest);
                }
                
                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        ImportResult result = get();
                        JOptionPane.showMessageDialog(NguyenVongXetTuyenPanel.this,
                            result.getSummary(),
                            "Kết quả import", JOptionPane.INFORMATION_MESSAGE);
                        loadTable();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(NguyenVongXetTuyenPanel.this,
                            "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
            progressDialog.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void functionXetTuyen() {
        JFileChooser chooser = new JFileChooser();

        try {
            JOptionPane.showMessageDialog(this, "Chọn file CHỈ TIÊU");
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File fileChiTieu = chooser.getSelectedFile();

            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đang xét tuyển...", true);
            JLabel statusLabel = new JLabel("Đang xử lý, vui lòng chờ...", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressDialog.setLayout(new BorderLayout(0, 8));
            progressDialog.add(statusLabel, BorderLayout.CENTER);
            progressDialog.add(progressBar, BorderLayout.SOUTH);
            progressDialog.setSize(340, 90);
            progressDialog.setLocationRelativeTo(this);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    xetTuyenService = new XetTuyenService();
                    xetTuyenService.loadChiTieu(fileChiTieu);
                    xetTuyenService.xetTuyen();
                    return null;
                }    
                
                @Override
                protected void done() {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(NguyenVongXetTuyenPanel.this,
                        "Xét tuyển hoàn tất!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadTable();
                }
            };

            worker.execute();
            progressDialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void functionExportDiemChuan() {
        if (!hasValidXetTuyenResults()) {
            JOptionPane.showMessageDialog(this,
                "Chưa có kết quả xét tuyển trong hệ thống!\n"
                + "Vui lòng chạy xét tuyển trước.",
                "Chưa có dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu file điểm chuẩn");
        chooser.setSelectedFile(new File("diem_chuan_result.xlsx"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File outputFile = chooser.getSelectedFile();
        if (!outputFile.getName().toLowerCase().endsWith(".xlsx")) {
            outputFile = new File(outputFile.getAbsolutePath() + ".xlsx");
        }

        final File finalOutputFile = outputFile;

        // Progress dialog
        JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đang xuất...", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressDialog.add(progressBar);
        progressDialog.setSize(280, 70);
        progressDialog.setLocationRelativeTo(this);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                DiemChuanExportService exportService = new DiemChuanExportService();
                exportService.exportDiemChuan(finalOutputFile);
                return null;
            }

            @Override
            protected void done() {
                progressDialog.dispose();
                try {
                    get();
                    JOptionPane.showMessageDialog(
                        NguyenVongXetTuyenPanel.this,
                        "<html><b>Xuất điểm chuẩn thành công!</b><br>"
                        + "File đã lưu tại:<br><i>" + finalOutputFile.getAbsolutePath() + "</i></html>",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        NguyenVongXetTuyenPanel.this,
                        "Lỗi khi xuất file:\n" + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }

    private boolean hasValidXetTuyenResults() {
        List<NguyenVongXetTuyen> list = controller.getNguyenVongXetTuyen();
        if (list.isEmpty()) return false;
        
        // Kiểm tra xem có bản ghi nào có kết quả khác "CHUA_XET" không
        return list.stream()
                .anyMatch(nv -> nv.getKetQua() != null 
                        && !"CHUA_XET".equals(nv.getKetQua())
                        && !nv.getKetQua().trim().isEmpty());
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
            if (Double.parseDouble(diemCong) < 0 || Double.parseDouble(diemCong) > 4) {
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