package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.services.SQliteConnection;

import java.sql.*;

abstract class RegisterRepository {
    public abstract boolean registerUser(String username, String password, String fullnanme);
}


public class RegisterRepositoryImpl extends RegisterRepository {
    @Override
    public boolean registerUser(String username, String password, String fullname) {

        String sql = "INSERT INTO user (username, password, fullname, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password); // store hashed password
            ps.setString(3, fullname);
            ps.setString(4, "admin");
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted: " + username);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    public boolean isUserExists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check user existence", e);
        }
        return false;
    }

    public boolean isFullnameExists(String fullname) {
        String sql = "SELECT COUNT(*) FROM user WHERE fullname = ?";
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullname);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check fullname existence", e);
        }
        return false;
    }
}