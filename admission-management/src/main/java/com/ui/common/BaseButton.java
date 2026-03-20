package com.ui.common;

import com.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BaseButton extends JButton {

  private Color hoverColor;
  private Color normalColor;

  public BaseButton(String text) {
    super(text);
    this.normalColor = AppConfig.COLOR_PRIMARY;
    this.hoverColor = normalColor.brighter();

    init();
  }

  // Constructor phụ để đổi màu nhanh (ví dụ màu đỏ cho nút Xóa)
  public BaseButton(String text, Color baseColor) {
    super(text);
    this.normalColor = baseColor;
    this.hoverColor = baseColor.brighter();

    init();
  }

  private void init() {
    setFont(new Font("Segoe UI", Font.BOLD, 14));
    setForeground(AppConfig.COLOR_WHITE);
    setBackground(normalColor);
    setCursor(new Cursor(Cursor.HAND_CURSOR));

    setFocusPainted(false);
    setBorderPainted(false);
    setContentAreaFilled(false);
    setOpaque(true);

    // QUAN TRỌNG: Ngăn Swing đổi màu sang màu mặc định khi nhấn
    setRolloverEnabled(true);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        setBackground(hoverColor);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        setBackground(normalColor);
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // Giữ màu đậm hơn một chút khi đang nhấn giữ
        setBackground(normalColor.darker());
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // Trả về màu hover sau khi thả chuột (nếu vẫn đang ở trong nút)
        if (contains(e.getPoint())) {
          setBackground(hoverColor);
        } else {
          setBackground(normalColor);
        }
      }
    });
  }

  // Ghi đè để ép kiểu màu nền luôn đi theo biến normalColor nếu cần
  @Override
  public void setBackground(Color bg) {
    super.setBackground(bg);
  }
}