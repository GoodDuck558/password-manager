package com.passwordmanager.crypto;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.*;
import java.util.Arrays;

public class CryptoManager {
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int PBKDF2_ITER = 310000;
    private static final int KEY_LENGTH = 256;

    public static SecretKey deriveKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = null;
        try {
            // 1. Create PBEKeySpec
            spec = new PBEKeySpec(password, salt, PBKDF2_ITER, KEY_LENGTH);

            // 2. Create SecretKeyFactory
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // 3. Generate raw key bytes
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();

            // 4. Return AES key
            return new SecretKeySpec(keyBytes, "AES");
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } finally {
            if (spec != null) {
                spec.clearPassword();
            }
        }
    }

    public static byte[] generateRandom(int length) {
        byte[] bytes = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] encrypt(byte[] plaintext, char[] password) {
        try {
            byte[] salt = generateRandom(SALT_LENGTH);
            SecretKey key = deriveKey(password, salt);

            byte[] iv = generateRandom(IV_LENGTH);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

            byte[] ciphertext = cipher.doFinal(plaintext);

            byte[] result = new byte[salt.length + iv.length + ciphertext.length];
            System.arraycopy(salt, 0, result, 0, salt.length);
            System.arraycopy(iv, 0, result, salt.length, iv.length);
            System.arraycopy(ciphertext, 0, result, salt.length + iv.length, ciphertext.length);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    public static byte[] decrypt(byte[] encryptedData, char[] password) {
        try {
        byte [] salt = Arrays.copyOfRange(encryptedData, 0, SALT_LENGTH);
        byte [] iv = Arrays.copyOfRange(encryptedData, SALT_LENGTH, SALT_LENGTH + IV_LENGTH);
        byte [] ciphertext = Arrays.copyOfRange(encryptedData, SALT_LENGTH + IV_LENGTH, encryptedData.length);

        SecretKey key = deriveKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        return cipher.doFinal(ciphertext); }
        catch (Exception e) {
                throw new RuntimeException("Decryption failed", e);
            }

    }
}