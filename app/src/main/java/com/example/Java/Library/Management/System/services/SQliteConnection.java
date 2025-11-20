/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Java.Library.Management.System.services;

/**
 *
 * @author wendevlife
 */

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQliteConnection {

    private static SQliteConnection instance;
    private static volatile boolean schemaInitialized = false;

    // Singleton pattern to ensure single instance
    public static SQliteConnection getInstance() {
        if (instance == null) {
            instance = new SQliteConnection();
        }
        return instance;
    }

    // Connect to SQLite
    public static Connection connect() {
        String url = resolveDatabaseUrl();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            initializeSchema(conn);
            System.out.println("✅ Connected to SQLite database! (" + url + ")");
        } catch (SQLException e) {
            System.out.println("❌ SQLite connection failed: " + e.getMessage());
        }
        return conn;
    }

    // Close connection safely
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("🔌 SQLite connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to close connection: " + e.getMessage());
        }
    }

    private static void initializeSchema(Connection conn) throws SQLException {
        if (conn == null || schemaInitialized) {
            return;
        }
        synchronized (SQliteConnection.class) {
            if (schemaInitialized) {
                return;
            }
            try (Statement stmt = conn.createStatement()) {
                String userTableSql = "CREATE TABLE IF NOT EXISTS user (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL, " +
                        "fullname TEXT NOT NULL, " +
                        "role TEXT NOT NULL DEFAULT 'admin', " +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                stmt.executeUpdate(userTableSql);
            }
            schemaInitialized = true;
        }
    }


    private static String resolveDatabaseUrl() {
        // 1) Try classpath resource (e.g. src/main/resources/library.db)
        try {
            URL res = SQliteConnection.class.getClassLoader().getResource("library.db");
            if (res != null) {
                Path p = Paths.get(res.toURI()).toAbsolutePath();
                // If the resource is inside a jar, this may not be a real file. Fall back below in that case.
                if (Files.exists(p)) {
                    return "jdbc:sqlite:" + p.toString();
                }
            }
        } catch (Exception ignored) {
        }

        // 2) Check common project locations relative to working directory
        String userDir = System.getProperty("user.dir");
        Path[] candidates = new Path[] {
                Paths.get(userDir, "app", "library.db"),
                Paths.get(userDir, "library.db"),
                Paths.get(userDir, "src", "main", "resources", "library.db")
        };
        for (Path p : candidates) {
            if (Files.exists(p)) {
                return "jdbc:sqlite:" + p.toAbsolutePath().toString();
            }
        }

        // 3) Default to creating/using app/library.db in working directory
        Path defaultPath = Paths.get(userDir, "app", "library.db");
        return "jdbc:sqlite:" + defaultPath.toAbsolutePath().toString();
    }
}
