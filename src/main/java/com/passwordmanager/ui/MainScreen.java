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
        JButton delete = new JButton("Delete Entry");

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

        delete.addActionListener(e -> {
            PasswordEntry selected = list.getSelectedValue();

            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Delete selected entry?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    AppState.vault.removeEntry(selected.getSite(), selected.getUsername());
                    // remove from vault
                    model.removeElement(selected);

                    try {
                        AppState.vault.save("vault.dat", AppState.masterPassword);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Failed to save vault after deletion.");
                    }// remove from UI
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No entry selected.");
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(add);
        bottom.add(copy);
        bottom.add(delete);

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}