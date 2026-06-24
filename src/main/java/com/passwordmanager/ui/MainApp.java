package com.passwordmanager.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Password Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 650);
            frame.setResizable(false);

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    if (AppState.masterPassword != null) {
                        for (int i = 0; i < AppState.masterPassword.length; i++) {
                            AppState.masterPassword[i] = '\0';
                        }
                    }
                }
            });

            MasterPasswordScreen screen = new MasterPasswordScreen(frame);
            frame.setContentPane(screen.getPanel());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
