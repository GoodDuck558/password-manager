package com.passwordmanager.util;

import java.io.File;

public class AppConfig {

    public static String getVaultPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String path;
        File basePath;

        if (os.contains("win")) {
            // Windows: Use APPDATA when available
            String appData = System.getenv("APPDATA");

            if (appData != null && !appData.isEmpty()) {
                basePath = new File(appData);
            } else {
                // Fallback if APPDATA is not set
                basePath = new File(System.getProperty("user.home"));
            }

        } else {
            // Linux/Unix: Check XDG_DATA_HOME first
            String xdgDataHome = System.getenv("XDG_DATA_HOME");

            if (xdgDataHome != null && !xdgDataHome.isEmpty()) {
                basePath = new File(xdgDataHome);
            } else {
                // Fallback: ~/.local/share
                basePath = new File(
                        new File(System.getProperty("user.home"), ".local"),
                        "share"
                );
            }
        }

        // Create PasswordManager under the selected base directory
        File passwordManager = new File(basePath, "PasswordManager");

        if (!passwordManager.exists()) {
            passwordManager.mkdirs();
        }

        // Build the vault path using a platform-independent separator
        path = new File(passwordManager, "vault.dat").getAbsolutePath();

        return path;
    }
}