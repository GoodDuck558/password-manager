# Password Manager
## About
This is an app created on Java that works as a Password Manager, it uses AES256-GCM and PBKDF2 310000 iterations. Passwords inside this app is securely encrypted.

## Features
-Master password required on index screen
-Main menu with passwords, entry screen, delete option, save option
-Copy password to clipboard (you have only 30 seconds before it gets deleted from clipboard)
-Password generator

## Security
-AES-256-GCM authenticated encryption
-PBKDF2-HMAC-SHA256 key derivation, 310,000 iterations
-Vault stored locally, never transmitted
-Clipboard auto-clears after 30 seconds

## Requirements
-Java 21
-Maven 3.x.x

## How to Build
git clone https://github.com/GoodDuck558/password-manager.git
cd password-manager
mvn package

## How to Run
java -jar target/password-manager-1.0-SNAPSHOT.jar
