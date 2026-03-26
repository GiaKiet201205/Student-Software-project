package com.ui.panel;

import javax.swing.*;

import com.ui.panel.QuyDoi.BangQuyDoiPanel;

import java.awt.*;

public class MainPanel extends JPanel {
  private CardLayout cardLayout;

  public MainPanel() {
    cardLayout = new CardLayout();
    setLayout(cardLayout);

    add(new TestFrame(), "TEST FRAME");
    add(new BangQuyDoiPanel(), "QUY_DOI");
    // Todo: Add Screens
  }

  public void showCard(String cardName) {
    cardLayout.show(this, cardName);
  }
}