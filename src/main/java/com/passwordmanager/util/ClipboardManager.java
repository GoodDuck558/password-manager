package com.passwordmanager.util;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClipboardManager {

    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public static void copyToClipboard(String text) {

        // 1. Copy text to clipboard
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);

        // 2. Schedule auto-clear after 30 seconds
        scheduler.schedule(() -> {
            StringSelection empty = new StringSelection("");
            clipboard.setContents(empty, null);
        }, 30, TimeUnit.SECONDS);
    }
}