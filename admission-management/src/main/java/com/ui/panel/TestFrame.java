package com.ui.panel;

import com.config.AppConfig;
import com.entity.NguoiDung;
import com.service.GenericImportService;
import com.service.mapper.NguoiDungRowMapper;
import com.service.mapper.RowMapper;
import com.ui.common.BaseButton;
import com.ui.common.BasePanel;
import com.ui.common.BaseTable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

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

    // --- BỔ SUNG: Panel chứa các nút Header ---
    JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    headerActions.setOpaque(false);

    BaseButton btnImport = new BaseButton(" Nhập Excel ");
    BaseButton btnAdd = new BaseButton(" + Thêm Người Dùng ");

    headerActions.add(btnImport);
    headerActions.add(btnAdd);
    headerPanel.add(headerActions, BorderLayout.EAST);

    mainContent.add(headerPanel, BorderLayout.NORTH);

    BasePanel tablePanel = new BasePanel(AppConfig.COLOR_WHITE, 15);
    tablePanel.setLayout(new BorderLayout());

    String[] cols = {"ID", "Tài khoản", "Họ tên", "Quyền", "Trạng thái"};
    DefaultTableModel model = new DefaultTableModel(cols, 0);

    // Test dữ liệu
    model.addRow(new Object[]{"1", "admin", "Nguyễn Văn Quản Trị", "ADMIN", "Hoạt động"});

    BaseTable table = new BaseTable(model);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getViewport().setBackground(AppConfig.COLOR_WHITE);

    tablePanel.add(scrollPane, BorderLayout.CENTER);
    mainContent.add(tablePanel, BorderLayout.CENTER);

    // --- Footer ---
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    footer.setOpaque(false);

    BaseButton btnEdit = new BaseButton("Chỉnh sửa");
    BaseButton btnDelete = new BaseButton("Xóa người dùng", new Color(220, 53, 69));

    footer.add(btnEdit);
    footer.add(btnDelete);
    mainContent.add(footer, BorderLayout.SOUTH);

    add(mainContent, BorderLayout.CENTER);

    btnImport.addActionListener((ActionEvent e) -> handleImportExcel());
  }

  private void handleImportExcel() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Chọn file Excel");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showOpenDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToImport = fileChooser.getSelectedFile();
      executeImportInBackground(fileToImport);
    }
  }

  private void executeImportInBackground(File file) {
    System.out.println("Bắt đầu xử lý import bằng SAX & StatelessSession...");

    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
      @Override
      protected Boolean doInBackground() throws Exception {

        RowMapper<NguoiDung> mapper = new NguoiDungRowMapper();
        GenericImportService importService = new GenericImportService();

        return importService.importFromExcel(file, mapper);
      }

      @Override
      protected void done() {
        try {
          boolean success = get();
          if (success) {
            JOptionPane.showMessageDialog(TestFrame.this,
                    "Import dữ liệu thành công với tốc độ cao!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(TestFrame.this,
                    "Có lỗi xảy ra trong quá trình Import. Vui lòng xem Log console.", "Thất bại", JOptionPane.ERROR_MESSAGE);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    worker.execute();
  }
}