# Password Manager v2.0.1

## About
A local password manager built in Java, using AES-256-GCM for encryption and Argon2id for key derivation. Vault is stored locally and never transmitted.

## Features
- Master password required on unlock screen
- Main vault view: list entries, add, delete, save
- Copy password to clipboard (auto-clears after 30 seconds)
- Password generator with adjustable length (12–128 characters)
- Custom entropy-based password strength evaluator
- More to come

## Security
- AES-256-GCM authenticated encryption
- Argon2id key derivation (memory=512 MiB, iterations=2, parallelism=4, salt=16 bytes, output=32 bytes) via Bouncy Castle
- Vault stored locally at `%APPDATA%\PasswordManager\vault.dat`, never transmitted
- Master password held in memory as `char[]` (not `String`) and zeroed on application close
- Clipboard auto-clears after 30 seconds (best-effort — not a guaranteed security boundary; see note below)
- Vault binary format: `[salt][IV][ciphertext+tag]`

**Note:** Clipboard clearing removes the current clipboard contents after 30 seconds but cannot guarantee that clipboard-history tools or other processes haven't already retained the copied password. Treat it as a convenience, not a hard security guarantee.

## Requirements
- Java 21
- Maven 3.x.x

## How to Build
git clone https://github.com/GoodDuck558/password-manager.git
cd password-manager
mvn package


## How to Run

java -jar target/password-manager-2.0.1.jar


## Roadmap
- Add Linux support (in progress)
- Auto-lock on inactivity (in progress)
- Migrate stored password fields from `String` to `char[]`
- v3: JavaFX UI rewrite
- More to come
