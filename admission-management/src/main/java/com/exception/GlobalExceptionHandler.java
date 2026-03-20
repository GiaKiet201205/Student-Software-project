package com.exception;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    System.err.println("[Global Error] Lỗi xảy ra trên thread: " + t.getName());
    e.printStackTrace();

    String userMessage = "Đã xảy ra lỗi hệ thống không xác định!";
    String title = "Lỗi Hệ Thống";
    int messageType = JOptionPane.ERROR_MESSAGE;

    if (e instanceof AppException) {
      userMessage = e.getMessage();
      title = "Thông báo lỗi";
      messageType = JOptionPane.WARNING_MESSAGE;
    }

    else if (e instanceof org.hibernate.exception.JDBCConnectionException) {
      userMessage = "Không thể kết nối đến Cơ sở dữ liệu. Vui lòng kiểm tra lại mạng hoặc MySQL!";
    }

    final String finalMsg = userMessage;
    final String finalTitle = title;
    final int finalType = messageType;

    SwingUtilities.invokeLater(() -> {
      JOptionPane.showMessageDialog(null, finalMsg, finalTitle, finalType);
    });
  }
}