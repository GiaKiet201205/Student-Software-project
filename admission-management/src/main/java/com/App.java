package com;

import com.exception.GlobalExceptionHandler;
import com.ui.MainUI;
import javax.swing.SwingUtilities;
import com.ui.common.BaseFrame;
import com.ui.panel.MainPanel;
import com.ui.panel.NavBarPanel;
import javax.swing.*;
import java.awt.*;
public class App
{
    /*public static void main( String[] args )
    {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());

        SwingUtilities.invokeLater(() -> {
            MainUI mainUI = new MainUI();
            mainUI.setVisible(true);
        });
    }*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Khởi tạo Frame chính (Kế thừa từ BaseFrame của bạn)
            BaseFrame frame = new BaseFrame();
            frame.setLayout(new BorderLayout());

            // 2. Khởi tạo các thành phần
            MainPanel mainPanel = new MainPanel();
            NavBarPanel navBar = new NavBarPanel(mainPanel);

            // 3. Ráp giao diện
            frame.add(navBar, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);

            // 4. Hiển thị
            frame.setVisible(true);
        });
    }
}
