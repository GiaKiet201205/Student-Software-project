package com.ui.panel;

import com.controller.BangQuyDoiController;
import com.entity.BangQuyDoi;
import com.service.BangQuyDoiService.ImportSummary;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;
import com.ui.data_form.DataFormBangQuyDoi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class BangQuyDoiPanel extends BasePanel {

    private final BangQuyDoiController controller = new BangQuyDoiController();

    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;
    private JComboBox<String> cbFilterPhuongThuc;

    // Columns
    private static final String[] COLUMNS = {
            "ID", "Phương thức", "Tổ hợp", "Môn", "Điểm A", "Điểm B", "Điểm C", "Điểm D", "Mã quy đổi", "Phân vị"
    };

    private static final Color PRIMARY   = new Color(0x1565C0);
    private static final Color SUCCESS   = new Color(0x2E7D32);
    private static final Color DANGER    = new Color(0xC62828);
    private static final Color ORANGE    = new Color(0xE65100);

    public BangQuyDoiPanel() {
        super();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildToolbar(), BorderLayout.CENTER); // tạm thời
        // Restructure: header + toolbar + table
        removeAll();
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.add(buildHeader(), BorderLayout.NORTH);
        top.add(buildToolbar(), BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
    }

    // ===================== HEADER =====================

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("📋  Quản lý Bảng Quy Đổi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("ĐGNL · VSAT · THPT");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(0xBBDEFB));
        panel.add(sub, BorderLayout.EAST);

        return panel;
    }

    // ===================== TOOLBAR =====================

    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(new Color(0xF5F5F5));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xDDDDDD)));

        // Search field
        tfSearch = new JTextField(18);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tfSearch.setToolTipText("Tìm theo phương thức, tổ hợp, môn, mã quy đổi...");
        tfSearch.addActionListener(e -> onSearch());

        BaseButton btnSearch = makeBtn("🔍 Tìm", PRIMARY);
        btnSearch.addActionListener(e -> onSearch());

        // Filter combobox
        cbFilterPhuongThuc = new JComboBox<>();
        cbFilterPhuongThuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFilterPhuongThuc.addItem("-- Tất cả --");
        cbFilterPhuongThuc.addItem("DGNL");
        cbFilterPhuongThuc.addItem("VSAT");
        cbFilterPhuongThuc.addItem("THPT");
        cbFilterPhuongThuc.addActionListener(e -> onFilterChange());

        BaseButton btnAdd    = makeBtn("➕ Thêm",  SUCCESS);
        BaseButton btnEdit   = makeBtn("✏️ Sửa",   PRIMARY);
        BaseButton btnDelete = makeBtn("🗑️ Xóa",   DANGER);
        BaseButton btnImport = makeBtn("📥 Import Excel", ORANGE);
        BaseButton btnRefresh = makeBtn("🔄 Tải lại", new Color(0x546E7A));

        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnImport.addActionListener(e -> onImportExcel());
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(new JLabel("Tìm kiếm:"));
        toolbar.add(tfSearch);
        toolbar.add(btnSearch);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(new JLabel("Lọc:"));
        toolbar.add(cbFilterPhuongThuc);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnImport);
        toolbar.add(btnRefresh);

        return toolbar;
    }

    // ===================== TABLE =====================

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0xE3F2FD));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(0xEEEEEE));
        table.setShowGrid(true);

        // Column widths
        int[] widths = {50, 110, 80, 70, 90, 90, 90, 90, 160, 70};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        // Hide ID column (still accessible by model)
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Double-click to edit
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) onEdit();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    // ===================== DATA LOAD =====================

    private void loadData() {
        List<BangQuyDoi> list = controller.getAll();
        populateTable(list);
    }

    private void populateTable(List<BangQuyDoi> list) {
        tableModel.setRowCount(0);
        for (BangQuyDoi b : list) {
            tableModel.addRow(new Object[]{
                    b.getIdqd(),
                    b.getPhuongthuc(),
                    b.getTohop(),
                    b.getMon(),
                    b.getDiemA(),
                    b.getDiemB(),
                    b.getDiemC(),
                    b.getDiemD(),
                    b.getMaQuydoi(),
                    b.getPhanVi()
            });
        }
    }

    // ===================== ACTIONS =====================

    private void onSearch() {
        String kw = tfSearch.getText().trim();
        List<BangQuyDoi> result = controller.search(kw);
        populateTable(result);
    }

    private void onFilterChange() {
        String selected = (String) cbFilterPhuongThuc.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            loadData();
        } else {
            populateTable(controller.getByPhuongThuc(selected));
        }
    }

    private void onAdd() {
        DataFormBangQuyDoi form = new DataFormBangQuyDoi(getParentFrame(), null, controller);
        form.setVisible(true);
        if (form.isSaved()) {
            loadData();
            JOptionPane.showMessageDialog(this, "✅ Thêm bản ghi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onEdit() {
        BangQuyDoi selected = getSelectedRecord();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để sửa.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DataFormBangQuyDoi form = new DataFormBangQuyDoi(getParentFrame(), selected, controller);
        form.setVisible(true);
        if (form.isSaved()) {
            loadData();
            JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onDelete() {
        BangQuyDoi selected = getSelectedRecord();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để xóa.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa bản ghi:\n" + selected.getMaQuydoi() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        String error = controller.delete(selected.getIdqd());
        if (error != null) {
            JOptionPane.showMessageDialog(this, "❌ " + error, "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            loadData();
            JOptionPane.showMessageDialog(this, "✅ Đã xóa bản ghi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onImportExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file Excel để import Bảng Quy Đổi");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        // Show progress indicator
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            ImportSummary summary = controller.importExcel(file);
            loadData();

            StringBuilder msg = new StringBuilder();
            msg.append(summary.toString()).append("\n");
            if (!summary.errorDetails.isEmpty()) {
                msg.append("\nChi tiết lỗi (tối đa 10 dòng đầu):\n");
                summary.errorDetails.stream().limit(10).forEach(e -> msg.append("  • ").append(e).append("\n"));
            }
            JOptionPane.showMessageDialog(this, msg.toString(), "Kết quả Import", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi đọc file:\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // ===================== HELPERS =====================

    private BangQuyDoi getSelectedRecord() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) tableModel.getValueAt(row, 0);
        return controller.getById(id);
    }

    private BaseButton makeBtn(String text, Color bg) {
        BaseButton btn = new BaseButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }

    private Frame getParentFrame() {
        Container c = getParent();
        while (c != null && !(c instanceof Frame)) c = c.getParent();
        return (Frame) c;
    }
}