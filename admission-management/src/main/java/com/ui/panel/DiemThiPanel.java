package com.ui.panel;

import java.awt.*;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.config.AppConfig;
import com.controller.DiemThiXetTuyenController;
import com.entity.DiemThiXetTuyen;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;
import com.ui.common.BaseButton;
import com.ui.data_form.DataFormDiemThi;

public class DiemThiPanel extends BasePanel {

    private DiemThiXetTuyenController controller = new DiemThiXetTuyenController();

    JTextField searchField;
    BaseTable table;
    DefaultTableModel model;

    public DiemThiPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        setLayout(new BorderLayout());
        initComponents();
        loadTable();
    }

    public void initComponents() {
        JPanel contentPanel = new BasePanel(AppConfig.COLOR_BACKGROUND, 0);
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BasePanel header = new BasePanel(AppConfig.COLOR_WHITE, 15);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("QUẢN LÝ ĐIỂM THÍ SINH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(titleLabel, BorderLayout.WEST);

        BasePanel searchPanel = new BasePanel(AppConfig.COLOR_WHITE, 0);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -4));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30));

        BaseButton searchButton = new BaseButton("Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        header.add(searchPanel, BorderLayout.CENTER);

        // =====================================================================
        // TÍCH HỢP NÚT IMPORT VỚI MENU THẢ XUỐNG
        // =====================================================================
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        BaseButton btnImport = new BaseButton("📥 Nhập từ Excel ▼", new Color(46, 204, 113));
        JPopupMenu popupImport = new JPopupMenu();

        // Định nghĩa 3 chức năng nạp điểm
        JMenuItem itemTHPT = new JMenuItem("1. Nạp điểm THPT (Cấu trúc Ngang)");
        JMenuItem itemDGNL = new JMenuItem("2. Nạp điểm ĐGNL (Cấu trúc Dọc)");
        JMenuItem itemVSAT = new JMenuItem("3. Nạp điểm V-SAT (Cấu trúc Dọc)");

        // Truyền (Loại File, Mã Phương Thức) xuống hàm xử lý
        itemTHPT.addActionListener(e -> functionImportExcel("THPT", "3"));
        itemDGNL.addActionListener(e -> functionImportExcel("DGNL", "4"));
        itemVSAT.addActionListener(e -> functionImportExcel("VSAT", "5"));

        popupImport.add(itemTHPT);
        popupImport.add(itemDGNL);
        popupImport.add(itemVSAT);

        btnImport.addActionListener(e -> popupImport.show(btnImport, 0, btnImport.getHeight()));

        BaseButton addButton = new BaseButton("Thêm mới");

        actionPanel.add(btnImport);
        actionPanel.add(addButton);
        header.add(actionPanel, BorderLayout.EAST);
        contentPanel.add(header, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] columns = {"ID", "CCCD", "Số Báo Danh", "Phương Thức"};
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

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        BaseButton btnEdit = new BaseButton("Chỉnh sửa / Chi tiết");
        BaseButton btnDelete = new BaseButton("Xóa", new Color(220, 53, 69));
        BaseButton btnRefresh = new BaseButton("Tải lại", new Color(41, 128, 185));

        footer.add(btnEdit);
        footer.add(btnDelete);
        footer.add(btnRefresh);
        contentPanel.add(footer, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> functionAddData());
        btnEdit.addActionListener(e -> functionEditData());
        btnDelete.addActionListener(e -> functionDeleteData());
        searchButton.addActionListener(e -> functionSearchData());
        btnRefresh.addActionListener(e -> loadTable());

        add(contentPanel, BorderLayout.CENTER);
    }

    // =====================================================================
    // HÀM XỬ LÝ IMPORT EXCEL KẾT HỢP SWING-WORKER
    // =====================================================================
    private void functionImportExcel(String loaiFile, String phuongThuc) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel...");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();

            // Hiện con trỏ chuột xoay tròn báo hiệu hệ thống đang bận
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() throws Exception {
                    // Gọi controller dựa trên loại file
                    if (loaiFile.equals("THPT")) {
                        return controller.importDiemThpt(file, phuongThuc);
                    } else if(loaiFile.equals("DGNL")){
                        // ĐGNL và V-SAT dùng chung hàm xử lý cấu trúc Dọc
                        return controller.importDiemDgnl(file, phuongThuc);
                    } else if(loaiFile.equals("VSAT")) {
                        return controller.importDiemVsat(file, phuongThuc);
                    } else return null;
                }

                @Override
                protected void done() {
                    // Trả lại con trỏ chuột bình thường
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        String message = get();
                        JOptionPane.showMessageDialog(DiemThiPanel.this, message, "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                        loadTable(); // Tự động load lại bảng
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(DiemThiPanel.this, "Lỗi nạp file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }

    private void loadTable() {
        List<DiemThiXetTuyen> list = controller.getAll();
        renderTableData(list);
    }

    private void renderTableData(List<DiemThiXetTuyen> list) {
        model.setRowCount(0);
        if (list == null) return;
        for(DiemThiXetTuyen item : list) {
            model.addRow(new Object[]{
                    item.getIdDiemThi(),
                    item.getCccd(),
                    item.getSoBaoDanh(),
                    item.getPhuongThuc()
            });
        }
    }

    // =====================================================================
    // CÁC HÀM XỬ LÝ CHỨC NĂNG (THÊM, SỬA, XÓA, TÌM KIẾM)
    // =====================================================================

    private void functionAddData() {
        DataFormDiemThi form = new DataFormDiemThi();
        form.txtId.setText("Tự động tạo");
        form.txtId.setEditable(false);

        form.btnSave.addActionListener(e -> {
            try {
                DiemThiXetTuyen entity = getEntityFromForm(form);
                // Giả định Controller có hàm add()
                if(controller.add(entity)) {
                    JOptionPane.showMessageDialog(form, "Thêm mới điểm thí sinh thành công!");
                    loadTable();
                    form.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    private void functionEditData() {
        int row = table.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một dòng trên bảng để xem chi tiết hoặc sửa!");
            return;
        }

        Integer id = Integer.parseInt(model.getValueAt(row, 0).toString());
        DiemThiXetTuyen entity = controller.getById(id);
        if(entity == null) return;

        DataFormDiemThi form = new DataFormDiemThi();
        fillFormWithEntity(form, entity);

        form.btnSave.addActionListener(e -> {
            try {
                DiemThiXetTuyen updatedEntity = getEntityFromForm(form);
                updatedEntity.setIdDiemThi(id); // Giữ nguyên ID cũ để Update đè

                // Giả định Controller có hàm update()
                if(controller.update(updatedEntity)) {
                    JOptionPane.showMessageDialog(form, "Cập nhật thành công!");
                    loadTable();
                    form.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    private void functionDeleteData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa dữ liệu điểm này?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Integer id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            DiemThiXetTuyen entity = controller.getById(id);

            // Giả định Controller có hàm delete()
            if(entity != null && controller.delete(entity)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadTable();
            }
        }
    }

    private void functionSearchData() {
        String keyString = searchField.getText();
        if(keyString.isEmpty()) {
            loadTable();
            return;
        }
        // Giả định Controller có hàm search() tra cứu theo CCCD hoặc SBD
        List<DiemThiXetTuyen> list = controller.search(keyString);
        renderTableData(list);
    }

    // =====================================================================
    // BỘ CHUYỂN ĐỔI DỮ LIỆU (MAPPER) GIỮA FORM VÀ ENTITY
    // =====================================================================

    private DiemThiXetTuyen getEntityFromForm(DataFormDiemThi form) {
        return DiemThiXetTuyen.builder()
                .cccd(form.txtCccd.getText().trim())
                .soBaoDanh(form.txtSbd.getText().trim())
                .phuongThuc(form.txtPhuongThuc.getText().trim())
                .toan(parseBD(form.txtTO.getText()))
                .ly(parseBD(form.txtLI.getText()))
                .hoa(parseBD(form.txtHO.getText()))
                .sinh(parseBD(form.txtSI.getText()))
                .van(parseBD(form.txtVA.getText()))
                .su(parseBD(form.txtSU.getText()))
                .dia(parseBD(form.txtDI.getText()))
                .n1Thi(parseBD(form.txtN1_THI.getText()))
                .n1CC(parseBD(form.txtN1_CC.getText()))
                .cncn(parseBD(form.txtCNCN.getText()))
                .cnnn(parseBD(form.txtCNNN.getText()))
                .tinHoc(parseBD(form.txtTI.getText()))
                .ktpl(parseBD(form.txtKTPL.getText()))
                .diemNangLuc(parseBD(form.txtNL1.getText()))
                .diemNangKhieu1(parseBD(form.txtNK1.getText()))
                .diemNangKhieu2(parseBD(form.txtNK2.getText()))
                // Đã fix lỗi trỏ nhầm txtNK2 của bản gốc
                .diemNangKhieu3(form.txtNK3 != null ? parseBD(form.txtNK3.getText()) : null)
                .diemNangKhieu4(form.txtNK4 != null ? parseBD(form.txtNK4.getText()) : null)
                .build();
    }

    private void fillFormWithEntity(DataFormDiemThi form, DiemThiXetTuyen entity) {
        form.txtId.setText(String.valueOf(entity.getIdDiemThi()));
        form.txtId.setEditable(false);
        form.txtCccd.setText(entity.getCccd() != null ? entity.getCccd() : "");
        form.txtSbd.setText(entity.getSoBaoDanh() != null ? entity.getSoBaoDanh() : "");
        form.txtPhuongThuc.setText(entity.getPhuongThuc() != null ? entity.getPhuongThuc() : "");

        form.txtTO.setText(formatBD(entity.getToan()));
        form.txtLI.setText(formatBD(entity.getLy()));
        form.txtHO.setText(formatBD(entity.getHoa()));
        form.txtSI.setText(formatBD(entity.getSinh()));
        form.txtVA.setText(formatBD(entity.getVan()));
        form.txtSU.setText(formatBD(entity.getSu()));
        form.txtDI.setText(formatBD(entity.getDia()));
        form.txtN1_THI.setText(formatBD(entity.getN1Thi()));
        form.txtN1_CC.setText(formatBD(entity.getN1CC()));
        form.txtCNCN.setText(formatBD(entity.getCncn()));
        form.txtCNNN.setText(formatBD(entity.getCnnn()));
        form.txtTI.setText(formatBD(entity.getTinHoc()));
        form.txtKTPL.setText(formatBD(entity.getKtpl()));
        form.txtNL1.setText(formatBD(entity.getDiemNangLuc()));
        form.txtNK1.setText(formatBD(entity.getDiemNangKhieu1()));
        form.txtNK2.setText(formatBD(entity.getDiemNangKhieu2()));

        if (form.txtNK3 != null) form.txtNK3.setText(formatBD(entity.getDiemNangKhieu3()));
        if (form.txtNK4 != null) form.txtNK4.setText(formatBD(entity.getDiemNangKhieu4()));
    }

    private BigDecimal parseBD(String text) {
        if(text == null || text.trim().isEmpty()) return null; // Nên trả về null thay vì 0 để DB hiểu là chưa thi môn đó
        try { return new BigDecimal(text.trim()); } catch(Exception e) { return null; }
    }

    private String formatBD(BigDecimal bd) {
        // Nếu có điểm thì hiện số, nếu null thì để trống cho Form đẹp
        return bd != null ? bd.toString() : "";
    }}