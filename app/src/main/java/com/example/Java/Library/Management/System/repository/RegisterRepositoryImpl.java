package com.example.Java.Library.Management.System.repository;

import com.example.Java.Library.Management.System.model.UserModel;
import com.example.Java.Library.Management.System.services.SQliteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

abstract class RegisterRepository {
    public abstract boolean registerUser(String username, String password, String fullnanme);

    public abstract boolean addUser(String username, String password, String fullname, String role);

    public abstract boolean isUserExists(String username);

    public abstract boolean isFullnameExists(String fullname);

    public abstract List<UserModel> getUsers();

    public abstract boolean editUser(String id, String username, String fullname, String password, String role);

    public abstract boolean deleteUserById(String userId);
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
            ps.setString(4, "user");
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted: " + username);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    @Override
    public boolean addUser(String username, String password, String fullname, String role) {

        String sql = "INSERT INTO user (username, password, fullname, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password); // store hashed password
            ps.setString(3, fullname);
            ps.setString(4, role);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted: " + username);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    @Override
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

    @Override
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

    @Override
    public List<UserModel> getUsers() {
        List<UserModel> users = new ArrayList<>();
        String sql = "SELECT id, fullname, username, role FROM user";
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String fullname = rs.getString("fullname");
                String username = rs.getString("username");
                String role = rs.getString("role"); // may be null

                // Debug output to verify what comes from DB
                System.out.println("getUsers row -> id=" + id + " username=" + username + " fullname=" + fullname + " role=" + role);

                if (role == null) {
                    role = ""; // or a default like "user"
                }

                // IMPORTANT: ensure this matches your UserModel constructor parameter order.
                // If UserModel expects (id, username, fullname, role) swap fullname and username below.
                UserModel user = new UserModel(id, fullname, username, role);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve users", e);
        }
        return users;
    }

    @Override
    public boolean editUser(String id, String username, String fullname, String password, String role) {
        // Build SQL depending on whether password should be updated
        String sql;
        boolean updatePassword = !password.isEmpty();
        if (updatePassword) {
            sql = "UPDATE user SET username = ?, fullname = ?, password = ? WHERE id = ?";
        } else {
            sql = "UPDATE user SET username = ?, fullname = ? WHERE id = ?";
        }

        try (java.sql.Connection conn = SQliteConnection.connect();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            int userId = Integer.parseInt(id);
            ps.setString(1, username);
            ps.setString(2, fullname);
            if (updatePassword) {
                ps.setString(3, password);
                ps.setString(4, role);
                ps.setInt(5, userId);
            } else {
                ps.setString(3, role);
                ps.setInt(4, userId);
            }

            int rows = ps.executeUpdate();
            if (rows > 0) {
                javax.swing.JOptionPane.showMessageDialog(null, "User updated successfully.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                 return true;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "No changes were made or user not found.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Failed to update user: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    // delete the user
    public boolean deleteUserById(String userId) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = SQliteConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}