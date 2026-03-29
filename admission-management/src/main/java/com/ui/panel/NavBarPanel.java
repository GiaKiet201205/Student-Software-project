package com.ui.panel;

import com.ui.common.BaseButton;
import com.ui.common.BasePanel;

import java.awt.*;
import javax.swing.*;

public class NavBarPanel extends BasePanel {

  private MainPanel mainPanel;
  private BaseButton lastSelected;

  public NavBarPanel(MainPanel mainPanel) {
    super(new Color(44, 62, 80), 0);
    this.mainPanel = mainPanel;

    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
    setPreferredSize(new Dimension(220, 0));

    addLogo();
    addNavButton("Test Frame", "TEST FRAME");
    addNavButton("Quản lý Ngành", "NGANH_PANEL");
    addNavButton("Quản lý Tổ hợp", "TOHOP_PANEL");

    addNavButton("ĐIỂM CỘNG XÉT TUYỂN", "EXTRA POINTS");
    addNavButton("NGUYỆN VỌNG XÉT TUYỂN", "APPLICATION REFERENCES");
    // TODO: Add screens
    addNavButton("Quản lý người dùng", "USER_MANAGEMENT");
    addNavButton("Quản lý thí sinh", "EXAMINEE_MANAGEMENT");
    addLogoutButton();
  }

  private void addLogo() {
    JLabel lblLogo = new JLabel("ADMISSION APP");
    lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblLogo.setForeground(Color.WHITE);
    lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    add(lblLogo);

    JSeparator sep = new JSeparator();
    sep.setPreferredSize(new Dimension(180, 1));
    add(sep);
  }

  private void addNavButton(String text, String cardName) {
    BaseButton btn = new BaseButton(text);
    btn.setPreferredSize(new Dimension(200, 45));

    btn.addActionListener(e -> {
      if (lastSelected != null) {
        lastSelected.setBackground(new Color(0, 102, 204));
      }
      btn.setBackground(new Color(52, 152, 219));
      lastSelected = btn;

      mainPanel.showCard(cardName);
    });

    add(btn);
  }

  private void addLogoutButton() {
    BaseButton btnLogout = new BaseButton("Đăng xuất", new Color(192, 57, 43));
    btnLogout.setPreferredSize(new Dimension(200, 45));
    btnLogout.addActionListener(e -> {
      int check = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
      if (check == JOptionPane.YES_OPTION) {
        System.exit(0);
      }
    });
    add(btnLogout);
  }
}
