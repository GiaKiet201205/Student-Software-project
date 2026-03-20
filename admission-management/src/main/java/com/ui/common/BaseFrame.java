package com.ui.common;

import com.config.AppConfig;
import com.config.HibernateUtil;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BaseFrame extends JFrame {

  public BaseFrame() {
    setTitle(AppConfig.APP_NAME + " - " + AppConfig.VERSION);
    setSize(AppConfig.MAIN_WINDOW_WIDTH, AppConfig.MAIN_WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        try {
          System.out.println("Đang đóng kết nối Database...");
          HibernateUtil.shutdown();
        } catch (Exception ex) {
          System.err.println("Không thể đóng SessionFactory vì nó chưa từng được khởi tạo.");
          System.exit(0);
        }
      }
    });
  }

  public void switchPanel(JPanel newPanel) {
    getContentPane().removeAll();
    getContentPane().add(newPanel);
    revalidate();
    repaint();
  }
}
