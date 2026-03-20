package com.ui.panel;

import com.config.AppConfig;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TestFrame extends BasePanel {

  public TestFrame() {
    super(AppConfig.COLOR_BACKGROUND, 0);
    setLayout(new BorderLayout());
    initComponents();
  }

  private void initComponents() {
    JPanel mainContent = new JPanel(new BorderLayout(20, 20));
    mainContent.setBackground(AppConfig.COLOR_BACKGROUND);
    mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    BasePanel headerPanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setPreferredSize(new Dimension(0, 60));

    JLabel lblTitle = new JLabel("DANH SÁCH NGƯỜI DÙNG");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitle.setForeground(AppConfig.COLOR_TEXT_MAIN);
    headerPanel.add(lblTitle, BorderLayout.WEST);

    BaseButton btnAdd = new BaseButton(" + Thêm Người Dùng ");
    headerPanel.add(btnAdd, BorderLayout.EAST);

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
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    footer.setOpaque(false);

    BaseButton btnEdit = new BaseButton("Chỉnh sửa");
    BaseButton btnDelete = new BaseButton("Xóa người dùng", new Color(220, 53, 69));

    footer.add(btnEdit);
    footer.add(btnDelete);
    mainContent.add(footer, BorderLayout.SOUTH);

    add(mainContent, BorderLayout.CENTER);
  }
}