package com.passwordmanager.ui;

import com.passwordmanager.ui.AppState;
import com.passwordmanager.vault.PasswordEntry;
import com.passwordmanager.util.PasswordGenerator;

import javax.swing.*;
import java.awt.*;

public class AddEntryScreen {

    private final JPanel panel = new JPanel();

    public AddEntryScreen(JFrame frame, DefaultListModel<PasswordEntry> model) {

        panel.setLayout(new GridLayout(6, 1));

        JTextField site = new JTextField();
        site.setBorder(BorderFactory.createTitledBorder("Website"));

        JTextField username = new JTextField();
        username.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        char defaultEcho = password.getEchoChar();

        boolean[] passwordVisible = {false};

        JButton toggleVisibility = new JButton("Show Password");

        toggleVisibility.addActionListener(e -> {

            if (passwordVisible[0]) {
                password.setEchoChar(defaultEcho);
                toggleVisibility.setText("Show Password");
            } else {
                password.setEchoChar((char) 0);
                toggleVisibility.setText("Hide Password");
            }

            passwordVisible[0] = !passwordVisible[0];
        });
        JButton save = new JButton("Save");

        save.addActionListener(e -> {

            if (site.getText().trim().isEmpty() || username.getText().trim().isEmpty() || password.getPassword().length == 0) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }
            PasswordEntry entry = new PasswordEntry(
                    site.getText(),
                    username.getText(),
                    new String(password.getPassword())

            );

            AppState.vault.addEntry(entry);

            try {
                AppState.vault.save("vault.dat", AppState.masterPassword);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            model.addElement(entry);

            MainScreen main = new MainScreen(frame);
            frame.setContentPane(main.getPanel());
            frame.revalidate();
            frame.repaint();
        });

        JButton generateBtn = new JButton("Generate Password");
        generateBtn.addActionListener(e -> {
            String generated = PasswordGenerator.generatePassword(16);
            password.setText(generated);
        });

        panel.add(site);
        panel.add(username);
        panel.add(password);
        panel.add(toggleVisibility);
        panel.add(save);
        panel.add(generateBtn);
    }

    public JPanel getPanel() {
        return panel;
    }
}