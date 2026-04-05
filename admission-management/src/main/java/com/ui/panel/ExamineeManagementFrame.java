package com.ui.panel;

import com.config.AppConfig;
import com.controller.ThiSinhXetTuyen25Controller;
import com.entity.ThiSinhXetTuyen25;
import com.service.ThiSinhXetTuyen25Service;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.*;
import java.awt.*;
import java.util.List;

public class ExamineeManagementFrame extends BasePanel {

    private final ThiSinhXetTuyen25Controller controller = new ThiSinhXetTuyen25Controller();
    private BaseButton btnAdd, btnDelete, btnEdit, btnImport, btnSearch, btnPrev, btnNext;
    private JTextField txtSearch;
    private BaseTable table;
    private DefaultTableModel model;
    private JLabel lblPaginationInfo;

    private int currentPage = 1;
    private int pageSize = 3;
    private int totalPages;
    private String keywork = "";

    public ExamineeManagementFrame() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        this.setLayout(new BorderLayout());
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(AppConfig.COLOR_BACKGROUND);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BasePanel headerPanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        headerPanel.setLayout(new BorderLayout(10, 0));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(AppConfig.COLOR_WHITE);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setPreferredSize(new Dimension(200, 25));
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setForeground(Color.BLACK);
        txtSearch.setCaretColor(Color.BLACK);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 1, 1, 10)
        ));

        btnSearch = new BaseButton("Tìm kiếm");
        actionPanel.add(txtSearch);
        actionPanel.add(btnSearch);

        btnAdd = new BaseButton(" + Thêm ");
//        actionPanel.add(btnAdd);

        btnImport = new BaseButton(" + Nhập ");
        actionPanel.add(btnImport);



        JLabel lblTitle = new JLabel("DANH SÁCH THÍ SINH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        mainContent.add(headerPanel, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] cols = {"ID", "CCCD", "Số báo danh", "Mật khẩu", "Họ tên", "Ngày sinh", "Điện thoại", "Email", "Giới tính", "Nơi sinh", "Đối tượng", "Khu vực"};
        model = new DefaultTableModel(cols, 0);

        table = new BaseTable(model);

        JTableHeader headerTable = table.getTableHeader();
        headerTable.setBackground(AppConfig.COLOR_PRIMARY);
        headerTable.setForeground(Color.WHITE);
        headerTable.setFont(new Font("Segoe UI",Font.BOLD,14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(tablePanel, BorderLayout.CENTER);

        // --- 3. Footer / Action Panel ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setOpaque(false);

        JPanel actionFooterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionFooterPanel.setOpaque(false);

        btnPrev = new BaseButton("‹");
        lblPaginationInfo = new JLabel("Trang 1/1");
        btnNext = new BaseButton("›");
        btnEdit = new BaseButton("Chỉnh sửa");
        btnDelete = new BaseButton("Xóa người dùng", new Color(220, 53, 69));

        footer.add(paginationPanel, BorderLayout.CENTER);
        footer.add(actionFooterPanel, BorderLayout.EAST);
        paginationPanel.add(btnPrev);
        paginationPanel.add(lblPaginationInfo);
        paginationPanel.add(btnNext);
        actionFooterPanel.add(btnEdit);
//        actionFooterPanel.add(btnDelete);
        mainContent.add(footer, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> {
            addUser();
        });

        btnImport.addActionListener(e -> {
            importUserFromExcel();
        });

        btnDelete.addActionListener(e -> {
            deleteUser();
        });

        btnEdit.addActionListener(e -> {
            editUser();
        });

        btnSearch.addActionListener(e -> {
            searchUser();
        });

        btnPrev.addActionListener(e -> {
            prevPage();
        });

        btnNext.addActionListener(e -> {
            nextPage();
        });
    }

    // Todo: Thêm sự kiện cho CRUD buttons
    private void addUser(){
        JOptionPane.showMessageDialog(this, "Chức năng thêm người dùng sẽ được triển khai sau.");
    }

    private void importUserFromExcel(){
        // Mở JFileChooser để chọn file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel");
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Chỉ cho phép chọn file .xlsx
        javax.swing.filechooser.FileNameExtensionFilter excelFilter =
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Excel files (*.xlsx)", "xlsx");
        fileChooser.addChoosableFileFilter(excelFilter);

        // Hiển thị dialog
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();

            // Thực hiện import
            try {
                int importedCount = controller.importFromExcel(selectedFile);
                JOptionPane.showMessageDialog(this,
                        "Import thành công! Đã thêm " + importedCount + " thí sinh.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reload lại dữ liệu hiển thị
                currentPage = 1;
                keywork = "";
                txtSearch.setText("");
                loadData();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi import: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteUser(){
        JOptionPane.showMessageDialog(this, "Chức năng xóa người dùng sẽ được triển khai sau.");
    }

    private void editUser(){
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Chọn 1 dòng trước");
            return;
        }

        Integer id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

        EditExamineeDialog dialog = new EditExamineeDialog(id);
        dialog.setVisible(true);

        loadData();
    }

    private void searchUser(){
        keywork = txtSearch.getText().trim();
        currentPage = 1;
        loadData();

    }

    private void prevPage(){
        if (currentPage > 1) {
            currentPage--;
            loadData();
        }
    }

    private void nextPage(){
        if (currentPage < totalPages) {
            currentPage++;
            loadData();
        }
    }

    private void loadData(){
        List<ThiSinhXetTuyen25> list;

        if(keywork.isEmpty()){
            list = controller.getAllByPage(currentPage, pageSize);
            totalPages = (int) Math.ceil((double) controller.countAll() / pageSize);
        } else {
            list = controller.searchByCccdOrNameByPage(keywork, currentPage, pageSize);
            totalPages = (int) Math.ceil((double) controller.countSearch(keywork) / pageSize);
        }

        renderTable(list);

        lblPaginationInfo.setText("Trang " + currentPage + " / " + totalPages);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    private void renderTable(List<ThiSinhXetTuyen25> list){
        model.setRowCount(0);
        for (ThiSinhXetTuyen25 ts : list) {
            model.addRow(new Object[]{
                    ts.getIdThiSinh(),
                    ts.getCccd(),
                    ts.getSoBaoDanh(),
                    ts.getPassword(),
                    ts.getHo() + " " + ts.getTen(),
                    ts.getNgaySinh(),
                    ts.getDienThoai(),
                    ts.getEmail(),
                    ts.getGioiTinh(),
                    ts.getNoiSinh(),
                    ts.getDoiTuong(),
                    ts.getKhuVuc()
            });
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("User Management");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.add(new ExamineeManagementFrame());

        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);
    }
}
