package com;

import com.ui.frame.TestFrame;

public class App 
{
    public static void main( String[] args )
    {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TestFrame().setVisible(true);
        });
    }
}
