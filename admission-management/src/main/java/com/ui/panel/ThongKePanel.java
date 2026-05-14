package com.ui.panel;

import com.dto.ChartData;
import com.ui.common.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;

public class ThongKePanel extends BasePanel {

  private static final Color[] BASE_COLORS = {
          new Color(55, 138, 221), new Color(29, 158, 117), new Color(127, 119, 221),
          new Color(216, 90, 48), new Color(212, 83, 126), new Color(186, 117, 23),
          new Color(136, 135, 128), new Color(46, 204, 113)
  };

  private static final int PIE_X = 40;
  private static final int PIE_Y = 30;
  private static final int PIE_SIZE = 300;
  private static final int PIE_R = PIE_SIZE / 2;
  private static final int HOLE_R = PIE_R / 3;
  private static final int HOVER_OFFSET = 12;
  private static final int HOVER_EXTRA = 6;
  private static final double GAP_DEG = 0.8;

  private String[] labels = new String[0];
  private double[] values = new double[0];
  private Color[] colors = new Color[0];
  private double total = 0;
  private int hoveredSlice = -1;

  private static class SliceInfo {
    double startDeg, sweepDeg;
    SliceInfo(double s, double sw) { startDeg = s; sweepDeg = sw; }
  }

  public ThongKePanel() {
    setPreferredSize(new Dimension(740, 430));
    setBackground(Color.WHITE);
    MouseAdapter ma = new MouseAdapter() {
      @Override public void mouseMoved(MouseEvent e) { updateHover(e.getX(), e.getY()); }
      @Override public void mouseExited(MouseEvent e) { setHovered(-1); }
    };
    addMouseMotionListener(ma);
    addMouseListener(ma);
  }

  public void setData(ChartData data) {
    this.labels = data.getLabels();
    this.values = data.getValues();
    this.total = sumOf(this.values);
    this.colors = generateColors(this.labels.length);
    this.hoveredSlice = -1;
    repaint();
  }

  private Color[] generateColors(int count) {
    Color[] generated = new Color[count];
    for (int i = 0; i < count; i++) {
      generated[i] = BASE_COLORS[i % BASE_COLORS.length];
    }
    return generated;
  }

  private SliceInfo[] computeSlices(double[] vals, double total) {
    SliceInfo[] slices = new SliceInfo[vals.length];
    double cur = 90.0;
    for (int i = 0; i < vals.length; i++) {
      double fullSweep = (total == 0) ? 0 : (vals[i] / total) * 360.0;
      double sweep = Math.max(0, fullSweep - 2 * GAP_DEG);
      slices[i] = new SliceInfo(cur + GAP_DEG, sweep);
      cur += fullSweep;
    }
    return slices;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (values == null || values.length == 0) return;

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    SliceInfo[] slices = computeSlices(values, total);

    double cx = PIE_X + PIE_R;
    double cy = PIE_Y + PIE_R;

    for (int i = 0; i < values.length; i++) {
      SliceInfo s = slices[i];
      boolean hov = (i == hoveredSlice);

      double midRad = Math.toRadians(s.startDeg + s.sweepDeg / 2.0);
      double ox = 0, oy = 0;
      int extra = 0;
      if (hov) {
        ox = Math.cos(midRad) * HOVER_OFFSET;
        oy = -Math.sin(midRad) * HOVER_OFFSET;
        extra = HOVER_EXTRA;
      }

      double ax = PIE_X + ox - extra;
      double ay = PIE_Y + oy - extra;
      int sz = PIE_SIZE + extra * 2;

      Arc2D arc = new Arc2D.Double(ax, ay, sz, sz, s.startDeg, s.sweepDeg, Arc2D.PIE);
      g2.setColor(colors[i]);
      g2.fill(arc);

      if (hov) {
        g2.setColor(colors[i].darker());
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      } else {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
      }
      g2.draw(arc);
    }

    g2.setColor(Color.WHITE);
    g2.fillOval((int) (cx - HOLE_R), (int) (cy - HOLE_R), HOLE_R * 2, HOLE_R * 2);

    if (hoveredSlice >= 0) {
      double pct = (total == 0) ? 0 : (values[hoveredSlice] / total) * 100;
      drawCenter(g2, String.format("%.1f%%", pct), (int) cx, (int) cy - 8, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 50));
      drawCenter(g2, formatNum((long) values[hoveredSlice]), (int) cx, (int) cy + 12, new Font("Arial", Font.PLAIN, 12), new Color(100, 100, 100));
    } else {
      drawCenter(g2, "Tổng", (int) cx, (int) cy - 6, new Font("Arial", Font.PLAIN, 11), new Color(130, 130, 130));
      drawCenter(g2, formatNum((long) total), (int) cx, (int) cy + 12, new Font("Arial", Font.BOLD, 13), new Color(50, 50, 50));
    }

    int lx = PIE_X + PIE_SIZE + 40;
    int ly = PIE_Y;
    int rowH = 46;
    for (int i = 0; i < values.length; i++) {
      boolean hov = (i == hoveredSlice);
      double pct = (total == 0) ? 0 : (values[i] / total) * 100;

      if (hov) {
        g2.setColor(new Color(245, 245, 245));
        g2.fillRoundRect(lx - 6, ly + i * rowH - 4, 290, rowH - 4, 8, 8);
        g2.setColor(new Color(210, 210, 210));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(lx - 6, ly + i * rowH - 4, 290, rowH - 4, 8, 8);
      }

      int dotSize = hov ? 14 : 11;
      int dotY = ly + i * rowH + 4;
      g2.setColor(colors[i]);
      g2.fillOval(lx, dotY, dotSize, dotSize);

      g2.setColor(hov ? new Color(30, 30, 30) : new Color(80, 80, 80));
      g2.setFont(new Font("Arial", hov ? Font.BOLD : Font.PLAIN, 13));
      g2.drawString(labels[i], lx + 22, dotY + 11);

      String numStr = formatNum((long) values[i]);
      g2.setFont(new Font("Arial", Font.BOLD, 12));
      g2.setColor(new Color(30, 30, 30));
      g2.drawString(numStr, lx + 22, dotY + 26);

      g2.setFont(new Font("Arial", Font.PLAIN, 11));
      g2.setColor(new Color(140, 140, 140));
      int numW = g2.getFontMetrics(new Font("Arial", Font.BOLD, 12)).stringWidth(numStr);
      g2.drawString(String.format("  %.1f%%", pct), lx + 22 + numW, dotY + 26);
    }

    g2.dispose();
  }

  private void updateHover(int mx, int my) {
    if (values == null || values.length == 0) return;

    SliceInfo[] slices = computeSlices(values, total);
    double cx = PIE_X + PIE_R;
    double cy = PIE_Y + PIE_R;
    double dist = Math.sqrt(Math.pow(mx - cx, 2) + Math.pow(my - cy, 2));

    if (dist > PIE_R + HOVER_OFFSET || dist < HOLE_R) {
      setHovered(-1);
      return;
    }

    for (int i = 0; i < slices.length; i++) {
      Arc2D arc = new Arc2D.Double(PIE_X, PIE_Y, PIE_SIZE, PIE_SIZE, slices[i].startDeg, slices[i].sweepDeg, Arc2D.PIE);
      if (arc.contains(mx, my)) {
        setHovered(i);
        return;
      }
    }
    setHovered(-1);
  }

  private void setHovered(int idx) {
    if (hoveredSlice != idx) {
      hoveredSlice = idx;
      repaint();
    }
  }

  private static double sumOf(double[] a) {
    double s = 0; for (double v : a) s += v; return s;
  }

  private static String formatNum(long n) {
    return String.format("%,d", n).replace(',', '.');
  }

  private static void drawCenter(Graphics2D g2, String s, int x, int y, Font f, Color c) {
    g2.setFont(f); g2.setColor(c);
    g2.drawString(s, x - g2.getFontMetrics().stringWidth(s) / 2, y);
  }
}