package com.ui.common;

import com.config.AppConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BaseTable extends JTable {

  public BaseTable(DefaultTableModel model) {
    super(model);

    getTableHeader().setReorderingAllowed(false);
    getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    getTableHeader().setBackground(AppConfig.COLOR_BACKGROUND);
    getTableHeader().setForeground(Color.WHITE);

    setRowHeight(30);
    setShowGrid(false);
    setIntercellSpacing(new Dimension(0, 0));

    setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
          c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
        }
        setHorizontalAlignment(JLabel.CENTER);
        return c;
      }
    });
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }
}
