package com.ui;

import com.ui.common.BaseFrame;
import com.ui.panel.MainPanel;
import com.ui.panel.NavBarPanel;

import java.awt.*;

public class MainUI extends BaseFrame {
  private NavBarPanel navBar;
  private MainPanel mainPanel;

  public MainUI() {
    super();
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    mainPanel = new MainPanel();
    navBar = new NavBarPanel(mainPanel);

    add(navBar, BorderLayout.WEST);
    add(mainPanel, BorderLayout.CENTER);
  }
}