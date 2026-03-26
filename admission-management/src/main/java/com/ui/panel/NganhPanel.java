package com.ui.panel;

import com.config.AppConfig;
import com.entity.Nganh;
import com.service.NganhService;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NganhPanel extends BasePanel {
    private DefaultTableModel defaultTableModel;
    private BaseTable baseTable;
    private final NganhService nganhService;

    private BaseButton addButton;
    private BaseButton editButton;
    private BaseButton deleteButton;

    public NganhPanel() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        this.nganhService = new NganhService();
        setLayout(new BorderLayout());
        initializeComponents();
        loadDataIntoTable();
        setupActionListeners();
    }

    private void initializeComponents() {
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(AppConfig.COLOR_BACKGROUND);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BasePanel headerPanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        headerPanel.setLayout(new BorderLayout(20, 0));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel(" QUẢN LÝ NGÀNH TUYỂN SINH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        addButton = new BaseButton(" + Thêm Ngành ");
        headerPanel.add(addButton, BorderLayout.EAST);
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] tableColumnNames = {
            "Mã Ngành", "Tên Ngành", "Tổ Hợp Gốc", "Chỉ Tiêu", 
            "Điểm Sàn", "Điểm Trúng Tuyển", "Tuyển Thẳng", 
            "ĐGNL", "THPT", "V-SAT", "SL XTT", "SL ĐGNL", "SL V-SAT", "SL THPT"
        };
        
        defaultTableModel = new DefaultTableModel(tableColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khóa bảng
            }
        };
        baseTable = new BaseTable(defaultTableModel);
        baseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Setup độ rộng cột cho đẹp
        int[] widths = {100, 250, 100, 80, 80, 120, 100, 80, 80, 80, 80, 80, 80, 80};
        for(int i = 0; i < widths.length; i++) {
            baseTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(baseTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setOpaque(false);

        editButton = new BaseButton("Chỉnh sửa");
        deleteButton = new BaseButton("Xóa ngành", AppConfig.COLOR_DANGER);

        footerPanel.add(editButton);
        footerPanel.add(deleteButton);
        mainContentPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void loadDataIntoTable() {
        defaultTableModel.setRowCount(0);
        try {
            List<Nganh> danhSachNganh = nganhService.getAllNganh();
            for (Nganh nganh : danhSachNganh) {
                Object[] rowData = {
                    nganh.getMaNganh(), nganh.getTenNganh(), nganh.getToHopGoc(),
                    nganh.getChiTieu(), nganh.getDiemSan(), nganh.getDiemTrungTuyen(),
                    nganh.getTuyenThang(), nganh.getDgnl(), nganh.getThpt(), nganh.getVsat(),
                    nganh.getSlXtt(), nganh.getSlDgnl(), nganh.getSlVsat(), nganh.getSlThpt()
                };
                defaultTableModel.addRow(rowData);
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu Ngành: " + exception.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- CÀI ĐẶT CÁC SỰ KIỆN Click NÚT ---
    private void setupActionListeners() {
        // 1. Chức năng Thêm
        addButton.addActionListener(e -> {
            NganhDialog dialog = new NganhDialog(SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);

            Nganh newNganh = dialog.getNganhResult();
            if (newNganh != null) {
                try {
                    nganhService.themNganh(newNganh);
                    loadDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Thêm ngành thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 2. Chức năng Sửa
        editButton.addActionListener(e -> {
            int selectedRow = baseTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngành trên bảng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Nganh nganhEdit = new Nganh();
            // Ép kiểu cẩn thận từ bảng
            nganhEdit.setMaNganh(String.valueOf(defaultTableModel.getValueAt(selectedRow, 0)));
            nganhEdit.setTenNganh(String.valueOf(defaultTableModel.getValueAt(selectedRow, 1)));
            nganhEdit.setToHopGoc(String.valueOf(defaultTableModel.getValueAt(selectedRow, 2)));
nganhEdit.setChiTieu(Integer.parseInt(String.valueOf(defaultTableModel.getValueAt(selectedRow, 3))));
            nganhEdit.setDiemSan(new java.math.BigDecimal(String.valueOf(defaultTableModel.getValueAt(selectedRow, 4))));
nganhEdit.setDiemTrungTuyen(new java.math.BigDecimal(String.valueOf(defaultTableModel.getValueAt(selectedRow, 5))));
nganhEdit.setTuyenThang(String.valueOf(defaultTableModel.getValueAt(selectedRow, 6)));
            nganhEdit.setDgnl(String.valueOf(defaultTableModel.getValueAt(selectedRow, 7)));
            nganhEdit.setThpt(String.valueOf(defaultTableModel.getValueAt(selectedRow, 8)));
            nganhEdit.setVsat(String.valueOf(defaultTableModel.getValueAt(selectedRow, 9)));
            nganhEdit.setSlXtt(Integer.parseInt(String.valueOf(defaultTableModel.getValueAt(selectedRow, 10))));
            nganhEdit.setSlDgnl(Integer.parseInt(String.valueOf(defaultTableModel.getValueAt(selectedRow, 11))));
            nganhEdit.setSlVsat(Integer.parseInt(String.valueOf(defaultTableModel.getValueAt(selectedRow, 12))));
            nganhEdit.setSlThpt(String.valueOf(defaultTableModel.getValueAt(selectedRow, 13)));

            NganhDialog dialog = new NganhDialog(SwingUtilities.getWindowAncestor(this), nganhEdit);
            dialog.setVisible(true);

            Nganh updatedNganh = dialog.getNganhResult();
            if (updatedNganh != null) {
                try {
                    nganhService.capNhatNganh(updatedNganh);
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
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngành trên bảng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maNganh = String.valueOf(defaultTableModel.getValueAt(selectedRow, 0));
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa ngành " + maNganh + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    nganhService.xoaNganh(maNganh);
                    loadDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}