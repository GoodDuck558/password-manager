package com.passwordmanager.util;

import java.io.File;

public class AppConfig {
    public static String getVaultPath() {
        String appData = System.getenv("APPDATA");
        File passwordManager = new File(appData, "PasswordManager");
        if (!passwordManager.exists()) {
            passwordManager.mkdirs();
        }
        String path = new File(passwordManager, "vault.dat").getAbsolutePath();


        return path;

    }}