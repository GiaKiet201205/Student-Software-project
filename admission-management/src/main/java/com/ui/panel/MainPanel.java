package com.ui.panel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
  private CardLayout cardLayout;

  public MainPanel() {
    cardLayout = new CardLayout();
    setLayout(cardLayout);

    add(new TestFrame(), "TEST FRAME");
    add(new DiemCongXetTuyenPanel(), "EXTRA POINTS");
    add(new NguyenVongXetTuyenPanel(), "APPLICATION REFERENCES");
    // Todo: Add Screens
    add(new NganhPanel(), "NGANH_PANEL");
    add(new ToHopPanel(), "TOHOP_PANEL");
    add(new UserManagementFrame(), "USER_MANAGEMENT");
    add(new ExamineeManagementFrame(), "EXAMINEE_MANAGEMENT");
  }

  public void showCard(String cardName) {
    cardLayout.show(this, cardName);
  }
}