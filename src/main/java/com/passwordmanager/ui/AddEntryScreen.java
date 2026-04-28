package com.passwordmanager.ui;

import com.passwordmanager.ui.AppState;
import com.passwordmanager.vault.PasswordEntry;

import javax.swing.*;
import java.awt.*;

public class AddEntryScreen {

    private final JPanel panel = new JPanel();

    public AddEntryScreen(JFrame frame, DefaultListModel<PasswordEntry> model) {

        panel.setLayout(new GridLayout(4, 1));

        JTextField site = new JTextField();
        site.setBorder(BorderFactory.createTitledBorder("Website"));

        JTextField username = new JTextField();
        username.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton save = new JButton("Save");

        save.addActionListener(e -> {

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

        panel.add(site);
        panel.add(username);
        panel.add(password);
        panel.add(save);
    }

    public JPanel getPanel() {
        return panel;
    }
}