package com.passwordmanager.util;

public class PasswordStrengthEvaluator {

    public static int evaluate(char[] password) {
        if (password == null) return 0;

        int n = password.length;
        if (n < 12) return 0;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (char c : password) {
            if (c >= 'A' && c <= 'Z') hasUpper = true;      // keep your char logic consistent
            else if (c >= 'a' && c <= 'z') hasLower = true;
            else if (c >= '0' && c <= '9') hasDigit = true;
            else hasSymbol = true;
        }

        int charset = 0;
        if (hasUpper) charset += 26;
        if (hasLower) charset += 26;
        if (hasDigit) charset += 10;
        if (hasSymbol) charset += 33;

        double entropyBits = n * log2(charset == 0 ? 1 : charset);

        if (entropyBits < 28) return 0; // ~< 2^28
        if (entropyBits < 36) return 1; // ~< 2^36
        if (entropyBits < 60) return 2; // ~< 2^60
        if (entropyBits < 80) return 3; // ~< 2^80
        return 4;
    }

    public static String labelForScore(int score) {
        return switch (score) {
            case 0 -> "✗ Strength: Very Weak";
            case 1 -> "✗ Strength: Weak";
            case 2 -> "○ Strength: Fair";
            case 3 -> "✓ Strength: Good";
            case 4 -> "✓ Strength: Excellent";
            default -> "";
        };
    }

    private static double log2(int x) {
        return Math.log(x) / Math.log(2);
    }
}
