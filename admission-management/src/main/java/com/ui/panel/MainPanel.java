package com.ui.panel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
  private CardLayout cardLayout;

  public MainPanel() {
    cardLayout = new CardLayout();
    setLayout(cardLayout);

    add(new TestFrame(), "TEST FRAME");
    // Todo: Add Screens
    add(new NganhPanel(), "NGANH_PANEL");
    add(new ToHopPanel(), "TOHOP_PANEL");
  }

  public void showCard(String cardName) {
    cardLayout.show(this, cardName);
  }
}