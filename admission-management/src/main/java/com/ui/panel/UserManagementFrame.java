package com.ui.panel;

import com.config.AppConfig;
import com.controller.NguoiDungController;
import com.entity.NguoiDung;
import com.entity.ThiSinhXetTuyen25;
import com.service.NguoiDungService;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementFrame extends BasePanel {

    private final NguoiDungController controller = new NguoiDungController();
    private BaseButton btnAdd, btnDelete, btnEdit, btnImport, btnSearch, btnPrev, btnNext;
    private JTextField txtSearch;
    private JLabel lblPaginationInfo;
    private BaseTable table;
    private DefaultTableModel model;

    private int currentPage = 1;
    private int pageSize = 4;
    private int totalPages;

    public UserManagementFrame() {
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
//        actionPanel.add(txtSearch);
//        actionPanel.add(btnSearch);

        btnAdd = new BaseButton(" + Thêm ");
//        actionPanel.add(btnAdd);

        btnImport = new BaseButton(" + Nhập ");
//        actionPanel.add(btnImport);



        JLabel lblTitle = new JLabel("DANH SÁCH NGƯỜI DÙNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        mainContent.add(headerPanel, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] cols = {"ID", "Tài khoản", "Mật khẩu", "Quyền", "Trạng thái"};
        model = new DefaultTableModel(cols, 0);
        table = new BaseTable(model);
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
        lblPaginationInfo = new JLabel("Trang 0/0");
        btnNext = new BaseButton("›");
        btnEdit = new BaseButton("Chỉnh sửa");
        btnDelete = new BaseButton("Enable/Disable", new Color(220, 53, 69));

        footer.add(paginationPanel, BorderLayout.CENTER);
        footer.add(actionFooterPanel, BorderLayout.EAST);
        paginationPanel.add(btnPrev);
        paginationPanel.add(lblPaginationInfo);
        paginationPanel.add(btnNext);
        actionFooterPanel.add(btnEdit);
        actionFooterPanel.add(btnDelete);
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

    private void loadData(){
        List<NguoiDung> list = controller.getAllByPage(currentPage, pageSize);
        totalPages = (int) Math.ceil((double) controller.countAll() / pageSize);

        renderTable(list);

        lblPaginationInfo.setText("Trang " + currentPage + " / " + totalPages);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    private void renderTable(List<NguoiDung> list){
        model.setRowCount(0);
        for (NguoiDung nd : list) {
            model.addRow(new Object[]{
                    nd.getIdNguoiDung(),
                    nd.getUsername(),
                    nd.getPassword(),
                    checkRole(nd.getRole()),
                    checkActive(nd.getActive())
            });
        }
    }

    private String checkRole(Integer role) {
        if (role == null) return "N/A";
        switch (role) {
            case 0: return "ADMIN";
            case 1: return "USER";
            default: return "UNKNOWN";
        }
    }

    private String checkActive(Integer status) {
        if (status == null) return "N/A";
        switch (status) {
            case 0: return "Đã vô hiệu hóa";
            case 1: return "Hoạt động";
            default: return "UNKNOWN";
        }
    }

    // Todo: Thêm sự kiện cho CRUD buttons
    private void addUser(){
        JOptionPane.showMessageDialog(this, "Chức năng thêm người dùng sẽ được triển khai sau.");
    }

    private void importUserFromExcel(){
        JOptionPane.showMessageDialog(this, "Chức năng nhập từ Excel sẽ được triển khai sau.");
    }

    private void deleteUser(){
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Chọn 1 dòng trước");
            return;
        }

        Integer id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn Enable/Disable người dùng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.toggleActive(id);
            loadData();
        }
    }

    private void editUser(){
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Chọn 1 dòng trước");
            return;
        }

        Integer id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

        EditUserDialog dialog = new EditUserDialog(id);
        dialog.setVisible(true);

        loadData();
    }

    private void searchUser(){
        String keyword = txtSearch.getText().trim();
        JOptionPane.showMessageDialog(this, "Chức năng tìm kiếm người dùng với từ khóa: " + keyword + " sẽ được triển khai sau.");
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("User Management");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.add(new UserManagementFrame());

        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);
    }
}
