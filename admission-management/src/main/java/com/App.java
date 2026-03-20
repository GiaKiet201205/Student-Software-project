package com;

import com.exception.GlobalExceptionHandler;
import com.ui.MainUI;
import javax.swing.SwingUtilities;

public class App
{
    public static void main( String[] args )
    {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());

        SwingUtilities.invokeLater(() -> {
            MainUI mainUI = new MainUI();
            mainUI.setVisible(true);
        });
    }
}
