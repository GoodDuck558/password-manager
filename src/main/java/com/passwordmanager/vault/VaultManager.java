package com.passwordmanager.vault;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.passwordmanager.crypto.CryptoManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class VaultManager {
    private List<PasswordEntry> entries;

    public VaultManager(List<PasswordEntry> entries) {
        this.entries = new ArrayList<>(entries); // defensive copy
    }

    public VaultManager() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(PasswordEntry entry) {
        entries.add(entry);
    }
    public boolean removeEntry(String site, String username) {
        return entries.removeIf(e ->
                e.getSite().equalsIgnoreCase(site) &&
                        e.getUsername().equalsIgnoreCase(username));
        }

    public List<PasswordEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    public void save(String filePath, char[] password) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 1. Serialize entries → JSON
            String json = mapper.writeValueAsString(entries);

            // 2. Convert to bytes
            byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

            // 3. Encrypt
            byte[] encrypted = CryptoManager.encrypt(jsonBytes, password);

            // 4. Write to file
            Files.write(Paths.get(filePath), encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save vault", e);
        }
    }

    public void load(String filePath, char[] password) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 1. Read encrypted bytes
            byte[] encrypted = Files.readAllBytes(Paths.get(filePath));

            // 2. Decrypt
            byte[] decrypted = CryptoManager.decrypt(encrypted, password);

            // 3. Convert to JSON string
            String json = new String(decrypted, StandardCharsets.UTF_8);

            // 4. Deserialize into List<PasswordEntry>
            List<PasswordEntry> loadedEntries =
                    mapper.readValue(json, new TypeReference<List<PasswordEntry>>() {});

            // 5. Replace current entries (defensive copy)
            this.entries = new ArrayList<>(loadedEntries);


        } catch (Exception e) {
            throw new RuntimeException("Failed to load vault", e);
        }
    }

}