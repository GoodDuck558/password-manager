package com.passwordmanager.ui;

import com.passwordmanager.ui.AppState;
import com.passwordmanager.util.AppConfig;
import com.passwordmanager.vault.VaultManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MasterPasswordScreen {

    private final JPanel panel = new JPanel();

    public MasterPasswordScreen(JFrame frame) {

        panel.setLayout(new GridLayout(3, 1));

        JLabel label = new JLabel("Enter Master Password");
        JPasswordField field = new JPasswordField();
        JButton login = new JButton("Unlock");

        String vaultPath = AppConfig.getVaultPath();

        login.addActionListener(e -> {
            try {
                char[] password = field.getPassword();

                VaultManager vault = new VaultManager();

                File file = new File(vaultPath);

                if (file.exists()) {
                    vault.load(vaultPath, password);
                } else {
                    // first run → create empty vault
                    vault = new VaultManager();
                    vault.save(vaultPath, password);
                }

                AppState.vault = vault;
                AppState.masterPassword = password;

                MainScreen main = new MainScreen(frame);
                frame.setContentPane(main.getPanel());
                frame.revalidate();
                frame.repaint();

            } catch (Exception ex) {
                label.setText("Wrong password or corrupted vault");
            }
        });

        panel.add(label);
        panel.add(field);
        panel.add(login);
    }

    public JPanel getPanel() {
        return panel;
    }
}