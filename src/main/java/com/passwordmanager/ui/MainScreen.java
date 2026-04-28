package com.passwordmanager.ui;

import com.passwordmanager.ui.AppState;
import com.passwordmanager.util.ClipboardManager;
import com.passwordmanager.vault.PasswordEntry;

import javax.swing.*;
import java.awt.*;

public class MainScreen {

    private final JPanel panel = new JPanel();

    public MainScreen(JFrame frame) {

        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Your Vault", SwingConstants.CENTER);

        DefaultListModel<PasswordEntry> model = new DefaultListModel<>();

        for (PasswordEntry entry : AppState.vault.getEntries()) {
            model.addElement(entry);
        }

        JList<PasswordEntry> list = new JList<>(model);

        JButton add = new JButton("Add Entry");
        JButton copy = new JButton("Copy Password");

        add.addActionListener(e -> {
            AddEntryScreen addScreen = new AddEntryScreen(frame, model);
            frame.setContentPane(addScreen.getPanel());
            frame.revalidate();
            frame.repaint();
        });

        copy.addActionListener(e -> {
            PasswordEntry selected = list.getSelectedValue();
            if (selected != null) {
                ClipboardManager.copyToClipboard(selected.getPassword());
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(add);
        bottom.add(copy);

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}