package com.ui.common;

import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {
  private int cornerRadius = 15;
  private Color backgroundColor;

  public BasePanel() {
    this(new Color(255, 255, 255), 15);
  }

  public BasePanel(Color bgColor, int radius) {
    this.backgroundColor = bgColor;
    this.cornerRadius = radius;
    setOpaque(false);
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Dimension arcs = new Dimension(cornerRadius, cornerRadius);
    int width = getWidth();
    int height = getHeight();
    Graphics2D graphics = (Graphics2D) g;
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (backgroundColor != null) {
      graphics.setColor(backgroundColor);
    } else {
      graphics.setColor(getBackground());
    }

    graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
  }
}