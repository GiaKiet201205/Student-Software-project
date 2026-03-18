package com.ui.frame;

import com.controller.AppController;
import com.entity.BangQuyDoi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TestFrame extends JFrame {

  private JTable table;
  private AppController controller = new AppController();

  public TestFrame() {

    setTitle("Test Database Flow");
    setSize(600,400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    table = new JTable();
    JButton btnLoad = new JButton("Load Data");

    btnLoad.addActionListener(e -> loadData());

    setLayout(new BorderLayout());
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(btnLoad, BorderLayout.SOUTH);
  }

  private void loadData() {

    List<BangQuyDoi> list = controller.getBangQuyDoi();

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("ID");
    model.addColumn("Phuong thuc");
    model.addColumn("To hop");
    model.addColumn("Mon");

    for (BangQuyDoi b : list) {

      model.addRow(new Object[]{
          b.getIdqd(),
          b.getD_phuongthuc(),
          b.getD_tohop(),
          b.getD_mon()
      });

    }

    table.setModel(model);
  }
}