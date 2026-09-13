package com.gts.lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;

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

    public static void main(String[] args) throws Exception {
        VulnerableApp app = new VulnerableApp();
        if (args.length > 0) {
            app.findUser(args[0]);
        }
        System.out.println("Lab app started");
    }
}
