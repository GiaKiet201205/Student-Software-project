package com.ui.panel;

import com.config.AppConfig;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManagementFrame extends BasePanel {

    private BaseButton btnAdd, btnDelete, btnEdit, btnImport, btnSearch, btnPrev, btnNext;
    private JTextField txtSearch;

    public UserManagementFrame() {
        super(AppConfig.COLOR_BACKGROUND, 0);
        this.setLayout(new BorderLayout());
        initComponents();
        initEvents();
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
        actionPanel.add(btnAdd);

        btnImport = new BaseButton(" + Nhập ");
        actionPanel.add(btnImport);



        JLabel lblTitle = new JLabel("DANH SÁCH NGƯỜI DÙNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(AppConfig.COLOR_TEXT_MAIN);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        mainContent.add(headerPanel, BorderLayout.NORTH);

        BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
        tablePanel.setLayout(new BorderLayout());

        String[] cols = {"ID", "Tài khoản", "Họ tên", "Quyền", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        // Test dữ liệu
        model.addRow(new Object[]{"1", "admin", "Nguyễn Văn Quản Trị", "ADMIN", "Hoạt động"});
        model.addRow(new Object[]{"2", "staff_01", "Trần Thị Tuyển Sinh", "STAFF", "Hoạt động"});

        BaseTable table = new BaseTable(model);
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
        btnNext = new BaseButton("›");
        btnEdit = new BaseButton("Chỉnh sửa");
        btnDelete = new BaseButton("Xóa người dùng", new Color(220, 53, 69));

        footer.add(paginationPanel, BorderLayout.CENTER);
        footer.add(actionFooterPanel, BorderLayout.EAST);
        paginationPanel.add(btnPrev);
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

    // Todo: Thêm sự kiện cho CRUD buttons
    private void addUser(){
        JOptionPane.showMessageDialog(this, "Chức năng thêm người dùng sẽ được triển khai sau.");
    }

    private void importUserFromExcel(){
        JOptionPane.showMessageDialog(this, "Chức năng nhập từ Excel sẽ được triển khai sau.");
    }

    private void deleteUser(){
        JOptionPane.showMessageDialog(this, "Chức năng xóa người dùng sẽ được triển khai sau.");
    }

    private void editUser(){
        JOptionPane.showMessageDialog(this, "Chức năng chỉnh sửa người dùng sẽ được triển khai sau.");
    }

    private void searchUser(){
        String keyword = txtSearch.getText().trim();
        JOptionPane.showMessageDialog(this, "Chức năng tìm kiếm người dùng với từ khóa: " + keyword + " sẽ được triển khai sau.");
    }

    private void prevPage(){
        JOptionPane.showMessageDialog(this, "Chức năng trang trước sẽ được triển khai sau.");
    }

    private void nextPage(){
        JOptionPane.showMessageDialog(this, "Chức năng trang sau sẽ được triển khai sau.");
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
