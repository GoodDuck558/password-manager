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

            MasterPasswordScreen screen = new MasterPasswordScreen(frame);
            frame.setContentPane(screen.getPanel());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}