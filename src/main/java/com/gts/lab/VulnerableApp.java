package com.gts.lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class VulnerableApp {

    // SQL Injection
    public ResultSet findUser(String username) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM users WHERE name = '" + username + "'";
        return stmt.executeQuery(query);
    }

    // Command Injection
    public void runReport(String reportName) throws Exception {
        Runtime.getRuntime().exec("sh /opt/reports/generate.sh " + reportName);
    }

    // Path Manipulation
    public String readFile(String filename) throws Exception {
        File f = new File("/var/data/" + filename);
        return f.getCanonicalPath();
    }

    // Hardcoded credentials
    private static final String DB_PASSWORD = "Admin@123456";

    // Insecure Randomness
    public String generateSessionToken() {
        Random rand = new Random();
        return String.valueOf(rand.nextInt());
    }

    // Weak Cryptographic Hash (MD5)
    public byte[] hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(password.getBytes());
    }

    // Weak Encryption (DES with hardcoded key)
    public byte[] encryptData(String data) throws Exception {
        byte[] key = "12345678".getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data.getBytes());
    }

    // Unreleased Resource - Stream
    public int readFirstByte(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        return fis.read();
    }

    // Log Forging
    public void logAccess(String user) {
        System.out.println("User logged in: " + user);
    }

    // Second SQL Injection variant
    public ResultSet searchByRole(String role, String dept) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(
            "SELECT * FROM users WHERE role = '" + role + "' AND dept = '" + dept + "'");
    }

    public static void main(String[] args) throws Exception {
        VulnerableApp app = new VulnerableApp();
        if (args.length > 0) {
            app.findUser(args[0]);
            app.logAccess(args[0]);
        }
        System.out.println("Lab app started");
    }
}
