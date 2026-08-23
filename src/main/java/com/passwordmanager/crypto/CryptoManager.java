package com.passwordmanager.crypto;

import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;
import java.util.Arrays;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class CryptoManager {
    private static final int ARGON2_MEMORY_KIB = 524288;
    private static final int ARGON2_ITERATIONS = 2;
    private static final int ARGON2_PARALLELISM = 4;
    private static final int KEY_LENGTH = 32;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;

    public static SecretKey deriveKey(char[] password, byte[] salt) {
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(ARGON2_ITERATIONS)
                .withMemoryAsKB(ARGON2_MEMORY_KIB)
                .withParallelism(ARGON2_PARALLELISM)
                .withSalt(salt)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] keyBytes = new byte[KEY_LENGTH];
        generator.generateBytes(password, keyBytes);

        return new SecretKeySpec(keyBytes, "AES");
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