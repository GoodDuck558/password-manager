package com.passwordmanager.util;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClipboardManager {

    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private static ScheduledFuture<?> pendingClear = null;

    public static void copyToClipboard(String text) {

        if (pendingClear != null && !pendingClear.isDone()) {
            pendingClear.cancel(false);
        }

        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);

        pendingClear = scheduler.schedule(() -> {
            StringSelection empty = new StringSelection("");
            clipboard.setContents(empty, null);
        }, 30, TimeUnit.SECONDS);
    }
}