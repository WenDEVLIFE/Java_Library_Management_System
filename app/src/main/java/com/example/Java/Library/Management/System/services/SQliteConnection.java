/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Java.Library.Management.System.services;

/**
 *
 * @author wendevlife
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQliteConnection {

    private static final String DB_URL = "jdbc:sqlite:library.db";

    // Connect to SQLite
    public static Connection connect() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("✅ Connected to SQLite database!");
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
}
